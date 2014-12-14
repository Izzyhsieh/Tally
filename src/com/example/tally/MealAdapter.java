package com.example.tally;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseImageView;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;

public class MealAdapter extends ParseQueryAdapter<ParseObject> {

	public MealAdapter(Context context) {
		super(context, new ParseQueryAdapter.QueryFactory<ParseObject>() {

			@Override
			public ParseQuery<ParseObject> create() {
				//
				ParseQuery query = new ParseQuery("Picture");
				query.orderByDescending("createdAt");
				return query;
			}
		});
	}
	
	
	public View getItemView(ParseObject meal, View v, ViewGroup parent) {

		if (v == null) {
			v = View.inflate(getContext(), R.layout.list_item, null);
		}

		super.getItemView(meal, v, parent);

		ParseImageView mealImage = (ParseImageView) v.findViewById(R.id.parse_view);
		ParseFile photoFile = meal.getParseFile("image");
		if (photoFile != null) {
			mealImage.setParseFile(photoFile);
			mealImage.loadInBackground();
		}

		return v;
	}
}
