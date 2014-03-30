package com.fireseverityapp;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseEmailContent extends SQLiteOpenHelper{
	
	// Database Version
	private static final int DATABASE_VERSION = 1;

	// Database Name
	private static final String DATABASE_NAME = "FSL_EMAIL_CONTENT";

	// Contacts table name
	private static final String TABLE_NAME = "EmailContent";

	// Contacts Table Columns names
	private static final String KEY_CONTENT_ID = "contentId";
	private static final String KEY_DATE_STAMP = "dateStamp";
	private static final String KEY_LATITUDE = "latitude";
	private static final String KEY_LONGITUDE = "longitude";
	private static final String KEY_IMG_BASE64 = "imgBase64";

	public DatabaseEmailContent(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_NAME + "("
				+ KEY_CONTENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," + KEY_DATE_STAMP + " TEXT,"
				+ KEY_LATITUDE + " TEXT," + KEY_LONGITUDE + " TEXT,"+ KEY_IMG_BASE64 + " TEXT" +")";
		db.execSQL(CREATE_CONTACTS_TABLE);
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

		// Create tables again
		onCreate(db);
		
	}

	/* Deleting content by date stamp
	public void deleteContentByDateStamp(Content content) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_NAME, KEY_DATE_STAMP + " = ?",
				new String[] { String.valueOf(content.dateStamp) });
		db.close();
	}
	*/
	
	// Getting contacts Count
	public int getContentCount() {
		int count = 0;
		String countQuery = "SELECT  * FROM " + TABLE_NAME;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		count = cursor.getCount();
		cursor.close();

		// return count
		return count;
	}


}
