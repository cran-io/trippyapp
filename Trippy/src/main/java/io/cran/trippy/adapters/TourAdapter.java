package io.cran.trippy.adapters;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.Set;

import io.cran.trippy.R;
import io.cran.trippy.utils.AppPreferences;

/**
 * Created by MariaSol on 16/03/2016.
 */
public class TourAdapter extends BaseAdapter {


    private static final String TAG = TourAdapter.class.getSimpleName();
    private final Context mContext;
    private final Application mApplication;
    private final ArrayList<ParseObject> mTourList;
    private ParseRelation<ParseObject> mTourFavourites;
    private ParseUser mCurrentUser;
    private final LayoutInflater mInflater;
    private boolean isChecked=false;

    public TourAdapter(Context context, Application application, ArrayList toursList) {
        mContext = context;
        mApplication = application;
        mTourList = toursList;
        mInflater = LayoutInflater.from(mContext);
        mCurrentUser = ParseUser.getCurrentUser();
        mTourFavourites = mCurrentUser.getRelation("favourite_tours");
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

        viewHolder.tourName = (TextView) convertView.findViewById(R.id.tourName);
        viewHolder.tourName.setText(mTourList.get(position).getString("name"));

        viewHolder.tourPic=(ImageView) convertView.findViewById(R.id.tourImage);
        Bitmap bmp = BitmapFactory.decodeFile(String.valueOf(mTourList.get(position).getParseFile("image")));
        viewHolder.tourPic.setImageBitmap(bmp);

        viewHolder.description= (TextView) convertView.findViewById(R.id.tourDescription);
        viewHolder.description.setText(mTourList.get(position).getString("description"));


        handleButtonClick(convertView, position);

        return convertView;
    }

    private void handleButtonClick(final View rowView, final int pos)
    {
        final ImageView favourite = (ImageView) rowView.findViewById(R.id.favourite);


        favourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isChecked) {
                    ParseObject tourSelected = getItem(pos);
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
