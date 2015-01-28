package com.example.tally;

import java.util.Arrays;
import java.util.List;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;

public class LoginActivity extends FragmentActivity {

	private Button loginButton;
	private Dialog progressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.login);

		loginButton = (Button) findViewById(R.id.loginButton);
		loginButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onLoginButtonClicked();
			}
		});

		// Check if there is a currently logged in user
		// and it's linked to a Facebook account.
		ParseUser currentUser = ParseUser.getCurrentUser();
		if ((currentUser != null) && ParseFacebookUtils.isLinked(currentUser)) {
			// Go to the user info activity
			//showUserDetailsActivity();
			
			// Go to main page (viewPager)
			showMainActivity();
			onBackPressed();
		}
	}

	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		ParseFacebookUtils.finishAuthentication(requestCode, resultCode, data);
	}

	private void onLoginButtonClicked() {
		LoginActivity.this.progressDialog = ProgressDialog.show(
				LoginActivity.this, "", "Logging in...", true);

		List<String> permissions = Arrays.asList("public_profile");
		// NOTE: for extended permissions, like "user_about_me", your app must
		// be reviewed by the Facebook team
		// (https://developers.facebook.com/docs/facebook-login/permissions/)

		ParseFacebookUtils.logIn(permissions, this, new LogInCallback() {
			@Override
			public void done(ParseUser user, ParseException err) {
				LoginActivity.this.progressDialog.dismiss();
				if (user == null) {
					Log.d(IntegratingFacebookApplication.TAG,
							"Uh oh. The user cancelled the Facebook login.");
				} else if (user.isNew()) {
					Log.d(IntegratingFacebookApplication.TAG,
							"User signed up and logged in through Facebook!");
					//showUserDetailsActivity();
					showMainActivity();
					onBackPressed();
				} else {
					Log.d(IntegratingFacebookApplication.TAG,
							"User logged in through Facebook!");
					//showUserDetailsActivity();
					showMainActivity();
					onBackPressed();
				}
			}
		});
	}
	
	private void showMainActivity() {
		// TODO Auto-generated method stub
		Intent intent = new Intent(this, ViewPagerActivity.class);
		startActivity(intent);
		onBackPressed();
		
	}
	
	/*private void showUserDetailsFragment() {
		UserDetailsFragment userDetailFragment = new UserDetailsFragment();
		userDetailFragment.setArguments(getIntent().getExtras());
		getSupportFragmentManager().beginTransaction().add(, userDetailFragment).commit();
		
		Intent intent = new Intent(this, UserDetailsActivity.class);
		startActivity(intent);
		onBackPressed();
	}*/
}
