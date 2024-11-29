package com.example.testsqlite;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import java.text.DateFormat;
import java.util.Calendar;

	public class EditIncome extends AppCompatActivity {
		public static final String DATABASE_NAME = "SavingsManager.db";
		private EditText edtTitle, edtAmount;
		private Button btnExit, btnUpdateIncome;
		DateFormat fmtDateAndTime = DateFormat.getDateTimeInstance();
		TextView lblDateAndTime;
		Calendar myCalendar = Calendar.getInstance();
		SQLiteDatabase db;
		DatePickerDialog.OnDateSetListener d = new DatePickerDialog.OnDateSetListener() {
			public void onDateSet(DatePicker view,
			                      int year, int monthOfYear, int dayOfMonth) {
				myCalendar.set(Calendar.YEAR, year);
				myCalendar.set(Calendar.MONTH, monthOfYear);
				myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
				updateLabel();
			}
		};

		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			EdgeToEdge.enable(this);
			setContentView(R.layout.activity_edit_income);
			lblDateAndTime  = (TextView) findViewById(R.id.lblDateAndTime);
			Button btnDate  = (Button) findViewById(R.id.btnDate);
			edtTitle        = (EditText) findViewById(R.id.edtTitleIcome);
			edtAmount       = (EditText) findViewById(R.id.edtIcomeAmount);
			btnExit         = findViewById(R.id.btnExit);
			btnUpdateIncome = findViewById(R.id.btnUpdateIncome);
			db = openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE, null);
			btnDate.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					new DatePickerDialog(EditIncome.this, d,
							myCalendar.get(Calendar.YEAR),
							myCalendar.get(Calendar.MONTH),
							myCalendar.get(Calendar.DAY_OF_MONTH)).show();
				}
			});
			updateLabel();
			btnExit.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					finish();
				}
			});
			loadUserData();
			btnUpdateIncome.setOnClickListener(v -> {
				String name = edtTitle.getText().toString().trim();
				String amount = edtAmount.getText().toString().trim();

				// Kiểm tra nếu thông tin không được nhập
				if (name.isEmpty() || amount.isEmpty()) {
					Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
					return;
				}
				// Lấy thông tin hiện tại từ cơ sở dữ liệu (hoặc biến lưu trữ sẵn)
				Cursor cursor = db.rawQuery("SELECT name, type FROM tblcategories WHERE id = ?",new String[]{ String.valueOf(IncomeManagementActivity.vitriID)});
				if (cursor.moveToFirst()) {
					String currentname      = cursor.getString(cursor.getColumnIndexOrThrow("name"));
					String currentamount    = cursor.getString(cursor.getColumnIndexOrThrow("type"));

					// Kiểm tra nếu không có thay đổi
					if (name.equals(currentname) && amount.equals(currentamount)) {
						Toast.makeText(this, "Không có thay đổi để lưu!", Toast.LENGTH_SHORT).show();
						// Quay lại màn hình Profile
						Intent intent = new Intent(this, IncomeManagementActivity.class);
						startActivity(intent);
						cursor.close();
						finish(); // Kết thúc Activity hiện tại
						return;
					}
				}
				cursor.close();

				// Cập nhật thông tin nếu có thay đổi
				updateUser(name, amount);
				Toast.makeText(this, "Cập nhật thành công!", Toast.LENGTH_SHORT).show();

				// Quay lại màn hình Profile
				Intent intent = new Intent(this, IncomeManagementActivity.class);
				startActivity(intent);
				finish();
			});

		}
		private void loadUserData() {
			// Lấy dữ liệu từ bảng
			Cursor cursor = db.rawQuery("SELECT * FROM tblcategories WHERE id=?", new String[] {String.valueOf(IncomeManagementActivity.vitriID)});
			//int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
			if (cursor.moveToFirst()) {
				String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
				String amount = cursor.getString(cursor.getColumnIndexOrThrow("type"));
				// Hiển thị vào EditText
				edtAmount.setText(amount);
				edtTitle.setText(name);
			}
			cursor.close();
		}

		private void updateUser(String name, String type) {
			// Cập nhật dữ liệu trong bảng
			String query = "SELECT * FROM tblcategories WHERE id=?";
			Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(IncomeManagementActivity.vitriID)});
			if (cursor.moveToFirst()) {
				// Lấy ID từ cột "id"
				@SuppressLint("Range")
				int id = Integer.valueOf(IncomeManagementActivity.vitriID);
				String updateSQL = "UPDATE tblcategories SET name = ?, type = ? WHERE id = ?";
				db.execSQL(updateSQL, new Object[]{name,type,id});
			}
		}
		private void updateLabel() {
			lblDateAndTime.setText(fmtDateAndTime.format(myCalendar.getTime()));
		}
	}

