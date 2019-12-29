package com.maihuythong.testlogin.ShowNotiOnRoadList;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.maihuythong.testlogin.R;
import com.maihuythong.testlogin.signup.APIService;
import com.maihuythong.testlogin.signup.ApiUtils;
import com.maihuythong.testlogin.userInfo.UserInfoRes;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotiOnRoadNotChatAdapter extends RecyclerView.Adapter<NotiOnRoadNotChatAdapter.ViewHolder> {
    private ArrayList<NotiOnRoad> notis;

    public NotiOnRoadNotChatAdapter(ArrayList<NotiOnRoad> notis) {
        this.notis = notis;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_noti_on_road, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        NotiOnRoad n = notis.get(position);

        TextView notiType = holder.notiType;
        TextView notiContent = holder.notiContent;
        TextView notiSpeed = holder.notiSpeed;

        notiType.setText(n.getNotificationType() + "");
        notiContent.setText(n.getNote());
        notiSpeed.setText(n.getSpeed() + "");
    }

    @Override
    public int getItemCount() {
        return notis.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView notiType;
        public TextView notiContent;
        public TextView notiSpeed;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            notiType = itemView.findViewById(R.id.noti_type);
            notiContent = itemView.findViewById(R.id.noti_content);
            notiSpeed = itemView.findViewById(R.id.noti_speed);
        }
    }
}
