package com.example.alon_ss.movies.utills;

import android.content.Context;

import com.example.alon_ss.movies.R;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by alon_ss on 5/23/16.
 */
public class Utills {


    public static String getQueryTypeByVodAndConfQueryType(String vodType, String queryType, Context context) {

        Set<String> commonQueryTypes = new HashSet<>(Arrays.asList(
                context.getString(R.string.pref_search_query_latest),
                context.getString(R.string.pref_search_query_popular),
                context.getString(R.string.pref_search_query_my_favorite),
                context.getString(R.string.pref_search_query_top_rated),
                context.getString(R.string.pref_search_query_favorites)
        ));

        Set<String> movieQueryTypes = new HashSet<>(Arrays.asList(
                context.getString(R.string.pref_search_movie_query_upcoming_value),
                context.getString(R.string.pref_search_movie_query_now_playing_value)));

        movieQueryTypes.addAll(commonQueryTypes);

        Set<String> tvQueryTypes = new HashSet<>(Arrays.asList(
                context.getString(R.string.pref_search_tv_query_airing_today_value),
                context.getString(R.string.pref_search_tv_query_on_the_air_value)));

        tvQueryTypes.addAll(commonQueryTypes);

        if (!((isMovie(vodType, context) && movieQueryTypes.contains(queryType)) ||
                (!isMovie(vodType, context) && tvQueryTypes.contains(queryType)))){

            return context.getString(R.string.pref_search_query_popular);
        }else {
            return queryType;
        }
    }


    private static Boolean isMovie(String vodType,  Context context) {
        return context.getString(R.string.pref_vod_type_movie).equals(vodType);
    }
}
