package com.maihuythong.testlogin.showlist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.maihuythong.testlogin.CreateTourActivity;
import com.maihuythong.testlogin.LoginActivity;
import com.maihuythong.testlogin.R;
import com.maihuythong.testlogin.signup.APIService;
import com.maihuythong.testlogin.signup.ApiUtils;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShowListActivity extends AppCompatActivity {
    private ListView lvTour;
    private Tour[] t;
    private SharedPreferences sf;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_list);

        //init fab button
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddTour();
            }
        });

        APIService mAPIService = ApiUtils.getAPIService();
        Intent intent = getIntent();

        String s;
        s = LoginActivity.token;
        if(s == null){
            sf = getSharedPreferences("com.maihuythong.testlogin_preferences", MODE_PRIVATE);
            s = sf.getString("login_access_token", "");
        }

        mAPIService.getList(s,25, 1).enqueue(new Callback<ShowListReq>() {
            @Override
            public void onResponse(Call<ShowListReq> call, Response<ShowListReq> response) {
                if(response.code() == 200){
                    t = response.body().getTours();
                    Log.d("mmm", "" + response.body().getTotal());
                    lvTour = (ListView) findViewById(R.id.lv_tour);
                    ArrayList<Tour> arrTour = new ArrayList<>();

                    for(int i = 0; i<t.length; i++){
                        arrTour.add(t[i]);
                    }

                    CustomAdapter customAdaper = new CustomAdapter(ShowListActivity.this,R.layout.row_listview,arrTour);
                    lvTour.setAdapter(customAdaper);
                }
                else{
                    //TODO
                }
            }

            @Override
            public void onFailure(Call<ShowListReq> call, Throwable throwable) {
                //TODO
            }
        });

//        lvTour = (ListView) findViewById(R.id.lv_tour);
//        ArrayList<Tour> arrTour = new ArrayList<>();
//
//        for(int i = 0; i<t.length; i++){
//            arrTour.add(t[i]);
//        }
//
//        CustomAdapter customAdaper = new CustomAdapter(this,R.layout.row_listview,arrTour);
//        lvTour.setAdapter(customAdaper);








//        findViewById(R.id.see_list).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                APIService mAPIService = ApiUtils.getAPIService();
//                mAPIService.getList(5, 1).enqueue(new Callback<ShowListReq>() {
//                    @Override
//                    public void onResponse(Call<ShowListReq> call, Response<ShowListReq> response) {
//                        if(response.code() == 200){
////                            Log.d("mmm", "" + response.body().getTotal());
//                            Tour[] t = response.body().getTours();
//                            Log.d("mmm", "" + response.body().getTotal());
//                        }
//                        else{
//                            //TODO
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Call<ShowListReq> call, Throwable throwable) {
//                        //TODO
//                    }
//                });
//            }
//        });
    }

    private void AddTour() {
        startActivity(new Intent(this, CreateTourActivity.class));
    }


//    mAPIService = new
//        mAPIService.getList(1, 3).enqueue(new Callback<ShowListReq>() {
//        @Override
//        public void onResponse(Call<ShowListReq> call, Response<ShowListReq> response) {
//            Log.i("aaa", "post submitted to API." + response.body().toString());
//        }
//
//        @Override
//        public void onFailure(Call<ShowListReq> call, Throwable throwable) {
//            Log.i("aaa", "post failed to API.");
//        }
//    });
}
