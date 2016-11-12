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

import com.fasterxml.jackson.databind.*
import com.github.salomonbrys.kodein.*
import com.google.common.cache.*
import com.google.common.io.*
import java.io.*
import java.net.*
import java.nio.charset.*
import java.util.*
import java.util.concurrent.*
import javax.validation.*

fun getFinalPackageName(clz : Class<Any> ) : String {
    val words = clz.`package`.name.split(".")
    return words[words.size - 1 ]
}

/**
 * attempts to find things similar to a ResourceBundle
 *
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
 */
object LocaleAwareResourceFinder {
    private fun locale_names(name: String, locale: Locale, ext: String) : List<String> {
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
        val suffixes_sanitized = locale_suffixes.filter { !it.endsWith("_")}
        // for all those left, append name as prefix, ext as suffix
        return suffixes_sanitized.map { String.format("%s%s%s", name, it, ext)}
    }

    /**
     * our resources are going to be YaML
     */
    fun <T> find(name: String, locale: Locale, clazz: Class<T>, ext: String = ".yml") : URL {
        val fnames_precendence_order = locale_names(name, locale, ext)
        val urls = fnames_precendence_order.map { it -> clazz.getResource(it) }
        val foundList = urls.filterNotNull()
        if (foundList.isEmpty()) {
            throw IllegalArgumentException("Unable to find any resources matching $name. Tried: $fnames_precendence_order ")
        }
        return foundList.first()
    }
}

/**
 * possibly premature optimization
 *    - i'm going to assume loading file resource, and deserializing into a DTO
 *      is a not-insignificant performance hit. This class caches the N most-recently accessed DTOs.
 *    - it's bound in Kodein as a singleton, so the cache is shared by many different generators
 */
class CachingResourceDeserializer(override val kodein: Kodein) : KodeinAware
{
    val objectReader : ObjectReader = instance()
    val validator : Validator = instance()
    val maxSize = 100L

    val cache : Cache<Triple<String, String, Locale>, Any> = CacheBuilder.newBuilder()
            .maximumSize(maxSize)
            .expireAfterAccess(5, TimeUnit.MINUTES)
            .build()

    @Suppress("UNCHECKED_CAST")
    fun <T> deserialize(clazz: Class<T>,
                        resource_prefix : String,
                        locale: Locale,
                        charset: Charset = StandardCharsets.UTF_8): T {
        val key = Triple(clazz.`package`.name, resource_prefix, locale)
        synchronized(cache) {
            val result = cache.get(key, object : Callable<T> {
                override fun call(): T {
                    return uncached_deserialize(clazz, resource_prefix, locale, charset)
                }
            })
            return result as T
        }
    }

    private fun <T> validate(obj: T) : T {
        val constraintViolations = validator.validate(obj)
        if (constraintViolations.size > 0)
            throw ConstraintViolationException(constraintViolations)
        return obj
    }

    private fun <T> uncached_deserialize(clazz: Class<T>,
                                         resource_prefix : String,
                                         locale: Locale,
                                         charset: Charset): T {
        val url = LocaleAwareResourceFinder.find(resource_prefix,locale,clazz)
        val str = Resources.toString(url, charset)
        try {
            val result: T = objectReader.forType(clazz).readValue(str)

            return validate(result)
        } catch (ex: Exception) {
            throw IOException("problem reading file ${url} (locale: ${locale}) - ${ex.toString()}", ex)
        }
    }
}
