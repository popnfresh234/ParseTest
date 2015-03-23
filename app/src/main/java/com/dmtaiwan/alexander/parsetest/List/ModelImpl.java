package com.dmtaiwan.alexander.parsetest.List;

import android.content.Context;

import com.dmtaiwan.alexander.parsetest.Utilities.CustomParseAdapter;
import com.dmtaiwan.alexander.parsetest.Utilities.Restaurant;
import com.parse.ParseQueryAdapter;

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
                listener.onSuccess();
            }
        });
        return parseAdapter;
    }
}
