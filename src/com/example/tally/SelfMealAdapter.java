package com.example.tally;

import android.content.Context;

import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;

public class SelfMealAdapter extends MealAdapter {

	public SelfMealAdapter(Context context) {
		super(context, new ParseQueryAdapter.QueryFactory<ParseObject>() {

			@Override
			public ParseQuery<ParseObject> create() {
				ParseUser user = ParseUser.getCurrentUser();
				ParseQuery query = new ParseQuery("Picture");
				query.whereEqualTo("owner", user);
				query.orderByDescending("createdAt");
				return query;
			}
		});
	}
}
