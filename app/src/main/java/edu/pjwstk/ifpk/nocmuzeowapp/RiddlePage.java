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
    VideoView videoVV;
    MediaController controller;
    Button buttonBT;


    RiddlePage(Activity activity,HeroAdapter heroes){
        ctx = activity;
        this.heroes = heroes;
        nameTV = (TextView)ctx.findViewById(R.id.riddle_name);
        descriptionTV = (TextView)ctx.findViewById(R.id.riddle_text);
        imageIV = (ImageView)ctx.findViewById(R.id.riddle_image);
        videoVV = (VideoView)ctx.findViewById(R.id.riddle_surface);
        controller = new MediaController(ctx);
        controller.setMediaPlayer(videoVV);
        videoVV.setMediaController(controller);

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
        videoVV.setZOrderMediaOverlay(true);
    }

    @Override
    void onPageExit() {
        videoVV.setVisibility(View.GONE);
        videoVV.setZOrderMediaOverlay(false);
    }
    void nextRiddle(){
        List<Riddle> riddles = heroes.getRiddles();
        if(riddles.size()==0){
            nameTV.setVisibility(View.GONE);
            descriptionTV.setVisibility(View.GONE);
            imageIV.setVisibility(View.GONE);
            videoVV.setVisibility(View.GONE);

        }else  if(riddles.size()==1){
            current = riddles.get(0);
        }else{
            Riddle random = riddles.get((new Random()).nextInt(riddles.size()));
            while(random == current)
                random = riddles.get((new Random()).nextInt(riddles.size()));
            current = random;
        }
        nameTV.setText(current.getName());

        imageIV.setVisibility(View.GONE);
        descriptionTV.setVisibility(View.GONE);
        videoVV.setVisibility(View.GONE);

        if(current.getType()== RiddleType.RT_TEXT){
            descriptionTV.setText(current.getDescription());
            descriptionTV.setVisibility(View.VISIBLE);
        }
        if(current.getType()==RiddleType.RT_IMAGE){
            imageIV.setImageResource(current.getResource());
            Log.e("nextRiddle",""+current.getResource());
            imageIV.setVisibility(View.VISIBLE);
        }
        if(current.getType()==RiddleType.RT_MOVIE){
            imageIV.setImageResource(0);
            videoVV.setVideoURI(Uri.parse("android.resource://" + ctx.getPackageName() + "/" + R.raw.secret));
            videoVV.requestFocus();
            videoVV.bringToFront();

            videoVV.start();
            videoVV.setVisibility(View.VISIBLE);

        }
    }
}
