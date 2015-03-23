package com.dmtaiwan.alexander.parsetest.List;

import android.content.Context;

import com.dmtaiwan.alexander.parsetest.Utilities.CustomParseAdapter;

/**
 * Created by Alexander on 3/19/2015.
 */
public class ListPresenterImpl implements ListPresenter, OnFinishedListener {
    private ListActivityView mListActivityView;
    private Model mModel;

    public ListPresenterImpl (ListActivityView listActivityView) {
        mListActivityView = listActivityView;
        mModel = new ModelImpl();
    }

    @Override
    public CustomParseAdapter createAdapter(Context context, String queryCode, String query) {
        return mModel.queryParse(context, queryCode, query, this);
    }

    @Override
    public void onLoading() {
        mListActivityView.showProgress();
    }

    @Override
    public void onSuccess() {
        mListActivityView.hideProgress();
    }
}
