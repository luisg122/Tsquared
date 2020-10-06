package com.example.tsquared.SharedPreference;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class DarkSharedPref {
    public static Boolean isDark = false;

    public DarkSharedPref() {}

    // this method will save the nightMode state: True or False
    public static void setNightModeState(Boolean state, Context context){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("NightMode", state);
        editor.apply();
    }

    // this method will load the Night Mode State
    public static Boolean loadNightModeState(Context context){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getBoolean("NightMode", false);
    }
}
