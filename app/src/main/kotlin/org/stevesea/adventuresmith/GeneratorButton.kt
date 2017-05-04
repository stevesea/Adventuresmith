/*
 * Copyright (c) 2017 Steve Christensen
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

package org.stevesea.adventuresmith

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.mikepenz.fastadapter.items.AbstractItem
import org.jetbrains.anko.AnkoLogger
import org.stevesea.adventuresmith.core.Generator
import org.stevesea.adventuresmith.core.GeneratorMetaDto
import java.util.Locale

class GeneratorButton(val generator: Generator,
                      var inputMap: Map<String, String>,
                      locale: Locale = Locale.US,
                      val meta : GeneratorMetaDto = generator.getMetadata(locale)) :
        AbstractItem<GeneratorButton, GeneratorButton.ViewHolder>(),
        AnkoLogger {
    init {
        withIdentifier(generator.getId().hashCode().toLong())
    }
    val name = meta.name

    var vh : ViewHolder? = null

    override fun getType(): Int {
        return R.id.btn_card
    }

    override fun getLayoutRes(): Int {
        return R.layout.button_grid_item
    }

    fun updateInputMap(i: Map<String, String>) {
        inputMap = i
        if (vh != null) {
            vh?.btnTextConfig?.text = htmlStrToSpanned(meta.processInputForDisplay(inputMap))
        }
    }

    override fun bindView(holder: ViewHolder?, payloads: MutableList<Any?>?) {
        super.bindView(holder, payloads)

        holder!!.btnText.text = htmlStrToSpanned(name)

        if (meta.input != null) {
            holder.btnSettings.visibility = View.VISIBLE
            holder.btnTextConfig.visibility = View.VISIBLE

            holder.btnTextConfig.text = htmlStrToSpanned(meta.processInputForDisplay(inputMap))

            holder.btnText.minLines = 1
        } else {
            holder.btnSettings.visibility = View.GONE
            holder.btnTextConfig.visibility = View.GONE

            holder.btnText.minLines = 2
        }
        vh = holder
    }

    override fun unbindView(holder: ViewHolder?) {
        super.unbindView(holder)
        vh = null
    }

    class ViewHolder(v: View?) : RecyclerView.ViewHolder(v) {
        val btnText = v!!.findViewById(R.id.btn_txt) as TextView
        val btnTextConfig = v!!.findViewById(R.id.btn_txt_config) as TextView
        val btnSettings = v!!.findViewById(R.id.btn_settings) as ImageView
    }

    override fun getViewHolder(v: View?): ViewHolder {
        return ViewHolder(v)
    }
}

