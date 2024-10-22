package com.example.whatsapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.whatsapp.Models.Message;
import com.example.whatsapp.Models.Users;

import java.util.ArrayList;
import java.util.Currency;
import java.util.ArrayList;

public class MyDatabase extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "contacts.db";
    private static final int DATABASE_VERSION = 8;
    private  static final String TABLE_NAME = "contacts";
    private  static final String TABLE_NAME2 = "account";
    private static final  String TABLE_NAME3 = "chats";
    private SQLiteDatabase mySQLite;
    public MyDatabase(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sql = "CREATE TABLE IF NOT EXISTS "+TABLE_NAME+" (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "contactOwner INT," +
                "userId INT,"+
                "isFavorite BOOL DEFAULT 0"+
                ") ";
        sqLiteDatabase.execSQL(sql);
        String sql2 = "CREATE TABLE IF NOT EXISTS "+TABLE_NAME2+" (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "email VARCHAR(255)," +
                "password VARCHAR(255),"+
                "userName VARCHAR(255)" +
                ")";
        sqLiteDatabase.execSQL(sql2);
        String sql3 = "CREATE TABLE IF NOT EXISTS "+TABLE_NAME3+" (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "senderId INT," +
                "receiverId INT,"+
                "message VARCHAR(255),"+
                "Timestamp DATETIME DEFAULT CURRENT_TIMESTAMP"+
                ")";
        sqLiteDatabase.execSQL(sql3);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME2);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME3);
        onCreate(sqLiteDatabase);
    }
    public void addContact(int ownerId, int userId){
        mySQLite = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("contactOwner", ownerId);
        contentValues.put("userId", userId);
        mySQLite.insert(TABLE_NAME,null, contentValues);
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
    public ArrayList<Integer> getFavoriteContacts(int ownerId){
        String []selectionArgs = {String.valueOf(ownerId), "1"};
        mySQLite = this.getReadableDatabase();
        String []column = {"userId"};
        Cursor cursor = mySQLite.query(TABLE_NAME, column,"contactOwner = ? AND isFavorite = ?", selectionArgs,null,null,null);
        ArrayList<Integer>usersId = new ArrayList<>();
        if (cursor.moveToFirst()){
            do {
                int userId = cursor.getInt(0);
                usersId.add(userId);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return usersId;
    }

    public boolean findContact(int ownerId,int userId){
        String []selectionArgs = {String.valueOf(ownerId), String.valueOf(userId)};
        mySQLite = this.getReadableDatabase();
        String []column = {"id"};
        Cursor cursor = mySQLite.query(TABLE_NAME, column,"contactOwner = ? AND userId = ?", selectionArgs,null,null,null);
        if (cursor.moveToFirst())return true;
        return false;
    }

    public void setFavorite(int ownerId,int uid, boolean isFavorite){
        mySQLite = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("isFavorite", isFavorite);
        Log.d("aaa", "database called");
        String[] selectionArgs = {String.valueOf(ownerId), String.valueOf(uid)};
        mySQLite.update(TABLE_NAME, values,"contactOwner = ? AND userid = ?", selectionArgs);
    }
    public void createUser(Users user){
        mySQLite = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("email", user.getEmail());
        contentValues.put("password", user.getPassword());
        contentValues.put("userName", user.getUserName());
        mySQLite.insert(TABLE_NAME2,null, contentValues);
    }
    public boolean userExists(String email){
        String []selectionArgs = {email};
        mySQLite = this.getReadableDatabase();
        String []column = {"email"};
        Cursor cursor = mySQLite.query(TABLE_NAME2, column,"email = ?", selectionArgs,null,null,null);
        if (cursor.moveToFirst()) {
            cursor.close();
            return true;
        }
        cursor.close();
        return false;
    }
    public int login(String email, String password){
        String []selectionArgs = {email, password};
        mySQLite = this.getReadableDatabase();
        String []column = {"id"};
        Cursor cursor = mySQLite.query(TABLE_NAME2, column,"email = ? AND password = ?", selectionArgs,null,null,null);
        if (cursor!=null &&cursor.moveToFirst()) {
            Log.d("userid", String.valueOf(cursor.getInt(0)));
            return cursor.getInt(0);
        }
        return -1;
    }
    public Users getUser(String email){
        String []selectionArgs = {email};
        mySQLite = this.getReadableDatabase();
        String []column = {"id", "userName"};
        Cursor cursor = mySQLite.query(TABLE_NAME2, column,"email = ?", selectionArgs,null,null,null);
        Users user = new Users();
        if (cursor!=null &&cursor.moveToFirst()) {
            user.setUserId(cursor.getInt(0));
            user.setUserName(cursor.getString(1));
            return user;
        }
        user.setUserId(-1);
        return user;
    }
    public Users getUser(int id){
        String []selectionArgs = {String.valueOf(id)};
        mySQLite = this.getReadableDatabase();
        String []column = {"id", "userName"};
        Cursor cursor = mySQLite.query(TABLE_NAME2, column,"id = ?", selectionArgs,null,null,null);
        Users user = new Users();
        if (cursor!=null &&cursor.moveToFirst()) {
            user.setUserId(cursor.getInt(0));
            user.setUserName(cursor.getString(1));
            return user;
        }
        user.setUserId(-1);
        return user;
    }
    public ArrayList<Users> getAllContacts(int ownerId){
        ArrayList<Users> users = new ArrayList<>();
        mySQLite = this.getReadableDatabase();
        String  []selectionArgs = {String.valueOf(ownerId)};
        String []column = {"userId", "isFavorite"};
        Cursor cursor = mySQLite.query(TABLE_NAME, column, "contactOwner = ?", selectionArgs, null, null, null);
        if(!cursor.moveToFirst())return users;
        do {
            Users user = getUser(cursor.getInt(0));
            user.setIsFavorite(cursor.getInt(1));
            user.setLastMessage(getLastMessage(ownerId, user.getUserId()));
            users.add(user);
        }while (cursor.moveToNext());
        return users;
    }
    public void createMessage(int receiverId,Message message){
        mySQLite = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("senderId", message.getUserId());
        contentValues.put("receiverId", receiverId);
        contentValues.put("message", message.getMessage());
        mySQLite.insert(TABLE_NAME3,null, contentValues);
    }
    public ArrayList<Message>  getMessages(int ownerId, int receiverId){
        ArrayList<Message> messages = new ArrayList<>();
        mySQLite = this.getReadableDatabase();
        String  []selectionArgs = {String.valueOf(ownerId), String.valueOf(receiverId)};
        String []column = {"senderId", "message", "Timestamp"};
        Cursor cursor = mySQLite.query(TABLE_NAME3, column, "senderId = ? OR senderId = ?", selectionArgs, null, null, "Timestamp ASC");
        if (!cursor.moveToFirst()) return messages;

        do {
            Message message = new Message();
            message.setUserId(cursor.getInt(0));
            message.setMessage(cursor.getString(1));
            message.setTimestamp(cursor.getString(2));
            messages.add(message);
        }while (cursor.moveToNext());
        return messages;
    }
    public String getLastMessage(int ownerId, int receiverId){
        mySQLite = this.getReadableDatabase();
        String  []selectionArgs = {String.valueOf(ownerId), String.valueOf(receiverId)};
        String []column = {"message"};
        Cursor cursor = mySQLite.query(TABLE_NAME3, column, "senderId = ? OR senderId = ?", selectionArgs, null, null, "Timestamp DESC");
        if(!cursor.moveToFirst())return "";
        return cursor.getString(0);
    }
    public void updateUsername(int userId, String userName){
        mySQLite = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("userName", userName);
        String[] selectionArgs = {String.valueOf(userId)};
        mySQLite.update(TABLE_NAME2, values,"id = ?", selectionArgs);
    }
}
