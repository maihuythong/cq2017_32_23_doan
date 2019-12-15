package com.maihuythong.testlogin.invitationTour;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.maihuythong.testlogin.LoginActivity;
import com.maihuythong.testlogin.R;
import com.maihuythong.testlogin.manager.MyApplication;
import com.maihuythong.testlogin.signup.APIService;
import com.maihuythong.testlogin.signup.ApiUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdapterInvitation extends ArrayAdapter {
    private Context context;
    private int layoutInvite;
    private ArrayList<Invitation> data = new ArrayList<>();


    public AdapterInvitation(@NonNull Context context, int layoutInvite, ArrayList<Invitation> data) {
        super(context, layoutInvite,data);
        this.layoutInvite = layoutInvite;
        this.context = context;
        this.data = data;


    }

    class ViewHolder{
        long tourId;
        TextView inviter;
        TextView tourName;
        ImageView avatar;
        TextView timeInvite;
        Button confirm;
        Button delete;
    }

    private Invitation getInvitationFromTourId(long tourId){
        for (int i = 0; i < data.size(); ++i) {
            if (data.get(i).getId() == tourId)
                return data.get(i);
        }
        return null;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder = null;

        final AdapterInvitation.ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(this.layoutInvite, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.avatar = convertView.findViewById(R.id.avatar_inviter);
            viewHolder.inviter = convertView.findViewById(R.id.inviter_name);
            viewHolder.tourName = convertView.findViewById(R.id.invite_tour_name);
            viewHolder.timeInvite = convertView.findViewById(R.id.time_invite);
            viewHolder.confirm = convertView.findViewById(R.id.button_invite_confirm);
            viewHolder.delete = convertView.findViewById(R.id.button_invite_delete);


            convertView.setTag(viewHolder);

        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final Invitation invitation = data.get(position);
//        holder.avatar.setImageResource(null);
        viewHolder.inviter.setText(invitation.getHostName());
        viewHolder.tourName.setText(invitation.getTourName());
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/YYYY");
        String dateString = new SimpleDateFormat("MM/dd/yyyy").format(new Date(invitation.getTimeInvite()));
        String timeString = new SimpleDateFormat("HH:mm").format(new Date(invitation.getTimeInvite()));
        viewHolder.timeInvite.setText(dateString + " at " + timeString);
        viewHolder.tourId = invitation.getId();

        viewHolder.confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
                String token = preferences.getString("login_access_token", null);
                APIService mAPIService = ApiUtils.getAPIService();
                mAPIService.responseInvitation(token, viewHolder.tourId, true).enqueue(new Callback<responseInvitation>() {
                    @Override
                    public void onResponse(Call<responseInvitation> call, Response<responseInvitation> response) {
                        Toast.makeText(getContext(),"Confirm Click success", Toast.LENGTH_SHORT).show();
                        Invitation invitation1 = getInvitationFromTourId(viewHolder.tourId);
                        data.remove(invitation1);
                        notifyDataSetChanged();
                    }

                    @Override
                    public void onFailure(Call<responseInvitation> call, Throwable t) {

                    }
                });
            }
        });




        viewHolder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
                String token = preferences.getString("login_access_token", null);
                APIService mAPIService = ApiUtils.getAPIService();
                mAPIService.responseInvitation(token, viewHolder.tourId, false).enqueue(new Callback<responseInvitation>() {
                    @Override
                    public void onResponse(Call<responseInvitation> call, Response<responseInvitation> response) {
                        Toast.makeText(getContext(), "Delete Click", Toast.LENGTH_SHORT).show();
    //                        ListView lvInvitation = findViewbyId(R.id.lv_)
                        Invitation invitation1 = getInvitationFromTourId(viewHolder.tourId);
                        data.remove(invitation1);
                        notifyDataSetChanged();
                    }

                    @Override
                    public void onFailure(Call<responseInvitation> call, Throwable t) {

                    }
                });
            }
        });

        return convertView;
    }

}
