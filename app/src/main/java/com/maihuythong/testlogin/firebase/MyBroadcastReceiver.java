package com.maihuythong.testlogin.firebase;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationManagerCompat;

import com.maihuythong.testlogin.invitationTour.Invitation;
import com.maihuythong.testlogin.invitationTour.responseInvitation;
import com.maihuythong.testlogin.signup.APIService;
import com.maihuythong.testlogin.signup.ApiUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyBroadcastReceiver extends BroadcastReceiver {
    private static final String TAG = "MyBroadcastReceiver";
    @Override
    public void onReceive(final Context context, Intent intent) {
        if (!TextUtils.isEmpty(intent.getAction()) && intent.getAction().equals("CONFIRM")) {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            String token = preferences.getString("login_access_token", null);
            int tourId = Integer.parseInt(intent.getStringExtra("tourId"));
            APIService mAPIService = ApiUtils.getAPIService();
            mAPIService.responseInvitation(token, tourId, true).enqueue(new Callback<responseInvitation>() {
                @Override
                public void onResponse(Call<responseInvitation> call, Response<responseInvitation> response) {
                   // Toast.makeText(getContext(),"Confirm Click success", Toast.LENGTH_SHORT).show();
                    Log.d("CONFIRMMMMMMMMMMMMMMMMMMMMMMm","Click");
                    NotificationManager notificationManager = (NotificationManager)
                            context.getSystemService(Context.
                                    NOTIFICATION_SERVICE);
                    notificationManager.cancelAll();

                    Intent it = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
                    context.sendBroadcast(it);
                }

                @Override
                public void onFailure(Call<responseInvitation> call, Throwable t) {
                    Log.d("CONFIRM ERRORRRRRRRRRRRRrr", "Click");
                }
            });
        }

        if (!TextUtils.isEmpty(intent.getAction()) && intent.getAction().equals("DELETE")) {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            String token = preferences.getString("login_access_token", null);
            int tourId = Integer.parseInt(intent.getStringExtra("tourId"));
            APIService mAPIService = ApiUtils.getAPIService();
            mAPIService.responseInvitation(token, tourId, false).enqueue(new Callback<responseInvitation>() {
                @Override
                public void onResponse(Call<responseInvitation> call, Response<responseInvitation> response) {
                    Log.d("DELETEEEEEEEEEEEEEEEEE","Click");
                    NotificationManager notificationManager = (NotificationManager)
                            context.getSystemService(Context.
                                    NOTIFICATION_SERVICE);
                    notificationManager.cancelAll();

                    Intent it = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
                    context.sendBroadcast(it);
                }

                @Override
                public void onFailure(Call<responseInvitation> call, Throwable t) {

                }
            });
        }
    }
}
