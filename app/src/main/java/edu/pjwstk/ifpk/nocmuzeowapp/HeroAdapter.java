package edu.pjwstk.ifpk.nocmuzeowapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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
import edu.pjwstk.ifpk.nocmuzeowapp.DTO.Riddle;
import edu.pjwstk.ifpk.nocmuzeowapp.DTO.RiddleType;

class HeroAdapter extends BaseAdapter {
    private Context context;
    private List<Hero> heroes = new ArrayList<>();
    private List<Riddle> riddles = new ArrayList<>();

    public HeroAdapter(Context ctx) {
        context=ctx;
        loadCSV();
        loadPreferences(ctx);
        cleanRiddles();
    }

    private void cleanRiddles() {
        List<Riddle> deleted = new ArrayList<>();
        for(Riddle r:riddles){
            if(r.getHero().istKnown()==true){
                deleted.add(r);
            }
        }
        for(Riddle d:deleted){
            riddles.remove(d);
        }
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
                insertedCount++;
            }
            inStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        inStream = context.getResources().openRawResource(R.raw.riddles);
        if(inStream==null){
            Toast.makeText(context, "cannot load riddle.csv", Toast.LENGTH_SHORT).show();
        }
        buffer = new BufferedReader(new InputStreamReader(inStream));
        try {
            Hero hero = null;
            while ((line = buffer.readLine()) != null) {
                String[] colums = line.split(";");
                if (colums.length != 4) {
                    Log.d("CSVParser", "Skipping Bad CSV Row");
                    continue;
                }
                Resources resources = context.getResources();
                hero = this.getHero(colums[0]);
                if(hero!=null){
                    RiddleType type = RiddleType.getType(colums[2]);
                    if(type==RiddleType.RT_TEXT)
                        riddles.add(new Riddle(hero,colums[1],RiddleType.RT_TEXT,colums[3],0));
                    if(type==RiddleType.RT_IMAGE) {
                        final int resourceID = resources.getIdentifier(colums[3].trim(), "drawable",
                                context.getPackageName());
                        Log.e("loadRiddle",""+resourceID);
                        riddles.add(new Riddle(hero, colums[1], RiddleType.RT_IMAGE, "",resourceID));
                    }
                    if(type==RiddleType.RT_MOVIE)
                        riddles.add(new Riddle(hero,colums[1],RiddleType.RT_MOVIE,colums[3],0));
                }
            }
            inStream.close();
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
                cleanRiddles();
            }

        }
    }
    public int getFirstUnknownHeroId(){
        if(riddles.size()!=0)
            return riddles.get(0).getHero().getId();
        return -1;
    }
    public Riddle getFirstUnknownRiddle(){
        if(riddles.size()!=0)
            return riddles.get(0);
        return null;
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
        textView.setTypeface(((MainActivity)context).getTypeface());
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
    public  List<Riddle> getRiddles() {return riddles;}
}
