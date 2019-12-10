package com.maihuythong.testlogin.showTourInfo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.maihuythong.testlogin.R;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class MemberAdapter extends ArrayAdapter {
    private Context context;
    private int layoutInvite;
    private ArrayList<Member> data = new ArrayList<>();


    public MemberAdapter(@NonNull Context context, int layoutInvite, ArrayList<Member> data) {
        super(context, layoutInvite, data);
        this.layoutInvite = layoutInvite;
        this.context = context;
        this.data = data;


    }

    class ViewHolder{
        TextView userId;
        ImageView userAvatar;
        TextView userName;
        TextView isHost;
        TextView phone;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder = null;

        final MemberAdapter.ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(this.layoutInvite, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.userId = convertView.findViewById(R.id.member_id);
            viewHolder.userAvatar = convertView.findViewById(R.id.member_avatar);
            viewHolder.userName = convertView.findViewById(R.id.member_name);
            viewHolder.phone = convertView.findViewById(R.id.member_phone);
            viewHolder.isHost = convertView.findViewById(R.id.member_host);

            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final Member member = data.get(position);
//        holder.avatar.setImageResource(null);
        viewHolder.userName.setText("Name: " + member.getName());
        viewHolder.phone.setText("Phone: " + member.getPhone());
        if (member.getAvatar() != null){
            Picasso.get().load(member.getAvatar()).into(viewHolder.userAvatar);
        }else {
            viewHolder.userAvatar = null;                                               // Add default avatar here
        }
        viewHolder.userId.setText("ID: " + member.getId());
        if (member.isHost()){
            viewHolder.isHost.setText("Host: Yes");
        }else{
            viewHolder.isHost.setText("Host: No");
        }

        return convertView;
    }
}
