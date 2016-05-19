package com.example.alon_ss.movies.utills;

import android.app.Activity;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.alon_ss.movies.R;
import com.example.alon_ss.movies.entities.TrailersData;
import com.example.alon_ss.movies.entities.VodData;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by alon_ss on 5/17/16.
 */
public class MovieDbClient  {

    private Activity activity;

    public MovieDbClient(Activity activity){
        this.activity = activity;
    }

    private final String LOG_TAG = this.getClass().getSimpleName();

    final String BASE_URL = "http://api.themoviedb.org/3/";
    final String APP_ID = "api_key";
    final String VIDEOS = "videos";

    public VodData[] getVodsByTypeAndQuery(String vodType, String confQueryType) {

        String queryType = getQueryTypeByVodAndConfQueryType(vodType ,confQueryType);

        Uri builtUri = buildUri(vodType, queryType);

        URL url = buildUrl(builtUri);

        String data = getDataFromServer(url);

        VodData[] vodDataArr = null;
        try {
            if (data != null){
                vodDataArr = DataParser.getVodDataArrFromString(data, vodType);
            }else{
                Log.e(LOG_TAG, "Cant get data from server!!!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return vodDataArr;
    }

    public VodData getVodDataByTypeAndId(String vodType, String vodId) {


        Uri builtUri = buildUri(vodType, vodId);

        URL url = buildUrl(builtUri);

        String rawData = getDataFromServer(url);

        VodData vodData = null;
        try {
            if (rawData != null){
                vodData = DataParser.getVodDataFromString(rawData, vodType);
            }else{
                Log.e(LOG_TAG, "Cant get data from server!!!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return vodData;
    }

    public ArrayList<TrailersData> getTrailersByTypeAndId(String vodType, String vodId) {


        Uri builtUri = buildUri(vodType, vodId, VIDEOS);

        URL url = buildUrl(builtUri);

        String rawData = getDataFromServer(url);

        ArrayList<TrailersData> vodData = null;
        try {
            if (rawData != null){
                vodData = DataParser.getTrailersFromString(rawData);
            }else{
                Log.e(LOG_TAG, "Cant get data from server!!!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return vodData;
    }

    private Uri buildUri(String vodType, String vodId, String... extraVals) {

        Uri.Builder builder = Uri.parse(BASE_URL)
                .buildUpon()
                .appendPath(vodType)
                .appendPath(vodId);

        for (String extraVal : extraVals) {
            builder.appendPath(extraVal);
        }

        builder.appendQueryParameter(APP_ID, activity.getString(R.string.movie_db_api_key));
        return builder.build();
    }


    @Nullable
    private URL buildUrl(Uri builtUri) {
        Log.d(LOG_TAG, "Uri to server : " + builtUri.toString());
        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    @Nullable
    private String getDataFromServer(URL url) {

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        // Will contain the raw JSON response as a string.
        String jsonStr = null;

        try {

            // Create the request to OpenWeatherMap, and open the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuilder sb = new StringBuilder();
            if (inputStream == null) {
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));


            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }

            if (sb.length() == 0) {
                return null;
            }
            jsonStr = sb.toString();

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
        return activity.getString(R.string.pref_vod_type_movie).equals(vodType);
    }

    private String getQueryTypeByVodAndConfQueryType(String vodType, String queryType) {

        Set<String> vodQueryTypes = new HashSet<>(Arrays.asList(
                activity.getString(R.string.pref_search_query_latest),
                activity.getString(R.string.pref_search_query_popular),
                activity.getString(R.string.pref_search_query_top_rated),
                activity.getString(R.string.pref_search_vod_query_upcoming_value),
                activity.getString(R.string.pref_search_vod_query_now_playing_value)));


        Set<String> tvQueryTypes = new HashSet<>(Arrays.asList(
                activity.getString(R.string.pref_search_query_latest),
                activity.getString(R.string.pref_search_query_popular),
                activity.getString(R.string.pref_search_query_top_rated),
                activity.getString(R.string.pref_search_tv_query_airing_today_value),
                activity.getString(R.string.pref_search_tv_query_on_the_air_value)));

        if (!((isMovie(vodType) && vodQueryTypes.contains(queryType)) ||
                (!isMovie(vodType) && tvQueryTypes.contains(queryType)))){

            return activity.getString(R.string.pref_search_query_popular);
        }else {
            return queryType;
        }
    }
}
