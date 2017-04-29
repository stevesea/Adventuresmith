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
import org.apache.camel.Handler
import org.apache.camel.builder.RouteBuilder
import org.apache.camel.impl.DefaultCamelContext

class Locator : KLoggable {
    override val logger = AdventuresmithCamel.logger()
    @Handler
    fun test(param: String) {
        logger.info("{}", param)
    }
}

object AdventuresmithCamel : KLoggable {

    override val logger = logger()
    @JvmStatic fun main(args: Array<String>) {
        val context = DefaultCamelContext()

        try {
            context.addRoutes(object : RouteBuilder() {
                override fun configure() {
                    from("direct:in")
                            .to("log:AdventuresmithCamel?level=INFO")
                            .bean(Locator())
                }
            })

            val template = context.createProducerTemplate()

            context.start()

            args.forEach {
                template.sendBody("direct:in", it)
            }


        } finally {
            context.stop()
        }
    }
}
