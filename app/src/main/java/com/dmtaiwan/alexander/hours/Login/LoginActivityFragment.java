package com.dmtaiwan.alexander.hours.Login;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.dmtaiwan.alexander.hours.Main.MainActivity;
import com.dmtaiwan.alexander.hours.R;
import com.dmtaiwan.alexander.hours.Signup.SignUpActivity;

/**
 * Created by Alexander on 3/18/2015.
 */
public class LoginActivityFragment extends Fragment implements LoginActivityView, View.OnClickListener{
    protected static LoginActivityFragment newInstance() {
        LoginActivityFragment f = new LoginActivityFragment();
        return f;
    }

    private EditText mUsername;
    private EditText mPassword;
    private Button mLoginButton;
    private TextView mSignUpTextView;
    private TextView mForgotPasswordTextView;
    private LoginPresenter mPresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("LOGIN ACTIVITY", "ON CREATE");

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_login, container, false);
        //Setup Widgets
        mUsername = (EditText) v.findViewById(R.id.edit_text_username);
        mPassword = (EditText) v.findViewById(R.id.edit_text_password);
        mLoginButton = (Button) v.findViewById(R.id.button_login);
        mLoginButton.setOnClickListener(this);
        mSignUpTextView = (TextView) v.findViewById(R.id.text_view_signup);
        mSignUpTextView.setOnClickListener(this);
        mPresenter = new LoginPresenterImpl(this);
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        //Clear fields when coming back from signup activity
        mUsername.setText("");
        mPassword.setText("");
    }

    public void onPause() {
        super.onPause();
        getActivity().setProgressBarIndeterminateVisibility(false);
    }

    @Override
    public void showProgress() {
        getActivity().setProgressBarIndeterminateVisibility(true);
    }

    @Override
    public void hideProgress() {
        getActivity().setProgressBarIndeterminateVisibility(false);
    }

    @Override
    public void onSuccess(){
        mPresenter.startLogin();
    }

    @Override
    public void startSignup() {

        Intent i = new Intent(getActivity(), SignUpActivity.class);
        startActivity(i);
    }

    @Override
    public void startLogin() {
        Log.i("YAY", "LOGIN");
        Intent i = new Intent(getActivity(), MainActivity.class);
        startActivity(i);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.button_login:
                mPresenter.validateCredentials(mUsername.getText().toString(), mPassword.getText().toString());
                break;

            case R.id.text_view_signup:
                mPresenter.startSignup();
                break;
        }

    }
}
