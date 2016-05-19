package com.example.alon_ss.movies.main;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.alon_ss.movies.utills.MovieDbClient;
import com.example.alon_ss.movies.R;
import com.example.alon_ss.movies.entities.VodData;
import com.example.alon_ss.movies.details.DetailActivity;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    private MainImageAdapter mainImageAdapter = new MainImageAdapter();

    public MainActivityFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        updateData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.main_fragment, container, false);
        GridView gridview = (GridView) rootView.findViewById(R.id.gridview);
        gridview.setAdapter(mainImageAdapter);

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                VodData vodData = mainImageAdapter.getItem(position);
                Intent intent = new Intent(getActivity(), DetailActivity.class);
                intent.putExtra(Intent.EXTRA_TEXT, vodData);
                startActivity(intent);
            }
        });


        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
//        updateData();;ljk
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

    private class FetchDataTask extends AsyncTask<String, Void, VodData[]> {

        private final String LOG_TAG = this.getClass().getSimpleName();

        @Override
        protected VodData[] doInBackground(String... strings) {

            MovieDbClient movieDbClient = new MovieDbClient(getActivity());

            String vodType = getFromPref(R.string.settings_vod_type_key, R.string.pref_default_vod_type);
            String confQueryType = getFromPref(R.string.settings_search_type_key, R.string.pref_default_search_type);

            return movieDbClient.getVodsByTypeAndQuery(vodType, confQueryType);
        }


        private String getFromPref(int name, int defaultVal) {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
            String key = getString(name);
            String defaultValue = getString(defaultVal);
            return prefs.getString(key, defaultValue);
        }

        protected void onPostExecute(VodData[] result) {
            if (result != null){
                mainImageAdapter.clear();
                ArrayList<VodData> all = new ArrayList<VodData>(Arrays.asList(result));
                mainImageAdapter.addAll(all);
            }
        }
    }

}
