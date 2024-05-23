package com.example.doan_diaryapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doan_diaryapp.Models.Notification;
import com.example.doan_diaryapp.R;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationHolder> {
    List<Notification> notificationList;

    public void setData(List<Notification> list){
        notificationList =list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public NotificationHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_item, parent, false);
        return new NotificationHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationHolder holder, int position) {
        Notification notification = notificationList.get(position);
        if(notification==null) return;
        Context context = holder.imageView.getContext();
        holder.imageView.setImageResource(R.drawable.icon_noti);
        holder.Date.setText(notification.getTime());
        if(notification.getContent() == 1){
            holder.Content.setText(R.string.notification_1);
        }
        else if(notification.getContent() == 2){
            holder.Content.setText(R.string.notification_2);
        }
        else if(notification.getContent() == 3){
            String []sub = notification.getSub().split(";");
            String month = sub[0];
            String score = sub[1];
            String month1 ="";
            int number = Integer.parseInt(month);
            switch (number) {
                case 1:
                    month1 = context.getString(R.string.January);
                    break;
                case 2:
                    month1 = context.getString(R.string.February);
                    break;
                case 3:
                    month1 = context.getString(R.string.March);
                    break;
                case 4:
                    month1 = context.getString(R.string.April);
                    break;
                case 5:
                    month1 = context.getString(R.string.May);
                    break;
                case 6:
                    month1 = context.getString(R.string.June);
                    break;
                case 7:
                    month1 = context.getString(R.string.July);
                    break;
                case 8:
                    month1 = context.getString(R.string.August);
                    break;
                case 9:
                    month1 = context.getString(R.string.September);
                    break;
                case 10:
                    month1 = context.getString(R.string.October);
                    break;
                case 11:
                    month1 = context.getString(R.string.November);
                    break;
                case 12:
                    month1 = context.getString(R.string.December);
                    break;
            }

            String textNotification = context.getString(R.string.notification_3_1) + " " + month1 + " " + context.getString(R.string.notification_3_2) + " " + score;
            holder.Content.setText(textNotification);
        }
        else if(notification.getContent() == 4){
            holder.Content.setText(R.string.notification_4);
        }
        else if(notification.getContent() == 5){
            holder.Content.setText(R.string.notification_5);
        }
        else if(notification.getContent() == 6){
            holder.Content.setText(R.string.notification_6);
        }
        else if(notification.getContent() == 7){
            holder.Content.setText(R.string.notification_7);
        }
        else if(notification.getContent() == 8){
            holder.Content.setText(R.string.notification_8);
        }
    }

    @Override
    public int getItemCount() {
        return notificationList.size();
    }

    class NotificationHolder extends RecyclerView.ViewHolder{
        CardView cardView;
        ImageView imageView;
        TextView Content;
        TextView Date;
        public NotificationHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.item_notification);
            Content = itemView.findViewById(R.id.text_notification);
            Date = itemView.findViewById(R.id.date);
            cardView = itemView.findViewById(R.id.cardView);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }


}
