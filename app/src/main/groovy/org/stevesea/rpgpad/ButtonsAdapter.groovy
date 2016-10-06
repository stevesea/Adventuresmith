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
 */
package org.stevesea.rpgpad

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.crashlytics.android.answers.Answers
import com.crashlytics.android.answers.CustomEvent
import groovy.transform.CompileStatic

@CompileStatic
class ButtonsAdapter extends RecyclerView.Adapter<ViewHolder> {
    List<DatasetButton> buttons
    MainActivity mainActivity

    public ButtonsAdapter(MainActivity mainActivity, List<DatasetButton> buttons) {
        this.buttons = buttons
        this.mainActivity = mainActivity
    }

    @Override
    ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.button_grid_item, parent, false)
        ViewHolder vh = new ViewHolder(v)
        return vh
    }

    @Override
    void onBindViewHolder(ViewHolder holder, int position) {
        final DatasetButton btn = buttons.get(position)
        final String btnText = holder.itemView.getContext().getString(btn.stringResourceId);
        holder.btn.setText(btnText)
        holder.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            void onClick(View v) {
                mainActivity.generateButtonPressed(btn)
                Answers.getInstance().logCustom(new CustomEvent("Generated Result")
                        .putCustomAttribute("Dataset", btn.getDataset().name())
                        .putCustomAttribute("Button", btn.getDataset().name() + '.' + btnText)
                )
            }
        })
    }

    @Override
    int getItemCount() {
        return buttons == null ? 0 : buttons.size()
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public Button btn;
        public ViewHolder(View v) {
            super(v);
            btn = (Button) v.findViewById(R.id.button_grid_item_btn);
        }
    }

}
