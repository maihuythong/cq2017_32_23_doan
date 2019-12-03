package com.maihuythong.testlogin;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.maihuythong.testlogin.manager.MyApplication;

public class MainActivity extends AppCompatActivity {

    private ProgressDialog mProgressDialog;

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
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mProgressDialog != null)
            mProgressDialog.dismiss();
    }
}
