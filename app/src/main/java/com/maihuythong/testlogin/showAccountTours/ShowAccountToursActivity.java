package com.maihuythong.testlogin.showAccountTours;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonObjectRequest;
import com.maihuythong.testlogin.LoginActivity;
import com.maihuythong.testlogin.R;
import com.maihuythong.testlogin.showlist.CustomAdapter;
import com.maihuythong.testlogin.showlist.ShowListActivity;
import com.maihuythong.testlogin.showlist.ShowListReq;
import com.maihuythong.testlogin.showlist.Tour;
import com.maihuythong.testlogin.signup.APIService;
import com.maihuythong.testlogin.signup.ApiUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShowAccountToursActivity extends AppCompatActivity {
    private Tour[] t;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_account_tours);
        final ArrayList<Tour> arrAccTours = new ArrayList<>();


        String s;
        s = LoginActivity.token;
        if(s == null){
            SharedPreferences sf = getSharedPreferences("com.maihuythong.testlogin_preferences", MODE_PRIVATE);
            s = sf.getString("login_access_token", "");
        }

        APIService mAPIService = ApiUtils.getAPIService();

        mAPIService.getAccountTours(s,1, 50).enqueue(new Callback<ShowAccountToursReq>() {
            @Override
            public void onResponse(Call<ShowAccountToursReq> call, Response<ShowAccountToursReq> response) {
                if(response.code() == 200){
                    t = response.body().getTours();
                    ListView lvTour = findViewById(R.id.lv_tour);
                    ArrayList<Tour> arrTour = new ArrayList<>();

                    for(int i = 0; i<t.length; i++){
                        arrTour.add(t[i]);
                    }

                    CustomAdapter customAdaper = new CustomAdapter(ShowAccountToursActivity.this,R.layout.row_listview_account_tours,arrTour);
                    lvTour.setAdapter(customAdaper);
                }
                else{
                    //TODO
                }
            }

            @Override
            public void onFailure(Call<ShowAccountToursReq> call, Throwable throwable) {
                //TODO
            }
        });
    }


}
