package edu.pjwstk.ifpk.nocmuzeowapp;


import android.app.Activity;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import java.io.IOException;
import java.util.List;
import java.util.Random;

import edu.pjwstk.ifpk.nocmuzeowapp.DTO.Riddle;
import edu.pjwstk.ifpk.nocmuzeowapp.DTO.RiddleType;

public class RiddlePage extends Page {
    Riddle current;
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

        buttonBT = (Button)ctx.findViewById(R.id.riddle_button);
        buttonBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextRiddle();
            }
        });
    }
    @Override
    void onPageEnter() {
        nextRiddle();
    }

    @Override
    void onPageExit() {
    }
    void nextRiddle(){
        List<Riddle> riddles = heroes.getRiddles();
        if(riddles.size()==0){

        }else  if(riddles.size()==1){
            current = riddles.get(0);
        }else{
            Riddle random = riddles.get((new Random()).nextInt(riddles.size()));
            while(random == current)
                random = riddles.get((new Random()).nextInt(riddles.size()));
            current = random;
        }
        nameTV.setText(current.getName());

            descriptionTV.setText(current.getDescription());
    }
}
