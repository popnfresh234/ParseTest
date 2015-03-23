package com.dmtaiwan.alexander.parsetest.List;

import android.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;

import com.dmtaiwan.alexander.parsetest.Login.LoginActivity;
import com.dmtaiwan.alexander.parsetest.R;
import com.dmtaiwan.alexander.parsetest.Restaurant.RestaurantActivity;
import com.dmtaiwan.alexander.parsetest.Restaurant.RestaurantFragment;
import com.dmtaiwan.alexander.parsetest.Utilities.CustomParseAdapter;
import com.dmtaiwan.alexander.parsetest.Utilities.Restaurant;
import com.parse.ParseUser;

/**
 * Created by Alexander on 3/19/2015.
 */
public class ListActivityFragment extends ListFragment implements ListActivityView {

    // Query codes for arguments for identifying how fragment was created
    public static final String QUERY_CODE = "query_code";
    public static final String MY_RESTAURATNS = "my_restaurants";
    public static final String ALL_RESTAURATNS = "all_restaurants";
    public static final String SEARCH = "search";
    public static final String RECENT_RESTAURANTS_ONE_DAY = "recent_restaurants_one_day";
    public static final String RECENT_RESTAURANTS_ONE_WEEK = "recent_restaurants_one_week";
    public static final String RECENT_UPDATE = "recent_update";
    public static final String FAVORITES = "favorites";

    private static final String TAG = "ListActivityFragment";
    private CustomParseAdapter mParseAdapter;
    private ListView listView;
    private ListPresenter mPresenter;
    private String mQueryType;
    private String mQueryString;

    protected static ListActivityFragment newInstance() {
        ListActivityFragment f = new ListActivityFragment();
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_list, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.menu_item_new_restaurant:
                Intent i = new Intent(getActivity(), RestaurantActivity.class);
                i.putExtra(QUERY_CODE, RestaurantFragment.NEW_RESTAURANT);
                startActivity(i);
                break;

            case R.id.action_logout:
                ParseUser.logOut();
                navigateToLogin();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_list, container, false);
        mPresenter = new ListPresenterImpl(this);
        mQueryType = GetIntentQueryCode();
        Log.i(TAG, mQueryType);
        //If there is a query string passed in from MainActivityFragment, retrieve it
        mQueryString = getActivity().getIntent().getStringExtra(SEARCH);

        //Deal with how to populate the listView based on the queryCode
        switch (mQueryType) {
            case ALL_RESTAURATNS:
                //Setup parse query adapter to query for all restaurants;
                //Here we pass in null for the queryString parameter in the parse adapter constructor as there is no string associated with this query.
                mParseAdapter = mPresenter.createAdapter(getActivity(), ALL_RESTAURATNS, null);
                break;

            case SEARCH:
                //setup parse query adapter to search for keywords
                mParseAdapter = mPresenter.createAdapter(getActivity(), SEARCH, mQueryString);
                break;

            default:
                mParseAdapter = mPresenter.createAdapter(getActivity(), ALL_RESTAURATNS, null);
                break;
        }

        //Set listView

        listView = (ListView) v.findViewById(R.id.list);
        listView.setAdapter(mParseAdapter);

        //Set listener to load more data when the end of scrollview is reached
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                int threshold = 1;
                int count = listView.getCount();

                if (scrollState == SCROLL_STATE_IDLE) {
                    if (listView.getLastVisiblePosition() >= count
                            - threshold) {
                        mParseAdapter.loadNextPage();
                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {


            }
        });

        mParseAdapter.loadObjects();


        return v;
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
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        //Get the restaurant from list item, launch restaurant activity with that ID
        Restaurant r = mParseAdapter.getItem(position);

        //get the query code to figure out which activity this ListView was created from
        String queryCode = GetIntentQueryCode();
        //get the restaurant's ID code
        String restaurantId = r.getObjectId();
        Intent i = new Intent(getActivity(), RestaurantActivity.class);
        i.putExtra(QUERY_CODE, queryCode);
        i.putExtra(RestaurantFragment.EXTRA_RESTAURANT_ID, restaurantId);
        startActivity(i);

    }

    private String GetIntentQueryCode() {
        if (getActivity().getIntent() != null) {
            String queryCode = getActivity().getIntent().getStringExtra(QUERY_CODE);
            return queryCode;
        } else return null;
    }

    private void navigateToLogin() {
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
