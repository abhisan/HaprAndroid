package com.hapr.fragments;

import java.util.HashMap;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Application;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import apt.tutorial.IPostListener;
import apt.tutorial.IPostMonitor;
import apt.tutorial.two.PostMonitor;

import com.hapr.HaprApplication;
import com.hapr.customview.BinderData;
import com.hapr.entities.Control;
import com.hapr.entities.ControlState;
import com.technotalkative.viewstubdemo.R;

public class ControlListFragment extends ListFragment {
	private IPostMonitor service = null;
	static final String KEY_TAG = "optiondata"; // parent node
	static final String KEY_ID = "id";
	static final String KEY_CONTROL = "control";
	static final String KEY_LOCATION = "location";
	static final String KEY_STATE = "state";
	static final String KEY_ICON = "icon";

	private BinderData bindingData;
	//private List<HashMap<String, String>> optionDataCollection;
	private int fragNum;

	public static ControlListFragment init(int val) {
		ControlListFragment controlListFragment = new ControlListFragment();
		Bundle args = new Bundle();
		args.putInt("val", val);
		controlListFragment.setArguments(args);
		return controlListFragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		fragNum = getArguments() != null ? getArguments().getInt("val") : 1;
		getActivity().bindService(new Intent(getActivity(), PostMonitor.class), svcConn, getActivity().BIND_AUTO_CREATE);
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
			Toast.makeText(getActivity().getApplicationContext(), "Ecl: Post service discontd!", Toast.LENGTH_SHORT).show();
			service = null;
		}
	};

	private IPostListener listener = new IPostListener() {
		@Override
		public void newStatus(String state) {
			getActivity().runOnUiThread(new Runnable() {
				public void run() {
					// TODO:
					bindingData.notifyDataSetChanged();
				}
			});
		}

		@Override
		public void newStatus(JSONObject jsonObject) {
			Message msg = mHandler.obtainMessage(1, jsonObject);
			mHandler.sendMessage(msg);
			// getActivity().runOnUiThread(new Runnable() { public void run()
			// {}});
		}

		@Override
		public void controlXML(JSONObject controlXML) {

		}
	};

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				JSONObject obj = (JSONObject)msg.obj;
				try {
					Control temp = getApplication().getControlById(obj.get("id").toString());
					temp.setState(ControlState.get(obj.get("state").toString()));
					getApplication().updateControl(temp);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				bindingData.notifyDataSetChanged();
				break;
			}
		}
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View layoutView = inflater.inflate(R.layout.fragment_pager_list, container, false);
		View tv = layoutView.findViewById(R.id.text);
		((TextView) tv).setText("Menu #" + fragNum);
		return layoutView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		getListView().setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				ToggleButton toggleButton = (ToggleButton) view.findViewById(R.id.controlImage);
				if (toggleButton.isChecked()) {
					toggleButton.setChecked(false);
					//toggleButton.setButtonDrawable(R.drawable.device_ac_sleep_off);
					//getApplication().getControls().get(position).setState(ControlState.get("off"));
					service.toggleControlState(getApplication().getControls().get(position).getId(), "off");
					getApplication().getControls().get(position).setIcon("device_ac_sleep_off");
					//bindingData.notifyDataSetChanged();
					//optionDataCollection.get(position).put(KEY_STATE, "Off");
					//optionDataCollection.get(position).put(KEY_ICON, "device_ac_sleep_off");

				} else {
					toggleButton.setChecked(true);
					//toggleButton.setButtonDrawable(R.drawable.device_ac_sleep_on);
					//getApplication().getControls().get(position).setState(ControlState.get("on"));
					service.toggleControlState(getApplication().getControls().get(position).getId(), "on");
					getApplication().getControls().get(position).setIcon("device_ac_sleep_on");
					//bindingData.notifyDataSetChanged();
					//optionDataCollection.get(position).put(KEY_STATE, "On");
					//optionDataCollection.get(position).put(KEY_ICON, "device_ac_sleep_on");
				}
			}
		});
		bindingData = new BinderData(getActivity(), getApplication().getControls());
		setListAdapter(bindingData);
		bindingData.notifyDataSetChanged();
	}

	private HaprApplication getApplication(){
		Application a = getActivity().getApplication();
		HaprApplication ha = (HaprApplication) a;
		return ha;
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		service.removeActivity(this.getClass().getName());
		getActivity().unbindService(svcConn);
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		Log.i("Menu ", "Item clicked: " + id);
	}
}