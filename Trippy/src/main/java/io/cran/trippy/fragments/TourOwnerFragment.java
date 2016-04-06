package io.cran.trippy.fragments;

import android.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import io.cran.trippy.R;
import io.cran.trippy.adapters.TourAdapter;
import io.cran.trippy.utils.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TourOwnerFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TourOwnerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TourOwnerFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    public static final String TAG = TourOwnerFragment.class.getSimpleName();
    private static final String OWNER_ID = "owner_id";

    // TODO: Rename and change types of parameters
    private String mOwnerId;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private TextView mTourOwnerName;
    private TextView mTourOwnerMail;
    private CircleImageView mProfilePic;
    private ArrayList availableTours= new ArrayList();
    private ListView mTourList;

    public TourOwnerFragment() {
        // Required empty public constructor
    }

    public static TourOwnerFragment newInstance(String ownerId) {
        TourOwnerFragment fragment = new TourOwnerFragment();
        Bundle args = new Bundle();
        args.putString(OWNER_ID, ownerId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mOwnerId = getArguments().getString(OWNER_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root= inflater.inflate(R.layout.fragment_tour_owner, container, false);

        mTourOwnerName = (TextView) root.findViewById(R.id.tourOwner);
        mTourOwnerMail= (TextView) root.findViewById(R.id.tourOwnerMail);
        mProfilePic = (CircleImageView) root.findViewById(R.id.profilePic);
        mTourList= (ListView) root.findViewById(R.id.tourOwnerList);

        ParseQuery<ParseObject> query = ParseQuery.getQuery("TourOwner");
        query.whereEqualTo("objectId", mOwnerId);
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, ParseException e) {
                mTourOwnerName.setText(object.getString("name"));
                mTourOwnerMail.setText(object.getString("email"));
                Uri imageUri = Uri.parse(object.getParseFile("profilePic").getUrl());
                Picasso.with(mProfilePic.getContext()).load(imageUri.toString()).into(mProfilePic);
            }
        });

        return root;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        ParseQuery query = new ParseQuery("Tour");
        query.whereEqualTo("ownerId",mOwnerId);
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> tours, ParseException e) {
                if (e == null) {
                    for (ParseObject tour : tours) {
                        availableTours.add(tour);


                        TourAdapter tourAdapter = new TourAdapter(getActivity().getApplicationContext(), getActivity().getApplication(), availableTours);
                        mTourList.setAdapter(tourAdapter);
                  /**      mTourList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                mListener.showTourDescription(availableTours.get(position).getObjectId());
                            }
                        });**/

                    }
                } else {
                    Log.e("Parse Error", "" + e.getMessage());
                }
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
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
