package com.neeber.andygen

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.ArrayAdapter
import android.widget.ListView
import com.arasthel.swissknife.SwissKnife
import com.arasthel.swissknife.annotations.InjectView
import com.arasthel.swissknife.annotations.OnClick
import groovy.transform.CompileStatic

@CompileStatic
class MainActivity extends AppCompatActivity {
    @InjectView(R.id.list_view)
    ListView resultList;

    //Context mContext

    @OnClick(R.id.btn_place)
    public void onButtonClickedPlace() {
        String[] items = ["place1", "place2"]
        resultList.adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
                items)
    }

    @OnClick(R.id.btn_region)
    public void onButtonClickedRegion() {
        resultList.adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
                Region.generate(5))
    }

    @OnClick(R.id.btn_fotf_spell)
    public void onButtonClickedSpells() {
        String[] items = ["spell1", "spell2", "spell3"]
        resultList.adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
                items)
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //mContext = this
        contentView = R.layout.activity_main

        // This must be called for injection of views and callbacks to take place
        SwissKnife.inject this

        // This must be called for saved state restoring
        //SwissKnife.restoreState(this, savedInstanceState);

        // This mus be called for automatic parsing of intent extras
        SwissKnife.loadExtras(this)
    }
}