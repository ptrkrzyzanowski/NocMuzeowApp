package edu.pjwstk.ifpk.nocmuzeowapp;

import android.app.Activity;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TabHost;

/**
 * Created by ptrkr on 06.05.2017.
 */

public class MapPage extends Page {

    Button riddleBttn, scannerBttn;
    TabHost tabHost;

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

        TabHost host = (TabHost) ctx.findViewById(R.id.tabHost);
        host.setup();

        //Tab 1
        TabHost.TabSpec spec = host.newTabSpec("Tab One");
        spec.setContent(R.id.tab1);
        spec.setIndicator("Tab One");
        host.addTab(spec);

        //Tab 2
        spec = host.newTabSpec("Tab Two");
        spec.setContent(R.id.tab2);
        spec.setIndicator("Tab Two");
        host.addTab(spec);

        //Tab 3
        spec = host.newTabSpec("Tab Three");
        spec.setContent(R.id.tab3);
        spec.setIndicator("Tab Three");
        host.addTab(spec);
    }
    @Override
    void onPageEnter() {

    }

    @Override
    void onPageExit() {

    }
}
