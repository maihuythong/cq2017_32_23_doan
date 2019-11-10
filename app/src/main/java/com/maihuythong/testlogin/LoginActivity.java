package com.maihuythong.testlogin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.maihuythong.testlogin.manager.MyApplication;
import com.maihuythong.testlogin.model.LoginResponse;
import com.maihuythong.testlogin.network.MyAPIClient;
import com.maihuythong.testlogin.network.MyAPILogin;
import com.maihuythong.testlogin.network.RetrofitServices;
import com.maihuythong.testlogin.network.UserService;
import com.maihuythong.testlogin.signup.sign_up;
import com.maihuythong.testlogin.socialNetwork.LoginService;

import java.util.Arrays;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {
    public static String TAG  = "LoginActivity";
    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private ProgressDialog mProgressDialog;
    private UserService userService;
    CallbackManager callbackManager;
    LoginButton loginButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setCancelable(false);

        //========================Login facebook area=====================================================
        callbackManager = CallbackManager.Factory.create(); // To receive response of server facebook
        loginButton = (LoginButton) findViewById(R.id.loginFB_button);
        loginButton.setReadPermissions(Arrays.asList("email","public_profile"));
        // If you are using in a fragment, call loginButton.setFragment(this);
        // Callback registration
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                AccessToken accessToken = AccessToken.getCurrentAccessToken();
                Toast.makeText(LoginActivity.this,"Successful", Toast.LENGTH_LONG).show();

                Retrofit retrofit = MyAPILogin.getRetrofit();
                LoginService loginService = retrofit.create(LoginService.class);
                Call<LoginResponse> call = loginService.isValidUser(accessToken.getToken().toString());
                call.enqueue(new Callback<LoginResponse>() {
                    @Override
                    public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {

                        LoginResponse result = response.body();

                        long time = (new Date()).getTime()/1000;
                        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putString(getString(R.string.saved_access_token), result.getToken());
                        editor.putString(
                                getString(R.string.saved_access_token), result.getToken());
                        editor.putLong(getString(R.string.saved_access_token_time), time);
                        editor.commit();
//
                        MyApplication app = (MyApplication) LoginActivity.this.getApplication();
                        app.setToken(result.getToken());

                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.putExtra("string_Token",result.getToken());
                        startActivity(intent);
                        LoginActivity.this.finish();



                        mProgressDialog.hide();
                    }

                    @Override
                    public void onFailure(Call<LoginResponse> call, Throwable t) {
                        Log.d(TAG, t.getMessage());
                        mProgressDialog.hide();
                    }
                });


//                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//                startActivity(intent);
            }

            @Override
            public void onCancel() {
                Toast.makeText(LoginActivity.this,"Login attempt canceled.", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(FacebookException exception) {
                Toast.makeText(LoginActivity.this,"Login attempt failed.", Toast.LENGTH_LONG).show();
            }
        });

        //================================End Face book area==============================



       //  userService = MyAPIClient.getInstance().getAdapter().create(UserService.class);
        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.integer.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        Button mOpenSignUpButton = findViewById(R.id.signup_open);
        mOpenSignUpButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, sign_up.class);
                startActivity(intent);
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mProgressDialog != null)
            mProgressDialog.dismiss();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        callbackManager.onActivityResult(requestCode,resultCode,data);
        super.onActivityResult(requestCode, resultCode, data);
    }





    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        mProgressDialog.show();
        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

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

            Retrofit retrofit = MyAPILogin.getRetrofit();
            RetrofitServices retrofitServices = retrofit.create(RetrofitServices.class);
            Call<LoginResponse> call = retrofitServices.isValidUser(mEmailView.getText().toString(),mPasswordView.getText().toString());
            call.enqueue(new Callback<LoginResponse>() {
                @Override
                public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
// Save login info

                    LoginResponse result = response.body();
//                    Log.i("Token",result.getToken());
//                    Log.i("UserId", String.valueOf(result.getUserId()));
                    
//                    MyAPIClient.getInstance().setAccessToken(response.body().getData().getToken());
                    long time = (new Date()).getTime()/1000;
                    SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString(getString(R.string.saved_access_token), result.getToken());
                    editor.putString(
                    getString(R.string.saved_access_token), result.getToken());
                    editor.putLong(getString(R.string.saved_access_token_time), time);
                    editor.commit();
//
                    MyApplication app = (MyApplication) LoginActivity.this.getApplication();
                    app.setToken(result.getToken());

                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    LoginActivity.this.finish();

                    mProgressDialog.hide();
                }

                @Override
                public void onFailure(Call<LoginResponse> call, Throwable t) {
                    Log.d(TAG, t.getMessage());
                    mProgressDialog.hide();
                }
            });
        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
//        return email.contains("@");
        return true;
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

}

