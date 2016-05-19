package com.example.alon_ss.movies.entities;

/**
 * Created by alon_ss on 5/8/16.
 */
public class TrailersData {

    private String name;
    private String youTubePath;

    public TrailersData(String name, String key) {
        this.name = name;
        this.youTubePath = key;
    }

    public String getName() {
        return name;
    }

    public String getYouTubePath() {
        return youTubePath;
    }
}
