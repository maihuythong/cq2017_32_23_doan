package com.maihuythong.testlogin;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class CreateTourActivity  extends AppCompatActivity {
    private EditText startDate;
    private EditText endDate;
    private AutoCompleteTextView tourNameView;
    private EditText adultsView;
    private EditText childrenView;
    private EditText minCostView;
    private EditText maxCostView;
    private ImageView chooseImageView;
    private Button chooseImageButton;
    private RadioButton isPrivateButton;




    private String tourName ="";
    private Long startDateMilis;
    private Long endDateMilis;
    private int adults=0;
    private int childs=0;
    private int minCost;
    private int maxCost;
    private boolean isPrivate = false;
    private String avatar ="";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_tour);

        tourNameView = (AutoCompleteTextView) findViewById(R.id.tourName);
        startDate = (EditText) findViewById(R.id.start_date);
        endDate = (EditText) findViewById(R.id.end_date);
        adultsView = (EditText) findViewById(R.id.adults_id);
        childrenView = (EditText) findViewById(R.id.children_id);
        minCostView = (EditText) findViewById(R.id.min_cost_id);
        maxCostView = (EditText) findViewById(R.id.max_cost_id);
        chooseImageView = (ImageView) findViewById(R.id.imageView_id);
        chooseImageButton = (Button) findViewById(R.id.chooseImage_button);
        isPrivateButton= (RadioButton)findViewById(R.id.isPrivate_id);



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

        isPrivateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isPrivate =!isPrivate;
                isPrivateButton.setChecked(isPrivate);
            }
        });


        Button CreateTourButton = (Button)findViewById(R.id.create_tour_button);

        CreateTourButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                tourName=tourNameView.getText().toString();
                if(!TextUtils.isEmpty(adultsView.getText()))
                adults = Integer.parseInt(adultsView.getText().toString());

                if(!TextUtils.isEmpty(childrenView.getText()))
                childs = Integer.parseInt(childrenView.getText().toString());

                if(!TextUtils.isEmpty(minCostView.getText()))
                minCost = Integer.parseInt(minCostView.getText().toString());

                if(!TextUtils.isEmpty(maxCostView.getText()))
                maxCost = Integer.parseInt(maxCostView.getText().toString());


                //===========put extra to another activity hear============

                //===========================================
            }
        });

    }

    private void chooseDateStart(){
        final Calendar calendar = Calendar.getInstance();
        //Get current date and time
        int dayCr = calendar.get(Calendar.DATE);
        int monthCr = calendar.get(Calendar.MONTH);
        int yearCr = calendar.get(Calendar.YEAR);



        //Take time from DatePicker into calendar
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                calendar.set(year,month,dayOfMonth);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/YYYY");
                startDate.setText(simpleDateFormat.format(calendar.getTime()));
                startDateMilis=calendar.getTimeInMillis();
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
                endDateMilis=calendar.getTimeInMillis();
            }
        },yearCr,monthCr,dayCr);

        datePickerDialog.show();
    }

}
