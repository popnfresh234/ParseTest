package com.dmtaiwan.alexander.hours.Restaurant;

import com.dmtaiwan.alexander.hours.Utilities.Restaurant;

/**
 * Created by Alexander on 3/22/2015.
 */
public interface OnFinishedListener {

    public void onCreateRestaurant();

    public void onFinishedCreating();

    public void onLoading();

    public void onFinishedLoading();

    public void onSuccess(Restaurant restaurant);
}
