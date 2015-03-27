package com.dmtaiwan.alexander.hours.Signup;

import android.util.Log;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

/**
 * Created by Alexander on 3/18/2015.
 */
public class ModelImpl implements Model {
    private static final String TAG = "Model";

    @Override
    public void SignUp(final String username, final String password, final String email, final OnSignupFinishedListener listener) {
        ParseUser newUser = new ParseUser();

        //Trim strings from Edit Text Fields
        String usernameTrimmed = username.trim();
        String passwordTrimmed = password.trim();
        String emailTrimmed = email.trim();

        //Check if any fields are blank

        if (usernameTrimmed.isEmpty() | passwordTrimmed.isEmpty() || emailTrimmed.isEmpty()) {
            listener.onError("Credential Error");
            Log.i(TAG, "signup error");
        } else {
            newUser.setUsername(username);
            newUser.setPassword(password);
            newUser.setEmail(email);

            newUser.signUpInBackground(new SignUpCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {
                        //Success
                        listener.onSuccess();
                    } else {
                        //failure
                        Log.i(TAG, e.toString());
                    }
                }
            });
        }
    }
}
