package com.example.testsqlite;

import android.graphics.Color;
import android.os.Bundle;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import androidx.appcompat.app.AppCompatActivity;


import java.util.ArrayList;

public class ChartActivity extends AppCompatActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chart);

		PieChart pieChart = (PieChart) findViewById(R.id.pieChart);

		ArrayList<PieEntry> entries = new ArrayList<>();
		entries.add(new PieEntry((float) IncomeManagementActivity.IncomeChart, "Thu Vào"));
		entries.add(new PieEntry((float) ExpenseManagementActivity.ExpenseChart, "Chi Ra"));
		float ConLai    = Float.valueOf((float) (IncomeManagementActivity.IncomeChart - ExpenseManagementActivity.ExpenseChart));
		entries.add(new PieEntry(ConLai, "Còn Lại"));
		// tạo và chỉnh sửa data của biểu đồ
		PieDataSet dataSet = new PieDataSet(entries, "");
		dataSet.setColors(new int[]{Color.GREEN, Color.RED, Color.BLUE});
		dataSet.setValueTextColor(Color.WHITE);
		dataSet.setValueTextSize(12f);

		PieData pieData = new PieData(dataSet);
		pieChart.setData(pieData);

		pieChart.setCenterText("SAVEMN");
		pieChart.setCenterTextSize(18f);
		pieChart.animateY(1000); // Animation
	}
}
