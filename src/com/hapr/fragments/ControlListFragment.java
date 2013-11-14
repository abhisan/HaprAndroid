package com.hapr.fragments;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.AsyncTask;
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

import com.hapr.customview.BinderData;
import com.technotalkative.viewstubdemo.R;

public class ControlListFragment extends ListFragment {
	private IPostMonitor service = null;
	static final String KEY_TAG = "optiondata"; // parent node
	static final String KEY_ID = "id";
	static final String KEY_CONTROL = "control";
	static final String KEY_LOCATION = "location";
	static final String KEY_STATE = "state";
	static final String KEY_ICON = "icon";

	private List<HashMap<String, String>> optionDataCollection;
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
				}
			});
		}

		@Override
		public void newStatus(JSONObject jsonObject) {
			Message msg = mHandler.obtainMessage(1, jsonObject);
			mHandler.sendMessage(msg);

			/*
			 * getActivity().runOnUiThread(new Runnable() { public void run() {
			 * // TODO: } });
			 */
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
				Object obj = msg.obj;
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
					toggleButton.setButtonDrawable(R.drawable.device_ac_sleep_off);
					service.toggleLight("off");
					optionDataCollection.get(position).put(KEY_STATE, "Off");
					optionDataCollection.get(position).put(KEY_ICON, "device_ac_sleep_off");

				} else {
					toggleButton.setChecked(true);
					toggleButton.setButtonDrawable(R.drawable.device_ac_sleep_on);
					optionDataCollection.get(position).put(KEY_STATE, "On");
					service.toggleLight("on");
					optionDataCollection.get(position).put(KEY_ICON, "device_ac_sleep_on");
				}
				// imageView.setButtonDrawable(R.drawable.device_ac_sleep_off);
				// Intent i = new Intent(); i.setClass(getActivity(),
				// SampleActivity.class); i.putExtra("position",
				// String.valueOf(position + 1)); i.putExtra("control",
				// optionDataCollection.get(position).get(KEY_CONTROL));
				// i.putExtra("location",
				// optionDataCollection.get(position).get(KEY_LOCATION));
				// i.putExtra("state",
				// optionDataCollection.get(position).get(KEY_STATE));
				// i.putExtra("icon",
				// optionDataCollection.get(position).get(KEY_ICON));
				// startActivity(i);
			}
		});

		AsyncTask<Void, Void, List<HashMap<String, String>>> task = new AsyncTask<Void, Void, List<HashMap<String, String>>>() {// List<HashMap<String,
			@Override
			protected List<HashMap<String, String>> doInBackground(Void... params) {
				try {
					Thread.sleep(10);
					loadControls();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				return optionDataCollection;
			}

			@Override
			protected void onPostExecute(List<HashMap<String, String>> result) {
				BinderData bindingData = new BinderData(getActivity(), result);
				setListAdapter(bindingData);
			}
		};
		task.execute();
		// loadControls();
		// service.requestStatus();
	}

	private void loadControls() {
		try {
			DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
			Document doc = docBuilder.parse(getActivity().getAssets().open("controldata.xml"));
			optionDataCollection = new ArrayList<HashMap<String, String>>();
			// normalize text representation
			doc.getDocumentElement().normalize();
			NodeList optionList = doc.getElementsByTagName(KEY_TAG);
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

					NodeList stateList = firstWeatherElement.getElementsByTagName(KEY_STATE);
					Element firstStateElement = (Element) stateList.item(0);
					NodeList textStateList = firstStateElement.getChildNodes();
					map.put(KEY_STATE, ((Node) textStateList.item(0)).getNodeValue().trim());
					
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

		} catch (IOException ex) {
			Log.e("Error", ex.getMessage());
		} catch (Exception ex) {
			Log.e("Error", "Loading exception");
		}
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