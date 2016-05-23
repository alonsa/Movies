package com.example.alon_ss.movies.entities;

import android.content.Context;
import android.net.Uri;

import com.example.alon_ss.movies.R;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

/**
 * Created by alon_ss on 5/8/16.
 */
public class SimpleVodData {

    private String posterRawPath;
    private String overview = "";
    private Date releaseDate;
    private String id = "";
    private String title = "";
    private double voteAverage = 0;
    private String vodType = "";
    private Integer runtime;


    public SimpleVodData(VodData vodData) {
        this.posterRawPath = vodData.getPosterRawPath();
        this.overview = vodData.getOverview();
        this.releaseDate = vodData.getReleaseDate();
        this.id = vodData.getId();
        this.title = vodData.getTitle();
        this.voteAverage = vodData.getVoteAverage();
        this.vodType = vodData.getVodType();
        this.runtime = vodData.getRuntime();
    }

    public String getPosterRawPath() {
        return posterRawPath;
    }

    public void setPosterRawPath(String posterRawPath) {
        this.posterRawPath = posterRawPath;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
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

    public double getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(double voteAverage) {
        this.voteAverage = voteAverage;
    }

    public String getVodType() {
        return vodType;
    }

    public void setVodType(String vodType) {
        this.vodType = vodType;
    }

    public Integer getRuntime() {
        return runtime;
    }

    public void setRuntime(Integer runtime) {
        this.runtime = runtime;
    }
}
