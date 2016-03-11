package io.cran.trippy;

import android.app.Application;

import com.parse.Parse;

/**
 * Created by MariaSol on 11/03/2016.
 */
public class TrippyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Parse.enableLocalDatastore(this);

        Parse.initialize(this);
    }
}
