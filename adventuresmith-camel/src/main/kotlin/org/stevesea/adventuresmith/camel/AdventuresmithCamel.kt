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
import org.apache.camel.Handler
import org.apache.camel.builder.RouteBuilder
import org.apache.camel.impl.DefaultCamelContext

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
//   advsmith_generate
//      inputMap + locale
//      + generator_id    --->  which gen? -+--> data merger -> whencedata? ??? -> read FS -> <merged ctxt>
//                                          \                          \-----> read resources -> <merged ctxt>
//                                           \---> run generator from D.I. -> <result>
//
//
//      advsmith_process_template
//        <merged ctxt>  --> template processor -> <result>
//
//  advsmith_get_collections
//      locale --> ???? --> coll. of collection metadata
//
//  advsmith_get_generators
//      locale --> ???? --> coll. of generator metadata
//

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
                            .to("direct:stage2")
                    from("direct:stage2")
                            .to("log:AdventuresmithCamel?level=INFO&showAll=true")
                            .bean(IntToSquare())
                            .to("log:AdventuresmithCamel?level=INFO&showAll=true")
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
