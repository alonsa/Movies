package com.example.alon_ss.movies;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    private ArrayAdapter<String> adapter;


    public MainActivityFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        return inflater.inflate(R.layout.fragment_main, container, false);
//    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        FragmentActivity activity = getActivity();
        int layout = R.layout.movie_list;
        int id = R.id.movie_list_textview;

        adapter = new ArrayAdapter<>(activity, layout, id, new ArrayList<String>());

        ListView listView = (ListView) rootView.findViewById(R.id.listview_movies);
        listView.setAdapter(adapter);
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
//                String forecast = adapter.getItem(position);
//                Intent intent = new Intent(getActivity(), DetailActivity.class)
//                        .putExtra(Intent.EXTRA_TEXT, forecast);
//                startActivity(intent);
//            }
//        });






        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        updateData();
    }

    private void updateData() {
        String[] tempDataList = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13"};

        FetchDataTask fetchWeatherTask = new FetchDataTask();
        fetchWeatherTask.execute();


        adapter.addAll(tempDataList);
    }

    private class FetchDataTask extends AsyncTask<String, Void, MovieData[]> {

        private final String LOG_TAG = this.getClass().getSimpleName();

        final String BASE_URL = "http://api.themoviedb.org/3/";
        final Integer DAYS_NUM = 7;
        final String APP_ID = "api_key";

        @Override
        protected MovieData[] doInBackground(String... strings) {

            String forecastData = getDataFromServer();

            MovieData[] weatherData = null;
            try {
                weatherData = DataParser.getDataFromJson(forecastData);
            } catch (Exception e) {
                e.printStackTrace();

                String errMsg = "No data from server";
                ArrayList list = new ArrayList<>(Collections.nCopies(DAYS_NUM, new MovieData()));
                weatherData = (MovieData[]) list.toArray(new MovieData[list.size()]);
            }

            return weatherData;
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
            String forecastJsonStr = null;

            String vodType = getFromPref(R.string.pref_vod_type, R.string.pref_default_vod_type);
            String queryType = getFromPref(R.string.pref_search_type, R.string.pref_default_search_type);

            try {
                Uri builtUri = Uri.parse(BASE_URL)
                        .buildUpon()
                        .appendPath(vodType)
                        .appendPath(queryType)
                        .appendQueryParameter(APP_ID, getString(R.string.movie_db_api_key))
                        .build();

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
                forecastJsonStr = buffer.toString();

            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                return null;
            }  catch (Exception e) {
                Log.e(LOG_TAG, "Error ", e);
                return null;
            }finally {
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

            return forecastJsonStr;
        }

        protected void onPostExecute(MovieData[] result) {

            if (result != null){
                adapter.clear();
                for (MovieData movie: result){
                    adapter.add(movie.getTitle());
                }
            }
        }
    }
}
