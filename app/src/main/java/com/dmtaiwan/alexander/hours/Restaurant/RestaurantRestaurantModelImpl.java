package com.dmtaiwan.alexander.hours.Restaurant;

import android.util.Log;

import com.dmtaiwan.alexander.hours.Utilities.Restaurant;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

/**
 * Created by Alexander on 3/22/2015.
 */
public class RestaurantRestaurantModelImpl implements RestaurantModel {
    private static final String TAG = "Restaurant Model";
    private Restaurant mRestaurant;

    @Override
    public void StartQueryRestaurant(String queryOrigin, String restaurantId, final OnFinishedListener listener) {
        ParseQuery<Restaurant> query = ParseQuery.getQuery("Restaurant");
        listener.onLoading();
        query.getInBackground(restaurantId, new GetCallback<Restaurant>() {
            @Override
            public void done(Restaurant restaurant, ParseException e) {
                if (e == null) {
                    mRestaurant = restaurant;
                    listener.onFinishedLoading();
                    listener.onSuccess(mRestaurant);

                } else {
                    Log.i(TAG, e.toString());
                    listener.onFinishedLoading();
                }
            }

        });

    }

    @Override
    public void SaveRestaurant(String queryCode, Restaurant restaurant, final OnFinishedListener listener) {
        //If restaurant = null we are creating new restaurant

        listener.onLoading();
        listener.onCreateRestaurant();
        restaurant.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                listener.onFinishedLoading();
                listener.onFinishedCreating();
            }
        });

    }


}
