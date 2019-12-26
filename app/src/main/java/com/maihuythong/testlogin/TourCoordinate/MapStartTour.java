package com.maihuythong.testlogin.TourCoordinate;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.util.LayoutDirection;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.AutocompleteSessionToken;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.messaging.FirebaseMessaging;
import com.maihuythong.testlogin.R;
import com.maihuythong.testlogin.googlemapapi.DirectionFinder;
import com.maihuythong.testlogin.googlemapapi.GetNearbyPlaces;
import com.maihuythong.testlogin.googlemapapi.Route;
import com.maihuythong.testlogin.googlemapapi.StopPointGoogleMap;
import com.maihuythong.testlogin.model.StopPoints;
import com.maihuythong.testlogin.pop.ShowPopupActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MapStartTour extends FragmentActivity implements  OnMapReadyCallback  {


    private static final String TAG = "MAP ACTIVITY";

    GoogleMap map;
    private ImageView myLocation;

    private static final float DEFAULT_ZOOM = 15f;
    private ServiceToActivity serviceReceiver;
    ArrayList<PostCoordinate> data;
    ArrayList<PostCoordinate> tempData;
    private boolean isMapReady = false;
    private boolean firstTime = true;
    ArrayList<Marker> listMemberPos = new ArrayList<>();
    private long tourId;
    private long userId;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_on_start);
        final Intent intent = getIntent();
        tourId = intent.getLongExtra("tourId", 0);
        userId = intent.getLongExtra("userId", 0);
        serviceReceiver = new ServiceToActivity();
        IntentFilter intentSFilter = new IntentFilter("ServiceToActivityAction");
        registerReceiver(serviceReceiver, intentSFilter);
        //searchText = findViewById(R.id.input_search);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);

        FloatingActionButton fab = findViewById(R.id.noti_btn);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(MapStartTour.this, NotificationOnRoad.class);
                intent1.putExtra("tourId", tourId);
                intent1.putExtra("userId", userId);
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


            if(!Places.isInitialized()){
                Places.initialize(getApplicationContext(),getResources().getString(R.string.google_maps_key));
            }

            //placesClient = Places.createClient(this);


            map.getUiSettings().setMyLocationButtonEnabled(false);
            map.getUiSettings().setZoomControlsEnabled(true);

            init();
            initMap();
            isMapReady = true;

    }


    private void HideSoftKeyboard(){
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }
}
