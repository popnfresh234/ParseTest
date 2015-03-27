package com.dmtaiwan.alexander.parsetest.List;

import android.app.ListFragment;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;

import com.dmtaiwan.alexander.parsetest.Login.LoginActivity;
import com.dmtaiwan.alexander.parsetest.R;
import com.dmtaiwan.alexander.parsetest.Restaurant.RestaurantActivity;
import com.dmtaiwan.alexander.parsetest.Restaurant.RestaurantFragment;
import com.dmtaiwan.alexander.parsetest.Utilities.CustomParseAdapter;
import com.dmtaiwan.alexander.parsetest.Utilities.ParseConstants;
import com.dmtaiwan.alexander.parsetest.Utilities.Restaurant;
import com.parse.ParseRelation;
import com.parse.ParseUser;

/**
 * Created by Alexander on 3/19/2015.
 */
public class ListActivityFragment extends ListFragment implements ListActivityView {

    private static final String ADMIN = "e7So6F4ytk";
    private static final String ANNIEADMIN = "45LT0GnGVU";

    // Query codes for arguments for identifying how fragment was created
    public static final String QUERY_CODE = "query_code";
    public static final String MY_RESTAURANTS = "my_restaurants";
    public static final String ALL_RESTAURANTS = "all_restaurants";
    public static final String SEARCH = "search";
    public static final String RECENT_RESTAURANTS_ONE_DAY = "recent_restaurants_one_day";
    public static final String RECENT_RESTAURANTS_ONE_WEEK = "recent_restaurants_one_week";
    public static final String RECENT_UPDATE = "recent_update";
    public static final String FAVORITES = "favorites";

    private static final String TAG = "ListActivityFragment";
    private CustomParseAdapter mParseAdapter;
    private ListView mListView;
    private ListPresenter mPresenter;
    private String mQueryType;
    private String mQueryString;
    private ParseUser mCurrentUser;
    private String mCurrentUserId;

