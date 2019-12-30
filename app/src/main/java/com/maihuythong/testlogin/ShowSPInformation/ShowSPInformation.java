package com.maihuythong.testlogin.ShowSPInformation;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.maihuythong.testlogin.LoginActivity;
import com.maihuythong.testlogin.R;
import com.maihuythong.testlogin.ShowListUsers.ListUsersActivity;
import com.maihuythong.testlogin.ShowListUsers.SendInvationRes;
import com.maihuythong.testlogin.rate_comment_review.Comment;
import com.maihuythong.testlogin.showTourInfo.GetTourInfo;
import com.maihuythong.testlogin.showTourInfo.Member;
import com.maihuythong.testlogin.showTourInfo.StopPoint;
import com.maihuythong.testlogin.showTourInfomation.SectionsPagerAdapter;
import com.maihuythong.testlogin.showTourInfomation.TourOverview;
import com.maihuythong.testlogin.signup.APIService;
import com.maihuythong.testlogin.signup.ApiUtils;
import com.maihuythong.testlogin.stopPointInfo.Feedback;
import com.maihuythong.testlogin.stopPointInfo.UpdateStopPointActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.maihuythong.testlogin.stopPointInfo.StopPointDetailActivity.getDate;

public class ShowSPInformation extends AppCompatActivity {
    private FloatingActionButton fab_main, fab1_invite, fab2_modify, fab3_delete;
    private Animation fab_open, fab_close, fab_clock, fab_anticlock;
    Boolean isOpen = false;

    private SPOverview spOverview = new SPOverview();
    private long mTourId;
    private Feedback[] arrFeedback;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.menu_scrolling, menu);
        inflater.inflate(R.menu.menu_placeholder, menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_sp_info_offical);

        fab_main = findViewById(R.id.fab);
        fab1_invite = findViewById(R.id.fab1);
        fab2_modify = findViewById(R.id.fab2);
        fab3_delete = findViewById(R.id.fab3);
        fab_close = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_close);
        fab_open = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        fab_clock = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_rotate_clock);
        fab_anticlock = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_rotate_anticlock);

        fab_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isOpen) {
                    fab3_delete.startAnimation(fab_close);
                    fab2_modify.startAnimation(fab_close);
                    fab1_invite.startAnimation(fab_close);
                    fab_main.startAnimation(fab_anticlock);
                    fab3_delete.setClickable(false);
                    fab2_modify.setClickable(false);
                    fab1_invite.setClickable(false);
                    isOpen = false;
                } else {
                    fab3_delete.startAnimation(fab_open);
                    fab2_modify.startAnimation(fab_open);
                    fab1_invite.startAnimation(fab_open);
                    fab_main.startAnimation(fab_clock);
                    fab3_delete.setClickable(true);
                    fab2_modify.setClickable(true);
                    fab1_invite.setClickable(true);
                    isOpen = true;
                }

            }
        });

        fab3_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DeleteStopPoint();

            }
        });

        fab2_modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UpdateStopPoint();
            }
        });

        fab1_invite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(ShowSPInformation.this, ListUsersActivity.class);
//                intent.putExtra("tourId",tourId);
//                startActivity(intent);
            }
        });


        //---------------------------Communicating time---------------------------------------
