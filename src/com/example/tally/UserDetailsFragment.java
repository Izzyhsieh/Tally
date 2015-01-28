package com.example.tally;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.FacebookRequestError;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.model.GraphUser;
import com.facebook.widget.ProfilePictureView;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;

public class UserDetailsFragment extends Fragment{
	
	private ProfilePictureView userProfilePictureView;
	private TextView userNameView;
	private TextView userGenderView;
	private TextView userEmailView;
	private TextView userLocaleView;
	private Button logoutButton;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.userdetails, null);
		userProfilePictureView = (ProfilePictureView) view.findViewById(R.id.userProfilePicture);
		userNameView = (TextView) view.findViewById(R.id.userName);
		userGenderView = (TextView) view.findViewById(R.id.userGender);
		userEmailView = (TextView) view.findViewById(R.id.userEmail);
		userLocaleView = (TextView) view.findViewById(R.id.userlocale);
		
		logoutButton = (Button) view.findViewById(R.id.logoutButton);
		logoutButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onLogoutButtonClicked();
			}
		});

		// Fetch Facebook user info if the session is active
		Session session = ParseFacebookUtils.getSession();
		if (session != null && session.isOpened()) {
			makeMeRequest();
		}
		return view;
	}

	private void makeMeRequest() {
		// TODO Auto-generated method stub
		Request request = Request.newMeRequest(ParseFacebookUtils.getSession(),
				new Request.GraphUserCallback() {
					@Override
					public void onCompleted(GraphUser user, Response response) {
						if (user != null) {
							// Create a JSON object to hold the profile info
							JSONObject userProfile = new JSONObject();
							try {
								// Populate the JSON object
								userProfile.put("facebookId", user.getId());
								userProfile.put("name", user.getName());
								if (user.getProperty("gender") != null) {
									userProfile.put("gender",
											(String) user.getProperty("gender"));
								}
								if (user.getProperty("locale") != null){
									userProfile.put("locale", (String) user.getProperty("locale"));
								}
								// if (user.getProperty("email") != null) {
								// userProfile.put("email", (String)
								// user.getProperty("email"));
								// }

								// Save the user profile info in a user property
								ParseUser currentUser = ParseUser
										.getCurrentUser();
								currentUser.put("profile", userProfile);
								currentUser.saveInBackground();

								// Show the user info
								updateViewsWithProfileInfo();
							} catch (JSONException e) {
								Log.d(IntegratingFacebookApplication.TAG,
										"Error parsing returned user data. "
												+ e);
							}

						} else if (response.getError() != null) {
							if ((response.getError().getCategory() == FacebookRequestError.Category.AUTHENTICATION_RETRY)
									|| (response.getError().getCategory() == FacebookRequestError.Category.AUTHENTICATION_REOPEN_SESSION)) {
								Log.d(IntegratingFacebookApplication.TAG,
										"The facebook session was invalidated."
												+ response.getError());
								onLogoutButtonClicked();
							} else {
								Log.d(IntegratingFacebookApplication.TAG,
										"Some other error: "
												+ response.getError());
							}
						}
					}
				});
		request.executeAsync();
		
	}

	protected void updateViewsWithProfileInfo() {
		// TODO Auto-generated method stub
		ParseUser currentUser = ParseUser.getCurrentUser();
		if (currentUser.has("profile")) {
			JSONObject userProfile = currentUser.getJSONObject("profile");
			try {

				if (userProfile.has("facebookId")) {
					userProfilePictureView.setProfileId(userProfile
							.getString("facebookId"));
				} else {
					// Show the default, blank user profile picture
					userProfilePictureView.setProfileId(null);
				}

				if (userProfile.has("name")) {
					userNameView.setText(userProfile.getString("name"));
				} else {
					userNameView.setText("");
				}

				if (userProfile.has("gender")) {
					userGenderView.setText(userProfile.getString("gender"));
				} else {
					userGenderView.setText("");
				}

				if (userProfile.has("email")) {
					userEmailView.setText(userProfile.getString("email"));
				} else {
					userEmailView.setText("");
				}
				
				if (userProfile.has("locale")){
					userLocaleView.setText(userProfile.getString("locale"));
				} else{
					userLocaleView.setText("");
				}

			} catch (JSONException e) {
				Log.d(IntegratingFacebookApplication.TAG,
						"Error parsing saved user data.");
			}
		}
	}

	protected void onLogoutButtonClicked() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		ParseUser currentUser = ParseUser.getCurrentUser();
		if (currentUser != null) {
			// Check if the user is currently logged
			// and show any cached content
			updateViewsWithProfileInfo();
		} else {
			// If the user is not logged in, go to the
			// activity showing the login view.
			startLoginActivity();
			//onBackPressed();
		}
	}

	private void startLoginActivity() {
		// TODO Auto-generated method stub
		Intent intent = new Intent(getActivity(), LoginActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
		//onBackPressed();
	}

	public static Fragment newInstance(Context _context) {
		// TODO Auto-generated method stub
		UserDetailsFragment f = new UserDetailsFragment();
		return f;
	}

}
