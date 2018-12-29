package com.lovepetapp.utils;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.lovepetapp.items.ItemAbout;
import com.lovepetapp.items.ItemRestaurant;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {

    private static String DB_NAME = "food.db";
    private final Context context;
    private SQLiteDatabase db;
    private String DB_PATH;
    private String REST_ID = "id";
    private String REST_NAME = "name";
    private String REST_IMAGE = "image";
    private String REST_TYPE = "type";
    private String REST_ADDRESS = "address";
    private String REST_AVG_RATING = "avgRating";
    private String REST_TOTAL_RATING = "totalRating";
    private String REST_CAT_NAME = "cat_name";

    public DBHelper(Context context) {
        super(context, DB_NAME, null, 1);
        this.context = context;
        DB_PATH = getReadableDatabase().getPath();
    }

    public void createDataBase() throws IOException {
        boolean dbExist = checkDataBase();
        if (!dbExist) {
            getReadableDatabase();
            copyDataBase();
        }
        createRestaurantTable();
    }

    private boolean checkDataBase() {
        File dbFile = new File(DB_PATH + DB_NAME);
        return dbFile.exists();
    }

    private void copyDataBase() throws IOException {

        InputStream myInput = context.getAssets().open(DB_NAME);
        String outFileName = DB_PATH + DB_NAME;
        OutputStream myOutput = new FileOutputStream(outFileName);
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }

        // Close the streams
        myOutput.flush();
        myOutput.close();
        myInput.close();
    }

    private Cursor getData(String Query) {
        String myPath = DB_PATH + DB_NAME;
        Cursor c = null;
        try {
            db = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
            c = db.rawQuery(Query, null);
        } catch (Exception e) {
            Log.e("Err", e.toString());
        }
        return c;
    }

    private void dml(String Query) {
        String myPath = DB_PATH + DB_NAME;
        if (db == null)
            db = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
        try {
            db.execSQL(Query);
        } catch (Exception e) {
            Log.e("Error", e.toString());
        }
    }





    public ArrayList<ItemRestaurant> getRestaurant() {
        ArrayList<ItemRestaurant> arrayList = new ArrayList<>();
        String selectQuery = "SELECT * FROM rest";

        Cursor c = getData(selectQuery);

        if (c != null && c.getCount() > 0) {
            c.moveToFirst();
            for (int i = 0; i < c.getCount(); i++) {
                String id = c.getString(c.getColumnIndex(REST_ID));
                String name = c.getString(c.getColumnIndex(REST_NAME));
                String image = c.getString(c.getColumnIndex(REST_IMAGE));
                String type = c.getString(c.getColumnIndex(REST_TYPE));
                String address = c.getString(c.getColumnIndex(REST_ADDRESS));
                float avgRating = Float.parseFloat(c.getString(c.getColumnIndex(REST_AVG_RATING)));
                int totalRating = Integer.parseInt(c.getString(c.getColumnIndex(REST_TOTAL_RATING)));
                String cname = c.getString(c.getColumnIndex(REST_CAT_NAME));

                ItemRestaurant itemRestaurant = new ItemRestaurant(id,name,image,type,address,avgRating,totalRating,cname);
                arrayList.add(itemRestaurant);

                c.moveToNext();
            }
            c.close();
        }
        return arrayList;
    }



    private void removeFromFavourite(String id) {
        try {
            dml("delete from rest where id="+id);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Boolean isFav(String id) {
        String selectQuery = "SELECT * FROM rest where " + REST_ID + "=" + id;
        Cursor c = getData(selectQuery);
        return c != null && c.getCount() > 0;
    }

    private void createRestaurantTable() {
        String createTable = "CREATE TABLE rest (" + REST_ID + " TEXT , " + REST_NAME + " TEXT , " +
                REST_IMAGE + " TEXT , " + REST_TYPE + " TEXT , " + REST_ADDRESS + " TEXT , " + REST_AVG_RATING + " TEXT , " +
                REST_TOTAL_RATING + " TEXT , " + REST_CAT_NAME + " TEXT" +" )";
        Log.e("aaa",createTable);
        dml(createTable);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}  