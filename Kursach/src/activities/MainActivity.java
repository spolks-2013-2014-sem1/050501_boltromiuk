package activities;

import getSMS.SMSMonitor;
import getSMS.SmsSrevice;

import java.util.ArrayList;

import sms.Cordinat;
import sms.SmsStruct;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;

import com.example.kursach.R;

import dbHelper.DBHelper;

public class MainActivity extends Activity implements TabListener {

	ComFragment lfr, afr;
	DBHelper dbHelper;
	SQLiteDatabase db;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);	

		dbHelper=new DBHelper(getApplicationContext());
		db = dbHelper.getReadableDatabase();
		lfr = new ComFragment(db,"listens",this);
		afr =  new ComFragment(db,"audience",this);
		ActionBar bar = getActionBar();
		bar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
	
		Tab lTab = bar.newTab();
		Tab aTab = bar.newTab();
		lTab.setText("Listners");
		aTab.setText("Audience");
		lTab.setTag(1);
		aTab.setTag(2);
		lTab.setTabListener(this);
		aTab.setTabListener(this);
		bar.addTab(lTab);
		bar.addTab(aTab);
	
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onTabReselected(Tab arg0, FragmentTransaction arg1) {

	}

	@Override
	public void onTabSelected(Tab arg0, FragmentTransaction arg1) {
		Fragment fr = null;
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		switch ((Integer) arg0.getTag()) {
		case 1:
			fr = lfr;
			Log.d("mes", "onTabSelected1");
			break;
		case 2:
			Log.d("mes", "onTabSelected2");
			fr = afr;
			break;
		}
		ft.replace(R.id.frgmCount, fr);
		ft.commit();
	}

	@Override
	public void onTabUnselected(Tab arg0, FragmentTransaction arg1) {

	}

}
