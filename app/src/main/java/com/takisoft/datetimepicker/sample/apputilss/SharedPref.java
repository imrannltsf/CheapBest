package com.takisoft.datetimepicker.sample.apputilss;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class SharedPref
{
    private static SharedPreferences mSharedPref;
  //  public static final String NAME = "NAME";
    //public static final String AGE = "AGE";
   // public static final String IS_SELECT = "IS_SELECT";
    public static final String User_ID="USER_ID";
    public static final String Access_Token="access-token";
    public static final String UID="uid";
    public static final String Client="client";
    public static final String UserEmail="User_Email";
    public static final String FBLogin="User_LoginFb";
    public static final String UserPassword="User_Password";
    public static final String IsLoginUser="Is_Login_User";
    private SharedPref()
    {

    }

    public static void init(Context context)
    {
        if(mSharedPref == null)
            mSharedPref = context.getSharedPreferences(context.getPackageName(), Activity.MODE_PRIVATE);
    }

    public static String read(String key, String defValue) {
        return mSharedPref.getString(key, defValue);
    }

    public static void write(String key, String value) {
        SharedPreferences.Editor prefsEditor = mSharedPref.edit();
        prefsEditor.putString(key, value);
        prefsEditor.apply();
    }

    public static boolean readBol(String key, boolean defValue) {
        return mSharedPref.getBoolean(key, defValue);
    }

    public static void write(String key, boolean value) {
        SharedPreferences.Editor prefsEditor = mSharedPref.edit();
        prefsEditor.putBoolean(key, value);
        prefsEditor.apply();
    }

    public static Integer read(String key, int defValue) {
        return mSharedPref.getInt(key, defValue);
    }

    public static void write(String key, Integer value) {
        SharedPreferences.Editor prefsEditor = mSharedPref.edit();
        prefsEditor.putInt(key, value).apply();
    }

    public static void removeallshared(){

     /*   SharedPreferences settings = context.getSharedPreferences(mSharedPref);
        settings.edit().clear().commit();*/
    }
}
