package com.maihuythong.testlogin.TourCoordinate;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.messaging.FirebaseMessaging;
import com.maihuythong.testlogin.R;
import com.maihuythong.testlogin.ShowNotiOnRoadList.ShowNotiOnRoad;
import com.maihuythong.testlogin.googlemapapi.DirectionFinder;
import com.maihuythong.testlogin.googlemapapi.GetNearbyPlaces;
import com.maihuythong.testlogin.googlemapapi.Route;
import com.maihuythong.testlogin.googlemapapi.StopPointGoogleMap;
import com.maihuythong.testlogin.model.StopPoints;
import com.maihuythong.testlogin.pop.ShowPopupActivity;
import com.maihuythong.testlogin.showTourInfo.GetTourInfo;
import com.maihuythong.testlogin.showTourInfo.StopPoint;
import com.maihuythong.testlogin.signup.APIService;
import com.maihuythong.testlogin.signup.ApiUtils;
import com.maihuythong.testlogin.userInfo.UserInfoRes;

import java.util.ArrayList;
import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapStartTour extends FragmentActivity implements  OnMapReadyCallback  {


    private static final String TAG = "MAP ACTIVITY";

    GoogleMap map;
    private ImageView myLocation;

    private static final float DEFAULT_ZOOM = 15f;
    private ServiceToActivity serviceReceiver;
    private LimitSpeedEnd endLimitSpeedReceiver;
    private LimitSpeedStart startLimitSpeedReceiver;
    ArrayList<PostCoordinate> data;
    ArrayList<PostCoordinate> tempData;
    private boolean isMapReady = false;
    private boolean firstTime = true;
    ArrayList<Marker> listMemberPos = new ArrayList<>();
    private long tourId;
    private long userId;
    private ImageView start_speed_limit;
    private ImageView end_speed_limit;
    LatLng currentLatlng;
    private String token;
    private SharedPreferences sf;
    private boolean isFirstTime = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_on_start);
        myLocation = findViewById(R.id.my_location);
        final Intent intent = getIntent();
        tourId = intent.getLongExtra("tourId", 0);
        //userId = intent.getLongExtra("userId", 0);

        if (token == null) {
            sf = getSharedPreferences("com.maihuythong.testlogin_preferences", MODE_PRIVATE);
            token = sf.getString("login_access_token", "");
        }
        APIService mapiService = ApiUtils.getAPIService();
        mapiService.getUserInfo(token).enqueue(new Callback<UserInfoRes>() {
            @Override
            public void onResponse(Call<UserInfoRes> call, Response<UserInfoRes> response) {
                UserInfoRes userInfoRes = response.body();
                userId = userInfoRes.getID();
            }

            @Override
            public void onFailure(Call<UserInfoRes> call, Throwable t) {

            }
        });

        serviceReceiver = new ServiceToActivity();
        IntentFilter intentSFilter = new IntentFilter("ServiceToActivityAction");
        registerReceiver(serviceReceiver, intentSFilter);

        IntentFilter intentFilterLocation = new IntentFilter("LocationDevice");
        registerReceiver(serviceReceiver, intentFilterLocation);

        startLimitSpeedReceiver = new LimitSpeedStart();
        IntentFilter intentFilterStartLimit = new IntentFilter("StartLimit");
        registerReceiver(startLimitSpeedReceiver, intentFilterStartLimit);

        endLimitSpeedReceiver = new LimitSpeedEnd();
        IntentFilter intentFilterEndLimit = new IntentFilter("EndLimit");
        registerReceiver(endLimitSpeedReceiver, intentFilterEndLimit);
        //searchText = findViewById(R.id.input_search);

        start_speed_limit = findViewById(R.id.speed_limit_60);
        end_speed_limit = findViewById(R.id.end_speed_limit_60);

        start_speed_limit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(MapStartTour.this, SendSpeedLimitStart.class);
                intent1.putExtra("tourId", tourId);
                intent1.putExtra("userId", userId);
                intent1.putExtra("latitude", currentLatlng.latitude);
                intent1.putExtra("longitude", currentLatlng.longitude);
                startActivity(intent1);
            }
        });

        end_speed_limit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(MapStartTour.this, SendSpeedLimitEnd.class);
                intent1.putExtra("tourId", tourId);
                intent1.putExtra("userId", userId);
                intent1.putExtra("latitude", currentLatlng.latitude);
                intent1.putExtra("longitude", currentLatlng.longitude);
                startActivity(intent1);
            }
        });


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);

        ImageButton fab = findViewById(R.id.noti_btn);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(MapStartTour.this, NotificationOnRoad.class);
                intent1.putExtra("tourId", tourId);
                intent1.putExtra("userId", userId);
                startActivity(intent1);
            }
        });

        ImageButton showNotiListButton = findViewById(R.id.show_noti_list);
        showNotiListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(MapStartTour.this, ShowNotiOnRoad.class);
                intent1.putExtra("tourId", tourId);
                startActivity(intent1);
            }
        });

        ImageButton fabrc = findViewById(R.id.send_record);
        fabrc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(MapStartTour.this, Record.class);
                startActivity(intent1);
            }
        });

        String topic = "/topics/tour-id-" + tourId;

        FirebaseMessaging.getInstance().subscribeToTopic(topic)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg = "Success";
                        if (!task.isSuccessful()) {
                            msg = "Fail";
                        }
                        Log.d("MESSAGING", msg);
                       // Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                    }
                });

        myLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatlng, 15f));
