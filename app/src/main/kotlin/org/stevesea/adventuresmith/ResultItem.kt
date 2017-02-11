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

import android.os.*
import android.support.v4.content.*
import android.support.v7.widget.*
import android.view.*
import android.widget.*
import com.mikepenz.fastadapter.commons.utils.*
import com.mikepenz.fastadapter.items.*
import com.mikepenz.fastadapter.utils.*
import com.mikepenz.materialize.util.*
import java.util.concurrent.atomic.*

class ResultItem(val htmlTxt: String) :
        AbstractItem<GeneratorButton, ResultItem.ViewHolder>(),
        Parcelable {
    val spannedText = htmlStrToSpanned(htmlTxt)

    init {
        withIdentifier(resultId.andDecrement)
    }

    override fun getType(): Int {
        return R.id.result_card
    }

    override fun getLayoutRes(): Int {
        return R.layout.result_list_item
    }

    override fun bindView(holder: ViewHolder, payloads: List<*>?) {
        super.bindView(holder, payloads)
        holder.itemText.text = (spannedText)

        val ctx = holder.itemText.getContext()
        //set the background for the item
        UIUtils.setBackground(holder.itemText, FastAdapterUIUtils.getSelectableBackground(ctx, ContextCompat.getColor(ctx, R.color.resultCardBgSelected), true));
    }

    class ViewHolder(val v: View?) : RecyclerView.ViewHolder(v) {
        val itemText = v!!.findViewById(R.id.result_list_item_text) as TextView
    }

    override fun getFactory(): ViewHolderFactory<out ViewHolder> {
        return Factory
    }

    companion object Factory : ViewHolderFactory<ViewHolder> {
        val resultId : AtomicLong = AtomicLong(0)

        // called by the FastAdapter
        override fun create(v: View?): ViewHolder {
            return ViewHolder(v)
        }

        // called by parcelable
        @JvmField val CREATOR: Parcelable.Creator<ResultItem> = object : Parcelable.Creator<ResultItem> {
            override fun createFromParcel(source: Parcel): ResultItem = ResultItem(source)
            override fun newArray(size: Int): Array<ResultItem?> = arrayOfNulls(size)
        }
    }

    constructor(source: Parcel) : this(source.readString())

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeString(htmlTxt)
    }
}
