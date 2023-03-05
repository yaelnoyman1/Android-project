package com.example.tasklist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class DBManager {

    private DatabaseHelper dbHelper;
    private Context context;
    private SQLiteDatabase db;

    public DBManager(Context c) {
        context = c;
    }

    public DBManager open() throws SQLException {
        dbHelper = new DatabaseHelper(context);
        db = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();
    }

    public void insert(String name, String desc, String check, String urgent) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(DatabaseHelper.SUBJECT, name);
        contentValue.put(DatabaseHelper.DESC, desc);
        contentValue.put(DatabaseHelper.CHECK, check);
        contentValue.put(DatabaseHelper.URGENT, urgent);
        db.insert(DatabaseHelper.TABLE_NAME, null, contentValue);
    }

    public Cursor read() {
        String[] columns = DatabaseHelper.get_columns();
        Cursor cursor = db.query(DatabaseHelper.TABLE_NAME, columns, null, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public void delete(long _id) {
        db.delete(DatabaseHelper.TABLE_NAME, DatabaseHelper._ID + "=" + _id, null);
    }

    public int update(long _id, String title, String desc, String checkBoxChange, String urgent) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.SUBJECT, title);
        contentValues.put(DatabaseHelper.DESC, desc);
        contentValues.put(DatabaseHelper.CHECK, checkBoxChange);
        contentValues.put(DatabaseHelper.URGENT, urgent);
        return db.update(DatabaseHelper.TABLE_NAME, contentValues, DatabaseHelper._ID + " = " + _id, null);
    }

    public Cursor searchByDate(String date){
        String s = "SELECT * FROM "+ DatabaseHelper.TABLE_NAME + " WHERE "+  DatabaseHelper.DESC + " = " + "'"+ date +"'";
        Cursor cursor = db.rawQuery(s, null);
        cursor.moveToFirst();
        return cursor;
    }
}
