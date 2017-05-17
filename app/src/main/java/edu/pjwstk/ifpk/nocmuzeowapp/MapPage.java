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
        WebView wv2 = (WebView) ctx.findViewById(R.id.mapwview2);
        WebView wv3 = (WebView) ctx.findViewById(R.id.mapwview3);
        wv.getSettings().setBuiltInZoomControls(true);
        wv2.getSettings().setBuiltInZoomControls(true);
        wv3.getSettings().setBuiltInZoomControls(true);

        wv.setInitialScale(1);
        wv.getSettings().setLoadWithOverviewMode(true);
        wv.getSettings().setUseWideViewPort(true);

        wv2.setInitialScale(1);
        wv2.getSettings().setLoadWithOverviewMode(true);
        wv2.getSettings().setUseWideViewPort(true);

        wv3.setInitialScale(1);
        wv3.getSettings().setLoadWithOverviewMode(true);
        wv3.getSettings().setUseWideViewPort(true);

       // wv.loadUrl("file:///android_asset/mapa1.png");
      //  wv2.loadUrl("file:///android_asset/mapa2.png");
      //  wv3.loadUrl("file:///android_asset/mapa3.png");

        String htmlString = "<!DOCTYPE html><html><body style = \"text-align:center\"><img src=\"file:///android_asset/mapa1.png\" alt=\"mapa1\" width=\"100%\"></body></html>";
        wv.loadDataWithBaseURL(null, htmlString, "text/html", "UTF-8", null);
        String htmlString2 = "<!DOCTYPE html><html><body style = \"text-align:center\"><img src=\"file:///android_asset/mapa2.png\" alt=\"mapa2\" width=\"100%\"></body></html>";
        wv2.loadDataWithBaseURL(null, htmlString2, "text/html", "UTF-8", null);
        String htmlString3 = "<!DOCTYPE html><html><body style = \"text-align:center\"><img src=\"file:///android_asset/mapa3.png\" alt=\"mapa3\" width=\"100%\"></body></html>";
        wv3.loadDataWithBaseURL(null, htmlString3, "text/html", "UTF-8", null);

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
        spec.setIndicator("Parter bud.A"); //Parter bud. A
        host.addTab(spec);

        //Tab 2
        spec = host.newTabSpec("Tab Two");
        spec.setContent(R.id.tab2);
        spec.setIndicator("Piwnica bud.A"); // Piwnica bud. A
        host.addTab(spec);

        //Tab 3
        spec = host.newTabSpec("Tab Three");
        spec.setContent(R.id.tab3);
        spec.setIndicator("Piwnica bud.C"); // Piwnica bud. C
        host.addTab(spec);
    }
    @Override
    void onPageEnter() {

    }

    @Override
    void onPageExit() {

    }
}
