package com.example.lahirufernando.skinsensor;

/**
 * Created by lahirufernando on 30/11/2017.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "bodyparts";

    // Contacts table name
    private static final String TABLE_PARTS = "parts";

    // Contacts Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_TIME = "time";
    private static final String KEY_DATE = "date";
    private static final String KEY_URL = "url";
    private static final String KEY_COMMENTS = "comments";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_PARTS + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_TIME+ " TEXT," + KEY_DATE+ " TEXT,"+ KEY_URL + " TEXT,"
                + KEY_COMMENTS + " TEXT" + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PARTS);

        // Create tables again
        onCreate(db);
    }

    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */

    // Adding new contact
    void addMetaData(MetaData data) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TIME, data.getTime());
        values.put(KEY_DATE, data.getDate());
        values.put(KEY_URL, data.getUrl());
        values.put(KEY_COMMENTS, data.getComments());

        // Inserting Row
        db.insert(TABLE_PARTS, null, values);
        db.close(); // Closing database connection
    }

    // Getting single contact
    MetaData getMetaData(String id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_PARTS, new String[] { KEY_ID,
                        KEY_TIME, KEY_DATE, KEY_URL, KEY_COMMENTS }, KEY_URL + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        MetaData metaData= new MetaData(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4));
        // return contact
        return metaData;
    }

   // Updating single contact
    public int updateMetaData(MetaData data) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TIME, data.getTime());
        values.put(KEY_DATE, data.getDate());
        values.put(KEY_URL, data.getUrl());
        values.put(KEY_COMMENTS, data.getComments());

        // updating row
        return db.update(TABLE_PARTS, values, KEY_ID + " = ?",
                new String[] { String.valueOf(data.getId()) });
    }

    // Deleting single contact
    public void deleteMetaData(MetaData data) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PARTS, KEY_ID + " = ?",
                new String[] { String.valueOf(data.getId()) });
        db.close();
    }
    public List<MetaData> getAllMetaData() {
        List<MetaData> dataList = new ArrayList<MetaData>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_PARTS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                MetaData data = new MetaData();
                data.setId(Integer.parseInt(cursor.getString(0)));
                data.setTime(cursor.getString(1));
                data.setDate(cursor.getString(2));
                data.setUrl(cursor.getString(3));
                data.setComments(cursor.getString(4));
                // Adding contact to list
                dataList.add(data);
            } while (cursor.moveToNext());
        }

        // return contact list
        return dataList;
    }


    // Getting contacts Count
    public int getMetaDataCount() {
        String countQuery = "SELECT  * FROM " + TABLE_PARTS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }

}