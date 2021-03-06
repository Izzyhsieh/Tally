package com.example.tally;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Media;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class PostPictureActivity extends ActionBarActivity {

	private ImageView mPic;
	private DisplayMetrics mPhone;
	private final static int CAMERA = 66;
	private final static int PHOTO = 99;
	private Uri fileuri;
	private Bitmap bitmap;
	private ParseFile photoFile;
	public static final int MEDIA_TYPE_IMAGE = 1;
	private Context mContext;
	private MealAdapter mainAdapter;
	private Dialog progressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_pic_post);
		mContext = this;
		// get mobile resolution
		mPhone = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(mPhone);

		mPic = (ImageView) findViewById(R.id.post_pic);
		ImageView btn_camera = (ImageView) findViewById(R.id.post_camera);
		ImageView btn_gallery = (ImageView) findViewById(R.id.post_gallery);
		ImageView btn_rotate = (ImageView) findViewById(R.id.post_rotate);

		btn_camera.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// open camera then shoot picture and save it to gallery
				ContentValues value = new ContentValues();
				value.put(Media.MIME_TYPE, "image/jpeg");
				// Uri uri=
				// getContentResolver().insert(Media.EXTERNAL_CONTENT_URI,value);
				fileuri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
				Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
				// intent.putExtra(MediaStore.EXTRA_OUTPUT, uri.getPath());
				intent.putExtra(MediaStore.EXTRA_OUTPUT, fileuri);
				startActivityForResult(intent, CAMERA);
			}
		});

		btn_gallery.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// open gallery then choose pictore
				Intent intent = new Intent();
				intent.setType("image/*");
				intent.setAction(Intent.ACTION_GET_CONTENT);
				startActivityForResult(intent, PHOTO);
			}
		});

		btn_rotate.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// rotate image
				if (bitmap == null) {
					Toast.makeText(getApplicationContext(), R.string.msg_warning_rotate, Toast.LENGTH_SHORT).show();
				} else {
					Matrix matrix = new Matrix();
					matrix.setRotate(90);
					Bitmap rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
					bitmap = rotatedBitmap; // re-asign for user would
											// re-clicked rotate and functional
					mPic.setImageBitmap(rotatedBitmap);
				}
			}
		});

		ActionBar actionbar = getSupportActionBar();
		actionbar.setDisplayHomeAsUpEnabled(true);
		actionbar.setDisplayShowTitleEnabled(false);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_post_pic, menu);
		// return true;
		return super.onCreateOptionsMenu(menu);
	}

	protected Uri getOutputMediaFileUri(int type) {
		return Uri.fromFile(getOutputMediaFile(type));
	}

	private File getOutputMediaFile(int type) {
		// To be safe, you should check that the SDCard is mounted
		// using Environment.getExternalStorageState() before doing this.

		File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "MyCameraApp");
		// This location works best if you want the created images to be shared
		// between applications and persist after your app has been uninstalled.

		// Create the storage directory if it does not exist
		if (!mediaStorageDir.exists()) {
			if (!mediaStorageDir.mkdirs()) {
				Log.d("MyCameraApp", "failed to create directory");
				return null;
			}
		}

		// Create a media file name
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
		File mediaFile;
		if (type == MEDIA_TYPE_IMAGE) {
			mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_" + timeStamp + ".jpg");
		} else {
			return null;
		}

		return mediaFile;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		//
		if ((requestCode == CAMERA || requestCode == PHOTO)) {
			// get picture path
			Uri uri = null;
			if (data == null) {
				uri = fileuri;
			} else {
				uri = data.getData();
			}

			ContentResolver cr = this.getContentResolver();

			try {
				bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));
				if (bitmap.getWidth() > bitmap.getHeight())
					ScalePic(bitmap, mPhone.heightPixels);
				else
					ScalePic(bitmap, 250);

			} catch (Exception e) {
				Log.e("ERROR", e.toString());
				e.printStackTrace();
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	private void ScalePic(Bitmap bitmap, int phone) {
		float mScale = 1;
		if (bitmap.getWidth() > phone) {
			mScale = (float) phone / (float) bitmap.getWidth();
			Matrix mMat = new Matrix();
			mMat.setScale(mScale, mScale);
			Bitmap mScaleBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), mMat, false);
			mPic.setImageBitmap(mScaleBitmap);
			this.bitmap = mScaleBitmap;
		} else {
			mPic.setImageBitmap(bitmap);
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case R.id.action_post:
			postPicMeal();
			return true;
		case android.R.id.home:
			finish();
			return true;
		default:
			return super.onOptionsItemSelected(item);

		}
	}

	private void postPicMeal() {
		// save image to parse object
		PostPictureActivity.this.progressDialog = ProgressDialog.show(PostPictureActivity.this, "", "Uploading...", true);
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);

		byte[] scaledData = bos.toByteArray();

		// Save the scaled image to Parse

		photoFile = new ParseFile(new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".jpg", scaledData);
		photoFile.saveInBackground();

		// create new Meal
		ParseObject meal = new ParseObject("Picture");
		meal.put("owner", ParseUser.getCurrentUser());
		meal.put("image", photoFile);
		meal.put("sum", 0.0);
		meal.put("count", 0);
		meal.saveInBackground(new SaveCallback() {
			@Override
			public void done(ParseException e) {
				PostPictureActivity.this.progressDialog.dismiss();
				if (e != null) {
					Toast.makeText(mContext, "Error saving: " + e.getMessage(), Toast.LENGTH_LONG).show();
				} else {
					Toast.makeText(mContext, "Picture uploaded", Toast.LENGTH_LONG).show();
					finish();
				}
			}
		});
	}

}
