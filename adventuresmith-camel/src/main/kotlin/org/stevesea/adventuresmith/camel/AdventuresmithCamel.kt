/*
 ()* Copyright (c) 2017 Steve Christensen
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

package org.stevesea.adventuresmith.camel

import mu.KLoggable
import org.apache.camel.Exchange
import org.apache.camel.FluentProducerTemplate
import org.apache.camel.Handler
import org.apache.camel.builder.DefaultFluentProducerTemplate
import org.apache.camel.builder.RouteBuilder
import org.apache.camel.impl.DefaultCamelContext
import java.util.*

class StringToInt : KLoggable {
    override val logger = AdventuresmithCamel.logger()
    @Handler
    fun test(param: String, exchange: Exchange) : Int {
        logger.info("body: {}", param)
        return param.toInt()
    }
}
class IntToSquare : KLoggable {
    override val logger = AdventuresmithCamel.logger()
    @Handler
    fun test(param: Int, exchange: Exchange) : Int {
        logger.info("body: {}", param)
        return param * param
    }
}

//
//      generator_id
//      + locale      --> data merger -> merged data
//
//      <merged ctxt>
//      + <input> (optional) --> template processor -> result

object AdventuresmithCamel : KLoggable {

    override val logger = logger()
    @JvmStatic fun main(args: Array<String>) {
        val context = DefaultCamelContext()

        try {
            context.addRoutes(object : RouteBuilder() {
                override fun configure() {
                    from("direct:in")
                            .to("log:AdventuresmithCamel?level=INFO&showAll=true")
                            .bean(StringToInt())
                            .to("log:AdventuresmithCamel?level=INFO&showAll=true")
                            .bean(IntToSquare())
                            .to("log:AdventuresmithCamel?level=INFO&showAll=true")
                            .bean(IntToSquare())
                }
            })


            val template = context.createProducerTemplate()

            context.start()

            val headers : MutableMap<String, Any> = mutableMapOf()
            headers.put("hdr1", "val1")
            headers.put("input", mapOf("i1" to "i1val", "i2" to "i2val"))

            args.forEach {
                val v = template.requestBodyAndHeaders(
                        "direct:in",
                        it,
                        headers,
                        Int::class.java)
                logger.info("returned: {}", v)
            }

        } finally {
            context.stop()
        }
    }
}
