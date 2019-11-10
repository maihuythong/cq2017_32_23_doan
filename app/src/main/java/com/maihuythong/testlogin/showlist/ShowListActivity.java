package com.maihuythong.testlogin.showlist;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.maihuythong.testlogin.R;
import com.maihuythong.testlogin.signup.APIService;
import com.maihuythong.testlogin.signup.ApiUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShowListActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_list);

        findViewById(R.id.see_list).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                APIService mAPIService = ApiUtils.getAPIService();
                mAPIService.getList(5, 1).enqueue(new Callback<ShowListReq>() {
                    @Override
                    public void onResponse(Call<ShowListReq> call, Response<ShowListReq> response) {
                        if(response.code() == 200){
//                            Log.d("mmm", "" + response.body().getTotal());
                            Tour[] t = response.body().getTours();
                            Log.d("mmm", "" + response.body().getTotal());
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
            }
        });
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
