package io.cran.trippy.adapters;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.squareup.picasso.Picasso;

import java.util.AbstractSequentialList;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import io.cran.trippy.R;
import io.cran.trippy.activities.MainActivity;
import io.cran.trippy.utils.AppPreferences;
import io.cran.trippy.utils.CircleImageView;

/**
 * Created by MariaSol on 16/03/2016.
 */
public class TourAdapter extends BaseAdapter {


    private static final String TAG = TourAdapter.class.getSimpleName();
    private final Context mContext;
    private final Application mApplication;
    private final ArrayList<ParseObject> mTourList;
    private ArrayList<ParseObject> mFavouriteTours = new ArrayList<>();
    private ParseRelation<ParseObject> mTourFavourites;
    private ParseUser mCurrentUser;
    private final LayoutInflater mInflater;
    private boolean isChecked=false;
    private ArrayList<ParseObject> tourOwner = new ArrayList<>();
    private CircleImageView profilePic;

    public TourAdapter(Context context, Application application, ArrayList toursList, ArrayList favouriteTours) {
        mContext = context;
        mApplication = application;
        mTourList = toursList;
        mInflater = LayoutInflater.from(mContext);
        mCurrentUser = ParseUser.getCurrentUser();
        mTourFavourites = mCurrentUser.getRelation("favourite_tours");
        mFavouriteTours = favouriteTours;
    }

    @Override
    public int getCount() {
        return mTourList.size();
    }

    @Override
    public ParseObject getItem(int position) {
        return mTourList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        MyViewHolder viewHolder;

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.tour_row, null);
            viewHolder = new MyViewHolder();
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (MyViewHolder) convertView.getTag();
        }

        viewHolder.transport= (ImageView) convertView.findViewById(R.id.transport);
        viewHolder.type= (ImageView) convertView.findViewById(R.id.type);
        viewHolder.weather = (ImageView) convertView.findViewById(R.id.weather);

        viewHolder.transport.setImageResource(choseTourTransport(mTourList.get(position).getString("transport")));
        viewHolder.type.setImageResource(choseTourType(mTourList.get(position).getString("type")));
        viewHolder.weather.setImageResource(choseTourWeather(mTourList.get(position).getString("weather")));

        viewHolder.tourName = (TextView) convertView.findViewById(R.id.tourName);
        viewHolder.tourName.setText(mTourList.get(position).getString("name"));

        viewHolder.tourPic=(ImageView) convertView.findViewById(R.id.tourImage);
        Uri imageUri= Uri.parse(mTourList.get(position).getParseFile("image").getUrl());
        Picasso.with(convertView.getContext()).load(imageUri.toString()).into(viewHolder.tourPic);

        viewHolder.description= (TextView) convertView.findViewById(R.id.tourDescription);
        viewHolder.description.setText(mTourList.get(position).getString("description"));

        viewHolder.favourite = (ImageView) convertView.findViewById(R.id.favourite);
        isChecked = isAFavouriteTour(mTourList.get(position).getString("name"));
        if (isChecked) {
            viewHolder.favourite.setImageResource(R.drawable.favourite_tours);
        } else {
            viewHolder.favourite.setImageResource(R.drawable.favourite);
        }

       /** profilePic = (CircleImageView) convertView.findViewById(R.id.profilePicture);
        Picasso.with(mContext).load(mOwnersPic.get(position).toString()).into(profilePic);**/


        handleButtonClick(convertView, position);

        return convertView;
    }

    private boolean isAFavouriteTour(String objectId) {

        for (int i = 0; i < mFavouriteTours.size(); i++) {
            if (mFavouriteTours.get(i).get("name").equals(objectId)) {
                return true;
            }
        }
        return false;
    }

    private int choseTourWeather(String weather) {
        switch (weather) {
            case "rain":
                return R.drawable.lluvia;
            default:
                return R.drawable.weather_sunny;
        }

    }

    private int choseTourTransport(String transport){
        switch (transport){
            case "bike":
                return R.drawable.transport_bicycle;
            case "walk":
                return R.drawable.transport_walking2;
            case "helicopter":
                return R.drawable.transport_walking;
            default:
                return R.drawable.transport_walking;
        }
    }

    private int choseTourType(String type){
        switch (type){
            case "sightseeing":
                return R.drawable.type_sightseeing;
            case "nightlife":
                return R.drawable.type_drinks;
            case "museum":
                return R.drawable.museum;
            case "art":
                return R.drawable.arte;
            case "translator":
                return R.drawable.translator;
            case "music":
                return R.drawable.music;

            default:
                return R.drawable.type_sightseeing;
        }
    }



    private void handleButtonClick(final View rowView, final int pos)
    {
        final ImageView favourite = (ImageView) rowView.findViewById(R.id.favourite);

        favourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ParseObject tourSelected = getItem(pos);

                if (!isChecked) {
                    favourite.setImageResource(R.drawable.favourite_tours);
                    mTourFavourites.add(tourSelected);
                    mCurrentUser.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e != null) {
                                Log.e(TAG, e.getMessage());
                            }
                        }
                    });
                    isChecked=true;
                }
                else{
                    favourite.setImageResource(R.drawable.favourite);
                    mTourFavourites.remove(tourSelected);
                    mCurrentUser.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e != null) {
                                Log.e(TAG, e.getMessage());
                            }
                        }
                    });
                    isChecked=false;

                }
            }
        });


    }

    private class MyViewHolder {
        TextView tourName;
        ImageView tourPic;
        ImageView favourite;
        TextView description;
        ImageView transport;
        ImageView weather;
        ImageView type;
    }
}
