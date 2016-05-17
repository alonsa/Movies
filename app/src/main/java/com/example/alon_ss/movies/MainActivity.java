package com.example.alon_ss.movies;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            int id = R.id.container;
            Fragment fragment = new MainActivityFragment();
            getSupportFragmentManager().beginTransaction().add(id, fragment).commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch(id){
            case R.id.action_settings :
                startActivity(new Intent(this, SettingsActivity.class));
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private String getFromPref(int name, int defaultVal) {
        Context context = getBaseContext();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String key = getString(name);
        String defaultValue = getString(defaultVal);
        return prefs.getString(key, defaultValue);
    }

    private Boolean isMovie(String vodType) {
        return getString(R.string.pref_vod_type_movie).equals(vodType);
    }

    private String getQueryTypeByVodAndConfQueryType(String vodType, String queryType) {

        Set<String> vodQueryTypes = new HashSet<>(Arrays.asList(
                getString(R.string.pref_search_query_latest),
                getString(R.string.pref_search_query_popular),
                getString(R.string.pref_search_query_top_rated),
                getString(R.string.pref_search_vod_query_upcoming_value),
                getString(R.string.pref_search_vod_query_now_playing_value)));


        Set<String> tvQueryTypes = new HashSet<>(Arrays.asList(
                getString(R.string.pref_search_query_latest),
                getString(R.string.pref_search_query_popular),
                getString(R.string.pref_search_query_top_rated),
                getString(R.string.pref_search_tv_query_airing_today_value),
                getString(R.string.pref_search_tv_query_on_the_air_value)));

        if (!((isMovie(vodType) && vodQueryTypes.contains(queryType)) ||
                (!isMovie(vodType) && tvQueryTypes.contains(queryType)))){

            return getString(R.string.pref_search_query_popular);
        }else {
            return queryType;
        }
    }

    @Override
    protected void onPostResume() {

        String vodType = getFromPref(R.string.settings_vod_type_key, R.string.pref_default_vod_type);
        String confQueryType = getFromPref(R.string.settings_search_type_key, R.string.pref_default_search_type);
        String checkedConfQueryType = getQueryTypeByVodAndConfQueryType(vodType, confQueryType);
        String titleVodType = vodType.substring(0,1).toUpperCase() + vodType.substring(1);
        String titleConfQueryType = checkedConfQueryType.substring(0,1).toUpperCase() + checkedConfQueryType.substring(1);
        setTitle(titleVodType + " - " + titleConfQueryType);

        super.onPostResume();
    }
}
