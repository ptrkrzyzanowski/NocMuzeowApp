package edu.pjwstk.ifpk.nocmuzeowapp;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * Created by ptrkr on 28.04.2017.
 */

public class HeroesPage {
    HeroAdapter heroes;
    Activity owner;

    public HeroesPage(final Activity owner, HeroAdapter adapter){
        this.owner=owner;
        GridView gridview = (GridView) owner.findViewById(R.id.heroesview);

        if(gridview==null) {
            Log.d("heroes page:init", "gridview == null");
        }
        heroes = adapter;
        gridview.setAdapter(heroes);
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {

                if(heroes.getHeroes().get(position).istKnown()){
                    Toast.makeText(owner, "" + position, Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(owner, "unknown", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
