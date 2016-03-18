package io.cran.trippy.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import java.util.ArrayList;

import io.cran.trippy.R;
import io.cran.trippy.adapters.TourAdapter;
import io.cran.trippy.pojo.TourPojo;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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

        }


        ListView tourList = (ListView) findViewById(R.id.tourList);

        ArrayList<TourPojo> availableTours = populateTours();
        TourAdapter tourAdapter = new TourAdapter(this, availableTours);
        tourList.setAdapter(tourAdapter);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

    }

    private ArrayList<TourPojo> populateTours() {
        TourPojo tour1 = new TourPojo(0, "Helicopter tour", "Fly over Buenos Aires city. Discover new places and get to see an unique city in an unique way", R.drawable.helicopter_tour, "Helicopter", "Sunny", "Sightseeing");
        TourPojo tour2 = new TourPojo(1, "Bicycle tour", "Cycle around Buenos Aires city. Discover new places and get to see a unique city in a fun way", R.drawable.bicycle_tour, "Bicycle", "Sunny", "Seightseeing");
        TourPojo tour3 = new TourPojo(2, "Pub Crawl", "Get to know the best pubs of Buenos Aires nightlife. Walk into the coolest places.", R.drawable.pubcrawl, "Walking", "Sunny", "Food and drink");

        ArrayList<TourPojo> availableTours = new ArrayList();
        availableTours.add(tour1);
        availableTours.add(tour2);
        availableTours.add(tour3);

        return availableTours;
    }

    @Override
    protected void onResume() {
        super.onResume();

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

        if (id == R.id.nav_tours) {
            // Handle the camera action
        } else if (id == R.id.nav_friends) {

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
