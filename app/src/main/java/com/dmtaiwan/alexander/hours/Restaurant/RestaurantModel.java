package com.dmtaiwan.alexander.hours.Restaurant;

import com.dmtaiwan.alexander.hours.Utilities.Restaurant;

/**
 * Created by Alexander on 3/22/2015.
 */
public interface RestaurantModel {
    public void StartQueryRestaurant(String queryOrigin, String restaurantId, final OnFinishedListener listener);

    public void SaveRestaurant(String queryCode, Restaurant restaurant, final OnFinishedListener listener);
    
}
