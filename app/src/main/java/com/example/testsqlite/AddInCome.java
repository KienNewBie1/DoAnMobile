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

public class AddInCome extends AppCompatActivity {
	public static final String DATABASE_NAME = "SavingsManager.db";
	private     EditText    edtTitle, edtAmount;
	private 	Button      btnSaveIncome,btnExit,btnDate;
	DateFormat fmtDateAndTime = DateFormat.getDateTimeInstance();
	TextView lblDateAndTime;
	Calendar myCalendar = Calendar.getInstance();
	SQLiteDatabase db;
	DatePickerDialog.OnDateSetListener d = new DatePickerDialog.OnDateSetListener()
	{
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
		setContentView(R.layout.activity_add_in_come);
		lblDateAndTime  = (TextView) findViewById(R.id.lblDateAndTime);
		btnDate         = (Button) findViewById(R.id.btnDate);
		edtTitle        = (EditText) findViewById(R.id.edtTitleIcome);
		edtAmount       = (EditText) findViewById(R.id.edtIcomeAmount);
		btnSaveIncome   = (Button) findViewById(R.id.btnsaveIncome);
		btnExit         =  findViewById(R.id.btnExit);
		db = openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE, null);
		btnDate.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				new DatePickerDialog(AddInCome.this, d,
						myCalendar.get(Calendar.YEAR),
						myCalendar.get(Calendar.MONTH),
						myCalendar.get(Calendar.DAY_OF_MONTH)).show();
			}
		});
		updateLabel();
		btnSaveIncome.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				String  name    =   edtTitle.getText().toString().trim();
				String  amount  =   edtAmount.getText().toString().trim();
				ContentValues values  = new ContentValues();
				values.put("name", name);
				values.put("type", amount);
				db.insert("tblcategories", null, values);
				edtTitle.setText("");
				edtAmount.setText("");
				Intent intent = new Intent(AddInCome.this, IncomeManagementActivity.class);
				startActivity(intent);
				Toast.makeText(getApplicationContext(), "Thêm thành công!", Toast.LENGTH_LONG).show();
			}
		});
		btnExit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent   =   new Intent(AddInCome.this,IncomeManagementActivity.class);
				startActivity(intent);
			}
		});
	}

	@SuppressLint("Range")
	private void updateItemById(int position) {
		ContentValues values    =   new ContentValues();
		String query = "SELECT * FROM tblcategories";
		Cursor cursor = db.rawQuery(query, null);
		int id = cursor.getInt(cursor.getColumnIndex("id"));
		try {
			values.put("name", edtTitle.getText().toString());
			values.put("type", edtAmount.getText().toString());
		//	values.put("number_student", Integer.parseInt(edtNumber.getText().toString()));
			int hahaa = db.update("tblcategories",
					values,
					"id=?",
					new String[] {String.valueOf(id)});
		}
		catch (Exception ex) {
			Toast.makeText(this,
					"Cập nhật lớp học không thành công",
					Toast.LENGTH_LONG).show();
		}

		// Đóng con trỏ để giải phóng tài nguyên
		cursor.close();
	}
	private void updateLabel() {
		lblDateAndTime.setText(fmtDateAndTime.format(myCalendar.getTime()));
	}
}