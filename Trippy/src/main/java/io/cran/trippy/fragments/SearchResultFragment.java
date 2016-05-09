package io.cran.trippy.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
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


public class SearchResultFragment extends Fragment {

    private static final String ARG_SEARCH = "search_value";
    public static final String TAG = SearchResultFragment.class.getSimpleName();

    private String mSearchValue = "";
    private ArrayList<ParseObject> availableTours = new ArrayList<ParseObject>();
    private ListView mTourList;



    private ArrayList<ParseObject> mFavouriteTours = new ArrayList<ParseObject>();
    private ArrayList<Uri> tourOwners = new ArrayList<>();
    private ToursSearchFragmentListener mListener;

    public SearchResultFragment() {
        // Required empty public constructor
    }


    public static SearchResultFragment newInstance(String searchValue) {
        SearchResultFragment fragment = new SearchResultFragment();
        Bundle args = new Bundle();
        args.putString(ARG_SEARCH, searchValue);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mSearchValue = getArguments().getString(ARG_SEARCH);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root=inflater.inflate(R.layout.fragment_search_result, container, false);
        mTourList = (ListView) root.findViewById(R.id.tourSearchList);
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        AppPreferences appPrefs = AppPreferences.instance(getActivity().getApplication());
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Tour");
        query.whereEqualTo("name", mSearchValue);
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> tours, ParseException e) {
                if (e == null) {

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

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (ToursSearchFragmentListener) activity;
        } catch (Exception ignored) {
            throw new IllegalArgumentException("The activity must implement ToursSearchFragmentListener.");
        }
    }

    public interface ToursSearchFragmentListener {
        // TODO: Update argument type and name

        void showTourDescription(String objectId);
    }
}
