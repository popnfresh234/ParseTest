package com.dmtaiwan.alexander.parsetest.Restaurant;

import com.dmtaiwan.alexander.parsetest.Utilities.Restaurant;

/**
 * Created by Alexander on 3/22/2015.
 */
public class PresenterImpl implements Presenter, OnFinishedListener{
    private RestaurantView mRestaurantView;
    private RestaurantModel mRestaurantModel;

    public PresenterImpl(RestaurantView restaurantView) {
        mRestaurantView = restaurantView;
        mRestaurantModel = new RestaurantRestaurantModelImpl();
    }

    @Override
    public void
    StartQueryRestaurant(String queryOrigin, String restaurantId) {
        mRestaurantView.showProgress();
        mRestaurantModel.StartQueryRestaurant(queryOrigin, restaurantId, this);
    }

    @Override
    public void SaveRestaurant(String queryCode, Restaurant restaurant) {
        mRestaurantModel.SaveRestaurant(queryCode, restaurant, this);
    }


    @Override
    public void onCreateRestaurant() {
        mRestaurantView.onCreateRestaurant();
    }

    @Override
    public void onFinishedCreating() {
        mRestaurantView.onFinishedCreating();
    }

    @Override
    public void onLoading() {
        mRestaurantView.onLoading();
    }

    @Override
    public void onFinishedLoading() {
        mRestaurantView.onFinishedLoading();
    }

    @Override
    public void onSuccess(Restaurant restaurant) {
        mRestaurantView.onSuccess(restaurant);
    }
}
