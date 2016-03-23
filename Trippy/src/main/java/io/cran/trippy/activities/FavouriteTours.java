package io.cran.trippy.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseRelation;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import io.cran.trippy.R;
import io.cran.trippy.adapters.TourAdapter;

public class FavouriteTours extends AppCompatActivity {
    private ArrayList<ParseObject> mFavouriteTours = new ArrayList();
    private ParseRelation<ParseObject> mFavouritesToursRelation;
    private ParseUser mCurrentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite_tours);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mCurrentUser = ParseUser.getCurrentUser();
        mFavouritesToursRelation = mCurrentUser.getRelation("favourite_tours");
        mFavouritesToursRelation.getQuery().findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> favouriteTours, ParseException e) {
                if (e == null) {
                    ListView tourList = (ListView) findViewById(R.id.favouriteTourList);

                    for (ParseObject tour : favouriteTours) {
                        mFavouriteTours.add(tour);
                    }
                    TourAdapter tourAdapter = new TourAdapter(FavouriteTours.this, getApplication(), mFavouriteTours);
                    tourList.setAdapter(tourAdapter);
                }
            }
        });
    }
}
