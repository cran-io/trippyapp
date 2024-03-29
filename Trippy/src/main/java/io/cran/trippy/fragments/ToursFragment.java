package io.cran.trippy.fragments;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import io.cran.trippy.R;
import io.cran.trippy.adapters.TourAdapter;
import io.cran.trippy.utils.AppPreferences;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link io.cran.trippy.fragments.ToursFragment.ToursFragmentListener} interface
 * to handle interaction events.
 * Use the {@link ToursFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ToursFragment extends android.app.Fragment {
    public static final String TAG = ToursFragment.class.getSimpleName();
    private ArrayList<ParseObject> availableTours = new ArrayList<ParseObject>();
    private ListView mTourList;



    private ToursFragmentListener mListener;
    private ArrayList<Uri> ownersPic = new ArrayList<>();
    private ArrayList<ParseObject> mFavouriteTours = new ArrayList<ParseObject>();
    private ArrayList<Uri> tourOwners = new ArrayList<>();

    public ToursFragment() {
        // Required empty public constructor
    }


    public static android.app.Fragment newInstance() {
        return new ToursFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AppPreferences.instance(getActivity().getApplication()).saveTourLocation("All");
        AppPreferences.instance(getActivity().getApplication()).saveTourDuration("All day");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root =inflater.inflate(R.layout.fragment_tours, container, false);
        mTourList = (ListView) root.findViewById(R.id.tourList);

        return root;
    }



    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (ToursFragmentListener) activity;
        } catch (Exception ignored) {
            throw new IllegalArgumentException("The activity must implement ToursFragmentListener.");
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        AppPreferences appPrefs = AppPreferences.instance(getActivity().getApplication());
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Tour");

        if (!appPrefs.getShowFriendlyTours()) {
            query.whereEqualTo("weather", "sunny");
        }
        if (appPrefs.getTourLocation() != null && !appPrefs.getTourLocation().equals("All")) {
            query.whereEqualTo("location", appPrefs.getTourLocation());
        }
        if (appPrefs.getTourDuration() != null && !appPrefs.getTourDuration().equals("All day")) {
            query.whereEqualTo("duration", appPrefs.getTourDuration());
        }

        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> tours, ParseException e) {
                if (e == null) {
                    availableTours.clear();
                    tourOwners.clear();
                    mFavouriteTours.clear();
                    for (ParseObject tour : tours) {
                        if (!availableTours.contains(tour)) {
                            availableTours.add(tour);
                            String ownerId = tour.getString("ownerId");
                            ParseQuery<ParseObject> query = ParseQuery.getQuery("TourOwner");
                            query.whereEqualTo("objectId", ownerId);
                            query.getFirstInBackground(new GetCallback<ParseObject>() {
                                @Override
                                public void done(ParseObject object, ParseException e) {
                                    if (e == null) {
                                        tourOwners.add(Uri.parse(object.getParseFile("profilePic").getUrl()));
                                    }
                                }
                            });
                        }
                    }
                    ParseUser mCurrentUser = ParseUser.getCurrentUser();
                    ParseRelation<ParseObject> mFavouritesToursRelation = mCurrentUser.getRelation("favourite_tours");
                    mFavouritesToursRelation.getQuery().findInBackground(new FindCallback<ParseObject>() {
                        @Override
                        public void done(List<ParseObject> favouriteTours, ParseException e) {
                            if (e == null) {
                                for (ParseObject tour : favouriteTours) {
                                    if (!mFavouriteTours.contains(tour)) {
                                        mFavouriteTours.add(tour);


                                    }
                                }
                                TourAdapter tourAdapter = new TourAdapter(getActivity().getApplicationContext(), getActivity().getApplication(), availableTours, mFavouriteTours, tourOwners);
                                assert mTourList != null;
                                mTourList.setAdapter(tourAdapter);
                            }
                        }
                    });
                    if (mTourList != null) {
                        mTourList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                mListener.showTourDescription(availableTours.get(position).getObjectId());
                            }
                        });
                    }

                } else {
                    Log.e("Parse Error", "" + e.getMessage());
                }
            }
        });
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface ToursFragmentListener {
        // TODO: Update argument type and name

        void showTourDescription(String objectId);
    }

    /**     String ownerId = tour.getString("ownerId");
     ParseQuery<ParseObject> query = ParseQuery.getQuery("TourOwner");
     query.whereEqualTo("objectId", ownerId);
     query.getFirstInBackground(new GetCallback<ParseObject>() {
    @Override public void done(ParseObject object, ParseException e) {
    if (e == null) {
    ownersPic.add(Uri.parse(object.getParseFile("profilePic").getUrl()));
    TourAdapter tourAdapter = new TourAdapter(getActivity().getApplicationContext(), getActivity().getApplication(), availableTours,ownersPic);
    mTourList.setAdapter(tourAdapter);
    mTourList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
    @Override public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    mListener.showTourDescription(availableTours.get(position).getObjectId());
    }
    });
    } else {
    Log.e("Parse Error", e.getMessage());
    }
    }
    });
     }**/
}
