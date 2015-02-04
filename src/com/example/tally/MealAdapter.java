package com.example.tally;

import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.widget.ProfilePictureView;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;
import com.parse.SaveCallback;

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

	public MealAdapter(Context context, com.parse.ParseQueryAdapter.QueryFactory<ParseObject> queryFactory) {
		super(context, queryFactory);
	}

	public View getItemView(final ParseObject meal, View v, ViewGroup parent) {

		if (v == null) {
			v = View.inflate(getContext(), R.layout.list_item, null);
		}

		super.getItemView(meal, v, parent);
		TextView name = (TextView) v.findViewById(R.id.name);
		TextView timestamp = (TextView) v.findViewById(R.id.timestamp);
		ProfilePictureView profilePic = (ProfilePictureView) v.findViewById(R.id.profilePic);
		final FeedImageView imageView = (FeedImageView) v.findViewById(R.id.image_view);
		imageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(getContext(), EnlargeImageViewActivity.class);
				String imageURL = meal.getParseFile("image").getUrl();
				intent.putExtra("imageURL", imageURL);
				int[] location = new int[2];
				imageView.getLocationOnScreen(location);
				intent.putExtra("locationX", location[0]);
				intent.putExtra("locationY", location[1]);
				intent.putExtra("width", imageView.getWidth());
				intent.putExtra("height", imageView.getHeight());
				getContext().startActivity(intent);
			}

		});

		RatingBar rating = (RatingBar) v.findViewById(R.id.ratingBar);
		rating.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {

			@Override
			public void onRatingChanged(RatingBar ratingBar, final float rating, boolean fromUser) {
				// TODO Auto-generated method stub
				Log.d("onRatingChanged", String.valueOf(rating));
				ParseObject rate = new ParseObject("Rating");
				rate.put("r_user", ParseUser.getCurrentUser());
				rate.put("r_picture_id", meal.getObjectId());
				rate.put("r_value", String.valueOf(rating));
				rate.saveInBackground(new SaveCallback() {

					@Override
					public void done(ParseException arg0) {
						// TODO Auto-generated method stub
						if (arg0 != null) {
							Toast.makeText(getContext(), "Error Rating : " + arg0.getMessage(), Toast.LENGTH_SHORT).show();
						} else {
							Toast.makeText(getContext(), "Rated value = " + String.valueOf(rating), Toast.LENGTH_SHORT).show();
						}
					}

				});
			}
		});

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
		// rating.setRating(meal.getNumber("rate").floatValue());
		setRatingValue(meal.getObjectId());

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

		return v;
	}

	private void setRatingValue(String id) {
		// TODO Auto-generated method stub
		// query
		ParseQuery rQuery = new ParseQuery("Rating");
		rQuery.whereEqualTo("objectId", id);
		// calculate
		// todo......
	}
}
