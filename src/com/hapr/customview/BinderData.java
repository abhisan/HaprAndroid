package com.hapr.customview;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.hapr.entities.Control;
import com.technotalkative.viewstubdemo.R;

public class BinderData extends BaseAdapter {

	LayoutInflater inflater;
	ImageView thumb_image;
	List<Control> controlDataCollection;
	ViewHolder holder;

	public BinderData() {
		// TODO Auto-generated constructor stub
	}

	public BinderData(Activity act, List<Control> map) {
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
			holder.controlImage = (ToggleButton) vi.findViewById(R.id.controlImage);
			vi.setTag(holder);
		} else {
			holder = (ViewHolder) vi.getTag();
		}
		holder.control.setText(controlDataCollection.get(position).getControlName());
		holder.location.setText(controlDataCollection.get(position).getLocation().getControlLocation());
		holder.state.setText(controlDataCollection.get(position).getState().getControlState());
		String uri = "drawable/" + controlDataCollection.get(position).getIcon();
		int imageResource = vi.getContext().getApplicationContext().getResources().getIdentifier(uri, null, vi.getContext().getApplicationContext().getPackageName());
		Drawable image = vi.getContext().getResources().getDrawable(imageResource);
		holder.controlImage.setButtonDrawable(image);
		holder.controlImage.setChecked(controlDataCollection.get(position).getState().getControlState().equals("on")?true:false);
		return vi;
	}

	static class ViewHolder {
		TextView control;
		TextView location;
		TextView state;
		ToggleButton controlImage;
	}
}
