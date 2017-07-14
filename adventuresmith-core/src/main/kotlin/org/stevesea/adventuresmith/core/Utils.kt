/*
 * Copyright (c) 2016 Steve Christensen
 *
 * This file is part of Adventuresmith.
 *
 * Adventuresmith is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Adventuresmith is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Adventuresmith.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package org.stevesea.adventuresmith.core

import com.fasterxml.jackson.databind.ObjectReader
import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.KodeinAware
import com.github.salomonbrys.kodein.instance
import com.google.common.base.Optional
import com.google.common.base.Throwables
import com.google.common.cache.Cache
import com.google.common.cache.CacheBuilder
import com.google.common.io.Resources
import com.google.common.util.concurrent.UncheckedExecutionException
import mu.KLoggable
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException
import java.net.URL
import java.nio.charset.Charset
import java.util.LinkedHashMap
import java.util.Locale
import java.util.concurrent.Callable
import java.util.concurrent.ExecutionException
import java.util.concurrent.TimeUnit

fun getFinalPackageName(clz : Class<Any> ) : String {
    val words = clz.`package`.name.split(".")
    return words[words.size - 1 ]
}

fun String.titleCase() : String {
    val DELIMITERS = setOf(' ', '\'', '-', '(', ')')

    val sb = StringBuilder()
    var capNext = true

    for (inc in this.toCharArray()) {
        val c = if (capNext)
            Character.toUpperCase(inc)
        else
            Character.toLowerCase(inc)
        sb.append(c)
        capNext = DELIMITERS.contains(c)
    }
    return sb.toString()
}

fun <K, V> Map<K, V>.mergeCombine(other: Map<K, V>, combine: (K, V, V) -> V = { k, a, b -> b }): Map<K, V> {
    val result = LinkedHashMap<K, V>(this.size + other.size)
    result.putAll(this)
    other.forEach { e ->
        val existing = result[e.key]

        if (existing == null) {
            result[e.key] = e.value
        }
        else {
            result[e.key] = combine(e.key, e.value, existing)
        }
    }
    return result
}

fun <K, V> MutableMap<K, V>.mergeCombineInPlace(other: Map<K, V>, combine: (K, V, V) -> V = { k, a, b -> b }): Map<K, V> {
    other.forEach { e ->
        val existing = this[e.key]

        if (existing == null) {
            this[e.key] = e.value
        }
        else {
            this[e.key] = combine(e.key, e.value, existing)
        }
    }
    return this
}

/**
 * attempts to find things similar to a ResourceBundle
 *
 * <pre>
 * If a ResourceBundle class for the specified Locale does not exist, getBundle tries to
 * find the closest match. For example, if ButtonLabel_fr_CA_UNIX is the desired class and
 * the default Locale is en_US, getBundle will look for classes in the following order:
 *
 *
 *   ButtonLabel_fr_CA_UNIX
 *   ButtonLabel_fr_CA
 *   ButtonLabel_fr
 *   ButtonLabel_en_US
 *   ButtonLabel_en
 *   ButtonLabel
 * </pre>
 */

abstract class AbstractLocaleAwareFinder {
    protected fun locale_names(name: String, locale: Locale, ext: String) : List<String> {
        // look for things similar to a resource bundle:

        val defaultLocale = Locale.getDefault()
        // constructs a list like
        //    _fr_CA
        //    _fr
        //    _en_US
        //    _en
        //    <emptystring>
        val locale_suffixes = linkedSetOf(
                String.format(".%s_%s_%s", locale.language, locale.country, locale.variant),
                String.format(".%s_%s", locale.language, locale.country),
                String.format(".%s", locale.language),
                String.format(".%s_%s_%s", defaultLocale.language, defaultLocale.country, defaultLocale.variant),
                String.format(".%s_%s", defaultLocale.language, defaultLocale.country),
                String.format(".%s", defaultLocale.language),
                ""
        )
        // if any of the locales didn't have country/variant, the list will have things like
        //   _en__ or _en_ , remove them
        val suffixes_sanitized = locale_suffixes.filter { !it.endsWith("_") }
        // for all those left, append name as prefix, ext as suffix
        return suffixes_sanitized.map { String.format("%s%s%s", name, it, ext) }
    }
}

