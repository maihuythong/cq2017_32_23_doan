package com.maihuythong.testlogin.MainTabbedLayout;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
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

import com.maihuythong.testlogin.LoginActivity;
import com.maihuythong.testlogin.R;
import com.maihuythong.testlogin.invitationTour.AdapterInvitation;
import com.maihuythong.testlogin.invitationTour.Invitation;
import com.maihuythong.testlogin.invitationTour.InvitationActivity;
import com.maihuythong.testlogin.invitationTour.ShowInvitationReq;
import com.maihuythong.testlogin.showListStopPointSystem.StopPointSystemRes;
import com.maihuythong.testlogin.showListStopPoints.StopPointAdapter;
import com.maihuythong.testlogin.showTourInfo.StopPoint;
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
 * {@link Frag4Main.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Frag4Main#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Frag4Main extends Fragment {
    private Invitation invitation[];

    private View mView;
    private AppCompatActivity mActivity;
    private OnFragmentInteractionListener mListener;

    public Frag4Main() {
        // Required empty public constructor
    }

    public static Frag4Main newInstance() {
        Frag4Main fragment = new Frag4Main();
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
        mView = inflater.inflate(R.layout.activity_invitation, container, false);
        mActivity = ((AppCompatActivity)getActivity());

//        Toolbar toolbar = mView.findViewById(R.id.toolbar);
//        mActivity.setSupportActionBar(toolbar);

        String s = LoginActivity.token;
        if(s == null){
            SharedPreferences sf = mActivity.getSharedPreferences("com.maihuythong.testlogin_preferences", MODE_PRIVATE);
            s = sf.getString("login_access_token", "");
        }

        APIService mAPIService = ApiUtils.getAPIService();
        mAPIService.getInvitation(s,1,80).enqueue(new Callback<ShowInvitationReq>() {
            @Override
            public void onResponse(Call<ShowInvitationReq> call, Response<ShowInvitationReq> response) {
                if(response.code() == 200){
                    invitation = response.body().getInvitations();

                    ArrayList<Invitation> arrInv = new ArrayList<>();
                    ListView lvInvitation = mView.findViewById(R.id.invitation_tour);

                    for(int i = 0; i<invitation.length; i++){
                        arrInv.add(invitation[i]);
                    }
                    AdapterInvitation adapterInvitation =
                            new AdapterInvitation(getContext(),R.layout.custom_invitation_list, arrInv);
//                    lvInvitation.setItemsCanFocus(false);
                    lvInvitation.setAdapter(adapterInvitation);
                }
            }

            @Override
            public void onFailure(Call<ShowInvitationReq> call, Throwable t) {

            }
        });

        return mView;
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
