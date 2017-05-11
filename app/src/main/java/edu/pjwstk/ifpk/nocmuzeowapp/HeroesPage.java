package edu.pjwstk.ifpk.nocmuzeowapp;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;



public class HeroesPage extends Page{
    HeroAdapter heroes;

    public HeroesPage(final Activity owner, HeroAdapter adapter){
        ctx=owner;
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
                    if(position==heroes.getFirstUnknownHeroId()){
                        ((MainActivity)ctx).changeSelectedPage(MainActivity.FLIP_RIDDLE,true);
                    }
                    else {
                        Toast.makeText(owner, "rozwiąż najpierw wcześniejszą zagadkę: "+heroes.getFirstUnknownHeroId(), Toast.LENGTH_SHORT).show();
                    }
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
