package com.maihuythong.testlogin;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
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
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.maihuythong.testlogin.model.CreateTourResponse;
import com.maihuythong.testlogin.network.CreateTourService;
import com.maihuythong.testlogin.signup.ApiUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

    private SharedPreferences sf;

    private String tourName ="";
    private Number startDateMilis;
    private Number endDateMilis;
    private Number adults=0;
    private Number childs=0;
    private Number minCost;
    private Number maxCost;
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

        final CreateTourService createTourService = ApiUtils.getCreateTourService();


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


                String token;
                token = LoginActivity.token;
                if(token == null){
                    sf = getSharedPreferences("com.maihuythong.testlogin", MODE_PRIVATE);
                    token = sf.getString("sf_token", "");
                }

                createTourService.getTourInfo(token,tourName,startDateMilis,endDateMilis,0,0,0,0,isPrivate,
                        null,null,null,null,null).enqueue(new Callback<CreateTourResponse>() {
                    @Override
                    public void onResponse(Call<CreateTourResponse> call, Response<CreateTourResponse> response) {
                        if(response.code() == 200) {
                            CreateTourResponse result = response.body();
                            Toast.makeText(CreateTourActivity.this,"Create Successful", Toast.LENGTH_LONG).show();
                            Number Tourid = result.getId();
                        }
                        if (response.code()==400)
                        {
                            CreateTourResponse result = response.body();
                            Toast.makeText(CreateTourActivity.this,"Bad request", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<CreateTourResponse> call, Throwable t) {
                        Toast.makeText(CreateTourActivity.this,"Create failed", Toast.LENGTH_LONG).show();
                    }
                });


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
