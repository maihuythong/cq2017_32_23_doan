package com.maihuythong.testlogin.ShowNotiOnRoadList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.widget.Toast;

import com.maihuythong.testlogin.R;
import com.maihuythong.testlogin.TourCoordinate.NotiOnRoadAdapter;
import com.maihuythong.testlogin.TourCoordinate.noti;
import com.maihuythong.testlogin.manager.MyApplication;
import com.maihuythong.testlogin.signup.APIService;
import com.maihuythong.testlogin.signup.ApiUtils;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShowNotiOnRoad extends AppCompatActivity {
    private String token;
    private long tourId;
    private SharedPreferences sf;
    private ArrayList<NotiOnRoad> arrayList = new ArrayList<>();
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_noti_on_road);

        getSupportActionBar().hide();

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;

        getWindow().setLayout((int)(width*0.9), (int)(height*0.64));
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.y = -140;
        getWindow().setAttributes(params);

        MyApplication app = (MyApplication)getApplication();
        token = app.getToken();
        if(token == null){
            sf = getSharedPreferences("com.maihuythong.testlogin_preferences", MODE_PRIVATE);
            token = sf.getString("login_access_token", "");
        }

        Intent intent = getIntent();
        tourId = intent.getLongExtra("tourId", 0);

        recyclerView = findViewById(R.id.recycle_view);

        APIService apiService = ApiUtils.getAPIService();
        apiService.getNotiOnRoad(token, String.valueOf(tourId), 1, "1000").enqueue(new Callback<NotiOnRoadList>() {
            @Override
            public void onResponse(Call<NotiOnRoadList> call, Response<NotiOnRoadList> response) {
                if(response.code() == 200){
                    Toast.makeText(getApplicationContext(), "Get noti on road", Toast.LENGTH_SHORT).show();
                    arrayList = response.body().getNotiList();
                    NotiOnRoad nor = new NotiOnRoad(10.0, 10.0, "haha", 3, 50, 1);
                    arrayList.add(nor);
                    NotiOnRoadNotChatAdapter notiOnRoadNotChatAdapter = new NotiOnRoadNotChatAdapter(arrayList);

                    recyclerView.setAdapter(notiOnRoadNotChatAdapter);
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
                    linearLayoutManager.scrollToPositionWithOffset(arrayList.size() -1, 0);
                    recyclerView.setLayoutManager(linearLayoutManager);
                }
                else{
                    Toast.makeText(getApplicationContext(), "Error getting noti on road", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<NotiOnRoadList> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Error getting noti on road", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
