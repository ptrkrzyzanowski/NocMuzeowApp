package edu.pjwstk.ifpk.nocmuzeowapp;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;



public class HeroesPage extends Page{
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
                    ((MainActivity)owner).swichToDetails(heroes.getHeroes().get(position));
                }
                else{
                    Toast.makeText(owner, "nieznany bohater", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    void onPageEnter() {

    }

    @Override
    void onPageExit() {

    }
}
