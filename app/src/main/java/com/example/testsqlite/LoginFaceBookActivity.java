package com.example.testsqlite;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.facebook.AccessToken;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.CallbackManager;

import org.json.JSONObject;

import java.util.Arrays;

public class LoginFaceBookActivity extends AppCompatActivity {
	private CallbackManager callbackManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		// Khởi tạo CallbackManager
		callbackManager = CallbackManager.Factory.create();

		// Yêu cầu quyền đăng nhập (tên, email)
		LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile"));

		// Đăng ký Callback
		LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
			@Override
			public void onSuccess(LoginResult loginResult) {
//				// Đăng nhập thành công
//				AccessToken accessToken = loginResult.getAccessToken();
//				getUserProfile(accessToken); // Gọi hàm lấy thông tin người dùng
				Toast.makeText(LoginFaceBookActivity.this, "Login Yeyyy!", Toast.LENGTH_LONG).show();
			}

			@Override
			public void onCancel() {
				// Hủy đăng nhập
				Toast.makeText(LoginFaceBookActivity.this, "Login Cancelled!", Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onError(FacebookException error) {
				// Lỗi khi đăng nhập
				Toast.makeText(LoginFaceBookActivity.this, "Login Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
			}
		});
	}

	private void getUserProfile(AccessToken accessToken) {
		GraphRequest request = GraphRequest.newMeRequest(
				accessToken,
				new GraphRequest.GraphJSONObjectCallback() {
					@Override
					public void onCompleted(JSONObject object, GraphResponse response) {
						try {
							// Lấy dữ liệu từ JSON trả về
							String name = object.getString("name"); // Tên người dùng

							// Log dữ liệu (có thể hiển thị hoặc chuyển qua Activity khác)
							Log.d("FacebookLogin", "Name: " + name);

							// Chuyển sang HomeActivity và truyền dữ liệu
							Intent intent = new Intent(LoginFaceBookActivity.this, HomeActivity.class);
							intent.putExtra("name", name);
							startActivity(intent);
							finish();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});

//		// Yêu cầu các trường thông tin cần thiết
//		Bundle parameters = new Bundle();
//		parameters.putString("fields", "id,name,email,picture.type(large)");
//		request.setParameters(parameters);
//		request.executeAsync();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		callbackManager.onActivityResult(requestCode, resultCode, data);
	}
}
