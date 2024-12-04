package com.example.testsqlite;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.View;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class ExpenseManagementActivity extends AppCompatActivity {
	public static int vitri = 0;
	public static String vitriID = "";
	public static double ExpenseChart = 0;
	LinearLayout    btnHome,btnChart,btnProfile;
	private ImageButton btnAddExpense;
	private ListView incomeListView;
	private EditText edtIcomeAmount, edtTitleIcome;
	ArrayAdapter<String> adapter;
	ArrayList<String> incomeList;
	SQLiteDatabase db;
	private double totalAmount = 0.0;
	private TextView txtTotalAmount;
	int posselected = -1;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_expense_management);
		btnChart            =   findViewById(R.id.btnChart);
		btnHome             =   findViewById(R.id.btnHome);
		btnProfile          =   findViewById(R.id.btnProfile);
		btnAddExpense        = findViewById(R.id.btnAddExpense);
		txtTotalAmount      = findViewById(R.id.txtTotalAmount);
		// Ánh xạ ListView
		incomeListView = findViewById(R.id.lstIncome);
		// Mở hoặc tạo cơ sở dữ liệu
		db = openOrCreateDatabase("SavingsManager.db", MODE_PRIVATE, null);
		// Khởi tạo danh sách
		incomeList = new ArrayList<>();
		// Load dữ liệu từ database
		loadIncomeData();
		// Gán adapter cho ListView
		adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, incomeList);
		incomeListView.setAdapter(adapter);
		registerForContextMenu(incomeListView);
		incomeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				posselected = position;
			}
		});
		btnHome.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent   =   new Intent(ExpenseManagementActivity.this,HomeActivity.class);
				startActivity(intent);
			}
		});
		btnChart.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent   =   new Intent(ExpenseManagementActivity.this,ChartActivity.class);
				startActivity(intent);
			}
		});
		btnProfile.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent   =   new Intent(ExpenseManagementActivity.this,ProfileActivity.class);
				startActivity(intent);
			}
		});

	}
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
	                                ContextMenu.ContextMenuInfo menuInfo) {
		// TODO Auto-generated method stub
		super.onCreateContextMenu(menu, v, menuInfo);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.contextmenu, menu);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
		int position = info.position;
		// Kiểm tra hành động từ menu ngữ cảnh
		if (item.getItemId() == R.id.menuDetail) {
			// Hiển thị chi tiết
			return true;
		} else if (item.getItemId() == R.id.menuDelete) {
			// Xóa mục
			deleteItemById(position);
			Intent intent   =   new Intent(ExpenseManagementActivity.this,ExpenseManagementActivity.class);
			startActivity(intent);
			// Cập nhật danh sách hiển thị
			loadIncomeData();
			return true;
		} else if (item.getItemId() == R.id.menuEdit) {
			Intent intent =  new Intent(ExpenseManagementActivity.this, EditExpense.class);
			startActivity(intent);
		}
		else {
			return super.onContextItemSelected(item);
		}
		return false;
	}
	// Hàm hiển thị chi tiết mục
	private void showItemDetails(String item) {
		Toast.makeText(this, "Chi tiết mục: " + item, Toast.LENGTH_SHORT).show();
	}
	// Hàm xóa mục
	@SuppressLint("Range")
	private void deleteItemById(int position) {
		String query = "SELECT * FROM tblexpenses";
		Cursor cursor = db.rawQuery(query, null);

		// Di chuyển con trỏ đến hàng tương ứng với vị trí trong ListView
		if (cursor.moveToPosition(position)) {
			// Lấy ID từ cột "id"
			int id = cursor.getInt(cursor.getColumnIndex("id"));
			vitri=id;
			// Thực hiện xóa mục dựa trên ID
			int rowsAffected = db.delete("tblexpenses", "id=?", new String[]{String.valueOf(id)});

			if (rowsAffected > 0) {
				// Xóa mục khỏi danh sách
				incomeList.remove(position); // Xóa khỏi danh sách hiển thị
				adapter.notifyDataSetChanged(); // Cập nhật giao diện
				Toast.makeText(this, "Xoá mục thành công", Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(this, "Xoá mục thất bại", Toast.LENGTH_SHORT).show();
			}
		} else {
			Toast.makeText(this, "Không tìm thấy mục để xóa", Toast.LENGTH_SHORT).show();
		}

		// Đóng con trỏ để giải phóng tài nguyên
		cursor.close();
	}
	@SuppressLint("Range")
	private void loadIncomeData() {
		// Xóa dữ liệu cũ trong danh sách
		incomeList.clear();
		// Truy vấn dữ liệu từ bảng income
		String query = "SELECT * FROM tblexpenses";
		Cursor cursor = db.rawQuery(query, null);

		// Kiểm tra dữ liệu trả về
		if (cursor.moveToFirst()) {
			do {
				@SuppressLint("Range") String title = cursor.getString(cursor.getColumnIndex("name"));
				@SuppressLint("Range") String amount = cursor.getString(cursor.getColumnIndex("amount"));
				vitriID = cursor.getString(cursor.getColumnIndex("id"));
				// Chuyển số tiền từ chuỗi sang số thực (double)
				double Amount = 0.0;
				try {
					Amount = Double.parseDouble(amount);
					totalAmount += Amount; // Cộng dồn vào tổng
					ExpenseChart = totalAmount;
				} catch (NumberFormatException e) {
					// Xử lý nếu không chuyển đổi được số tiền
					Amount = 0.0;
				}
				// Thêm dữ liệu vào danh sách
				incomeList.add(title + "                    " + amount + " VND");
			} while (cursor.moveToNext());
		}
		cursor.close();
		//////
		txtTotalAmount.setText(String.format(Locale.getDefault(), "%.2f VND", totalAmount));
		btnAddExpense.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(ExpenseManagementActivity.this, AddExpense.class);
				startActivity(intent);
			}
		});
	}
}
