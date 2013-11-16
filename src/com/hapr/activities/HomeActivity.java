package com.hapr.activities;

import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.json.JSONObject;
import org.w3c.dom.Document;

import android.app.AlertDialog;
import android.app.Application;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import apt.tutorial.IPostListener;
import apt.tutorial.IPostMonitor;
import apt.tutorial.two.PostMonitor;

import com.hapr.HaprApplication;
import com.hapr.entities.Control;
import com.hapr.utils.ControlXmlUtil;
import com.hapr.utils.net.NetworkUtil;
import com.technotalkative.viewstubdemo.R;

public class HomeActivity extends DashBoardActivity {
	private IPostMonitor service = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		setHeader(getString(R.string.HomeActivityTitle), false, true);
		String status = NetworkUtil.getConnectivityStatusString(getApplicationContext());
		boolean flag = NetworkUtil.isConnectingToInternet(getApplicationContext());
		Toast.makeText(getApplicationContext(), status, Toast.LENGTH_LONG).show();
		initializeHapr();
		bindService(new Intent(this, PostMonitor.class), svcConn, BIND_AUTO_CREATE);
	}

	private ServiceConnection svcConn = new ServiceConnection() {
		public void onServiceConnected(ComponentName className, IBinder binder) {
			service = (IPostMonitor) binder;
			//initializeHapr();
			try {
				service.registerActivity(this.getClass().getName(), listener);
			} catch (Throwable t) {
				Log.e("Patchy", "Exception in call to registerAccount()", t);
			}
		}

		public void onServiceDisconnected(ComponentName className) {
			Toast.makeText(getApplicationContext(), "Home: Post service discontd!", Toast.LENGTH_LONG).show();
			service = null;
		}
	};

	public void initializeHapr() {
		AsyncTask<Void, Void, Map<String, Control>> task = new AsyncTask<Void, Void, Map<String, Control>>() {
			@Override
			protected Map<String, Control> doInBackground(Void... params) {
				Document doc = null;
				DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
				try {
					DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
					doc = docBuilder.parse(getAssets().open("controldata.xml"));

				} catch (Exception e) {
					e.printStackTrace();
				}
				return ControlXmlUtil.getControls(doc);
			}

			@Override
			protected void onPostExecute(Map<String, Control> result) {
				// BinderData bindingData = new BinderData(this, result);
				// setListAdapter(bindingData);
				Application a = getApplication();
				HaprApplication ha = (HaprApplication) a;
				ha.loadControls(result);
			}
		};
		task.execute();
	}

	private IPostListener listener = new IPostListener() {
		@Override
		public void newStatus(String state) {
			runOnUiThread(new Runnable() {
				public void run() {
					// Do nothing.
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

	public void onButtonClicker(View v) {
		Intent intent;
		switch (v.getId()) {
		case R.id.main_btn_eclair:
			intent = new Intent(this, Activity_Eclair.class);
			startActivity(intent);
			break;

		case R.id.main_btn_froyo:
			intent = new Intent(this, Activity_Froyo.class);
			startActivity(intent);
			break;

		case R.id.main_btn_gingerbread:
			intent = new Intent(this, Activity_Gingerbread.class);
			startActivity(intent);
			break;

		case R.id.main_btn_honeycomb:
			intent = new Intent(this, Activity_Honeycomb.class);
			startActivity(intent);
			break;

		case R.id.main_btn_ics:
			intent = new Intent(this, Activity_ICS.class);
			startActivity(intent);
			break;

		case R.id.main_btn_jellybean:
			intent = new Intent(this, Activity_JellyBean.class);
			startActivity(intent);
			break;
		default:
			break;
		}
	}

	@Override
	protected void onStop() {
		super.onStop(); // Always call the superclass method first
	}

	public void onBackPressed() {
		confirmAlert();
	}

	private void confirmAlert() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("CLOSE");
		builder.setMessage("Do You Want to Close the Application").setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dialog.dismiss();
				onYesClick();
			}

		}).setNegativeButton("No", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dialog.cancel();
				onNoClick();
			}
		});
		AlertDialog alert = builder.create();
		alert.show();
	}

	private void onYesClick() {
		Intent setIntent = new Intent(Intent.ACTION_MAIN);
		setIntent.addCategory(Intent.CATEGORY_HOME);
		setIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(setIntent);
		this.finish();
	}

	private void onNoClick() {

	}
}