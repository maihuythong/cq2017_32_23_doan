package com.maihuythong.testlogin.firebase;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.maihuythong.testlogin.R;
import com.maihuythong.testlogin.TourCoordinate.GetNotificationList;
import com.maihuythong.testlogin.TourCoordinate.NotiOnRoadAdapter;
import com.maihuythong.testlogin.TourCoordinate.SendMessage;
import com.maihuythong.testlogin.TourCoordinate.noti;
import com.maihuythong.testlogin.manager.MyApplication;
import com.maihuythong.testlogin.signup.APIService;
import com.maihuythong.testlogin.signup.ApiUtils;
import com.maihuythong.testlogin.userInfo.UserInfoRes;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MessagingTour extends AppCompatActivity {

    private String tourId;
    private String userId;
    private String token;
    private RecyclerView recyclerView;
    private SharedPreferences sf;
    private ImageButton sendMessage;
    private EditText messText;
    private ArrayList<noti> arrayList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_tour);
        MyApplication app = (MyApplication) getApplication();
        sendMessage = findViewById(R.id.send_message_btn);
        messText = findViewById(R.id.input_message);
        token = app.getToken();
        if (token == null) {
            sf = getSharedPreferences("com.maihuythong.testlogin_preferences", MODE_PRIVATE);
            token = sf.getString("login_access_token", "");
        }
        recyclerView = findViewById(R.id.recycle_view_chat);
//        Intent intent = getIntent();
        tourId = getIntent().getStringExtra("tourId");
        APIService service = ApiUtils.getAPIService();
        service.getUserInfo(token).enqueue(new Callback<UserInfoRes>() {
            @Override
            public void onResponse(Call<UserInfoRes> call, Response<UserInfoRes> response) {
                UserInfoRes UserInfo = response.body();
                userId = String.valueOf(UserInfo.getID());
            }
            @Override
            public void onFailure(Call<UserInfoRes> call, Throwable t) {
            }
        });
        String topic = "/topics/tour-id-" + tourId;

        FirebaseMessaging.getInstance().subscribeToTopic(topic)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg = "Success";
                        if (!task.isSuccessful()) {
                            msg = "Fail";
                        }
                        Log.d("MESSAGING", msg);
                        setNotification();

                        sendMessage.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                sendMes();
                            }
                        });
                    }
                });

        registerReceiver(broadcastReceiver, new IntentFilter("broadCastName"));
    }

    private void sendMes() {
        String mess = messText.getText().toString();
        if (!mess.equals("")) {
            APIService apiService = ApiUtils.getAPIService();
            apiService.sendMessage(token, tourId, String.valueOf(userId), mess).enqueue(new Callback<SendMessage>() {
                @Override
                public void onResponse(Call<SendMessage> call, Response<SendMessage> response) {
                    Log.d("Send mess success", response.body().toString());
                }

                @Override
                public void onFailure(Call<SendMessage> call, Throwable t) {
                    Log.d("Send mess fail", t.toString());
                }
            });

            arrayList.add(new noti(userId, messText.getText().toString()));
            NotiOnRoadAdapter notiOnRoadAdapter = new NotiOnRoadAdapter(arrayList, Long.valueOf(userId), Long.valueOf(userId));

            recyclerView.setAdapter(notiOnRoadAdapter);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
            linearLayoutManager.scrollToPositionWithOffset(arrayList.size() - 1, 0);
            recyclerView.setLayoutManager(linearLayoutManager);
            messText.setText("");

        }
    }

    private void setNotification() {
        APIService apiService = ApiUtils.getAPIService();
        apiService.getNotificationList(token, tourId, 1, "20000").enqueue(new Callback<GetNotificationList>() {
            @Override
            public void onResponse(Call<GetNotificationList> call, Response<GetNotificationList> response) {
                arrayList = response.body().getNotiList();
                NotiOnRoadAdapter notiOnRoadAdapter = new NotiOnRoadAdapter(arrayList, Long.valueOf(userId), Long.valueOf(userId));

                recyclerView.setAdapter(notiOnRoadAdapter);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
                linearLayoutManager.scrollToPositionWithOffset(arrayList.size() - 1, 0);
                recyclerView.setLayoutManager(linearLayoutManager);
                recyclerView.getRecycledViewPool().setMaxRecycledViews(0,0);
            }

            @Override
            public void onFailure(Call<GetNotificationList> call, Throwable t) {

            }
        });
    }

    BroadcastReceiver broadcastReceiver =  new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            Bundle b = intent.getExtras();

            String message = b.getString("message");
            String usid = b.getString("uid");

            long uid = Long.valueOf(usid);
            Log.e("newmesage", "" + message);

            arrayList.add(new noti(String.valueOf(uid), message));
            NotiOnRoadAdapter notiOnRoadAdapter = new NotiOnRoadAdapter(arrayList, uid, Long.valueOf(userId));

            recyclerView.setAdapter(notiOnRoadAdapter);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
            linearLayoutManager.scrollToPositionWithOffset(arrayList.size() - 1, 0);
            recyclerView.setLayoutManager(linearLayoutManager);
            messText.setText("");
        }
    };

}
