package com.maihuythong.testlogin.googlemapapi;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
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
import com.maihuythong.testlogin.R;
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

//import com.mancj.materialsearchbar.MaterialSearchBar;


public class StopPointGoogleMap extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, ResultCallback, OnMapReadyCallback, GoogleMap.OnMapClickListener, DirectionFinderListener{

    public static final int TEXT_REQUEST = 1;
    private JSONArray stopPointsArray = new JSONArray();
    private String addressFromMarker;
    private double mLat, mLng;

    private static final String TAG = "MAP ACTIVITY";
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private  static final  int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private static final float DEFAULT_ZOOM = 15f;
    private Marker desMarker;
    private Marker startLocation;
    private Marker currentStopPoint;
    //private Polyline currentPolylinePaths;

    private List<Marker> originMarkers = new ArrayList<>();
    private List<Marker> destinationMarkers = new ArrayList<>();
    private List<Polyline> polylinePaths = new ArrayList<>();
    private ProgressDialog progressDialog;
    private ArrayList<LatLng> stopPoints = new ArrayList<>();
    private ArrayList<LatLng> tempstopPoints = new ArrayList<>();
    private LatLng StartPoint;


    private Boolean mLocationPermissionGranted = false;
    GoogleMap map;
    private FusedLocationProviderClient fusedLocationProviderClient;

    private LatLng myLatlng;
    private LatLng currentLatlngSelected;

    private ArrayList<LatLng> listStep;
    private PolylineOptions polyline;
    private EditText searchText;
    private ImageView myLocation;

    //private MaterialSearchBar materialSearchBar;
    PlacesClient placesClient;

    private AutocompleteSessionToken token;
    private List<AutocompletePrediction> predictionList;


    protected GoogleApiClient mGoogleApiClient;
    protected LocationRequest locationRequest;
    int REQUEST_CHECK_SETTINGS = 100;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stop_point_google_map);
        //searchText = findViewById(R.id.input_search);


        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).build();
        mGoogleApiClient.connect();

        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(30 * 1000);
        locationRequest.setFastestInterval(5 * 1000);




