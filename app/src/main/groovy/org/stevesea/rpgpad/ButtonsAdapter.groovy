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

import android.support.design.widget.Snackbar
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import groovy.transform.CompileStatic
import org.stevesea.rpgpad.data.AbstractGenerator

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@CompileStatic
class ButtonsAdapter extends RecyclerView.Adapter<ViewHolder> {
    List<DatasetButton> buttons;
    ResultsAdapter resultsAdapter

    RpgPadApp app

    @Inject
    public ButtonsAdapter(ResultsAdapter resultsAdapter, RpgPadApp app) {
        this.app = app
        this.buttons = new ArrayList<>()
        this.resultsAdapter = resultsAdapter
    }

    @Override
    ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.button_grid_item, parent, false)
        ViewHolder vh = new ViewHolder(v)
        return vh
    }

    @Override
    void onBindViewHolder(ViewHolder holder, int position) {
        final AbstractGenerator generator = app.generatorFactory(buttons.get(position))
        final String btnText = holder.itemView.getContext().getString(buttons.get(position).stringResourceId);
        final int numToGenerate = holder.itemView.getContext().getResources().getInteger(buttons.get(position).numGeneratedId)
        holder.btn.setText(btnText)
        holder.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            void onClick(View v) {
                def results = generator.generate(numToGenerate).toList()
                resultsAdapter.addAll(results)
            }
        })
        if (buttons.get(position).helpTextId != 0) {
            final String btnHelp = holder.itemView.getContext()
            holder.btn.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    Snackbar.make(v, btnHelp, Snackbar.LENGTH_SHORT).setAction("Action", null).show();
                    return true;
                }
            });
        }
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

    public void useDb(Dataset key) {
        buttons.clear()
        buttons.addAll(DatasetButton.getButtonsForDataset(key))
        notifyDataSetChanged()
    }
}
