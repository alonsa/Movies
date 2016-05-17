package com.example.alon_ss.movies;

import android.content.Context;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

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
public class MovieData implements Parcelable {

    private String posterRawPath;
    private Context context;
    private String overview = "";
    private Date releaseDate;
    private String id = "";
    private String title = "";
    private double voteAverage = 0;

    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

    public MovieData(JSONObject movieDataJson, Context context) throws JSONException, ParseException, MalformedURLException {
        String POSTER_PATH = "poster_path";

        String OVERVIEW = "overview";
        String RELEASE_DATE = "release_date";
        String ID = "id";
        String dateInString = movieDataJson.getString(RELEASE_DATE);
        String ORIGINAL_TITLE = "original_title";
        String VOTE_AVERAGE = "vote_average";

        this.context = context;
        this.posterRawPath = movieDataJson.getString(POSTER_PATH).replace("/", "");
        this.overview = movieDataJson.getString(OVERVIEW);
        this.releaseDate = formatter.parse(dateInString);
        this.id = movieDataJson.getString(ID);
        this.title = movieDataJson.getString(ORIGINAL_TITLE);
        this.voteAverage = movieDataJson.getDouble(VOTE_AVERAGE);
    }

    public MovieData(Parcel parcel){
        posterRawPath = parcel.readString();
        overview = parcel.readString();
        try {
            releaseDate = formatter.parse(parcel.readString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        id = parcel.readString();
        title = parcel.readString();
        voteAverage = parcel.readDouble();
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(getPosterRawPath());
        parcel.writeString(getOverview());
        parcel.writeString(formatter.format(getReleaseDate()));
        parcel.writeString(getId());
        parcel.writeString(getTitle());
        parcel.writeDouble(getVoteAverage());
    }

    @Override
    public int describeContents() {
        return 0;
    }

//    private URL getPosterUrl(JSONObject movieDataJson, Context context) throws JSONException, MalformedURLException {
//        String size = context.getString(R.string.image_poster_size_small);
//
//        String POSTER_PATH = "poster_path";
//        String posterRawPath = movieDataJson.getString(POSTER_PATH).replace("/", "");
//        String p = "p";
//        String t = "t";
//        String APP_ID = "api_key";
//        String IMAGE_BASE_URL = "http://image.tmdb.org";
//        Uri.Builder builtUri = Uri.parse(IMAGE_BASE_URL).buildUpon().appendPath(t).appendPath(p).appendPath(size)
//                .appendPath(posterRawPath)
//                .appendQueryParameter(APP_ID, context.getString(R.string.movie_db_api_key));
//
//        return new URL(builtUri.toString());
//    }

    public String getPosterRawPath() {
        return posterRawPath;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public URL getPosterUrl(int sizeInt) {
        String size = context.getString(sizeInt);

        String p = "p";
        String t = "t";
        String APP_ID = "api_key";
        String IMAGE_BASE_URL = "http://image.tmdb.org";
        Uri.Builder builtUri = Uri.parse(IMAGE_BASE_URL).buildUpon().appendPath(t).appendPath(p).appendPath(size).appendPath(posterRawPath)
                .appendQueryParameter(APP_ID, context.getString(R.string.movie_db_api_key));

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    public String getOverview() {
        return overview;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public String getReleaseDateString() {
        return formatter.format(releaseDate);
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

    public static final Creator<MovieData> CREATOR = new Creator<MovieData>() {
        @Override
        public MovieData createFromParcel(Parcel in) {
            return new MovieData(in);
        }

        @Override
        public MovieData[] newArray(int size) {
            return new MovieData[size];
        }
    };
}
