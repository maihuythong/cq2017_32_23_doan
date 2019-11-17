package com.maihuythong.testlogin.pop;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.maihuythong.testlogin.R;

public class ShowPopupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup_stop_point);

        // Get the intent that launched this activity, and the message in
        // the intent extra.
        Intent intent = getIntent();
        String addressPassedFromMap = intent.getStringExtra("EXTRA_ADDRESS");

        // Put that message into the text_message TextView
        TextView mAddressTextView = findViewById(R.id.address_id);
        mAddressTextView.setText(addressPassedFromMap);
    }


    public void returnReply(View view) {
        EditText mStopPointName = findViewById(R.id.stop_point_name);
        Spinner mServiceType = findViewById(R.id.service_type_spinner);
        Spinner mProvinceSpinner = findViewById(R.id.province_spinner);

        // Get the reply message from the edit text.
        String stopPointName = mStopPointName.getText().toString();
        String serviceType = mServiceType.getSelectedItem().toString();
        String province = mProvinceSpinner.getSelectedItem().toString();


        // Create a new intent for the reply, add the reply message to it
        // as an extra, set the intent result, and close the activity.
        Intent replyIntent = new Intent();
        replyIntent.putExtra("REPLY_STOP_POINT_NAME", stopPointName);
        replyIntent.putExtra("REPLY_SERVICE_TYPE", serviceType);
        replyIntent.putExtra("REPLY_PROVINCE", province);
        setResult(RESULT_OK, replyIntent);
        finish();
    }
}
