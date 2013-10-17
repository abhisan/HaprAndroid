package com.hapr.activities;

import com.technotalkative.viewstubdemo.R;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import apt.tutorial.IPostListener;
import apt.tutorial.IPostMonitor;
import apt.tutorial.two.PostMonitor;

public class HomeActivity extends DashBoardActivity {

	public static String ACTIVITY_NAME = "main_activity";
	private IPostMonitor service = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		setHeader(getString(R.string.HomeActivityTitle), false, true);
		bindService(new Intent(this, PostMonitor.class), svcConn, BIND_AUTO_CREATE);
	}

	private ServiceConnection svcConn = new ServiceConnection() {
		public void onServiceConnected(ComponentName className, IBinder binder) {
			service = (IPostMonitor) binder;
			try {
				service.registerActivity(ACTIVITY_NAME, listener);
			} catch (Throwable t) {
				Log.e("Patchy", "Exception in call to registerAccount()", t);
			}
		}

		public void onServiceDisconnected(ComponentName className) {
			service = null;
		}
	};


	private IPostListener listener = new IPostListener() {
		@Override
		public void newStatus(String state) {
			runOnUiThread(new Runnable() {
				public void run() {
					//TextView tv = (TextView) findViewById(R.id.textView1);
					//DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
					//Date date = new Date();
					//tv.setText(dateFormat.format(date).toString());
				}
			});
		}
	};
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		service.removeActivity(ACTIVITY_NAME);
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