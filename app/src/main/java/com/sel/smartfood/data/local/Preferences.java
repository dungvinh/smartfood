package com.sel.smartfood.data.local;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

public class Preferences {
    // class quan ly shared preference
    private SharedPreferences sharedPreferences;
    public Preferences(Application application, String name){
        sharedPreferences = application.getSharedPreferences(name, Context.MODE_PRIVATE);
    }
    // luu trang thai dang nhap
    public void saveBooleanValue(String key, boolean value){
        sharedPreferences.edit().putBoolean(key, value).apply();
    }
    public void saveStringValue(String key, String value){
        sharedPreferences.edit().putString(key, value).apply();
    }
    public boolean getBooleanValue(String key){
        return sharedPreferences.getBoolean(key, false);
    }
    public String getStringValue(String key){
        return sharedPreferences.getString(key, "");
    }
}
