package com.maihuythong.testlogin.rate_comment_review;

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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class CommentAdapter extends ArrayAdapter {
    private Context context;
    private int layoutInvite;
    private ArrayList<Comment> data = new ArrayList<>();


    public CommentAdapter(@NonNull Context context, int layoutInvite, ArrayList<Comment> data) {
        super(context, layoutInvite,data);
        this.layoutInvite = layoutInvite;
        this.context = context;
        this.data = data;


    }

    class ViewHolder{
        long userId;
        ImageView userAvatar;
        TextView userName;
        TextView contentComment;
        TextView dateComment;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder = null;

        final CommentAdapter.ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(this.layoutInvite, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.contentComment = convertView.findViewById(R.id.content_comment);
            viewHolder.userAvatar = convertView.findViewById(R.id.user_avatar_comment);
            viewHolder.userName = convertView.findViewById(R.id.user_name_comment);
            viewHolder.dateComment = convertView.findViewById(R.id.tour_comment_create_on);

            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final Comment comment = data.get(position);
//        holder.avatar.setImageResource(null);
        viewHolder.userName.setText(comment.getName());
        viewHolder.contentComment.setText(comment.getComment());
        //viewHolder.userAvatar = null;                                               // Add avatar here

        if (comment.getAvatar() != null){
            Picasso.get().load(comment.getAvatar()).into(viewHolder.userAvatar);
        }else {
            viewHolder.userAvatar = null;                                               // Add default avatar here
        }

        viewHolder.userId = comment.getUserId();

        String dateString = new SimpleDateFormat("MM/dd/yyyy").format(new Date(comment.getCreateOn()));
        String timeString = new SimpleDateFormat("HH:mm").format(new Date(comment.getCreateOn()));
        viewHolder.dateComment.setText(dateString + " at " + timeString);

        return convertView;
    }
}
