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

import android.content.SharedPreferences
import android.support.v7.widget.*
import android.text.Editable
import android.text.InputType
import android.view.*
import android.widget.*
import com.fasterxml.jackson.core.type.TypeReference
import com.google.common.base.Strings
import com.mikepenz.fastadapter.items.*
import com.mikepenz.fastadapter.utils.*
import org.jetbrains.anko.*
import org.stevesea.adventuresmith.core.*
import java.util.*

class GeneratorButton(val generator: Generator,
                      val sharedPreferences: SharedPreferences,
                      locale: Locale = Locale.US,
                      val meta : GeneratorMetaDto = generator.getMetadata(locale)) :
        AbstractItem<GeneratorButton, GeneratorButton.ViewHolder>(),
        AnkoLogger {
    init {
        withIdentifier(generator.getId().hashCode().toLong())
    }
    val name = meta.name

    override fun getType(): Int {
        return R.id.btn_card
    }

    override fun getLayoutRes(): Int {
        return R.layout.button_grid_item
    }

    override fun bindView(holder: ViewHolder?, payloads: MutableList<Any?>?) {
        super.bindView(holder, payloads)

        holder!!.btnText.text = htmlStrToSpanned(name)

        if (meta.input != null) {
            holder.btnSettings.visibility = View.VISIBLE
            holder.btnTextConfig.visibility = View.VISIBLE
        } else {
            holder.btnSettings.visibility = View.GONE
            holder.btnTextConfig.visibility = View.GONE
        }
    }

    class ViewHolder(v: View?) : RecyclerView.ViewHolder(v) {
        val btnText =  v!!.findViewById(R.id.btn_txt) as TextView
        val btnTextConfig =  v!!.findViewById(R.id.btn_txt_config) as TextView
        val btnSettings = v!!.findViewById(R.id.btn_settings) as ImageView
    }

    override fun getFactory(): ViewHolderFactory<out ViewHolder> {
        return Factory
    }
    companion object Factory : ViewHolderFactory<ViewHolder> {
        override fun create(v: View?): ViewHolder {
            return ViewHolder(v)
        }
    }
}

