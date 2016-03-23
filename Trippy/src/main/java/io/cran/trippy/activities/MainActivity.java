package io.cran.trippy.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.facebook.appevents.AppEventsLogger;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import bolts.AppLinks;
import io.cran.trippy.R;
import io.cran.trippy.adapters.TourAdapter;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ArrayList<ParseObject> availableTours = new ArrayList();

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
            String email = getIntent().getStringExtra("User mail");
            String imageUrl= getIntent().getStringExtra("User pic");
            TextView username = (TextView) headerView.findViewById(R.id.userName);
            username.setText(name);
            TextView usermail = (TextView) headerView.findViewById(R.id.userMail);
            usermail.setText(email);
            ImageView userPic= (ImageView) headerView.findViewById(R.id.profilePic);
            URL url = null;
            try {
                url = new URL(imageUrl);
                Bitmap image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                userPic.setImageBitmap(image);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

    }



    private ArrayList<ParseObject> populateTours() {

        ParseQuery query = new ParseQuery("Tour");
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> tours, ParseException e) {
                if (e == null) {
                    ListView tourList = (ListView) findViewById(R.id.tourList);

                    for (ParseObject tour : tours) {
                        availableTours.add(tour);
                    }
                    TourAdapter tourAdapter = new TourAdapter(MainActivity.this, getApplication(), availableTours);
                    tourList.setAdapter(tourAdapter);

                } else {
                    Log.e("Parse Error",""+e.getMessage());
                }
            }
        });

        return availableTours;

    }

    @Override
    protected void onResume() {
        super.onResume();
        populateTours();
        AppEventsLogger.activateApp(this);
    }

    @Override
    protected void onPause() {
        super.onPause();

        // Logs 'app deactivate' App Event.
        AppEventsLogger.deactivateApp(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Intent i;

        if (id == R.id.nav_tours) {
            i= new Intent(MainActivity.this, FavouriteTours.class);
            startActivity(i);
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
}
