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

package org.stevesea.adventuresmith;

import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.widget.TextView;

import com.mikepenz.fastadapter.items.AbstractItem;
import com.mikepenz.fastadapter.utils.ViewHolderFactory;

import java.util.List;

public class ResultAdapterItem extends AbstractItem<ResultAdapterItem, ResultAdapterItem.ViewHolder> implements Parcelable {
    //the static ViewHolderFactory which will be used to generate the ViewHolder for this Item
    private static final ViewHolderFactory<? extends ViewHolder> FACTORY = new ItemFactory();

    @SuppressWarnings("deprecation")
    public static Spanned htmlStrToSpanned(String input) {
        if (Build.VERSION.SDK_INT >= 24 /*Build.VERSION_CODES.N*/) {
            return Html.fromHtml(input, Html.FROM_HTML_MODE_LEGACY);
        } else {
            return Html.fromHtml(input);
        }
    }
    public String htmlTxt;
    public Spanned spannedText;
    // TODO: from the buttonId, we can lookup more info (maybe display icon/change background)
    public int buttonId;

    ResultAdapterItem withResult(String txt) {
        htmlTxt = txt;
        spannedText = htmlStrToSpanned(txt);
        return this;
    }
    ResultAdapterItem withButtonId(int buttonId) {
        this.buttonId = buttonId;
        return this;
    }

    @Override
    public int getType() {
        return R.id.result_card;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.result_list_item;
    }

    //The logic to bind your data to the view
    @Override
    public void bindView(ViewHolder viewHolder, List payloads) {
        //call super so the selection is already handled for you
        super.bindView(viewHolder, payloads);

        // bind item's data to the view
        viewHolder.itemText.setText(spannedText);
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView itemText;

        ViewHolder(View v) {
            super(v);
            itemText = (TextView) v.findViewById(R.id.result_list_item_text);
        }
    }

    /**
     * our ItemFactory implementation which creates the ViewHolder for our adapter.
     * It is highly recommended to implement a ViewHolderFactory as it is 0-1ms faster for ViewHolder creation,
     * and it is also many many times more efficient if you define custom listeners on views within your item.
     */
    protected static class ItemFactory implements ViewHolderFactory<ViewHolder> {
        public ViewHolder create(View v) {
            return new ViewHolder(v);
        }
    }

    /**
     * return our ViewHolderFactory implementation here
     *
     * @return
     */
    @Override
    public ViewHolderFactory<? extends ViewHolder> getFactory() {
        return FACTORY;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.htmlTxt);
        dest.writeInt(this.buttonId);
        dest.writeLong(this.mIdentifier);
    }

    public ResultAdapterItem() {
    }

    protected ResultAdapterItem(Parcel in) {
        this.htmlTxt = in.readString();
        this.buttonId = in.readInt();
        this.mIdentifier = in.readLong();
        this.spannedText = htmlStrToSpanned(htmlTxt);
    }

    public static final Creator<ResultAdapterItem> CREATOR = new Creator<ResultAdapterItem>() {
        @Override
        public ResultAdapterItem createFromParcel(Parcel source) {
            return new ResultAdapterItem(source);
        }

        @Override
        public ResultAdapterItem[] newArray(int size) {
            return new ResultAdapterItem[size];
        }
    };
}
