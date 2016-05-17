package com.example.alon_ss.movies;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    private ImageAdapter imageAdapter = new ImageAdapter();


    public MainActivityFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        imageAdapter.setContext(getContext());
        GridView gridview = (GridView) rootView.findViewById(R.id.gridview);
        gridview.setAdapter(imageAdapter);

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                MovieData movieData = imageAdapter.getItem(position);
                Intent intent = new Intent(getActivity(), DetailActivity.class);
                intent.putExtra(Intent.EXTRA_TEXT, movieData);
                startActivity(intent);
            }
        });


        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        updateData();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        switch(itemId){
            case R.id.action_refresh :
                updateData();
                return true;

            default: return super.onOptionsItemSelected(item);
        }
    }

    private void updateData() {
        FetchDataTask fetchWeatherTask = new FetchDataTask();
        fetchWeatherTask.execute();
    }

    private class FetchDataTask extends AsyncTask<String, Void, MovieData[]> {

        private final String LOG_TAG = this.getClass().getSimpleName();

        final String BASE_URL = "http://api.themoviedb.org/3/";
        final String APP_ID = "api_key";

        @Override
        protected MovieData[] doInBackground(String... strings) {

            MovieDbClient movieDbClient = new MovieDbClient();

            String vodType = getFromPref(R.string.settings_vod_type_key, R.string.pref_default_vod_type);
            String confQueryType = getFromPref(R.string.settings_search_type_key, R.string.pref_default_search_type);

            String data = movieDbClient.getDataFromServer(vodType, confQueryType);

            MovieData[] movieDataArr = null;
            try {
                if (data != null){
                    movieDataArr = DataParser.getDataFromJson(data);
                }else{
                    Log.e(LOG_TAG, "Cant get data from server!!!");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return movieDataArr;
        }


        private String getFromPref(int name, int defaultVal) {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
            String key = getString(name);
            String defaultValue = getString(defaultVal);
            return prefs.getString(key, defaultValue);
        }

        protected void onPostExecute(MovieData[] result) {
            if (result != null){
                imageAdapter.clear();
                ArrayList<MovieData> all = new ArrayList<MovieData>(Arrays.asList(result));
                imageAdapter.addAll(all);
            }
        }
    }

}
