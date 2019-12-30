package com.maihuythong.testlogin.ShowSystemTourInfo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

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
import com.maihuythong.testlogin.ShowSPInformation.SectionsPagerAdapterSP;
import com.maihuythong.testlogin.rate_comment_review.Comment;
import com.maihuythong.testlogin.showTourInfo.GetTourInfo;
import com.maihuythong.testlogin.showTourInfo.Member;
import com.maihuythong.testlogin.showTourInfomation.SectionsPagerAdapter;
import com.maihuythong.testlogin.showTourInfomation.TourOverview;
import com.maihuythong.testlogin.signup.APIService;
import com.maihuythong.testlogin.signup.ApiUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShowSystemTourInfo extends AppCompatActivity {
//    private FloatingActionButton fab_main, fab1_modify, fab2_delete;
//    private Animation fab_open, fab_close, fab_clock, fab_anticlock;
//    Boolean isOpen = false;

    private FloatingActionButton fab_main, fab1_invite, fab2_modify, fab3_delete;
    private Animation fab_open, fab_close, fab_clock, fab_anticlock;
    Boolean isOpen = false;

    private SharedPreferences sf;
    private String token;
    private long tourId;
    private long tourStatus;
    private String tourName;
    private long adults;
    private long child;
    private String minCost;
    private String maxCost;
    private long startDate;
    private long endDate;

    private TourOverview tourOverview = null;

    private ArrayList<Member> listMembers;
    private ArrayList<Comment> listComments;

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
                Intent intent = new Intent(ShowSystemTourInfo.this, ListUsersActivity.class);
                intent.putExtra("tourId",tourId);
                startActivity(intent);
            }
        });


        //---------------------------Communicating time---------------------------------------
        Intent intent = getIntent();
        tourId = intent.getLongExtra("id", 1);
        tourStatus = intent.getLongExtra("status", 0);
        tourName = intent.getStringExtra("name");
        adults = intent.getLongExtra("adult",0);
        child = intent.getLongExtra("child",0);
        minCost = intent.getStringExtra("minCost");
        maxCost = intent.getStringExtra("maxCost");

        SectionsPagerAdapterSTI sectionsPagerAdapterSTI = new SectionsPagerAdapterSTI(getApplicationContext(), getSupportFragmentManager(),
                tourId, tourName, adults, child, minCost, maxCost, 0, 0, tourStatus);

        //-------------------------------Setting up tabs----------------------------
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapterSTI);

        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(tourName);

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
}

