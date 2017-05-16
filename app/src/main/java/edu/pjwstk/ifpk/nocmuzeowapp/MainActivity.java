package edu.pjwstk.ifpk.nocmuzeowapp;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;


import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.pjwstk.ifpk.nocmuzeowapp.DTO.Hero;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    //  ----------- Pages -------------------------------
    public static final int FLIP_RIDDLE = 3;
    public  static final int FLIP_HEROES = 2;
    public  static final int FLIP_MAP = 4;
    public  static final int FLIP_DETAILS = 5;
    public  static final int FLIP_SCAN = 1;
    public static final int FLIP_MAIN = 0;
    private int current_page = FLIP_MAIN;

    private final Map<Integer,Integer> pageMap = createPageMap() ;
    private Map<Integer,Integer> createPageMap(){
        Map<Integer,Integer> map = new HashMap<>();
        map.put( R.id.nav_heroes,FLIP_HEROES);
//        map.put( R.id.nav_challange,FLIP_RIDDLE);
        map.put( R.id.nav_map,FLIP_MAP);
        map.put( R.id.nav_scan,FLIP_SCAN);
        map.put( R.id.nav_main,FLIP_MAIN);
        return map;
    }
    private Map<Integer,Page> pages = new HashMap<>();
    // ------------ Drawer --------------------------------

    private ViewFlipper flipper;
    DrawerLayout drawer;
    NavigationView navigationView;
    private Deque<Integer> navigationStack = new ArrayDeque<>();
    Typeface typeface;
    private static final int CAMERA_PERM=11 ;
    HeroAdapter heroes;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        heroes = new HeroAdapter(this);

        setContentView(R.layout.activity_main);
        if(ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA},
                    CAMERA_PERM);
        }
        else{
            pages.put(FLIP_SCAN,new ScannerPage(this,heroes));
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("PJHero");

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        flipper = (ViewFlipper) findViewById(R.id.vf);




        pages.put(FLIP_HEROES,new HeroesPage(this,heroes));
        pages.put(FLIP_DETAILS,new DetailsPage(this));
        pages.put(FLIP_MAP,new MapPage(this));
        pages.put(FLIP_RIDDLE,new RiddlePage(this,heroes));
        pages.put(FLIP_MAIN, new MainPage(this));

        typeface = Typeface.createFromAsset(getAssets(),
                "fonts/OCR-A.ttf"); 
        setTypeface(typeface);
    }

    public Typeface getTypeface(){
        return typeface;
    }


    private List<View> getAllChildren(View v) {

        if (!(v instanceof ViewGroup)) {
            ArrayList<View> viewArrayList = new ArrayList<View>();
            viewArrayList.add(v);
            return viewArrayList;
        }

        ArrayList<View> result = new ArrayList<View>();
        ViewGroup viewGroup = (ViewGroup) v;
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            View child = viewGroup.getChildAt(i);
            result.addAll(getAllChildren(child));
        }
        return result;
    }
    private void applyFontToMenuItem(MenuItem mi) {
        SpannableString mNewTitle = new SpannableString(mi.getTitle());
        mNewTitle.setSpan(new CustomTypefaceSpan("" , typeface), 0 , mNewTitle.length(),  Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        mi.setTitle(mNewTitle);
    }

    private void setTypeface(Typeface tf) {
        List<View> views = getAllChildren(getWindow().getDecorView());
   //     views.addAll(getAllChildren(navigationView));
        for(View v:views){
            if(v instanceof TextView){
                Log.e("setTypeface","TV");
                ((TextView)v).setTypeface(tf);
            }
            if(v instanceof Button){
                ((Button)v).setTypeface(tf);
                Log.e("setTypeface","B");
            }
            Menu m = navigationView.getMenu();
            for (int i=0;i<m.size();i++) {
                MenuItem mi = m.getItem(i);

                //for applying a font to subMenu ...
                SubMenu subMenu = mi.getSubMenu();
                if (subMenu!=null && subMenu.size() >0 ) {
                    for (int j=0; j <subMenu.size();j++) {
                        MenuItem subMenuItem = subMenu.getItem(j);
                        applyFontToMenuItem(subMenuItem);
                    }
                }
                //the method we have create in activity
                applyFontToMenuItem(mi);
            }
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case CAMERA_PERM: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    pages.put(FLIP_SCAN,new ScannerPage(this,heroes));
                } else {
                    // permission denied, boo! Disable
                }
                return;
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if(navigationStack.isEmpty()!=true){
                changeSelectedPage(navigationStack.pop(),false);
            }
            else{
                super.onBackPressed();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            SharedPreferences sharedPref = getSharedPreferences("heroPref", Context.MODE_PRIVATE);// TODO: skasować z ostatecznej wersji. niebezpieczne
            sharedPref.edit().clear().apply();
            Intent i = getBaseContext().getPackageManager()
                    .getLaunchIntentForPackage( getBaseContext().getPackageName() );
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        changeSelectedPage(pageMap.get(id),true);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void changeSelectedPage(int page,boolean pushToNavigationStack){
        if(pushToNavigationStack==true)
            navigationStack.push(current_page);
        if(current_page == page)
            return;

        pages.get(page).onPageEnter();
        pages.get(current_page).onPageExit();
        current_page = page;
        flipper.setDisplayedChild(page);

    }

    @Override
    protected void onResume() {
        super.onResume();
        for(Page p:pages.values()){
            p.onResume();
        }
    }
    @Override
    protected void onPause() {
        super.onPause();
        for(Page p:pages.values()){
            p.onPause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        for(Page p:pages.values()){
            p.onDestroy();
        }
    }

    public void switchToDetails(Hero hero) {
        ((DetailsPage)pages.get(FLIP_DETAILS)).setHero(hero);
        changeSelectedPage(FLIP_DETAILS,true);
    }
    public void switchToRiddle(int heroid){
        ((RiddlePage)pages.get(FLIP_RIDDLE)).heroID = heroid;
        changeSelectedPage(FLIP_RIDDLE,true);
    }
}
