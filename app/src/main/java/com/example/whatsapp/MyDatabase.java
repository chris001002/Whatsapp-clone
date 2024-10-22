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
    private static final String DATABASE_NAME = "contacts.db"; //Define the name of database file
    private static final int DATABASE_VERSION = 2; //Set the version number of the database
    private  static final String TABLE_NAME = "contacts"; //Define the name of table within database
    private SQLiteDatabase mySQLite; //A variable used to hold reference for database when is opened or created allowing CRUD operations
    public MyDatabase(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    } //Constructor to setup database and to allow the class to manage database operations

    //Method below is used to create table for contacts
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) { //Method is called when database first created

        //SQL command to create table named contacts(if not exist) with three columns id, userid and isFavorite
        String sql = "CREATE TABLE IF NOT EXISTS "+TABLE_NAME+" (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "userId VARCHAR(255)," +
                "isFavorite BOOLEAN"+
                ") ";
        sqLiteDatabase.execSQL(sql); //To execute or run the SQL Query to create the table
    }

    //Method called when database need to be upgraded
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase /*SQLite database instance*/, int i /*old version*/, int i1/*New version*/) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME); //Deletes contact table if it exist
        onCreate(sqLiteDatabase); //Method to recreate table with updated schema and re-initialize the database.
    }

    //Method inserts a new user into the contact
    public void addUser(String uid){
        ContentValues contentValues = new ContentValues(); //key value map used to store the values that will be inserted to database
        contentValues.put("userId", uid); //Puts userId into ContentValues object which then will be stored in the userId column in the contacts table
        contentValues.put("isFavorite", false); //Set new users not marked as favorite by default
        mySQLite = this.getWritableDatabase(); //Open database in writable mode allowing insertion or update of data
        mySQLite.insert(TABLE_NAME, null, contentValues); //Inserting ContentValues into a table without any special behavior for inserting rows
    }

    //This method retrieves all userId values from contacts table and return them in an ArrayList
    public ArrayList<String> getContacts(){
        String sql = "SELECT userId FROM " + TABLE_NAME;//Selects userId column from contacts table
        mySQLite = this.getReadableDatabase(); //Opens database in readable mode for read-only operations
        ArrayList<String>usersId = new ArrayList<>(); //Initialize empty ArrayList to hold the list of user IDs retrieved from DataBase
        Cursor cursor = mySQLite.rawQuery(sql, null); //Execute SQL query and returns a cursor allowing access to the rows returned by the query
        if (cursor.moveToFirst()){ //Checks if query returned any result
            do { //A loop to iterate through all the rows in result set.
                String userid = cursor.getString(0); //Retrieves the value of first column of the current row  userId)
                usersId.add(userid); //Add retrieved userId to the ArrayList
            } while (cursor.moveToNext());
        }
        cursor.close(); //Close cursor to free up resources after query operation is complete
        return usersId; //return the list of user ID's
    }

    //Method to retrieve all contacts marked as favourite
    public ArrayList<String> getFavoriteContacts(){ //used to return a list of contacts userId
        String []selectionArgs = {"1"}; //Used to find favorite contacts in the table by selecting rows where isFavorite = 1 (true)
        mySQLite = this.getReadableDatabase(); //Opens database in readable mode for read-only operations
        String []column = {"userId"}; //To specify which column should be retrieved
        //The cursor is a data structure that holds the result of database query
        Cursor cursor = mySQLite.query(TABLE_NAME, column,"isFavorite = ?", selectionArgs,null,null,null);
        ArrayList<String>usersId = new ArrayList<>(); //Initialize empty list to store userId
        if (cursor.moveToFirst()){ //Check if query returned any result
            do { //Loop iterating each row returned by query
                String userid = cursor.getString(0); //Retrieve the value of userId from current row
                usersId.add(userid); //Adds retrieved userId to the usersId list
            } while (cursor.moveToNext());
        }
        cursor.close(); //Close cursor to free up resource after query is complete
        return usersId; //Return the list of user id for the favorite contact
    }

    //Method returns a boolean indicating whether contact with given userId exists in database
    public boolean findContact(String uid){
        String []selectionArgs = {uid}; //Creates an array that holds userId to be used in query selection
        mySQLite = this.getReadableDatabase(); //Open database in readable mode
        String []column = {"userId"}; //Specifies that only the userid column is needed in query result

        //The cursor below executes a query to check if specified userId exist
        Cursor cursor = mySQLite.query(TABLE_NAME, column,"userid = ?", selectionArgs,null,null,null);
        if (cursor.moveToFirst())return true; //Checks if the cursor can move to the first row, if a contact with userId is found return true
        return false; //return false if no contact was found
    }

    //The method below update the isFavorite status of a contact
    public void setFavorite(String uid, boolean isFavorite){
        mySQLite = this.getWritableDatabase(); //Open database in writeable mode
        ContentValues values = new ContentValues(); //Create ContentValues object to hold the key-value pair that will be updated
        values.put("isFavorite", isFavorite); //add key value pair to ContentValue, set the isFavorite column to true or false value
        Log.d("aaa", "database called"); //Logs a debug message
        String[] selectionArgs = {uid}; //Creates an array that holds userId to be used in WHERE clause to update the query
        mySQLite.update(TABLE_NAME, values, "userid = ?", selectionArgs); //Executes an update to query to modify isFavorite column with matching userId
    }

    //This method checks whether a contact is marked as Favorite by returning 1(true) or 0(false)
    public int isFavorite(String uid){
        mySQLite = this.getWritableDatabase();//Opens database in writeable mode
        String []column = {"isFavorite"}; //specifies to only retrieve isFavorite column
        String []selectionArgs = {uid}; //creates an array that holds userId

        //Cursor executes query to retrieve isFavorite value
        Cursor cursor = mySQLite.query(TABLE_NAME, column, "userid = ?", selectionArgs, null, null, null);
        cursor.moveToFirst(); //Move cursor to first row of result
        return cursor.getInt(0); //Return value from the isFavorite column
    }

}
