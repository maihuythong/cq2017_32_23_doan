package com.maihuythong.testlogin.showListStopPointSystem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.maihuythong.testlogin.LoginActivity;
import com.maihuythong.testlogin.R;
import com.maihuythong.testlogin.showListStopPoints.StopPointAdapter;
import com.maihuythong.testlogin.showListStopPoints.showListStopPointsActivity;
import com.maihuythong.testlogin.showTourInfo.StopPoint;
import com.maihuythong.testlogin.signup.APIService;
import com.maihuythong.testlogin.signup.ApiUtils;
import com.maihuythong.testlogin.stopPointInfo.StopPointDetailActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListStopPointSystemActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    StopPoint[] arrStopPoints;
    ListView lvStopPoints;

    private TextView totalStopPointView;
    private long totalStopPoint;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_stop_point_system);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        lvStopPoints = (ListView)findViewById(R.id.lv_stop_point_system);
        totalStopPointView = findViewById(R.id.total_stop_point_system);
        new LoadStopPointSystemAsyncTask(this).execute();
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

        if(!arrayStopPoints.isEmpty()) {
            StopPointAdapter stopPointAdapter = new StopPointAdapter(getApplicationContext(),R.layout.stop_point_card,arrayStopPoints);
            lvStopPoints.setAdapter(stopPointAdapter);
            lvStopPoints.setAdapter(stopPointAdapter);
        }

        return true;
    }


    private class LoadStopPointSystemAsyncTask extends AsyncTask<Void, Void, Void>{

        private ProgressDialog dialog;

        LoadStopPointSystemAsyncTask(ListStopPointSystemActivity activity) {
            dialog = new ProgressDialog(activity);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setMessage("Loading stop points, please wait....");
            dialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            getStopPointSystem();
            dialog.dismiss();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // do UI work here
            super.onPostExecute(result);
        }
    }

    private void getStopPointSystem(){
        String token = GetTokenLoginAccess();
        APIService mAPIService = ApiUtils.getAPIService();
        mAPIService.searchDestination(token,"",null,null,1,2000).enqueue(new Callback<StopPointSystemRes>() {
            @Override
            public void onResponse(Call<StopPointSystemRes> call, Response<StopPointSystemRes> response) {
                if(response.code()==200)
                {

                    assert response.body() != null;
                    arrStopPoints=response.body().getStopPoints();
                    totalStopPoint=response.body().getTotal();

                    totalStopPointView.setText(String.valueOf(totalStopPoint));
                    final ArrayList<StopPoint> arrayStopPoints = new ArrayList<>(Arrays.asList(arrStopPoints));
                    StopPointAdapter stopPointAdapter = new StopPointAdapter(getApplicationContext(),R.layout.stop_point_card,arrayStopPoints);
                    lvStopPoints.setAdapter(stopPointAdapter);
                    lvStopPoints.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            showInfoStopPoint(arrayStopPoints,position);
                        }
                    });

                    Toast.makeText(getApplicationContext(),"Get stop points finished!", Toast.LENGTH_LONG).show();
                }
                if(response.code()==500)
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
                    Toast.makeText(getApplicationContext(), "Error: " + message, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<StopPointSystemRes> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Error: failed", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void showInfoStopPoint(ArrayList<StopPoint> arrayList,int position){
        StopPoint newStopPoint;
        newStopPoint = arrayList.get(position);
        Intent intent = new Intent(getApplicationContext(), StopPointDetailActivity.class);
        intent.putExtra("id",newStopPoint.getId());

        intent.putExtra("serviceId","");
        intent.putExtra("address",newStopPoint.getAddress());
        intent.putExtra("provinceId",newStopPoint.getProvinceId());
        intent.putExtra("name",newStopPoint.getName());
        intent.putExtra("lat",newStopPoint.getLat().doubleValue());
        intent.putExtra("long",newStopPoint.getLong().doubleValue());
        intent.putExtra("arrivalAt","");
        intent.putExtra("leaveAt","");
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
}
