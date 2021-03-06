/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.alon_ss.movies.details;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.alon_ss.movies.R;
import com.example.alon_ss.movies.entities.SimpleVodData;
import com.example.alon_ss.movies.entities.TrailersData;
import com.example.alon_ss.movies.entities.VodData;
import com.example.alon_ss.movies.settings.SettingsActivity;
import com.example.alon_ss.movies.utills.MovieDbClient;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutionException;

public class DetailActivity extends AppCompatActivity {

    private final String LOG_TAG = DetailActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().add(R.id.container, new DetailFragment()).commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class DetailFragment extends Fragment {

        Gson gson = new Gson();

        FetchVodDataTask fetchVodDataTask = new FetchVodDataTask();
        FetchTrailersDataTask fetchTrailersDataTask = new FetchTrailersDataTask();

        private DetailsTrailerAdapter detailsTrailerAdapter = new DetailsTrailerAdapter();
        private final String LOG_TAG = DetailFragment.class.getSimpleName();

        public DetailFragment() {
            setHasOptionsMenu(true);
        }



        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            Log.d(LOG_TAG, "onStart");
            updateData();

            super.onCreate(savedInstanceState);
            setHasOptionsMenu(true);
        }

        private void updateData() {
            Log.d(LOG_TAG, "updateData");

            Intent intent = getActivity().getIntent();
            if (intent != null && intent.hasExtra(Intent.EXTRA_TEXT)) {
                VodData vodData = intent.getParcelableExtra(Intent.EXTRA_TEXT);
                fetchVodDataTask.execute(vodData.getVodType(),vodData.getId());
                fetchTrailersDataTask.execute(vodData.getVodType(),vodData.getId());
            }
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

            Log.d(LOG_TAG, "onCreateView");
            View mainView = inflater.inflate(R.layout.detail_fragment, container, false);

            TextView originalTextView = (TextView) mainView.findViewById(R.id.detail_original_title);
            TextView releaseDateTextView = (TextView) mainView.findViewById(R.id.detail_release_date);
            TextView ratingTextView = (TextView) mainView.findViewById(R.id.detail_rating);
            TextView runtimeTextView = (TextView) mainView.findViewById(R.id.detail_runtime);
            TextView plotTextView = (TextView) mainView.findViewById(R.id.detail_plot_synopsis);
            ImageView detailImageView = (ImageView) mainView.findViewById(R.id.detail_image_view);

            VodData vodData = null;

            try {
                vodData = fetchVodDataTask.get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }

            if (vodData != null) {

                originalTextView.setText(vodData.getTitle());
                releaseDateTextView.setText(vodData.getReleaseDateString());
                String voteAverage = String.valueOf(vodData.getVoteAverage()) + "/" + 10;
                ratingTextView.setText(voteAverage);

                if (vodData.getRuntime()  != null){
                    String runtime = vodData.getRuntime() + " min";
                    runtimeTextView.setText(runtime);
                }

                // Enable the plot text to scroll
                plotTextView.setText(vodData.getOverview());
                plotTextView.setMovementMethod(new ScrollingMovementMethod());

                String path = vodData.getPosterUrl(R.string.image_poster_size_small, getContext()).toString();
                Picasso.with(getContext()).load(path).fit().centerInside().into(detailImageView);

                Button favoriteButton = (Button) mainView.findViewById(R.id.favorite_button);
                favoriteButton.setClickable(true);
                favoriteButton.setTag(vodData);

                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
                String favoriteListName = getFavoriteListName(vodData.getVodType());
                Set<String> favoriteVods = prefs.getStringSet(favoriteListName, new HashSet<String>());
                buttonLogic(favoriteButton, favoriteVods.contains(vodData.toJsonString()));

                favoriteButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        VodData vodData = (VodData) v.getTag();
                        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
                        String favoriteListName = getFavoriteListName(vodData.getVodType());
                        Set<String> favoriteVods = prefs.getStringSet(favoriteListName, new HashSet<String>());


                        if (favoriteVods.contains(vodData.toJsonString())){
                            favoriteVods.remove(vodData.toJsonString());
                        }else {
                            favoriteVods.add(vodData.toJsonString());
                        }

                        buttonLogic((Button) v, favoriteVods.contains(vodData.getId()));

                        SharedPreferences.Editor prefsEdit = prefs.edit();
                        prefsEdit.putStringSet(favoriteListName, favoriteVods);
                        prefsEdit.apply();
                    }
                });

            }else {
                Log.w(LOG_TAG, "We don't have a vodData to use");
            }

            ListView listView = (ListView) mainView.findViewById(R.id.trailers_view);
            listView.setAdapter(detailsTrailerAdapter);
            TextView emptyText = (TextView) mainView.findViewById(R.id.empty);
            emptyText.setText(getString(R.string.no_trailers_for_this_vod));
            listView.setEmptyView(emptyText);

            return mainView;
        }

        private String getFavoriteListName(String vodType) {
            if (vodType.equals(getString(R.string.pref_vod_type_movie))){
                return getString(R.string.favorites_movies);
            }else{
                return getString(R.string.favorites_tvs);
            }
        }

        private void buttonLogic(Button favoriteButton, Boolean isClicked) {
            if (isClicked){
                int color = ContextCompat.getColor(getContext(), R.color.blueviolet);
                favoriteButton.getBackground().setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
                favoriteButton.setText(getContext().getString(R.string.remove_from_favorite));

            }else {
                favoriteButton.getBackground().clearColorFilter();
                favoriteButton.setText(getContext().getString(R.string.mark_as_favorite));

            }
            favoriteButton.invalidate();
        }

        private class FetchVodDataTask extends AsyncTask<String, Void, VodData> {

            private final String LOG_TAG = this.getClass().getSimpleName();

            @Override
            protected VodData doInBackground(String... strings) {
                Log.d(LOG_TAG, "doInBackground");

                if (strings != null && strings.length == 2) {

                    String vodType = strings[0];
                    String vodId = strings[1];

                    MovieDbClient movieDbClient = new MovieDbClient(getActivity());

                    return movieDbClient.getVodDataByTypeAndId(vodType, vodId);

                } else {
                    return null;
                }
            }
        }

        private class FetchTrailersDataTask extends AsyncTask<String, Void, ArrayList<TrailersData>> {

            private final String LOG_TAG = this.getClass().getSimpleName();

            @Override
            protected ArrayList<TrailersData> doInBackground(String... strings) {
                Log.d(LOG_TAG, "doInBackground");

                if (strings != null && strings.length == 2) {

                    String vodType = strings[0];
                    String vodId = strings[1];

                    MovieDbClient movieDbClient = new MovieDbClient(getActivity());

                    return movieDbClient.getTrailersByTypeAndId(vodType, vodId);

                } else {
                    return null;
                }
            }

            protected void onPostExecute(ArrayList<TrailersData> result) {
                if (result != null){
                    detailsTrailerAdapter.clear();
                    ArrayList<TrailersData> all = new ArrayList<>(result);
                    detailsTrailerAdapter.addAll(all);
                }
            }
        }
    }
}