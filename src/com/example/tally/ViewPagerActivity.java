package com.example.tally;

import android.graphics.ColorFilter;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffColorFilter;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;



public class ViewPagerActivity extends FragmentActivity {
	private ViewPager _mViewPager;
	private ViewPagerAdapter _adapter;
	private ImageView mainPage;
	private ImageView personalPage;
	private ImageView userPage;
	
	//color filter
	ColorFilter colorFilter = new PorterDuffColorFilter(R.color.l_blue, Mode.SRC_IN);
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        setUpView();
        setTab();
    }
    private void setUpView(){    	
   	 _mViewPager = (ViewPager) findViewById(R.id.viewPager);
     _adapter = new ViewPagerAdapter(getApplicationContext(),getSupportFragmentManager());
     _mViewPager.setAdapter(_adapter);
	 _mViewPager.setCurrentItem(1);
	 
	 LinearLayout _mFirstTab = (LinearLayout) findViewById(R.id.first_text);	 
	 _mFirstTab.setOnClickListener(new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			_mViewPager.setCurrentItem(0, true);
		}
	});
	 LinearLayout _mMidTab = (LinearLayout) findViewById(R.id.mid_text);	 
	 _mMidTab.setOnClickListener(new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			_mViewPager.setCurrentItem(1, true);
		}
	});
	 LinearLayout _mSecondTab = (LinearLayout) findViewById(R.id.second_text);
	 _mSecondTab.setOnClickListener(new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			_mViewPager.setCurrentItem(2, true);
		}
	});
	 
	 
    }
    private void setTab(){
			_mViewPager.setOnPageChangeListener(new OnPageChangeListener(){
						@Override
						public void onPageScrollStateChanged(int position) {}
						@Override
						public void onPageScrolled(int arg0, float arg1, int arg2) {}
						@Override
						public void onPageSelected(int position) {
							mainPage = (ImageView) findViewById(R.id.main_page);
					        personalPage = (ImageView) findViewById(R.id.personal_page);
					        userPage = (ImageView) findViewById(R.id.user_details);
							// TODO Auto-generated method stub
							switch(position){
							case 0:
								//findViewById(R.id.first_tab).setVisibility(View.VISIBLE);
								//findViewById(R.id.second_tab).setVisibility(View.INVISIBLE);
								userPage.setColorFilter(colorFilter);
								personalPage.setColorFilter(null);
								mainPage.setColorFilter(null);
								break;
							case 1:
								mainPage.setColorFilter(colorFilter);
								personalPage.setColorFilter(null);
								userPage.setColorFilter(null);
								break;
							case 2:
								//findViewById(R.id.first_tab).setVisibility(View.INVISIBLE);
								//findViewById(R.id.second_tab).setVisibility(View.VISIBLE);
								personalPage.setColorFilter(colorFilter);
								mainPage.setColorFilter(null);
								userPage.setColorFilter(null);
								break;
							}
						}
						
					});

    }
}