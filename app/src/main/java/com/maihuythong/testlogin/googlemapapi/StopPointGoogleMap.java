package com.maihuythong.testlogin.googlemapapi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
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
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsResponse;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.gson.Gson;
import com.maihuythong.testlogin.R;
import com.maihuythong.testlogin.model.StopPointInfo;
import com.maihuythong.testlogin.pop.ShowPopupActivity;
import com.maihuythong.testlogin.showlist.ShowListActivity;
//import com.mancj.materialsearchbar.MaterialSearchBar;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;



public class StopPointGoogleMap extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener, DirectionFinderListener {

    public static final int TEXT_REQUEST = 1;

    private static final String TAG = "MAP ACTIVITY";
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private  static final  int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private static final float DEFAULT_ZOOM = 15f;
    private Marker desMarker;

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

    private ArrayList<StopPointInfo> arrayStopPoint = new ArrayList<>();

    //private MaterialSearchBar materialSearchBar;
    PlacesClient placesClient;

    private AutocompleteSessionToken token;
    private List<AutocompletePrediction> predictionList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stop_point_google_map);
        //searchText = findViewById(R.id.input_search);
        myLocation = findViewById(R.id.my_location);
//        materialSearchBar = findViewById(R.id.searchBar);

//        token = AutocompleteSessionToken.newInstance();

        if(!Places.isInitialized()){
            Places.initialize(getApplicationContext(),getResources().getString(R.string.google_maps_key));
        }

        placesClient = Places.createClient(this);




        getLocationPermission();        // check permission before init map
        init();
    }

    private void init(){
        // text enter ok
        /*searchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_SEARCH
                        ||actionId == EditorInfo.IME_ACTION_DONE
                        || event.getAction() == KeyEvent.ACTION_DOWN
                        ||event.getAction() == KeyEvent.KEYCODE_ENTER){
                    //excute seacrh
                    geoLocate();
                }
                return false;
            }
        });*/

        // ======================================================//
        //       Suggestion
/*        materialSearchBar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
            @Override
            public void onSearchStateChanged(boolean enabled) {

            }

            @Override
            public void onSearchConfirmed(CharSequence text) {
                startSearch(text.toString(),true,null,true);
            }

            @Override
            public void onButtonClicked(int buttonCode) {
                if (buttonCode == MaterialSearchBar.BUTTON_NAVIGATION){

                }else if(buttonCode == MaterialSearchBar.BUTTON_BACK){
                    materialSearchBar.disableSearch();
                }
            }
        });

        materialSearchBar.addTextChangeListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                FindAutocompletePredictionsRequest predictionsRequest = FindAutocompletePredictionsRequest.builder()
                        .setCountry("vi")
                        .setTypeFilter(TypeFilter.ADDRESS)
                        .setSessionToken(token)
                        .setQuery(s.toString())
                        .build();
                placesClient.findAutocompletePredictions(predictionsRequest).addOnCompleteListener(new OnCompleteListener<FindAutocompletePredictionsResponse>() {
                    @Override
                    public void onComplete(@NonNull Task<FindAutocompletePredictionsResponse> task) {
                        if(task.isSuccessful()){
                            FindAutocompletePredictionsResponse predictionsResponse = task.getResult();
                            if (predictionsResponse != null){
                                predictionList = predictionsResponse.getAutocompletePredictions();

                                List<String> suggestionList = new ArrayList<>();
                                for (int i = 0 ; i < predictionList.size(); ++ i){
                                    AutocompletePrediction prediction = predictionList.get(i);
                                    suggestionList.add(prediction.getFullText(null).toString());
                                }

                                materialSearchBar.updateLastSuggestions(suggestionList);
                                if (!materialSearchBar.isSuggestionsVisible()){
                                    materialSearchBar.showSuggestionsList();
                                }
                            }
                        }else {
                            Log.i(TAG,"Prediction fetching task unsuccessful!");
                        }
                    }
                });
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });*/

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

        //marker.showInfoWindow();
        // map.moveCamera(CameraUpdateFactory.newLatLngZoom(KHTN,17));


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
            }else
            {
                ActivityCompat.requestPermissions(this,
                        permissions,
                        LOCATION_PERMISSION_REQUEST_CODE);
            }
        }else
        {
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
                    for (int i = 0; i < grantResults.length; ++i) {
                        if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                            mLocationPermissionGranted = false;
                            return;
                        }
                    }
                    mLocationPermissionGranted = true;
                    //if all permission granted => init map
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
            getDeviceLocation();

            if(ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) !=
                    PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                return;

            map.setMyLocationEnabled(true);   // my location (blue dot)
            map.getUiSettings().setMyLocationButtonEnabled(false);
            map.getUiSettings().setZoomControlsEnabled(true);

            map.setOnMapClickListener(this);


            init();

        }

//        LatLng KHTN = new LatLng(10.762643, 106.682079);
//        LatLng NHStreet = new LatLng(10.774467, 106.703274);
//
//        MarkerOptions options = new MarkerOptions();
//        options.position(KHTN);
//        options.title("Đại học KHTN").snippet("Số 227 NVC, Q5");
//        options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
//        options.alpha(0.8f);
//        options.rotation(0);
//        Marker marker = map.addMarker(options);
//        marker.showInfoWindow();
//        map.moveCamera(CameraUpdateFactory.newLatLngZoom(KHTN,17));
//
//        MarkerOptions options2 = new MarkerOptions();
//        options.position(NHStreet);
//        options.title("Phố đi bộ NH").snippet("Quận 1 HCM");
//        options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
//        options.alpha(0.8f);
//        options.rotation(0);
//        Marker marker2 = map.addMarker(options);


