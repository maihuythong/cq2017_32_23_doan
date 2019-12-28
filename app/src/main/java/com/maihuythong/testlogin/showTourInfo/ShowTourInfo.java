package com.maihuythong.testlogin.showTourInfo;

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
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.firebase.messaging.FirebaseMessaging;
import com.maihuythong.testlogin.LoginActivity;
import com.maihuythong.testlogin.R;
import com.maihuythong.testlogin.ShowListUsers.User;
import com.maihuythong.testlogin.ShowListUsers.UserReq;
import com.maihuythong.testlogin.TourCoordinate.LocationService;
import com.maihuythong.testlogin.TourCoordinate.MapStartTour;
import com.maihuythong.testlogin.firebase.MessagingTour;
import com.maihuythong.testlogin.googlemapapi.StopPointGoogleMap;
import com.maihuythong.testlogin.rate_comment_review.Comment;
import com.maihuythong.testlogin.rate_comment_review.CommentAdapter;
import com.maihuythong.testlogin.rate_comment_review.GetPointOfTour;
import com.maihuythong.testlogin.rate_comment_review.SendCommentTour;
import com.maihuythong.testlogin.rate_comment_review.SendReviewTour;
import com.maihuythong.testlogin.showListStopPoints.showListStopPointsActivity;
import com.maihuythong.testlogin.signup.APIService;
import com.maihuythong.testlogin.signup.ApiUtils;
import com.maihuythong.testlogin.userInfo.UserInfoRes;
import com.taufiqrahman.reviewratings.BarLabels;
import com.taufiqrahman.reviewratings.RatingReviews;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShowTourInfo extends AppCompatActivity {
    public int a = 5;

    private SharedPreferences sf;
    private String token;
    private long tourId;
    private long userId;
    private TextView hostName;
    private TextView date;
    private TextView adult;
    private TextView child;
    private TextView price;

    private Button member;
    private Button review;
    private Button comment;
    private Button chat;
    private Button startTour;
    private Button reviewStopPoint;
    private LinearLayout linearLayoutComment;
    private LinearLayout linearLayoutReview;
    private ImageButton sendComment;
    private EditText commentContent;
    private RatingBar ratingBar;
    private EditText reviewContent;
    RatingReviews ratingReviews;
    private TextView averageStar;
    private RatingBar smallRating;
    private TextView totalRating;

    private ArrayList<Member> listMembers;
    private ArrayList<Comment> listComments;

    private Button testrc;

    /**
     * permissions request code
     */
    private final static int REQUEST_CODE_ASK_PERMISSIONS = 1;

    /**
     * Permissions that need to be explicitly requested from end user.
     */
    private static final String[] REQUIRED_SDK_PERMISSIONS = new String[] {
            Manifest.permission.ACCESS_FINE_LOCATION };


    public static final int REQUEST_LOCATION = 99;
    ListView listView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_tour_info);

        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        getWindow().setStatusBarColor(Color.TRANSPARENT);

        Intent intent = getIntent();
        tourId = intent.getLongExtra("id", 1);

        hostName = findViewById(R.id.tour_info_host_name);
        date = findViewById(R.id.tour_info_day);
        adult = findViewById(R.id.tour_info_adult);
        child = findViewById(R.id.tour_info_child);
        price = findViewById(R.id.tour_info_price);
        member = findViewById(R.id.tour_info_member);
        review = findViewById(R.id.tour_info_review);
        comment = findViewById(R.id.tour_info_comment);
        listView = findViewById(R.id.tour_info_list_view);
        linearLayoutComment = findViewById(R.id.tour_info_linear_layout_send_comment);
        sendComment = findViewById(R.id.send_comment_tour_info);
        commentContent = findViewById(R.id.tour_info_comment_content);
        linearLayoutReview = findViewById(R.id.tour_info_linear_layout_review);
        ratingBar = findViewById(R.id.ratingBar_tour_info);
        reviewContent = findViewById(R.id.review_content_tour_info);
        ratingReviews = findViewById(R.id.rating_reviews);
        averageStar = findViewById(R.id.average_star);
        smallRating = findViewById(R.id.small_rating_bar);
        totalRating = findViewById(R.id.total_rating);
        chat = findViewById(R.id.tour_chat);
        startTour = findViewById(R.id.start_tour);
        reviewStopPoint= findViewById(R.id.stop_points_acc_tour_button);

        token = LoginActivity.token;
        if(token == null){
            sf = getSharedPreferences("com.maihuythong.testlogin_preferences", MODE_PRIVATE);
            token = sf.getString("login_access_token", "");
        }
        //get comment of tour
        APIService service = ApiUtils.getAPIService();

        service.getUserInfo(token).enqueue(new Callback<UserInfoRes>() {
            @Override
            public void onResponse(Call<UserInfoRes> call, Response<UserInfoRes> response) {
                UserInfoRes UserInfo = response.body();
                userId = UserInfo.getID();
            }

            @Override
            public void onFailure(Call<UserInfoRes> call, Throwable t) {

            }
        });

        service.getTourInfo(token, tourId).enqueue(new Callback<GetTourInfo>() {

            @Override
            public void onResponse(Call<GetTourInfo> call, Response<GetTourInfo> response) {
                if(response.code() == 200) {
                    GetTourInfo tourInfo = response.body();

                    listComments = tourInfo.getComments();
                    listMembers = tourInfo.getMembers();

                    String hostNameTour = getHostName(tourInfo.getMembers());
                    String startDateString = new SimpleDateFormat("MM/dd/yyyy").format(new Date(tourInfo.getStartDate()));
                    String endDateString = new SimpleDateFormat("MM/dd/yyyy").format(new Date(tourInfo.getEndDate()));


                    date.setText(startDateString + " - " + endDateString);
                    hostName.setText(hostNameTour);
                    if (tourInfo.getAdults() != 0){
                        adult.setText(String.valueOf(tourInfo.getAdults()));
                    }else {
                        adult.setText("0");
                    }

                    if (tourInfo.getChilds() != 0){
                        child.setText(String.valueOf(tourInfo.getChilds()));
                    }else {
                        child.setText("0");
                    }

                    price.setText(tourInfo.getMinCost() + " - " + tourInfo.getMaxCost());

                    member.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ClickMember();
                        }
                    });

                    comment.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ClickComment();
                        }
                    });

                    review.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ClickReview();
                        }
                    });

                    reviewStopPoint.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(ShowTourInfo.this, showListStopPointsActivity.class);
                            intent.putExtra("isAccTour",true);
                            intent.putExtra("tourId",tourId);
                            startActivity(intent);
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<GetTourInfo> call, Throwable t) {

            }
        } );


        sendComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendCommentTour();
            }
        });

        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openChat();
            }
        });

        startTour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPermissions();

            }
        });
    }

    private void openChat() {
        Intent intent = new Intent(ShowTourInfo.this, MessagingTour.class);
        intent.putExtra("tourId", String.valueOf(tourId));
        intent.putExtra("userId", String.valueOf(userId));
        startActivity(intent);
    }

    private String getHostName(ArrayList<Member> members){
        for (Member m : members){
            if (m.isHost()){
                return m.getName();
            }
        }
        return "Unnamed";
    }

    private void ClickMember(){
        linearLayoutComment.setVisibility(View.GONE);
        linearLayoutReview.setVisibility(View.GONE);
        MemberAdapter adapter =
                new MemberAdapter(this, R.layout.custom_tour_member, listMembers);
        listView.setAdapter(adapter);

        listView.getLayoutParams().height = 220*listMembers.size();

    }

    private void ClickComment(){
        linearLayoutComment.setVisibility(View.VISIBLE);
        linearLayoutReview.setVisibility(View.GONE);
        Collections.reverse(listComments);
        CommentAdapter adapter =
                new CommentAdapter(this, R.layout.custom_comment, listComments);
        listView.setAdapter(adapter);

        listView.getLayoutParams().height = 180*listComments.size();
    }

    private void ClickReview(){
        linearLayoutComment.setVisibility(View.GONE);
        linearLayoutReview.setVisibility(View.VISIBLE);
        listView.setAdapter(null);

        setRatingReviews();

        ImageButton sendReview = findViewById(R.id.send_review_tour_info);
        sendReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendPreviewTour();
            }
        });
    }

    private void SendCommentTour(){

        hideInputKeyboard();
        APIService mAPIService = ApiUtils.getAPIService();
        String comment = commentContent.getText().toString();
        if (comment == null || comment == ""){
            Toast.makeText(getApplicationContext(),"Please type your comment before send!", Toast.LENGTH_SHORT).show();
            return;
        }
        mAPIService.sendCommentTour(token,(int)tourId,69, comment).enqueue(new Callback<SendCommentTour>() {
            @Override
            public void onResponse(Call<SendCommentTour> call, Response<SendCommentTour> response) {
                Comment comment1 = new Comment(69,"You", commentContent.getText().toString(),null,System.currentTimeMillis());
                listComments.add(0,comment1);
                listView.getLayoutParams().height = 180*listComments.size();
                //listViewComment.setEnabled(false);
                CommentAdapter adapterComment =
                        new CommentAdapter(ShowTourInfo.this, R.layout.custom_comment, listComments);
                listView.setAdapter(adapterComment);
                commentContent.setText("");
                Toast.makeText(getApplicationContext(),"Sent your comment", Toast.LENGTH_SHORT).show();
                finish();
                overridePendingTransition(0, 0);
                startActivity(getIntent().setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                overridePendingTransition(0, 0);
            }

            @Override
            public void onFailure(Call<SendCommentTour> call, Throwable t) {

            }
        });
    }

    private void SendPreviewTour(){
        hideInputKeyboard();
        APIService mAPIService = ApiUtils.getAPIService();
        int numStar = (int)ratingBar.getRating();
        if(numStar == 0){

            Toast.makeText(getApplicationContext(),"Please choose your star rating!",Toast.LENGTH_SHORT).show();
            return;
        }
        mAPIService.sendReview(token, tourId, numStar, reviewContent.getText().toString()).enqueue(new Callback<SendReviewTour>() {
            @Override
            public void onResponse(Call<SendReviewTour> call, Response<SendReviewTour> response) {
                reviewContent.setText("");
                Toast.makeText(getApplicationContext(),"Sent your review", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<SendReviewTour> call, Throwable t) {

            }
        });
    }

    private void setRatingReviews(){
        final int colors[] = new int[]{
                Color.parseColor("#0e9d58"),
                Color.parseColor("#bfd047"),
                Color.parseColor("#ffc105"),
                Color.parseColor("#ef7e14"),
                Color.parseColor("#d36259")};

        // add data from api


        APIService mAPIService = ApiUtils.getAPIService();
        mAPIService.getPointOfTour(token, tourId).enqueue(new Callback<GetPointOfTour>() {
            @Override
            public void onResponse(Call<GetPointOfTour> call, Response<GetPointOfTour> response) {
                if (response.code() == 200){
                    //GetPointOfTour res[] = response.body().getServicedId();
                    GetPointOfTour getPointOfTour = response.body();

                    GetPointOfTour.Point point[] = getPointOfTour.getPointStats();
                    final int raters[] = {point[4].getTotal(), point[3].getTotal(), point[2].getTotal(),
                            point[1].getTotal(), point[0].getTotal()};



                    int count = 0;
                    for (GetPointOfTour.Point i : point){
                        count += i.getTotal();
                    }

                    double total = 0.0;
                    for (GetPointOfTour.Point p : point){
                        if (p.getTotal()!=0){
                            total += p.getPoint()*p.getTotal();
                        }
                    }
                    total = total/count;
                    smallRating.setRating((float)total);

                    averageStar.setText(String.valueOf(total));

                    totalRating.setText(String.valueOf(count));

                    ratingReviews.createRatingBars(100, BarLabels.STYPE1, colors, raters);
                }
            }

            @Override
            public void onFailure(Call<GetPointOfTour> call, Throwable t) {

            }
        });


    }

    void hideInputKeyboard(){
        try {
            InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {
            // TODO: handle exception

        }
    }

    // check location permission
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
                                        Intent map = new Intent(ShowTourInfo.this, MapStartTour.class);
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
}
