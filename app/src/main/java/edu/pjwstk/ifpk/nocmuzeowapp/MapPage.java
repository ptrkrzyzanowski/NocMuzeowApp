package edu.pjwstk.ifpk.nocmuzeowapp;

import android.app.Activity;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;

/**
 * Created by ptrkr on 06.05.2017.
 */

public class MapPage extends Page {

    Button riddleBttn, scannerBttn;

    MapPage(Activity act){
        ctx = act;
        WebView wv = (WebView) ctx.findViewById(R.id.mapwview);
        wv.getSettings().setBuiltInZoomControls(true);
        wv.setInitialScale(100);
        wv.loadUrl("file:///android_asset/mapa.png");

        riddleBttn = (Button)ctx.findViewById(R.id.riddleBttn);
        riddleBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)ctx).changeSelectedPage(MainActivity.FLIP_RIDDLE,true);
            }
        });
        scannerBttn = (Button)ctx.findViewById(R.id.scannerBttn);
        scannerBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)ctx).changeSelectedPage(MainActivity.FLIP_SCAN,true);
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
