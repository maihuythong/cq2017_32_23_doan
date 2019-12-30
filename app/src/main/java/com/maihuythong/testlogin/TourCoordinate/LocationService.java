package com.maihuythong.testlogin.TourCoordinate;


import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.maihuythong.testlogin.LoginActivity;
import com.maihuythong.testlogin.signup.APIService;
import com.maihuythong.testlogin.signup.ApiUtils;


import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

public class LocationService extends Service implements LocationListener {
    public static final int notify = 10000;  //interval between two services(Here Service run every 5 seconds)
    //int count = 0;  //number of times service is display
    private Handler mHandler = new Handler();   //run on another Thread to avoid crash
    private Timer mTimer = null;    //timer handling
    private SharedPreferences sf;
    private LatLng currentLatLng = new LatLng(0,0);
    private LatLng lastLatlng;
    private String tourId;
    protected LocationManager locationManager;
    protected LocationListener locationListener;
    protected Context context;
    protected String latitude, longitude;
    protected boolean gps_enabled, network_enabled;
    private boolean firstTime = true;

    private FusedLocationProviderClient fusedLocationProviderClient;

    final public static String TAG = "SERVICE";


    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        tourId = String.valueOf(intent.getLongExtra("tourId", 1));
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    Activity#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for Activity#requestPermissions for more details.
            return START_STICKY;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        return START_STICKY;
    }

    @Override
    public void onCreate() {


        if (mTimer != null) // Cancel if already existed
            mTimer.cancel();
        else
            mTimer = new Timer();   //recreate new
        mTimer.scheduleAtFixedRate(new TimeDisplay(), 0, notify);   //Schedule task
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mTimer.cancel();    //For Cancel Timer
    //    Toast.makeText(this, "Service is Destroyed", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLocationChanged(Location location) {
        currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());
        Log.d("CURRENT LOCATION",currentLatLng.toString());
        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction("LocationDevice");
        broadcastIntent.putExtra("LocationLat", currentLatLng.latitude);
        broadcastIntent.putExtra("LocationLong", currentLatLng.longitude);
        sendBroadcast(broadcastIntent);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d("Latitude","status");
        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction("LocationDevice");
        broadcastIntent.putExtra("LocationLat", currentLatLng.latitude);
        broadcastIntent.putExtra("LocationLong", currentLatLng.longitude);
        sendBroadcast(broadcastIntent);
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.d("Latitude","enable");
    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.d("Latitude","disable");
    }


    //class TimeDisplay for handling task
    class TimeDisplay extends TimerTask {
        @Override
        public void run() {
            // run on another thread
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    // display toast
                    //Toast.makeText(LocationService.this, "Service is running", Toast.LENGTH_SHORT).show();

                    String token = LoginActivity.token;
                    if (token == null) {
                        sf = getSharedPreferences("com.maihuythong.testlogin_preferences", MODE_PRIVATE);
                        token = sf.getString("login_access_token", "");
                    }

                    //currentLatLng = getDeviceLocation();
                    double latitude = currentLatLng.latitude;
                    double longitude = currentLatLng.longitude;
                    //if (!currentLatLng.equals(lastLatlng)) {
                    if (latitude == -200 || longitude == -200)
                        return;
                    if (true) {
                        //userId se tu nhan qua token

                        APIService service = ApiUtils.getAPIService();
                        service.postCoordinate(token, "1", tourId, latitude, longitude).enqueue(new Callback<ArrayList<PostCoordinate>>() {

                            @Override
                            public void onResponse(Call<ArrayList<PostCoordinate>> call, Response<ArrayList<PostCoordinate>> response) {
                                //Toast.makeText(getApplicationContext(), response.body().toString(), Toast.LENGTH_SHORT).show();
                                ArrayList<PostCoordinate> arrayList = new ArrayList<>(response.body());


                                Intent broadcastIntent = new Intent();
                                broadcastIntent.setAction("ServiceToActivityAction");
                                broadcastIntent.putExtra("ServiceToActivityKey", arrayList);
                                sendBroadcast(broadcastIntent);
                            }

                            @Override
                            public void onFailure(Call<ArrayList<PostCoordinate>> call, Throwable t) {
                                //Toast.makeText(getApplicationContext(), t.toString(), Toast.LENGTH_SHORT).show();

                            }
                        });
                    }
                }
            });
        }
    }

//    private LatLng getDeviceLocation() {
////        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
////        try {
////            Thread.sleep(2000);
////        } catch (InterruptedException e) {
////            e.printStackTrace();
////        }
////
////        final LatLng[] ret = new LatLng[1];
////        final Task location = fusedLocationProviderClient.getLastLocation();
////        location.addOnCompleteListener(new OnCompleteListener() {
////            @Override
////            public void onComplete(@NonNull Task task) {
////                if (task.isSuccessful()) {
////                    Log.d("LOCATION", "onComplete: found location!");
////                    Location currentLocation = (Location) task.getResult();
////                    ret[0] = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
////
////                } else {
////                    Log.d("LOCATION", "onConplete: current location is null");
////                    Toast.makeText(getApplicationContext(), "unable to get current location", Toast.LENGTH_SHORT).show();
////                }
////            }
////        });
////
////        return ret[0];
//
//        //   }
//
//
////        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
////        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
////            // TODO: Consider calling
////            //    Activity#requestPermissions
////            // here to request the missing permissions, and then overriding
////            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
////            //                                          int[] grantResults)
////            // to handle the case where the user grants the permission. See the documentation
////            // for Activity#requestPermissions for more details.
////            return new LatLng(0, 0);
////        }
//
//        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
//
////        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
////        double longitude = location.getLongitude();
////        double latitude = location.getLatitude();
//        return new LatLng(latitude, longitude);
//    }





    private LatLng getDeviceLocation() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

//        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
//            @Override
//            public void onSuccess(Location location) {
//                if (location!= null){
//                    lastLatlng = currentLatLng;
//                    currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());
//                }
//            }
//        });
//        final Task<Location> location = fusedLocationProviderClient.getLastLocation();
//        location.addOnSuccessListener(new OnSuccessListener<Location>() {
//            @Override
//            public void onSuccess(Location location) {
//                lastLatlng = currentLatLng;
//                currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());
//            }
//        });

//        {
//            @Override
//            public void onComplete(@NonNull Task task) {
//                if (task.isSuccessful()) {
//                    Log.d("SERVICE", "onComplete: found location!");
//                    Location currentLocation = (Location) task.getResult();
//                    lastLatlng = currentLatLng;
//                    currentLatLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
//
//                } else {
//                    Log.d("SERVICE", "onConplete: current location is null");
//                }
//            }
//        });

        if (firstTime){
            try {
                Thread.sleep(4000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            firstTime = false;
        }
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    Activity#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for Activity#requestPermissions for more details.
            if (currentLatLng == null){
                currentLatLng = new LatLng(-200,-200);
            }

            return currentLatLng;
        }
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());

        if (location == null){
            currentLatLng = new LatLng(0,0);
        }

        return currentLatLng;
    }
}
