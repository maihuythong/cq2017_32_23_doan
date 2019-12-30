package com.maihuythong.testlogin.showTourInfomation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.messaging.FirebaseMessaging;
import com.maihuythong.testlogin.LoginActivity;
import com.maihuythong.testlogin.R;
import com.maihuythong.testlogin.ShowListUsers.ListUsersActivity;
import com.maihuythong.testlogin.TourCoordinate.LocationService;
import com.maihuythong.testlogin.TourCoordinate.MapStartTour;
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
import com.maihuythong.testlogin.updateTour.UpdateTour;
import com.maihuythong.testlogin.updateTour.UpdateTourReq;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.maihuythong.testlogin.showTourInfo.ShowTourInfo.REQUEST_LOCATION;

public class ShowTourInformation extends AppCompatActivity {
    private FloatingActionButton fab_main, fab1_invite, fab2_modify, fab3_delete, fab4_start;
    private Animation fab_open, fab_close, fab_clock, fab_anticlock;
    Boolean isOpen = false;

    private SharedPreferences sf;
    private String token;
    private long tourId;
    private long userId;

    private TourOverview tourOverview = null;
    private GetTourInfo tourInfo;
    private String startDateString;
    private String endDateString;

    private ArrayList<Member> listMembers;
    private ArrayList<Comment> listComments;