//        Intent intent = getIntent();
//        tourId = intent.getLongExtra("id", 1);
//
//        token = LoginActivity.token;
//        if(token == null){
//            sf = getSharedPreferences("com.maihuythong.testlogin_preferences", MODE_PRIVATE);
//            token = sf.getString("login_access_token", "");
//        }

        Intent intent = getIntent();
        spOverview.setId(intent.getIntExtra("id",0));
        mTourId = intent.getLongExtra("tourId", 0);

        if(Objects.isNull(intent.getStringExtra("address"))){
            spOverview.setAddress("No address");
        }
        else{
            spOverview.setAddress(intent.getStringExtra("address"));
        }

        spOverview.setServiceId(intent.getIntExtra("serviceId",0));
        spOverview.setProvinceId(intent.getIntExtra("provinceId",0));

        if(Objects.isNull(intent.getStringExtra("name"))){
            spOverview.setName(intent.getStringExtra("Unnamed"));
        }
        else{
            spOverview.setName(intent.getStringExtra("name"));
        }

        spOverview.setLat(intent.getDoubleExtra("lat",0));
        spOverview.setLong(intent.getDoubleExtra("long",0));
        spOverview.setArrivalAt(intent.getLongExtra("arrivalAt",0));
        spOverview.setLeaveAt(intent.getLongExtra("leaveAt",0));
        spOverview.setMinCost(intent.getLongExtra("minCost",0));
        spOverview.setMaxCost(intent.getLongExtra("maxCost",0));
        spOverview.setServiceTypeId(intent.getIntExtra("serviceTypeId",0));

        if(Objects.isNull(intent.getStringExtra("avatar"))){
            spOverview.setAvatar("");
        }
        else{
            spOverview.setAvatar(intent.getStringExtra("avatar"));
        }

        SectionsPagerAdapterSP sectionsPagerAdapterSP = new SectionsPagerAdapterSP(
                ShowSPInformation.this, getSupportFragmentManager(),
                spOverview);

        //Setting up tabs
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapterSP);

        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(spOverview.getName());

        //-------------------------Modify expanded title and fab------------------------------------------
        CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.toolbar_layout);
        collapsingToolbarLayout.setExpandedTitleMarginBottom(54);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.collapsingToolbarLayoutTitleColor);
        collapsingToolbarLayout.setCollapsedTitleGravity(1);

        NestedScrollView nestedScrollView = findViewById(R.id.content_scrolling);
        nestedScrollView.setFillViewport(true);
    }

    private String getHostName(ArrayList<Member> members){
        for (Member m : members){
            if (m.isHost()){
                return m.getName();
            }
        }
        return "Unnamed";
    }


    private void UpdateStopPoint(){
        Intent intent = new Intent(ShowSPInformation.this, UpdateStopPointActivity.class);
        intent.putExtra("address",spOverview.getAddress());
        intent.putExtra("provinceId",spOverview.getProvinceId());
        intent.putExtra("name",spOverview.getName());
        intent.putExtra("arrivalAt",spOverview.getArrivalAt());
        intent.putExtra("lat",spOverview.getLat());
        intent.putExtra("long",spOverview.getLong());
        intent.putExtra("leaveAt",spOverview.getLeaveAt());
        intent.putExtra("minCost",spOverview.getMinCost());
        intent.putExtra("maxCost",spOverview.getMaxCost());
        intent.putExtra("id",spOverview.getId());
        intent.putExtra("serviceTypeId",spOverview.getServiceTypeId());
        intent.putExtra("tourId", mTourId);
        startActivityForResult(intent, 7777);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 7777 && resultCode == RESULT_OK){
            setResult(RESULT_OK);
            finish();
        }
    }

    private void DeleteStopPoint(){
        String token = GetTokenLoginAccess();

        APIService mAPIService = ApiUtils.getAPIService();
        mAPIService.DeleteStopPoint(token,String.valueOf(spOverview.getId())).enqueue(new Callback<SendInvationRes>() {
            @Override
            public void onResponse(Call<SendInvationRes> call, Response<SendInvationRes> response) {

                if(response.code()==200){

                    Toast.makeText(getApplicationContext(),"Delete suscess", Toast.LENGTH_LONG).show();
                    setResult(RESULT_OK);
                    finish();
                }

                if(response.code()==400||response.code()==404||response.code()==403 || response.code()==500) {
                    String message = "Send failed!";
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        message = jObjError.getString("message");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(getApplicationContext(),"Error: "+ message, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<SendInvationRes> call, Throwable t) {

            }
        });


    }


    private String GetTokenLoginAccess(){
        SharedPreferences sf;
        String token;
        token = LoginActivity.token;
        if(token== null){
            sf = getSharedPreferences("com.maihuythong.testlogin_preferences", MODE_PRIVATE);
            token = sf.getString("login_access_token", "");
        }
        return token;
    }
}

