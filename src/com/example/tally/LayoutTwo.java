package com.example.tally;

import android.widget.ListAdapter;

public class LayoutTwo extends BottomFragment {

	@Override
	public ListAdapter createAdapter() {
		return new SelfMealAdapter(getActivity());
	}
}
