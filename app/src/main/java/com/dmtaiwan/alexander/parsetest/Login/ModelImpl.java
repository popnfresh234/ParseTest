package com.dmtaiwan.alexander.parsetest.Login;

import android.util.Log;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

/**
 * Created by Alexander on 3/18/2015.
 */
public class ModelImpl implements Model {
    @Override
    public void login(final String username, final String password,  final OnLoginFinishedListener listener) {
        ParseUser.logInInBackground(username, password, new LogInCallback() {
            @Override
            public void done(ParseUser parseUser, ParseException e) {
                if (parseUser != null) {
                    listener.onSuccess();
                }else {
                    Log.e("shit", e.toString());
                    listener.onError();
                }
            }
        });
    }
}
