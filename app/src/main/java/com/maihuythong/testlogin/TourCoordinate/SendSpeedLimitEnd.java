package com.maihuythong.testlogin.TourCoordinate;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.maihuythong.testlogin.LoginActivity;
import com.maihuythong.testlogin.R;
import com.maihuythong.testlogin.signup.APIService;
import com.maihuythong.testlogin.signup.ApiUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SendSpeedLimitEnd extends Activity {
    private SharedPreferences sf;
    private EditText text;
    private Button button;
    double latitude;
    double longitude;
    long tourId;
    long userId;

    @Override
    protected  void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end_speed_limit);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        final Intent intent = getIntent();
        tourId = intent.getLongExtra("tourId", 0);
        userId = intent.getLongExtra("userId", 0);
        latitude = intent.getDoubleExtra("latitude", 1);
        longitude = intent.getDoubleExtra("longitude",1);

        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;

        getWindow().setLayout((int)(width*0.9), (int)(height*0.2));
        text = findViewById(R.id.text_end_limit_speed);
        button = findViewById(R.id.send_speed_limit);

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.y = -50;
        getWindow().setAttributes(params);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s;
                if (!text.getText().toString().equals(""))
                {
                   s = "e"+text.getText().toString();
                }else {
                    s = "e";
                }
                String token = LoginActivity.token;
                if (token == null) {
                    sf = getSharedPreferences("com.maihuythong.testlogin_preferences", MODE_PRIVATE);
                    token = sf.getString("login_access_token", "");
                }
                APIService apiService = ApiUtils.getAPIService();
                apiService.sendNotificationOnRoad(token,latitude,longitude,tourId,userId,3,60, s).enqueue(new Callback<SendNotificationOnRoad>() {
                    @Override
                    public void onResponse(Call<SendNotificationOnRoad> call, Response<SendNotificationOnRoad> response) {
                        Log.d("SEND SPEED", response.body().toString());
                        text.setText("");
                        Toast.makeText(getApplicationContext(),"Send notification successful", Toast.LENGTH_SHORT).show();
                        finish();
                    }

                    @Override
                    public void onFailure(Call<SendNotificationOnRoad> call, Throwable t) {
                        Log.d("SEND SPEED", t.toString());
                    }
                });
            }
        });


    }
}
