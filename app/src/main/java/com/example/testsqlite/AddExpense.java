package com.example.testsqlite;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
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
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.text.DateFormat;
import java.util.Calendar;

public class AddExpense extends AppCompatActivity {
	public static final String DATABASE_NAME = "SavingsManager.db";
	DateFormat  fmtDateAndTime = DateFormat.getDateTimeInstance();
	TextView    lblDateAndTime;
	Calendar    myCalendar = Calendar.getInstance();
	EditText    edtTitleExpense,edtExpenseAmount;
	Button      btnAddExpense,btnDate,btnExit;
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
		setContentView(R.layout.activity_add_expense);
		lblDateAndTime      =   findViewById(R.id.lblDateAndTime);
		btnDate             =   findViewById(R.id.btnDate);
		btnAddExpense       =   findViewById(R.id.btnAddExpense);
		edtTitleExpense     =   findViewById(R.id.edtTitleExpense);
		edtExpenseAmount    =   findViewById(R.id.edtExpenseAmount);
		btnExit             =   findViewById(R.id.btnExit);
		db = openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE, null);
		btnDate.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				new DatePickerDialog(AddExpense.this, d,
						myCalendar.get(Calendar.YEAR),
						myCalendar.get(Calendar.MONTH),
						myCalendar.get(Calendar.DAY_OF_MONTH)).show();
			}
		});
		updateLabel();
		btnAddExpense.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				String  name    =   edtTitleExpense.getText().toString().trim();
				String  amount  =   edtExpenseAmount.getText().toString().trim();
				ContentValues values  = new ContentValues();
				values.put("name", name);
				values.put("amount", amount);
				db.insert("tblexpenses", null, values);
				edtTitleExpense.setText("");
				edtExpenseAmount.setText("");
				Intent intent = new Intent(AddExpense.this, ExpenseManagementActivity.class);
				startActivity(intent);
				Toast.makeText(getApplicationContext(), "Thêm thành công!", Toast.LENGTH_LONG).show();
			}
		});
		btnExit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent   =   new Intent(AddExpense.this,ExpenseManagementActivity.class);
				startActivity(intent);
			}
		});
	}
	private void updateLabel() {
		lblDateAndTime.setText(fmtDateAndTime.format(myCalendar.getTime()));
	}
}