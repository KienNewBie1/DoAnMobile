package com.example.testsqlite;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

	private DatabaseHelper db;
	private EditText usernameEditText, passwordEditText;
	private Button loginButton, registerButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		db = new DatabaseHelper(this);

		usernameEditText = findViewById(R.id.usernameEditText);
		passwordEditText = findViewById(R.id.passwordEditText);
		loginButton = findViewById(R.id.loginButton);
		registerButton = findViewById(R.id.registerButton);

		// Xử lý nút Đăng nhập
		loginButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String username = usernameEditText.getText().toString().trim();
				String password = passwordEditText.getText().toString().trim();

				if (db.checkUser(username, password)) {
					// Đăng nhập thành công
					Toast.makeText(MainActivity.this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
					Intent intent = new Intent(MainActivity.this, HomeActivity.class);
					intent.putExtra("username", username); // Gửi tên người dùng tới trang Home
					startActivity(intent);
				} else {
					// Sai tên đăng nhập hoặc mật khẩu
					Toast.makeText(MainActivity.this, "Tên đăng nhập hoặc mật khẩu không đúng!", Toast.LENGTH_SHORT).show();
				}
			}
		});

		// Xử lý nút Đăng ký
		registerButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String username = usernameEditText.getText().toString().trim();
				String password = passwordEditText.getText().toString().trim();

				if (username.isEmpty() || password.isEmpty()) {
					Toast.makeText(MainActivity.this, "Vui lòng điền đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
				} else if (db.checkUserExists(username)) {
					// Tên đăng nhập đã tồn tại
					Toast.makeText(MainActivity.this, "Tên đăng nhập đã tồn tại. Vui lòng chọn tên khác!", Toast.LENGTH_SHORT).show();
				} else {
					// Thêm người dùng mới
					if (db.addUser(username, password)) {
						Toast.makeText(MainActivity.this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();
					} else {
						Toast.makeText(MainActivity.this, "Có lỗi xảy ra. Vui lòng thử lại!", Toast.LENGTH_SHORT).show();
					}
				}
			}
		});
	}
}
