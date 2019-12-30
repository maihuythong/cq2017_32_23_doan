package com.maihuythong.testlogin.forgotPassword;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.maihuythong.testlogin.R;
import com.maihuythong.testlogin.signup.APIService;
import com.maihuythong.testlogin.signup.ApiUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RequestOTPActivity extends AppCompatActivity {

    private String type;
    private String value;
    private AutoCompleteTextView valueView;
    private Spinner spinnerView;
    private Button SendButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_otp);
        spinnerView = (Spinner) findViewById(R.id.type_request_otp_spinner);
        SendButton = (Button)findViewById(R.id.send_request_OTP);
        valueView = (AutoCompleteTextView)findViewById(R.id.value_base_on_type);

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.type_request_otp, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinnerView.setAdapter(adapter);

        SendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendRequestOTP();
                hideInputKeyboard();
            }
        });

    }


    private void SendRequestOTP(){
        type = spinnerView.getSelectedItem().toString().toLowerCase();
        value = valueView.getText().toString();

        if (TextUtils.isEmpty(value)) {
            valueView.setError("Please input your email or phone!");
            return;
        }
        if(type.contains("email") && !isEmailValid(value)){
            valueView.setError("Invalid Emails!");
            return;
        }

        APIService mAPIService = ApiUtils.getAPIService();
        mAPIService.sendOTPRequest(type,value).enqueue(new Callback<SendRequestOTPRes>() {
            @Override
            public void onResponse(Call<SendRequestOTPRes> call, Response<SendRequestOTPRes> response) {
                if(response.code()==200) {
                    Toast.makeText(RequestOTPActivity.this, "Send success! Please check your email to get verify code OTP", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(RequestOTPActivity.this,RecoveryPasswordActivity.class);
                    assert response.body() != null;
                    intent.putExtra("userId",response.body().getUserId());
                    intent.putExtra("value",value);
                    intent.putExtra("type",type);
                    startActivity(intent);
                }
                if(response.code()==400||response.code()==404||response.code()==500){
                    String message = "Send failed!";
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        message = jObjError.getString("message");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    if(message.isEmpty()) message="Send failed!";
                    Toast.makeText(RequestOTPActivity.this, "Error: " + message, Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<SendRequestOTPRes> call, Throwable t) {
                Toast.makeText(RequestOTPActivity.this, "Error: Send failed!", Toast.LENGTH_LONG).show();
            }
        });
    }


    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic

        return email.contains("@");
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
