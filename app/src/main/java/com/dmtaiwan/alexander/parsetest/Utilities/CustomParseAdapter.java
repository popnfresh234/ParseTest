package com.dmtaiwan.alexander.parsetest.Utilities;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dmtaiwan.alexander.parsetest.List.ListActivityFragment;
import com.dmtaiwan.alexander.parsetest.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseRelation;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alexander on 3/20/2015.
 */
public class CustomParseAdapter extends ParseQueryAdapter<Restaurant> {
    public CustomParseAdapter(Context context, final String queryCode, final String queryString) {

// Use the QueryFactory to construct a PQA that will show all restaurants

        super(context, new ParseQueryAdapter.QueryFactory<Restaurant>() {
            public ParseQuery create() {

                ParseQuery<Restaurant> query = new ParseQuery<Restaurant>("Restaurant");
                //query for all restaurants
                if (queryCode == ListActivityFragment.ALL_RESTAURANTS) {
                    query.whereExists(ParseConstants.KEY_RESTAURANT_TITLE);
                    query.addAscendingOrder(ParseConstants.KEY_RESTAURANT_LOWERCASE_TITLE);
                }


                if (queryCode == ListActivityFragment.SEARCH) {
                    ParseQuery<Restaurant> queryTitle = new ParseQuery<Restaurant>(
                            "Restaurant");
                    queryTitle.whereContains(
                            ParseConstants.KEY_RESTAURANT_LOWERCASE_TITLE, queryString);

                    ParseQuery<Restaurant> queryAddress = new ParseQuery<Restaurant>(
                            "Restaurant");
                    queryAddress.whereContains(
                            ParseConstants.KEY_ADDRESS_LOWER_CASE, queryString);

                    ParseQuery<Restaurant> queryCity = new ParseQuery<Restaurant>(
                            "Restaurant");
                    queryCity.whereContains(
                            ParseConstants.KEY_CITY_LOWERCASE, queryString);

                    List<ParseQuery<Restaurant>> queries = new ArrayList<ParseQuery<Restaurant>>();
                    queries.add(queryTitle);
                    queries.add(queryAddress);
                    queries.add(queryCity);

                    query = ParseQuery.or(queries);
                    query.addAscendingOrder(ParseConstants.KEY_RESTAURANT_LOWERCASE_TITLE);

                }
                return query;

            }

        });
    }


    @Override
    public View getItemView(final Restaurant restaurant, View v, ViewGroup parent) {
        if (v == null) {
            v = View.inflate(getContext(), R.layout.parse_list_item, null);
        }
        super.getItemView(restaurant, v, parent);


        // Set Title
        TextView titleTextView = (TextView) v
                .findViewById(R.id.restaurant_list_item_titleTextView);
        titleTextView.setText(restaurant.getTitle());

        // Set Address
        TextView addressTextView = (TextView) v
                .findViewById(R.id.restaurant_list_item_addressTextView);
        addressTextView.setText(restaurant.getAddress());

        // Set day abbreviations
        TextView sunday = (TextView) v
                .findViewById(R.id.restaurant_list_sunday);
        TextView monday = (TextView) v
                .findViewById(R.id.restaurant_list_monday);
        TextView tuesday = (TextView) v
                .findViewById(R.id.restaurant_list_tuesday);
        TextView wednesday = (TextView) v
                .findViewById(R.id.restaurant_list_wednesday);
        TextView thursday = (TextView) v
                .findViewById(R.id.restaurant_list_thursday);
        TextView friday = (TextView) v
                .findViewById(R.id.restaurant_list_friday);
        TextView saturday = (TextView) v
                .findViewById(R.id.restaurant_list_saturday);


        //Fix the damn favorites
        final ImageView favoriteImage = (ImageView) v.findViewById(R.id.favourite_image);
        favoriteImage.setBackgroundResource(R.drawable.ic_rating_not_important);

        ParseRelation<Restaurant> relation = ParseUser.getCurrentUser().getRelation(ParseConstants.RELATION_FAVORITE);
        ParseQuery<Restaurant> relationQuery = relation.getQuery();
        relationQuery.findInBackground(new FindCallback<Restaurant>() {
            @Override
            public void done(List<Restaurant> restaurants, ParseException e) {
                for (Restaurant r : restaurants) {
                    if (r.getObjectId().equals(restaurant.getObjectId())) {
                        favoriteImage.setBackgroundResource(R.drawable.ic_rating_important);
                    }
                }
            }
        });


        // Set text to red if not open
        if (restaurant.getSunday() == false) {
            sunday.setTextColor(0xffbbbbbb);
        } else {
            sunday.setTextColor(0xff000000);
        }
        if (restaurant.getMonday() == false) {
            monday.setTextColor(0xffbbbbbb);
        } else {
            monday.setTextColor(0xff000000);
        }
        if (restaurant.getTuesday() == false) {
            tuesday.setTextColor(0xffbbbbbb);
        } else {
            tuesday.setTextColor(0xff000000);
        }
        if (restaurant.getWednesday() == false) {
            wednesday.setTextColor(0xffbbbbbb);
        } else {
            wednesday.setTextColor(0xff000000);
        }
        if (restaurant.getThursday() == false) {
            thursday.setTextColor(0xffbbbbbb);
        } else {
            thursday.setTextColor(0xff000000);
        }
        if (restaurant.getFriday() == false) {
            friday.setTextColor(0xffbbbbbb);
        } else {
            friday.setTextColor(0xff000000);
        }
        if (restaurant.getSaturday() == false) {
            saturday.setTextColor(0xffbbbbbb);
        } else {
            saturday.setTextColor(0xff000000);
        }
        return v;
    }


}