//        final AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
//
//            @Override
//            protected Void doInBackground(Void... params) {
//
//                // 227 Nguyễn Văn Cừ : 10.762643, 106.682079
//                // Phố đi bộ Nguyễn Huệ : 10.774467, 106.703274
//
//                String request = makeURL("10.762643","106.682079","10.774467","106.703274");
//                GetDirectionsTask task = new GetDirectionsTask(request);
//                ArrayList<LatLng> list = task.testDirection();
//                for (LatLng latLng : list) {
//                    listStep.add(latLng);
//                }
//                return null;
//            }
//            @Override
//            protected void onPostExecute(Void result) {
//                // TODO Auto-generated method stub
//                super.onPostExecute(result);
//                polyline.addAll(listStep);
//                Polyline line = map.addPolyline(polyline);
//                line.setColor(Color.BLUE);
//                line.setWidth(5);
//            }
//        };
//
//        task.execute();
    }

    private void HideSoftKeyboard(){
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }


    @Override
    public void onMapClick(LatLng latLng) {
        Log.d(TAG,"LAT: "+ latLng.latitude + " LONG: "+ latLng.longitude);
        // Toast.makeText(this,"LAT: "+ latLng.latitude + " LONG: ", Toast.LENGTH_SHORT).show();
        //desMarker = map.addMarker(new MarkerOptions()
        //        .position(latLng));
        //currentLatlngSelected
        if(StartPoint == null){
            Marker myLocation = map.addMarker(new MarkerOptions()
                    .position(latLng)
                    .title("Start Tour"));
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
            Marker newmarker = map.addMarker(new MarkerOptions()
                    .position(latLng)
                    .title("Stop point"));

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
                startActivityForResult(intent, 1);




            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        //
//        Geocoder geocoder;
//
//        List<Address> addresses;
//        geocoder = new Geocoder(this, Locale.getDefault());
//        try {
//            addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
//            String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
//            String city = addresses.get(0).getLocality();
//            String state = addresses.get(0).getAdminArea();
//            String country = addresses.get(0).getCountryName();
//            String postalCode = addresses.get(0).getPostalCode();
//            String knownName = addresses.get(0).getFeatureName();
//
//            moveCamera(latLng,DEFAULT_ZOOM,address);
//        } catch (IOException e) {
//            e.printStackTrace();
//            Log.d(TAG,"Geocoder destination error");
//        }
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

        polylinePaths.clear();

        for (Route r : routes) {
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(r.startLocation, 16));

            originMarkers.add(map.addMarker(new MarkerOptions()
                    //.icon(BitmapDescriptorFactory.fromResource(R.drawable.start_blue))
                    .title(r.startAddress)
                    .position(r.startLocation)));
            destinationMarkers.add(map.addMarker(new MarkerOptions()
                    //.icon(BitmapDescriptorFactory.fromResource(R.drawable.end_green))
                    .title(r.endAddress)
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
        switch (v.getId())
        {
            case R.id.hotel:
                map.clear();
                String url = getUrlNearby(currentLatlngSelected.latitude, currentLatlngSelected.longitude, hospital);
                transferData[0] = map;
                transferData[1] = url;

                getNearbyPlaces.execute(transferData);
                Toast.makeText(this, "Searching for Nearby Hospitals...", Toast.LENGTH_SHORT).show();
                Toast.makeText(this, "Showing Nearby Hospitals...", Toast.LENGTH_SHORT).show();
                break;


            case R.id.restaurant:
                map.clear();
                url = getUrlNearby(currentLatlngSelected.latitude, currentLatlngSelected.longitude, restaurant);
                transferData[0] = map;
                transferData[1] = url;

                getNearbyPlaces.execute(transferData);
                Toast.makeText(this, "Searching for Nearby Restaurant...", Toast.LENGTH_SHORT).show();
                Toast.makeText(this, "Showing Nearby Restaurant...", Toast.LENGTH_SHORT).show();
                break;


            case R.id.rest_station:
                map.clear();
                url = getUrlNearby(currentLatlngSelected.latitude, currentLatlngSelected.longitude, rest_station);
                transferData[0] = map;
                transferData[1] = url;

                getNearbyPlaces.execute(transferData);
                Toast.makeText(this, "Searching for Nearby Rest Station...", Toast.LENGTH_SHORT).show();
                Toast.makeText(this, "Showing Nearby Rest Station...", Toast.LENGTH_SHORT).show();
                break;

            case R.id.other:
                map.clear();
                url = getUrlNearby(currentLatlngSelected.latitude, currentLatlngSelected.longitude, other);
                transferData[0] = map;
                transferData[1] = url;

                getNearbyPlaces.execute(transferData);
                Toast.makeText(this, "Searching for Nearby Places...", Toast.LENGTH_SHORT).show();
                Toast.makeText(this, "Showing Nearby Places...", Toast.LENGTH_SHORT).show();
                break;
                //Complete button click here
            case R.id.complete_add_stop_point:

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
        if (resultCode == RESULT_OK) {
            String stopPointName = data.getStringExtra("REPLY_STOP_POINT_NAME");
     //       String serviceType = data.getStringExtra("REPLY_SERVICE_TYPE");
    //        String province = data.getStringExtra("REPLY_PROVINCE");

            Log.d(TAG,stopPointName + "  "  );

        }
    }

}

// Direction https://github.com/hiepxuan2008/GoogleMapDirectionSimple/blob/master/app/src/main/java/com/itshareplus/googlemapdemo/MapsActivity.java

// https://github.com/hiepxuan2008/GoogleMapDirectionSimple
// nearby
//https://drive.google.com/drive/folders/1oC1EKNPyd0j6RilwJ-w4YYpKd-6XCn0s