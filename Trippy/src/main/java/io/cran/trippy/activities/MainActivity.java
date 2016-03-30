package io.cran.trippy.activities;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.facebook.appevents.AppEventsLogger;
import com.squareup.picasso.Picasso;

import bolts.AppLinks;
import io.cran.trippy.R;
import io.cran.trippy.fragments.FavouriteToursFragment;
import io.cran.trippy.fragments.TourDescription;
import io.cran.trippy.fragments.ToursFragment;
import io.cran.trippy.utils.CircleImageView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ToursFragment.ToursFragmentListener {

    private FragmentManager mFm;

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
            mFm = getFragmentManager();
            FragmentTransaction ft = mFm.beginTransaction();
            Fragment favToursFragment = FavouriteToursFragment.newInstance();
            ft.replace(R.id.container, favToursFragment);
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
        ft.replace(R.id.container, tourDescription);
        ft.commit();
    }
}
