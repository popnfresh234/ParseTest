package com.dmtaiwan.alexander.hours.List;

import android.content.Context;
import android.util.Log;

import com.dmtaiwan.alexander.hours.Utilities.CustomParseAdapter;
import com.dmtaiwan.alexander.hours.Utilities.Restaurant;
import com.parse.DeleteCallback;
import com.parse.ParseException;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.List;

/**
 * Created by Alexander on 3/19/2015.
 */
public class ModelImpl implements Model {

    @Override
    public CustomParseAdapter queryParse(Context context, String queryCode, String query, final OnFinishedListener listener) {

        CustomParseAdapter parseAdapter = new CustomParseAdapter(context, queryCode, query);
        parseAdapter.setObjectsPerPage(100);
        parseAdapter.addOnQueryLoadListener(new ParseQueryAdapter.OnQueryLoadListener<Restaurant>() {
            @Override
            public void onLoading() {
                listener.onLoading();
            }

            @Override
            public void onLoaded(List<Restaurant> restaurants, Exception e) {
                listener.onFinishedLoading();
            }
        });
        return parseAdapter;
    }

    @Override
    public void DeleteRestaurant(final Restaurant restaurant, final OnFinishedListener listener) {

        restaurant.deleteInBackground(new DeleteCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    listener.onFinishedLoading();
                    listener.onFinishedUpdatingData();

                }else {
                    Log.i("PARSE ERROR", e.toString());
                    listener.onFinishedLoading();
                }

            }
        });
    }

    @Override
    public void SaveUserRelation(ParseUser currentUser, final OnFinishedListener listener) {
        currentUser.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                listener.onFinishedLoading();
                listener.onFinishedUpdatingData();
            }
        });
    }
}
