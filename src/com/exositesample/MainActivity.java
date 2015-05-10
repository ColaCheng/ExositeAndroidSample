package com.exositesample;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener, LocationListener{

	private Button postBtn, getBtn;
	private Context myContext;
	protected LocationManager locationManager;
	protected LocationListener locationListener;
	private TextView txtLat, readtxtLat;
	protected String latitude = "", longitude = "";
	Location location;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		myContext = this;
		findView();
		setListeners();
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
		location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		latitude = String.valueOf(location.getLatitude());
		longitude = String.valueOf(location.getLongitude());
		txtLat.setText("Latitude:" + latitude + ", Longitude:" + longitude);
	}

	private void findView() {
		postBtn = (Button)findViewById(R.id.post_btn);
		getBtn = (Button)findViewById(R.id.get_btn);
		txtLat = (TextView)findViewById(R.id.lat_txt);
		readtxtLat = (TextView)findViewById(R.id.read_lat_txt);
	}
	
	private void setListeners() {
		postBtn.setOnClickListener(this);
		getBtn.setOnClickListener(this);
	}
	
	private class GetData extends AsyncTask<String, Integer, String> {

		private ProgressDialog pDialog;

		protected void onPreExecute() {

			super.onPreExecute();
			pDialog = new ProgressDialog(myContext);
			pDialog.setMessage("query ...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}
		
		@Override
		protected String doInBackground(
				String... arg0) {
			String temp = "";
			// TODO Auto-generated method stub
			final String json = HttpMethod.getData("gps").replaceAll("%2c", ", ");
			if(json.equals(null)) { 
				temp = "";
			}else {
				MainActivity.this.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						try {
							readtxtLat.setText(json);
						} catch (NullPointerException e) {
							e.printStackTrace();
						}
					}
				});
				temp = "ok";
			}
			return temp;
		}
		
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			if(result.equals("ok")) {
				Toast.makeText(myContext, "查詢成功", Toast.LENGTH_SHORT).show();
			}else {
				Toast.makeText(myContext, "查詢失敗，請再試一次", Toast.LENGTH_SHORT).show();
			}
			pDialog.dismiss();
		}
		
	}
	
	private class PostData extends AsyncTask<String, Integer, String> {

		private ProgressDialog pDialog;

		protected void onPreExecute() {

			super.onPreExecute();
			pDialog = new ProgressDialog(myContext);
			pDialog.setMessage("uploading ...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}
		
		@Override
		protected String doInBackground(
				String... arg0) {
			String temp = "";
			// TODO Auto-generated method stub
			String postStatus = HttpMethod.postData(latitude + "," + longitude);
			if(postStatus.equals("ok"))
				return "ok";
			
			return temp;
		}
		
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			if(result.equals("ok"))
				Toast.makeText(myContext, "上傳成功", Toast.LENGTH_SHORT).show();
			else
				Toast.makeText(myContext, "上傳失敗，請再試一次", Toast.LENGTH_SHORT).show();
			pDialog.dismiss();
		}
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.post_btn:
			new PostData().execute();
			break;
		case R.id.get_btn:
			new GetData().execute();
			break;
		}
		
	}

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		latitude = String.valueOf(location.getLatitude());
		longitude = String.valueOf(location.getLongitude());
		txtLat.setText("Latitude:" + latitude + ", Longitude:" + longitude);
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}
}