object LocaleAwareFinderForClasspathResources : AbstractLocaleAwareFinder(), KLoggable {
    override val logger = logger()

    val maxSize = 100L

    val cache : Cache<Pair<String, String>, Optional<URL>> = CacheBuilder.newBuilder()
            .maximumSize(maxSize)
            .expireAfterAccess(10, TimeUnit.MINUTES)
            .build()

    fun <T> getPossiblyCachedResourceURL(clazz: Class<T>, path: String) : URL? {
        val key = Pair(clazz.`package`.name, path)
        try {
            return cache.get(key) { Optional.fromNullable(clazz.getResource(path)) }.orNull()
        } catch (e: ExecutionException) {
            Throwables.throwIfInstanceOf(e.cause, IOException::class.java)
            throw e.cause!!
        } catch (e: UncheckedExecutionException) {
            throw e.cause!!
        }
    }

    fun <T> find(name: String, locale: Locale, clazz: Class<T>, ext: String = ".yml") : URL {
        val fnames_precedence_order = locale_names(name, locale, ext)
        val urls = fnames_precedence_order.map { it -> getPossiblyCachedResourceURL(clazz, it) }
        val foundList = urls.filterNotNull()
        if (foundList.isEmpty()) {
            throw FileNotFoundException("Unable to find any resources matching $name. Looked in: ${clazz.`package`.name} Tried: $fnames_precedence_order ")
        }
        /**
         * TODO: instead of picking best, seems like we could gather dtos from multiple languages here and combine
         */
        return foundList.first()
    }
}

object LocaleAwareFinderForFiles : AbstractLocaleAwareFinder(), KLoggable {
    override val logger = logger()

    fun findBestFile(f: File, locale: Locale, ext: String = ".yml") : File {
        val fnorm = File(f.parentFile, f.nameWithoutExtension).absolutePath
        val fnames_precedence_order = locale_names(fnorm, locale, ext)
        val files = fnames_precedence_order.filter { File(it).exists() }.map { File(it) }
        if (files.isEmpty()) {
            throw FileNotFoundException("Unable to find files matching '$fnorm'. Tried: $fnames_precedence_order")
        }
        /**
         * TODO: instead of picking best, seems like we could gather dtos from multiple languages here and combine
         */
        return files.first()
    }
}

/**
 * possibly premature optimization
 *    - i'm going to assume loading file resource, and deserializing into a DTO is a not-insignificant
 *      performance hit. This class caches the N most-recently accessed DTOs.
 *    - it's bound in Kodein as a singleton, so the cache is shared by many different generators
 */
class CachingResourceDeserializer(override val kodein: Kodein) : KodeinAware
{
    val objectReader : ObjectReader = instance()
    val maxSize = 75L

    val cache : Cache<Triple<String, String, Locale>, Any> = CacheBuilder.newBuilder()
            .maximumSize(maxSize)
            .expireAfterAccess(5, TimeUnit.MINUTES)
            .build()

    @Suppress("UNCHECKED_CAST")
    fun <T> deserialize(clazz: Class<T>,
                        resource_prefix : String,
                        locale: Locale,
                        charset: Charset = Charsets.UTF_8): T {
        val key = Triple(clazz.`package`.name, resource_prefix, locale)
        try {
            val result = cache.get(key, object : Callable<T> {
                override fun call(): T {
                    return uncached_deserialize(clazz, resource_prefix, locale, charset)
                }
            })
            return result as T
        } catch (e: ExecutionException) {
            Throwables.throwIfInstanceOf(e.cause, IOException::class.java)
            throw e.cause!!
        } catch (e: UncheckedExecutionException) {
            throw e.cause!!
        }
    }

    private fun <T> uncached_deserialize(clazz: Class<T>,
                                         resource_prefix : String,
                                         locale: Locale,
                                         charset: Charset): T {
        val url = LocaleAwareFinderForClasspathResources.find(resource_prefix, locale, clazz)
        val str = Resources.toString(url, charset)
        try {
            val result: T = objectReader.forType(clazz).readValue(str)
            return result
        } catch (ex: Exception) {
            throw IOException("problem reading file $url - ${ex.message}", ex)
        }
    }
}

