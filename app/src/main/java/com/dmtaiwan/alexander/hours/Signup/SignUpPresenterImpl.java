package com.dmtaiwan.alexander.hours.Signup;

/**
 * Created by Alexander on 3/18/2015.
 */
public class SignUpPresenterImpl implements SignUpPresenter, OnSignupFinishedListener {
    private static final String TAG ="SignUpPresenter";
    private SignUpView mSignUpView;
    private Model mModel;

    public SignUpPresenterImpl(SignUpView signUpView) {
        mSignUpView = signUpView;
        mModel = new ModelImpl();
    }
    @Override
    public void signUp(String username, String password, String email) {
        mSignUpView.showProgress();
        mModel.SignUp(username, password, email, this);
    }

    @Override
    public void onSuccess() {
        mSignUpView.hideProgress();
        mSignUpView.logSuccess();
    }

    @Override
    public void onError(String error) {
        mSignUpView.hideProgress();
        mSignUpView.onError(error);

    }
}
