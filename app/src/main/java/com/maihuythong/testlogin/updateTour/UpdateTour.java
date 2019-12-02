package com.maihuythong.testlogin.updateTour;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.maihuythong.testlogin.LoginActivity;
import com.maihuythong.testlogin.R;
import com.maihuythong.testlogin.showAccountTours.ShowAccountToursActivity;
import com.maihuythong.testlogin.showlist.ShowListActivity;
import com.maihuythong.testlogin.signup.APIService;
import com.maihuythong.testlogin.signup.ApiUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateTour extends AppCompatActivity {
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
    private Button editTourButton;
    private int position;

    private String nameReceived, startDateReceived, endDateReceived, minCostReceived, maxCostReceived;
    private long idReceived, adultsReceived, childrenReceived;
    private boolean isPrivateReceived;


    private String nameNew, startDateNew, endDateNew, minCostNew, maxCostNew;
    private long idNew, adultsNew, childrenNew;
    private boolean isPrivateNew;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_tour);

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
        editTourButton = findViewById(R.id.edit_tour_button);

        Intent intent = getIntent();

        idReceived = intent.getLongExtra("id", 0);

        nameReceived = intent.getStringExtra("name");
        tourNameView.setText(nameReceived);

        startDateReceived = intent.getStringExtra("startDate");
        SimpleDateFormat originalFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date startDateDT = new Date(Long.parseLong(startDateReceived));
        startDate.setText(originalFormat.format(startDateDT));

        endDateReceived = intent.getStringExtra("endDate");
        Date endDateDT = new Date(Long.parseLong(endDateReceived));
        endDate.setText(originalFormat.format(endDateDT));

        adultsReceived = intent.getLongExtra("adult",0);
        adultsView.setText(adultsReceived + "");

        childrenReceived = intent.getLongExtra("children",0);
        childrenView.setText(childrenReceived + "");

        minCostReceived = intent.getStringExtra("minCost");
        minCostView.setText(minCostReceived);

        maxCostReceived = intent.getStringExtra("maxCost");
        maxCostView.setText(maxCostReceived);

        isPrivateReceived = intent.getBooleanExtra("isPrivate",false);
        if(isPrivateReceived){
            isPrivateButton.setEnabled(true);
        }
        else{
            isPrivateButton.setChecked(false);
        }

//        idNew = idReceived;
//        nameNew = tourNameView.getText().toString();
//        minCostNew = minCostView.getText().toString();
//        maxCostNew = maxCostView.getText().toString();
//        adultsNew = Long.parseLong(adultsView.getText().toString());
//        childrenNew = Long.parseLong(childrenView.getText().toString());
//        isPrivateNew = isPrivateButton.isChecked();

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

        editTourButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String token;
                token = LoginActivity.token;
                if(token == null){
                    SharedPreferences sf = getSharedPreferences("com.maihuythong.testlogin_preferences", MODE_PRIVATE);
                    token = sf.getString("login_access_token", "");
                }

                idNew = idReceived;
                nameNew = tourNameView.getText().toString();
                minCostNew = minCostView.getText().toString();
                maxCostNew = maxCostView.getText().toString();
                adultsNew = Long.parseLong(adultsView.getText().toString());
                childrenNew = Long.parseLong(childrenView.getText().toString());
                isPrivateNew = isPrivateButton.isChecked();

                APIService mAPIService = ApiUtils.getAPIService();
                mAPIService.updateTour(token, idNew, nameNew, startDateNew, endDateNew,
                        adultsNew, childrenNew, minCostNew, maxCostNew, isPrivateNew).enqueue(new Callback<UpdateTourReq>() {
                    @Override
                    public void onResponse(Call<UpdateTourReq> call, Response<UpdateTourReq> response) {
                        if(response.code() == 200){
                            Toast.makeText(UpdateTour.this, "Sua thanh cong", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                        else{
                            //TODO:
                        }
                    }

                    @Override
                    public void onFailure(Call<UpdateTourReq> call, Throwable t) {
                        //TODO:
                    }
                });
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
                startDateNew=calendar.getTimeInMillis() + "";
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
                endDateNew=calendar.getTimeInMillis() + "";
            }
        },yearCr,monthCr,dayCr);

        datePickerDialog.show();
    }
}
