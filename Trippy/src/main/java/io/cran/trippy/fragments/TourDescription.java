package io.cran.trippy.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import io.cran.trippy.R;
import io.cran.trippy.utils.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link io.cran.trippy.fragments.TourDescription.TourDescriptionListener} interface
 * to handle interaction events.
 * Use the {@link TourDescription#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TourDescription extends Fragment {

    public static final String TAG = TourDescription.class.getSimpleName();
    private TourDescriptionListener mListener;
    private String tourId;
    private ArrayList<ParseObject> selectedTour = new ArrayList<>();
    private ArrayList<ParseObject> tourOwner= new ArrayList<>();


    public TourDescription() {
        // Required empty public constructor
    }

    public static TourDescription newInstance(String objectId) {
        TourDescription fragment = new TourDescription();
        Bundle args = new Bundle();
        args.putString("Id",objectId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            tourId = getArguments().getString("Id");
        }

    }


    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View root = inflater.inflate(R.layout.fragment_tour_description, container, false);

        ImageView tourLocation = (ImageView) root.findViewById(R.id.tourLocation);
        tourLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.openMap();
            }
        });
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Tour");
        query.whereEqualTo("objectId", tourId);
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, ParseException e) {
                if (e == null) {
                    selectedTour.add(object);

                    TextView tourName = (TextView) root.findViewById(R.id.tourNameDescription);
                    tourName.setText(selectedTour.get(0).getString("name"));

                    ImageView tourPic = (ImageView) root.findViewById(R.id.imageTour);
                    Uri imageUri = Uri.parse(selectedTour.get(0).getParseFile("image").getUrl());
                    Picasso.with(root.getContext()).load(imageUri.toString()).into(tourPic);

                    TextView description = (TextView) root.findViewById(R.id.tourDescription);
                    description.setText(selectedTour.get(0).getString("description"));

                    final String ownerId = selectedTour.get(0).getString("ownerId");

                    ParseQuery<ParseObject> query = ParseQuery.getQuery("TourOwner");
                    query.whereEqualTo("objectId", ownerId);
                    query.getFirstInBackground(new GetCallback<ParseObject>() {
                        @Override
                        public void done(ParseObject object, ParseException e) {
                            if (e == null) {
                                tourOwner.add(object);
                                TextView ownerName = (TextView) root.findViewById(R.id.tourOwner);
                                ownerName.setText(tourOwner.get(0).getString("name"));

                                TextView cantTours = (TextView) root.findViewById(R.id.tourCant);
                                cantTours.setText("" + tourOwner.get(0).getInt("tourQuantity") + " enrolled tours");

                                CircleImageView profilePic = (CircleImageView) root.findViewById(R.id.profileDescriptionPic);
                                Uri imageUri = Uri.parse(tourOwner.get(0).getParseFile("profilePic").getUrl());
                                Picasso.with(root.getContext()).load(imageUri.toString()).into(profilePic);

                                profilePic.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        mListener.showTourOwner(selectedTour.get(0).getString("ownerId"));
                                    }
                                });

                                ImageView shareTour = (ImageView) root.findViewById (R.id.share);
                                shareTour.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        mListener.shareTour(selectedTour.get(0).getString("name"));
                                    }
                                });

                                ImageView bookThisTourBtn = (ImageView) root.findViewById(R.id.bookTour);
                                bookThisTourBtn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        mListener.sendEmail(tourOwner.get(0).getString("email"),selectedTour.get(0).getString("name"));
                                    }
                                });

                            } else {
                                Log.e("Parse Error", e.getMessage());
                            }
                        }
                    });

                } else {
                    Log.e("Parse Error", e.getMessage());
                }
            }
        });


        return root;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {

    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof TourDescriptionListener) {
            mListener = (TourDescriptionListener) activity;
        } else {
            throw new RuntimeException(activity.toString()
                    + " must implement TourDescriptionListener");
        }
    }

    @Override
    public void onResume() {
        super.onResume();

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
    public interface TourDescriptionListener {
        // TODO: Update argument type and name
        void sendEmail(String email, String tourName);

        void openMap();

        void showTourOwner(String ownerId);


        void shareTour(String name);
    }
}
