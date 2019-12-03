package com.maihuythong.testlogin.ShowListUsers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.maihuythong.testlogin.R;

import java.util.ArrayList;
import java.util.List;

public class UsersAdapter extends ArrayAdapter<User> {
    private Context context;
    private int resource;
    private List<User> arrUser;

    public UsersAdapter(@NonNull Context context, int resource, ArrayList<User> arrUser) {
        super(context, resource,arrUser);
        this.context = context;
        this.resource = resource;
        this.arrUser = arrUser;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(this.resource, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.userId = (TextView) convertView.findViewById(R.id.userId);
            viewHolder.userName= (TextView) convertView.findViewById(R.id.userName);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        User user = arrUser.get(position);
        viewHolder.userId.setText("Mã người dùng: " + user.getID());
        viewHolder.userName.setText("Tên người dùng: " + user.getFullName());
        return convertView;
    }

    public class ViewHolder {
        public TextView userId;
        public TextView userName;
    }
    public User getItem(int position){
        return arrUser.get(position);
    }
}