package com.dmtaiwan.alexander.hours.Signup;

/**
 * Created by Alexander on 3/18/2015.
 */
public interface SignUpView {
    public void showProgress();

    public void hideProgress();

    public void logSuccess();

    public void onError(String error);
}
