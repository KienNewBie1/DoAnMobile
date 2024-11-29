package com.example.testsqlite;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class IncomeManager extends AppCompatActivity {
	private ImageButton btnAddIncome;
	private Spinner incomeSourceSpinner;
	private TextView totalIncomeAmount;
	private ListView incomeListView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_hahahaaa);

		btnAddIncome = findViewById(R.id.btnAddIncome);
		totalIncomeAmount = findViewById(R.id.txtTotalAmount);
		incomeListView = findViewById(R.id.lstIncome);
		// Thêm thu nhập
		btnAddIncome.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// Giả sử thu nhập là 1000000 và người dùng chọn "Lương"
				String incomeSource = incomeSourceSpinner.getSelectedItem().toString();
				int incomeAmount = 1000000; // Ví dụ thu nhập cố định
				totalIncomeAmount.setText(String.valueOf(incomeAmount));

				// Hiển thị thông báo
				Toast.makeText(IncomeManager.this, "Thu nhập đã được thêm từ " + incomeSource, Toast.LENGTH_SHORT).show();
			}
		});
	}
}
