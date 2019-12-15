package com.maihuythong.testlogin.firebase;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.maihuythong.testlogin.LoginActivity;
import com.maihuythong.testlogin.MainActivity;
import com.maihuythong.testlogin.R;
import com.maihuythong.testlogin.invitationTour.Invitation;
import com.maihuythong.testlogin.signup.APIService;
import com.maihuythong.testlogin.signup.ApiUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyFirebaseService extends FirebaseMessagingService {
    private static final String TAG = "MyFirebaseService";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // handle a notification payload.
        Log.d(TAG,remoteMessage.getData().toString());
        sendNotification(remoteMessage.getData().get("id"), remoteMessage.getData().get("name"), remoteMessage.getData().get("hostName"));
    }

    @Override
    public void onNewToken(String token) {
        Log.d(TAG, "Refreshed token: " + token);

        sendRegistrationToServer(token);
    }

    private void sendRegistrationToServer(String token) {

//        deviceId = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
//
//        String tk = LoginActivity.token;
//        if(tk == null){
//            sf = getSharedPreferences("com.maihuythong.testlogin_preferences", MODE_PRIVATE);
//            tk = sf.getString("login_access_token", "");
//        }
//
//        tk = "eyJ1c2VySWQiOiI2OSIsInBob25lIjoiMDgzMzUyNzQ1MCIsImVtYWlsIjoibWFpaHV5dGhvbmd4QGdtYWlsLmNvbSIsImV4cCI6MTU3ODgwMTk1NTc0MSwiYWNjb3VudCI6InVzZXIiLCJpYXQiOjE1NzYyMDk5NTV9.Xww9S5CVb91S62dP84Jr5YLLjkASGgccz6elFGz_4Ic";
//        APIService mAPIService = ApiUtils.getAPIService();
//        mAPIService.putTokenFirebase(tk, token, deviceId, 1, "1.0").enqueue(new Callback<PutTokenFirebase>() {
//            @Override
//            public void onResponse(Call<PutTokenFirebase> call, Response<PutTokenFirebase> response) {
//                Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onFailure(Call<PutTokenFirebase> call, Throwable t) {
//                Log.d(TAG, t.toString());
//            }
//        });

    }

    private void sendNotification(String id, String tourName, String hostName) {
        Intent intent = new Intent(this, Invitation.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        Intent btConfirm = new Intent(this, MyBroadcastReceiver.class);
        btConfirm.putExtra("tourId", String.valueOf(id));
        btConfirm.setAction("CONFIRM");
        PendingIntent confirm = PendingIntent.getBroadcast(this, 0, btConfirm, PendingIntent.FLAG_ONE_SHOT);

        Intent btDelete = new Intent(this, MyBroadcastReceiver.class);
        btDelete.putExtra("tourId", String.valueOf(id));
        btDelete.setAction("DELETE");
        PendingIntent delete = PendingIntent.getBroadcast(this, 0, btDelete, PendingIntent.FLAG_UPDATE_CURRENT);

        String channelId = getString(R.string.project_id);

        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.drawable.ic_launcher_background)
                        .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher_background))
                        .setContentTitle("Tour Invitation")
                        .setContentText(hostName + " invites you to tour " + tourName)
                        .setContentIntent(pendingIntent)
                        .setColor(getResources().getColor(R.color.colorPrimary))
                        .setDefaults(NotificationCompat.DEFAULT_ALL)
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .addAction(R.drawable.ic_confirm,"Confirm",confirm)
                        .addAction(R.drawable.ic_delete, "Delete", delete)
                        .setAutoCancel(true);
        notificationBuilder.build().flags |= Notification.FLAG_AUTO_CANCEL;

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_HIGH);

            notificationManager.createNotificationChannel(channel);
        }


        notificationManager.notify(0, notificationBuilder.build());
    }
}
