package edu.pjwstk.ifpk.nocmuzeowapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import edu.pjwstk.ifpk.nocmuzeowapp.R;

/**
 * Created by ptrkr on 28.04.2017.
 */

public class DBHelper extends SQLiteOpenHelper {

    private static final  String CREATE_TABLE_HEROES = "";
    private static final String DB_NAME = "HeroesDatabase";
    private static final int DB_VERSION = 1;
    Context context;




    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE heroes (id INTEGER PRIMARY KEY, name TEXT, desc TEXT, imagename TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS heroes");
        onCreate(db);
    }

    public void addHero(String name,String description,String image){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name",name);
        values.put("desc",description);
        values.put("imagename",image);
        db.insert("heroes",null,values);
        db.close();
    }
    public Hero getHero(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("heroes", new String[] { "id",
                        "name", "desc","imagename"}, "id=?",
                new String[] { String.valueOf(id) },null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        Hero hero = new Hero(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2),cursor.getString(3));
        cursor.close();
        return hero;
    }
    public List<Hero> getHeroes(){
        List<Hero> heroes = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM heroes", null);
        if (cursor.moveToFirst()) {
            do {
                Hero hero = new Hero(Integer.parseInt(cursor.getString(0)),
                        cursor.getString(1), cursor.getString(2),cursor.getString(3));
                heroes.add(hero);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return heroes;
    }
    public int getHeroesCount(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM heroes", null);
        cursor.close();
        return cursor.getCount();
    }
    public int loadCSV(){
        int insertedCount = 0;

       // String heroesCSVfile = "raw/heroes.csv";
        InputStream inStream = null;
        inStream = context.getResources().openRawResource(R.raw.heroes);
        if(inStream==null){
            Toast.makeText(context, "cannot load heroes.csv", Toast.LENGTH_SHORT).show();

            return 0;
        }
        BufferedReader buffer = new BufferedReader(new InputStreamReader(inStream));
        String line = "";
        SQLiteDatabase db = this.getReadableDatabase();
        db.beginTransaction();
        try {
            while ((line = buffer.readLine()) != null) {
                String[] colums = line.split(";");
                if (colums.length != 3) {
                    Log.d("CSVParser", "Skipping Bad CSV Row");
                    continue;
                }
                ContentValues cv = new ContentValues(3);
                ContentValues values = new ContentValues();
                values.put("name",colums[0].trim());
                values.put("desc",colums[1].trim());
                values.put("imagename",colums[2].trim());
                db.insert("heroes",null,values);
                insertedCount++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
        return insertedCount;
    }
}
