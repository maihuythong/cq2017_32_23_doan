package com.maihuythong.testlogin.showlist;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.maihuythong.testlogin.CreateTourActivity;
import com.maihuythong.testlogin.LoginActivity;
import com.maihuythong.testlogin.R;
import com.maihuythong.testlogin.ShowSystemTourInfo.ShowSystemTourInfo;
import com.maihuythong.testlogin.showListStopPointSystem.ListStopPointSystemActivity;
import com.maihuythong.testlogin.showTourInfomation.ShowTourInformation;
import com.maihuythong.testlogin.userInfo.UpdateUserInfoActivity;
import com.maihuythong.testlogin.userInfo.UserInfoActivity;
import com.maihuythong.testlogin.invitationTour.InvitationActivity;
import com.maihuythong.testlogin.rate_comment_review.RateCommentTour;
import com.maihuythong.testlogin.showAccountTours.ShowAccountToursActivity;
import com.maihuythong.testlogin.signup.APIService;
import com.maihuythong.testlogin.signup.ApiUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import android.provider.Settings.Secure;
import android.widget.ProgressBar;
import android.widget.TextView;

import android.widget.Toast;


public class ShowListActivity extends AppCompatActivity implements SearchView.OnQueryTextListener{
    private ListView lvTour;
    private Tour[] t;
    private SharedPreferences sf;
    private Toolbar toolbar;
    private TextView totalToursView;
    private TextView visibleItemView;
    private long totalTours;
    private final ArrayList<Tour> toursTotalArray = new ArrayList<>();
    private final ArrayList<Tour> toursVisitble = new ArrayList<>();

    private int visitbleItemCapacity = 10;


