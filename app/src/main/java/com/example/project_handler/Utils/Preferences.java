package com.example.project_handler.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class Preferences {
    SharedPreferences sharedPreferences;

    public Preferences(Activity activity) {
        this.sharedPreferences = activity.getSharedPreferences("Pref" ,Context.MODE_PRIVATE);
    }

    public String getSearch() {
        return sharedPreferences.getString("search", "papa");
    }

    public void setSearch(String search) {
        this.sharedPreferences.edit().putString("search", search).commit();
    }
}
