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
import com.fasterxml.jackson.dataformat.yaml.*
import com.fasterxml.jackson.module.kotlin.*
import com.google.common.cache.*
import com.google.common.io.*
import java.net.*
import java.nio.charset.*
import java.util.*
import java.util.concurrent.*

// replaces elements of String with entries from the given map
// any keys from map in source string which match %{key} will be substituted with val
fun inefficientStrSubstitutor(inputStr: String, replacements: Map<String,String>) : String {
    var result = inputStr
    for (e in replacements.entries) {
        result = result.replace("%{${e.key}}", e.value )
    }
    return result
}

object MapperProvider {
    val mapper: ObjectMapper by lazy {
        ObjectMapper(YAMLFactory())
                .registerKotlinModule()
    }

    fun getReader() : ObjectReader = mapper.reader()
    fun getWriter() : ObjectWriter = mapper.writer()
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
                String.format("_%s_%s_%s", locale.language, locale.country, locale.variant),
                String.format("_%s_%s", locale.language, locale.country),
                String.format("_%s", locale.language),
                String.format("_%s_%s_%s", defaultLocale.language, defaultLocale.country, defaultLocale.variant),
                String.format("_%s_%s", defaultLocale.language, defaultLocale.country),
                String.format("_%s", defaultLocale.language),
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
        return foundList.first()
    }
}

/**
 *
 */
object CachingResourceDeserializer
{
    val maxSize = 100L

    val cache : Cache<URL, Any> = CacheBuilder.newBuilder()
            .maximumSize(maxSize)
            .expireAfterAccess(5, TimeUnit.MINUTES)
            .build()

    @Suppress("UNCHECKED_CAST")
    fun <T> deserialize(url: URL, clazz: Class<T>, charset: Charset = StandardCharsets.UTF_8): T {
        synchronized(cache) {
            val result = cache.get(url, object : Callable<T> {
                override fun call(): T {
                    return uncached_deserialize(url, clazz, charset)
                }
            })
            return result as T
        }
    }

    private fun <T> uncached_deserialize(url: URL, clazz: Class<T>, charset: Charset = StandardCharsets.UTF_8): T {
        val str = Resources.toString(url, charset)
        return MapperProvider.getReader().forType(clazz).readValue(str)
    }
}
