/*
 * Copyright (c) 2016 Steve Christensen
 *
 * This file is part of RPG-Boy 2000.
 *
 * RPG-Boy 2000 is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * RPG-Boy 2000 is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with RPG-Boy 2000.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.stevesea.rpg_boy2000

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import groovy.transform.CompileStatic
import org.stevesea.rpg_boy2000.data.AbstractGenerator
import org.stevesea.rpg_boy2000.data.Dataset
import org.stevesea.rpg_boy2000.data.DatasetButton

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@CompileStatic
class ButtonsAdapter extends RecyclerView.Adapter<ViewHolder> {
    List<DatasetButton> buttons;
    ResultsAdapter resultsAdapter
    Dataset currentDataset

    RpgBoy2000App app

    @Inject
    public ButtonsAdapter(ResultsAdapter resultsAdapter, RpgBoy2000App app) {
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
        final String btnText = app.getString(buttons.get(position).stringResourceId);
        holder.btn.setText(btnText)
        holder.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            void onClick(View v) {
                def results = generator.generate(5).toList()
                resultsAdapter.addAll(results)
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

    public void clear() {
        buttons.clear()
        notifyDataSetChanged()
    }

    public void useDb(Dataset key) {
        currentDataset = key
        buttons.clear()
        buttons.addAll(DatasetButton.getButtonsForDataset(key))
        notifyDataSetChanged()
    }
}
