package com.dmtaiwan.alexander.parsetest.Restaurant;

import com.dmtaiwan.alexander.parsetest.Utilities.Restaurant;

/**
 * Created by Alexander on 3/22/2015.
 */
public interface Model {
    public void StartQueryRestaurant(String queryOrigin, String restaurantId, final OnFinishedListener listener);

    public void SaveRestaurant(String queryCode, Restaurant restaurant, final OnFinishedListener listener);
    
}
