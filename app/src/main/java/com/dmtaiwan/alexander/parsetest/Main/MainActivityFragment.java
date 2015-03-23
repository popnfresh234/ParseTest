package com.dmtaiwan.alexander.parsetest.Main;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.dmtaiwan.alexander.parsetest.List.ListActivity;
import com.dmtaiwan.alexander.parsetest.List.ListActivityFragment;
import com.dmtaiwan.alexander.parsetest.Login.LoginActivity;
import com.dmtaiwan.alexander.parsetest.R;
import com.dmtaiwan.alexander.parsetest.Restaurant.RestaurantActivity;
import com.dmtaiwan.alexander.parsetest.Restaurant.RestaurantFragment;
import com.parse.ParseUser;

/**
 * Created by Alexander on 3/21/2015.
 */
public class MainActivityFragment extends Fragment implements View.OnClickListener{

    protected static MainActivityFragment newInstance() {
        MainActivityFragment f = new MainActivityFragment();
        return f;
    }

    private final String TAG = "MainActivityFragment";
    private EditText mSearchField;
    private Button mSearchButton;
    private Button mAllRestaurantsButton;
    private ParseUser mCurrentUser;
    private String mCurrentUserId;

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_logout:
                ParseUser.logOut();
                navigateToLogin();
                break;
            case R.id.menu_item_new_restaurant:
                Intent i = new Intent(getActivity(), RestaurantActivity.class);
                i.putExtra(ListActivityFragment.QUERY_CODE, RestaurantFragment.NEW_RESTAURANT);
                startActivity(i);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
        //Check if a user is logged in, if not boot to login screen
        mCurrentUser = ParseUser.getCurrentUser();

        if (mCurrentUser == null) {
            navigateToLogin();
        }else{
            mCurrentUserId = mCurrentUser.getObjectId().toString();
        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main, container, false);

        mSearchField = (EditText)v.findViewById(R.id.edit_text_search);
        mSearchButton = (Button) v.findViewById(R.id.button_search);
        mSearchButton.setOnClickListener(this);
        mAllRestaurantsButton = (Button)v.findViewById(R.id.button_all_restaurants);
        mAllRestaurantsButton.setOnClickListener(this);
        return v;
    }

    @Override
    public void onClick(View v) {
        Intent i = new Intent(getActivity(), ListActivity.class);
        int id = v.getId();
        switch (id) {
            case R.id.button_all_restaurants:
                i.putExtra(ListActivityFragment.QUERY_CODE, ListActivityFragment.ALL_RESTAURATNS);
                startActivity(i);
                break;

            case R.id.button_search:
                //Get the string in mSearchField and convert to lower case for search
                String searchQuery = mSearchField.getText().toString().toLowerCase();
                i.putExtra(ListActivityFragment.QUERY_CODE, ListActivityFragment.SEARCH);
                i.putExtra(ListActivityFragment.SEARCH, searchQuery);
                startActivity(i);
                break;
        }
    }

    private void navigateToLogin() {
        Intent i = new Intent(getActivity(), LoginActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
    }
}
