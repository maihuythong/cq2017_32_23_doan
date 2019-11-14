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
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.tasks.Task;
import com.maihuythong.testlogin.manager.MyApplication;
import com.maihuythong.testlogin.model.LoginResponse;
import com.maihuythong.testlogin.network.MyAPIClient;
import com.maihuythong.testlogin.network.MyAPILogin;
import com.maihuythong.testlogin.network.RetrofitServices;
import com.maihuythong.testlogin.network.UserService;
import com.maihuythong.testlogin.showlist.ShowListActivity;
import com.maihuythong.testlogin.signup.sign_up;
import com.maihuythong.testlogin.socialNetwork.LoginService;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {
    private static final int RC_SIGN_IN = 0;
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
    SignInButton signInButton;
    GoogleSignInClient mGoogleSignInClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setCancelable(false);


        //=========================Login Google Area======================================================

        SignInButton signInButton = findViewById(R.id.loginGG_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        String serverClientId = getString(R.string.server_client_id);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestScopes(new Scope(Scopes.DRIVE_APPFOLDER))
                .requestServerAuthCode(serverClientId)
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        signInButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.loginGG_button:
                        signIn();
                        break;
                    // ...
                }
            }
        });
        //=========================End login Google Area


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

                        Intent intent = new Intent(LoginActivity.this, ShowListActivity.class);
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
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);

    }

    //====================Google Area===========================
    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {

            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            String authCode = account.getServerAuthCode();
            GetAccessTokenGG(authCode); // get access token of google

            //Log.d("thanh1",authCode);
            Toast.makeText(LoginActivity.this,"Login successfully.", Toast.LENGTH_LONG).show();
            // Signed in successfully, show authenticated UI.s


        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            Toast.makeText(LoginActivity.this,"Login failed.", Toast.LENGTH_LONG).show();
        }
    }

    private void GetAccessTokenGG(String authCode){
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new FormBody.Builder()
                .add("grant_type", "authorization_code")
                .add("client_id",getString(R.string.server_client_id))
                .add("client_secret", getString(R.string.client_secret))
                .add("redirect_uri","")
                .add("code", authCode)
                .build();

        final Request request = new Request.Builder()
                .url("https://www.googleapis.com/oauth2/v4/token")
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(new okhttp3.Callback() {

            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                Toast.makeText(LoginActivity.this,"Login api failed.", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {

                try {
                    JSONObject jsonObject = new JSONObject(response.body().string());
                    final String accessTokenGG = jsonObject.toString(5);
                    Log.d("accesstokenGG",accessTokenGG);
                    //===============================Send access token to main activiy

                    //==============================================================================

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
//
//    @Override
//    protected void onStart() {
//        // Check for existing Google Sign In account, if the user is already signed in
//        // the GoogleSignInAccount will be non-null.
//        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
//        String authCode = account.getServerAuthCode();
//        //Log.d("thanh",authCode);
//        if(account !=null) {
//            Toast.makeText(LoginActivity.this,"Login successfully.", Toast.LENGTH_LONG).show();
//        }
//        super.onStart();
//    }

    //====================End Google Area===========================

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

                    if(response.code() == 200){
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

                        Intent intent = new Intent(LoginActivity.this, ShowListActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        LoginActivity.this.finish();

                        mProgressDialog.hide();
                    }
                    else if(response.code() == 400){
                        ((TextView)findViewById(R.id.error_dialogue_signin)).setText("Missing email/phone or password");
                        mProgressDialog.hide();
                    }
                    else if(response.code() == 404){
                        ((TextView)findViewById(R.id.error_dialogue_signin)).setText("Wrong email/phone or password");
                        mProgressDialog.hide();
                    }

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

