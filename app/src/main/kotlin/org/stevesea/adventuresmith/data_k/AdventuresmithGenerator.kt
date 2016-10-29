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

package org.stevesea.adventuresmith.data_k

import android.content.*
import android.support.annotation.*
import android.support.v4.util.*
import org.stevesea.adventuresmith.core.*
import java.io.*
import java.nio.charset.*

// TODO: seems like long-term, having all this generation happen in a Jar library would make this
//  much easier to share among different client GUIs.



object ContextProvider {
    var context: Context? = null
}

object CachingRawResourceDeserializer
{
    val maxSize = 16

    val cache: LruCache<Int, Any> = LruCache(maxSize)

    private fun open(@RawRes resId: Int): InputStream {
        return ContextProvider.context!!.resources.openRawResource(resId)
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> deserialize(@RawRes resId: Int, clazz: Class<T>, charset: Charset = StandardCharsets.UTF_8): T {
        synchronized(cache) {
            var result = cache.get(resId)
            if (result == null) {
                result = uncached_deserialize(resId, clazz, charset)
                cache.put(resId, result)
            }
            return result as T
        }
    }

    private fun <T> uncached_deserialize(@RawRes resId: Int, clazz: Class<T>, charset: Charset = StandardCharsets.UTF_8): T {
        return open(resId).bufferedReader(charset).use {
            MapperProvider.getReader().forType(clazz).readValue(it)
        }
    }
}

object StringResourceLoader {
    fun get(@StringRes resId: Int) {
        ContextProvider.context!!.resources.getString(resId)
    }
}
