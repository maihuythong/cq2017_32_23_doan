package com.maihuythong.testlogin.userInfo;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;

import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.google.android.material.snackbar.Snackbar;
import com.maihuythong.testlogin.LoginActivity;
import com.maihuythong.testlogin.R;
import com.maihuythong.testlogin.signup.APIService;
import com.maihuythong.testlogin.signup.ApiUtils;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
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

    public ProgressDialog mProgressDialog;

    private TextView userIdView;
    private TextView genderView;

    UserInfoRes UserInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_user_info);
        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        userIdView = (TextView)findViewById(R.id.userInfo_id);
        genderView = (TextView)findViewById(R.id.genderInfo_user);
        avatarView = (ImageView)findViewById(R.id.avatar_user);
        avatarView = (ImageView)findViewById(R.id.avatar_view_up);
        fullNameView = (AutoCompleteTextView)findViewById(R.id.full_name_up);
        emailView = (AutoCompleteTextView)findViewById(R.id.email_up);
        phoneView = (AutoCompleteTextView)findViewById(R.id.phone_number_up);
        dobView = (EditText)findViewById(R.id.dob_up);
        genderGroup = (RadioGroup)findViewById(R.id.radio_group_gender_up);

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setCancelable(false);

        GetUserInfo();

        Button LogOutButton = (Button)findViewById(R.id.logout_button);
        LogOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogOut();
            }
        });

        Button VerifyEmail =(Button)findViewById(R.id.verify_email);

        VerifyEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getVerifyCode("email");
            }
        });

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

    private void LogOut(){
        mProgressDialog.show();
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(UpdateUserInfoActivity.this);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.remove(UpdateUserInfoActivity.this.getString(R.string.saved_access_token));
        editor.remove(UpdateUserInfoActivity.this.getString(R.string.saved_access_token_time));
        editor.commit();
        LogOutFaceBook();
        // Open LoginActivity
        Intent intent = new Intent(UpdateUserInfoActivity.this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        UpdateUserInfoActivity.this.startActivity(intent);
        UpdateUserInfoActivity.this.finish();
        mProgressDialog.hide();
    }

    private void LogOutFaceBook(){
        FacebookSdk.sdkInitialize(getApplicationContext());
        LoginManager.getInstance().logOut();
        AccessToken.setCurrentAccessToken(null);
    }

    private void getVerifyCode(final String typeVerify){
        APIService mAPIService = ApiUtils.getAPIService();

        mAPIService.getVerify(UserInfo.getID(),typeVerify).enqueue(new Callback<GetVerifyCodeRes>() {
            @Override
            public void onResponse(Call<GetVerifyCodeRes> call, Response<GetVerifyCodeRes> response) {
                if(response.code()==200) {
//                    Toast.makeText(UpdateUserInfoActivity.this, "Verify was sent, please check email to get verify code!", Toast.LENGTH_LONG).show();
                    Snackbar.make(findViewById(android.R.id.content).getRootView(),
                            "Verify was sent, please check email to get verify code!", Snackbar.LENGTH_LONG).show();
                    Intent intent = new Intent(UpdateUserInfoActivity.this,InputVerifyCodeActivity.class);
                    intent.putExtra("userId",UserInfo.getID());
                    intent.putExtra("typeVerify",typeVerify);
                    startActivity(intent);
                }

                if(response.code()==400) {
                    String message = "Send failed!";
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        message = jObjError.getString("message");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(UpdateUserInfoActivity.this,"Error "+ message, Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<GetVerifyCodeRes> call, Throwable t) {

            }
        });
    }

    private void GetUserInfo(){
        String token = GetTokenLoginAccess();
        APIService mAPIService = ApiUtils.getAPIService();

        mAPIService.getUserInfo(token).enqueue(new Callback<UserInfoRes>() {
            @Override
            public void onResponse(Call<UserInfoRes> call, Response<UserInfoRes> response) {
                if(response.code()==200) {
                    Toast.makeText(UpdateUserInfoActivity.this, "Get info success", Toast.LENGTH_LONG).show();
                    UserInfo = response.body();
                    if(!Objects.isNull(UserInfo.getAvatar()))
                        Picasso.get().load(UserInfo.getAvatar()).into(avatarView);

                    if(!Objects.isNull(UserInfo.getID()))
                        userIdView.setText(String.valueOf(UserInfo.getID()));
                    if(!Objects.isNull(UserInfo.getEmail()))
                        emailView.setText(UserInfo.getEmail());
                    if(!Objects.isNull(UserInfo.getFullName()))
                        fullNameView.setText(UserInfo.getFullName());
                    if(!Objects.isNull(UserInfo.getPhone()))
                        phoneView.setText(UserInfo.getPhone());
                    if(!Objects.isNull(UserInfo.getGender()))
                        if(UserInfo.getGender()==0)
                            genderView.setText("Nam");
                        else genderView.setText("Nữ");
                    if(!Objects.isNull(UserInfo.getDob()))
                        dobView.setText(UserInfo.getDob());

                }

                if(response.code()==401)
                    Toast.makeText(UpdateUserInfoActivity.this,"No authorization token was found", Toast.LENGTH_LONG).show();

                if(response.code()==503)
                    Toast.makeText(UpdateUserInfoActivity.this,"Server error on creating user", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<UserInfoRes> call, Throwable t) {
                Toast.makeText(UpdateUserInfoActivity.this,"Error get information user", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                Intent intent = new Intent(UpdateUserInfoActivity.this,UserInfoActivity.class);
                startActivity(intent);
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
//                    Toast.makeText(UpdateUserInfoActivity.this, "Update information success", Toast.LENGTH_LONG).show();
                    Snackbar.make(findViewById(android.R.id.content),
                            "Update infomation successfully", Snackbar.LENGTH_LONG).show();
//                    Intent intent = new Intent(UpdateUserInfoActivity.this,UserInfoActivity.class);
//                    startActivity(intent);
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
    public void onBackPressed() {
        finish();
        Intent intent = new Intent(UpdateUserInfoActivity.this,UserInfoActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mProgressDialog != null)
            mProgressDialog.dismiss();
    }
}
