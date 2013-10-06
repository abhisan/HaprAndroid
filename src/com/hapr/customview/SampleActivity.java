package com.hapr.customview;

import com.technotalkative.viewstubdemo.R;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.TextView;

public class SampleActivity extends Activity {

	String position = "1";
	String city = "";
	String weather = "";
	String temperature = "";
	String windSpeed = "";
	String iconfile = "";
	ImageButton controlIcon;
	TextView tvcity;
	TextView tvtemp;
	TextView tvwindspeed;
	TextView tvCondition;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detailpage);
		try {
			// handle for the UI elements
			controlIcon = (ImageButton) findViewById(R.id.imageButtonAlpha);
			// Text fields
			tvcity = (TextView) findViewById(R.id.textViewCity);
			tvtemp = (TextView) findViewById(R.id.textViewTemperature);
			tvwindspeed = (TextView) findViewById(R.id.textViewWindSpeed);
			tvCondition = (TextView) findViewById(R.id.textViewCondition);
			// Get position to display
			Intent i = getIntent();
			this.position = i.getStringExtra("position");
			this.city = i.getStringExtra("city");
			this.weather = i.getStringExtra("weather");
			this.temperature = i.getStringExtra("temperature");
			this.windSpeed = i.getStringExtra("windspeed");
			this.iconfile = i.getStringExtra("icon");
			String uri = "drawable/" + "d" + iconfile;
			int imageBtnResource = getResources().getIdentifier(uri, null, getPackageName());
			Drawable dimgbutton = getResources().getDrawable(imageBtnResource);
			// text elements
			tvcity.setText(city);
			tvtemp.setText(temperature);
			tvwindspeed.setText(windSpeed);
			tvCondition.setText(weather);
			// thumb_image.setImageDrawable(image);
			controlIcon.setImageDrawable(dimgbutton);
		} catch (Exception ex) {
			Log.e("Error", "Loading exception");
		}
	}
}
