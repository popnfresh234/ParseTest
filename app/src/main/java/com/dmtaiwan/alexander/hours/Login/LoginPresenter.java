package com.dmtaiwan.alexander.hours.Login;

/**
 * Created by Alexander on 3/18/2015.
 */
public interface LoginPresenter {
    public void validateCredentials(String username, String password);

    public void startSignup();

    public void startLogin();
}
