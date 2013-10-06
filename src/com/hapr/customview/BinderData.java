package com.hapr.customview;

import java.util.HashMap;
import java.util.List;

import com.technotalkative.viewstubdemo.R;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class BinderData extends BaseAdapter {
	static final String KEY_TAG = "option"; // parent node
	static final String KEY_ID = "id";
	static final String KEY_CONTROL = "control";
	static final String KEY_LOCATION = "location";
	static final String KEY_STATE = "state";
	static final String KEY_ICON = "icon";

	LayoutInflater inflater;
	ImageView thumb_image;
	List<HashMap<String, String>> controlDataCollection;
	ViewHolder holder;

	public BinderData() {
		// TODO Auto-generated constructor stub
	}

	public BinderData(Activity act, List<HashMap<String, String>> map) {
		this.controlDataCollection = map;
		inflater = (LayoutInflater) act.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public int getCount() {
		return controlDataCollection.size();
	}

	public Object getItem(int arg0) {
		return null;
	}

	public long getItemId(int position) {
		return 0;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		View vi = convertView;
		if (convertView == null) {
			vi = inflater.inflate(R.layout.list_row, null);
			holder = new ViewHolder();
			holder.control = (TextView) vi.findViewById(R.id.controlName); 
			holder.state = (TextView) vi.findViewById(R.id.controlState); 
			holder.location = (TextView) vi.findViewById(R.id.location); 
			holder.controlImage = (ImageView) vi.findViewById(R.id.controlImage); 
			vi.setTag(holder);
		} else {
			holder = (ViewHolder) vi.getTag();
		}
		holder.control.setText(controlDataCollection.get(position).get(KEY_CONTROL));
		holder.location.setText(controlDataCollection.get(position).get(KEY_LOCATION));
		String uri = "drawable/" + controlDataCollection.get(position).get(KEY_ICON);
		int imageResource = vi.getContext().getApplicationContext().getResources().getIdentifier(uri, null, vi.getContext().getApplicationContext().getPackageName());
		Drawable image = vi.getContext().getResources().getDrawable(imageResource);
		holder.controlImage.setImageDrawable(image);
		return vi;
	}

	static class ViewHolder {
		TextView control;
		TextView location;
		TextView state;
		ImageView controlImage;
	}
}
