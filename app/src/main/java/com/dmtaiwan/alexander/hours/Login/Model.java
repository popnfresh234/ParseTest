package com.dmtaiwan.alexander.hours.Login;

/**
 * Created by Alexander on 3/18/2015.
 */
public interface Model {
    public void login(String username, String password, OnLoginFinishedListener listener);
}
