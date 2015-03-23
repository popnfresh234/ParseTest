package com.dmtaiwan.alexander.parsetest.Restaurant;

import com.dmtaiwan.alexander.parsetest.Utilities.Restaurant;

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
