package com.dmtaiwan.alexander.parsetest.Main;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;

/**
 * Created by Alexander on 3/21/2015.
 */
public class MainActivity extends Activity {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getFragmentManager().findFragmentById(android.R.id.content) == null) {
            Fragment f = MainActivityFragment.newInstance();
            getFragmentManager().beginTransaction().add(android.R.id.content, f).commit();
        }

    }

}
