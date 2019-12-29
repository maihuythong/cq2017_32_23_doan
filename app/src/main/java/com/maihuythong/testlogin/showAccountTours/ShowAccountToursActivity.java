package com.maihuythong.testlogin.showAccountTours;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.maihuythong.testlogin.LoginActivity;
import com.maihuythong.testlogin.R;
import com.maihuythong.testlogin.ShowListUsers.ListUsersActivity;
import com.maihuythong.testlogin.showTourInfo.ShowTourInfo;
import com.maihuythong.testlogin.showTourInfomation.ShowTourInformation;
import com.maihuythong.testlogin.showlist.Tour;
import com.maihuythong.testlogin.signup.APIService;
import com.maihuythong.testlogin.signup.ApiUtils;
import com.maihuythong.testlogin.updateTour.UpdateTour;
import com.maihuythong.testlogin.updateTour.UpdateTourReq;
import com.maihuythong.testlogin.userInfo.InputVerifyCodeActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShowAccountToursActivity extends AppCompatActivity implements SearchView.OnQueryTextListener{
    private Tour[] t;
    private long totalAccTours;
    private ListView lvTour;
    ArrayList<Tour> arrTour = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_account_tours);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        lvTour = findViewById(R.id.lv_tour);

        new LoadAccTourAsyncTask(this).execute();

    }

    private class LoadAccTourAsyncTask extends AsyncTask<Void, Void, Void>{

        private ProgressDialog dialog;

        LoadAccTourAsyncTask(ShowAccountToursActivity activity) {
            dialog = new ProgressDialog(activity);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setMessage("Loading tours, please wait....");
            dialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            String s;
            s = LoginActivity.token;
            if(s == null){
                SharedPreferences sf = getSharedPreferences("com.maihuythong.testlogin_preferences", MODE_PRIVATE);
                s = sf.getString("login_access_token", "");
            }

            APIService mAPIService = ApiUtils.getAPIService();

            mAPIService.getAccountTours(s,1, 200).enqueue(new Callback<ShowAccountToursReq>() {
                @Override
                public void onResponse(Call<ShowAccountToursReq> call, Response<ShowAccountToursReq> response) {
                    if(response.code() == 200){
                        t = response.body().getTours();
                        totalAccTours = response.body().getTotal();

                        for(int i= t.length-1; i>=0; i--){
                            if(t[i].getStatus()!=-1)
                                arrTour.add(t[i]);
                        }

                        CustomAdapterAccountTours customAdaperAccountTours =
                                new CustomAdapterAccountTours(ShowAccountToursActivity.this,R.layout.row_listview_account_tours,arrTour);
                        lvTour.setAdapter(customAdaperAccountTours);

                        lvTour.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                sendTourInfo(arrTour, position);
                            }
                        });

                        lvTour.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                            @Override
                            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

                                AlertDialog alertDialog = new AlertDialog.Builder(ShowAccountToursActivity.this).setNegativeButton
                                        ("Update", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                UpdateTour(arrTour,position);
                                            }
                                        }).setNeutralButton("Invite", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        SendInvitation(arrTour,position);
                                    }
                                }).setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        deleteTour(arrTour,position);
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

                        dialog.dismiss();
                    }
                    else{
                        //TODO
                    }
                }

                @Override
                public void onFailure(Call<ShowAccountToursReq> call, Throwable throwable) {
                    //TODO
                }
            });
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // do UI work here
            super.onPostExecute(result);
        }
    }

    private  void UpdateTour(ArrayList<Tour> arrTour,int position){
        Tour tt;
        tt = arrTour.get(position);
        Intent intent = new Intent(ShowAccountToursActivity.this, UpdateTour.class);
        intent.putExtra("pos", position);
        intent.putExtra("id", tt.getID());
        intent.putExtra("status", tt.getStatus());
        intent.putExtra("name", tt.getName());
        intent.putExtra("minCost", tt.getMinCost());
        intent.putExtra("maxCost", tt.getMaxCost());
        intent.putExtra("startDate", tt.getStartDate());
        intent.putExtra("endDate", tt.getEndDate());
        intent.putExtra("adult", tt.getAdults());
        intent.putExtra("child", tt.getChilds());
        intent.putExtra("isPrivate", tt.getIsPrivate());
        intent.putExtra("avatar", tt.getAvatar());
        startActivity(intent);
    }

    private void deleteTour(ArrayList<Tour> arrTour,int position){

        String s;
        s = LoginActivity.token;
        if(s == null){
            SharedPreferences sf = getSharedPreferences("com.maihuythong.testlogin_preferences", MODE_PRIVATE);
            s = sf.getString("login_access_token", "");
        }

        Tour tour;
        tour = arrTour.get(position);

        APIService mApiService = ApiUtils.getAPIService();

        mApiService.DeleteTour(s,tour.getID(),-1).enqueue(new Callback<UpdateTourReq>() {
            @Override
            public void onResponse(Call<UpdateTourReq> call, Response<UpdateTourReq> response) {
                if(response.code()==200)
                {
                    Toast.makeText(getApplicationContext(),"Delete tour success!", Toast.LENGTH_LONG).show();
                    finish();
                    overridePendingTransition(0, 0);
                    startActivity(getIntent().setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    overridePendingTransition(0, 0);
                }else {
                    Toast.makeText(getApplicationContext(),"Delete failed!", Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<UpdateTourReq> call, Throwable t) {
            }
        });

    }

    //Send invitation
    private  void SendInvitation(ArrayList<Tour> arrTour,int position){
        Tour tt;
        tt = arrTour.get(position);
        Intent intent = new Intent(ShowAccountToursActivity.this, ListUsersActivity.class);
        intent.putExtra("tourId",tt.getID());
        startActivity(intent);
    }

    private  void sendTourInfo(ArrayList<Tour> arrTour,int position){
        Tour tour;
        tour = arrTour.get(position);
        Intent intent = new Intent(ShowAccountToursActivity.this, ShowTourInformation.class);
        intent.putExtra("id",tour.getID());
        startActivity(intent);
    }


    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search_user, menu);

        MenuItem menuItem= menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setOnQueryTextListener(this);

        EditText textSearch =(EditText)searchView.findViewById(androidx.appcompat.R.id.search_src_text);
        textSearch.setTextColor(Color.WHITE);

        textSearch.setHint("Enter tour name to search");
        textSearch.setHintTextColor(ContextCompat.getColor(getApplicationContext(), R.color.friend_serch_hint_text_color));

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
        String userInput = query.toString();
        arrTour = new ArrayList<>();

        if(userInput.isEmpty())
        {
            arrTour.addAll(Arrays.asList(t));

        }else{
            for (int i =0;i < totalAccTours;i++){
                if(!Objects.isNull(t[i].getName()))
                    if(t[i].getName().contains(userInput))
                        arrTour.add(t[i]);
                if(Long.toString(t[i].getID()).contains(userInput))
                    arrTour.add(t[i]);
            }
        }

            CustomAdapterAccountTours customAdaperAccountTours =
                    new CustomAdapterAccountTours(ShowAccountToursActivity.this,R.layout.row_listview_account_tours,arrTour);
            lvTour.setAdapter(customAdaperAccountTours);

        return true;

    }

    @Override
    public boolean onQueryTextChange(String newText) {
        String userInput = newText.toString();
        arrTour = new ArrayList<>();

        if(userInput.isEmpty())
        {
            arrTour.addAll(Arrays.asList(t));

        }else{
            for (int i =0;i < totalAccTours;i++){
                if(!Objects.isNull(t[i].getName()))
                    if(t[i].getName().contains(userInput))
                        arrTour.add(t[i]);
                if(Long.toString(t[i].getID()).contains(userInput))
                    arrTour.add(t[i]);
            }
        }

            CustomAdapterAccountTours customAdaperAccountTours =
                    new CustomAdapterAccountTours(ShowAccountToursActivity.this,R.layout.row_listview_account_tours,arrTour);
            lvTour.setAdapter(customAdaperAccountTours);


        return true;
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
