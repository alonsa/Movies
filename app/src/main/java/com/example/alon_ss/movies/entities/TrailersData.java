package com.example.alon_ss.movies.entities;

import android.net.Uri;

/**
 * Created by alon_ss on 5/8/16.
 */
public class TrailersData {

    private String name;
    private Uri youTubePath;

    public TrailersData(String name, Uri uri) {
        this.name = name;
        this.youTubePath = uri;
    }

    public String getName() {
        return name;
    }

    public Uri getYouTubePath() {
        return youTubePath;
    }
}
