package com.maihuythong.testlogin.TourCoordinate;

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

public class NotiOnRoadAdapter extends RecyclerView.Adapter<NotiOnRoadAdapter.ViewHolder> {
    private ArrayList<noti> notis;
    private long userId;
    private long accountId;

    public NotiOnRoadAdapter(ArrayList<noti> notis, long userId, long accountId) {
        this.notis = notis;
        this.userId = userId;
        this.accountId = accountId;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_chat, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        noti n = notis.get(position);

        TextView senderMessage = holder.senderMessage;
        TextView receiverMessage = holder.receiverMessage;
        CircleImageView avatarSender = holder.avatarSender;

        if (n.getUserId().equals(String.valueOf(accountId))){
            senderMessage.setText(n.getNotification());
            receiverMessage.setVisibility(View.GONE);
            avatarSender.setVisibility(View.GONE);
        }else{
            senderMessage.setVisibility(View.GONE);
            receiverMessage.setText(n.getNotification());
            if (n.getAvatar() != null){
                Picasso.get().load(n.getAvatar()).into(avatarSender);
            }else {
                avatarSender.setImageResource(R.drawable.default_img);
            }
        }
        //senderMessage.setText(n.getNotification());
    }

    @Override
    public int getItemCount() {
        return notis.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView senderMessage;
        public TextView receiverMessage;
        public CircleImageView avatarSender;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            senderMessage = itemView.findViewById(R.id.sender_messsage_text);
            receiverMessage = itemView.findViewById(R.id.receiver_message_text);
            avatarSender = itemView.findViewById(R.id.message_receiver_avatar);
        }
    }
}
