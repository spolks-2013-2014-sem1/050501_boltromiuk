package dbHelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

	public DBHelper(Context context) {
		super(context, "LocatorDB", null, 1);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(" create table listens ( "
				+ " id integer primary key  ," + "name text , "
				+ " address text , " + " connect INTEGER ); ");

		db.execSQL(" create table audience ( "
				+ " id integer primary key  ," + " name text , "
				+ " address text , " + " connect INTEGER ); ");

		db.execSQL(" create table myCordinats ( "
				+ " id integer primary key autoincrement ,"
				+ " latitude real , " + " longitude REAL ); ");

		db.execSQL(" create table takeCordinats ( "
				+ " id integer primary key autoincrement ,"
				+ " latitude real , " + " longitude REAL , " + " address text ); ");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

}
