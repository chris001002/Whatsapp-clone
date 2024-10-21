package com.example.whatsapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Currency;
import java.util.ArrayList;

public class MyDatabase extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "contacts.db";
    private static final int DATABASE_VERSION = 2;
    private  static final String TABLE_NAME = "contacts";
    private SQLiteDatabase mySQLite;
    public MyDatabase(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sql = "CREATE TABLE IF NOT EXISTS "+TABLE_NAME+" (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "userId VARCHAR(255)," +
                "isFavorite BOOLEAN"+
                ") ";
        sqLiteDatabase.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
    public void addUser(String uid){
        ContentValues contentValues = new ContentValues();
        contentValues.put("userId", uid);
        contentValues.put("isFavorite", false);
        mySQLite = this.getWritableDatabase();
        mySQLite.insert(TABLE_NAME, null, contentValues);
    }
    public ArrayList<String> getContacts(){
        String sql = "SELECT userId FROM " + TABLE_NAME;
        mySQLite = this.getReadableDatabase();
        ArrayList<String>usersId = new ArrayList<>();
        Cursor cursor = mySQLite.rawQuery(sql, null);
        if (cursor.moveToFirst()){
            do {
                String userid = cursor.getString(0);
                usersId.add(userid);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return usersId;
    }
    public ArrayList<String> getFavoriteContacts(){
        String []selectionArgs = {"1"};
        mySQLite = this.getReadableDatabase();
        String []column = {"userId"};
        Cursor cursor = mySQLite.query(TABLE_NAME, column,"isFavorite = ?", selectionArgs,null,null,null);
        ArrayList<String>usersId = new ArrayList<>();
        if (cursor.moveToFirst()){
            do {
                String userid = cursor.getString(0);
                usersId.add(userid);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return usersId;
    }
    public boolean findContact(String uid){
        String []selectionArgs = {uid};
        mySQLite = this.getReadableDatabase();
        String []column = {"userId"};
        Cursor cursor = mySQLite.query(TABLE_NAME, column,"userid = ?", selectionArgs,null,null,null);
        if (cursor.moveToFirst())return true;
        return false;
    }
    public void setFavorite(String uid, boolean isFavorite){
        mySQLite = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("isFavorite", isFavorite);
        Log.d("aaa", "database called");
        String[] selectionArgs = {uid};
        mySQLite.update(TABLE_NAME, values, "userid = ?", selectionArgs);
    }
    public int isFavorite(String uid){
        mySQLite = this.getWritableDatabase();
        String []column = {"isFavorite"};
        String []selectionArgs = {uid};
        Cursor cursor = mySQLite.query(TABLE_NAME, column, "userid = ?", selectionArgs, null, null, null);
        cursor.moveToFirst();
        return cursor.getInt(0);
    }

}
