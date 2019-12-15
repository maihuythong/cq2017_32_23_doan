package com.maihuythong.testlogin.userInfo;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.maihuythong.testlogin.LoginActivity;
import com.maihuythong.testlogin.R;
import com.maihuythong.testlogin.signup.APIService;
import com.maihuythong.testlogin.signup.ApiUtils;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateUserInfoActivity extends AppCompatActivity {

    private ImageView avatarView;
    private AutoCompleteTextView fullNameView;
    private AutoCompleteTextView emailView;
    private AutoCompleteTextView phoneView;
    private EditText dobView;
    private RadioGroup genderGroup;

    private Date dobUser;
    private String fullNameUser;
    private String emailUser;
    private String phoneNumberUser;
    private String avatarUser;
    private long genderUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_user_info);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        avatarView = (ImageView)findViewById(R.id.avatar_view_up);
        fullNameView = (AutoCompleteTextView)findViewById(R.id.full_name_up);
        emailView = (AutoCompleteTextView)findViewById(R.id.email_up);
        phoneView = (AutoCompleteTextView)findViewById(R.id.phone_number_up);
        dobView = (EditText)findViewById(R.id.dob_up);
        genderGroup = (RadioGroup)findViewById(R.id.radio_group_gender_up);

        fullNameView.setText(intent.getStringExtra("fullName"));
        emailView.setText(intent.getStringExtra("email"));
        phoneView.setText(intent.getStringExtra("phone"));
        dobView.setText(intent.getStringExtra("dob"));
        if(!Objects.isNull(intent.getStringExtra("avatar")))
            Picasso.get().load(intent.getStringExtra("avatar")).into(avatarView);
        genderGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if(checkedId == R.id.radio_male_up)
                    genderUser=0;
                if(checkedId == R.id.radio_female_up)
                    genderUser=1;
                else genderUser = 0;
            }
        });

        dobView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseDOB();
            }
        });


        Button submitInfo = (Button)findViewById(R.id.submit_update_user_info);
        submitInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fullNameUser= fullNameView.getText().toString();
                emailUser = emailView.getText().toString();
                phoneNumberUser = phoneView.getText().toString();
                if(TextUtils.isEmpty(fullNameUser)){
                    Toast.makeText(UpdateUserInfoActivity.this, "Input full name information!", Toast.LENGTH_LONG).show();
                } else {
                    hideInputKeyboard();
                    SubmitUpdateInfoUser(fullNameUser, emailUser, phoneNumberUser, genderUser, dobUser);
                }
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void chooseDOB(){
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
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                dobView.setText(simpleDateFormat.format(calendar.getTime()));
                try {
                    dobUser =  simpleDateFormat.parse(simpleDateFormat.format(calendar.getTime()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

        },yearCr,monthCr,dayCr);

        datePickerDialog.show();
    }

    void SubmitUpdateInfoUser(String fullNameUser,String emailUser,String phoneNumberUser,long genderUser,Date dobUser){

        String token = GetTokenLoginAccess();
        APIService mAPIService = ApiUtils.getAPIService();
        mAPIService.EditInfo(token,fullNameUser,emailUser,phoneNumberUser,genderUser,dobUser).enqueue(new Callback<UpdateUserInfoRes>() {
            @Override
            public void onResponse(Call<UpdateUserInfoRes> call, Response<UpdateUserInfoRes> response) {
                if(response.code()==200) {
                    Toast.makeText(UpdateUserInfoActivity.this, "Update information success", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(UpdateUserInfoActivity.this,UserInfoActivity.class);
                    startActivity(intent);
                }
                if(response.code()==400)
                    Toast.makeText(UpdateUserInfoActivity.this, "Invalid params", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<UpdateUserInfoRes> call, Throwable t) {

                Toast.makeText(UpdateUserInfoActivity.this, "Failed update!!", Toast.LENGTH_LONG).show();

            }
        });

    }

    private String GetTokenLoginAccess(){
        SharedPreferences sf;
        String token;
        token = LoginActivity.token;
        if(token== null){
            sf = getSharedPreferences("com.maihuythong.testlogin_preferences", MODE_PRIVATE);
            token = sf.getString("login_access_token", "");
        }
        return token;
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }


    private void hideInputKeyboard(){
        try {
            InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {
            // TODO: handle exception
            Toast.makeText(UpdateUserInfoActivity.this,"Cant hide keyboard!", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
