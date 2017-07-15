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
import com.github.salomonbrys.kodein.bind
import com.github.salomonbrys.kodein.instance
import com.github.salomonbrys.kodein.provider
import org.junit.Assert
import org.junit.Test
import java.util.Locale

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
        nested_tables:
            ntable1:
                ntable1subA:
                - ntsA
                - ntsB
                - 3..6, ntsC
        """.trimIndent()

    @Test
    fun verifyRangeMapDeserialization() {

        val kodein = getKodein(getMockRandom())
        val reader : ObjectReader = kodein.instance()

        val dto : DataDrivenGenDto = reader.forType(DataDrivenGenDto::class.java).readValue(input)

        Assert.assertEquals(3, dto.tables!!.size)
        Assert.assertEquals(4, dto.templates!!.size)
        Assert.assertEquals(listOf("effect1", "effect2"), dto.tables!!.get("effect")!!.values.toList())
    }

    @Test
    fun verifyGeneratorBasics() {
        val testKey = "test_resource"
        val base = getKodein(getMockRandom(0))
        val testKodein = Kodein {
            extend(base)

            bind<Generator>(testKey) with provider {
                DataDrivenGeneratorForResources(testKey, base)
            }
        }

        val g : Generator = testKodein.instance(testKey)

        Assert.assertEquals("""
        table1: t1_valA
        table2: t2_valA

        table3: t2_valA   # recursive reference, tab3's string is a key to point at tab2

        table1 N: t1_valB, t1_valC               # pick N elements (default delimeter is comma)
        table1 N: t1_valB;&nbsp;t1_valC       # pick N elements (supply own delim)
        table1 NDice: t1_valB, t1_valC     # pick <dice> elements

        table2 pick: t2_valA    # pick from a table, but only interested in first <dice> vals

        sibling_table: ts_val1      # direct dependency
        sibling_table2: ts2_val1    # transitive dependency (brought in by sibling_table)

        nested_table: subtA_val1 # selecting nested table works just like selecting a any RangeMap

        # to randomly select a particular nested table, you might do something like the following
        # the template is processed repeatedly until all {{'s are gone.
        # (only up to five times). first pass through the processor, the table below will be
        # selected, second pass it'll select items from the nested tables.
        random pick of nested: subtableA - subtA_val1

        # we can also just have variables in 'definitions' map
        asdfasdf, asdfasf2, asfdasdf3

        objval1objval2objval3

        dice:
        3d6: 3
        20d20+1: 21
        6d4: 6
        6d4: 6, 6, 6, 6  # roll 4 tiles

        state:





        4
        val1<br/>val2
        1
        1
        1













        5,3,10,7,2 # show list
         # sort int list
        2,3,5,7,10 # show list again

         # sort string list
        apple,apricot,mango,peach,plum # display string list

          # keep last two entries of int list
        7,10 # show list again (should be highest two)
        17 # summed truncated int-list (17)

         # keep first three string-list entries
        apple,apricot,mango
        """.trimIndent(), g.generate(Locale.US))
    }
}
