package com.example.xermart.CartProducts;

import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static String DB_NAME = "Carttable";
    static final int DB_VERSION = 2;

    static final String TABLE_NAME = "Products";
    public static final String ID = "_id";
    public static final String ITEM = "item";
    public static final String QUANTITY = "quantity";
    public static final String ITEM_IMAGE = "image_url";
    public static final String TOTAL_AMOUNT = "total";


    static final String CREATE_DB_QUERY = "CREATE TABLE " + TABLE_NAME
            + " (" + ID + "INTEGER PRIMARY KEY AUTOINCREMENT, " + ITEM + " TEXT NOT NULL, " +
            QUANTITY + " INTEGER NOT NULL, " + TOTAL_AMOUNT + " INTEGER NOT NULL, " + ITEM_IMAGE + " TEXT NOT NULL " + ");";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(android.database.sqlite.SQLiteDatabase db) {
        db.execSQL(CREATE_DB_QUERY);
    }

    @Override
    public void onUpgrade(android.database.sqlite.SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(" DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
