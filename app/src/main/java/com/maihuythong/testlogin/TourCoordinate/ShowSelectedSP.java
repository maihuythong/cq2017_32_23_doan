package com.maihuythong.testlogin.TourCoordinate;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.maihuythong.testlogin.R;
import com.maihuythong.testlogin.manager.MyApplication;
import com.maihuythong.testlogin.showTourInfo.StopPoint;
import com.maihuythong.testlogin.signup.APIService;
import com.maihuythong.testlogin.signup.ApiUtils;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShowSelectedSP extends Activity {
    private RecyclerView recyclerView;
    private SharedPreferences sf;
    private ArrayList<StopPoint> arrayList = new ArrayList<>();
    long mTourId;

//    private static final String LOG_TAG = "AudioRecordTest";
//    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
//    private static String fileName = null;
//
//    private MediaRecorder recorder = null;
//
//    private MediaPlayer player = null;


    // Requesting permission to RECORD_AUDIO
    private boolean permissionToRecordAccepted = false;
    private String [] permissions = {Manifest.permission.RECORD_AUDIO};


//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        switch (requestCode){
//            case REQUEST_RECORD_AUDIO_PERMISSION:
//                permissionToRecordAccepted  = grantResults[0] == PackageManager.PERMISSION_GRANTED;
//                break;
//        }
//        if (!permissionToRecordAccepted ) finish();
//
//    }

    @Override
    protected  void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_selected_stop_points);
        MyApplication app = (MyApplication)getApplication();

        arrayList = getIntent().getParcelableArrayListExtra("selected_stop_points");
        mTourId = getIntent().getLongExtra("tourId", 0);

        SelectedSPAdapter selectedSPAdapter = new SelectedSPAdapter(arrayList, mTourId);
        recyclerView = findViewById(R.id.recycle_view_chat);
        recyclerView.setAdapter(selectedSPAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.scrollToPositionWithOffset(arrayList.size() -1, 0);
        recyclerView.setLayoutManager(linearLayoutManager);


        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;

        getWindow().setLayout((int)(width*0.9), (int)(height*0.64));
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.y = -140;
        getWindow().setAttributes(params);
    }
}
