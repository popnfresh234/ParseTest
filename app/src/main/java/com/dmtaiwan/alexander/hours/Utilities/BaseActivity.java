package com.dmtaiwan.alexander.hours.Utilities;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;

import com.dmtaiwan.alexander.hours.List.ListActivity;
import com.dmtaiwan.alexander.hours.List.ListActivityFragment;
import com.dmtaiwan.alexander.hours.Main.MainActivity;
import com.dmtaiwan.alexander.hours.R;

/**
 * Created by Alexander on 3/27/2015.
 */
public class BaseActivity extends Activity {
    protected DrawerLayout mDrawerLayout;
    protected ActionBarDrawerToggle mDrawerToggle;
    protected ListView mDrawerList;
    private boolean mIsDrawerLocked = false;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private static final String TAG = "NavDrawer";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_drawer_layout);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
                Gravity.START);



        //This is about creating custom listview for navigate drawer
        //Implementation for NavigateDrawer HERE !

        ObjectDrawerItem[] drawerItem = new ObjectDrawerItem[8];
        drawerItem[0] = new ObjectDrawerItem(R.drawable.ic_av_home,
                getString(R.string.nav_drawer_home));
        drawerItem[1] = new ObjectDrawerItem(R.drawable.ic_my_favorites,
                getString(R.string.nav_drawer_my_favorites));
        drawerItem[2] = new ObjectDrawerItem(R.drawable.ic_all,
                getString(R.string.nav_drawer_all_restaurants));
        drawerItem[3] = new ObjectDrawerItem(R.drawable.ic_my_restaurants,
                getString(R.string.nav_drawer_my_restaurants));
        drawerItem[4] = new ObjectDrawerItem(R.drawable.ic_added_today,
                getString(R.string.nav_drawer_added_today));
        drawerItem[5] = new ObjectDrawerItem(R.drawable.ic_added_week,
                getString(R.string.nav_drawer_added_this_week));
        drawerItem[6] = new ObjectDrawerItem(R.drawable.ic_updated,
                getString(R.string.nav_drawer_recently_updated));
        drawerItem[7] = new ObjectDrawerItem(R.drawable.ic_city,
                getString(R.string.nav_drawer_city));

        DrawerItemCustomAdapter mDrawerAdapter = new DrawerItemCustomAdapter(this, R.layout.list_item_drawer, drawerItem);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        mDrawerList.setAdapter(mDrawerAdapter);
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());


        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_close, R.string.drawer_open);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.content_frame);
        if (((ViewGroup.MarginLayoutParams) frameLayout.getLayoutParams()).leftMargin == (int) getResources()
                .getDimension(R.dimen.drawer_size)) {
            Log.i("LOCK", "LOCK");
            mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_OPEN,
                    mDrawerList);
            mDrawerLayout.setFocusableInTouchMode(false);
            mDrawerLayout.setScrimColor(Color.TRANSPARENT);
            mIsDrawerLocked = true;
            getActionBar().setDisplayHomeAsUpEnabled(false);
            getActionBar().setHomeButtonEnabled(false);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);

    }

    private class DrawerItemClickListener implements
            ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            selectItem(position);
        }
    }

    private void selectItem(int position) {
        Intent queryIntent = new Intent(this, ListActivity.class);
        switch (position) {
            case 0:
                Log.i(TAG, "Home");
                Intent mainIntent = new Intent(this, MainActivity.class);
                //Clear out other activities when returning home
                mainIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(mainIntent);

                break;
            case 1:
                Log.i(TAG, "Favorites");
                queryIntent.putExtra(ListActivityFragment.QUERY_CODE, ListActivityFragment.FAVORITES);
                startActivity(queryIntent);

                break;
            case 2:
                Log.i(TAG, "All Restaurants");

                queryIntent.putExtra(ListActivityFragment.QUERY_CODE, ListActivityFragment.ALL_RESTAURANTS);
                startActivity(queryIntent);
                break;
            case 3:
                Log.i(TAG, "My Restaurants");
                queryIntent.putExtra(ListActivityFragment.QUERY_CODE, ListActivityFragment.MY_RESTAURANTS);
                startActivity(queryIntent);
                break;
            case 4:
                Log.i(TAG, "Added Today");
                queryIntent.putExtra(ListActivityFragment.QUERY_CODE, ListActivityFragment.RECENT_RESTAURANTS_ONE_DAY);
                startActivity(queryIntent);
                break;
            case 5:
                Log.i(TAG, "Added This Week");
                queryIntent.putExtra(ListActivityFragment.QUERY_CODE, ListActivityFragment.RECENT_RESTAURANTS_ONE_WEEK);
                startActivity(queryIntent);
                break;
            case 6:
                Log.i(TAG, "Recently Updated");
                queryIntent.putExtra(ListActivityFragment.QUERY_CODE, ListActivityFragment.RECENT_UPDATE);
                startActivity(queryIntent);
                break;
            case 7:
                Log.i(TAG, "View By City");
                break;
        }
// update selected item and title, then close the drawer
        mDrawerList.setItemChecked(position, false);
// setTitle(mPlanetTitles[position]);
        if (!mIsDrawerLocked) {
            mDrawerLayout.closeDrawer(mDrawerList);
        }
    }
    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getActionBar().setTitle(mTitle);
    }
    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
// Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
// Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }
    @Override
    public void onBackPressed() {
// TODO Auto-generated method stub
        if (!mIsDrawerLocked) {
            if (mDrawerLayout.isDrawerOpen(Gravity.LEFT)) {
                mDrawerLayout.closeDrawer(Gravity.LEFT);
            } else {
                super.onBackPressed();
            }
        }
        if (mIsDrawerLocked) {
            super.onBackPressed();
        }
    }

}
