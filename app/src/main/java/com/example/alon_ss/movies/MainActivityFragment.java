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
import java.lang.reflect.Array;
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

            String data = getDataFromServer();

            MovieData[] movieDataArr = null;
            try {
                if (data != null){
                    movieDataArr = DataParser.getDataFromJson(data, getContext());
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

        protected String getDataFromServer() {

            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;

            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String jsonStr = null;

            String vodType = getFromPref(R.string.settings_vod_type_key, R.string.pref_default_vod_type);
            String confQueryType = getFromPref(R.string.settings_search_type_key, R.string.pref_default_search_type);

            String queryType = getQueryTypeByVodAndConfQueryType(vodType ,confQueryType);

            try {
                Uri builtUri = Uri.parse(BASE_URL)
                        .buildUpon()
                        .appendPath(vodType)
                        .appendPath(queryType)
                        .appendQueryParameter(APP_ID, getString(R.string.movie_db_api_key))
                        .build();

                Log.d(LOG_TAG, "Uri to server : " + builtUri.toString());


                URL url = new URL(builtUri.toString());
                // Create the request to OpenWeatherMap, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));


                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line).append("\n");
                }

                if (buffer.length() == 0) {
                    return null;
                }
                jsonStr = buffer.toString();

            } catch (Exception e) {
                Log.e(LOG_TAG, "Error ", e);
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }

            return jsonStr;
        }

        protected void onPostExecute(MovieData[] result) {
            if (result != null){
                imageAdapter.clear();
                ArrayList<MovieData> all = new ArrayList<MovieData>(Arrays.asList(result));
                imageAdapter.addAll(all);
            }
        }
    }

    private Boolean isMovie(String vodType) {
        return getString(R.string.pref_vod_type_movie).equals(vodType);
    }

    private String getQueryTypeByVodAndConfQueryType(String vodType, String queryType) {

        Set<String> vodQueryTypes = new HashSet<>(Arrays.asList(
                getString(R.string.pref_search_query_latest),
                getString(R.string.pref_search_query_popular),
                getString(R.string.pref_search_query_top_rated),
                getString(R.string.pref_search_vod_query_upcoming_value),
                getString(R.string.pref_search_vod_query_now_playing_value)));


        Set<String> tvQueryTypes = new HashSet<>(Arrays.asList(
                getString(R.string.pref_search_query_latest),
                getString(R.string.pref_search_query_popular),
                getString(R.string.pref_search_query_top_rated),
                getString(R.string.pref_search_tv_query_airing_today_value),
                getString(R.string.pref_search_tv_query_on_the_air_value)));

        if (!((isMovie(vodType) && vodQueryTypes.contains(queryType)) ||
                (!isMovie(vodType) && tvQueryTypes.contains(queryType)))){

            return getString(R.string.pref_search_query_popular);
        }else {
            return queryType;
        }
    }
}
