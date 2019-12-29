package com.maihuythong.testlogin.MainTabbedLayout;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.maihuythong.testlogin.CreateTourActivity;
import com.maihuythong.testlogin.LoginActivity;
import com.maihuythong.testlogin.R;
import com.maihuythong.testlogin.ShowSystemTourInfo.ShowSystemTourInfo;
import com.maihuythong.testlogin.showListStopPointSystem.ListStopPointSystemActivity;
import com.maihuythong.testlogin.showListStopPointSystem.StopPointSystemRes;
import com.maihuythong.testlogin.showListStopPoints.StopPointAdapter;
import com.maihuythong.testlogin.showTourInfo.StopPoint;
import com.maihuythong.testlogin.showlist.CustomAdapter;
import com.maihuythong.testlogin.showlist.ShowListReq;
import com.maihuythong.testlogin.showlist.Tour;
import com.maihuythong.testlogin.signup.APIService;
import com.maihuythong.testlogin.signup.ApiUtils;
import com.maihuythong.testlogin.stopPointInfo.StopPointDetailActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;
import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Frag3Main.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Frag3Main#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Frag3Main extends Fragment {
    StopPoint[] arrStopPoints;
    ListView lvStopPoints;

    private TextView totalStopPointView;
    private TextView visibleStopPointView;
    private long totalStopPoint;

    ArrayList<StopPoint> totalStopPointArray = new ArrayList<>();
    ArrayList<StopPoint> visitbleStopPointArray  = new ArrayList<>();
    private int visitbleItemCapacity=10;
    private int currentPos=0;
    private boolean isLoading =false;

    private View mView;
    private AppCompatActivity mActivity;
    private OnFragmentInteractionListener mListener;

    public Frag3Main() {
        // Required empty public constructor
    }

    public static Frag3Main newInstance() {
        Frag3Main fragment = new Frag3Main();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.activity_list_stop_point_system, container, false);
        mActivity = ((AppCompatActivity)getActivity());

        Toolbar toolbar = mView.findViewById(R.id.toolbar);
        mActivity.setSupportActionBar(toolbar);

        lvStopPoints = (ListView)mView.findViewById(R.id.lv_stop_point_system);
        totalStopPointView = mView.findViewById(R.id.total_stop_point_system);
        visibleStopPointView = mView.findViewById(R.id.item_visitble_capacity_list_stop_point);

        new LoadStopPointSystemAsyncTask(mActivity).execute();

        visibleStopPointView.setText(String.valueOf(visitbleItemCapacity));

        return mView;
    }


    private class LoadStopPointSystemAsyncTask extends AsyncTask<Void, Void, Void>{

        private ProgressDialog dialog;

        LoadStopPointSystemAsyncTask(Activity activity) {
            dialog = new ProgressDialog(activity);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setMessage("Loading stop points, please wait....");
            dialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            getStopPointSystem(dialog);

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // do UI work here
            super.onPostExecute(result);
        }
    }


    private void getStopPointSystem(final ProgressDialog dialog){
        String token = GetTokenLoginAccess();
        APIService mAPIService = ApiUtils.getAPIService();
        mAPIService.searchDestination(token,"",null,null,1,2000).enqueue(new Callback<StopPointSystemRes>() {
            @Override
            public void onResponse(Call<StopPointSystemRes> call, Response<StopPointSystemRes> response) {
                if(response.code()==200)
                {

                    assert response.body() != null;
                    arrStopPoints=response.body().getStopPoints();
                    totalStopPoint=response.body().getTotal();

                    totalStopPointView.setText(String.valueOf(totalStopPoint));

                    totalStopPointArray = new ArrayList<>(Arrays.asList(arrStopPoints));
                    LoadData();
                    lvStopPoints.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            showInfoStopPoint(totalStopPointArray,position);
                        }
                    });

                    Toast.makeText(getContext(),"Get stop points finished!", Toast.LENGTH_LONG).show();
                    dialog.dismiss();
                }
                if(response.code()==500)
                {
                    String message = "Send failed!";
                    try {
                        assert response.errorBody() != null;
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        message = jObjError.getString("message");
                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                    }

                    if(message.isEmpty()) message="Send failed!";
                    Toast.makeText(getContext(), "Error: " + message, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<StopPointSystemRes> call, Throwable t) {
                Toast.makeText(getContext(), "Error: failed", Toast.LENGTH_LONG).show();
            }
        });
    }


    private String GetTokenLoginAccess(){
        SharedPreferences sf;
        String token;
        token = LoginActivity.token;
        if(token== null){
            sf = mActivity.getSharedPreferences("com.maihuythong.testlogin_preferences", MODE_PRIVATE);
            token = sf.getString("login_access_token", "");
        }
        return token;
    }


    private void LoadData(){

        if(totalStopPointArray.size()<visitbleItemCapacity)
        {
            visitbleStopPointArray.addAll(totalStopPointArray);
            currentPos=visitbleStopPointArray.size()-1;

        }else {

            for (int i = 0; i < visitbleItemCapacity; i++) {
                visitbleStopPointArray.add(totalStopPointArray.get(i));
                currentPos = i;
            }
        }
        StopPointAdapter stopPointAdapter = new StopPointAdapter(getApplicationContext(),R.layout.stop_point_card,visitbleStopPointArray);
        lvStopPoints.setAdapter(stopPointAdapter);

        lvStopPoints.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (lvStopPoints.getAdapter() == null)
                    return ;

                if (lvStopPoints.getAdapter().getCount() == 0)
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


    private void showInfoStopPoint(ArrayList<StopPoint> arrayList,int position){
        StopPoint newStopPoint;
        newStopPoint = arrayList.get(position);
        Intent intent = new Intent(getApplicationContext(), StopPointDetailActivity.class);
        intent.putExtra("id",newStopPoint.getId());

        intent.putExtra("serviceId","");
        intent.putExtra("address",newStopPoint.getAddress());
        intent.putExtra("provinceId",newStopPoint.getProvinceId());
        intent.putExtra("name",newStopPoint.getName());
        intent.putExtra("lat",newStopPoint.getLat().doubleValue());
        intent.putExtra("long",newStopPoint.getLong().doubleValue());
        intent.putExtra("arrivalAt","");
        intent.putExtra("leaveAt","");
        intent.putExtra("minCost",newStopPoint.getMinCost());
        intent.putExtra("maxCost",newStopPoint.getMaxCost());
        intent.putExtra("serviceTypeId",newStopPoint.getServiceTypeId());
        intent.putExtra("avatar",newStopPoint.getAvatar());
        startActivity(intent);
    }


    private void loadMoreData(){
        if(totalStopPointArray.size()<visitbleStopPointArray.size()) return;

        if(totalStopPointArray.size()<= currentPos + visitbleItemCapacity)
        {
            visitbleStopPointArray.addAll(totalStopPointArray);
        }else{
            for (int i=currentPos;i<currentPos + visitbleItemCapacity;i++){
                visitbleStopPointArray.add(totalStopPointArray.get(i));
            }
        }

        currentPos = visitbleStopPointArray.size()-1;
        StopPointAdapter stopPointAdapter = new StopPointAdapter(getApplicationContext(),R.layout.stop_point_card,visitbleStopPointArray);
        lvStopPoints.setAdapter(stopPointAdapter);
        lvStopPoints.setSelection(currentPos-visitbleItemCapacity);
        isLoading=false;
        visibleStopPointView.setText(String.valueOf(visitbleStopPointArray.size()));
    }








    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
