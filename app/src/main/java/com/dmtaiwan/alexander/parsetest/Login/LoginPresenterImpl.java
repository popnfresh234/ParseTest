package com.dmtaiwan.alexander.parsetest.Login;

/**
 * Created by Alexander on 3/18/2015.
 */
public class LoginPresenterImpl implements LoginPresenter, OnLoginFinishedListener {
    private LoginActivityView mLoginView;
    private Model mModel;

    public LoginPresenterImpl(LoginActivityView loginView){
        mLoginView = loginView;
        mModel = new ModelImpl();
    }


    @Override
    public void validateCredentials(String username, String password) {
        mLoginView.showProgress();
        mModel.login(username, password, this);
    }

    @Override
    public void startSignup() {
        mLoginView.startSignup();
    }

    @Override
    public void startLogin() {
        mLoginView.startLogin();
    }


    //Communicates with Model to determine login status

    @Override
    public void onError() {
        mLoginView.hideProgress();
    }

    @Override
    public void onSuccess() {
        mLoginView.onSuccess();
        mLoginView.hideProgress();
    }
}
