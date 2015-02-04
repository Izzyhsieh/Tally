package com.example.tally;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.ImageView.ScaleType;


public class EnlargeImageViewActivity extends Activity{
	
	private ArrayList<String> mDatas;
	private int mPosition;
	private int mLocationX;
	private int mLocationY;
	private int mWidth;
	private int mHeight;
	SmoothImageView imageView = null;
	private String imageURL = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		mPosition = getIntent().getIntExtra("position", 0);
		mLocationX = getIntent().getIntExtra("locationX", 0);
		mLocationY = getIntent().getIntExtra("locationY", 0);
		mWidth = getIntent().getIntExtra("width", 0);
		mHeight = getIntent().getIntExtra("height", 0);
		imageURL = getIntent().getStringExtra("imageURL");
		
		imageView = new SmoothImageView(this);
		imageView.setOriginalInfo(mWidth, mHeight, mLocationX, mLocationY);
		imageView.setLayoutParams(new ViewGroup.LayoutParams(-1, -1));
		imageView.transformIn();
		imageView.setScaleType(ScaleType.FIT_CENTER);
		setContentView(imageView);
		imageView.setImageUrl(imageURL, AppController.getInstance().getImageLoader());
	}
}
