package edu.pjwstk.ifpk.nocmuzeowapp;


import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import edu.pjwstk.ifpk.nocmuzeowapp.DTO.Hero;
import edu.pjwstk.ifpk.nocmuzeowapp.DTO.Riddle;

public class RiddlePage extends Page {
    HeroAdapter heroes;
    TextView nameTV;
    TextView descriptionTV;
    ImageView imageIV;
    Button buttonBT;


    RiddlePage(Activity activity,HeroAdapter heroes){
        ctx = activity;
        this.heroes = heroes;
        nameTV = (TextView)ctx.findViewById(R.id.riddle_name);
        descriptionTV = (TextView)ctx.findViewById(R.id.riddle_text);
        imageIV = (ImageView)ctx.findViewById(R.id.riddle_image);

    }
    @Override
    void onPageEnter() {
        updateRiddle();
    }

    @Override
    void onPageExit() {
    }
    void updateRiddle(){
        Riddle riddle  = heroes.getFirstUnknownRiddle();
        if(riddle!=null){
            nameTV.setText(riddle.getName());
            imageIV.setImageResource(riddle.getHero().getDetailsDrawable());
            descriptionTV.setText(riddle.getDescription());
        }
        else{
            nameTV.setText("Wszystkie zagadki rozwiązane");
            imageIV.setVisibility(View.INVISIBLE);
            descriptionTV.setText("");
        }

    }
}
