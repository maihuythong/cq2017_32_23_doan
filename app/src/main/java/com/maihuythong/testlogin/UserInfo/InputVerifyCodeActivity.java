package com.maihuythong.testlogin.UserInfo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.maihuythong.testlogin.R;
import com.maihuythong.testlogin.signup.APIService;
import com.maihuythong.testlogin.signup.ApiUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InputVerifyCodeActivity extends AppCompatActivity {

    private EditText verifyCodeView;
    private Button submit;
    private TextView notfyView;
    private String typeVerify;
    private long userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_verify_code);

        verifyCodeView = (EditText)findViewById(R.id.verify_code_text);
        submit = (Button)findViewById(R.id.submit_verify_code);
        notfyView = (TextView)findViewById(R.id.notify_verify_code);
        Intent intent = getIntent();
        typeVerify = intent.getStringExtra("typeVerify");
        userId = intent.getLongExtra("userId",0);

        if(typeVerify.contains("email")){
            notfyView.setText("Please check your email to get verify code!");
        }

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideInputKeyboard();
                sendVerifyCode(userId,typeVerify);
            }
        });

    }



    private void sendVerifyCode(long userId,String typeVerify){

        String verifyCode = verifyCodeView.getText().toString();

        if(verifyCode.isEmpty())
            Toast.makeText(InputVerifyCodeActivity.this,"Please input verify code...", Toast.LENGTH_LONG).show();
        else{

        APIService mAPIService = ApiUtils.getAPIService();

        mAPIService.sendVerifycode(userId,typeVerify,verifyCode).enqueue(new Callback<SendVerifyCodeRes>() {
            @Override
            public void onResponse(Call<SendVerifyCodeRes> call, Response<SendVerifyCodeRes> response) {
                if(response.code()==200){
                    if(response.body().getSuccess()==true) {
                        Toast.makeText(InputVerifyCodeActivity.this, "This email address has been verified!", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(InputVerifyCodeActivity.this,UserInfoActivity.class);
                        startActivity(intent);
                    }
                    else{
                        Toast.makeText(InputVerifyCodeActivity.this, "This email address verify not success!", Toast.LENGTH_LONG).show();
                    }

                }

                if(response.code()==400||response.code()==404||response.code()==403)
                    Toast.makeText(InputVerifyCodeActivity.this,"Error: "+ response.body().getMessage(), Toast.LENGTH_LONG).show();

            }

            @Override
            public void onFailure(Call<SendVerifyCodeRes> call, Throwable t) {
                Toast.makeText(InputVerifyCodeActivity.this,  "Send verified code not success!", Toast.LENGTH_LONG).show();
            }
        });

        }
    }


    void hideInputKeyboard(){
        try {
            InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {
            // TODO: handle exception
            Toast.makeText(InputVerifyCodeActivity.this,"Cant hide keyboard!", Toast.LENGTH_LONG).show();
        }
    }
}
