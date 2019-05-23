package com.example.treesapv2new.control;

import android.content.Context;
import android.preference.PreferenceManager;

import java.util.Date;

public class PrefManager {

    private static final String HOME = "home";
    static Context parentContext = null;

    public PrefManager() {
    }

    static public void setContext(Context parent) {
        parentContext = parent;
    }

    // date of schedule
    static public void saveDateString(String dateKey, String lastDate) {
        PreferenceManager.getDefaultSharedPreferences(parentContext).edit()
                .putString(dateKey, lastDate).apply();
    }

    static public String getDateString(String dateKey) {
        return PreferenceManager.getDefaultSharedPreferences(parentContext)
                .getString(dateKey, "");
    }

    static public void saveDate(String dateKey, Date lastDate) {
        String str = lastDate.toString();
        PreferenceManager.getDefaultSharedPreferences(parentContext).edit()
                .putString(dateKey, str).apply();
    }

    static public Date getDate(String dateKey) {
        String str = PreferenceManager.getDefaultSharedPreferences(
                parentContext).getString(dateKey, "");
        if (str == null)
            return null;
        else if (str.length() == 0)
            return null;
        else
            return new Date(str);
    }

    // anything else ... as a String
    static public void putString(String key, String value) {
        PreferenceManager.getDefaultSharedPreferences(parentContext).edit()
                .putString(key, value).apply();
    }

    static public String getString(String key, String defaultValue) {
        String str = PreferenceManager.getDefaultSharedPreferences(parentContext).getString(key, "");
        if (str == null) str = defaultValue;
        return str;
    }

    static public void putBoolean(String key, Boolean value) {
        PreferenceManager.getDefaultSharedPreferences(parentContext).edit()
                .putBoolean(key, value).apply();
    }

    static public Boolean getBoolean(String key, Boolean defaultValue) {
        Boolean b = PreferenceManager.getDefaultSharedPreferences(parentContext).getBoolean(key, defaultValue);
        return b;
    }

    static public void putInteger(String key, Integer value) {
        PreferenceManager.getDefaultSharedPreferences(parentContext).edit()
                .putInt(key, value).apply();
    }

    static public Integer getInteger(String key, Integer defaultValue) {
        Integer i = PreferenceManager.getDefaultSharedPreferences(parentContext).getInt(key, defaultValue);
        return i;
    }

    static public void putFloat(String key, Float value) {
        PreferenceManager.getDefaultSharedPreferences(parentContext).edit()
                .putFloat(key, value).apply();
    }

    static public Float getFloat(String key, Float defaultValue) {
        Float f = PreferenceManager.getDefaultSharedPreferences(parentContext).getFloat(key, defaultValue);
        return f;
    }
}

