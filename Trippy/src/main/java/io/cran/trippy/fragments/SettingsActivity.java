package io.cran.trippy.fragments;


import android.app.Application;
import android.os.Build;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import io.cran.trippy.R;
import io.cran.trippy.utils.AppPreferences;


public class SettingsActivity extends AppCompatPreferenceActivity {


    private static Application mApp;


    private static void bindPreferenceSummaryToValue(Preference preference) {
        // Set the listener to watch for value changes.
        preference.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);

        // Trigger the listener immediately with the preference's
        // current value.
      /** sBindPreferenceSummaryToValueListener.onPreferenceChange(preference,
                PreferenceManager
                        .getDefaultSharedPreferences(preference.getContext())
                        .getString(preference.getKey(), "")); **/
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupActionBar();
        addPreferencesFromResource(R.xml.preferences);
        mApp = getApplication();
        

       bindPreferenceSummaryToValue(findPreference("sortBy_list"));
        bindPreferenceSummaryToValue(findPreference("duration_list"));
        bindPreferenceSummaryToValue(findPreference("location_list"));
        bindPreferenceSummaryToValue(findPreference("weatherFriendly"));


    }


    /**
     * Set up the {@link android.app.ActionBar}, if the API is available.
     */
    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        LinearLayout root = (LinearLayout)findViewById(android.R.id.list).getParent().getParent().getParent();
        Toolbar bar = (Toolbar) LayoutInflater.from(this).inflate(R.layout.settings_toolbar, root, false);
        root.addView(bar, 0); // insert at top
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            bar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
    }

    private static Preference.OnPreferenceChangeListener sBindPreferenceSummaryToValueListener = new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object value) {
            String stringValue = value.toString();

            switch (preference.getTitle().toString()) {
                case ("Sort By"):
                    AppPreferences.instance(mApp).saveTourSorting(value.toString());
                    break;
                case ("Duration"):
                    AppPreferences.instance(mApp).saveTourDuration(value.toString());
                    break;
                case ("Location"):
                    AppPreferences.instance(mApp).saveTourLocation(value.toString());
                    break;
                case ("Weather: Rainy friend tours"):
                    if (value.toString().equals("true")) {
                        AppPreferences.instance(mApp).saveShowRainFriendlyTours(true);
                    } else {
                        AppPreferences.instance(mApp).saveShowRainFriendlyTours(false);
                    }
                    break;

            }


            if (preference instanceof ListPreference) {
                // For list preferences, look up the correct display value in
                // the preference's 'entries' list.
                ListPreference listPreference = (ListPreference) preference;
                int index = listPreference.findIndexOfValue(stringValue);

                // Set the summary to reflect the new value.
                preference.setSummary(
                        index >= 0
                                ? listPreference.getEntries()[index]
                                : null);

            } else {
                // For all other preferences, set the summary to the value's
                // simple string representation.
                preference.setSummary(stringValue);
            }
            return true;
        }
    };

}
