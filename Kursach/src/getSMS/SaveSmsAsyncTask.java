package getSMS;


import getCordinat.UpdatesService;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import sms.Cordinat;
import sms.SmsStruct;


import dbHelper.DBHelper;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;

public class SaveSmsAsyncTask extends AsyncTask<String, Integer, String> {
	Context context;
	SmsSrevice service;
	String from, message;
	WeakReference<SmsSrevice> wr;
	DBHelper dBHelper;

	SaveSmsAsyncTask(Context context, SmsSrevice service) {
		this.context = context;
		this.service = service;
		wr = new WeakReference<SmsSrevice>(service);
		dBHelper = new DBHelper(context);
	}

	// доделать добавление нового человека в базу если нет такого!"!!!!!

	@Override
	protected String doInBackground(String... params) {
		from = params[0];
		message = params[1];
		// проверить естьли запись с пришедшим адресом
		SQLiteDatabase db = dBHelper.getWritableDatabase();
		ContentValues contentVal = new ContentValues();
		SmsStruct sms = new SmsStruct(message);
		ArrayList<Cordinat> cordinats = sms.getCordinats();
		for (int i = 0; i < 5; i++) {
			contentVal.put("address", from);
			contentVal.put("latitude", cordinats.get(i).latitude);
			contentVal.put("longitude", cordinats.get(i).longitude);
			long rowId = db.insert("takeCordinats", null, contentVal);
			Log.d("mes", "rowId = " + rowId);
		}
		db.close();
		return null;
	}

	@Override
	protected void onPostExecute(String result) {
		if (wr != null && (wr.get() != null))
			wr.get().showNotification();
		super.onPostExecute(result);
	}

}
