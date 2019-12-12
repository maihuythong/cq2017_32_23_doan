package com.maihuythong.testlogin.showlist;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.maihuythong.testlogin.CreateTourActivity;
import com.maihuythong.testlogin.LoginActivity;
import com.maihuythong.testlogin.R;
import com.maihuythong.testlogin.userInfo.UserInfoActivity;
import com.maihuythong.testlogin.invitationTour.InvitationActivity;
import com.maihuythong.testlogin.rate_comment_review.RateCommentTour;
import com.maihuythong.testlogin.showAccountTours.ShowAccountToursActivity;
import com.maihuythong.testlogin.signup.APIService;
import com.maihuythong.testlogin.signup.ApiUtils;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import android.provider.Settings.Secure;


public class ShowListActivity extends AppCompatActivity {
    private ListView lvTour;
    private Tour[] t;
    private SharedPreferences sf;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_list);

        String android_id = Secure.getString(getApplicationContext().getContentResolver(),
                Secure.ANDROID_ID);
        Log.d("deviceId", android_id);

        try {
            PackageInfo pInfo = getApplicationContext().getPackageManager().getPackageInfo(getPackageName(), 0);
            String version = pInfo.versionName;
            int verCode = pInfo.versionCode;
            Log.d("appVersion", verCode + "");
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w("fcmTokenFailed", "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        String token = task.getResult().getToken();

                        // Log and toast
                        Log.d("fcmTokenSuccess", token);
                    }
                });

        //init fab button
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddTour();
            }
        });

        //init show account tours button
        Button showAccountTours = findViewById(R.id.show_account_tours);
        showAccountTours.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShowListActivity.this, ShowAccountToursActivity.class);
                startActivity(intent);
            }
        });

        //init show invitation of tours button
        Button showInvitation = findViewById(R.id.show_invitation);
        showInvitation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShowListActivity.this, InvitationActivity.class);
                startActivity(intent);
            }
        });

        //init show user info button
        Button showUserInfo = findViewById(R.id.show_userInfo);
        showUserInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShowListActivity.this, UserInfoActivity.class);
                startActivity(intent);
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

        mAPIService.getList(s,50, 1).enqueue(new Callback<ShowListReq>() {
            @Override
            public void onResponse(Call<ShowListReq> call, Response<ShowListReq> response) {
                if(response.code() == 200){
                    t = response.body().getTours();
                    Log.d("mmm", "" + response.body().getTotal());
                    lvTour = (ListView) findViewById(R.id.lv_tour);
                    final ArrayList<Tour> arrTour = new ArrayList<>();

                    for(int i = 0; i<t.length; i++){
                        arrTour.add(t[i]);
                    }

                    CustomAdapter customAdaper = new CustomAdapter(ShowListActivity.this,R.layout.row_listview,arrTour);
                    lvTour.setAdapter(customAdaper);

                    lvTour.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            openRateCommentTour(arrTour, position);
                        }
                    });
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

    private void AddTour() {
        startActivity(new Intent(this, CreateTourActivity.class));
    }



    private  void openRateCommentTour(ArrayList<Tour> arrTour,int position){
        Tour tt;
        tt = arrTour.get(position);
        Intent intent = new Intent(ShowListActivity.this, RateCommentTour.class);
        intent.putExtra("pos", position);
        intent.putExtra("id", tt.getID());
        intent.putExtra("status", tt.getStatus());
        intent.putExtra("name", tt.getName());
        intent.putExtra("minCost", tt.getMinCost());
        intent.putExtra("maxCost", tt.getMaxCost());
        intent.putExtra("startDate", tt.getStartDate());
        intent.putExtra("endDate", tt.getEndDate());
        intent.putExtra("adult", tt.getAdults());
        intent.putExtra("child", tt.getChilds());
        intent.putExtra("isPrivate", tt.getIsPrivate());
        intent.putExtra("avatar", tt.getAvatar());
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
    }

}
