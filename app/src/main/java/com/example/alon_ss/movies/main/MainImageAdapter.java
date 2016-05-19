package com.example.alon_ss.movies.main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.alon_ss.movies.R;
import com.example.alon_ss.movies.entities.VodData;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by alon_ss on 5/9/16.
 */
public class MainImageAdapter extends BaseAdapter {

    private final String LOG_TAG = this.getClass().getSimpleName();

    ArrayList<VodData> data = new ArrayList<>();

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public VodData getItem(int i) {
        return data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {

//        Log.d(LOG_TAG, "getView [position: " + position + ", convertView: " + convertView + "]");
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.main_movie_list_view, viewGroup, false);
        VodData vodData = getItem(position);

        Context context = viewGroup.getContext();

        if (vodData != null){
            ImageView imageView = (ImageView) view.findViewById(R.id.movie_image_view);
            String path = vodData.getPosterUrl(R.string.image_poster_size_small, context).toString();
            Picasso.with(context).load(path).fit().centerInside().into(imageView);
            TextView textView = (TextView) view.findViewById(R.id.movie_text_view);
            textView.setText(vodData.getTitle());
        }


        return view;
    }

    public void clear(){
        data.clear();
    }

    public void add(VodData vodData){
        data.add(vodData);
        notifyDataSetChanged();
    }

    public void addAll(ArrayList<VodData> vodDataList){
        data.addAll(vodDataList);
        notifyDataSetChanged();
    }

}
