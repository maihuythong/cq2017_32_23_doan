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
import com.maihuythong.testlogin.showAccountTours.CustomAdapterAccountTours;
import com.maihuythong.testlogin.showAccountTours.ShowAccountToursReq;
import com.maihuythong.testlogin.showTourInfomation.ShowTourInformation;
import com.maihuythong.testlogin.showlist.CustomAdapter;
import com.maihuythong.testlogin.showlist.ShowListActivity;
import com.maihuythong.testlogin.showlist.ShowListReq;
import com.maihuythong.testlogin.showlist.Tour;
import com.maihuythong.testlogin.signup.APIService;
import com.maihuythong.testlogin.signup.ApiUtils;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Frag2Main.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Frag2Main#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Frag2Main extends Fragment {
    private ListView lvTour;
    private Tour[] t;
    private SharedPreferences sf;
    private Toolbar toolbar;
    private TextView totalToursView;
    private TextView visibleItemView;
    private long totalTours;
    private final ArrayList<Tour> toursTotalArray = new ArrayList<>();
    private final ArrayList<Tour> toursVisitble = new ArrayList<>();

    private int visitbleItemCapacity = 10;


    private int currentPos=0;
    private boolean isLoading = false;

    private View mView;
    private AppCompatActivity mActivity;
    private OnFragmentInteractionListener mListener;

    public Frag2Main() {
        // Required empty public constructor
    }

    public static Frag2Main newInstance() {
        Frag2Main fragment = new Frag2Main();
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
        mView = inflater.inflate(R.layout.activity_show_list, container, false);
        mActivity = ((AppCompatActivity)getActivity());

        Toolbar toolbar = mView.findViewById(R.id.toolbar);
        mActivity.setSupportActionBar(toolbar);

        totalToursView = mView.findViewById(R.id.total_available_tours);
        visibleItemView = mView.findViewById(R.id.item_visitble_capacity_list_tour);

        visibleItemView.setText(String.valueOf(visitbleItemCapacity));
        try {
            PackageInfo pInfo = getContext().getPackageManager().getPackageInfo(mActivity.getPackageName(), 0);
            String version = pInfo.versionName;
            int verCode = pInfo.versionCode;
            Log.d("appVersion", verCode + "");
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        //init fab button
        FloatingActionButton fab = mView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddTour();
            }
        });

        new LoadTourAsyncTask( mActivity).execute();

        return mView;
    }


    private void AddTour() {
        startActivity(new Intent(mActivity, CreateTourActivity.class));
    }


    @SuppressLint("StaticFieldLeak")
    private class LoadTourAsyncTask extends AsyncTask<Void, Void, Void> {
        private ProgressDialog dialog;

        LoadTourAsyncTask(Activity activity) {
            dialog = new ProgressDialog(activity);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setMessage("Loading tours, please wait....");
            dialog.show();
        }
        @Override
        protected Void doInBackground(Void... args) {
            String s;
            s = LoginActivity.token;
            if(s == null){
                SharedPreferences sf = mActivity.getSharedPreferences("com.maihuythong.testlogin_preferences", MODE_PRIVATE);
                s = sf.getString("login_access_token", "");
            }

            APIService mAPIService = ApiUtils.getAPIService();
            Intent intent = mActivity.getIntent();

            mAPIService.getList(s,5000, 1).enqueue(new Callback<ShowListReq>() {
                @Override
                public void onResponse(Call<ShowListReq> call, Response<ShowListReq> response) {
                    if(response.code() == 200){
                        t = response.body().getTours();
                        totalTours =response.body().getTotal();
                        totalToursView.setText(Long.toString(totalTours));
                        lvTour = (ListView) mView.findViewById(R.id.lv_tour);

                        for (int i =0 ; i< t.length;i++)
                            if(t[i].getStatus()!=-1) toursTotalArray.add(t[i]);

                        LoadData();

                        lvTour.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                openRateCommentTour(toursVisitble, position);
                            }
                        });
                        Toast.makeText(getContext(),"Get tours finished!", Toast.LENGTH_LONG).show();
                        dialog.dismiss();
                    }
                    else{
                        //TODO
                    }
                }

                @Override
                public void onFailure(Call<ShowListReq> call, Throwable throwable) {
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


    private void LoadData(){

        if(toursTotalArray.size()<visitbleItemCapacity)
        {
            toursVisitble.addAll(toursTotalArray);
            currentPos=toursVisitble.size()-1;

        }else {

            for (int i = 0; i < visitbleItemCapacity; i++) {
                toursVisitble.add(toursTotalArray.get(i));
                currentPos = i;
            }
        }
        CustomAdapter customAdaper = new CustomAdapter(getContext() ,R.layout.row_listview,toursVisitble);
        lvTour.setAdapter(customAdaper);

        lvTour.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (lvTour.getAdapter() == null)
                    return ;

                if (lvTour.getAdapter().getCount() == 0)
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


    private void loadMoreData(){
        if(toursTotalArray.size()<toursVisitble.size()) return;

        if(toursTotalArray.size()<= currentPos + visitbleItemCapacity)
        {
            toursVisitble.addAll(toursTotalArray);
        }else{
            for (int i=currentPos;i<currentPos + visitbleItemCapacity;i++){
                toursVisitble.add(toursTotalArray.get(i));
            }
        }

        currentPos = toursVisitble.size()-1;
        CustomAdapter customAdaper = new CustomAdapter(getContext(),R.layout.row_listview,toursVisitble);
        lvTour.setAdapter(customAdaper);
        lvTour.setSelection(currentPos-visitbleItemCapacity);
        isLoading=false;
        visibleItemView.setText(String.valueOf(toursVisitble.size()));
    }


    private  void openRateCommentTour(ArrayList<Tour> arrTour,int position){
        Tour tt;
        tt = arrTour.get(position);
        Intent intent = new Intent(getContext(), ShowSystemTourInfo.class);
        intent.putExtra("pos", position);
        intent.putExtra("id", tt.getID());
        intent.putExtra("status", tt.getStatus());
        intent.putExtra("name", tt.getName());
        intent.putExtra("minCost", tt.getMinCost());
        intent.putExtra("maxCost", tt.getMaxCost());
        intent.putExtra("startDate", tt.getStartDate());
        intent.putExtra("endDate", tt.getEndDate());
        intent.putExtra("adult", tt.getAdults());
        intent.putExtra("child", tt.getChilds());
        intent.putExtra("isPrivate", tt.getIsPrivate());
        intent.putExtra("avatar", tt.getAvatar());
        startActivity(intent);
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
