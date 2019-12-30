package com.maihuythong.testlogin.ShowSystemTourInfo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.Snackbar;
import com.maihuythong.testlogin.LoginActivity;
import com.maihuythong.testlogin.R;
import com.maihuythong.testlogin.ShowSPInformation.ShowSPInformation;
import com.maihuythong.testlogin.showListStopPoints.StopPointAdapter;
import com.maihuythong.testlogin.showTourInfo.GetTourInfo;
import com.maihuythong.testlogin.showTourInfo.StopPoint;
import com.maihuythong.testlogin.signup.APIService;
import com.maihuythong.testlogin.signup.ApiUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

public class Frag5STI extends Fragment {
    StopPoint[] arrStopPoints;
    ListView lvStopPoints;
    private View mView;

    private final static String TOUR_ID = "tourId";

    private long mTourId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mTourId = getArguments().getLong(TOUR_ID);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_frag5_sti, container, false);

        lvStopPoints = mView.findViewById(R.id.lv_stop_points);
        GetStopPoints(mTourId);

        return mView;
    }

    public static Frag5STI newInstance(long tourId) {
        Frag5STI fragment = new Frag5STI();
        Bundle args = new Bundle();
        args.putLong(TOUR_ID, tourId);
        fragment.setArguments(args);
        return fragment;
    }

    private void GetStopPoints(final long tourId){
        String token = GetTokenLoginAccess();
        APIService mAPIService = ApiUtils.getAPIService();

        mAPIService.getTourInfo(token,tourId).enqueue(new Callback<GetTourInfo>() {
            @Override
            public void onResponse(Call<GetTourInfo> call, Response<GetTourInfo> response) {
                if(response.code()==200) {
                    GetTourInfo tourInfo = response.body();

                    arrStopPoints = tourInfo.getStopPoints();

                    final ArrayList<StopPoint> arrayStopPoints = new ArrayList<>(Arrays.asList(arrStopPoints));

                    StopPointAdapter stopPointAdapter = new StopPointAdapter(mView.getContext(), R.layout.stop_point_card,arrayStopPoints);
                    lvStopPoints.setAdapter(stopPointAdapter);

                    lvStopPoints.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            showInfoStopPoint(arrayStopPoints,position);
                        }
                    });

                    Snackbar.make(mView, "Get stop points finished!", Snackbar.LENGTH_SHORT).show();
                }

                if(response.code()==404||response.code()==500)
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
                    Snackbar.make(mView, "Error: " + message, Snackbar.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GetTourInfo> call, Throwable t) {
                Snackbar.make(mView, "Error: Can't get stop points! ", Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    private String GetTokenLoginAccess(){
        SharedPreferences sf;
        String token;
        token = LoginActivity.token;
        if(token== null){
            sf = mView.getContext().getSharedPreferences("com.maihuythong.testlogin_preferences", MODE_PRIVATE);
            token = sf.getString("login_access_token", "");
        }
        return token;
    }

    private void showInfoStopPoint(ArrayList<StopPoint> arrayList,int position){
        StopPoint newStopPoint;
        newStopPoint = arrayList.get(position);

        Intent intent = new Intent(mView.getContext(), ShowSPInformation.class);
        intent.putExtra("id",newStopPoint.getId());
        intent.putExtra("serviceId",newStopPoint.getServiceId());
        intent.putExtra("address",newStopPoint.getAddress());
        intent.putExtra("provinceId",newStopPoint.getProvinceId());
        intent.putExtra("name",newStopPoint.getName());
        intent.putExtra("lat",newStopPoint.getLat().doubleValue());
        intent.putExtra("long",newStopPoint.getLong().doubleValue());
        intent.putExtra("arrivalAt",newStopPoint.getArrivalAt());
        intent.putExtra("leaveAt",newStopPoint.getLeaveAt());
        intent.putExtra("minCost",newStopPoint.getMinCost());
        intent.putExtra("maxCost",newStopPoint.getMaxCost());
        intent.putExtra("serviceTypeId",newStopPoint.getServiceTypeId());
        intent.putExtra("avatar",newStopPoint.getAvatar());
        startActivity(intent);
    }

}
