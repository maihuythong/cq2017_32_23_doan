package com.maihuythong.testlogin.UserInfo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.maihuythong.testlogin.LoginActivity;
import com.maihuythong.testlogin.R;
import com.maihuythong.testlogin.signup.APIService;
import com.maihuythong.testlogin.signup.ApiUtils;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserInfoActivity extends AppCompatActivity {
    public ProgressDialog mProgressDialog;

    private TextView userIdView;
    private TextView emailView;
    private TextView fullNameView;
    private TextView phoneView;
    private TextView genderView;
    private TextView dobView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);


        userIdView = (TextView)findViewById(R.id.userInfo_id);
        emailView = (TextView)findViewById(R.id.emailInfo_user);
        fullNameView= (TextView)findViewById(R.id.nameInfo_user);
        phoneView = (TextView)findViewById(R.id.phoneInfo_user);
        genderView = (TextView)findViewById(R.id.genderInfo_user);
        dobView = (TextView)findViewById(R.id.dobInfo_user);


        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setCancelable(false);
        Button LogOutButton = (Button)findViewById(R.id.logout);
        GetUserInfo();
        LogOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogOut();
            }
        });
    }

    //Handle logout
    private void LogOut(){
        mProgressDialog.show();
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(UserInfoActivity.this);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.remove(UserInfoActivity.this.getString(R.string.saved_access_token));
        editor.remove(UserInfoActivity.this.getString(R.string.saved_access_token_time));
        editor.commit();
        LogOutFaceBook();
        // Open LoginActivity
        Intent intent = new Intent(UserInfoActivity.this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        UserInfoActivity.this.startActivity(intent);
        UserInfoActivity.this.finish();
        mProgressDialog.hide();
    }

    private void LogOutFaceBook(){
        FacebookSdk.sdkInitialize(getApplicationContext());
        LoginManager.getInstance().logOut();
        AccessToken.setCurrentAccessToken(null);
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

    private void GetUserInfo(){
        String token = GetTokenLoginAccess();
        APIService mAPIService = ApiUtils.getAPIService();

        mAPIService.getUserInfo(token).enqueue(new Callback<UserInfoRes>() {
            @Override
            public void onResponse(Call<UserInfoRes> call, Response<UserInfoRes> response) {
                if(response.code()==200) {
                    Toast.makeText(UserInfoActivity.this, "Get info success", Toast.LENGTH_LONG).show();
                    UserInfoRes newUserInfo = response.body();
                    if(!Objects.isNull(newUserInfo.getID()))
                        userIdView.setText(String.valueOf(newUserInfo.getID()));
                    if(!Objects.isNull(newUserInfo.getEmail()))
                        emailView.setText(newUserInfo.getEmail());
                    if(!Objects.isNull(newUserInfo.getFullName()))
                        fullNameView.setText(newUserInfo.getFullName());
                    if(!Objects.isNull(newUserInfo.getPhone()))
                        phoneView.setText(newUserInfo.getPhone());
                    if(!Objects.isNull(newUserInfo.getGender()))
                        if(newUserInfo.getGender()==0)
                            genderView.setText("Nam");
                        else genderView.setText("Nữ");
                    if(!Objects.isNull(newUserInfo.getDob()))
                        dobView.setText(newUserInfo.getDob());

                }

                if(response.code()==401)
                    Toast.makeText(UserInfoActivity.this,"No authorization token was found", Toast.LENGTH_LONG).show();

                if(response.code()==503)
                    Toast.makeText(UserInfoActivity.this,"Server error on creating user", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<UserInfoRes> call, Throwable t) {
                Toast.makeText(UserInfoActivity.this,"Error get information user", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mProgressDialog != null)
            mProgressDialog.dismiss();
    }
}
