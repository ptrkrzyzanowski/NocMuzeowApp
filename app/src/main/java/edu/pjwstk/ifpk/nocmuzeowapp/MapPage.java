package edu.pjwstk.ifpk.nocmuzeowapp;

import android.app.Activity;
import android.webkit.WebView;

/**
 * Created by ptrkr on 06.05.2017.
 */

public class MapPage extends Page {
    MapPage(Activity act){
        ctx = act;
        WebView wv = (WebView) ctx.findViewById(R.id.mapwview);
        wv.getSettings().setBuiltInZoomControls(true);
        wv.setInitialScale(100);
        wv.loadUrl("file:///android_asset/mapa.png");
    }
    @Override
    void onPageEnter() {

    }

    @Override
    void onPageExit() {

    }
}
