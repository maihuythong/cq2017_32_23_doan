package com.maihuythong.testlogin.pop;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.maihuythong.testlogin.R;

public class ShowPopupActivity extends AppCompatActivity {

    private Spinner spinnerProvince;
    private MenuItem itemSpinnerProvince;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup_stop_point);

        Spinner spinnerProvince = findViewById(R.id.province_spinner);
        ArrayAdapter<CharSequence> adapterProvince = ArrayAdapter.createFromResource(this,
                R.array.province_array, android.R.layout.simple_spinner_item);
        adapterProvince.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerProvince.setAdapter(adapterProvince);

        Spinner spinnerService = findViewById(R.id.service_type_spinner);
        ArrayAdapter<CharSequence> adapterService = ArrayAdapter.createFromResource(this,
                R.array.service_array, android.R.layout.simple_spinner_item);
        adapterService.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerService.setAdapter(adapterService);

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
        EditText mArrivalAt = findViewById(R.id.arrive_time_id);
        EditText mLeaveAt = findViewById(R.id.leave_time_id);


        // Get the reply message from the edit text.
        String stopPointName = mStopPointName.getText().toString();
        int serviceType = mServiceType.getSelectedItemPosition();
        int province = mProvinceSpinner.getSelectedItemPosition();
        //TODO:
        String arrivalAt = mArrivalAt.getText().toString();
//        long arrivalAtInt = Long.parseLong(arrivalAt);
        String leaveAt = mLeaveAt.getText().toString();
//        long leaveAtInt = Long.parseLong(leaveAt);


        // Create a new intent for the reply, add the reply message to it
        // as an extra, set the intent result, and close the activity.
        Intent replyIntent = new Intent();
        replyIntent.putExtra("REPLY_STOP_POINT_NAME", stopPointName);
        replyIntent.putExtra("REPLY_SERVICE_TYPE", serviceType);
        replyIntent.putExtra("REPLY_PROVINCE", province);
        //TODO:
        replyIntent.putExtra("REPLY_ARRIVAL_AT", 1000000);
        replyIntent.putExtra("REPLY_LEAVE_AT", 20000000);
        setResult(RESULT_OK, replyIntent);
        finish();
    }
}
