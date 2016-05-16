package com.example.alon_ss.movies;

import android.content.Context;
import android.net.Uri;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by alon_ss on 5/8/16.
 */
public class MovieData {

    private URL posterUrl;
    private String overview = "";
    private Date releaseDate;
    private String id = "";
    private String title = "";
    private double voteAverage = 0;

    public MovieData(JSONObject movieDataJson, Context context) throws JSONException, ParseException, MalformedURLException {
        posterUrl = getPosterUrl(movieDataJson, context);
        String OVERVIEW = "overview";
        String RELEASE_DATE = "release_date";
        String ID = "id";
        String dateInString = movieDataJson.getString(RELEASE_DATE);
        String ORIGINAL_TITLE = "original_title";
        String VOTE_AVERAGE = "vote_average";

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-mm-dd");

        overview = movieDataJson.getString(OVERVIEW);
        releaseDate = formatter.parse(dateInString);
        id = movieDataJson.getString(ID);
        title = movieDataJson.getString(ORIGINAL_TITLE);
        voteAverage = movieDataJson.getDouble(VOTE_AVERAGE);
    }

    private URL getPosterUrl(JSONObject movieDataJson, Context context) throws JSONException, MalformedURLException {
        String size = context.getString(R.string.image_poster_size_small);

        String POSTER_PATH = "poster_path";
        String posterRawPath = movieDataJson.getString(POSTER_PATH).replace("/", "");
        String p = "p";
        String t = "t";
        String APP_ID = "api_key";
        String IMAGE_BASE_URL = "http://image.tmdb.org";
        Uri.Builder builtUri = Uri.parse(IMAGE_BASE_URL).buildUpon().appendPath(t).appendPath(p).appendPath(size)
                .appendPath(posterRawPath)
                .appendQueryParameter(APP_ID, context.getString(R.string.movie_db_api_key));

        return new URL(builtUri.toString());
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public URL getPosterUrl() {
        return posterUrl;
    }

    public String getOverview() {
        return overview;
    }

    public Date getReleaseDate() {
        return releaseDate;
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

}
