package activities;

import android.annotation.SuppressLint;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.Button;
import android.widget.EditText;

import com.example.kursach.R;

import dbHelper.DBHelper;

@SuppressLint("ValidFragment")
public class AddFragment extends DialogFragment {

	DBHelper dBHelper;
	EditText name, address;
	Cursor c;

	public AddFragment(Cursor c) {
		this.c = c;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.d("mes", "onCreateView_add");
		dBHelper = new DBHelper(getActivity());
		View v = inflater.inflate(R.layout.add, null);
		name = (EditText) v.findViewById(R.id.editTextNameAdd);
		address = (EditText) v.findViewById(R.id.editTextAddresAdd);
		((Button) v.findViewById(R.id.buttonOkAdd))
				.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						SQLiteDatabase db = dBHelper.getWritableDatabase();
						Cursor cursor = db.query(getTag(), null, null, null,
								null, null, null);
						ContentValues cv = new ContentValues();
						cv.put("name", name.getText().toString());
						cv.put("address", address.getText().toString());
						cv.put("connect", 1);
						int id = 0;
						cursor.moveToLast();
						if (!cursor.isBeforeFirst())
							id = cursor.getInt(cursor.getColumnIndex("id"));
						cv.put("id", id + 1);
						long rowId = db.insert(getTag(), null, cv);
						Log.d("mes", "Add rowId = " + rowId);
						c.requery();
						db.close();
						dismiss();
					}
				});
		((Button) v.findViewById(R.id.buttonCanselAdd))
				.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {

						Log.d("mes", " onClick(View v)");

						dismiss();
					}
				});
		Log.d("mes", "onCreateView_add_complit");
		return v;
	}

}
