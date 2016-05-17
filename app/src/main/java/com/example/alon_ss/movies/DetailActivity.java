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

package com.example.alon_ss.movies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class DetailActivity extends AppCompatActivity {

    private final String LOG_TAG = DetailActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
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

//    @Override
//    protected void onPostResume() {
//
//        getac
//
//        String vodType = getFromPref(R.string.settings_vod_type_key, R.string.pref_default_vod_type);
//        String confQueryType = getFromPref(R.string.settings_search_type_key, R.string.pref_default_search_type);
//        String checkedConfQueryType = getQueryTypeByVodAndConfQueryType(vodType, confQueryType);
//        String titleVodType = vodType.substring(0,1).toUpperCase() + vodType.substring(1);
//        String titleConfQueryType = checkedConfQueryType.substring(0,1).toUpperCase() + checkedConfQueryType.substring(1);
//        setTitle(titleVodType + " - " + titleConfQueryType);
//
//        super.onPostResume();
//    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class DetailFragment extends Fragment {

        private final String LOG_TAG = DetailFragment.class.getSimpleName();

        public DetailFragment() {
            setHasOptionsMenu(true);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

            View view = inflater.inflate(R.layout.fragment_detail, container, false);

            Intent intent = getActivity().getIntent();
            if (intent != null && intent.hasExtra(Intent.EXTRA_TEXT)) {
                MovieData movieData = intent.getParcelableExtra(Intent.EXTRA_TEXT);
                TextView originalTitleTextView = (TextView) view.findViewById(R.id.detail_original_title);
                CharSequence originalTitle = getHtml(getText(R.string.detail_title_original_title), movieData.getTitle());
                originalTitleTextView.setText(originalTitle);


                TextView plotTitleTextView = (TextView) view.findViewById(R.id.detail_plot_synopsis);
                CharSequence plot = getHtml(getText(R.string.detail_title_plot), movieData.getOverview());
                plotTitleTextView.setText(plot);

                TextView ratingTitleTextView = (TextView) view.findViewById(R.id.detail_rating);
                CharSequence rating = getHtml(getText(R.string.detail_title_rating), String.valueOf(movieData.getVoteAverage()));
                ratingTitleTextView.setText(rating);

                TextView releaseDateTitleTextView = (TextView) view.findViewById(R.id.detail_release_date);
                CharSequence releaseDate = getHtml(getText(R.string.detail_title_release_date), movieData.getReleaseDateString());
                releaseDateTitleTextView.setText(releaseDate);
            }

            return view;
        }

        private Spanned getHtml(CharSequence title, String body) {
            return Html.fromHtml("<b>" + title + ": " + "</b>" + "<br />" +  body);
        }

//        @Override
//        public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//            // Inflate the menu; this adds items to the action bar if it is present.
//            // inflater.inflate(R.menu.detailfragment, menu);
//            // Retrieve the share menu item
//            MenuItem menuItem = menu.findItem(R.id.action_share);
//
//            // Get the provider and hold onto it to set/change the share intent.
//            ShareActionProvider mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);
//
//            // Attach an intent to this ShareActionProvider.  You can update this at any time,
//            // like when the user selects a new piece of data they might like to share.
//
//            if (mShareActionProvider != null ) {
//            mShareActionProvider.setShareIntent(createShareForecastIntent());
//            } else {
//                Log.d(LOG_TAG, "Share Action Provider is null?");
//            }
//        }

//        public Intent createShareForecastIntent() {
//
//            Intent shareIntent = new Intent(Intent.ACTION_SEND);
//            shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
//            shareIntent.setType("text/plain");
//            shareIntent.putExtra(Intent.EXTRA_TEXT, mForecastStr + FORECAST_SHARE_HASHTAG);
//            return shareIntent;
//
//        }
    }
}