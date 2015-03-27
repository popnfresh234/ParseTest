package com.dmtaiwan.alexander.hours.Signup;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;

/**
 * Created by Alexander on 3/18/2015.
 */
public class SignUpActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Setup fragment manager and load fragment
        if (getFragmentManager().findFragmentById(android.R.id.content) == null) {
            Fragment f = SignupActivityFragment.newInstance();
            getFragmentManager().beginTransaction().add(android.R.id.content,f).commit();
        }
    }
}
