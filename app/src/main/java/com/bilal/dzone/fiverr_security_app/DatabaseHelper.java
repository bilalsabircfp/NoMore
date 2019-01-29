package com.bilal.dzone.fiverr_security_app;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Belal on 1/27/2017.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    //Constants for Database name, table name, and column names

    public static final String DB_NAME = "NamesDB";
    public static final String TABLE_NAME = "survey";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_NUM = "num";
    public static final String COLUMN_STATUS = "status";


    public static final String[] COL_NAME = {"name", "num"};

    //database version
    private static final int DB_VERSION = 1;

    //Constructor
    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    //creating the database
    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE " + TABLE_NAME
                + "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_NAME + " VARCHAR,  " +
                COLUMN_NUM + " VARCHAR,  " + COLUMN_STATUS + " INTEGER );";
        db.execSQL(sql);
    }

    //upgrading the database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        String sql = "DROP TABLE IF EXISTS Persons";
//        db.execSQL(sql);
//        onCreate(db);
    }

    /*
    * This method is taking two arguments
    * first one is the name that is to be saved
    * second one is the status
    * 0 means the name is synced with the server
    * 1 means the name is not synced with the server
    * */
    public boolean addName(String name, String num) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COLUMN_NAME, name);
        contentValues.put(COLUMN_NUM, num);
        contentValues.put(COLUMN_STATUS, 0);

        db.insert(TABLE_NAME, null, contentValues);
        db.close();
        return true;
    }



    public Cursor getNames() {

        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_STATUS + " = 0 ;";
        Cursor c = db.rawQuery(sql, null);
        return c;
    }


    public boolean updateNameStatus(int id, int status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_STATUS, status);
        db.update(TABLE_NAME, contentValues, COLUMN_ID + "=" + id, null);
        db.close();
        return true;
    }


}