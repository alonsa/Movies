package com.example.alon_ss.movies;

import android.content.Context;
import android.net.Uri;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.text.ParseException;

/**
 * Created by alon_ss on 5/8/16.
 */
public class DataParser {

    public static MovieData[] getDataFromJson(String jsonStr, Context context) throws JSONException, MalformedURLException {

        JSONObject json = new JSONObject(jsonStr);
        JSONArray results = json.getJSONArray("results");
        MovieData[] resultStr = new MovieData[results.length()];

        for(int i = 0; i < results.length(); i++) {

            // Get the JSON object representing the day
            JSONObject movieData = results.getJSONObject(i);

            try {
                resultStr[i] = new MovieData(movieData, context);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        return resultStr;


    }
}


