package com.dmtaiwan.alexander.parsetest.Restaurant;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.Window;

/**
 * Created by Alexander on 3/22/2015.
 */
public class RestaurantActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        if (getFragmentManager().findFragmentById(android.R.id.content) == null) {
            Fragment f = RestaurantFragment.newInstance();
            getFragmentManager().beginTransaction().add(android.R.id.content, f).commit();
        }
    }
}
