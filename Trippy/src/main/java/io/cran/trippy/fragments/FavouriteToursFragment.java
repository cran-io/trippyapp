package io.cran.trippy.fragments;

import android.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FavouriteToursFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FavouriteToursFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FavouriteToursFragment extends Fragment {
    public static final String TAG = FavouriteToursFragment.class.getSimpleName();
    private ArrayList<ParseObject> mFavouriteTours = new ArrayList();
    private ParseRelation<ParseObject> mFavouritesToursRelation;
    private ParseUser mCurrentUser;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private ListView mTourList;

    public FavouriteToursFragment() {
        // Required empty public constructor
    }


    public static FavouriteToursFragment newInstance() {
        return new FavouriteToursFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root =inflater.inflate(R.layout.fragment_favourite_tours, container, false);
        mTourList = (ListView) root.findViewById(R.id.favouriteTourList);
        return root;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement FavouriteToursFragment");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mCurrentUser = ParseUser.getCurrentUser();
        mFavouritesToursRelation = mCurrentUser.getRelation("favourite_tours");
        mFavouritesToursRelation.getQuery().findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> favouriteTours, ParseException e) {
                if (e == null) {
                    for (ParseObject tour : favouriteTours) {
                        mFavouriteTours.add(tour);
                    }
                    TourAdapter tourAdapter = new TourAdapter(getActivity().getApplicationContext(), getActivity().getApplication(), mFavouriteTours);
                    mTourList.setAdapter(tourAdapter);
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
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
