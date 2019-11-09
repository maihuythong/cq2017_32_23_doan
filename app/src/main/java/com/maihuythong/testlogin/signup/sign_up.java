package com.maihuythong.testlogin.signup;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.maihuythong.testlogin.R;
import com.maihuythong.testlogin.network.MyAPIClient;
import com.maihuythong.testlogin.network.UserService;

public class sign_up extends AppCompatActivity {
    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private EditText mPhoneView;

    private View mProgressView;
    private View mSignUpFormView;

    private ProgressDialog mProgressDialog;
    private UserService userService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setCancelable(false);
        userService = MyAPIClient.getInstance().getAdapter().create(UserService.class);

        mEmailView = findViewById(R.id.email);
        mPhoneView = findViewById(R.id.phone);

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.integer.signup || id == EditorInfo.IME_NULL) {
                    //attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignUpButton = (Button) findViewById(R.id.email_sign_up_button);
        mEmailSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //attemptLogin();
            }
        });

        mSignUpFormView = findViewById(R.id.registration_form);
        mProgressView = findViewById(R.id.signup_progress);
    }



}
