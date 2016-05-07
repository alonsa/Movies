package com.example.alon_ss.movies;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by alon_ss on 5/5/16.
 */
public class ContainerActivity extends AppCompatActivity {
    private final String LOG_TAG = this.getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.container_detail);
        if (savedInstanceState == null) {
            int id = R.id.container;
            Fragment fragment = new MainActivityFragment();
            getSupportFragmentManager().beginTransaction().add(id, fragment).commit();
        }
    }

}
