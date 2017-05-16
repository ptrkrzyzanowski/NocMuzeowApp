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
                    ((MainActivity)owner).switchToDetails(heroes.getHeroes().get(position));
                    return;
                }
                if(position<7){
                    ((MainActivity)ctx).switchToRiddle(position);
                    return;
                }
                if(heroes.getUnresolvedRiddleCount()>1) {
                    Toast.makeText(owner, "rozwiąż najpierw wcześniejsze zagadki", Toast.LENGTH_SHORT).show();
                }
                else{
                    ((MainActivity)ctx).switchToRiddle(position);
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
