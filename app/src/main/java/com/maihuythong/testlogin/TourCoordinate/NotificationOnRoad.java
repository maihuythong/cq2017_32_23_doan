package com.maihuythong.testlogin.TourCoordinate;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.maihuythong.testlogin.R;
import com.maihuythong.testlogin.manager.MyApplication;
import com.maihuythong.testlogin.signup.APIService;
import com.maihuythong.testlogin.signup.ApiUtils;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationOnRoad extends Activity {
    private long tourId;
    private long userId;
    private String token;
    private RecyclerView recyclerView;
    private SharedPreferences sf;
    private ImageButton sendMessage;
    private EditText messText;
    private ArrayList<noti> arrayList = new ArrayList<>();

    @Override
    protected  void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_road_chat);
        MyApplication app = (MyApplication)getApplication();
        sendMessage = findViewById(R.id.send_message_btn);
        messText = findViewById(R.id.input_message);
        token = app.getToken();
        if(token == null){
            sf = getSharedPreferences("com.maihuythong.testlogin_preferences", MODE_PRIVATE);
            token = sf.getString("login_access_token", "");
        }

        Intent intent = getIntent();
        tourId = intent.getLongExtra("tourId", 0);
        userId = intent.getLongExtra("userId",0);

        recyclerView = findViewById(R.id.recycle_view_chat);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;

        getWindow().setLayout((int)(width*0.9), (int)(height*0.8));

        setNotification();

        sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMes();
            }
        });


    }

    private void sendMes() {
        String mess = messText.getText().toString();
        if (!mess.equals("")) {
            APIService apiService = ApiUtils.getAPIService();
            apiService.sendMessage(token, String.valueOf(tourId), String.valueOf(userId), mess).enqueue(new Callback<SendMessage>() {
                @Override
                public void onResponse(Call<SendMessage> call, Response<SendMessage> response) {
                    Log.d("Send mess success", response.body().toString());
                }

                @Override
                public void onFailure(Call<SendMessage> call, Throwable t) {
                    Log.d("Send mess fail", t.toString());
                }
            });

            arrayList.add(new noti(String.valueOf(userId), messText.getText().toString()));
            NotiOnRoadAdapter notiOnRoadAdapter = new NotiOnRoadAdapter(arrayList, userId, userId);

            recyclerView.setAdapter(notiOnRoadAdapter);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
            linearLayoutManager.scrollToPositionWithOffset(arrayList.size() -1, 0);
            recyclerView.setLayoutManager(linearLayoutManager);
            messText.setText("");

        }
    }

    private void setNotification() {
        APIService apiService = ApiUtils.getAPIService();
        apiService.getNotificationList(token, String.valueOf(tourId), 1, "100").enqueue(new Callback<GetNotificationList>() {
            @Override
            public void onResponse(Call<GetNotificationList> call, Response<GetNotificationList> response) {
                arrayList = response.body().getNotiList();
                NotiOnRoadAdapter notiOnRoadAdapter = new NotiOnRoadAdapter(arrayList, userId, userId);

                recyclerView.setAdapter(notiOnRoadAdapter);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
                linearLayoutManager.scrollToPositionWithOffset(arrayList.size() -1, 0);
                recyclerView.setLayoutManager(linearLayoutManager);
            }

            @Override
            public void onFailure(Call<GetNotificationList> call, Throwable t) {

            }
        });
    }
}
