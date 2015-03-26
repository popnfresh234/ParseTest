package com.dmtaiwan.alexander.parsetest.Login;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.Window;


public class LoginActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

        //Setup fragment manager and load fragment
        if (getFragmentManager().findFragmentById(android.R.id.content) == null) {
            Fragment f = LoginActivityFragment.newInstance();
            getFragmentManager().beginTransaction().add(android.R.id.content,f).commit();
        }
    }
}
