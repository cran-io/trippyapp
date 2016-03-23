package io.cran.trippy.utils;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.ArrayList;
import java.util.Set;

/**
 * Created by MariaSol on 21/03/2016.
 */
public class AppPreferences {
    private static final String KEY_TOURNAME = "tour_name";
    private static AppPreferences mInstance;
    private SharedPreferences sharedPrefs;

    public static AppPreferences instance(Application app) {
        if (mInstance == null) {
            mInstance = new AppPreferences(app);
        }
        return mInstance;
    }

    private AppPreferences(Application app) {
        this.sharedPrefs = PreferenceManager.getDefaultSharedPreferences(app);
    }

    public void saveFavouriteTours (Set<String> toursName){
        sharedPrefs.edit().putStringSet(KEY_TOURNAME,toursName).commit();
    }

    public Set<String> getFavouriteTours (){
        return sharedPrefs.getStringSet(KEY_TOURNAME,null);
    }

}
