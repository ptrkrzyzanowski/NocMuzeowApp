package edu.pjwstk.ifpk.nocmuzeowapp;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import edu.pjwstk.ifpk.nocmuzeowapp.database.Hero;

/**
 * Created by ptrkr on 28.04.2017.
 */

public class ScannerPage {
    Activity ctx;
    HeroAdapter heroes;

    TextView foundText;
    Button foundButton;
    LinearLayout foundLayout;
    ImageView foundImage;
    String foundName;

    public ScannerPage(final Activity owner, final HeroAdapter heroes){
        ctx = owner;
        this.heroes = heroes;
        foundText = (TextView) ctx.findViewById(R.id.foundHeroText);
        foundButton = (Button) ctx.findViewById(R.id.foundHeroButton);
        foundLayout = (LinearLayout) ctx.findViewById(R.id.foundHeroLayout);
        foundImage = (ImageView) ctx.findViewById(R.id.foundHeroImage);
        foundButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                foundLayout.setVisibility(View.INVISIBLE);
                heroes.meetHero(ctx,foundName);
                ((MainActivity)ctx).changeSelectedPage(MainActivity.FLIP_HEROES);
            }
        });
    }
    public void setFoundVisibility(boolean visibility){
        foundLayout.setVisibility(visibility? View.VISIBLE:View.INVISIBLE);
    }
    public void trySetFoundHeroes(final String heroName){

        Log.e("trySetFoundHeroes",heroName);
        final Hero hero = heroes.getHero(heroName);
        if(hero == null){
            return;
        }
        ctx.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                foundText.setText("Znalazłeś\r\n"+heroName);
                foundName=heroName;
                foundImage.setImageResource(hero.getDrawable());
                foundLayout.setVisibility(View.VISIBLE);
            }
        });
        Log.e("trySetFoundHeroes","znaleziono "+heroName);
    }
}
