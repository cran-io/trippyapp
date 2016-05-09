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
    private static final String KEY_SESSIONOPEN = "session_open";
    private static final String KEY_USERNAME = "user_name";
    private static final String KEY_USERMAIL = "user_email";
    private static final String KEY_USERPIC = "user_picpath";
    private static final String KEY_RAINFRIENDLY = "rainfriendly";
    private static final String KEY_DURATION = "duration";
    private static final String KEY_LOCATION = "location";
    private static final String KEY_SORTING = "sorting";
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

    public void saveSessionOpen(Boolean sessionOpen) {
        sharedPrefs.edit().putBoolean(KEY_SESSIONOPEN, sessionOpen).apply();
    }

    public boolean getSessionOpen() {
        return sharedPrefs.getBoolean(KEY_SESSIONOPEN, false);
    }


    public void saveUsername(String name) {
        sharedPrefs.edit().putString(KEY_USERNAME, name).apply();
    }

    public void saveUserMail(String email) {
        sharedPrefs.edit().putString(KEY_USERMAIL, email).apply();
    }

    public void saveUserImage(String imagePath) {
        sharedPrefs.edit().putString(KEY_USERPIC, imagePath).apply();
    }

    public String getUsername() {
        return sharedPrefs.getString(KEY_USERNAME, "");
    }

    public String getUserEmail() {
        return sharedPrefs.getString(KEY_USERMAIL, "");
    }

    public String getImagePath() {
        return sharedPrefs.getString(KEY_USERPIC, "");
    }

    public void saveShowRainFriendlyTours(boolean showRainFriendlyTours) {
        sharedPrefs.edit().putBoolean(KEY_RAINFRIENDLY, showRainFriendlyTours).apply();
    }

    public boolean getShowFriendlyTours() {
        return sharedPrefs.getBoolean(KEY_RAINFRIENDLY, true);
    }

    public void saveTourDuration(String duration) {
        sharedPrefs.edit().putString(KEY_DURATION, duration).apply();
    }

    public String getTourDuration() {
        return sharedPrefs.getString(KEY_DURATION,"All day");
    }

    public String getTourLocation() {
        return sharedPrefs.getString(KEY_LOCATION, "All");
    }

    public void saveTourLocation(String location){
        sharedPrefs.edit().putString(KEY_LOCATION,location).apply();
    }

    public String getTourSorting(){
        return sharedPrefs.getString(KEY_SORTING,"none");
    }

    public void saveTourSorting(String sorting){
        sharedPrefs.edit().putString(KEY_SORTING, sorting).apply();
    }
}
