package activities;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnCreateContextMenuListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CursorAdapter;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.example.kursach.R;

import dbHelper.DBHelper;

public class Adaptr extends CursorAdapter implements
		OnCreateContextMenuListener {
	DBHelper dbHelper;
	String table;
	MainActivity activity;
	Context context1;

	public Adaptr(Context context, Cursor c, String s, MainActivity activity) {
		super(context, c);
		this.activity = activity;
		this.context1 = context;
		table = s;

	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		LayoutInflater inflater = LayoutInflater.from(parent.getContext());
		dbHelper = new DBHelper(parent.getContext());
		View retView = inflater.inflate(R.layout.item, parent, false);
		return retView;
	}

	@Override
	public void bindView(View view, Context context, final Cursor cursor) {
		TextView name = (TextView) view.findViewById(R.id.textViewItemName);
		name.setText(cursor.getString(cursor.getColumnIndex("name")));
		TextView address = (TextView) view
				.findViewById(R.id.textViewItemTelephone);
		address.setText(cursor.getString(cursor.getColumnIndex("address")));
		Boolean b = false;

		Integer connect = cursor.getInt(cursor.getColumnIndex("connect"));
		if (connect == 1)
			b = true;
		Switch sw = (Switch) view.findViewById(R.id.switchButton);
		sw.setTag(cursor.getPosition());
		view.setTag(cursor.getPosition());
		sw.setChecked(b);
		OnClickListener cl = new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (table == "audience") {
					Log.d("log", String.valueOf(v.getTag()));

					Intent intent = new Intent(
							activity.getApplicationContext(), Map.class);
					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					intent.putExtra("num", (Integer) v.getTag());
					activity.getApplicationContext().startActivity(intent);
				}

			}
		};
		OnCheckedChangeListener change = new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				SQLiteDatabase db = dbHelper.getWritableDatabase();
				cursor.moveToPosition((Integer) buttonView.getTag());
				ContentValues cv = new ContentValues();
				cv.put("name", cursor.getString(cursor.getColumnIndex("name")));
				cv.put("address",
						cursor.getString(cursor.getColumnIndex("address")));
				Integer connect = 0;
				if (isChecked)
					connect = 1;
				Log.d("log", cursor.getString(cursor.getColumnIndex("name"))
						+ ":");
				cv.put("connect", connect);
				Log.d("log",
						"update:"
								+ db.update(table, cv, "id = "
										+ ((Integer) buttonView.getTag() + 1),
										null));
			}
		};
		sw.setOnCheckedChangeListener(change);
		view.setOnClickListener(cl);
		view.setOnCreateContextMenuListener(this);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View view,
			ContextMenuInfo arg2) {
		menu.add(1, (Integer) view.getTag(), 0, "Edit");
		menu.add(2, (Integer) view.getTag(), 0, "Remove");

	}

	public boolean isGPRSConnectoins() {
		ConnectivityManager cm = (ConnectivityManager) activity
				.getSystemService(activity.CONNECTIVITY_SERVICE);
		NetworkInfo netInf = cm.getActiveNetworkInfo();
		if (netInf != null && netInf.isConnectedOrConnecting())
			return true;
		else
			return false;
	}
}