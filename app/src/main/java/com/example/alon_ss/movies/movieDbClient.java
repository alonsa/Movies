package com.example.alon_ss.movies;

import android.support.v4.app.Fragment;
import android.net.Uri;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by alon_ss on 5/17/16.
 */
public class MovieDbClient extends Fragment {

    private final String LOG_TAG = this.getClass().getSimpleName();

    final String BASE_URL = "http://api.themoviedb.org/3/";
    final String APP_ID = "api_key";


    public String getDataFromServer(String vodType, String confQueryType) {

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        // Will contain the raw JSON response as a string.
        String jsonStr = null;
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
