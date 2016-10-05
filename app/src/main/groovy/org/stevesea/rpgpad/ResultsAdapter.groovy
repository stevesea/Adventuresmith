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

import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.text.Spanned
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
        String htmlTxt = dataset.get(position)

        Spanned spanned = MainActivity.htmlStrToSpanned(htmlTxt)
        String plainTxt = spanned.toString()

        holder.itemText.text = spanned

        holder.itemText.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                // TODO: http://stackoverflow.com/questions/24737622/how-add-copy-to-clipboard-to-custom-intentchooser
                // TODO: https://gist.github.com/mediavrog/5625602
                /*
                ClipboardManager clipboard = v.getContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager;
                ClipData clipData = ClipData.newHtmlText(v.getContext().getString(R.string.app_name), plainTxt, htmlTxt)
                clipboard.setPrimaryClip(clipData)
                */

                Intent sIntent = new Intent()
                sIntent.setAction(Intent.ACTION_SEND)
                sIntent.setType("text/html")
                //sIntent.putExtra(Intent.EXTRA_SUBJECT, "Look at what I generated with ${v.getContext().getString(R.string.app_name)}")
                sIntent.putExtra(Intent.EXTRA_TEXT, plainTxt)
                sIntent.putExtra(Intent.EXTRA_HTML_TEXT, htmlTxt)
                //sIntent.setClipData(clipData)
                v.getContext().startActivity(Intent.createChooser(sIntent,
                        v.getContext().getString(R.string.action_share)))

                //Snackbar.make(v, "Shared...", Snackbar.LENGTH_SHORT).setAction("Action", null).show();
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
