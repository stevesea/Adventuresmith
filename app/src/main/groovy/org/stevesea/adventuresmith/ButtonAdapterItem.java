/*
 * Copyright (c) 2016 Steve Christensen
 *
 * This file is part of RPG-Pad.
 *
 * RPG-Pad is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * RPG-Pad is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with RPG-Pad.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package org.stevesea.adventuresmith;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.mikepenz.fastadapter.items.AbstractItem;
import com.mikepenz.fastadapter.utils.ViewHolderFactory;
import com.mikepenz.materialdrawer.holder.StringHolder;

import java.util.List;

public class ButtonAdapterItem extends AbstractItem<ButtonAdapterItem, ButtonAdapterItem.ViewHolder> implements Parcelable {
    //the static ViewHolderFactory which will be used to generate the ViewHolder for this Item
    private static final ViewHolderFactory<? extends ViewHolder> FACTORY = new ItemFactory();

    public StringHolder btnText;
    public ButtonData buttonData;

    ButtonAdapterItem withButton(ButtonData bd) {
        btnText = new StringHolder(bd.getId());
        buttonData = bd;
        withIdentifier(bd.getId());
        return this;
    }

    @Override
    public int getType() {
        return R.id.btn_card;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.button_grid_item;
    }

    //The logic to bind your data to the view
    @Override
    public void bindView(ViewHolder viewHolder, List payloads) {
        //call super so the selection is already handled for you
        super.bindView(viewHolder, payloads);

        // bind item's data to the view
        StringHolder.applyTo(btnText, viewHolder.btnText);
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView btnText;

        ViewHolder(View v) {
            super(v);
            btnText = (TextView) v.findViewById(R.id.btn_txt);
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
        dest.writeLong(this.mIdentifier);
    }

    public ButtonAdapterItem() {
    }

    protected ButtonAdapterItem(Parcel in) {
        this.mIdentifier = in.readLong();

        this.buttonData = ButtonData.getButton(mIdentifier);
        this.btnText = new StringHolder(buttonData.getId());
    }

    public static final Creator<ButtonAdapterItem> CREATOR = new Creator<ButtonAdapterItem>() {
        @Override
        public ButtonAdapterItem createFromParcel(Parcel source) {
            return new ButtonAdapterItem(source);
        }

        @Override
        public ButtonAdapterItem[] newArray(int size) {
            return new ButtonAdapterItem[size];
        }
    };
}