//        getLocationPermission();        // check permission before init map
//        myLocation = findViewById(R.id.my_location);
////        materialSearchBar = findViewById(R.id.searchBar);
//
////        token = AutocompleteSessionToken.newInstance();
//
//        if(!Places.isInitialized()){
//            Places.initialize(getApplicationContext(),getResources().getString(R.string.google_maps_key));
//        }
//
//        placesClient = Places.createClient(this);




        //init();
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

        myLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDeviceLocation();
            }
        });
        HideSoftKeyboard();
    }

    private void  geoLocate(){
        String searchString = searchText.getText().toString();

        Geocoder geocoder = new Geocoder(this);
        List<Address> list = new ArrayList<>();
        try {
            while(list.size() == 0) {
                list = geocoder.getFromLocationName(searchString, 1);
            }
        }catch (IOException e){
            Log.e(TAG, "geoLocate: IOException: " + e.getMessage());
        }

        if (list.size() > 0){
            Address address = list.get(0);

            Log.d(TAG,"geoLocate: found a location: "+ address.toString());
            moveCamera(new LatLng(address.getLatitude(),address.getLongitude()), DEFAULT_ZOOM, address.getAddressLine(0));
        }

    }
    private void initMap(){
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);

            myLocation = findViewById(R.id.my_location);
        }

    }


    private void getDeviceLocation(){
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        try {
            if(mLocationPermissionGranted){
                final Task location = fusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()){
                            Log.d(TAG,"onComplete: found location!");
                            Location currentLocation = (Location) task.getResult();

                            myLatlng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
                            moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()),
                                    DEFAULT_ZOOM,
                                    "Start Tour");
                        }else{
                            Log.d(TAG, "onConplete: current location is null");
                            Toast.makeText(StopPointGoogleMap.this,"unable to get current location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }catch (SecurityException e){
            Log.e(TAG, "getDeviceLocation SecurityException: " + e.getMessage());
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

    private void getLocationPermission(){
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};

        if(ContextCompat.checkSelfPermission(this.getApplicationContext(),
                FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            if(ContextCompat.checkSelfPermission(this.getApplicationContext(),
                    COURSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                mLocationPermissionGranted = true;
                initMap();
            }else{
                ActivityCompat.requestPermissions(this,
                        permissions,
                        LOCATION_PERMISSION_REQUEST_CODE);
            }
        }else{
            ActivityCompat.requestPermissions(this,
                    permissions,
                    LOCATION_PERMISSION_REQUEST_CODE);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        mLocationPermissionGranted = false;

        switch(requestCode){
            case LOCATION_PERMISSION_REQUEST_CODE:{
                if(grantResults.length > 0){
                    for(int i = 0; i < grantResults.length; i++){
                        if(grantResults[i] != PackageManager.PERMISSION_GRANTED){
                            mLocationPermissionGranted = false;
                            Log.d(TAG, "onRequestPermissionsResult: permission failed");
                            return;
                        }
                    }
                    Log.d(TAG, "onRequestPermissionsResult: permission granted");
                    mLocationPermissionGranted = true;
                    //initialize our map
                    initMap();
                }
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        // get current location
        if(mLocationPermissionGranted){
            //getDeviceLocation();
//        materialSearchBar = findViewById(R.id.searchBar);

//        token = AutocompleteSessionToken.newInstance();

            if(!Places.isInitialized()){
                Places.initialize(getApplicationContext(),getResources().getString(R.string.google_maps_key));
            }

            placesClient = Places.createClient(this);

            if(ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) !=
                    PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                return;
            }


            map.setMyLocationEnabled(true);   // my location (blue dot)

            getDeviceLocation();

            map.getUiSettings().setMyLocationButtonEnabled(false);
            map.getUiSettings().setZoomControlsEnabled(true);

            map.setOnMapClickListener(this);

            init();
            map.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
                @Override
                public void onMarkerDragStart(Marker marker) {
                    Log.d("System out", "onMarkerDragStart..."+marker.getPosition().latitude+"..."+marker.getPosition().longitude);
                }

                @Override
                public void onMarkerDrag(Marker marker) {
                    Log.i("System out", "onMarkerDrag...");
                }

                @Override
                public void onMarkerDragEnd(Marker marker) {
                    Log.d("System out", "onMarkerDragEnd..."+marker.getPosition().latitude+"..."+marker.getPosition().longitude);

                    map.animateCamera(CameraUpdateFactory.newLatLng(marker.getPosition()));
                }
            });

        }

    }

    private void HideSoftKeyboard(){
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }



    @Override
    public void onMapClick(LatLng latLng) {
        mLat = latLng.latitude;
        mLng = latLng.longitude;
        Log.d(TAG,"LAT: "+ latLng.latitude + " LONG: "+ latLng.longitude);
        // Toast.makeText(this,"LAT: "+ latLng.latitude + " LONG: ", Toast.LENGTH_SHORT).show();
        //desMarker = map.addMarker(new MarkerOptions()
        //        .position(latLng));
        //currentLatlngSelected
        if(StartPoint == null){
            startLocation = map.addMarker(new MarkerOptions()
                    .position(latLng)
                    .title("Start Tour")
                    .draggable(true));
            StartPoint = new LatLng(latLng.latitude, latLng.longitude);
        }else {
            currentLatlngSelected = new LatLng(latLng.latitude, latLng.longitude);
            tempstopPoints.add(new LatLng(latLng.latitude, latLng.longitude));
            if (tempstopPoints.size() > 1) {
                for (int i = 0; i < tempstopPoints.size() - 1; ++i) {
                    stopPoints.add(tempstopPoints.get(i));
                }
            }

            try {
//            if(currentLatlngSelected == null) {
//                currentLatlngSelected = new LatLng(latLng.latitude, latLng.longitude);
//            }else
//            {
//                stopPoints.add(new LatLng(latLng.latitude,latLng.longitude));
//            }
                currentStopPoint = map.addMarker(new MarkerOptions()
                        .position(latLng)
                        .title("Stop point")
                        .draggable(true));


                new DirectionFinder(this, StartPoint, currentLatlngSelected, stopPoints).execute();

                Geocoder geocoder;
                List<Address> addresses;
                String address = "";
                geocoder = new Geocoder(this, Locale.getDefault());
                try {
                    addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                    address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
//                    String city = addresses.get(0).getLocality();
//                    String state = addresses.get(0).getAdminArea();
//                    String country = addresses.get(0).getCountryName();
//                    String postalCode = addresses.get(0).getPostalCode();
//                    String knownName = addresses.get(0).getFeatureName();
                }catch (IOException e){
                    Log.d(TAG,"Addresses error!" + e.getMessage());
                }

                Intent intent = new Intent(this, ShowPopupActivity.class);

                intent.putExtra("EXTRA_ADDRESS", address);
                addressFromMarker = address;
                startActivityForResult(intent, 1);




            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void onDirectionFinderStart() {
        progressDialog = ProgressDialog.show(this, "Please wait.",
                "Finding direction..!", true);

        if (originMarkers != null) {
            for (Marker marker : originMarkers) {
                marker.remove();
            }
        }

        if (destinationMarkers != null) {
            for (Marker marker : destinationMarkers) {
                marker.remove();
            }
        }

        if (polylinePaths != null) {
            for (Polyline polyline:polylinePaths ) {
                polyline.remove();
            }
        }
    }

    @Override
    public void onDirectionFinderSuccess(List<Route> routes) {
        progressDialog.dismiss();
        polylinePaths = new ArrayList<>();
        originMarkers = new ArrayList<>();
        destinationMarkers = new ArrayList<>();

        for(Polyline line : polylinePaths)
        {
            line.remove();
        }

        polylinePaths.clear();

        for (Route r : routes) {
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(r.endLocation, 16));

//            originMarkers.add(map.addMarker(new MarkerOptions()
//                    //.icon(BitmapDescriptorFactory.fromResource(R.drawable.start_blue))
//                    .title(r.startAddress)
//                    .position(r.startLocation)));
            startLocation.setTitle("Start tour at: " + r.startAddress);
            destinationMarkers.add(map.addMarker(new MarkerOptions()
                    //.icon(BitmapDescriptorFactory.fromResource(R.drawable.end_green))
                    .title("Current destination: " + r.endAddress)
                    .position(r.endLocation)));

            PolylineOptions polylineOptions = new PolylineOptions().
                    geodesic(true).
                    color(Color.BLUE).
                    width(10);

            for (int i = 0; i < r.points.size(); i++)
                polylineOptions.add(r.points.get(i));



            polylinePaths.add(map.addPolyline(polylineOptions));
        }
    }


//    public class GetDirectionsTask {
//        private String request;
//
//        public GetDirectionsTask(String request) {
//            this.request = request;
//        }
//
//        public ArrayList<LatLng> testDirection() {
//            ArrayList<LatLng> ret = new ArrayList<>();
//            try {
//                URL url;
//                url = new URL(request);
//
//                InputStreamReader reader = new InputStreamReader(url.openStream(), "UTF-8");
//
//                Directions results = new Gson().fromJson(reader, Directions.class);
//                Directions.Route[] routes = results.getRoutes();
//                Directions.Leg[] legs = routes[0].getLegs();
//                Directions.Leg.Step[] steps = legs[0].getSteps();
//                for (Directions.Leg.Step s : steps) {
//                    LatLng latLngStart = new LatLng(s.getStart_location().getLat(),
//                            s.getStart_location().getLng());
//
//                    LatLng latLngEnd = new LatLng(s.getEnd_location().getLat(),
//                            s.getEnd_location().getLat());
//
//                    ret.add(latLngStart);
//                    ret.add(latLngEnd);
//                }
//                return ret;
//            } catch (UnsupportedEncodingException e) {
//                e.printStackTrace();
//            } catch (MalformedURLException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            return ret;
//        }
//
//    }

//    public String makeURL (String sourceLat, String sourceLong, String desLat, String desLong){
//        StringBuilder urlString = new StringBuilder();
//        urlString.append("https://maps.googleapis.com/maps/api/directions/json?");
//        urlString.append("origin=");
//        urlString.append(sourceLat);
//        urlString.append(",");
//        urlString.append(sourceLong);
//        urlString.append("&destination=");
//        urlString.append(desLat);
//        urlString.append(",");
//        urlString.append(desLong);
//        urlString.append("&key=" + getResources().getString(R.string.google_maps_key));
//
//        return urlString.toString();
//    }

    public void onClick(View v) {
        String hospital = "hospital", restaurant = "restaurant",
                rest_station = "rest%station", other = "other";
        Object transferData[] = new Object[2];
        GetNearbyPlaces getNearbyPlaces = new GetNearbyPlaces();
        String url;

        LatLng curnentCamera = map.getCameraPosition().target;

        switch (v.getId())
        {
            case R.id.hotel:
                map.clear();
                url = getUrlNearby(curnentCamera.latitude, curnentCamera.longitude, hospital);
                transferData[0] = map;
                transferData[1] = url;

                getNearbyPlaces.execute(transferData);
                Toast.makeText(this, "Searching for Nearby Hospitals...", Toast.LENGTH_SHORT).show();
                Toast.makeText(this, "Showing Nearby Hospitals...", Toast.LENGTH_SHORT).show();
                break;


            case R.id.restaurant:
                map.clear();
                url = getUrlNearby(curnentCamera.latitude, curnentCamera.longitude, restaurant);
                transferData[0] = map;
                transferData[1] = url;

                getNearbyPlaces.execute(transferData);
                Toast.makeText(this, "Searching for Nearby Restaurant...", Toast.LENGTH_SHORT).show();
                Toast.makeText(this, "Showing Nearby Restaurant...", Toast.LENGTH_SHORT).show();
                break;


            case R.id.rest_station:
                map.clear();
                url = getUrlNearby(curnentCamera.latitude, curnentCamera.longitude, rest_station);
                transferData[0] = map;
                transferData[1] = url;

                getNearbyPlaces.execute(transferData);
                Toast.makeText(this, "Searching for Nearby Rest Station...", Toast.LENGTH_SHORT).show();
                Toast.makeText(this, "Showing Nearby Rest Station...", Toast.LENGTH_SHORT).show();
                break;

            case R.id.other:
                map.clear();
                url = getUrlNearby(curnentCamera.latitude, curnentCamera.longitude, other);
                transferData[0] = map;
                transferData[1] = url;

                getNearbyPlaces.execute(transferData);
                Toast.makeText(this, "Searching for Nearby Places...", Toast.LENGTH_SHORT).show();
                Toast.makeText(this, "Showing Nearby Places...", Toast.LENGTH_SHORT).show();
                break;

            case R.id.complete_add_stop_point:
                StopPoints stopPoints = new StopPoints();
                //TODO:hihih
//                stopPoints.setTourId("123123123");
//                stopPoints.setStopPoints(arrayStopPoint);







                RequestQueue requestQueue;

                // Instantiate the cache
                Cache cache = new DiskBasedCache(getCacheDir(), 1024 * 1024); // 1MB cap

                // Set up the network to use HttpURLConnection as the HTTP client.
                Network network = new BasicNetwork(new HurlStack());

                // Instantiate the RequestQueue with the cache and network.
                requestQueue = new RequestQueue(cache, network);

                // Start the queue
                requestQueue.start();

                String theUrl ="http://35.197.153.192:3000/tour/set-stop-points";

                JSONObject req = new JSONObject();
                try {
                    Intent intent = getIntent();
                    int tourId = intent.getIntExtra("tourId", 1387);
                    String tourIdString = tourId + "";
                    req.put("tourId", tourIdString);
                    req.put("stopPoints", (Object) stopPointsArray);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, theUrl, req, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray b = new JSONArray();
                            Log.d("kkk", response.getString("message"));
                            finish();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("lll", error.toString());
                    }
                }){
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String>  params = new HashMap<String, String>();
                        SharedPreferences sf = getSharedPreferences("com.maihuythong.testlogin_preferences", MODE_PRIVATE);
                        String tok = sf.getString("login_access_token", "");
                        params.put("Content-Type", "application/json");
//                        params.put("Authorization", "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiI2OSIsInBob25lIjoiMDgzMzUyNzQ1MCIsImVtYWlsIjoibWFpaHV5dGhvbmd4QGdtYWlsLmNvbSIsImV4cCI6MTU3NTk2OTE0NzMxNCwiYWNjb3VudCI6InVzZXIiLCJpYXQiOjE1NzMzNzcxNDd9.0WdtlhBt-5NHzKGRvtKKnoxhoM0vn3_0p4dJfkNRBjA");
                        params.put("Authorization", tok);
                        return params;
                    }
                };
                requestQueue.add(jsonObjectRequest);


        }
    }

    private String getUrlNearby(double latitude, double longitude, String nearbyPlace)
    {
        StringBuilder googleURL = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googleURL.append("location=" + latitude + "," + longitude);
        googleURL.append("&radius=" + 10000);
        googleURL.append("&type=" + nearbyPlace);
        googleURL.append("&sensor=true");
        googleURL.append("&key=" + getResources().getString(R.string.google_maps_key));

        Log.d("GoogleMapsActivity", "url = " + googleURL.toString());

        return googleURL.toString();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Test for the right intent reply.
        // Test to make sure the intent reply result was good.
        if (resultCode == RESULT_OK && requestCode == 1) {
            String stopPointName = data.getStringExtra("REPLY_STOP_POINT_NAME");
            int serviceType = data.getIntExtra("REPLY_SERVICE_TYPE", 1);
            int province = data.getIntExtra("REPLY_PROVINCE", 1);
            long arrivalAt = data.getLongExtra("REPLY_ARRIVAL_AT", 1000000);
            long leaveAt = data.getLongExtra("REPLY_LEAVE_AT", 2000000);

            currentStopPoint.setTitle("Stop point: " + stopPointName);


//            StopPointInfo stopPointInfo = new StopPointInfo();
////            stopPointInfo.setAddress(addressFromMarker);
//            stopPointInfo.setName("hihi");
//            stopPointInfo.setLatitude(123);
//            stopPointInfo.setLongtitude(123);
//            stopPointInfo.setArrivalAt(123856);
//            stopPointInfo.setLeaveAt(545312);
//            stopPointInfo.setServiceTypeId(1);
////            stopPointInfo.setProvinceId(1);
//
//            Log.d("fff" ,stopPointInfo.toString());
//
//            arrayStopPoint.add(stopPointInfo);


            JSONObject tmp = new JSONObject();
            try{
                tmp.put("name", stopPointName);
                tmp.put("lat", mLat);
                tmp.put("long", mLng);
                tmp.put("leaveAt", arrivalAt);
                tmp.put("arrivalAt", leaveAt);
                tmp.put("serviceTypeId", serviceType);
                tmp.put("address", addressFromMarker);
                stopPointsArray.put(tmp);
            }
            catch (JSONException e) {
                e.printStackTrace();
            }


        }


        if (requestCode == REQUEST_CHECK_SETTINGS) {
            if (resultCode == RESULT_OK) {

                myLocation = findViewById(R.id.my_location);
                Toast.makeText(getApplicationContext(), "GPS enabled", Toast.LENGTH_LONG).show();
                getLocationPermission();        // check permission before init map

            } else {

                Toast.makeText(getApplicationContext(), "GPS is not enabled", Toast.LENGTH_LONG).show();
            }

        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);
        PendingResult result =
                LocationServices.SettingsApi.checkLocationSettings(
                        mGoogleApiClient,
                        builder.build()
                );

        result.setResultCallback(this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onResult(@NonNull Result result) {
        final Status status = result.getStatus();
        switch (status.getStatusCode()) {
            case LocationSettingsStatusCodes.SUCCESS:

                getLocationPermission();        // check permission before init map
                myLocation = findViewById(R.id.my_location);

                if(!Places.isInitialized()){
                    Places.initialize(getApplicationContext(),getResources().getString(R.string.google_maps_key));
                }

                placesClient = Places.createClient(this);

                break;

            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                //  Location settings are not satisfied. Show the user a dialog

                try {
                    // Show the dialog by calling startResolutionForResult(), and check the result
                    // in onActivityResult().

                    status.startResolutionForResult(StopPointGoogleMap.this, REQUEST_CHECK_SETTINGS);

                } catch (IntentSender.SendIntentException e) {

                    //failed to show
                }
                break;

            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                // Location settings are unavailable so not possible to show any dialog now
                break;
        }
    }
}

// Direction https://github.com/hiepxuan2008/GoogleMapDirectionSimple/blob/master/app/src/main/java/com/itshareplus/googlemapdemo/MapsActivity.java

// https://github.com/hiepxuan2008/GoogleMapDirectionSimple
// nearby
//https://drive.google.com/drive/folders/1oC1EKNPyd0j6RilwJ-w4YYpKd-6XCn0s