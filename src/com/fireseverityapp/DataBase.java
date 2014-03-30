package com.fireseverityapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/*
 * Author Kavy 15/09/2013
 */
public class DataBase {

	public static final String _id = "_id";
	public static final String location = "location";
	
	// add by Ling An 19/10/2013
	public static final String latitude = "latitude";
	public static final String longitude = "longitude";

	public static final String priority = "priority";
	public static final String path_image = "path_image";

	public static final String email = "email";

	public static final String name = "name";
	public static final String organization = "organization";
	public static final String designation = "designation";

	private static final String DataBase_Name = "moral_story";
	private static final String location_table = "location_table";
	private static final int version = 1;
	private static final String TAG = "DBAdapter";

	private static final String DATA_CREATE1 = "Create table location_table(_id integer primary key autoincrement,"
			+ "latitude text ,"
			+ "longitude text,"
			+ "priority text ,"
			+ "path_image text ,"
			+ "email text ,"
			+ "name text ,"
			+ "organization text ,"
			+ "designation text );";

	@SuppressWarnings("unused")
	private static Context context;
	private DatabaseHelper DBHelper;
	private SQLiteDatabase db;

	@SuppressWarnings("static-access")
	public DataBase(Context con) {
		this.context = con;
		DBHelper = new DatabaseHelper(con);
	}

	private static class DatabaseHelper extends SQLiteOpenHelper {
		DatabaseHelper(Context context) {
			super(context, DataBase_Name, null, version);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(DATA_CREATE1);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.w(TAG, "Upgrading dataBase from version " + oldVersion + " to "
					+ newVersion + ", which will destroy all old data");
			db.execSQL("alter TABLE IF EXISTS note_table");
			onCreate(db);
		}
	}

	public DataBase open() throws Exception {
		db = DBHelper.getWritableDatabase();
		return this;
	}

	public void close() {
		DBHelper.close();
	}

	public long insert__loc(
						String imagepath, 
						String priority_s,
						String latitude_s, 
						String longitude_s,
						String email_s,
						String name_s,
						String organization_s,
						String desig_s ) {

		ContentValues cv1 = new ContentValues();
		cv1.put(path_image, imagepath);
		cv1.put(priority, priority_s);
		//cv1.put(location, location_s);
		cv1.put(latitude, latitude_s);
		cv1.put(longitude, longitude_s);
		cv1.put(email, email_s);
		cv1.put(name, name_s);
		cv1.put(organization, organization_s);
		cv1.put(designation, desig_s);
		return db.insert(location_table, null, cv1);
	}

	public Cursor getAll() {
		Cursor rt = db.rawQuery("Select * from  location_table", null);
		return rt;
	}

	public void delete_table() {
		db.delete(location_table, null, null);
	}

	public void Delete_from_story(int id_s) {

		Log.e("", "sss" + db);
		long as = 00;
		try {
			db.delete(location_table, "_id=" + id_s, null);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public void Delete_from(int idd) {

		Log.e("", "sss" + db);
		
		try {
			db.delete(location_table, _id + "=" + idd, null);
		} catch (Exception e) {
			Log.e("ff", ""+e.getMessage());
		}
	}
}
