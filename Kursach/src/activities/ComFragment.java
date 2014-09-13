package activities;

import getSMS.SmsSrevice;

import java.util.ArrayList;

import sms.Cordinat;
import sms.SmsStruct;
import android.annotation.SuppressLint;
import android.app.ListFragment;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.kursach.R;

@SuppressLint("ValidFragment")
public class ComFragment extends ListFragment {

	String table;
	AddFragment addfr;
	Adaptr adapter;
	Cursor c;
	SQLiteDatabase db;
	MainActivity activity;

	public ComFragment(SQLiteDatabase db, String table, MainActivity activity) {
		this.db = db;
		this.table = table;
		this.c = db.rawQuery("select rowid _id,* from " + table, null);
		adapter = new Adaptr(getActivity(), c, table, activity);
		addfr = new AddFragment(c);
		this.activity = activity;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		c.requery();
		setListAdapter(adapter);
		adapter.notifyDataSetChanged();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.comfragment, container, false);
		adapter.notifyDataSetChanged();
		((Button) v.findViewById(R.id.buttonAdd))
				.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						addfr.show(getFragmentManager(), table);
						c.requery();
					}
				});
	/*	((Button)v.findViewById(R.id.sysbtn)).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent smsIntnt = new Intent(activity.getApplicationContext(),SmsSrevice.class);
				smsIntnt.putExtra("from", "+375292592484");
				smsIntnt.putExtra("message", genCordinat());
				activity.getApplicationContext().startService(smsIntnt);
			}
		});*/

		return v;
	}
	

	private String genCordinat() {
		Double latitude, longitude;
		Double[] c = new Double[10];
		c[0] = 10.0;
		c[1] = 11.0;
		c[2] = 12.0;
		c[3] = 13.0;
		c[4] = 14.0;
		c[5] = 15.0;
		c[6] = 16.0;
		c[7] = 17.0;
		c[8] = 18.0;
		c[9] = 19.0;
		ArrayList<Cordinat> cc = new ArrayList<Cordinat>();
		for (int i = 0; i < 5; i++) {
			Cordinat s = new Cordinat(c[i], c[i + 5]);
			cc.add(s);
		}
		SmsStruct sms = new SmsStruct(cc);
		return sms.getSMS();

	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		Integer id = item.getItemId();
		Log.d("mes", "select:" + id);
		deleteFromTable(id + 1);
		switch (item.getGroupId()) {
		case 1:
			AddFragment addfr = new AddFragment(c);
			addfr.show(getFragmentManager(), table);
			c.requery();
			return true;
		case 2:
			c.requery();
			return true;
		}
		return super.onContextItemSelected(item);
	}

	public boolean deleteFromTable(Integer id) {

		Log.d("mes", "" + db.delete(table, "id = " + id, null));
		c.moveToPosition(id);
		if (c.isAfterLast())
			return true;
		do {
			ContentValues cv = new ContentValues();
			cv.put("id", id);
			cv.put("address", c.getString(c.getColumnIndex("address")));
			cv.put("name", c.getString(c.getColumnIndex("name")));
			cv.put("connect", c.getInt(c.getColumnIndex("connect")));
			Log.d("log",
					"update:" + db.update(table, cv, "id = " + (id + 1), null));
			id++;
		} while (c.moveToNext());
		return true;

	}

}
