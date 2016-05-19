package com.example.alon_ss.movies.utills;

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
    private static final String ID = "id";
    private static final String ORIGINAL_TITLE = "original_title";
    private static final String VOTE_AVERAGE = "vote_average";
    private static final String RUNTIME = "runtime";
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

                vodVideos.add(new TrailersData(name, YOU_TUBE_URL + key));
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
        String title = json.getString(ORIGINAL_TITLE);
        double voteAverage = json.getDouble(VOTE_AVERAGE);

        String dateInString = json.getString(RELEASE_DATE);
        Date releaseDate = null;
        try {
            releaseDate = formatter.parse(dateInString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Integer runtime = null;
        if (!json.isNull(RUNTIME)){
            runtime = json.getInt(RUNTIME);
        }


        return new VodData(posterRawPath, overview, releaseDate, id, title, voteAverage, runtime, vodType);
    }

}


