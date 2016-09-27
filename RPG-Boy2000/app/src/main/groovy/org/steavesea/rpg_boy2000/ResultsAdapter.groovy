package org.steavesea.rpg_boy2000

import android.support.design.widget.Snackbar
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView;

class ResultsAdapter extends RecyclerView.Adapter<ViewHolder> {
    public static final String SEPARATOR = "------------"

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
        final String name = dataset.get(position);
        holder.itemText.setText(dataset.get(position));
        if (name != SEPARATOR)
            holder.itemText.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    remove(name);
                    Snackbar.make(v, "Erased item ${name}", Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
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

    ResultsAdapter(List<String> dataset) {
        this.dataset = dataset
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

    boolean isEmpty() {
        return dataset.isEmpty()
    }
}
