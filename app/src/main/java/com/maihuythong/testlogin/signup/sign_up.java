package com.maihuythong.testlogin.signup;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.maihuythong.testlogin.LoginActivity;
import com.maihuythong.testlogin.MainActivity;
import com.maihuythong.testlogin.R;
import com.maihuythong.testlogin.manager.MyApplication;
import com.maihuythong.testlogin.model.LoginRequest;
import com.maihuythong.testlogin.model.LoginResponse;
import com.maihuythong.testlogin.network.MyAPIClient;
import com.maihuythong.testlogin.network.UserService;
import com.maihuythong.testlogin.showlist.ShowListReq;
import com.maihuythong.testlogin.showlist.ShowListRes;

import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class sign_up extends AppCompatActivity {
    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private EditText mPhoneView;
    private TextView mErrorDialogue;

    private View mProgressView;
    private View mSignUpFormView;

    private ProgressDialog mProgressDialog;
    private UserService userService;

    private APIService mAPIService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setCancelable(false);
        userService = MyAPIClient.getInstance().getAdapter().create(UserService.class);

        mEmailView = findViewById(R.id.email);
        mPhoneView = findViewById(R.id.phone);
        mErrorDialogue = findViewById(R.id.error_dialogue);

        mPasswordView = findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.integer.signup || id == EditorInfo.IME_NULL) {
                    //sendPost(email, password, phone);
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignUpButton = (Button) findViewById(R.id.email_sign_up_button);
        mEmailSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = mEmailView.getText().toString();
                String password = mPasswordView.getText().toString();
                String phone = mPhoneView.getText().toString();
                sendPost(email, password, phone);
            }
        });

        mSignUpFormView = findViewById(R.id.registration_form);
        mProgressView = findViewById(R.id.signup_progress);

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mProgressDialog != null)
            mProgressDialog.dismiss();
    }


    private void sendPost(String email, String password, String phone) {
        mProgressDialog.show();
        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if(cancel == true){
            mProgressDialog.hide();
        }else{
//            final LoginRequest request = new LoginRequest();
//            request.setUsername(email);
//            request.setPassword(password);
//            Call<LoginResponse> call = userService.login(request);
//
//            call.enqueue(new Callback<LoginResponse>() {
//                @Override
//                public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
//// Save login info
//
//                    MyAPIClient.getInstance().setAccessToken(response.body().getData().getToken());
//                    long time = (new Date()).getTime()/1000;
//                    SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
//                    SharedPreferences.Editor editor = sharedPref.edit();
//                    editor.putString(getString(R.string.saved_access_token), response.body().getData().getToken());
//                    editor.putString(
//                            getString(R.string.saved_access_token), response.body().getData().getToken());
//                    editor.putLong(getString(R.string.saved_access_token_time), time);
//                    editor.commit();
//
//                    MyApplication app = (MyApplication) LoginActivity.this.getApplication();
//                    app.setTokenInfo(response.body().getData());
//
//                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                    startActivity(intent);
//                    LoginActivity.this.finish();
//
//                    mProgressDialog.hide();
//                }
//
//                @Override
//                public void onFailure(Call<LoginResponse> call, Throwable t) {
//                    Log.d(TAG, t.getMessage());
//                    mProgressDialog.hide();
//                }
//            });


            mAPIService = ApiUtils.getAPIService();

            mAPIService.signUp(email, password, phone).enqueue(new Callback<Post>() {
                @Override
                public void onResponse(Call<Post> call, Response<Post> response) {

                    if(response.isSuccessful()) {
                        Intent intent = new Intent(sign_up.this, LoginActivity.class);
                        startActivity(intent);
                        Log.i("aaa", "post submitted to API." + response.body().toString());
                    }
                    else if(response.code() == 400){
                        mErrorDialogue.setText("Thông tin đăng ký bị trùng");
                        mProgressDialog.hide();
                    }
                    else if(response.code() == 503){
                        mErrorDialogue.setText("Server đang bận, vui lòng thử lại sau ít phút");
                        mProgressDialog.hide();
                    }
                }

                @Override
                public void onFailure(Call<Post> call, Throwable t) {
                    mErrorDialogue.setText("Kiểm tra kết nối internet của bạn và thử lại");
                    mProgressDialog.hide();
                }
            });
        }
    }


    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }
}
