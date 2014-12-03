package com.jacobsheehy.coffeetracker;

import java.util.Calendar;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class CoffeeDb {

	// Tables
	public static final String COFFEE_SIZES = "coffee_sizes";
	public static final String CONSUMPTION_LOG = "consumption_log";
	public static final String PRODUCTIVITY_LOG = "productivity_log";
	
	// Coffee Size fields
	public static final String KEY_ROW_ID = "_id";
	public static final String KEY_COFFEE_SIZE_NAME = "coffee_size_name";
	public static final String KEY_COFFEE_SIZE_OZ = "coffee_size_oz";
	
	// Consumption Log fields
	public static final String KEY_TIME = "time";
	public static final String KEY_SIZE_NAME = "size";

	// Productivity fields
	public static final String KEY_PRODUCTIVITY_TIME = "productivity_notification_entered";
	public static final String KEY_PRODUCTIVITY_RATING = "productivity_rating";
	
	
	private Context mContext;

	private DatabaseHelper mDbHelper;
	private SQLiteDatabase mDB;
	private static final String COFFEE_SIZES_TABLE_CREATE = "create table "
			+ COFFEE_SIZES
			+ " (_id integer primary key autoincrement, " + KEY_COFFEE_SIZE_NAME
			+ " text not null, " + KEY_COFFEE_SIZE_OZ + " real not null)";
	
	private static final String CONSUMPTION_LOG_TABLE_CREATE = "create table "
			+ CONSUMPTION_LOG
			+ " (_id integer primary key autoincrement, " + KEY_TIME
			+ " real not null, " + KEY_SIZE_NAME + " text not null)";
	
	private static final String PRODUCTIVITY_LOG_TABLE_CREATE = "create table "
			+ PRODUCTIVITY_LOG
			+ " (_id integer primary key autoincrement, " + KEY_PRODUCTIVITY_TIME
			+ " real not null, " + KEY_PRODUCTIVITY_RATING + " text not null)";

	private static final String DATABASE_NAME = "CoffeeDb";
	private static final int DATABASE_VERSION = 4; 

	public CoffeeDb open() throws SQLException {
		mDbHelper = new DatabaseHelper(mContext);
		mDB = mDbHelper.getWritableDatabase();
		return this;
	}

	public void close() {
		mDbHelper.close();
	}
	
	/**
	 * Add new productivity

	 * @return
	 */
	public long addProductivity(int rating) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_PRODUCTIVITY_TIME, System.currentTimeMillis());
		initialValues.put(KEY_PRODUCTIVITY_RATING, rating);
		return mDB.insert(PRODUCTIVITY_LOG, null, initialValues);
	}

	/**
	 * Add new coffee

	 * @return
	 */
	public long addCoffee(String sizeName) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_TIME, System.currentTimeMillis());
		initialValues.put(KEY_SIZE_NAME, sizeName);
		return mDB.insert(CONSUMPTION_LOG, null, initialValues);
	}
	

	/**
	 * Delete a coffee coffee

	 * @return
	 */
	public long deleteCoffee(int index) {
		return mDB.delete(CONSUMPTION_LOG, "_id=?", new String[] { index + "" });
	}
	
	/**
	 * Fetch recent condition delivery
	 * 
	 * @return
	 */
	public Cursor fetchTodaysCoffees() {
		Calendar today = Calendar.getInstance();
		today.set(Calendar.HOUR_OF_DAY, 0);
		today.set(Calendar.MINUTE, 0);
		today.set(Calendar.SECOND, 0);
		long msAgo = today.getTimeInMillis();
		return mDB.query(CONSUMPTION_LOG, new String[] { KEY_ROW_ID,
				KEY_TIME, KEY_SIZE_NAME},
				KEY_TIME + " > " + msAgo, null, null, null, null);
	}

	private class DatabaseHelper extends SQLiteOpenHelper {

		DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}
		
		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(COFFEE_SIZES_TABLE_CREATE);
			db.execSQL(CONSUMPTION_LOG_TABLE_CREATE);
			db.execSQL(PRODUCTIVITY_LOG_TABLE_CREATE);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// Build upgrade mechanism
			//db.execSQL("DROP TABLE " + COFFEE_SIZES);
			//db.execSQL("DROP TABLE " + CONSUMPTION_LOG);
		
			// db.execSQL(COFFEE_SIZES_TABLE_CREATE);
			// db.execSQL(CONSUMPTION_LOG_TABLE_CREATE);
			// db.execSQL(PRODUCTIVITY_LOG_TABLE_CREATE);
		}
	}
	
	public CoffeeDb(Context ctx) {
		this.mContext = ctx;
	}

}
