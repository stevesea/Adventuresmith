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

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Build
import android.support.design.widget.Snackbar
import android.support.v7.widget.RecyclerView
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import groovy.transform.CompileStatic

@CompileStatic
class ResultsAdapter extends RecyclerView.Adapter<ViewHolder> {
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

        //https://blog.egorand.me/all-the-things-compat/
        // https://developer.android.com/reference/android/text/Html.html
        if (Build.VERSION.SDK_INT >= 24 /*Build.VERSION_CODES.N*/) {
            holder.itemText.setText(Html.fromHtml(dataset.get(position), Html.FROM_HTML_MODE_LEGACY));
        } else {
            holder.itemText.setText(Html.fromHtml(dataset.get(position)));
        }

        holder.itemText.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ClipboardManager clipboard = (ClipboardManager)holder.itemView.getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newHtmlText("RPG-Pad result",txt, txt);
                clipboard.setPrimaryClip(clip);
                Snackbar.make(v, "Copied item to clipboard", Snackbar.LENGTH_SHORT).setAction("Action", null).show();
                return true;
            }
        });
    }


    List<String> dataset;

    ResultsAdapter(List<String> data) {
        this.dataset = data
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
}
