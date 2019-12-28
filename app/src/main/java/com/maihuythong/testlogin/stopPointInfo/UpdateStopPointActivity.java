package com.maihuythong.testlogin.stopPointInfo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonObjectRequest;
import com.maihuythong.testlogin.LoginActivity;
import com.maihuythong.testlogin.R;
import com.maihuythong.testlogin.model.StopPoints;
import com.maihuythong.testlogin.showListStopPoints.showListStopPointsActivity;
import com.maihuythong.testlogin.signup.APIService;
import com.maihuythong.testlogin.signup.ApiUtils;
import com.maihuythong.testlogin.userInfo.GetVerifyCodeRes;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;

public class UpdateStopPointActivity extends AppCompatActivity {

    private long tourId;
    private long id;
    private long serviceId;
    private long serviceTypeId;
    private String address;
    private int provinceId;
    private String name;
    private long arrivalAt;
    private long leaveAt;

    private Number latti;
    private Number longti;

    private long arrivalBefore;
    private long leaveBefore;

    private Number minCost;
    private Number maxCost;

    private AutoCompleteTextView NameView;
    private Spinner serviceTypeSpinner;
    private AutoCompleteTextView addressView;
    private Spinner provinceIdSpinner;

    private EditText minCostEdit;
    private EditText maxCostEdit;
    private EditText arrivalAtEdit;
    private EditText leaveAtEdit;
    private Button submitUpdateButton;

