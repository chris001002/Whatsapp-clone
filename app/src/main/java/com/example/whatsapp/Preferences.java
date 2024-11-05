package com.example.whatsapp;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;

import java.util.ConcurrentModificationException;

public class Preferences {
    Preferences(){}
    static final String KEY_DARKMODE = "dark_mode";
    static final String KEY_USERID = "user_id";
    private static SharedPreferences getSharedPreference(Context context){
    return  PreferenceManager.getDefaultSharedPreferences(context);
    }
    public static void setDarkMode(Context context, Boolean darkMode){
        SharedPreferences.Editor editor = getSharedPreference(context).edit();
        editor.putBoolean(KEY_DARKMODE, darkMode);
        editor.apply();
    }
    public static Boolean getDarkMode(Context context){
        return getSharedPreference(context).getBoolean(KEY_DARKMODE, false);
    }
    public static void setUserId(Context context, int userId){
        SharedPreferences.Editor editor = getSharedPreference(context).edit();
        editor.putInt(KEY_USERID, userId);
        editor.apply();
    }
    public static int getUserId (Context context){
        return getSharedPreference(context).getInt(KEY_USERID, -1);
    }
}