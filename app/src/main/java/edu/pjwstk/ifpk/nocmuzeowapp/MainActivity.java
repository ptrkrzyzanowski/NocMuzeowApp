package edu.pjwstk.ifpk.nocmuzeowapp;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ViewFlipper;


import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

import edu.pjwstk.ifpk.nocmuzeowapp.DTO.Hero;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    //  ----------- Pages -------------------------------
    public static final int FLIP_RIDDLE = 2;
    public  static final int FLIP_HEROES = 1;
    public  static final int FLIP_MAP = 3;
    public  static final int FLIP_DETAILS = 4;
    public  static final int FLIP_SCAN = 0;
    private int current_page = FLIP_SCAN;

//    private DetailsPage detailsPage = null;

    private final Map<Integer,Integer> pageMap = createPageMap() ;
    private Map<Integer,Integer> createPageMap(){
        Map<Integer,Integer> map = new HashMap<>();
        map.put( R.id.nav_heroes,FLIP_HEROES);
        map.put( R.id.nav_challange,FLIP_RIDDLE);
        map.put( R.id.nav_map,FLIP_MAP);
        map.put( R.id.nav_scan,FLIP_SCAN);
        return map;
    }
    private Map<Integer,Page> pages = new HashMap<>();
    // ------------ Drawer --------------------------------

    private ViewFlipper flipper;
    private Menu menuDrawer;
    private Deque<Integer> navigationStack = new ArrayDeque<Integer>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("PJHero");

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        flipper = (ViewFlipper) findViewById(R.id.vf);


        HeroAdapter heroes = new HeroAdapter(this);

        pages.put(FLIP_SCAN,new ScannerPage(this,heroes));
        pages.put(FLIP_HEROES,new HeroesPage(this,heroes));
        pages.put(FLIP_DETAILS,new DetailsPage(this));
        pages.put(FLIP_MAP,new MapPage(this));
        pages.put(FLIP_RIDDLE,new RiddlePage(this,heroes));
    }



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
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
        menuDrawer = menu;
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

    public void swichToDetails(Hero hero) {
        ((DetailsPage)pages.get(FLIP_DETAILS)).setHero(hero);
        changeSelectedPage(FLIP_DETAILS,true);
    }
}
