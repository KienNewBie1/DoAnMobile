package com.example.testsqlite;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.facebook.login.widget.LoginButton;

public class LoginActivity extends AppCompatActivity {

	public static final String COLUMN_ID = "id";
	public static final String COLUMN_USER_ID = "user_id";
	public static final String COLUMN_GOAL_NAME = "goal_name";
	public static final String COLUMN_TARGET_AMOUNT = "target_amount";
	public static final String COLUMN_SAVED_AMOUNT = "saved_amount";
	public static final String COLUMN_START_DATE = "start_date";
	public static final String COLUMN_END_DATE = "end_date";
	public static final String COLUMN_STATUS = "status";
	public static String UserName="";

	public static final String DATABASE_NAME = "SavingsManager.db";
	SQLiteDatabase db;
	EditText edtUsername, edtPassword,edtName;
	LoginButton btnloginface;
	Button btnLogin;
	TextView txtCloseLogin;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		btnloginface    = findViewById(R.id.btnloginface);
		edtUsername     = findViewById(R.id.usernameEditText);
		edtPassword     = findViewById(R.id.passwordEditText);
		txtCloseLogin   = findViewById(R.id.registerLink);
		btnLogin        = findViewById(R.id.loginButton);

		initDB();
		btnLogin.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String username = edtUsername.getText().toString();
				String password = edtPassword.getText().toString();

				if (username.isEmpty()) {
					Toast.makeText(getApplicationContext(),
							"Xin vui lòng nhập tài khoản",
							Toast.LENGTH_LONG).show();
					edtUsername.requestFocus();
				}
				else if (password.isEmpty()) {
					Toast.makeText(getApplicationContext(),
							"Xin vui lòng nhập mật khẩu",
							Toast.LENGTH_LONG).show();
					edtUsername.requestFocus();
				}
				else if (isUser(username, password)) {
					String query = "SELECT name FROM tblAcc WHERE username = ? ";
					Cursor cursor = db.rawQuery(query, new String[]{username});
					if (cursor != null && cursor.moveToFirst()) {
						// Xử lý dữ liệu
						@SuppressLint("Range")
						String fullname = cursor.getString(cursor.getColumnIndexOrThrow("name"));
						Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
						intent.putExtra("name",fullname);
						startActivity(intent);
					}
					cursor.close();
				}
				else {
					Toast.makeText(getApplicationContext(),
							"Tài khoản hoặc mật khẩu không tồn tại",
							Toast.LENGTH_LONG).show();
				}
			}
		});
				// Chuyển sang trang Đăng ký
		txtCloseLogin.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
				startActivity(intent);
			}
		});
		btnloginface.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(LoginActivity.this,LoginFaceBookActivity.class);
				startActivity(intent);
			}
		});
	}

	private void initDB() {
		db = openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE, null);
		String sql="";
		try {
			// Kiểm tra và tạo bảng `tbluser`
//			if (!isTableExists(db, "tbluser")) {
//				db.execSQL("CREATE TABLE tbluser (id INTEGER PRIMARY KEY AUTOINCREMENT, username TEXT UNIQUE, password TEXT)");
//				sql = "INSERT INTO tbluser(username, password) VALUES('admin', 'admin')";
//				db.execSQL(sql);
//			}
			if (!isTableExists(db, "tblAcc")) {
				db.execSQL("CREATE TABLE tblAcc (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, username TEXT UNIQUE, password TEXT)");
				sql = "INSERT INTO tblAcc(name,username, password) VALUES('ADMIN','admin', 'admin')";
				db.execSQL(sql);
			}

			// Kiểm tra và tạo bảng `tblcategories`
			if (!isTableExists(db, "tblcategories")) {
				db.execSQL("CREATE TABLE tblcategories (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, type TEXT)");
			}

			if (!isTableExists(db, "tblexpenses")) {
				db.execSQL("CREATE TABLE tblexpenses (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, amount TEXT)");
			}
			// Kiểm tra và tạo bảng `tbltransactions`
			if (!isTableExists(db, "tbltransactions")) {
				db.execSQL("CREATE TABLE tbltransactions (" +
						"id INTEGER PRIMARY KEY AUTOINCREMENT, " +
						"user_id INTEGER, " +
						"title TEXT, " +
						"amount REAL, " +
						"type TEXT, " +
						"date TEXT, " +
						"note TEXT, " +
						"FOREIGN KEY(user_id) REFERENCES tblAcc(id))");
			}
			// Kiểm tra và tạo bảng `tblexpense`
			if (!isTableExists(db, "tblexpense")) {
				db.execSQL("CREATE TABLE tblexpense (" +
						"id INTEGER PRIMARY KEY AUTOINCREMENT, " +
						"user_id INTEGER, " +
						"title TEXT, " +
						"amount REAL, " +
						"date TEXT NOT NULL, " +
						"FOREIGN KEY(user_id) REFERENCES tblAcc(id))");
			}

			// Kiểm tra và tạo bảng `tblbudgets`
			if (!isTableExists(db, "tblbudgets")) {
				db.execSQL("CREATE TABLE tblbudgets (" +
						"id INTEGER PRIMARY KEY AUTOINCREMENT, " +
						"user_id INTEGER, " +
						"month TEXT, " +
						"category_id INTEGER, " +
						"amount REAL, " +
						"FOREIGN KEY(user_id) REFERENCES tblAcc(id), " +
						"FOREIGN KEY(category_id) REFERENCES tblcategories(id))");

			}
			// Kiểm tra và tạo bảng `tblsavings`
			if (!isTableExists(db, "tblsavings")) {
				db.execSQL("CREATE TABLE tblsavings (" +
						COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
						COLUMN_USER_ID + " INTEGER NOT NULL, " +
						COLUMN_GOAL_NAME + " TEXT NOT NULL, " +
						COLUMN_TARGET_AMOUNT + " REAL NOT NULL, " +
						COLUMN_SAVED_AMOUNT + " REAL DEFAULT 0, " +
						COLUMN_START_DATE + " TEXT NOT NULL, " +
						COLUMN_END_DATE + " TEXT NOT NULL, " +
						COLUMN_STATUS + " TEXT  NOT NULL, " +
						"FOREIGN KEY (" + COLUMN_USER_ID + ") REFERENCES " + "tblAcc" + "(id)" +
						");") ;
			}
			Toast.makeText(this,
					"Khởi tạo CSDL thành công",
					Toast.LENGTH_LONG).show();
		}
		catch (Exception ex) {
			Toast.makeText(this, "Khởi tạo CSDL không thành công: " + ex.getMessage(), Toast.LENGTH_LONG).show();
		}
	}


	private boolean isTableExists(SQLiteDatabase db, String tableName) {
		Cursor cursor = db.rawQuery(
				"SELECT DISTINCT name FROM sqlite_master WHERE type='table' AND name=?",
				new String[]{tableName}
		);
		boolean exists = cursor.getCount() > 0;
		cursor.close();
		return exists;
	}


	private boolean isUser(String username, String password) {
		try {
			db = openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE, null);
			Cursor c = db.rawQuery("select * from tblAcc where username = ? and password = ?",
					new String[] {username, password});
			c.moveToFirst();
			if (c.getCount() > 0) {
				UserName = username;
				return true;
			}
		}
		catch (Exception ex) {
			Toast.makeText(this,
					"Lỗi hệ thống",
					Toast.LENGTH_LONG).show();
		}
		return false;
	}

}
