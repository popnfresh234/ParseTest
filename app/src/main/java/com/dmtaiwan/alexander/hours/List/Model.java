package com.dmtaiwan.alexander.hours.List;

import android.content.Context;

import com.dmtaiwan.alexander.hours.Utilities.CustomParseAdapter;
import com.dmtaiwan.alexander.hours.Utilities.Restaurant;
import com.parse.ParseUser;

/**
 * Created by Alexander on 3/19/2015.
 */
public interface Model {

    public CustomParseAdapter queryParse(Context context, String queryCode, String query, OnFinishedListener listener);

    public void DeleteRestaurant( Restaurant restaurant, OnFinishedListener listener);

    public void SaveUserRelation(ParseUser currentUser, OnFinishedListener listener);

}
