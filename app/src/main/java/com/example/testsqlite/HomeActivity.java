package com.example.testsqlite;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class HomeActivity extends AppCompatActivity {
	private LinearLayout    btnHome,btnIncome, btnExpense, btnSavings, btnDebt,btnProfile,btnChart;
	private TextView welcomeTextView;

	@SuppressLint("MissingInflatedId")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);

		String name = getIntent().getStringExtra("name");
		welcomeTextView = findViewById(R.id.welcomeTextView);
		welcomeTextView.setText("Xin Chào, " + name + "!");

		// Gắn sự kiện cho từng mục
		btnIncome   =   findViewById(R.id.btnIncome);
		btnExpense  =   findViewById(R.id.btnExpense);
		btnSavings  =   findViewById(R.id.btnSavings);
		btnDebt     =   findViewById(R.id.btnDebt);
		btnProfile  =   findViewById(R.id.btnProfile);
		btnChart    =   findViewById(R.id.btnChart);
		btnHome    =   findViewById(R.id.btnHome);
		btnHome.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent   =   new Intent(HomeActivity.this,HomeActivity.class);
				startActivity(intent);
			}
		});
		btnChart.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(HomeActivity.this, ChartActivity.class);
				startActivity(intent);
			}
		});

		btnProfile.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(HomeActivity.this, ProfileActivity.class);
				startActivity(intent);
			}
		});

		// Set OnClickListener cho nút quản lý thu nhập
		btnIncome.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				// Điều hướng sang trang Quản lý thu nhập
				Intent intent = new Intent(HomeActivity.this, IncomeManagementActivity.class);
				startActivity(intent);
			}
		});

		// Quản lý chi tiêu
		btnExpense.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(HomeActivity.this, ExpenseManagementActivity.class);
				startActivity(intent);
			}
		});
//
//		// Quản lý tiết kiệm
		btnSavings.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(HomeActivity.this, SavingsManagementActivity.class);
				startActivity(intent);
			}
		});
//
//		// Quản lý nợ
		btnDebt.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(HomeActivity.this, DebtManagementActivity.class);
				startActivity(intent);
			}
		});
	}
}