    protected static ListActivityFragment newInstance() {
        ListActivityFragment f = new ListActivityFragment();
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mCurrentUser = ParseUser.getCurrentUser();
        if (mCurrentUser == null) {
            navigateToLogin();
        } else {
            mCurrentUserId = mCurrentUser.getObjectId().toString();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_list, menu);

        //Get the search view
        SearchManager searchManager = (SearchManager) getActivity()
                .getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.menu_search)
                .getActionView();
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getActivity().getComponentName()));
        searchView.setQuery("", false);
        searchView.setIconified(true);
        searchView.clearFocus();

        // Get the search icon ID and set its icon
        int searchIconId = searchView.getContext().getResources()
                .getIdentifier("android:id/search_button", null, null);
        ImageView searchIcon = (ImageView) searchView
                .findViewById(searchIconId);
        searchIcon.setImageResource(R.drawable.ic_action_search);

        // Set the background so it matches theme
        int searchPlateId = searchView.getContext().getResources()
                .getIdentifier("android:id/search_plate", null, null);
        searchView.findViewById(searchPlateId).setBackgroundResource(
                R.drawable.apptheme_textfield_activated_holo_light);

        //Set the listener

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            // Query on text submitted
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.i(TAG, query);
                mParseAdapter = mPresenter.createAdapter(getActivity(), SEARCH, query);

                mListView.setAdapter(mParseAdapter);


                return false;
            }

            // Also query on text changed, immediate results
            @Override
            public boolean onQueryTextChange(String newText) {
                Log.i(TAG, newText);
                mParseAdapter = mPresenter.createAdapter(getActivity(), SEARCH, newText);
                mListView.setAdapter(mParseAdapter);
                return false;
            }
        });
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
            case ALL_RESTAURANTS:
                //Setup parse query adapter to query for all restaurants;
                //Here we pass in null for the queryString parameter in the parse adapter constructor as there is no string associated with this query.

                mParseAdapter = mPresenter.createAdapter(getActivity(), ALL_RESTAURANTS, null);

                //Set stars if restaurant is in the favorites list
                break;

            case SEARCH:
                //setup parse query adapter to search for keywords
                mParseAdapter = mPresenter.createAdapter(getActivity(), SEARCH, mQueryString);

                break;

            default:
                mParseAdapter = mPresenter.createAdapter(getActivity(), ALL_RESTAURANTS, null);
                break;
        }

        //Set listView
        mListView = (ListView) v.findViewById(R.id.list);
        mListView.setAdapter(mParseAdapter);

        mListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);


        //Set listener to load more data when the end of scrollview is reached
        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                int threshold = 1;
                int count = mListView.getCount();

                if (scrollState == SCROLL_STATE_IDLE) {
                    if (mListView.getLastVisiblePosition() >= count
                            - threshold) {
                        mParseAdapter.loadNextPage();
                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {


            }
        });

        //Set the choice mode for listview


        mParseAdapter.loadObjects();


        return v;
    }

    @Override
    public void onResume() {
        super.onResume();

        //Set contextual choice menu depending on whether or not user is Admin

        ListView listView = getListView();
        listView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE_MODAL);
        if (mCurrentUserId.equals(ADMIN) || mCurrentUserId.equals(ANNIEADMIN)) {
            listView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
                @Override
                public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {

                }

                @Override
                public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                    MenuInflater inflater = mode.getMenuInflater();
                    inflater.inflate(R.menu.menu_list_context_admin, menu);
                    return true;

                }

                @Override
                public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                    return false;
                }

                @Override
                public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                    int id = item.getItemId();
                    switch (id) {
                        case R.id.menu_item_delete_restaurant:


                            for (int i = mParseAdapter.getCount() - 1; i >= 0; i--) {
                                if (getListView().isItemChecked(i)) {
                                    Restaurant r = (Restaurant) mParseAdapter.getItem(i);
                                    Log.i(TAG, r.toString());
                                    mPresenter.DeleteRestaurant(r);


                                }
                            }
                    }
                    mode.finish();
                    return true;
                }

                @Override
                public void onDestroyActionMode(ActionMode mode) {

                }
            });
        } else {
            //If not admin, set the choice mode
            listView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
                @Override
                public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {

                }

                @Override
                public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                    MenuInflater inflater = mode.getMenuInflater();
                    inflater.inflate(R.menu.menu_list_context, menu);
                    return true;
                }

                @Override
                public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                    return false;
                }

                @Override
                public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                    int id = item.getItemId();
                    switch (id) {
                        case R.id.menu_item_favourite_restaurant:
                            for (int i = mParseAdapter.getCount() - 1; i >= 0; i--) {
                                if (getListView().isItemChecked(i)) {
                                    Restaurant r = (Restaurant) mParseAdapter.getItem(i);
                                    ParseUser user = ParseUser.getCurrentUser();
                                    ParseRelation<Restaurant> relation = user.getRelation(ParseConstants.RELATION_FAVORITE);
                                    relation.add(r);
                                    mPresenter.SaveUserRelation(user);
                                }

                            }
                            mode.finish();
                            return true;
                        case R.id.menu_item_unfavourite_restaurant:
                            for (int i = mParseAdapter.getCount() - 1; i >= 0; i--) {
                                if (getListView().isItemChecked(i)) {
                                    Restaurant r = (Restaurant) mParseAdapter.getItem(i);
                                    ParseUser user = ParseUser.getCurrentUser();
                                    ParseRelation<Restaurant> relation = user.getRelation(ParseConstants.RELATION_FAVORITE);
                                    relation.remove(r);
                                    mPresenter.SaveUserRelation(user);
                                }
                            }
                            mode.finish();
                            return true;
                        default:
                            return false;
                    }
                }

                @Override
                public void onDestroyActionMode(ActionMode mode) {

                }
            });
        }
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

    public void onFinishedUpdatingData() {

        mParseAdapter = mPresenter.createAdapter(getActivity(), ALL_RESTAURANTS, null);
        setListAdapter(mParseAdapter);

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
