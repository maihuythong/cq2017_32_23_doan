package com.maihuythong.testlogin.firebase;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.preference.PreferenceManager;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.app.RemoteInput;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.maihuythong.testlogin.R;
import com.maihuythong.testlogin.ShowListUsers.User;
import com.maihuythong.testlogin.ShowListUsers.UserReq;
import com.maihuythong.testlogin.TourCoordinate.MapStartTour;
import com.maihuythong.testlogin.TourCoordinate.noti;
import com.maihuythong.testlogin.invitationTour.InvitationActivity;
import com.maihuythong.testlogin.signup.APIService;
import com.maihuythong.testlogin.signup.ApiUtils;
import com.maihuythong.testlogin.userInfo.UserInfoRes;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyFirebaseService extends FirebaseMessagingService {
    private static final String TAG = "MyFirebaseService";
    public User[] users;
    private long userId;

    @Override
    public void onMessageReceived(final RemoteMessage remoteMessage) {
        // handle a notification payload.
        Log.d(TAG, remoteMessage.getData().toString());
        switch (remoteMessage.getData().get("type")){
            case "3":
                sendNotificationSpeedLimit(remoteMessage.getData().get("tourId"), remoteMessage.getData().get("userId"), remoteMessage.getData().get("lat"),
                        remoteMessage.getData().get("long"), remoteMessage.getData().get("note"));
                break;
            case "6":
                sendNotificationInvitation(remoteMessage.getData().get("id"), remoteMessage.getData().get("name"), remoteMessage.getData().get("hostName"));
                break;
            case "4":
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                String token = preferences.getString("login_access_token", null);
                APIService service = ApiUtils.getAPIService();
                service.getUserInfo(token).enqueue(new Callback<UserInfoRes>() {
                    @Override
                    public void onResponse(Call<UserInfoRes> call, Response<UserInfoRes> response) {
                        UserInfoRes UserInfo = response.body();
                        userId = UserInfo.getID();
                        if (userId != Long.valueOf(remoteMessage.getData().get("userId")))
                        {
                            sendNotificationMessage(remoteMessage.getData().get("userId"), remoteMessage.getData().get("notification"), remoteMessage.getData().get("tourId"));
                        }
                    }
                    @Override
                    public void onFailure(Call<UserInfoRes> call, Throwable t) {
                    }
                });
                break;
        }


    }

    private void sendNotificationSpeedLimit(String tourId, final String userId, final String latitude, final String longitude, String note) {
        Intent intent = new Intent(this, noti.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        final PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        final String channelId = getString(R.string.project_id);
        APIService mAPIService = ApiUtils.getAPIService();
        char c = note.charAt(0);
        boolean typeSpeed = true;
        String title = "", textContent = "";
        if (c == 'e'){
            typeSpeed = false;
            title = "End limit speed";
        }else {
            title = "Limit speed";
        }

        textContent = note.substring(1);
        final String finalTitle = title;
        final String finalTextContent = "Limit speed: " + textContent;
        final boolean finalTypeSpeed = typeSpeed;
        mAPIService.getListUsers("", 1, 2000).enqueue(new Callback<UserReq>() {
            @Override
            public void onResponse(Call<UserReq> call, Response<UserReq> response) {

                if (response.code() == 200) {
                    users = response.body().getUsers();
                }

                String userName = "";
                for (int i = 0; i < users.length; ++i){
                    if (users[i].getID() == Long.valueOf(userId)){
                        userName = users[i].getFullName();
                    }
                }

                if (!finalTypeSpeed){
                    Intent broadcastIntent = new Intent();
                    broadcastIntent.setAction("EndLimit");
                    broadcastIntent.putExtra("endLimitLocationLat", latitude);
                    broadcastIntent.putExtra("endLimitLocationLong", longitude);
                    broadcastIntent.putExtra("endLimitUserId", userId);
                    broadcastIntent.putExtra("endLimitNoti", finalTextContent);
                    broadcastIntent.putExtra("endLimitUserName", userName);
                    sendBroadcast(broadcastIntent);
                }else {
                    Intent broadcastIntent = new Intent();
                    broadcastIntent.setAction("StartLimit");
                    broadcastIntent.putExtra("startLimitLocationLat", latitude);
                    broadcastIntent.putExtra("startLimitLocationLong", longitude);
                    broadcastIntent.putExtra("startLimitUserId", userId);
                    broadcastIntent.putExtra("startLimitNoti", finalTextContent);
                    broadcastIntent.putExtra("startLimitUserName", userName);
                    sendBroadcast(broadcastIntent);
                }

                NotificationCompat.Builder notificationBuilder =
                        new NotificationCompat.Builder(getApplicationContext(), channelId)
                                .setSmallIcon(R.drawable.ic_launcher_background)
                                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher_background))
                                .setContentTitle(finalTitle)
                                .setContentText(userName + " : " + finalTextContent)
                                .setContentIntent(pendingIntent)
                                .setColor(getResources().getColor(R.color.colorPrimary))
                                .setDefaults(NotificationCompat.DEFAULT_ALL)
                                .setPriority(NotificationCompat.PRIORITY_HIGH)
                                .setGroup("ALERT")
                                .setGroupSummary(true)
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

            @Override
            public void onFailure(Call<UserReq> call, Throwable t) {

            }
        });

    }

    private void sendNotificationMessage(final String userId, final String notification, final String tourId) {
//        Intent btConfirm = new Intent(this, MyBroadcastReceiver.class);
//        btConfirm.putExtra("tourId", String.valueOf(id));
//        btConfirm.setAction("CONFIRM");
//        PendingIntent confirm = PendingIntent.getBroadcast(this, 0, btConfirm, PendingIntent.FLAG_ONE_SHOT);
//
//        Intent btDelete = new Intent(this, MyBroadcastReceiver.class);
//        btDelete.putExtra("tourId", String.valueOf(id));
//        btDelete.setAction("DELETE");
//        PendingIntent delete = PendingIntent.getBroadcast(this, 0, btDelete, PendingIntent.FLAG_UPDATE_CURRENT);


        APIService mAPIService = ApiUtils.getAPIService();
        mAPIService.getListUsers("", 1, 2000).enqueue(new Callback<UserReq>() {
            @Override
            public void onResponse(Call<UserReq> call, Response<UserReq> response) {

                if (response.code() == 200) {
                    users = response.body().getUsers();
                }

                String userName = "";
                for (int i = 0; i < users.length; ++i){
                    if (users[i].getID() == Integer.valueOf(userId)){
                        userName = users[i].getFullName();
                    }
                }
                String channelId = getString(R.string.project_id);

                Intent i = new Intent("broadCastName");
                // Data you need to pass to activity
                i.putExtra("message", notification);
                i.putExtra("uid", userId);

                getApplication().sendBroadcast(i);

                Intent intent = new Intent(getApplicationContext(), MessagingTour.class);
                intent.putExtra("tourId", tourId);
                intent.putExtra("userId", userId);
                intent.addCategory(Intent.CATEGORY_LAUNCHER);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);


                RemoteInput remoteInput = new RemoteInput.Builder("key_text_reply")
                        .setLabel("Type your answer...")
                        .build();
                Intent replyIntent = new Intent (getApplicationContext(), MyBroadcastReceiver.class);
                replyIntent.putExtra("tourId", tourId);
                replyIntent.putExtra("userId", userId);

                PendingIntent replyPendingIntent = PendingIntent.getBroadcast(getApplicationContext(),
                        0,replyIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                NotificationCompat.Action replyAction = new NotificationCompat.Action.Builder(
                        R.drawable.ic_send,
                        "REPLY",
                        replyPendingIntent
                ).addRemoteInput(remoteInput).build();

                NotificationCompat.Builder notificationBuilder =
                        new NotificationCompat.Builder(getApplicationContext(), channelId)
                                .setSmallIcon(R.drawable.ic_launcher_background)
                                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher_background))
                                .setContentTitle("Tour Message")
                                .setContentText(userName + " : " + notification)
                                .setContentIntent(pendingIntent)
                                .setColor(getResources().getColor(R.color.colorPrimary))
                                .setDefaults(NotificationCompat.DEFAULT_ALL)
                                .setPriority(NotificationCompat.PRIORITY_HIGH)
                                .setGroup("MESSAGE")
                                .addAction(replyAction)

                                .setGroupSummary(true)
                                .setAutoCancel(true);
                notificationBuilder.build().flags |= Notification.FLAG_AUTO_CANCEL;

                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());

                // Since android Oreo notification channel is needed.
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    NotificationChannel channel = new NotificationChannel(
                            channelId,
                            "Channel human readable title",
                            NotificationManager.IMPORTANCE_HIGH);

                    notificationManager.createNotificationChannel(channel);
                }

                notificationManager.notify(4, notificationBuilder.build());
            }
            @Override
            public void onFailure(Call<UserReq> call, Throwable t) {
            }

        });


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

    private void sendNotificationInvitation(String id, String tourName, String hostName) {
        int idOfNoti = (int) System.currentTimeMillis();
        Intent intent = new Intent(this, InvitationActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        Intent btConfirm = new Intent(this, MyBroadcastReceiver.class);
        btConfirm.putExtra("tourId", String.valueOf(id));
        btConfirm.putExtra("ifOfNoti", idOfNoti);
        btConfirm.setAction("CONFIRM");
        PendingIntent confirm = PendingIntent.getBroadcast(this, 0, btConfirm, PendingIntent.FLAG_ONE_SHOT);

        Intent btDelete = new Intent(this, MyBroadcastReceiver.class);
        btDelete.putExtra("tourId", String.valueOf(id));
        btDelete.putExtra("ifOfNoti", idOfNoti);
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
                        .setGroup("INVITATION")
                        .setGroupSummary(true)
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


        notificationManager.notify(idOfNoti, notificationBuilder.build());
    }

//    public void getUserInfoFromId(String userId){
//
//        APIService mAPIService = ApiUtils.getAPIService();
//        mAPIService.getListUsers("", 1, 2000).enqueue(new Callback<UserReq>() {
//            @Override
//            public void onResponse(Call<UserReq> call, Response<UserReq> response) {
//
//                if (response.code() == 200) {
//                    users = response.body().getUsers();
//                }
//            }
//            @Override
//            public void onFailure(Call<UserReq> call, Throwable t) {
//            }
//
//        });
//
////        for (int i = 0; i < users.length; ++i){
////            if (users[i].getID() == Integer.valueOf(userId)){
////                return users[i];
////            }
////        }
//
//    }
}
