package com.maihuythong.testlogin.stopPointInfo;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.maihuythong.testlogin.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Objects;

public class StopPointDetailActivity extends AppCompatActivity {

    private int id;
    private int serviceId;
    private String address;
    private int provinceId;
    private String name;
    private double Lat;
    private double Long;
    private long arrivalAt;
    private long leaveAt;
    private long minCost;
    private long maxCost;
    private int serviceTypeId;
    private String avatar;


    private TextView titleView;
    private TextView idView;
    private TextView serviceIdView;
    private TextView addressView;
    private TextView provinceIdView;
    private TextView dateView;
    private TextView priceView;
    private TextView serviceTypeView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stop_point_detail);

        titleView = findViewById(R.id.title_stop_point_info);
        idView = findViewById(R.id.stop_point_id);
        serviceIdView = findViewById(R.id.stop_point_serviceId);
        addressView = findViewById(R.id.stop_point_address);
        dateView = findViewById(R.id.stop_point_day);
        serviceTypeView = findViewById(R.id.stop_point_serviceType);
        provinceIdView = findViewById(R.id.stop_point_province);
        priceView = findViewById(R.id.stop_point_price);



        setData();





    }


    @SuppressLint("SetTextI18n")
    private void setData(){
        Intent intent = getIntent();
        id = intent.getIntExtra("id",0);
        serviceId = intent.getIntExtra("serviceId",0);
        address = intent.getStringExtra("address");
        if(Objects.isNull(address)) address = "";
        provinceId = intent.getIntExtra("provinceID",0);
        name = intent.getStringExtra("name");
        if(Objects.isNull(name)) name = "";
        Lat = intent.getDoubleExtra("lat",0);
        Long = intent.getDoubleExtra("long",0);
        arrivalAt = intent.getLongExtra("arrivalAt",0);
        leaveAt = intent.getLongExtra("leaveAt",0);
        minCost = intent.getLongExtra("minCost",0);
        maxCost = intent.getLongExtra("maxCost",0);
        serviceTypeId = intent.getIntExtra("serviceTypeId",0);
        avatar = intent.getStringExtra("avatar");
        if(Objects.isNull(avatar)) avatar = "";


        String dateFormat= "dd/MM/yyyy";
        String dateArrival = getDate(arrivalAt,dateFormat);
        String dateLeave = getDate(leaveAt,dateFormat);
        String[] serviceArray;
        String[] provinceArray;
        serviceArray = getResources().getStringArray(R.array.service_array);
        provinceArray = getResources().getStringArray(R.array.province_array);


        titleView.setText(name);
        idView.setText(Integer.toString(id));
        serviceIdView.setText(Integer.toString(serviceId));
        addressView.setText(address);
        dateView.setText(dateArrival + " - " +dateLeave);
        serviceTypeView.setText(serviceArray[serviceTypeId+1]);
        provinceIdView.setText(provinceArray[provinceId]);
        priceView.setText(minCost + " - " + maxCost);



    }


    public static String getDate(long milliSeconds, String dateFormat)
    {
        // Create a DateFormatter object for displaying date in specified format.
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }
}
