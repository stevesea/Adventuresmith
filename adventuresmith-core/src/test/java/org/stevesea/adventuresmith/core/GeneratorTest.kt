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
import org.junit.*

class GeneratorTest {
    val input = """
        ---
        # tables is a map of RangeMaps
        # - each key of the tables map become fields you can use in the template(s).
        # - each time the field is referenced in the template, the table[field] will be picked from
        tables:
            effect:
            - effect1
            - effect2
            form:
            - 1..2, form1
            - 3..6, form2
            adjective:
            - adj1
            - adj2

        # templates is a rangemap too
        templates:
            - >
                example multiline {{effect}} {{form}}
                this is on same line as above

                this is separated by newline
            - "{{effect}} {{form}}"
            - "{{adjective}} {{form}}"
            - 4..6, Templates can also have range prefixes
        """.trimIndent()

    @Test
    fun readFancyYaml() {

        val kodein = getKodein(getMockRandom())
        val reader : ObjectReader = kodein.instance()

        val dto : DataDrivenGenDto = reader.forType(DataDrivenGenDto::class.java).readValue(input)

        Assert.assertEquals(3, dto.tables.size)
        Assert.assertEquals(4, dto.templates.size)
        Assert.assertEquals(listOf("effect1", "effect2"),dto.tables.get("effect")!!.values.toList())

    }
}
