package com.dmtaiwan.alexander.hours.List;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.dmtaiwan.alexander.hours.R;
import com.dmtaiwan.alexander.hours.Utilities.BaseActivity;

/**
 * Created by Alexander on 3/19/2015.
 */
public class ListActivity extends BaseActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getFragmentManager().findFragmentById(R.id.content_frame) == null) {
            Fragment f = ListActivityFragment.newInstance();
            getFragmentManager().beginTransaction().add(R.id.content_frame, f).commit();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.i("NEW INTENT", "NEW INTENT");
        setIntent(intent);
        Fragment f = getFragmentManager().findFragmentById(R.id.content_frame);
        getFragmentManager().beginTransaction().remove(f).commit();
        Fragment newF = ListActivityFragment.newInstance();
        getFragmentManager().beginTransaction().add(R.id.content_frame, newF).commit();
    }
}
