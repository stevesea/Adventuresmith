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


object ResourceFinder {
    private fun locale_names(name: String, locale: Locale, ext: String) : List<String> {
        // look for things like resource bundle:
        // If a ResourceBundle class for the specified Locale does not exist, getBundle tries to
        // find the closest match. For example, if ButtonLabel_fr_CA_UNIX is the desired class and
        // the default Locale is en_US, getBundle will look for classes in the following order:

        //ButtonLabel_fr_CA_UNIX
        //ButtonLabel_fr_CA
        //ButtonLabel_fr
        //ButtonLabel_en_US
        //ButtonLabel_en
        //ButtonLabel
        val defaultLocale = Locale.getDefault()
        val lnames = linkedSetOf(
                String.format("_%s_%s_%s", locale.language, locale.country, locale.variant),
                String.format("_%s_%s", locale.language, locale.country),
                String.format("_%s", locale.language),
                String.format("_%s_%s_%s", defaultLocale.language, defaultLocale.country, defaultLocale.variant),
                String.format("_%s_%s", defaultLocale.language, defaultLocale.country),
                String.format("_%s", defaultLocale.language),
                ""
        )
        val lnames2 = lnames.filter { !it.endsWith("_")}
        val fnames = lnames2.map { String.format("%s%s%s", name, it, ext)}

        return fnames
    }
    fun <T> find(name: String, locale: Locale, clazz: Class<T>, ext: String = ".yml") : URL {
        val lnames = locale_names(name, locale, ext)
        val urls = lnames.map { it -> clazz.getResource(it) }
        val foundList = urls.filterNotNull()
/*
        val nullableList: List<URL?> = mutableListOf(
                clazz.getResource(name + ".yml")

                Thread.currentThread().getContextClassLoader().getResource(name),
                Thread.currentThread().getContextClassLoader().getResource("/org/stevesea/adventuresmith/core/" + name),
                Thread.currentThread().getContextClassLoader().getResource("org/stevesea/adventuresmith/core/" + name),
                Thread.currentThread().getContextClassLoader().getResource("/org/stevesea/adventuresmith/core/" + name),
                Thread.currentThread().getContextClassLoader().getResource("/org/stevesea/adventuresmith/core/" + name)

                //Resources.getResource(FotfSpellWizardNamesDto::class.java, "wizard_names.yml"),
        )
        val foundList: List<URL> = nullableList.filterNotNull()
        */
        //FotfSpellWizardNamesDto::class.java.getClassLoader().getResource("wizard_names.yml")
        //if (foundList.isEmpty())
        //    return Resources.getResource("org/stevesea/adventuresmith/core/" + name)
        return foundList.first()
    }

}

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
