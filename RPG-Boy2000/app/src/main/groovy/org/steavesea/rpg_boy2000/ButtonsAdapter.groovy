package org.steavesea.rpg_boy2000

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import groovy.transform.CompileStatic
import org.steavesea.rpg_boy2000.data.RpgBoyData

import javax.inject.Inject

@CompileStatic
class ButtonsAdapter extends RecyclerView.Adapter<ViewHolder> {
    List<String> buttons;
    ResultsAdapter resultsAdapter
    RpgBoyData rpgBoyData
    String currentDataset = RpgBoyData.DEFAULT

    @Inject
    public ButtonsAdapter(RpgBoyData rpgBoyData, ResultsAdapter resultsAdapter) {
        this.buttons = rpgBoyData.getButtons(currentDataset)
        this.rpgBoyData = rpgBoyData
        notifyDataSetChanged()
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
        final String btnText = buttons.get(position);
        holder.btn.setText(btnText)
        holder.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            void onClick(View v) {
                def results = rpgBoyData.runGenerator(currentDataset, btnText)
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

    public void useDb(String key) {
        currentDataset = key
        buttons = rpgBoyData.getButtons(key)
        notifyDataSetChanged()
    }
}
