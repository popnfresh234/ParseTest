package com.dmtaiwan.alexander.parsetest.Signup;

import android.app.Fragment;
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
import android.widget.Toast;

import com.dmtaiwan.alexander.parsetest.R;

/**
 * Created by Alexander on 3/18/2015.
 */
public class SignupActivityFragment extends Fragment implements SignUpView, View.OnClickListener {
    protected static SignupActivityFragment newInstance() {
        SignupActivityFragment f = new SignupActivityFragment();
        return f;
    }

    private static final String TAG = "SignUpFragment";

    private ProgressBar mProgressBar;
    private EditText mUsername;
    private EditText mPassword;
    private EditText mEmail;
    private Button mSignupButton;
    private Button mCancelButton;

    private SignUpPresenter mPresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_login, menu);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_signup, container, false);

        //Setup widgets
        mProgressBar = (ProgressBar) v.findViewById(R.id.progressBar);
        mUsername = (EditText) v.findViewById(R.id.edit_text_username);
        mPassword = (EditText) v.findViewById(R.id.edit_text_password);
        mEmail = (EditText) v.findViewById(R.id.edit_text_email);
        mSignupButton = (Button) v.findViewById(R.id.button_signup);
        mSignupButton.setOnClickListener(this);
        mCancelButton = (Button) v.findViewById(R.id.button_cancel);
        mCancelButton.setOnClickListener(this);

        mPresenter = new SignUpPresenterImpl(this);

        return v;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.button_signup:
//                Log.i(tag, "signup");

                //Check for empty fields
                mPresenter.signUp(mUsername.getText().toString(),mPassword.getText().toString(),mEmail.getText().toString());
                break;

            case R.id.button_cancel:
                getActivity().finish();
                break;
        }
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
    public void logSuccess() {
        Log.i(TAG, "success");
    }

    @Override
    public void onError(String error) {
        Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
    }
}
