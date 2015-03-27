package com.dmtaiwan.alexander.parsetest.List;

import android.content.Context;

import com.dmtaiwan.alexander.parsetest.Utilities.CustomParseAdapter;
import com.dmtaiwan.alexander.parsetest.Utilities.Restaurant;
import com.parse.ParseUser;

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
    public void DeleteRestaurant(Restaurant restaurant) {
        mModel.DeleteRestaurant( restaurant, this);
    }

    @Override
    public void SaveUserRelation(ParseUser currentUser) {
        mModel.SaveUserRelation(currentUser, this);
    }

    @Override
    public void onLoading() {
        mListActivityView.showProgress();
    }

    @Override
    public void onFinishedLoading() {
        mListActivityView.hideProgress();
    }

    public void onFinishedUpdatingData() {
        mListActivityView.onFinishedUpdatingData();
    }


}
