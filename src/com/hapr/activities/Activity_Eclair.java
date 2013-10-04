package com.hapr.activities;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.hapr.coverflow.CoverAdapterView;
import com.hapr.coverflow.CoverFlow;
import com.technotalkative.viewstubdemo.R;

public class Activity_Eclair extends DashBoardActivity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_eclair);
		setHeader(getString(R.string.EclairActivityTitle), true, true);
		CoverFlow coverFlow = (CoverFlow) findViewById(R.id.coverflow);
		coverFlow.setAdapter(new ImageAdapter(this));
		ImageAdapter coverImageAdapter = new ImageAdapter(this);
		coverImageAdapter.createReflectedImages();
		coverFlow.setAdapter(coverImageAdapter);
		coverFlow.setSpacing(-15);
		coverFlow.setSelection(2, true);
		OnItemClick oic = new OnItemClick();
		coverFlow.setOnItemClickListener(oic);
	}

	public class OnItemClick implements com.hapr.coverflow.CoverAdapterView.OnItemClickListener {
		public void onItemClick(CoverAdapterView<?> parent, View view, int position, long id) {
			System.out.println("hi");
		}
	}

	public void OnItemClickListener() {

	}

	public class ImageAdapter extends BaseAdapter {
		int mGalleryItemBackground;
		private Context mContext;
		private Integer[] mImageIds = { R.drawable.devices_control, R.drawable.devices_environment, R.drawable.devices_health, R.drawable.devices_light_manage, R.drawable.devices_respondservice, R.drawable.devices_safeguards };
		private ImageView[] mImages;

		public ImageAdapter(Context c) {
			mContext = c;
			mImages = new ImageView[mImageIds.length];
		}

		public boolean createReflectedImages() {
			int index = 0;
			for (int imageId : mImageIds) {
				Bitmap originalImage = BitmapFactory.decodeResource(getResources(), imageId);
				ImageView imageView = new ImageView(mContext);
				imageView.setImageBitmap(originalImage);
				imageView.setLayoutParams(new CoverFlow.LayoutParams(100, 100));
				imageView.setScaleType(ScaleType.MATRIX);
				mImages[index++] = imageView;
			}
			return true;
		}

		public int getCount() {
			return mImageIds.length;
		}

		public Object getItem(int position) {
			return position;
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			return mImages[position];
		}

		public float getScale(boolean focused, int offset) {
			return Math.max(0, 1.0f / (float) Math.pow(2, Math.abs(offset)));
		}
	}
}
