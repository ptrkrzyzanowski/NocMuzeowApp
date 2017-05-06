package edu.pjwstk.ifpk.nocmuzeowapp;

import android.app.Activity;


public abstract class Page {
    Activity ctx;
    abstract void onPageEnter();
    abstract void onPageExit();
    void onPause(){};
    void onResume(){};
    void onDestroy(){};
}
