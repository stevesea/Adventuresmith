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

import android.annotation.SuppressLint
import android.os.Parcel
import android.os.Parcelable
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.mikepenz.fastadapter.commons.utils.FastAdapterUIUtils
import com.mikepenz.fastadapter.items.AbstractItem
import com.mikepenz.iconics.view.IconicsTextView
import com.mikepenz.materialize.util.UIUtils
import java.util.concurrent.atomic.AtomicLong

class ResultItem(val htmlTxt: String, val useIconicsTextView: Boolean = false) :
        AbstractItem<GeneratorButton, ResultItem.ViewHolder>(),
        Parcelable {
    @SuppressLint("NewApi")
    val spannedText = htmlStrToSpanned(htmlTxt)
    val plainText by lazy {
        spannedText.toString()
    }

    init {
        withIdentifier(resultId.andIncrement)
    }

    override fun getType(): Int {
        return R.id.result_card
    }

    override fun getLayoutRes(): Int {
        return R.layout.result_list_item
    }

    override fun bindView(holder: ViewHolder, payloads: List<*>?) {
        super.bindView(holder, payloads)

        // iconics layout inflater causes crash after a few results (sometimes).
        // using iconicstextview fixes crash, but it doesn't treat html->spannables the same way
        // as regular textview.
        //
        // so... how about an awful compromise! If result has icon in it, use IconicsTextView,
        // if not, use the TextView
        if (useIconicsTextView) {
            holder.itemText.visibility = View.GONE
            holder.itemIconicsText.visibility = View.VISIBLE
            holder.itemIconicsText.text = (spannedText)
            val ctx = holder.itemIconicsText.context
            //set the background for the item
            UIUtils.setBackground(holder.itemIconicsText,
                    FastAdapterUIUtils.getSelectableBackground(ctx, ContextCompat.getColor(ctx, R.color.resultCardBgSelected), true))
        } else {
            holder.itemText.visibility = View.VISIBLE
            holder.itemIconicsText.visibility = View.GONE
            holder.itemText.text = (spannedText)
            val ctx = holder.itemText.context
            //set the background for the item
            UIUtils.setBackground(holder.itemText,
                    FastAdapterUIUtils.getSelectableBackground(ctx, ContextCompat.getColor(ctx, R.color.resultCardBgSelected), true))
        }

    }

    class ViewHolder(v: View?) : RecyclerView.ViewHolder(v!!) {
        val itemText = v?.findViewById(R.id.result_list_item_text) as TextView
        val itemIconicsText = v?.findViewById(R.id.result_list_item_iconics_text) as IconicsTextView
    }

    override fun getViewHolder(v: View?): ViewHolder {
        return ViewHolder(v)
    }

    companion object {
        val resultId : AtomicLong = AtomicLong(0)

        // called by parcelable
        @JvmField val CREATOR: Parcelable.Creator<ResultItem> = object : Parcelable.Creator<ResultItem> {
            override fun createFromParcel(source: Parcel): ResultItem = ResultItem(source)
            override fun newArray(size: Int): Array<ResultItem?> = arrayOfNulls(size)
        }
    }

    constructor(source: Parcel) : this(source.readString(), if (source.readInt() == 1) true else false)

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeString(htmlTxt)
        dest?.writeInt(if (useIconicsTextView) 1 else 0 )
    }
}
