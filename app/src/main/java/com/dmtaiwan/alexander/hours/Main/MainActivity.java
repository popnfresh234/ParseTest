package com.dmtaiwan.alexander.hours.Main;

import android.app.Fragment;
import android.os.Bundle;

import com.dmtaiwan.alexander.hours.R;
import com.dmtaiwan.alexander.hours.Utilities.BaseActivity;

/**
 * Created by Alexander on 3/21/2015.
 */
public class MainActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getFragmentManager().findFragmentById(R.id.content_frame) == null) {
            Fragment f = MainActivityFragment.newInstance();
            getFragmentManager().beginTransaction().add(R.id.content_frame, f).commit();
        }
    }





}

