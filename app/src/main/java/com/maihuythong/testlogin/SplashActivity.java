package com.maihuythong.testlogin;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import android.provider.Settings;
import android.text.TextUtils;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;

import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.google.firebase.iid.FirebaseInstanceId;
import com.maihuythong.testlogin.firebase.MyFirebaseService;
import com.maihuythong.testlogin.firebase.PutTokenFirebase;
import com.maihuythong.testlogin.network.MyAPIClient;
import com.maihuythong.testlogin.manager.Constants;
import com.maihuythong.testlogin.showlist.ShowListActivity;
import com.maihuythong.testlogin.signup.APIService;
import com.maihuythong.testlogin.signup.ApiUtils;

import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String accessToken = sharedPref.getString(getString(R.string.saved_access_token), null);
        long time = sharedPref.getLong(getString(R.string.saved_access_token_time), (long)0);
        // Get user info with access token
        long expire = (new Date()).getTime()/1000 - time;
        MyAPIClient.getInstance().setAccessToken(accessToken);

        if (TextUtils.isEmpty(accessToken) || expire > Constants.expire_token) {
            Intent intent = new Intent(this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | IntentCompat.FLAG_ACTIVITY_CLEAR_TASK);
            FacebookSdk.sdkInitialize(getApplicationContext());
            LoginManager.getInstance().logOut();
            AccessToken.setCurrentAccessToken(null);
            startActivity(intent);
            finish();

        }else{
            @SuppressLint("HardwareIds") String deviceId = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);

            APIService mAPIService = ApiUtils.getAPIService();

            mAPIService.putTokenFirebase(accessToken, FirebaseInstanceId.getInstance().getToken(), deviceId, 1, "1.0").enqueue(new Callback<PutTokenFirebase>() {
                @Override
                public void onResponse(Call<PutTokenFirebase> call, Response<PutTokenFirebase> response) {
                    Toast.makeText(getApplicationContext(), "Firebase: " + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Call<PutTokenFirebase> call, Throwable t) {

                }
            });

            Intent intent = new Intent(this, ShowListActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | IntentCompat.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();

        }
    }
}
