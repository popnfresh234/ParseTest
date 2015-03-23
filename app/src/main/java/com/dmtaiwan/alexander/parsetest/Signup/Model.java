package com.dmtaiwan.alexander.parsetest.Signup;

/**
 * Created by Alexander on 3/18/2015.
 */
public interface Model {
    public void SignUp (String username, String password, String email, OnSignupFinishedListener listener);
}
