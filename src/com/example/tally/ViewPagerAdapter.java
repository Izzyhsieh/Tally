package com.example.tally;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class ViewPagerAdapter extends FragmentPagerAdapter {
	private Context _context;

	public ViewPagerAdapter(Context context, FragmentManager fm) {
		super(fm);
		_context = context;

	}

	@Override
	public Fragment getItem(int position) {
		Fragment f = new Fragment();
		switch (position) {
		case 0:
			f = UserDetailsFragment.newInstance(_context);
			break;
		case 1:
			f = new BottomFragment();
			break;
		case 2:
			f = new LayoutTwo();
			break;
		}
		return f;
	}

	@Override
	public int getCount() {
		return 3;
	}

}
