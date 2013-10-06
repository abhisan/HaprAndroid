package com.hapr.activities;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.ListView;

import com.hapr.coverflow.CoverAdapterView;
import com.hapr.coverflow.CoverFlow;
import com.hapr.customview.BinderData;
import com.hapr.customview.SampleActivity;
import com.technotalkative.viewstubdemo.R;

public class Activity_Eclair extends FragmentActivity {
	AppSectionsPagerAdapter mAppSectionsPagerAdapter;
	ViewPager mViewPager;
	// XML node keys
	static final String KEY_TAG = "optiondata"; // parent node
	static final String KEY_ID = "id";
	static final String KEY_CONTROL = "control";
	static final String KEY_LOCATION = "location";
	static final String KEY_STATE = "state";
	static final String KEY_ICON = "icon";

	// List items
	ListView list;
	BinderData adapter = null;
	List<HashMap<String, String>> optionDataCollection;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_eclair);
		/*
		final CoverFlow coverFlow = (CoverFlow) findViewById(R.id.coverflow);
		coverFlow.setAdapter(new ImageAdapter(this));
		ImageAdapter coverImageAdapter = new ImageAdapter(this);
		coverImageAdapter.createReflectedImages();
		coverFlow.setAdapter(coverImageAdapter);
		coverFlow.setSpacing(-15);
		coverFlow.setSelection(2, true);
		OnItemClick oic = new OnItemClick();
		//coverFlow.setOnClickListener(oic);
		*/
		try {
			DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
			Document doc = docBuilder.parse(getAssets().open("controldata.xml"));
			optionDataCollection = new ArrayList<HashMap<String, String>>();
			// normalize text representation
			doc.getDocumentElement().normalize();
			NodeList optionList = doc.getElementsByTagName("optiondata");
			HashMap<String, String> map = null;
			for (int i = 0; i < optionList.getLength(); i++) {
				map = new HashMap<String, String>();
				Node firstOptionNode = optionList.item(i);
				if (firstOptionNode.getNodeType() == Node.ELEMENT_NODE) {
					Element firstWeatherElement = (Element) firstOptionNode;
					NodeList idList = firstWeatherElement.getElementsByTagName(KEY_ID);
					Element firstIdElement = (Element) idList.item(0);
					NodeList textIdList = firstIdElement.getChildNodes();
					map.put(KEY_ID, ((Node) textIdList.item(0)).getNodeValue().trim());
					NodeList cityList = firstWeatherElement.getElementsByTagName(KEY_CONTROL);
					Element firstCityElement = (Element) cityList.item(0);
					NodeList textCityList = firstCityElement.getChildNodes();
					map.put(KEY_CONTROL, ((Node) textCityList.item(0)).getNodeValue().trim());
					NodeList tempList = firstWeatherElement.getElementsByTagName(KEY_LOCATION);
					Element firstTempElement = (Element) tempList.item(0);
					NodeList textTempList = firstTempElement.getChildNodes();
					map.put(KEY_LOCATION, ((Node) textTempList.item(0)).getNodeValue().trim());
					NodeList iconList = firstWeatherElement.getElementsByTagName(KEY_ICON);
					Element firstIconElement = (Element) iconList.item(0);
					NodeList textIconList = firstIconElement.getChildNodes();
					map.put(KEY_ICON, ((Node) textIconList.item(0)).getNodeValue().trim());
					optionDataCollection.add(map);
				}
			}
			BinderData bindingData = new BinderData(this, optionDataCollection);
		
		} catch (IOException ex) {
			Log.e("Error", ex.getMessage());
		} catch (Exception ex) {
			Log.e("Error", "Loading exception");
		}
		mAppSectionsPagerAdapter = new AppSectionsPagerAdapter(getSupportFragmentManager());
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mAppSectionsPagerAdapter);
		mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				//coverFlow.setSelection(position);
			}
		});
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
				imageView.setLayoutParams(new CoverFlow.LayoutParams(300, 300));
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

	public static class AppSectionsPagerAdapter extends FragmentPagerAdapter {
		public AppSectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int i) {
			switch (i) {
				default:
					return new LaunchpadSectionFragment();
			}
		}

		@Override
		public int getCount() {
			return 3;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return "Section " + (position + 1);
		}
	}

	public static class LaunchpadSectionFragment extends Fragment {
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_section_launchpad, container, false);
			ListView lv = (ListView) rootView.findViewById(R.id.list);
			
			return rootView;
		}
	}
}
