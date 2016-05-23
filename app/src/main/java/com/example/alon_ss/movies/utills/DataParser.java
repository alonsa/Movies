package com.example.alon_ss.movies.utills;

import android.net.Uri;

import com.example.alon_ss.movies.entities.TrailersData;
import com.example.alon_ss.movies.entities.VodData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

/**
 * Created by alon_ss on 5/8/16.
 */
public class DataParser {

    private static final String POSTER_PATH = "poster_path";
    private static final String OVERVIEW = "overview";
    private static final String RELEASE_DATE = "release_date";
    private static final String FIRST_AIR_DATE = "first_air_date";
    private static final String ID = "id";
    private static final String ORIGINAL_TITLE = "original_title";
    private static final String VOTE_AVERAGE = "vote_average";
    private static final String RUNTIME = "runtime";
    private static final String EPISODE_RUN_TIME = "episode_run_time";
    private static final String KEY = "key";
    private static final String NAME = "name";
    private static final String SITE = "site";
    private static final String YOU_TUBE = "YouTube";
    private static final String YOU_TUBE_URL = "https://www.youtube.com/watch?v=";

    private static SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

    public static VodData[] getVodDataArrFromString(String jsonStr, String vodType) throws JSONException {

        JSONObject json = new JSONObject(jsonStr);
        JSONArray results = json.getJSONArray("results");
        VodData[] resultStr = new VodData[results.length()];

        for(int i = 0; i < results.length(); i++) {
            JSONObject movieData = results.getJSONObject(i);
            resultStr[i] = getVodDataFromJson(movieData, vodType);
        }

        return resultStr;
    }

    public static VodData getVodDataFromString(String jsonStr, String vodType) throws JSONException {

        JSONObject json = new JSONObject(jsonStr);
        return getVodDataFromJson(json, vodType);
    }

    public static ArrayList<TrailersData> getTrailersFromString(String jsonStr) throws JSONException {

        JSONObject json = new JSONObject(jsonStr);
        JSONArray results = json.getJSONArray("results");
        ArrayList<TrailersData> vodVideos = new ArrayList<TrailersData>();

        for(int i = 0; i < results.length(); i++) {
            JSONObject movieData = results.getJSONObject(i);

            String site = movieData.getString(SITE);
            if (site.equals(YOU_TUBE)){
                String key = movieData.getString(KEY);
                String name = movieData.getString(NAME);

                Uri uri = Uri.parse(YOU_TUBE_URL + key);
                vodVideos.add(new TrailersData(name, uri));
            }
        }

        Collections.sort(vodVideos,new Comparator<TrailersData>(){
            public int compare(TrailersData s1, TrailersData s2){
                return s1.getYouTubePath().compareTo(s2.getYouTubePath());
            }});

        return vodVideos;
    }

    private static VodData getVodDataFromJson(JSONObject json, String vodType) throws JSONException {
        String posterRawPath = json.getString(POSTER_PATH).replace("/", "");
        String overview = json.getString(OVERVIEW);
        String id = json.getString(ID);
        String title = getTitle(json);
        double voteAverage = json.getDouble(VOTE_AVERAGE);

        String dateInString = getDate(json);
        Date releaseDate = null;
        try {
            releaseDate = formatter.parse(dateInString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Integer runtime = null;
        if (!json.isNull(RUNTIME)){
            runtime = json.getInt(RUNTIME);
        }else if (!json.isNull(EPISODE_RUN_TIME)){
            String episode_run_time = json.getString(EPISODE_RUN_TIME);
            String runtimeStr = episode_run_time.subSequence(1, episode_run_time.length() - 1).toString();
            runtime = Integer.valueOf(runtimeStr);
        }

        return new VodData(posterRawPath, overview, releaseDate, id, title, voteAverage, runtime, vodType);
    }

    private static String getDate(JSONObject json) throws JSONException {
        if (!json.isNull(RELEASE_DATE)){
            return json.getString(RELEASE_DATE);
        }else{
            return json.getString(FIRST_AIR_DATE);
        }
    }

    private static String getTitle(JSONObject json) throws JSONException {
        if (!json.isNull(ORIGINAL_TITLE)){
            return json.getString(ORIGINAL_TITLE);
        }else {
            return json.getString(NAME);
        }
    }

}


