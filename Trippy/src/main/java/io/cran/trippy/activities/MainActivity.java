package io.cran.trippy.activities;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.appevents.AppEventsLogger;
import com.squareup.picasso.Picasso;

import bolts.AppLinks;
import io.cran.trippy.R;
import io.cran.trippy.fragments.FavouriteToursFragment;
import io.cran.trippy.fragments.SettingsActivity;
import io.cran.trippy.fragments.SettingsFragment;
import io.cran.trippy.fragments.TourDescription;
import io.cran.trippy.fragments.TourOwnerFragment;
import io.cran.trippy.fragments.ToursFragment;
import io.cran.trippy.utils.CircleImageView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ToursFragment.ToursFragmentListener, TourDescription.TourDescriptionListener {

    private FragmentManager mFm;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        Uri targetUrl = AppLinks.getTargetUrlFromInboundIntent(this, getIntent());
        if (targetUrl != null) {
            Log.i("Activity", "App Link Target URL: " + targetUrl.toString());
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerView =  navigationView.inflateHeaderView(R.layout.nav_header_main);
        navigationView.setNavigationItemSelectedListener(this);

        if (intent.getExtras()!= null) {
            String name = getIntent().getStringExtra("User name");
            email = getIntent().getStringExtra("User mail");
            Uri imageUri= getIntent().getData();
            TextView username = (TextView) headerView.findViewById(R.id.userName);
            username.setText(name);
            TextView usermail = (TextView) headerView.findViewById(R.id.userMail);
            usermail.setText(email);
            CircleImageView userPic= (CircleImageView) headerView.findViewById(R.id.profilePic);

            Picasso.with(this).load(imageUri.toString()).into(userPic);
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        mFm = getFragmentManager();
        FragmentTransaction ft = mFm.beginTransaction();
        Fragment toursFragment = ToursFragment.newInstance();
        ft.add(R.id.container, toursFragment, ToursFragment.TAG);
        ft.commit();

    }



    @Override
    protected void onResume() {
        super.onResume();
        AppEventsLogger.activateApp(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        AppEventsLogger.deactivateApp(this);
    }

    @Override
    public void onBackPressed() {

        int count = getFragmentManager().getBackStackEntryCount();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (count == 0) {
                super.onBackPressed();
                //additional code
            } else {
                getFragmentManager().popBackStack();
            }
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Intent i;

        if (id == R.id.nav_tours) {
            mFm = getFragmentManager();
            FragmentTransaction ft = mFm.beginTransaction();
            Fragment favToursFragment = FavouriteToursFragment.newInstance();
            ft.replace(R.id.container, favToursFragment, FavouriteToursFragment.TAG);
            ft.addToBackStack(FavouriteToursFragment.TAG);
            ft.commit();

        } else if (id == R.id.nav_friends) {
            i = new Intent(MainActivity.this, ShareWithFriends.class);
            startActivity(i);

        } else if (id == R.id.nav_notifications) {

        } else if (id == R.id.nav_about) {

        } else if (id == R.id.nav_feedback) {

        } else if (id == R.id.nav_help) {

        } else if (id == R.id.nav_logout) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void showTourDescription(String parseObject) {
        mFm = getFragmentManager();
        FragmentTransaction ft = mFm.beginTransaction();
        Fragment tourDescription = TourDescription.newInstance(parseObject);
        ft.replace(R.id.container, tourDescription, TourDescription.TAG);
        ft.addToBackStack(TourDescription.TAG);
        ft.commit();
    }

    @Override
    public void sendEmail(String emailAddress, String tourName) {
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("message/rfc822");
        i.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{emailAddress});
        i.putExtra(Intent.EXTRA_SUBJECT, "Book Tour: " + tourName + "");
        i.putExtra(Intent.EXTRA_TEXT, "I would like to book the " + tourName + " for the day: (complete day), for (complete quantity of people) ");
        try {
            startActivity(Intent.createChooser(i, "Send mail..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(MainActivity.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void openMap() {
        Intent i = new Intent(MainActivity.this, MapsActivity.class);
        startActivity(i);
    }

    @Override
    public void showTourOwner(String ownerId) {
        mFm = getFragmentManager();
        FragmentTransaction ft = mFm.beginTransaction();
        Fragment tourOwner = TourOwnerFragment.newInstance(ownerId);
        ft.replace(R.id.container, tourOwner, TourOwnerFragment.TAG);
        ft.addToBackStack(TourOwnerFragment.TAG);
        ft.commit();
    }

    @Override
    public void shareTour(String name) {

            PackageManager pm=getPackageManager();
            try {

                Intent waIntent = new Intent(Intent.ACTION_SEND);
                waIntent.setType("text/plain");
                String text = "Hey I'm going to the "+name+" ! Join me! Download Trippy to book the tour";

                PackageInfo info=pm.getPackageInfo("com.whatsapp", PackageManager.GET_META_DATA);
                //Check if package exists or not. If not then code
                //in catch block will be called
                waIntent.setPackage("com.whatsapp");

                waIntent.putExtra(Intent.EXTRA_TEXT, text);
                startActivity(Intent.createChooser(waIntent, "Share with"));

            } catch (PackageManager.NameNotFoundException e) {
                Toast.makeText(this, "WhatsApp not Installed", Toast.LENGTH_SHORT)
                        .show();
            }

        }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_filter:
               Intent i = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(i);
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }
}
