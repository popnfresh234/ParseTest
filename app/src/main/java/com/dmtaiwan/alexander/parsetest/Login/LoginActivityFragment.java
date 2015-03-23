package com.dmtaiwan.alexander.parsetest.Login;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.dmtaiwan.alexander.parsetest.Main.MainActivity;
import com.dmtaiwan.alexander.parsetest.R;
import com.dmtaiwan.alexander.parsetest.Signup.SignUpActivity;

/**
 * Created by Alexander on 3/18/2015.
 */
public class LoginActivityFragment extends Fragment implements LoginActivityView, View.OnClickListener{
    protected static LoginActivityFragment newInstance() {
        LoginActivityFragment f = new LoginActivityFragment();
        return f;
    }

    private ProgressBar mProgressBar;
    private EditText mUsername;
    private EditText mPassword;
    private Button mLoginButton;
    private Button mSignupButon;
    private LoginPresenter mPresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        Log.i("LOGIN ACTIVITY", "ON CREATE");

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_login, menu);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_login, container, false);
        //Setup Widgets
        mProgressBar = (ProgressBar) v.findViewById(R.id.progressBar);
        mUsername = (EditText) v.findViewById(R.id.edit_text_username);
        mPassword = (EditText) v.findViewById(R.id.edit_text_password);
        mLoginButton = (Button) v.findViewById(R.id.button_login);
        mLoginButton.setOnClickListener(this);
        mSignupButon = (Button) v.findViewById(R.id.button_signup);
        mSignupButon.setOnClickListener(this);
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

    @Override
    public void showProgress() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        mProgressBar.setVisibility(View.INVISIBLE);
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

            case R.id.button_signup:
                mPresenter.startSignup();
                break;
        }

    }
}
