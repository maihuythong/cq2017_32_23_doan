package com.maihuythong.testlogin;

import android.app.ProgressDialog;
import android.app.VoiceInteractor;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.maihuythong.testlogin.R;
import com.maihuythong.testlogin.manager.MyApplication;
import com.maihuythong.testlogin.network.MyAPIClient;
import com.maihuythong.testlogin.network.UserService;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private ProgressDialog mProgressDialog;
    private UserService userService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MyApplication app = (MyApplication)getApplication();
        TextView tv = (TextView) findViewById(R.id.textView);
        //  tv.setText(getString(R.string.hello) + " " + app.getTokenInfo().getUserName());
        tv.setText(app.getToken());

//        userService = MyAPIClient.getInstance().getAdapter().create(UserService.class);
        Button logout = (Button)findViewById(R.id.b_Logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mProgressDialog.show();
                Call<Void> call = userService.logout();
                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        // Clear token
//                        MyAPIClient.getInstance().setAccessToken(null);
                        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.remove(MainActivity.this.getString(R.string.saved_access_token));
                        editor.remove(MainActivity.this.getString(R.string.saved_access_token_time));
                        editor.commit();
                        // Open LoginActivity
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | IntentCompat.FLAG_ACTIVITY_CLEAR_TASK);
                        mProgressDialog.hide();
                        MainActivity.this.startActivity(intent);
                        MainActivity.this.finish();
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        mProgressDialog.hide();
                    }
                });
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
