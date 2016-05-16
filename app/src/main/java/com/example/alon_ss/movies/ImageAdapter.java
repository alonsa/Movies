package com.example.alon_ss.movies;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by alon_ss on 5/9/16.
 */
public class ImageAdapter extends BaseAdapter {

    private final String LOG_TAG = this.getClass().getSimpleName();

    ArrayList<MovieData> data = new ArrayList<>();
    private Context context;
    private LayoutInflater inflater;

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {

//        Log.d(LOG_TAG, "getView [position: " + position + ", convertView: " + convertView + "]");

        View view = convertView;

//        if (needToUpdate[position] || view == null) {
            view = inflater.inflate(R.layout.movie_image_view, viewGroup, false);
            MovieData movieData = (MovieData) getItem(position);

            if (movieData.getPosterUrl() != null){
                ImageView imageView = (ImageView) view.findViewById(R.id.movie_image_view);
                String path = movieData.getPosterUrl().toString();
                Picasso.with(context).load(path).fit().centerInside().into(imageView);
            }

//        }

        return view;
    }

    public void setContext(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    public void clear(){
        data.clear();
    }

    public void add(MovieData movieData){
        data.add(movieData);
        notifyDataSetChanged();
    }

    public void addAll(ArrayList<MovieData> movieDataList){
        data.addAll(movieDataList);
        notifyDataSetChanged();
    }

}
