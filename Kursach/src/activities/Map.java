package activities;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.example.kursach.R;
import com.example.kursach.R.id;
import com.example.kursach.R.layout;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import android.support.v4.app.FragmentActivity;

import dbHelper.DBHelper;

public class Map extends FragmentActivity {

	SupportMapFragment mapFragment;
	GoogleMap map;
	DBHelper dbHelper;
	SQLiteDatabase db;
	Cursor cursor;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map);
		dbHelper = new DBHelper(getApplicationContext());
		db = dbHelper.getReadableDatabase();
		cursor = db.query("audience", null, null, null, null, null, null);
		Intent intent = getIntent();
		Integer id = intent.getIntExtra("num", 0);
		cursor.moveToPosition(id);
		String[] address = new String[]{cursor.getString(cursor.getColumnIndex("address"))};
		Log.d("log", "onCreate "+address);
		Cursor cursor = db.query("takeCordinats", null, "address = ?" ,address,
				null, null, null);
		mapFragment = (SupportMapFragment) getSupportFragmentManager()
				.findFragmentById(R.id.mapFragment);
		map = mapFragment.getMap();
		if (map == null) {
			finish();
			return;
		}
		setCordinate(cursor);
	}

	public void setCordinate(Cursor c) {
		Log.d("log", "setCordinate");
		PolylineOptions po = new PolylineOptions();
		if (c.moveToFirst()) {
			int i=1;
			Log.d("log", "position verification");
			int namelatitude = c.getColumnIndex("latitude");
			int namelongitude = c.getColumnIndex("longitude");
			do {
				Double latitude = c.getDouble(namelatitude);
				Double longitude = c.getDouble(namelongitude);
				Log.d("log", latitude+" "+longitude);
				po.add(new LatLng(latitude, longitude));
				map.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).title(String.valueOf(i)));
				i++;
			} while (c.moveToNext());
		} else
			Log.d("log", "0 rows");
		map.addPolyline(po);
		c.close();
		db.close();
	}
}
