package com.neeber.andygen

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import com.arasthel.swissknife.SwissKnife
import com.arasthel.swissknife.annotations.InjectView
import com.arasthel.swissknife.annotations.OnClick
import com.arasthel.swissknife.annotations.OnLongClick
import com.neeber.andygen.data.freebooters_on_the_frontier.FotFSpells
import com.neeber.andygen.data.freebooters_on_the_frontier.FotFTraits
import com.neeber.andygen.data.maze_rats.*
import com.neeber.andygen.data.perilous_wilds.PwPlace
import com.neeber.andygen.data.perilous_wilds.PwRegion
import groovy.transform.CompileStatic

import javax.inject.Inject

@CompileStatic
class MainActivity extends AppCompatActivity {
    @Inject
    PwRegion region;
    @Inject
    PwPlace place;
    @Inject
    FotFSpells fotFSpells;
    @Inject
    FotFTraits traits;
    @Inject
    MazeRatsCharacter mrChar;
    @Inject
    MazeRatsMagic mrSpell;
    @Inject
    MazeRatsItems mrItem;
    @Inject
    MazeRatsMonsters mrMonster;
    @Inject
    MazeRatsAfflictions mrAfflictions;
    @Inject
    MazeRatsPotionEffects mrPotEffects;

    @InjectView(R.id.list_view)
    ListView resultList;

    //Context mContext

    @OnClick(R.id.btn_place)
    public void onButtonClickedPlace() {
        resultList.adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
                place.generate(5))
    }

    @OnClick(R.id.btn_region)
    public void onButtonClickedRegion() {
        resultList.adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
                region.generate(5))
    }

    @OnClick(R.id.btn_fotf_spell)
    public void onButtonClickedSpells() {
        resultList.adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
                fotFSpells.generate(5))
    }

    @OnClick(R.id.btn_fotf_traits)
    public void onButtonClickedTraits() {
        resultList.adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
                traits.generate(5))
    }

    @OnClick(R.id.btn_mr_char)
    public void onButtonClickedMazeRatsChar() {
        resultList.adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
                mrChar.generate(5))
    }

    @OnClick(R.id.btn_mr_spell)
    public void onButtonClickedMazeRatsSpell() {
        resultList.adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
                mrSpell.generate(5))
    }

    @OnClick(R.id.btn_mr_item)
    public void onButtonClickedMazeRatsItem() {
        resultList.adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
                mrItem.generate(5))
    }

    @OnClick(R.id.btn_mr_monster)
    public void onButtonClickedMazeRatsMonster() {
        resultList.adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
                mrMonster.generate(5))
    }

    @OnClick(R.id.btn_mr_afflictions)
    public void onButtonClickedMazeRatsAfflictions() {
        resultList.adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
                mrAfflictions.generate(5))
    }

    @OnClick(R.id.btn_mr_potion_effects)
    public void onButtonClickedMazeRatsPotionEffects() {
        resultList.adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
                mrPotEffects.generate(5))
    }

    @OnLongClick(R.id.lbl_mr)
    public boolean onLongClickMR() {
        Toast.makeText(this,"Maze Rats 0.1\nby Ben Milton\nhttp://questingblog.com/maze-rats/", Toast.LENGTH_SHORT).show();
        return false;
    }
    @OnLongClick(R.id.lbl_pw)
    public boolean onLongClickPW() {
        Toast.makeText(this,"Perilous Wilds\nby Jason Lutes\nhttp://lampblackandbrimstone.blogspot.com/", Toast.LENGTH_SHORT).show();
        return false;
    }
    @OnLongClick(R.id.lbl_fotf)
    public boolean onLongClickFotF() {
        Toast.makeText(this,"Freebooters on the Frontier\nby Jason Lutes\nhttp://lampblackandbrimstone.blogspot.com/", Toast.LENGTH_SHORT).show();
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //mContext = this
        contentView = R.layout.activity_main


        ((AndyGenApp) getApplication()).inject(this);

        // This must be called for injection of views and callbacks to take place
        SwissKnife.inject this

        // This must be called for saved state restoring
        //SwissKnife.restoreState(this, savedInstanceState);

        // This mus be called for automatic parsing of intent extras
        SwissKnife.loadExtras(this)
    }
}
