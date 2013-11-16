package com.hapr.activities;

import org.json.JSONObject;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ImageView.ScaleType;
import apt.tutorial.IPostListener;
import apt.tutorial.IPostMonitor;
import apt.tutorial.two.PostMonitor;

import com.hapr.coverflow.CoverAdapterView;
import com.hapr.coverflow.CoverFlow;
import com.hapr.fragments.ControlListFragment;
import com.technotalkative.viewstubdemo.R;

public class Activity_Eclair extends FragmentActivity {
	private IPostMonitor service = null;
	static final String KEY_TAG = "optiondata"; // parent node
	static final String KEY_ID = "id";
	static final String KEY_CONTROL = "control";
	static final String KEY_LOCATION = "location";
	static final String KEY_STATE = "state";
	static final String KEY_ICON = "icon";
	static final int ITEMS = 10;

	private MyAdapter mAdapter;
	private ViewPager mPager;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_eclair);
		final CoverFlow coverFlow = (CoverFlow) findViewById(R.id.coverflow);
		coverFlow.setAdapter(new ImageAdapter(this));
		ImageAdapter coverImageAdapter = new ImageAdapter(this);
		coverImageAdapter.createReflectedImages();
		coverFlow.setAdapter(coverImageAdapter);
		coverFlow.setSpacing(-20);
		coverFlow.setSelection(2, true);

		mAdapter = new MyAdapter(getSupportFragmentManager());
		mPager = (ViewPager) findViewById(R.id.pager);
		mPager.setAdapter(mAdapter);

		mPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				coverFlow.setSelection(position);
			}
		});
		coverFlow.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				mPager.setCurrentItem(arg2);
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});
		bindService(new Intent(this, PostMonitor.class), svcConn, BIND_AUTO_CREATE);
	}

	private ServiceConnection svcConn = new ServiceConnection() {
		public void onServiceConnected(ComponentName className, IBinder binder) {
			service = (IPostMonitor) binder;
			try {
				service.registerActivity(this.getClass().getName(), listener);
			} catch (Throwable t) {
				Log.e("Patchy", "Exception in call to registerAccount()", t);
			}
		}

		public void onServiceDisconnected(ComponentName className) {
			Toast.makeText(getApplicationContext(), "Ecl: Post service discontd!", Toast.LENGTH_SHORT).show();
			service = null;
		}
	};

	private IPostListener listener = new IPostListener() {
		@Override
		public void newStatus(String state) {
			runOnUiThread(new Runnable() {
				public void run() {
					//TODO: 
				}
			});
		}

		@Override
		public void newStatus(JSONObject jsonObject) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void controlXML(JSONObject controlXML) {
			// TODO Auto-generated method stub
			
		}
	};

	@Override
	public void onDestroy() {
		super.onDestroy();
		service.removeActivity(this.getClass().getName());
		unbindService(svcConn);
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
				// Bitmap originalImage =
				// BitmapFactory.decodeResource(getResources(), imageId);
				ImageView imageView = new ImageView(mContext);
				// imageView.setImageBitmap(originalImage);
				imageView.setImageResource(imageId);
				imageView.setLayoutParams(new CoverFlow.LayoutParams(130, 139));
				imageView.setScaleType(ScaleType.FIT_CENTER);
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

	public static class MyAdapter extends FragmentPagerAdapter {
		public MyAdapter(FragmentManager fragmentManager) {
			super(fragmentManager);
		}

		@Override
		public int getCount() {
			return ITEMS;
		}

		@Override
		public Fragment getItem(int position) {
			switch (position) {
			default:// Fragment # 2-9 - Will show list
				return ControlListFragment.init(position);
			}
		}
	}
}
