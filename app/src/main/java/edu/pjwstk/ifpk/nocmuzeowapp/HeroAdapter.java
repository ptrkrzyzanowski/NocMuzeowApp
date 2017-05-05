package edu.pjwstk.ifpk.nocmuzeowapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import edu.pjwstk.ifpk.nocmuzeowapp.DTO.Hero;

class HeroAdapter extends BaseAdapter {
    private Context context;
    private List<Hero> heroes = new ArrayList<>();

    public HeroAdapter(Context ctx) {
        context=ctx;
        loadCSV();
        loadPreferences(ctx);
    }
    private void loadCSV(){
        int insertedCount = 0;
        InputStream inStream = null;
        inStream = context.getResources().openRawResource(R.raw.heroes);
        if(inStream==null){
            Toast.makeText(context, "cannot load heroes.csv", Toast.LENGTH_SHORT).show();
        }
        BufferedReader buffer = new BufferedReader(new InputStreamReader(inStream));
        String line = "";
        try {
            while ((line = buffer.readLine()) != null) {
                String[] colums = line.split(";");
                if (colums.length != 4) {
                    Log.d("CSVParser", "Skipping Bad CSV Row");
                    continue;
                }

                Resources resources = context.getResources();
                final int resourceId = resources.getIdentifier(colums[2].trim(), "drawable",
                        context.getPackageName());
                final int detailsId = resources.getIdentifier(colums[3].trim(), "drawable",
                        context.getPackageName());
                heroes.add(new Hero(insertedCount, colums[0].trim(),colums[1].trim(),colums[2].trim(),resourceId,detailsId,false));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    private void loadPreferences(Context ctx){
        SharedPreferences sharedPref = ctx.getSharedPreferences("heroPref",Context.MODE_PRIVATE);
        for(Hero hero:heroes){
            if(sharedPref.getBoolean(hero.getName(),false)){
                hero.meet();
            }
        }
    }
    public void savePeferences(Context ctx){
        SharedPreferences sharedPref = ctx.getSharedPreferences("heroPref",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        for(Hero hero:heroes){
            editor.putBoolean(hero.getName(),hero.istKnown());
        }
        editor.commit();
    }
    public void meetHero(Context ctx,String heroName){
        Log.e("meetHero",heroName+" search");
        for(Hero hero:heroes) {
            if(hero.getName().equals(heroName)) {
                hero.meet();
                SharedPreferences sharedPref = ctx.getSharedPreferences("heroPref",Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putBoolean(heroName,true);
                editor.apply();
                Log.e("meetHero","found "+heroName+", data invalidated");
                this.notifyDataSetChanged();
            }

        }
    }
    public Hero getHero(String heroName){
        for(Hero hero:heroes) {
            Log.e("getHero",hero.getName()+": "+(hero.getName().equals(heroName)));
            if (hero.getName().equals(heroName)) {
                return hero;
            }
        }
        return null;
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
        View view;
        if (convertView == null) {            // if it's not recycled, initialize some attributes
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.grid_item, null);
        } else {
            view = convertView;
        }
        ImageView imageView = (ImageView) view.findViewById(R.id.grid_item_image);
        TextView textView = (TextView) view.findViewById(R.id.grid_item_text);
        if(heroes.get(position).istKnown()){
            imageView.setImageResource(heroes.get(position).getDrawable());
            textView.setText(heroes.get(position).getName());
        }
        else{
            imageView.setImageResource(R.drawable.hero_unknown);
            textView.setText("????");
        }
        return view;
    }

    public  List<Hero> getHeroes(){
        return heroes;
    }

}
