/*
 * Copyright 2012 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.tally;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.TranslateAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;

import com.parse.ParseObject;

public class BottomFragment extends ListFragment {

	private QuickReturnListView mListView;
	// private TextView mQuickReturnView;
	private RelativeLayout mQuickReturnView;
	private int mQuickReturnHeight;
	private ImageView postBtn;

	private static final int STATE_ONSCREEN = 0;
	private static final int STATE_OFFSCREEN = 1;
	private static final int STATE_RETURNING = 2;
	private int mState = STATE_ONSCREEN;
	private int mScrollY;
	private int mMinRawY = 0;

	private TranslateAnimation anim;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.footer_fragment, null);
		// mQuickReturnView = (TextView) view.findViewById(R.id.footer);
		mQuickReturnView = (RelativeLayout) view.findViewById(R.id.footer);
		postBtn = (ImageView) mQuickReturnView.findViewById(R.id.btn_post);
		postBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onPostButtonClicked();
			}
		});
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		mListView = (QuickReturnListView) getListView();
		setListAdapter(createAdapter());

		mListView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
			@Override
			public void onGlobalLayout() {
				mQuickReturnHeight = mQuickReturnView.getHeight();
				mListView.computeScrollY();
			}
		});
		
		mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				FeedImageView imageView = (FeedImageView) view.findViewById(R.id.image_view); 
				Intent intent = new Intent();
				intent.setClass(getActivity(), EnlargeImageViewActivity.class);
				//get ParseObject the image's url via adapter
				ParseObject meal = (ParseObject) parent.getAdapter().getItem(position);
				String imageURL = meal.getParseFile("image").getUrl();
				intent.putExtra("imageURL", imageURL);
				intent.putExtra("position", position);
				int[] location = new int[2];
				imageView.getLocationOnScreen(location);
				intent.putExtra("locationX", location[0]);
				intent.putExtra("locationY", location[1]);
				intent.putExtra("width", imageView.getWidth());
				intent.putExtra("height", imageView.getHeight());
				startActivity(intent); 
			}
		});

		mListView.setOnScrollListener(new OnScrollListener() {
			@SuppressLint("NewApi")
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

				mScrollY = 0;
				int translationY = 0;

				if (mListView.scrollYIsComputed()) {
					mListView.computeScrollY();
					mScrollY = mListView.getComputedScrollY();
				}

				int rawY = mScrollY;

				switch (mState) {
				case STATE_OFFSCREEN:
					if (rawY >= mMinRawY) {
						mMinRawY = rawY;
					} else {
						mState = STATE_RETURNING;
					}
					translationY = rawY;
					break;

				case STATE_ONSCREEN:
					if (rawY > mQuickReturnHeight) {
						mState = STATE_OFFSCREEN;
						mMinRawY = rawY;
					}
					translationY = rawY;
					break;

				case STATE_RETURNING:

					translationY = (rawY - mMinRawY) + mQuickReturnHeight;

					System.out.println(translationY);
					if (translationY < 0) {
						translationY = 0;
						mMinRawY = rawY + mQuickReturnHeight;
					}

					if (rawY == 0) {
						mState = STATE_ONSCREEN;
						translationY = 0;
					}

					if (translationY > mQuickReturnHeight) {
						mState = STATE_OFFSCREEN;
						mMinRawY = rawY;
					}
					break;
				}

				/** this can be used if the build is below honeycomb **/
				if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.HONEYCOMB) {
					anim = new TranslateAnimation(0, 0, translationY, translationY);
					anim.setFillAfter(true);
					anim.setDuration(0);
					mQuickReturnView.startAnimation(anim);
				} else {
					mQuickReturnView.setTranslationY(translationY);
				}

			}

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
			}
		});
	}

	public ListAdapter createAdapter() {
		return new MealAdapter(getActivity());
	}

	private void onPostButtonClicked() {
		// TODO Auto-generated method stub
		Intent i = new Intent(getActivity(), PostPictureActivity.class);
		startActivity(i);
	}
}