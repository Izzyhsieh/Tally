package com.example.tally;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseFacebookUtils;

public class IntegratingFacebookApplication extends Application {

	static final String TAG = "Weasley Clock";

	@Override
	public void onCreate() {
		super.onCreate();

		Parse.initialize(this, "5vjG9jI9RQYPzldbp60twfEFXBpVIhhAqnNCmhG0",
				"v9O9kmwjoe6qvMwKaMJEv9AhTDxLZwhusO2En0Qh");

		// Set your Facebook App Id in strings.xml
		ParseFacebookUtils.initialize(getString(R.string.app_id));
	}
}