    private int currentPos=0;
    private boolean isLoading = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_list);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        totalToursView = findViewById(R.id.total_available_tours);
        visibleItemView = findViewById(R.id.item_visitble_capacity_list_tour);

        visibleItemView.setText(String.valueOf(visitbleItemCapacity));
        try {
            PackageInfo pInfo = getApplicationContext().getPackageManager().getPackageInfo(getPackageName(), 0);
            String version = pInfo.versionName;
            int verCode = pInfo.versionCode;
            Log.d("appVersion", verCode + "");
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        //init fab button
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddTour();
            }
        });

        new LoadTourAsyncTask(this).execute();

    }

    @SuppressLint("StaticFieldLeak")
    private class LoadTourAsyncTask extends AsyncTask<Void, Void, Void> {
        private ProgressDialog dialog;

        LoadTourAsyncTask(ShowListActivity activity) {
            dialog = new ProgressDialog(activity);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setMessage("Loading tours, please wait....");
            dialog.show();
        }
        @Override
        protected Void doInBackground(Void... args) {
            String s;
            s = LoginActivity.token;
            if(s == null){
                SharedPreferences sf = getSharedPreferences("com.maihuythong.testlogin_preferences", MODE_PRIVATE);
                s = sf.getString("login_access_token", "");
            }

            APIService mAPIService = ApiUtils.getAPIService();
            Intent intent = getIntent();

            mAPIService.getList(s,5000, 1).enqueue(new Callback<ShowListReq>() {
                @Override
                public void onResponse(Call<ShowListReq> call, Response<ShowListReq> response) {
                    if(response.code() == 200){
                        t = response.body().getTours();
                        totalTours =response.body().getTotal();
                        totalToursView.setText(Long.toString(totalTours));
                        lvTour = (ListView) findViewById(R.id.lv_tour);

                        for (int i =0 ; i< t.length;i++)
                            if(t[i].getStatus()!=-1) toursTotalArray.add(t[i]);

                        LoadData();

                        lvTour.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                openRateCommentTour(toursVisitble, position);
                            }
                        });
                        Toast.makeText(ShowListActivity.this,"Get tours finished!", Toast.LENGTH_LONG).show();
                        dialog.dismiss();
                    }
                    else{
                        //TODO
                    }
                }

                @Override
                public void onFailure(Call<ShowListReq> call, Throwable throwable) {
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


    private void AddTour() {
        startActivityForResult(new Intent(this, CreateTourActivity.class), 111);
    }

    private  void openRateCommentTour(ArrayList<Tour> arrTour,int position){
        Tour tt;
        tt = arrTour.get(position);
        Intent intent = new Intent(ShowListActivity.this, ShowSystemTourInfo.class);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 111 && resultCode == RESULT_OK){
            finish();
            overridePendingTransition(0, 0);
            startActivity(getIntent().setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            overridePendingTransition(0, 0);
        }
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
    public void onBackPressed() {
    }

    @Override
    public boolean onQueryTextSubmit(String query) {

        visibleItemView.setText(String.valueOf(visitbleItemCapacity));
        toursVisitble.clear();
        toursTotalArray.clear();
        String userInput = query.toString();

        if(userInput.isEmpty())
        {
            for (int i =0 ; i< t.length;i++)
                if(t[i].getStatus()!=-1) toursTotalArray.add(t[i]);
        }else{
            for (int i =0;i < totalTours;i++){
                if(t[i].getStatus()!=-1) {
                    if (!Objects.isNull(t[i].getName()))
                        if (t[i].getName().contains(userInput))
                            toursTotalArray.add(t[i]);
                    if (Long.toString(t[i].getID()).contains(userInput))
                        toursTotalArray.add(t[i]);
                }
            }
        }


        LoadData();

        if(!toursTotalArray.isEmpty()) {
            lvTour.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    openRateCommentTour(toursTotalArray, position);
                }
            });
        }
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        visibleItemView.setText(String.valueOf(visitbleItemCapacity));
        toursVisitble.clear();
        toursTotalArray.clear();
        String userInput = newText.toString();

        if(userInput.isEmpty())
        {
            for (int i =0 ; i< t.length;i++)
                if(t[i].getStatus()!=-1) toursTotalArray.add(t[i]);
        }else{
            for (int i =0;i < totalTours;i++){
                if(t[i].getStatus()!=-1) {
                    if (!Objects.isNull(t[i].getName()))
                        if (t[i].getName().contains(userInput))
                            toursTotalArray.add(t[i]);
                    if (Long.toString(t[i].getID()).contains(userInput))
                        toursTotalArray.add(t[i]);
                }
            }
        }


        LoadData();

        if(!toursTotalArray.isEmpty()) {
            lvTour.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    openRateCommentTour(toursTotalArray, position);
                }
            });
        }
        return true;
    }

     private void LoadData(){

        if(toursTotalArray.size()<visitbleItemCapacity)
        {
            toursVisitble.addAll(toursTotalArray);
            currentPos=toursVisitble.size()-1;

        }else {

            for (int i = 0; i < visitbleItemCapacity; i++) {
                toursVisitble.add(toursTotalArray.get(i));
                currentPos = i;
            }
        }
        CustomAdapter customAdaper = new CustomAdapter(getApplicationContext(),R.layout.row_listview,toursVisitble);
        lvTour.setAdapter(customAdaper);

        lvTour.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (lvTour.getAdapter() == null)
                    return ;

                if (lvTour.getAdapter().getCount() == 0)
                    return ;
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                int l = visibleItemCount + firstVisibleItem;
                if (l >= totalItemCount && !isLoading) {
                    // It is time to add new data. We call the listener
                    isLoading = true;
                    loadMoreData();
                }
            }
        });

    }

    private void loadMoreData(){
        if(toursTotalArray.size()<toursVisitble.size()) return;

        if(toursTotalArray.size()<= currentPos + visitbleItemCapacity)
        {
            toursVisitble.addAll(toursTotalArray);
        }else{
            for (int i=currentPos;i<currentPos + visitbleItemCapacity;i++){
                toursVisitble.add(toursTotalArray.get(i));
            }
        }

        currentPos = toursVisitble.size()-1;
        CustomAdapter customAdaper = new CustomAdapter(getApplicationContext(),R.layout.row_listview,toursVisitble);
        lvTour.setAdapter(customAdaper);
        lvTour.setSelection(currentPos-visitbleItemCapacity);
        isLoading=false;
        visibleItemView.setText(String.valueOf(toursVisitble.size()));
    }
}
