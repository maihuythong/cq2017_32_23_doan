package com.maihuythong.testlogin.UserInfo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.maihuythong.testlogin.LoginActivity;
import com.maihuythong.testlogin.R;

public class UserInfoActivity extends AppCompatActivity {
    public ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setCancelable(false);
        Button LogOutButton = (Button)findViewById(R.id.logout);

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


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mProgressDialog != null)
            mProgressDialog.dismiss();
    }
}
