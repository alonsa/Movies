package com.example.alon_ss.movies.entities;

import android.content.Context;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import com.example.alon_ss.movies.R;
import com.google.gson.Gson;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by alon_ss on 5/8/16.
 */
public class VodData implements Parcelable {

    private String posterRawPath;
    private String overview = "";
    private Date releaseDate;
    private String id = "";
    private String title = "";
    private double voteAverage = 0;
    private String vodType = "";
    private Integer runtime;

    private Gson gson = new Gson();


    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");


    public VodData(String posterRawPath, String overview, Date releaseDate, String id, String title, double voteAverage, Integer runtime, String vodType) {
        this.posterRawPath = posterRawPath;
        this.overview = overview;
        this.releaseDate = releaseDate;
        this.id = id;
        this.title = title;
        this.voteAverage = voteAverage;
        this.vodType = vodType;
        this.runtime = runtime;
    }

    public VodData(String json) {
        SimpleVodData simpleVodData = gson.fromJson(json, SimpleVodData.class);

        this.posterRawPath = simpleVodData.getPosterRawPath();
        this.overview = simpleVodData.getOverview();
        this.releaseDate = simpleVodData.getReleaseDate();
        this.id = simpleVodData.getId();
        this.title = simpleVodData.getTitle();
        this.voteAverage = simpleVodData.getVoteAverage();
        this.vodType = simpleVodData.getVodType();
        this.runtime = simpleVodData.getRuntime();
    }

    public VodData(Parcel parcel){
        posterRawPath = parcel.readString();
        overview = parcel.readString();
        try {
            releaseDate = formatter.parse(parcel.readString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        id = parcel.readString();
        title = parcel.readString();
        vodType = parcel.readString();
        voteAverage = parcel.readDouble();
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(getPosterRawPath());
        parcel.writeString(getOverview());
        parcel.writeString(formatter.format(getReleaseDate()));
        parcel.writeString(getId());
        parcel.writeString(getTitle());
        parcel.writeString(getVodType());
        parcel.writeDouble(getVoteAverage());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String getPosterRawPath() {
        return posterRawPath;
    }

    public String getVodType() {
        return vodType;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public URL getPosterUrl(int sizeInt, Context context) {
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

    public Integer getRuntime() {
        return runtime;
    }

    public String getTitle() {
        return title;
    }

    public static final Creator<VodData> CREATOR = new Creator<VodData>() {
        @Override
        public VodData createFromParcel(Parcel in) {
            return new VodData(in);
        }

        @Override
        public VodData[] newArray(int size) {
            return new VodData[size];
        }
    };

    public String toJsonString() {
        return gson.toJson(new SimpleVodData(this));
    }
}
