package com.example.whatsapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

public class MyDatabase extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "contacts.db";
    private static final int DATABASE_VERSION = 1;
    private  static final String TABLE_NAME = "contacts";
    private SQLiteDatabase mySQLite;
    MyDatabase(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sql = "CREATE TABLE IF NOT EXISTS "+TABLE_NAME+" (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "userId VARCHAR(255)" +
                ") ";
        sqLiteDatabase.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
    }
    public void addUser(String uid){
        ContentValues contentValues = new ContentValues();
        contentValues.put("userId", uid);
        mySQLite = this.getWritableDatabase();
        mySQLite.insert(TABLE_NAME, null, contentValues);
    }
    public List<String> getContacts(){
        String sql = "SELECT userId FROM " + TABLE_NAME;
        mySQLite = this.getReadableDatabase();
        List<String>usersId = new ArrayList<>();
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
}
