package edu.pjwstk.ifpk.nocmuzeowapp;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.MultiProcessor;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;

import edu.pjwstk.ifpk.nocmuzeowapp.DTO.Hero;

public class ScannerPage extends Page{
    HeroAdapter heroes;

    private CameraSource mCameraSource;
    private CameraSourcePreview mPreview;
    private GraphicOverlay<BarcodeGraphic> mGraphicOverlay;

    TextView foundText;
    Button foundButton;
    LinearLayout foundLayout;
    ImageView foundImage;
    String foundName;
    private static final int RC_HANDLE_GMS = 9001;


    public ScannerPage(final Activity owner, final HeroAdapter heroes){
        ctx = owner;
        this.heroes = heroes;
        foundText = (TextView) ctx.findViewById(R.id.foundHeroText);
        foundButton = (Button) ctx.findViewById(R.id.foundHeroButton);
        foundLayout = (LinearLayout) ctx.findViewById(R.id.foundHeroLayout);
        foundImage = (ImageView) ctx.findViewById(R.id.foundHeroImage);
        foundButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                foundLayout.setVisibility(View.INVISIBLE);
                heroes.meetHero(ctx,foundName);
                ((MainActivity)ctx).changeSelectedPage(MainActivity.FLIP_HEROES,true); // true zapamiętuje stronę w backu
            }
        });
        mPreview = (CameraSourcePreview) ctx.findViewById(R.id.preview);
        mGraphicOverlay = (GraphicOverlay<BarcodeGraphic>) ctx.findViewById(R.id.graphicOverlay);
        createCameraSource(true, false,this);

    }
    public void trySetFoundHeroes(final String heroName){

        Log.e("trySetFoundHeroes",heroName);
        final Hero hero = heroes.getHero(heroName);
        if(hero == null||hero.istKnown()){
            return;
        }
        ctx.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                foundText.setText("Znalazłeś\r\n"+heroName);
                foundName=heroName;
                foundImage.setImageResource(hero.getDrawable());
                foundLayout.setVisibility(View.VISIBLE);
            }
        });
        Log.e("trySetFoundHeroes","znaleziono "+heroName);
    }

    private void createCameraSource(boolean autoFocus, boolean useFlash, ScannerPage scannerPage) {
        Context context = ctx.getApplicationContext();
        BarcodeDetector barcodeDetector = new BarcodeDetector.Builder(context).build();
        BarcodeTrackerFactory barcodeFactory = new BarcodeTrackerFactory(mGraphicOverlay);
        barcodeFactory.setScannerPage(scannerPage);
        barcodeDetector.setProcessor(
                new MultiProcessor.Builder<>(barcodeFactory).build());

        CameraSource.Builder builder = new CameraSource.Builder(ctx.getApplicationContext(), barcodeDetector)
                .setFacing(CameraSource.CAMERA_FACING_BACK)
                .setRequestedPreviewSize(1600, 1024)
                .setAutoFocusEnabled(true)
                .setRequestedFps(10.0f);
        mCameraSource = builder.build();
    }
    public void startCameraSource() throws SecurityException {
        // check that the device has play services available.
        int code = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(
                ctx.getApplicationContext());
        if (code != ConnectionResult.SUCCESS) {
            Dialog dlg =
                    GoogleApiAvailability.getInstance().getErrorDialog(ctx, code, RC_HANDLE_GMS);
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
    void onPageEnter() {
        startCameraSource();
        mPreview.setVisibility(View.VISIBLE);
       // mPreview.setZ
    }

    @Override
    void onPageExit() {
        mPreview.stop();
        mPreview.setVisibility(View.GONE);
    }

    @Override
    void onPause() {
        if (mPreview != null) {
            mPreview.stop();
        }
    }

    @Override
    void onResume() {
        startCameraSource();

    }

    @Override
    void onDestroy() {
        if (mPreview != null) {
            mPreview.release();
        }
    }
}
