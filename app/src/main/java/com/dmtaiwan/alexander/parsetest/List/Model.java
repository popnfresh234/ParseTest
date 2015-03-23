package com.dmtaiwan.alexander.parsetest.List;

import android.content.Context;

import com.dmtaiwan.alexander.parsetest.Utilities.CustomParseAdapter;

/**
 * Created by Alexander on 3/19/2015.
 */
public interface Model {

    public CustomParseAdapter queryParse(Context context, String queryCode, String query, OnFinishedListener listener);

}
