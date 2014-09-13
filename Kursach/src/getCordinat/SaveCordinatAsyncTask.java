package getCordinat;


import java.lang.ref.WeakReference;
import java.util.ArrayList;

import sms.Cordinat;
import sms.SmsStruct;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.gsm.SmsManager;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;

import dbHelper.DBHelper;

public class SaveCordinatAsyncTask extends AsyncTask<String, Integer, String> {
	Context context;
	WeakReference<UpdatesService> wr = null;
	DBHelper dBHelper;
	SQLiteDatabase db;
	Cursor c;
	LocationRequest mLocRequest;
	LocationClient locationClient;
	ArrayList<Cordinat> cordinats;
	Cordinat myCordinat;

	public SaveCordinatAsyncTask(Context context, UpdatesService service) {
		this.context = context;
		wr = new WeakReference<UpdatesService>(service);
		dBHelper = new DBHelper(context);
		db = dBHelper.getWritableDatabase();
		c = db.query("myCordinats", null, null, null, null, null, null);
		cordinats = new ArrayList<Cordinat>();

	}

	@Override
	protected String doInBackground(String... arg0) {
		getLocation();
		while(myCordinat==null)
			try {
				Thread.sleep(500);
				Log.d("log","wait");
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		Log.d("log", myCordinat.latitude + " " + myCordinat.longitude);
		setOnBase(myCordinat);
		db.close();
		return null;
	}

	public void setOnBase(Cordinat cordinat) {
		Log.d("mes", "setOnBase");
		ContentValues contentVal = new ContentValues();
		contentVal.put("latitude", cordinat.latitude);
		contentVal.put("longitude", cordinat.longitude);
		long rowId = db.insert("myCordinats", null, contentVal);
		c.requery();
		int leng = 0;
		if (c.moveToFirst()) {
			int latitudeIndex = c.getColumnIndex("latitude");
			int longitudeIndex = c.getColumnIndex("longitude");
			int i = 0;
			do {
				double latitude = c.getDouble(latitudeIndex);
				double longitude = c.getDouble(longitudeIndex);
				cordinats.add(new Cordinat(latitude, longitude));
				i++;
				leng = i;
			} while (c.moveToNext());
		} else {
		}
		Log.d("mes", leng + "полей в базе");
		if (leng >= 5)
			sendLocation();
	}

	public void sendLocation() {
		Log.d("mes", "sendLocation");
		Cursor c = db.query("audience", null, null, null, null, null, null);
		if (c.moveToFirst()) {
			int nameIndex = c.getColumnIndex("name");
			int addressIndex = c.getColumnIndex("address");
			int connectIndex = c.getColumnIndex("connect");
			do {
				String name = c.getString(nameIndex);
				String address = c.getString(addressIndex);
				int connect = c.getInt(connectIndex);
				// получаем значения по номерам столбцов и пишем все в лог
				if (connect >= 1) {
					sendSMS(address);
				}
			} while (c.moveToNext());
		} else
			Log.d("mes", "0 rows");
		int clearCount = db.delete("myCordinats", null, null);
	}

	public void sendSMS(String address) {
		
		SmsManager sms = SmsManager.getDefault();
		SmsStruct message = new SmsStruct(cordinats);
		Log.d("mes", "sendSMS " + address+ message.getSMS());
		// sms.sendTextMessage(address, null, message.getSMS(), null, null);
	}

	public void getLocation() {


		locationClient = new LocationClient(context, new ConnectionCallbacks() {

			@Override
			public void onDisconnected() {
			}

			@Override
			public void onConnected(Bundle arg0) {
				LocationRequest request = new LocationRequest();
				locationClient.requestLocationUpdates(request,
						new LocationListener() {

							@Override
							public void onLocationChanged(Location arg0) {
								myCordinat = new Cordinat(arg0.getLatitude(),
										arg0.getLongitude());
								locationClient.disconnect();
							}
						});
			}
		}, new OnConnectionFailedListener() {

			@Override
			public void onConnectionFailed(ConnectionResult arg0) {
			}
		});
		locationClient.connect();

	}

	@Override
	protected void onPostExecute(String result) {
		if (wr != null && (wr.get() != null))
			wr.get().callBack();
		super.onPostExecute(result);
	}

}
