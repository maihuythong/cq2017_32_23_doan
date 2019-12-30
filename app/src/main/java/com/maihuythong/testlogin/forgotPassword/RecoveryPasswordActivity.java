package com.maihuythong.testlogin.forgotPassword;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.maihuythong.testlogin.LoginActivity;
import com.maihuythong.testlogin.R;
import com.maihuythong.testlogin.signup.APIService;
import com.maihuythong.testlogin.signup.ApiUtils;
import com.maihuythong.testlogin.userInfo.GetVerifyCodeRes;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecoveryPasswordActivity extends AppCompatActivity {

    private String value;
    private String type;
    private long userId;

    private AutoCompleteTextView newPasswordView;
    private AutoCompleteTextView otpView;
    private TextView informationView;
    private Button submitRecovery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recovery_password);
        newPasswordView = findViewById(R.id.new_password_recovery);
        otpView = findViewById(R.id.OTP_recovery);
        informationView = findViewById(R.id.information_recovery);
        submitRecovery = findViewById(R.id.submit_password_recovery);

        Intent intent = getIntent();
        value = intent.getStringExtra("value");
        type = intent.getStringExtra("type");
        userId = intent.getLongExtra("userId",0);
        if(type.contains("email")){
            informationView.setText("Check your email: " + value + " to get verify code OTP");
        }
        if(type.contains("phone")){
            informationView.setText("Check your message I send to phone number: " + value + " to get verify code OTP");
        }

        submitRecovery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newPasswordRecovery(userId);
                hideInputKeyboard();
            }
        });


    }

    private void newPasswordRecovery(long userId){

        String newPassword = newPasswordView.getText().toString();
        if(newPassword.isEmpty() || !isPasswordValid(newPassword)){
            newPasswordView.setError("Invalid Password, password must be at least 4 character!");
            return;
        }

        String verifyCode = otpView.getText().toString();
        if(verifyCode.isEmpty())
        {
            otpView.setError("Invalid verify code!");
            return;
        }
        APIService mAPIService = ApiUtils.getAPIService();
        mAPIService.verifyOTP(userId,newPassword,verifyCode).enqueue(new Callback<GetVerifyCodeRes>() {
            @Override
            public void onResponse(Call<GetVerifyCodeRes> call, Response<GetVerifyCodeRes> response) {
                if(response.code()==200){

                    Toast.makeText(RecoveryPasswordActivity.this,"Successful!", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(RecoveryPasswordActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
                if(response.code()==403||response.code()==500)
                {
                    String message = "Send failed!";
                    try {
                        assert response.errorBody() != null;
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        message = jObjError.getString("message");
                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                    }

                    if(message.isEmpty()) message="Send failed!";
                    Toast.makeText(RecoveryPasswordActivity.this, "Error: " + message, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<GetVerifyCodeRes> call, Throwable t) {
                Toast.makeText(RecoveryPasswordActivity.this, "Error: Send failed!", Toast.LENGTH_LONG).show();
            }
        });

    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    private void hideInputKeyboard(){
        try {
            InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {
            // TODO: handle exception
        }
    }
}
