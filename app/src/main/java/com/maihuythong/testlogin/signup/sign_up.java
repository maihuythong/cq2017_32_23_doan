package com.maihuythong.testlogin.signup;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.maihuythong.testlogin.LoginActivity;
import com.maihuythong.testlogin.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class sign_up extends AppCompatActivity {
    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private EditText mPhoneView;
    private TextView mErrorDialogue;
    private EditText mDobVew;

    private View mProgressView;
    private View mSignUpFormView;

    private ProgressDialog mProgressDialog;

    private APIService mAPIService;

    private String valueDobUser="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setCancelable(false);
        mEmailView = findViewById(R.id.email);
        mPhoneView = findViewById(R.id.phone);
        mErrorDialogue = findViewById(R.id.error_dialogue);
        mDobVew = findViewById(R.id.dob_user_sign_up);

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
                sendPost(email, password, phone,valueDobUser);
            }
        });

        mDobVew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseDOB();
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


    private void sendPost(String email, String password, String phone, String valueDobUser) {
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

            valueDobUser = "2019-12-30";
            mAPIService = ApiUtils.getAPIService();

            mAPIService.signUp(email, password, phone,valueDobUser).enqueue(new Callback<Post>() {
                @Override
                public void onResponse(Call<Post> call, Response<Post> response) {

                    if(response.isSuccessful()) {
                        Intent intent = new Intent(sign_up.this, LoginActivity.class);
                        finish();
                        startActivity(intent);
                    }
                    else if(response.code() == 400){
                        mErrorDialogue.setText("Duplicated user");
                        mProgressDialog.hide();
                    }
                    else if(response.code() == 503){
                        mErrorDialogue.setText("Server is busy, please try again after few minutes");
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
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-mm-dd");
                mDobVew.setText(simpleDateFormat.format(calendar.getTime()));
                valueDobUser = simpleDateFormat.format(calendar.getTime());
            }

        },yearCr,monthCr,dayCr);

        datePickerDialog.show();
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
