package edu.pjwstk.ifpk.nocmuzeowapp;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.provider.SyncStateContract;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import edu.pjwstk.ifpk.nocmuzeowapp.database.Hero;

/**
 * Created by ptrkr on 28.04.2017.
 */

class HeroAdapter extends BaseAdapter {
    private Context context;
    private List<Hero> heroes = new ArrayList<>();

    public HeroAdapter(Context ctx) {
        context=ctx;
        loadCSV();
    }
    public int loadCSV(){
        int insertedCount = 0;

        InputStream inStream = null;
        inStream = context.getResources().openRawResource(R.raw.heroes);
        if(inStream==null){
            Toast.makeText(context, "cannot load heroes.csv", Toast.LENGTH_SHORT).show();
            return 0;
        }
        BufferedReader buffer = new BufferedReader(new InputStreamReader(inStream));
        String line = "";
        try {
            while ((line = buffer.readLine()) != null) {
                String[] colums = line.split(";");
                if (colums.length != 3) {
                    Log.d("CSVParser", "Skipping Bad CSV Row");
                    continue;
                }

                Resources resources = context.getResources();
                final int resourceId = resources.getIdentifier(colums[2].trim(), "drawable",
                        context.getPackageName());
                heroes.add(new Hero(insertedCount, colums[0].trim(),colums[1].trim(),colums[2].trim(),resourceId,insertedCount!=3));
                insertedCount++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return insertedCount;
    }
    @Override
    public int getCount() {
        return heroes.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = new ImageView(context);
            imageView.setLayoutParams(new GridView.LayoutParams( 200,200));
            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
//            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            imageView.setPadding(4, 4, 4, 4);
        } else {
            imageView = (ImageView) convertView;
        }
        if(heroes.get(position).istKnown()){
            imageView.setImageResource(heroes.get(position).getDrawable());
        }
        else{
            imageView.setImageResource(R.drawable.hero_unknown);
        }
        return imageView;
    }
    public  List<Hero> getHeroes(){
        return heroes;
    }

}
