package com.example.testsqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "users.db";
	private static final String TABLE_NAME = "users";
	private static final String COL_ID = "id";
	private static final String COL_USERNAME = "username";
	private static final String COL_PASSWORD = "password";

	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String createTable = "CREATE TABLE " + TABLE_NAME + " (" +
				COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
				COL_USERNAME + " TEXT UNIQUE, " +
				COL_PASSWORD + " TEXT)";
		db.execSQL(createTable);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
		onCreate(db);
	}

	// Thêm người dùng
	public boolean addUser(String username, String password) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(COL_USERNAME, username);
		values.put(COL_PASSWORD, password);
		long result = db.insert(TABLE_NAME, null, values);
		return result != -1;
	}
	// Kiểm tra người dùng
	public boolean checkUser(String username, String password) {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME +
						" WHERE " + COL_USERNAME + "=? AND " + COL_PASSWORD + "=?",
				new String[]{username, password});
		boolean exists = cursor.getCount() > 0;
		cursor.close();
		return exists;
	}
	// Bổ sung vào DatabaseHelper.java
// Kiểm tra người dùng đã tồn tại (tránh đăng ký trùng)
	public boolean checkUserExists(String username) {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + COL_USERNAME + "=?", new String[]{username});
		boolean exists = cursor.getCount() > 0;
		cursor.close();
		return exists;
	}

}
