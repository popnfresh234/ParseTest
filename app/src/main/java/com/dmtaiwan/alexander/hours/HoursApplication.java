package com.dmtaiwan.alexander.hours;

import android.app.Application;

import com.dmtaiwan.alexander.hours.Utilities.Restaurant;
import com.parse.Parse;
import com.parse.ParseObject;

/**
 * Created by Alexander on 3/18/2015.
 */
public class HoursApplication extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
        //Initialize Parse
        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);
        ParseObject.registerSubclass(Restaurant.class);
        Parse.initialize(this, "hBnh7k8KhNrDiQIyXELw5tnfvBa4eLXBjgzmHOjL",
                "5KLdf0eGFBB0LPcZ1fFHaq6IEE2TtrH6yJjoJ21m");
    }
}
