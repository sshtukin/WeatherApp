package com.sshtukin.weatherapp;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.fragment_holder);

        if (fragment == null) {
            fragment = new ListOfDaysFragment();
            fragmentManager.beginTransaction()
                    .add(R.id.fragment_holder, fragment)
                    .commit();
        }
    }
}
