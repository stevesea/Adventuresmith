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

import android.content.Context
import android.support.design.widget.Snackbar
import android.support.v7.widget.RecyclerView
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import groovy.transform.CompileStatic
import org.steavesea.rpg_boy2000.R

import javax.inject.Inject
import javax.inject.Singleton

@CompileStatic
@Singleton
class ResultsAdapter extends RecyclerView.Adapter<ViewHolder> {

    // so we can look up resources
    @Inject @ForApplication
    Context context

    List<Integer> colors = [
            R.color.resultsBg0,
            R.color.resultsBg1,
            R.color.resultsBg2,
            R.color.resultsBg3,
            R.color.resultsBg4,
            R.color.resultsBg5,
            R.color.resultsBg6,
            R.color.resultsBg7,
            R.color.resultsBg8,
            R.color.resultsBg9,
            R.color.resultsBg10,
            R.color.resultsBg11,
    ]
    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    @Override
    ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.result_list_item, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        final String txt = dataset.get(position);
        holder.itemText.setText(Html.fromHtml(dataset.get(position), Html.FROM_HTML_MODE_LEGACY));
        holder.itemText.setBackgroundColor(context.getColor((int)colors.get(position % colors.size())))
        holder.itemText.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                remove(txt);
                Snackbar.make(v, "Erased item", Snackbar.LENGTH_SHORT).setAction("Action", null).show();
                return true;
            }
        });
    }

    @Override
    int getItemCount() {
        return dataset.size()
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView itemText;
        public ViewHolder(View v) {
            super(v);
            itemText = (TextView) v.findViewById(R.id.result_list_item_text);
        }
    }

    private List<String> dataset;

    ResultsAdapter() {
        this.dataset = new ArrayList<String>()
    }

    public void add(int position, String item) {
        dataset.add(position, item);
        notifyItemInserted(position);
    }

    public void add(String item) {
        dataset.add(0, item)
        notifyDataSetChanged()
    }
    public void addAll(List<String> items) {
        dataset.addAll(0, items)
        notifyDataSetChanged()
    }

    // TODO: is there more robust way to remove item by widget? this removes by string... which if generators are unique shouldn't be a problem
    public void remove(String item) {
        int position = dataset.indexOf(item);
        dataset.remove(position);
        notifyItemRemoved(position);
    }

    public void clear() {
        dataset.clear()
        notifyDataSetChanged()
    }

    public int getTextLength(int position) {
        return dataset.get(position).size()
    }
}
