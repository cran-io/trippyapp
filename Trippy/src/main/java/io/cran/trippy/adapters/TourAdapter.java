package io.cran.trippy.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import io.cran.trippy.R;
import io.cran.trippy.pojo.TourPojo;

/**
 * Created by MariaSol on 16/03/2016.
 */
public class TourAdapter extends BaseAdapter {


    private final Context mContext;
    private final ArrayList<TourPojo> mTourList;
    private final LayoutInflater mInflater;

    public TourAdapter(Context context, ArrayList toursList) {
        mContext = context;
        mTourList = toursList;
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return mTourList.size();
    }

    @Override
    public Object getItem(int position) {
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
        viewHolder.tourName.setText(mTourList.get(position).getName());

        viewHolder.tourPic=(ImageView) convertView.findViewById(R.id.tourImage);
        viewHolder.tourPic.setImageResource(mTourList.get(position).getImageId());

        viewHolder.description= (TextView) convertView.findViewById(R.id.tourDescription);
        viewHolder.description.setText(mTourList.get(position).getDescription());


        return convertView;
    }

    private class MyViewHolder {
        TextView tourName;
        ImageView tourPic;
        TextView description;
        ImageView transport;
        ImageView weather;
        ImageView type;
    }
}
