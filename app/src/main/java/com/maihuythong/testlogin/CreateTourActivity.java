package com.maihuythong.testlogin;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class CreateTourActivity  extends AppCompatActivity {
    private EditText startDate;
    private EditText endDate;


    private String tourName;
    private Long startDateMilis;
    private Long endDateMilis;
    private boolean isPrivate = true;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_tour);

        startDate = (EditText) findViewById(R.id.start_date);
        endDate = (EditText) findViewById(R.id.end_date);

        startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseDateStart();
            }
        });
        endDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseDateEnd();
            }
        });

    }

    private void chooseDateStart(){
        final Calendar calendar = Calendar.getInstance();
        int dayCr = calendar.get(Calendar.DATE);
        int monthCr = calendar.get(Calendar.MONTH);
        int yearCr = calendar.get(Calendar.YEAR);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(year,month,dayOfMonth);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/YYYY");
                startDate.setText(simpleDateFormat.format(calendar.getTime()));

            }
        },yearCr,monthCr,dayCr);

        datePickerDialog.show();
    }
    private void chooseDateEnd(){
        final Calendar calendar = Calendar.getInstance();
        int dayCr = calendar.get(Calendar.DATE);
        int monthCr = calendar.get(Calendar.MONTH);
        int yearCr = calendar.get(Calendar.YEAR);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(year,month,dayOfMonth);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/YYYY");
                endDate.setText(simpleDateFormat.format(calendar.getTime()));
            }
        },yearCr,monthCr,dayCr);

        datePickerDialog.show();
    }
}
