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

    val SETTING_GENERATOR_CONFIG_PREFIX = "GeneratorConfig."
    fun getGeneratorConfig(genId: String) : Map<String, String> {
        val str = sharedPreferences.getString(SETTING_GENERATOR_CONFIG_PREFIX + genId, "")
        if (Strings.isNullOrEmpty(str)) {
            return mapOf()
        } else {
            return AdventuresmithApp.objectReader.forType(object: TypeReference<Map<String, String>>(){}).readValue(str)
        }
    }
    fun setGeneratorConfig(genId: String, value: Map<String, String>) {
        val str = AdventuresmithApp.objectWriter.writeValueAsString(value)
        sharedPreferences.edit()
                .putString(SETTING_GENERATOR_CONFIG_PREFIX + genId, str)
                .apply()
    }

    fun showGeneratorConfigDialog(v: View) {
        val previousConfig = getGeneratorConfig(generator.getId())
        info("Previous config: " + previousConfig)
        with(v.context) {
            alert(R.string.generator_config) {
                customView {
                    verticalLayout {
                        val newConfigEdits: MutableMap<String, EditText> = mutableMapOf()
                        meta.inputParams.map {
                            info("param: " + it)
                            val displayVal = if (previousConfig.containsKey(it.name)) previousConfig.get(it.name) else it.defaultValue
                            verticalLayout {
                                textView {
                                    text = Editable.Factory.getInstance().newEditable(it.helpText)
                                }
                                newConfigEdits.put(it.name, editText {
                                    hint = it.uiName
                                    maxLines = 1
                                    singleLine = true
                                    inputType = if (it.numbersOnly) InputType.TYPE_CLASS_NUMBER else InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS
                                    text = Editable.Factory.getInstance().newEditable(displayVal)
                                })
                            }
                        }
                        okButton {
                            val newConfig: MutableMap<String, String> = mutableMapOf()
                            meta.inputParams.forEach {
                                val userVal = newConfigEdits.get(it.name)!!.text.toString().trim()
                                if (Strings.isNullOrEmpty(userVal)) {
                                    newConfig.put(it.name, it.defaultValue)
                                } else {
                                    newConfig.put(it.name, userVal)
                                }
                            }
                            info("new config: " + newConfig)
                            setGeneratorConfig(generator.getId(), newConfig)
                        }
                    }
                }
            }.show()
        }
    }

    override fun getType(): Int {
        return R.id.btn_card
    }

    override fun getLayoutRes(): Int {
        return R.layout.button_grid_item
    }

    override fun bindView(holder: ViewHolder?, payloads: MutableList<Any?>?) {
        super.bindView(holder, payloads)

        holder!!.btnText.text = htmlStrToSpanned(name)

        if (meta.inputParams.isNotEmpty()) {
            holder.btnSettings.visibility = View.VISIBLE
            holder.btnSettings.setOnClickListener(object: View.OnClickListener {
                override fun onClick(v: View?) {
                    info("Button click!")
                    showGeneratorConfigDialog(v!!)
                }
            })
        } else {
            holder.btnSettings.visibility = View.GONE
            holder.btnSettings.setOnClickListener(object: View.OnClickListener {
                override fun onClick(v: View?) {
                    debug("Ignored!")
                }
            })
        }
    }

    class ViewHolder(v: View?) : RecyclerView.ViewHolder(v) {
        val btnText =  v!!.findViewById(R.id.btn_txt) as TextView
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
