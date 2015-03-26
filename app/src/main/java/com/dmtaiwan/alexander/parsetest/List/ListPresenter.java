package com.dmtaiwan.alexander.parsetest.List;

import android.content.Context;

import com.dmtaiwan.alexander.parsetest.Utilities.CustomParseAdapter;
import com.dmtaiwan.alexander.parsetest.Utilities.Restaurant;
import com.parse.ParseUser;

/**
 * Created by Alexander on 3/19/2015.
 */
public interface ListPresenter {

    public CustomParseAdapter createAdapter(Context context, String queryCode, String query);

    public void DeleteRestaurant(Restaurant restaurant);

    public void SaveUserRelation(ParseUser currentUser);
}
