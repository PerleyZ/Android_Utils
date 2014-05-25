package com.example.android_test;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.ListView;

public class MainActivity extends Activity {

	public static final String TAG = "MainActivity";

	private MainActivity mThis = this;

	private ListView mListView;

	private View mTopBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mTopBar = findViewById(R.id.top_bar);

		final View clickView = findViewById(R.id.textView1);

		clickView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				new HideViewTask().execute(mTopBar);
				
				Bitmap icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
				
				ImageView imageView = (ImageView) findViewById(R.id.imageView);
				
				imageView.setImageBitmap(toGrayscale(icon));
				

			}
		});

	}

	public static Bitmap toGrayscale(Bitmap bmpOriginal) {
		int width, height;
		height = bmpOriginal.getHeight();
		width = bmpOriginal.getWidth();

		Bitmap bmpGrayscale = Bitmap.createBitmap(width, height,
				Bitmap.Config.ARGB_8888);
		Canvas c = new Canvas(bmpGrayscale);
		Paint paint = new Paint();
		ColorMatrix cm = new ColorMatrix();
		cm.setSaturation(0);
		ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
		paint.setColorFilter(f);
		c.drawBitmap(bmpOriginal, 0, 0, paint);
		return bmpGrayscale;
	}

	class HideViewTask extends AsyncTask<View, Integer, Boolean> {
		
		private static final int SPEED = 2;
		private static final int DURATION = 200;

		private View mTargetView;
		private LayoutParams mParams;
		private int mHeight;

		@Override
		protected Boolean doInBackground(View... params) {
			mTargetView = params[0];
			mHeight = mTargetView.getHeight();
			mParams = (LayoutParams) mTargetView.getLayoutParams();

			int currentMargin = mParams.topMargin;
			final boolean isExpand = (currentMargin != 0);

			while ((isExpand && currentMargin < 0)
					|| (!isExpand && currentMargin > -mHeight)) {

				try {
					Thread.sleep((DURATION * SPEED) / mHeight);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				if (isExpand) {
					currentMargin += SPEED;
				} else {
					currentMargin -= SPEED;
				}

				publishProgress(currentMargin);

			}

			return isExpand;
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			LLog.d(TAG, "onProgressUpdate: " + values[0]);
			super.onProgressUpdate(values);
			mParams.topMargin = values[0];
			mTargetView.setLayoutParams(mParams);
		}

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			// 修正误差值
			if (result) {
				mParams.topMargin = 0;
			} else {
				mParams.topMargin = -mHeight;
			}
			mTargetView.setLayoutParams(mParams);
		}

	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		LLog.d("Perley", "onBackPressed");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
