package edu.pjwstk.ifpk.nocmuzeowapp;

import android.app.Activity;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;

public class MainPage extends Page {
    MainPage(Activity act){
        ctx = act;
        Button startBttn = (Button) ctx.findViewById(R.id.startBttn);
        startBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)ctx).changeSelectedPage(MainActivity.FLIP_HEROES,true);
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
