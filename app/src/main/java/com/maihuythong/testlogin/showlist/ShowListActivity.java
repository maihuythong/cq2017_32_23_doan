package com.maihuythong.testlogin.showlist;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_list);
        APIService mAPIService = ApiUtils.getAPIService();
        mAPIService.getList(25, 1).enqueue(new Callback<ShowListReq>() {
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
