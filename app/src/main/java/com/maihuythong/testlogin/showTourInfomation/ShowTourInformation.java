package com.maihuythong.testlogin.showTourInfomation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.maihuythong.testlogin.LoginActivity;
import com.maihuythong.testlogin.R;
import com.maihuythong.testlogin.ShowListUsers.ListUsersActivity;
import com.maihuythong.testlogin.rate_comment_review.Comment;
import com.maihuythong.testlogin.rate_comment_review.CommentAdapter;
import com.maihuythong.testlogin.rate_comment_review.SendCommentTour;
import com.maihuythong.testlogin.showAccountTours.ShowAccountToursActivity;
import com.maihuythong.testlogin.showTourInfo.GetTourInfo;
import com.maihuythong.testlogin.showTourInfo.Member;
import com.maihuythong.testlogin.showTourInfo.ShowTourInfo;
import com.maihuythong.testlogin.showlist.Tour;
import com.maihuythong.testlogin.signup.APIService;
import com.maihuythong.testlogin.signup.ApiUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShowTourInformation extends AppCompatActivity {
//    private FloatingActionButton fab_main, fab1_modify, fab2_delete;
//    private Animation fab_open, fab_close, fab_clock, fab_anticlock;
//    Boolean isOpen = false;

    private FloatingActionButton fab_main, fab1_invite, fab2_modify, fab3_delete;
    private Animation fab_open, fab_close, fab_clock, fab_anticlock;
    Boolean isOpen = false;

    private SharedPreferences sf;
    private String token;
    private long tourId;

    private TourOverview tourOverview = null;

    private ArrayList<Member> listMembers;
    private ArrayList<Comment> listComments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_tour_info_offical);

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

                //TODO:

            }
        });

        fab2_modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //TODO:

            }
        });

        fab1_invite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ShowTourInformation.this, ListUsersActivity.class);
                intent.putExtra("tourId",tourId);
                startActivity(intent);
            }
        });


        //---------------------------Communicating time---------------------------------------
        Intent intent = getIntent();
        tourId = intent.getLongExtra("id", 1);

        token = LoginActivity.token;
        if(token == null){
            sf = getSharedPreferences("com.maihuythong.testlogin_preferences", MODE_PRIVATE);
            token = sf.getString("login_access_token", "");
        }

        APIService service = ApiUtils.getAPIService();
        service.getTourInfo(token, tourId).enqueue(new Callback<GetTourInfo>() {

            @Override
            public void onResponse(Call<GetTourInfo> call, Response<GetTourInfo> response) {
                if(response.code() == 200) {
                    GetTourInfo tourInfo = response.body();

                    listComments = tourInfo.getComments();
                    listMembers = tourInfo.getMembers();

                    String hostNameTour = getHostName(tourInfo.getMembers());
                    String tourNameString = tourInfo.getName();
                    String startDateString = new SimpleDateFormat("MM/dd/yyyy").format(new Date(tourInfo.getStartDate()));
                    String endDateString = new SimpleDateFormat("MM/dd/yyyy").format(new Date(tourInfo.getEndDate()));

                    tourOverview = new TourOverview();
                    tourOverview.setHostName(hostNameTour);
                    tourOverview.setPrice(tourInfo.getMinCost() + " - " + tourInfo.getMaxCost());
                    tourOverview.setAdult(String.valueOf(tourInfo.getAdults()));
                    tourOverview.setChild(String.valueOf(tourInfo.getChilds()));
                    tourOverview.setDate(startDateString + " - " + endDateString);
                    if(tourNameString.equals("")){
                        tourNameString = "Unnamed tour";
                    }
                    tourOverview.setName(tourNameString);

                    SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(ShowTourInformation.this, getSupportFragmentManager(),
                            tourOverview, listComments, token, tourId);

                    //Setting up tabs
                    ViewPager viewPager = findViewById(R.id.view_pager);
                    viewPager.setAdapter(sectionsPagerAdapter);

                    TabLayout tabs = findViewById(R.id.tabs);
                    tabs.setupWithViewPager(viewPager);

                    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
                    setSupportActionBar(toolbar);
                    getSupportActionBar().setTitle(tourOverview.getName());
                }
            }

            @Override
            public void onFailure(Call<GetTourInfo> call, Throwable t) {
                //TODO:
            }
        });

        //-------------------------Modify expanded title and fab------------------------------------------
        CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.toolbar_layout);
        collapsingToolbarLayout.setExpandedTitleMarginBottom(54);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.collapsingToolbarLayoutTitleColor);

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
}

