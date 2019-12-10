package com.maihuythong.testlogin.showAccountTours;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.maihuythong.testlogin.LoginActivity;
import com.maihuythong.testlogin.R;
import com.maihuythong.testlogin.ShowListUsers.ListUsersActivity;
import com.maihuythong.testlogin.showTourInfo.ShowTourInfo;
import com.maihuythong.testlogin.showlist.Tour;
import com.maihuythong.testlogin.signup.APIService;
import com.maihuythong.testlogin.signup.ApiUtils;
import com.maihuythong.testlogin.updateTour.UpdateTour;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShowAccountToursActivity extends AppCompatActivity {
    private Tour[] t;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_account_tours);
        final ArrayList<Tour> arrAccTours = new ArrayList<>();


        String s;
        s = LoginActivity.token;
        if(s == null){
            SharedPreferences sf = getSharedPreferences("com.maihuythong.testlogin_preferences", MODE_PRIVATE);
            s = sf.getString("login_access_token", "");
        }

        APIService mAPIService = ApiUtils.getAPIService();

        mAPIService.getAccountTours(s,1, 50).enqueue(new Callback<ShowAccountToursReq>() {
            @Override
            public void onResponse(Call<ShowAccountToursReq> call, Response<ShowAccountToursReq> response) {
                if(response.code() == 200){
                    t = response.body().getTours();
                    ListView lvTour = findViewById(R.id.lv_tour);
                    final ArrayList<Tour> arrTour = new ArrayList<>();

                    for(int i = 0; i<t.length; i++){
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
                            }).setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }).create();
                            alertDialog.getWindow().setLayout(600, 400);
                            alertDialog.show();

                            // Get screen width and height in pixels
                            DisplayMetrics displayMetrics = new DisplayMetrics();
                            getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                            // The absolute width of the available display size in pixels.
                            int displayWidth = displayMetrics.widthPixels;
                            // The absolute height of the available display size in pixels.
                            int displayHeight = displayMetrics.heightPixels;

                            // Initialize a new window manager layout parameters
                            WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();

                            // Copy the alert dialog window attributes to new layout parameter instance
                            layoutParams.copyFrom(alertDialog.getWindow().getAttributes());

                            // Set alert dialog width equal to screen width 80%
                            int dialogWindowWidth = (int) (displayWidth * 0.8f);
                            // Set alert dialog height equal to screen height 14%
                            int dialogWindowHeight = (int) (displayHeight * 0.14f);

                            // Set the width and height for the layout parameters
                            // This will bet the width and height of alert dialog
                            layoutParams.width = dialogWindowWidth;
                            layoutParams.height = dialogWindowHeight;

                            // Apply the newly created layout parameters to the alert dialog window
                            alertDialog.getWindow().setAttributes(layoutParams);
                            return true;
                        }
                    });
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
        Intent intent = new Intent(ShowAccountToursActivity.this, ShowTourInfo.class);
        intent.putExtra("id",tour.getID());
        startActivity(intent);
    }
}