    private Toolbar toolbar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_stop_point);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        NameView = findViewById(R.id.up_stop_point_name);
        serviceTypeSpinner = findViewById(R.id.up_service_type_spinner);
        addressView = findViewById(R.id.up_address_stop_point);
        provinceIdSpinner = findViewById(R.id.up_province_spinner);

        minCostEdit = findViewById(R.id.up_min_cost_id);
        maxCostEdit = findViewById(R.id.up_max_cost_id);
        arrivalAtEdit = findViewById(R.id.up_arrive_at_id);
        leaveAtEdit = findViewById(R.id.up_leave_at_id);
        submitUpdateButton = findViewById(R.id.submit_update_stop_point_button);

        setData();

        arrivalAtEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseArrivalAt();
            }
        });

        leaveAtEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseleaveAt();
            }
        });

        submitUpdateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateStopPoint();
            }
        });

    }


    private void chooseArrivalAt(){
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
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/mm/yyyy");
                arrivalAtEdit.setText(simpleDateFormat.format(calendar.getTime()));
                arrivalAt=calendar.getTimeInMillis();
            }

        },yearCr,monthCr,dayCr);

        datePickerDialog.show();
    }

    private void chooseleaveAt(){
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
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/mm/yyyy");
                leaveAtEdit.setText(simpleDateFormat.format(calendar.getTime()));
                leaveAt=calendar.getTimeInMillis();
            }

        },yearCr,monthCr,dayCr);

        datePickerDialog.show();
    }


    private void setData(){
        Intent intent = getIntent();
        tourId = intent.getLongExtra("tourId",0);
        id = intent.getIntExtra("id",0);
        serviceId = intent.getIntExtra("serviceId",0);
        address = intent.getStringExtra("address");
        if(Objects.isNull(address)) address = "";
        provinceId = intent.getIntExtra("provinceId",0);
        name = intent.getStringExtra("name");
        if(Objects.isNull(name)) name = "";
        arrivalAt = intent.getLongExtra("arrivalAt",0);
        leaveAt = intent.getLongExtra("leaveAt",0);
        arrivalBefore = intent.getLongExtra("arrivalAt",0);
        leaveBefore = intent.getLongExtra("leaveAt",0);
        minCost = intent.getLongExtra("minCost",0);
        maxCost = intent.getLongExtra("maxCost",0);
        serviceTypeId = intent.getIntExtra("serviceTypeId",0);
        latti= intent.getDoubleExtra("lat",0);
        longti = intent.getDoubleExtra("long",0);

        NameView.setText(name);
        addressView.setText(address);
        minCostEdit.setText(minCost.toString());
        maxCostEdit.setText(maxCost.toString());

        String dateFormat= "dd/MM/yyyy";

        if(serviceTypeId>0&&serviceTypeId<5)
            serviceTypeSpinner.setSelection((int)serviceTypeId-1);
        else serviceTypeId =1;
        if(provinceId>0&&provinceId<64) provinceIdSpinner.setSelection((int)provinceId-1);

        arrivalAtEdit.setText(getDate(arrivalAt,dateFormat));
        leaveAtEdit.setText(getDate(leaveAt,dateFormat));

    }

    public static String getDate(long milliSeconds, String dateFormat)
    {
        // Create a DateFormatter object for displaying date in specified format.
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }

    private Boolean checkDataChange(){


        if(!name.equals(NameView.getText().toString())) return true;
        if(!address.equals(addressView.getText().toString())) return true;
        if(!minCost.equals(Long.valueOf(minCostEdit.getText().toString()))) return true;
        if(!maxCost.equals(Long.valueOf(maxCostEdit.getText().toString()))) return true;
        if(arrivalAt != arrivalBefore) return true;
        if(leaveAt != leaveBefore) return true;

        if(provinceId>0 && provinceId<64)
            if(provinceId!=(provinceIdSpinner.getSelectedItemPosition()+1)) return true;
        if(serviceTypeId>0 && serviceTypeId<5)
            if(serviceTypeId!=(serviceTypeSpinner.getSelectedItemPosition()+1)) return true;

        return false;
    }

    private void updateStopPoint(){

        hideInputKeyboard();
        if(checkDataChange()==false) return;

        String token=GetTokenLoginAccess();
        UpdateStopPointInfo newStopPoint = new UpdateStopPointInfo();

        name = NameView.getText().toString();
        address = addressView.getText().toString();
        minCost = Double.valueOf(minCostEdit.getText().toString());
        maxCost = Double.valueOf(maxCostEdit.getText().toString());
        serviceTypeId = serviceTypeSpinner.getSelectedItemPosition()+1;
        provinceId = provinceIdSpinner.getSelectedItemPosition()+1;


        newStopPoint.setId(id);
        if(!name.isEmpty())
            newStopPoint.setName(name);
        if(!address.isEmpty())
            newStopPoint.setAddress(address);
        if(!Objects.isNull(serviceId))
            newStopPoint.setServiceId(serviceId);
        if(!Objects.isNull(serviceTypeId))
            newStopPoint.setServiceTypeId((int)serviceTypeId);
        if(!Objects.isNull(provinceId))
            newStopPoint.setProvinceId(provinceId);
        if(!Objects.isNull(latti))
            newStopPoint.setLat(latti);
        if(!Objects.isNull(longti))
            newStopPoint.setLong(longti);
        if(!Objects.isNull(arrivalAt))
            newStopPoint.setArrivalAt(arrivalAt);
        if(!Objects.isNull(leaveAt))
            newStopPoint.setLeaveAt(leaveAt);
        if(!Objects.isNull(minCost))
            newStopPoint.setMinCost(minCost.longValue());
        if(!Objects.isNull(maxCost))
            newStopPoint.setMaxCost(maxCost.longValue());

//        APIService mApiService = ApiUtils.getAPIService();
//        ArrayList<UpdateStopPointInfo> arrayList = new ArrayList<>();
//        arrayList.add(newStopPoint);
//
//        mApiService.UpdateStopPoint(token,String.valueOf(tourId),arrayList).enqueue(new Callback<GetVerifyCodeRes>() {
//            @Override
//            public void onResponse(Call<GetVerifyCodeRes> call, Response<GetVerifyCodeRes> response) {
//                if(response.code()==200)
//                {
//                    Toast.makeText(getApplicationContext(), "Update success!", Toast.LENGTH_LONG).show();
//                    finish();
//                }
//                if( response.code()== 400 || response.code()== 403 || response.code()==404)
//                {
//
//                    String message = "Send failed!";
//                    try {
//                        assert response.errorBody() != null;
//                        JSONObject jObjError = new JSONObject(response.errorBody().string());
//                        String abc = jObjError.toString();
//                    } catch (JSONException | IOException e) {
//                        e.printStackTrace();
//                    }
//                    Toast.makeText(getApplicationContext(), "Update failed!", Toast.LENGTH_LONG).show();
//                }
//            }
//            @Override
//            public void onFailure(Call<GetVerifyCodeRes> call, Throwable t) {
//                Toast.makeText(getApplicationContext(), "Update failed!", Toast.LENGTH_LONG).show();
//            }
//        });



        JSONArray stopPointsArray = new JSONArray();

        JSONObject tmp = new JSONObject();
        try{
            tmp.put("name", name);
            tmp.put("lat", latti);
            tmp.put("long", longti);
            tmp.put("leaveAt", arrivalAt);
            tmp.put("arrivalAt", leaveAt);
            tmp.put("provinceId",provinceId);
            tmp.put("serviceTypeId", serviceTypeId);
            tmp.put("address", address);
            tmp.put("minCost", minCost);
            tmp.put("address", maxCost);
            tmp.put("arrivalAt",arrivalAt);
            tmp.put("leaveAt",leaveAt);
            tmp.put("id",id);
            stopPointsArray.put(tmp);
        }
        catch (JSONException e) {
            e.printStackTrace();
        }


        //================Call api=================================

        RequestQueue requestQueue;

        // Instantiate the cache
        Cache cache = new DiskBasedCache(getCacheDir(), 1024 * 1024); // 1MB cap

        // Set up the network to use HttpURLConnection as the HTTP client.
        Network network = new BasicNetwork(new HurlStack());

        // Instantiate the RequestQueue with the cache and network.
        requestQueue = new RequestQueue(cache, network);

        // Start the queue
        requestQueue.start();

        String theUrl ="http://35.197.153.192:3000/tour/set-stop-points";

        JSONObject req = new JSONObject();
        try {
            Intent intent = getIntent();
            req.put("tourId", tourId);
            req.put("stopPoints", (Object) stopPointsArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, theUrl, req, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Toast.makeText(getApplicationContext(), "Update Success!", Toast.LENGTH_LONG).show();
                overridePendingTransition(0, 0);
                Intent intent = new Intent(getApplicationContext(), showListStopPointsActivity.class);
                intent.putExtra("isAccTour",true);
                intent.putExtra("tourId",tourId);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Update failed!", Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                SharedPreferences sf = getSharedPreferences("com.maihuythong.testlogin_preferences", MODE_PRIVATE);
                String tok = sf.getString("login_access_token", "");
                params.put("Content-Type", "application/json");
//                        params.put("Authorization", "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiI2OSIsInBob25lIjoiMDgzMzUyNzQ1MCIsImVtYWlsIjoibWFpaHV5dGhvbmd4QGdtYWlsLmNvbSIsImV4cCI6MTU3NTk2OTE0NzMxNCwiYWNjb3VudCI6InVzZXIiLCJpYXQiOjE1NzMzNzcxNDd9.0WdtlhBt-5NHzKGRvtKKnoxhoM0vn3_0p4dJfkNRBjA");
                params.put("Authorization", tok);
                return params;
            }
        };
        requestQueue.add(jsonObjectRequest);

    }

    //======================================API update==================================

    private String GetTokenLoginAccess(){
        SharedPreferences sf;
        String token;
        token = LoginActivity.token;
        if(token== null){
            sf = getSharedPreferences("com.maihuythong.testlogin_preferences", MODE_PRIVATE);
            token = sf.getString("login_access_token", "");
        }
        return token;
    }

    void hideInputKeyboard(){
        try {
            InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
