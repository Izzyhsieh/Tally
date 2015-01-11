package com.example.tally;

import org.json.JSONObject;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.widget.ProfilePictureView;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;

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
		TextView name = (TextView) v.findViewById(R.id.name);
		TextView timestamp = (TextView) v.findViewById(R.id.timestamp);
		ProfilePictureView profilePic = (ProfilePictureView) v.findViewById(R.id.profilePic);
		FeedImageView imageView = (FeedImageView) v.findViewById(R.id.image_view);

		// user profile
		try {
			ParseUser user = meal.getParseUser("owner").fetchIfNeeded();
			if (user.has("profile")) {
				JSONObject userProfile = user.getJSONObject("profile");
				if (userProfile.has("facebookId")) {
					profilePic.setProfileId(userProfile.optString("facebookId"));
				} else {
					// Show the default, blank user profile picture
					profilePic.setProfileId(null);
				}
				if (userProfile.has("name")) {
					name.setText(userProfile.optString("name"));
				} else {
					name.setText("");
				}
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		timestamp.setText(meal.getCreatedAt().toLocaleString());

		// image view
		ParseFile photoFile = meal.getParseFile("image");
		if (photoFile != null) {
			// set image view resource

			// set imageview params
			/*
			 * LayoutParams params = imageView.getLayoutParams(); params.width =
			 * 500; params.height = 500; imageView.setLayoutParams(params);
			 */

			imageView.setImageUrl(photoFile.getUrl(), AppController.getInstance().getImageLoader());
			imageView.setVisibility(View.VISIBLE);
			imageView.setResponseObserver(new FeedImageView.ResponseObserver() {
				@Override
				public void onError() {
				}

				@Override
				public void onSuccess() {
				}
			});
		} else {
			imageView.setVisibility(View.GONE);
		}

		// ParseImageView mealImage = (ParseImageView) v
		// .findViewById(R.id.parse_view);
		// ParseFile photoFile = meal.getParseFile("image");
		// if (photoFile != null) {
		// mealImage.setParseFile(photoFile);
		// mealImage.loadInBackground();
		// }

		return v;
	}
}
