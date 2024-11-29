package com.example.testsqlite;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {
	public static final String DATABASE_NAME = "SavingsManager.db";
	private SQLiteDatabase db;


	private EditText registerUsernameEditText, registerPasswordEditText,edtName;
	private Button registerButton;
	private TextView loginLink;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);

		registerUsernameEditText    = findViewById(R.id.registerUsernameEditText);
		registerPasswordEditText    = findViewById(R.id.registerPasswordEditText);
		edtName                     = findViewById(R.id.edtName);
		registerButton              = findViewById(R.id.registerButton);
		loginLink                   = findViewById(R.id.loginLink);
		db = openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE, null);

		registerButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				String  username    =   registerUsernameEditText.getText().toString().trim();
				String  password    =   registerPasswordEditText.getText().toString().trim();
				String  name        =   edtName.getText().toString().trim();
				 if (isUser(username)) {
					Toast.makeText(getApplicationContext(),
							"Tài khoản đã tồn tại!",
							Toast.LENGTH_LONG).show();
					registerUsernameEditText.requestFocus();
				}
				else{
					 ContentValues values = new ContentValues();
					 values.put("Name",name);
					 values.put("username", username);
					 values.put("password", password);
					 db.insert("tblAcc", null, values);
					 Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
					 startActivity(intent);
				}
			}
		});
		// Chuyển sang trang Đăng ký
		loginLink.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
				startActivity(intent);
			}
		});
	}
	private boolean isUser(String username) {
		Cursor c = null;
		try {
			// Truy vấn để kiểm tra tài khoản
			c = db.rawQuery("SELECT * FROM tblAcc WHERE username = ?", new String[]{username});
			// Kiểm tra nếu có ít nhất một dòng trả về
			return c.moveToFirst();
		} catch (Exception ex) {
			Toast.makeText(this, "Lỗi hệ thống: " + ex.getMessage(), Toast.LENGTH_LONG).show();
			return false;
		} finally {
			if (c != null) {
				c.close(); // Đảm bảo con trỏ được đóng sau khi sử dụng
			}
		}
	}
}
