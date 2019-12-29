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
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.maihuythong.testlogin.LoginActivity;
import com.maihuythong.testlogin.R;
import com.maihuythong.testlogin.showAccountTours.CustomAdapterAccountTours;
import com.maihuythong.testlogin.showAccountTours.ShowAccountToursReq;
import com.maihuythong.testlogin.showTourInfomation.ShowTourInformation;
import com.maihuythong.testlogin.showlist.Tour;
import com.maihuythong.testlogin.signup.APIService;
import com.maihuythong.testlogin.signup.ApiUtils;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Frag1Main.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Frag1Main#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Frag1Main extends Fragment {
    private Tour[] t;
    private long totalAccTours;
    private ListView lvTour;
    ArrayList<Tour> arrTour = new ArrayList<>();

    private View mView;
    private AppCompatActivity mActivity;
    private OnFragmentInteractionListener mListener;

    public Frag1Main() {
        // Required empty public constructor
    }

    public static Frag1Main newInstance() {
        Frag1Main fragment = new Frag1Main();
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
        mView = inflater.inflate(R.layout.activity_show_account_tours, container, false);
        mActivity = ((AppCompatActivity)getActivity());

        Toolbar toolbar = mView.findViewById(R.id.toolbar);
        mActivity.setSupportActionBar(toolbar);
        lvTour = mView.findViewById(R.id.lv_tour);

        new LoadAccTourAsyncTask(getActivity()).execute();

        return mView;
    }


    private class LoadAccTourAsyncTask extends AsyncTask<Void, Void, Void>{

        private ProgressDialog dialog;

        LoadAccTourAsyncTask(Activity activity) {
            dialog = new ProgressDialog(activity);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setMessage("Loading tours, please wait....");
            dialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            String s;
            s = LoginActivity.token;
            if(s == null){
                SharedPreferences sf = mActivity.getSharedPreferences("com.maihuythong.testlogin_preferences", MODE_PRIVATE);
                s = sf.getString("login_access_token", "");
            }

            APIService mAPIService = ApiUtils.getAPIService();

            mAPIService.getAccountTours(s,1, 200).enqueue(new Callback<ShowAccountToursReq>() {
                @Override
                public void onResponse(Call<ShowAccountToursReq> call, Response<ShowAccountToursReq> response) {
                    if(response.code() == 200){
                        t = response.body().getTours();
                        totalAccTours = response.body().getTotal();

                        for(int i= t.length-1; i>=0; i--){
                            if(t[i].getStatus()!=-1)
                                arrTour.add(t[i]);
                        }

                        CustomAdapterAccountTours customAdaperAccountTours =
                                new CustomAdapterAccountTours(getContext(), R.layout.row_listview_account_tours,arrTour);
                        lvTour.setAdapter(customAdaperAccountTours);

                        lvTour.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                sendTourInfo(arrTour, position);
                            }
                        });

                        dialog.dismiss();
                    }
                    else{
                        //TODO
                    }
                }

                @Override
                public void onFailure(Call<ShowAccountToursReq> call, Throwable throwable) {
                    //TODO
                }
            });
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // do UI work here
            super.onPostExecute(result);
        }
    }


    private  void sendTourInfo(ArrayList<Tour> arrTour,int position){
        Tour tour;
        tour = arrTour.get(position);
        Intent intent = new Intent(getContext(), ShowTourInformation.class);
        intent.putExtra("id",tour.getID());
        startActivityForResult(intent, 9999);

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            if(requestCode == 9999){
                mActivity.finish();
                mActivity.overridePendingTransition(0, 0);
                startActivity(mActivity.getIntent().setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                mActivity.overridePendingTransition(0, 0);
            }
        }
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
