package edu.pjwstk.ifpk.nocmuzeowapp;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridView;
import android.widget.ViewFlipper;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.MultiProcessor;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import edu.pjwstk.ifpk.nocmuzeowapp.DTO.Hero;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    //  ----------- Pages -------------------------------
    public static final int FLIP_CHALLENGE = 2;
    public  static final int FLIP_HEROES = 1;
    public  static final int FLIP_MAP = 3;
    public  static final int FLIP_DETAILS = 4;
    public  static final int FLIP_SCAN = 0;
    private int current_page = 0;

    private ScannerPage scannerPage = null;
    private HeroesPage heroesPage = null;
    private DetailsPage detailsPage = null;

    private final Map<Integer,Integer> pageMap = createPageMap() ;
    private Map<Integer,Integer> createPageMap(){
        Map<Integer,Integer> map = new HashMap<>();
        map.put( R.id.nav_heroes,FLIP_HEROES);
        map.put( R.id.nav_challange,FLIP_CHALLENGE);
        map.put( R.id.nav_map,FLIP_MAP);
        map.put( R.id.nav_scan,FLIP_SCAN);
        return map;
    }
    // ------------ Drawer --------------------------------

    ViewFlipper flipper;
    Menu menuDrawer;

    // ----------- Barcode ---------------------------------

    private CameraSource mCameraSource;
    private CameraSourcePreview mPreview;
    private GraphicOverlay<BarcodeGraphic> mGraphicOverlay;

    // --------------------------------------------------------------------------------
    // intent request code to handle updating play services if needed.
    private static final int RC_HANDLE_GMS = 9001;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("MainActivity","Content view set");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        flipper = (ViewFlipper) findViewById(R.id.vf);


        //scanner
        HeroAdapter heroes = new HeroAdapter(this);

        scannerPage = new ScannerPage(this,heroes);
        mPreview = (CameraSourcePreview) findViewById(R.id.preview);
        mGraphicOverlay = (GraphicOverlay<BarcodeGraphic>) findViewById(R.id.graphicOverlay);
        createCameraSource(true, false,scannerPage);


//        Toast.makeText(this, "loaded "+count+" heroes", Toast.LENGTH_SHORT).show();
        GridView gridview = (GridView) findViewById(R.id.heroesview);

        if(gridview==null) {
            Log.d("activity:init", "gridview == null");
        }
        flipper.setDisplayedChild(FLIP_HEROES);
        heroesPage = new HeroesPage(this,heroes);
        detailsPage = new DetailsPage(this);



    }
    private void createCameraSource(boolean autoFocus, boolean useFlash, ScannerPage scannerPage) {
        Context context = getApplicationContext();
        BarcodeDetector barcodeDetector = new BarcodeDetector.Builder(context).build();
        BarcodeTrackerFactory barcodeFactory = new BarcodeTrackerFactory(mGraphicOverlay);
        barcodeFactory.setScannerPage(scannerPage);
        barcodeDetector.setProcessor(
                new MultiProcessor.Builder<>(barcodeFactory).build());

        CameraSource.Builder builder = new CameraSource.Builder(getApplicationContext(), barcodeDetector)
                .setFacing(CameraSource.CAMERA_FACING_BACK)
                .setRequestedPreviewSize(1600, 1024)
                .setAutoFocusEnabled(true)
                .setRequestedFps(10.0f);
        mCameraSource = builder.build();
    }

    private void startCameraSource() throws SecurityException {
        // check that the device has play services available.
        int code = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(
                getApplicationContext());
        if (code != ConnectionResult.SUCCESS) {
            Dialog dlg =
                    GoogleApiAvailability.getInstance().getErrorDialog(this, code, RC_HANDLE_GMS);
            dlg.show();
        }

        if (mCameraSource != null) {
            try {
                mPreview.start(mCameraSource, mGraphicOverlay);
            } catch (IOException e) {
                mCameraSource.release();
                mCameraSource = null;
            }
        }
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
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
        changeSelectedPage(pageMap.get(id));

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    public void changeSelectedPage(int page){
        if(current_page == FLIP_SCAN && page!= FLIP_SCAN){
            //disable scan
        }
        current_page = page;
        if(page == FLIP_SCAN){
            //enable scan
        }
        flipper.setDisplayedChild(page);
    }

    @Override
    protected void onResume() {
        super.onResume();
        startCameraSource();
    }
    @Override
    protected void onPause() {
        super.onPause();
        if (mPreview != null) {
            mPreview.stop();
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPreview != null) {
            mPreview.release();
        }
    }

    public void swichToDetails(Hero hero) {
        detailsPage.setHero(hero);
        changeSelectedPage(FLIP_DETAILS);
    }
}
