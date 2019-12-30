package com.maihuythong.testlogin.showListStopPoints;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.maihuythong.testlogin.LoginActivity;
import com.maihuythong.testlogin.R;
import com.maihuythong.testlogin.ShowListUsers.SendInvationRes;
import com.maihuythong.testlogin.forgotPassword.RecoveryPasswordActivity;
import com.maihuythong.testlogin.showTourInfo.GetTourInfo;
import com.maihuythong.testlogin.showTourInfo.StopPoint;
import com.maihuythong.testlogin.signup.APIService;
import com.maihuythong.testlogin.signup.ApiUtils;
import com.maihuythong.testlogin.stopPointInfo.StopPointDetailActivity;
import com.maihuythong.testlogin.stopPointInfo.UpdateStopPointActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class showListStopPointsActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    StopPoint[] arrStopPoints;
    long tourId;
    ListView lvStopPoints;
    private Toolbar toolbar;

    private boolean isAccTour=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_list_stop_points);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        lvStopPoints = (ListView)findViewById(R.id.lv_stop_points);

        Intent intent = getIntent();
        tourId = intent.getLongExtra("tourId",0);
        isAccTour= intent.getBooleanExtra("isAccTour",false);
        GetStopPoints(tourId);

    }


    private void GetStopPoints(final long tourId){
        String token = GetTokenLoginAccess();
        APIService mAPIService = ApiUtils.getAPIService();
        mAPIService.getTourInfo(token,tourId).enqueue(new Callback<GetTourInfo>() {
            @Override
            public void onResponse(Call<GetTourInfo> call, Response<GetTourInfo> response) {
                if(response.code()==200) {
                    GetTourInfo tourInfo = response.body();

                    arrStopPoints = tourInfo.getStopPoints();

                    final ArrayList<StopPoint> arrayStopPoints = new ArrayList<>(Arrays.asList(arrStopPoints));

                    StopPointAdapter stopPointAdapter = new StopPointAdapter(showListStopPointsActivity.this,R.layout.stop_point_card,arrayStopPoints);
                    lvStopPoints.setAdapter(stopPointAdapter);

                    lvStopPoints.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            showInfoStopPoint(arrayStopPoints,position);
                        }
                    });

                    if(isAccTour) setLongClick();

                    Toast.makeText(showListStopPointsActivity.this,"Get stop points finished!", Toast.LENGTH_LONG).show();
                }

                if(response.code()==404||response.code()==500)
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
                    Toast.makeText(showListStopPointsActivity.this, "Error: " + message, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<GetTourInfo> call, Throwable t) {
                Toast.makeText(showListStopPointsActivity.this, "Error: Can't get stop points! ", Toast.LENGTH_LONG).show();

            }
        });

    }

    private void showInfoStopPoint(ArrayList<StopPoint> arrayList,int position){
        StopPoint newStopPoint;
        newStopPoint = arrayList.get(position);

        Intent intent = new Intent(showListStopPointsActivity.this, StopPointDetailActivity.class);
        intent.putExtra("id",newStopPoint.getId());
        intent.putExtra("serviceId",newStopPoint.getServiceId());
        intent.putExtra("address",newStopPoint.getAddress());
        intent.putExtra("provinceId",newStopPoint.getProvinceId());
        intent.putExtra("name",newStopPoint.getName());
        intent.putExtra("lat",newStopPoint.getLat().doubleValue());
        intent.putExtra("long",newStopPoint.getLong().doubleValue());
        intent.putExtra("arrivalAt",newStopPoint.getArrivalAt());
        intent.putExtra("leaveAt",newStopPoint.getLeaveAt());
        intent.putExtra("minCost",newStopPoint.getMinCost());
        intent.putExtra("maxCost",newStopPoint.getMaxCost());
        intent.putExtra("serviceTypeId",newStopPoint.getServiceTypeId());
        intent.putExtra("avatar",newStopPoint.getAvatar());
        startActivity(intent);
    }

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

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search_user, menu);

        MenuItem menuItem= menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setOnQueryTextListener(this);

        EditText textSearch =(EditText)searchView.findViewById(androidx.appcompat.R.id.search_src_text);
        textSearch.setTextColor(Color.WHITE);

        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        String userInput = newText.toString();
        ArrayList<StopPoint> arrayStopPoints;
        arrayStopPoints = new ArrayList<>();

        if(userInput.isEmpty())
        {
            arrayStopPoints.addAll(Arrays.asList(arrStopPoints));

        }else{
            for (int i =0;i < arrStopPoints.length;i++){
                if(!Objects.isNull(arrStopPoints[i].getName()))
                    if(arrStopPoints[i].getName().contains(userInput))
                        arrayStopPoints.add(arrStopPoints[i]);
                if(Long.toString(arrStopPoints[i].getId()).contains(userInput))
                    arrayStopPoints.add(arrStopPoints[i]);
            }
        }

        StopPointAdapter stopPointAdapter = new StopPointAdapter(showListStopPointsActivity.this,R.layout.stop_point_card,arrayStopPoints);
        lvStopPoints.setAdapter(stopPointAdapter);


        return true;
    }

    private void setLongClick(){

        lvStopPoints.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

                AlertDialog alertDialog = new AlertDialog.Builder(showListStopPointsActivity.this).setNegativeButton
                        ("Update", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                UpdateStopPoint(position);
                            }
                        }).setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).setNeutralButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DeleteStopPoint(position);
                    }
                }).create();
                alertDialog.show();

                Button btnPositive = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                Button btnNegative = alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE);
                Button btnNeutral = alertDialog.getButton(AlertDialog.BUTTON_NEUTRAL);

                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) btnPositive.getLayoutParams();
                layoutParams.weight = 10;
                btnPositive.setLayoutParams(layoutParams);
                btnNegative.setLayoutParams(layoutParams);
                btnNeutral.setLayoutParams(layoutParams);
                return true;
            }
        });
    }


    private void DeleteStopPoint(int position){
        final ArrayList<StopPoint> arrayStopPoints = new ArrayList<>(Arrays.asList(arrStopPoints));
        StopPoint newStopPoint;
        newStopPoint = arrayStopPoints.get(position);
        String token = GetTokenLoginAccess();
        APIService mAPIService = ApiUtils.getAPIService();

        mAPIService.DeleteStopPoint(token,String.valueOf(newStopPoint.getId())).enqueue(new Callback<SendInvationRes>() {
            @Override
            public void onResponse(Call<SendInvationRes> call, Response<SendInvationRes> response) {

                if(response.code()==200){

                    Toast.makeText(getApplicationContext(),"Delete suscess", Toast.LENGTH_LONG).show();
                    finish();
                    overridePendingTransition(0, 0);
                    startActivity(getIntent().setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    overridePendingTransition(0, 0);
                }

                if(response.code()==400||response.code()==404||response.code()==403 || response.code()==500) {
                    String message = "Send failed!";
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        message = jObjError.getString("message");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(getApplicationContext(),"Error: "+ message, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<SendInvationRes> call, Throwable t) {

            }
        });


    }
    private void UpdateStopPoint(int position){

        final ArrayList<StopPoint> arrayStopPoints = new ArrayList<>(Arrays.asList(arrStopPoints));
        StopPoint newStopPoint;
        newStopPoint = arrayStopPoints.get(position);

        Intent intent = new Intent(getApplicationContext(), UpdateStopPointActivity.class);
        intent.putExtra("address",newStopPoint.getAddress());
        intent.putExtra("provinceId",newStopPoint.getProvinceId());
        intent.putExtra("name",newStopPoint.getName());
        intent.putExtra("arrivalAt",newStopPoint.getArrivalAt());
        intent.putExtra("lat",newStopPoint.getLat());
        intent.putExtra("long",newStopPoint.getLong());
        intent.putExtra("leaveAt",newStopPoint.getLeaveAt());
        intent.putExtra("minCost",newStopPoint.getMinCost());
        intent.putExtra("maxCost",newStopPoint.getMaxCost());
        intent.putExtra("id",newStopPoint.getId());
        intent.putExtra("serviceTypeId",newStopPoint.getServiceTypeId());
        intent.putExtra("tourId",tourId);
        startActivity(intent);
    }
}
