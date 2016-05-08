package com.example.alon_ss.movies;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by alon_ss on 5/8/16.
 */
public class MovieData {

    final String POSTER_PATH = "poster_path";
    final String OVERVIEW = "overview";
    final String RELEASE_DATE = "release_date";
    final String ID = "id";
    final String ORIGINAL_TITLE = "title";
    final String VOTE_AVERAGE = "vote_average";

    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-mm-dd");

    String poster_path = "";
    String overview = "";
    Date release_date;
    String id = "";
    String title = "";
    double vote_average = 0;

    public MovieData() {
        overview = "Temp data overview";
        release_date = new Date();
        title = "Temp title";
    }

    public MovieData(JSONObject movieDataJson) throws JSONException, ParseException {


        poster_path = movieDataJson.getString(POSTER_PATH);
        overview = movieDataJson.getString(OVERVIEW);
        String dateInString = movieDataJson.getString(RELEASE_DATE);
        release_date = formatter.parse(dateInString);

        id = movieDataJson.getString(ID);
        title = movieDataJson.getString(ORIGINAL_TITLE);
        vote_average = movieDataJson.getDouble(VOTE_AVERAGE);
    }

    public double getVote_average() {
        return vote_average;
    }

    public void setVote_average(double vote_average) {
        this.vote_average = vote_average;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public Date getRelease_date() {
        return release_date;
    }

    public void setRelease_date(Date release_date) {
        this.release_date = release_date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}
