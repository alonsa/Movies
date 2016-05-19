package com.example.alon_ss.movies.details;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.alon_ss.movies.R;
import com.example.alon_ss.movies.entities.TrailersData;

import java.util.ArrayList;

/**
 * Created by alon_ss on 5/9/16.
 */
public class DetailsTrailerAdapter extends BaseAdapter {

    private final String LOG_TAG = this.getClass().getSimpleName();

    ArrayList<TrailersData> data = new ArrayList<>();

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public TrailersData getItem(int i) {
        return data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {

        Log.d(LOG_TAG, "getView [position: " + position + ", convertView: " + convertView + "]");
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.detail_trailer_list_view, viewGroup, false);
        TrailersData trailersData = getItem(position);

        if (trailersData != null){

            ImageView imageView = (ImageView) view.findViewById(R.id.youtube_icon);
//            imageView.setImageURI();
//            imageView.setClickable(true);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(LOG_TAG, "getView [" + v + "]");



                }
            });
            TextView textView = (TextView) view.findViewById(R.id.trailer_text_view);
            textView.setText(trailersData.getName());
        }


        return view;
    }

    public void clear(){
        data.clear();
    }

    public void add(TrailersData trailersData){
        data.add(trailersData);
        notifyDataSetChanged();
    }

    public void addAll(ArrayList<TrailersData> trailersDataList){
        data.addAll(trailersDataList);
        notifyDataSetChanged();
    }

}