//                map.moveCamera(CameraUpdateFactory
//                        .newCameraPosition
//                                (new CameraPosition.Builder()
//                                        .target(currentLatlng)
//                                        .zoom(15.5f)
//                                        .build()));
            }
        });
    }

    public class ServiceToActivity extends BroadcastReceiver
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            if (!isMapReady)
                return;
            data = (ArrayList<PostCoordinate>) intent.getSerializableExtra("ServiceToActivityKey");

            // newData is from the service
            double latitude = intent.getDoubleExtra("LocationLat",0);
            double longitude = intent.getDoubleExtra("LocationLong",0);
            currentLatlng = new LatLng(latitude,longitude);


            if (data!=null){
                if (firstTime){
                    tempData = data;
                    firstTime = false;
                    for (PostCoordinate p : data){
                        Marker marker = map.addMarker(new MarkerOptions()
                                .position(new LatLng(p.getLatitude(), p.getLongitude()))
                                .flat(true)
                                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.icon_car))
                                .title(p.getId()));
                        listMemberPos.add(marker);
                    }

//                    map.addMarker(new MarkerOptions().position(new LatLng(22.035455000000011, 105.86231166666257)).title("1"));
//                    map.addMarker(new MarkerOptions().position(new LatLng(23.035455200000011, 105.86231166566257)).title("2"));
                }else {
                    for (int i = 0; i < data.size(); ++i) {
                        PostCoordinate a = data.get(i);
                        for (int j = 0; j < tempData.size(); ++j) {
                            PostCoordinate b = tempData.get(j);
                            if (a.getId().equals(b.getId())) {
                                if (a.getLatitude() != b.getLatitude() || a.getLongitude() != b.getLongitude()) {
                                    if (isExistMarker(a.getId())){
                                        int index = getIndexMarker(a.getId());
                                        if (index != -1){
                                            Marker m = listMemberPos.get(index);
                                            m.remove();
                                            listMemberPos.remove(index);
                                        }
                                    }
                                    Marker marker = map.addMarker(new MarkerOptions()
                                            .position(new LatLng(a.getLatitude(), a.getLongitude()))
                                            .flat(true)
                                            .icon(BitmapDescriptorFactory.fromResource(R.mipmap.icon_car))
                                            .title(a.getId()));


                                    listMemberPos.add(marker);
                                }
                            }
                        }
                    }
                }
            }

        }
    }

    public class LimitSpeedStart extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            double startLimitLat = Double.valueOf(intent.getStringExtra("startLimitLocationLat"));
            double startLimitLong = Double.valueOf(intent.getStringExtra("startLimitLocationLong"));
            long startLimitUserId = Long.valueOf(intent.getStringExtra("startLimitUserId"));
            String startLitmitUserName = intent.getStringExtra("startLimitUserName");
            String notificationText = intent.getStringExtra("startLimitNoti");

            map.addMarker(new MarkerOptions()
                    .position(new LatLng(startLimitLat, startLimitLong))
                    .flat(true)
                    .icon(BitmapDescriptorFactory.fromResource(R.mipmap.warning_on_road))
                    .title("Start limit speed by: " + startLitmitUserName + " (" + startLimitUserId + " )" + " " + notificationText));

        }
    }

    public class LimitSpeedEnd extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            double endLimitLat = Double.valueOf(intent.getStringExtra("endLimitLocationLat"));
            double endLimitLong = Double.valueOf(intent.getStringExtra("endLimitLocationLong"));
            long endLimitUserId = Long.valueOf(intent.getStringExtra("endLimitUserId"));
            String endLitmitUserName = intent.getStringExtra("endLimitUserName");
            String notificationText = intent.getStringExtra("endLimitNoti");
            map.addMarker(new MarkerOptions()
                    .position(new LatLng(endLimitLat, endLimitLong))
                    .flat(true)
                    .icon(BitmapDescriptorFactory.fromResource(R.mipmap.safe_on_road))
                    .title("End limit speed by: " + endLitmitUserName + " (" + endLimitUserId + " )" + " " + notificationText));
        }
    }
    private boolean isExistMarker(String idTitle){
        for (Marker m : listMemberPos){
            if (m.getTitle().equals(idTitle))
                return true;
        }
        return false;
    }

    private int getIndexMarker(String title){
        for (int i = 0; i < listMemberPos.size(); ++i){
            if (listMemberPos.get(i).getTitle().equals(title))
                return i;
        }
        return -1;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(serviceReceiver);
    }

    private void init(){
        // ================================================= //

        // search better
        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);

        // Specify the types of place data to return.
        if(autocompleteFragment != null) {
            autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.LAT_LNG, Place.Field.NAME));


            // Set up a PlaceSelectionListener to handle the response.
            autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
                @Override
                public void onPlaceSelected(Place place) {
                    // TODO: Get info about the selected place.
                    moveCamera(place.getLatLng(), DEFAULT_ZOOM, place.getName());
                    Log.i(TAG, "Place: " + place.getName() + ", " + place.getId());
                }

                @Override
                public void onError(Status status) {
                    // TODO: Handle the error.
                    Log.i(TAG, "An error occurred: " + status);
                }
            });
        }

        HideSoftKeyboard();
    }


    private void initMap(){
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);

            myLocation = findViewById(R.id.my_location);
        }

    }



    private void moveCamera(LatLng latLng, float zoom, String title) {
        Log.d(TAG, "Move Camera!");
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));

        if (!title.equals("My location")) {
            MarkerOptions markerOptions = new MarkerOptions()
                    .position(latLng)
                    .title(title);
            //  map.addMarker(markerOptions);
        }

        HideSoftKeyboard();
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        // get current location


        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), getResources().getString(R.string.google_maps_key));
        }

        //placesClient = Places.createClient(this);


        map.getUiSettings().setMyLocationButtonEnabled(false);
        map.getUiSettings().setZoomControlsEnabled(true);

        init();
        initMap();
        isMapReady = true;




        if (isFirstTime){
            isFirstTime = false;
            APIService apiService = ApiUtils.getAPIService();
            apiService.getTourInfo(token,tourId).enqueue(new Callback<GetTourInfo>() {
                @Override
                public void onResponse(Call<GetTourInfo> call, Response<GetTourInfo> response) {
                    StopPoint[] stopPoints = response.body().getStopPoints();
                    boolean isFirstPos = true;
                    for (StopPoint point : stopPoints)
                    {
                        if (isFirstPos){
                            map.moveCamera(CameraUpdateFactory
                                    .newCameraPosition
                                            (new CameraPosition.Builder()
                                                    .target(new LatLng(point.getLat().doubleValue(), point.getLong().doubleValue()))
                                                    .zoom(15.5f)
                                                    .build()));
                            isFirstPos = false;
                        }
                        if (point.getName().equals("Start Tour")){
                            map.addMarker(new MarkerOptions()
                                    .position(new LatLng(point.getLat().doubleValue(), point.getLong().doubleValue()))
                                    .flat(true)
                                    .icon(BitmapDescriptorFactory.fromResource(R.mipmap.start_end))
                                    .title("Start tour"));
                        }else if (point.getName().equals("End Tour")){
                            map.addMarker(new MarkerOptions()
                                    .position(new LatLng(point.getLat().doubleValue(), point.getLong().doubleValue()))
                                    .flat(true)
                                    .icon(BitmapDescriptorFactory.fromResource(R.mipmap.start_end))
                                    .title("End tour"));
                        }else {
                            map.addMarker(new MarkerOptions()
                                    .position(new LatLng(point.getLat().doubleValue(), point.getLong().doubleValue()))
                                    .flat(true)
                                    .icon(BitmapDescriptorFactory.fromResource(R.mipmap.stoppoint))
                                    .title(point.getName()));
                        }
                    }
                }

                @Override
                public void onFailure(Call<GetTourInfo> call, Throwable t) {

                }
            });
        }

    }


    private void HideSoftKeyboard(){
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }
}
