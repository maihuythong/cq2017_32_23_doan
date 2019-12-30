package com.maihuythong.testlogin.MainTabbedLayout;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.messaging.FirebaseMessaging;
import com.maihuythong.testlogin.LoginActivity;
import com.maihuythong.testlogin.R;
import com.maihuythong.testlogin.ShowListUsers.ListUsersActivity;
import com.maihuythong.testlogin.TourCoordinate.LocationService;
import com.maihuythong.testlogin.TourCoordinate.MapStartTour;
import com.maihuythong.testlogin.rate_comment_review.Comment;
import com.maihuythong.testlogin.showTourInfo.GetTourInfo;
import com.maihuythong.testlogin.showTourInfo.Member;
import com.maihuythong.testlogin.showTourInfomation.SectionsPagerAdapter;
import com.maihuythong.testlogin.showTourInfomation.ShowTourInformation;
import com.maihuythong.testlogin.showTourInfomation.TourOverview;
import com.maihuythong.testlogin.signup.APIService;
import com.maihuythong.testlogin.signup.ApiUtils;
import com.maihuythong.testlogin.updateTour.UpdateTour;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.maihuythong.testlogin.showTourInfo.ShowTourInfo.REQUEST_LOCATION;

public class MainTabbedActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_tabbed);

        //Setting up tabs
        SectionsPagerAdapterMain sectionsPagerAdapterMain = new SectionsPagerAdapterMain(this.getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapterMain);

        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

        tabs.getTabAt(0).setIcon(R.drawable.my_tour);
        tabs.getTabAt(1).setIcon(R.drawable.tour_sys);
        tabs.getTabAt(2).setIcon(R.drawable.stop_point_system);
        tabs.getTabAt(3).setIcon(R.drawable.invitations);
        tabs.getTabAt(4).setIcon(R.drawable.me);
    }

    @Override
    public void onBackPressed() {
    }


}

