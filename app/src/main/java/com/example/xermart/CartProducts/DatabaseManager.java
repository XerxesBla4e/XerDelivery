package com.example.xermart.CartProducts;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.SQLDataException;

public class DatabaseManager {
    Context context;
    DatabaseHelper databaseHelper;
    SQLiteDatabase sqLiteDatabase;

    public DatabaseManager(Context ctxt) {
        context = ctxt;
    }

    public DatabaseManager open() throws SQLDataException {
        databaseHelper = new DatabaseHelper(context);
        sqLiteDatabase = databaseHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        databaseHelper.close();
    }

    public int insert(String itemname, int quantity, int total, String img) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.ITEM, itemname);
        contentValues.put(DatabaseHelper.QUANTITY, quantity);
        contentValues.put(DatabaseHelper.TOTAL_AMOUNT, total);
        contentValues.put(DatabaseHelper.ITEM_IMAGE, img);
        int i = (int) sqLiteDatabase.insert(DatabaseHelper.TABLE_NAME, null, contentValues);
        return i;
    }

    public Cursor fetch(String itemname) {
        String[] items = new String[]{DatabaseHelper.ID, DatabaseHelper.ITEM, DatabaseHelper.QUANTITY, DatabaseHelper.TOTAL_AMOUNT};
        Cursor cursor = sqLiteDatabase.query(DatabaseHelper.TABLE_NAME, items, DatabaseHelper.ITEM + " = '" + itemname + "'", null, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public Cursor fetch() {
        String[] items = new String[]{DatabaseHelper.ID, DatabaseHelper.ITEM, DatabaseHelper.QUANTITY, DatabaseHelper.TOTAL_AMOUNT, DatabaseHelper.ITEM_IMAGE};
        Cursor cursor = sqLiteDatabase.query(DatabaseHelper.TABLE_NAME, items, null, null, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public int update(long _id, String itemname, int quantity, int total) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.ITEM, itemname);
        contentValues.put(DatabaseHelper.QUANTITY, quantity);
        contentValues.put(DatabaseHelper.TOTAL_AMOUNT, total);
        int f = sqLiteDatabase.update(DatabaseHelper.TABLE_NAME, contentValues, DatabaseHelper.ID + "=" + _id, null);
        return f;
    }

    public int delete(long id) {
        int r = sqLiteDatabase.delete(DatabaseHelper.TABLE_NAME, DatabaseHelper.ID + " = " + id, null);
        return r;
    }



    public void delete() {
        sqLiteDatabase.execSQL("delete from "+ DatabaseHelper.TABLE_NAME);
    }
}