    private static final String[] REQUIRED_SDK_PERMISSIONS = new String[] {
            Manifest.permission.ACCESS_FINE_LOCATION };
    private final static int REQUEST_CODE_ASK_PERMISSIONS = 1;


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
        fab4_start = findViewById(R.id.fab4);
        fab_close = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_close);
        fab_open = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        fab_clock = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_rotate_clock);
        fab_anticlock = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_rotate_anticlock);

        fab_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isOpen) {
                    fab3_delete.startAnimation(fab_close);
                    fab4_start.startAnimation(fab_close);
                    fab2_modify.startAnimation(fab_close);
                    fab1_invite.startAnimation(fab_close);
                    fab_main.startAnimation(fab_anticlock);
                    fab3_delete.setClickable(false);
                    fab4_start.setClickable(false);
                    fab2_modify.setClickable(false);
                    fab1_invite.setClickable(false);
                    isOpen = false;
                } else {
                    fab3_delete.startAnimation(fab_open);
                    fab4_start.startAnimation(fab_open);
                    fab2_modify.startAnimation(fab_open);
                    fab1_invite.startAnimation(fab_open);
                    fab_main.startAnimation(fab_clock);
                    fab3_delete.setClickable(true);
                    fab4_start.setClickable(true);
                    fab2_modify.setClickable(true);
                    fab1_invite.setClickable(true);
                    isOpen = true;
                }

            }
        });

        fab3_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                deleteTour();

            }
        });

        fab4_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPermissions();
            }
        });

        fab2_modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UpdateTour();
            }
        });

        fab1_invite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ShowTourInformation.this, ListUsersActivity.class);
                intent.putExtra("tourId",tourId);
                intent.putExtra("userId", userId);
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
                    tourInfo = response.body();

                    listComments = tourInfo.getComments();
                    listMembers = tourInfo.getMembers();

                    String hostNameTour = getHostName(tourInfo.getMembers());
                    String tourNameString = tourInfo.getName();
                    startDateString = new SimpleDateFormat("MM/dd/yyyy").format(new Date(tourInfo.getStartDate()));
                    endDateString = new SimpleDateFormat("MM/dd/yyyy").format(new Date(tourInfo.getEndDate()));

                    tourOverview = new TourOverview();
                    tourOverview.setId(tourInfo.getId());
                    tourOverview.setMaxCost(tourInfo.getMaxCost());
                    tourOverview.setMinCost(tourInfo.getMinCost());
                    tourOverview.setStatus(tourInfo.getStatus());
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
                            tourOverview, listComments, token, tourId, listMembers);

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

    private  void UpdateTour(){
        Intent intent = new Intent(ShowTourInformation.this, UpdateTour.class);
        intent.putExtra("id", tourOverview.getId());
        intent.putExtra("status", tourOverview.getStatus());
        intent.putExtra("name", tourOverview.getName());
        intent.putExtra("minCost", tourOverview.getMinCost());
        intent.putExtra("maxCost", tourOverview.getMaxCost());
        intent.putExtra("startDate", tourInfo.getStartDate() + "");
        intent.putExtra("endDate", tourInfo.getEndDate() + "");
        intent.putExtra("adult", tourOverview.getAdult());
        intent.putExtra("child", tourOverview.getChild());
        intent.putExtra("isPrivate", tourInfo.isPrivate());
        intent.putExtra("avatar", tourInfo.getAvatar());
        startActivity(intent);
    }

    protected void checkPermissions() {
        final List<String> missingPermissions = new ArrayList<String>();
        // check all required dynamic permissions
        for (final String permission : REQUIRED_SDK_PERMISSIONS) {
            final int result = ContextCompat.checkSelfPermission(this, permission);
            if (result != PackageManager.PERMISSION_GRANTED) {
                missingPermissions.add(permission);
            }
        }
        if (!missingPermissions.isEmpty()) {
            // request all missing permissions
            final String[] permissions = missingPermissions
                    .toArray(new String[missingPermissions.size()]);
            ActivityCompat.requestPermissions(this, permissions, REQUEST_CODE_ASK_PERMISSIONS);
        } else {
            final int[] grantResults = new int[REQUIRED_SDK_PERMISSIONS.length];
            Arrays.fill(grantResults, PackageManager.PERMISSION_GRANTED);
            onRequestPermissionsResult(REQUEST_CODE_ASK_PERMISSIONS, REQUIRED_SDK_PERMISSIONS,
                    grantResults);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                for (int index = permissions.length - 1; index >= 0; --index) {
                    if (grantResults[index] != PackageManager.PERMISSION_GRANTED) {
                        // exit the app if one permission is not granted
                        Toast.makeText(this, "Required permission '" + permissions[index]
                                + "' not granted, exiting", Toast.LENGTH_LONG).show();
                        finish();
                        return;
                    }
                }
                // all permissions were granted
                LocationManager lm = (LocationManager)
                        getSystemService(Context. LOCATION_SERVICE ) ;
                boolean gps_enabled = false;
                boolean network_enabled = false;
                try {
                    gps_enabled = lm.isProviderEnabled(LocationManager. GPS_PROVIDER ) ;
                } catch (Exception e) {
                    e.printStackTrace() ;
                }
                try {
                    network_enabled = lm.isProviderEnabled(LocationManager. NETWORK_PROVIDER ) ;
                } catch (Exception e) {
                    e.printStackTrace() ;
                }
                if (!gps_enabled && !network_enabled) {
                    new AlertDialog.Builder(this )
                            .setMessage( "GPS Enable" )
                            .setPositiveButton( "Settings" , new
                                    DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick (DialogInterface paramDialogInterface , int paramInt) {

                                            startActivityForResult(new Intent(Settings. ACTION_LOCATION_SOURCE_SETTINGS ), REQUEST_LOCATION);
                                        }
                                    })
                            .setNegativeButton( "Cancel" , null )
                            .show() ;
                }else {
                    LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                    if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
                        Toast.makeText(this,"All permission granted",Toast.LENGTH_SHORT).show();
                        String id = String.valueOf(tourId);
                        String topic = "/topics/tour-id-" + id;

                        FirebaseMessaging.getInstance().subscribeToTopic(topic)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        String msg = "Success";
                                        if (!task.isSuccessful()) {
                                            msg = "Fail";
                                        }
                                        Log.d("MESSAGING", msg);
                                        Intent intent1 = new Intent(getApplicationContext(), LocationService.class);
                                        intent1.putExtra("tourId", tourId);
                                        startService(intent1);
                                        Intent map = new Intent(ShowTourInformation.this, MapStartTour.class);
                                        map.putExtra("tourId", tourId);
                                        map.putExtra("userId", userId);
                                        startActivity(map);
                                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }else {
                        Toast.makeText(this,"Fail permission location",Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case REQUEST_LOCATION:
                LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){

                    Toast.makeText(this,"All permission granted",Toast.LENGTH_SHORT).show();

                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    String id = String.valueOf(tourId);
                    String topic = "/topics/tour-id-" + id;

                    FirebaseMessaging.getInstance().subscribeToTopic(topic)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    String msg = "Success";
                                    if (!task.isSuccessful()) {
                                        msg = "Fail";
                                    }
                                    Log.d("MESSAGING", msg);
                                    Intent intent1 = new Intent(getApplicationContext(), LocationService.class);
                                    intent1.putExtra("tourId", tourId);
                                    startService(intent1);
                                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                                }
                            });
                }else {
                    Toast.makeText(this,"Fail permission location",Toast.LENGTH_SHORT).show();
                }
        }
    }


    private void deleteTour(){
        APIService mApiService = ApiUtils.getAPIService();

        mApiService.DeleteTour(token ,tourId,-1).enqueue(new Callback<UpdateTourReq>() {
            @Override
            public void onResponse(Call<UpdateTourReq> call, Response<UpdateTourReq> response) {
                if(response.code()==200)
                {
                    Toast.makeText(getApplicationContext(),"Delete tour success!", Toast.LENGTH_LONG).show();
                    setResult(RESULT_OK, null);
                    finish();
                }else {
                    Toast.makeText(getApplicationContext(),"Delete failed!", Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<UpdateTourReq> call, Throwable t) {
            }
        });

    }
}

