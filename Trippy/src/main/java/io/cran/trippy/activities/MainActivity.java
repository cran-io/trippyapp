package io.cran.trippy.activities;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.parse.GetCallback;
import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.squareup.picasso.Picasso;

import bolts.AppLinks;
import io.cran.trippy.R;
import io.cran.trippy.fragments.FavouriteToursFragment;
import io.cran.trippy.fragments.SearchResultFragment;
import io.cran.trippy.fragments.SettingsActivity;
import io.cran.trippy.fragments.TourDescription;
import io.cran.trippy.fragments.TourOwnerFragment;
import io.cran.trippy.fragments.ToursFragment;
import io.cran.trippy.utils.AppPreferences;
import io.cran.trippy.utils.CircleImageView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ToursFragment.ToursFragmentListener, TourDescription.TourDescriptionListener, SearchResultFragment.ToursSearchFragmentListener {

    private FragmentManager mFm;
    private String email = "";
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private CircleImageView userPic;
    private String imageUri;
    private String name = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        Uri targetUrl = AppLinks.getTargetUrlFromInboundIntent(this, getIntent());
        if (targetUrl != null) {
            Log.i("Activity", "App Link Target URL: " + targetUrl.toString());
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        assert toolbar != null;
        toolbar.setNavigationIcon(R.drawable.menu_icon);
        setSupportActionBar(toolbar);


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        assert navigationView != null;
        View headerView =  navigationView.inflateHeaderView(R.layout.nav_header_main);
        navigationView.setNavigationItemSelectedListener(this);

        AppPreferences appPrefs = AppPreferences.instance(getApplication());

        AppPreferences.instance(getApplication()).saveSessionOpen(true);

        ParseQuery<ParseObject> query = ParseQuery.getQuery("User");
        query.whereEqualTo("email", appPrefs.getUserEmail());
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, ParseException e) {
                if (e == null) {
                    name = "" + object.getString("firstname") + " " + object.getString("lastname");
                    AppPreferences.instance(getApplication()).saveUsername(name);

                }
            }
        });

        TextView username = (TextView) headerView.findViewById(R.id.userName);
        username.setText(appPrefs.getUsername());
        TextView usermail = (TextView) headerView.findViewById(R.id.userMail);
        usermail.setText(appPrefs.getUserEmail());
        userPic = (CircleImageView) headerView.findViewById(R.id.profileUserPic);

        if (!appPrefs.getImagePath().equals("")) {
            Picasso.with(this).load(appPrefs.getImagePath()).into(userPic);
        } else {
            userPic.setImageResource(R.drawable.user_emptystate);
            userPic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dispatchTakePictureIntent();
                }
            });

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

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            userPic.setImageBitmap(imageBitmap);
        }
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
            Intent in = new Intent(Intent.ACTION_SEND);
            in.setType("message/rfc822");
            in.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{"sol@cran.io"});
            in.putExtra(Intent.EXTRA_SUBJECT, "Feedback Trippy");

            try {
                startActivity(Intent.createChooser(in, "Send mail..."));
            } catch (android.content.ActivityNotFoundException ex) {
                Toast.makeText(MainActivity.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
            }

        } else if (id == R.id.nav_help) {

        } else if (id == R.id.nav_logout) {
            ParseUser.logOutInBackground(new LogOutCallback() {
                @Override
                public void done(ParseException e) {
                    AppPreferences.instance(getApplication()).saveSessionOpen(false);
                    LoginManager.getInstance().logOut();
                    Intent i = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(i);
                }
            });

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

        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.e("User Search Submit",query);
                mFm = getFragmentManager();
                FragmentTransaction ft = mFm.beginTransaction();
                Fragment searchFragment = SearchResultFragment.newInstance(query);
                ft.replace(R.id.container, searchFragment, SearchResultFragment.TAG);
                ft.addToBackStack(SearchResultFragment.TAG);
                ft.commit();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                return false;
            }
        });
        changeSearchViewTextColor(searchView);
        return true;
    }


    private void changeSearchViewTextColor(View view) {
        if (view != null) {
            if (view instanceof TextView) {
                ((TextView) view).setTextColor(Color.WHITE);
                return;
            } else if (view instanceof ViewGroup) {
                ViewGroup viewGroup = (ViewGroup) view;
                for (int i = 0; i < viewGroup.getChildCount(); i++) {
                    changeSearchViewTextColor(viewGroup.getChildAt(i));
                }
            }
        }
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
