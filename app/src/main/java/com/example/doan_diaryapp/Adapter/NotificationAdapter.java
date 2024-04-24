package com.example.doan_diaryapp.Adapter;

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

    public NotificationAdapter(List<Notification> notificationList) {
        this.notificationList = notificationList;
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
        holder.imageView.setImageResource(R.drawable.img);
        holder.Date.setText(notification.getTime());
        holder.Content.setText(notification.getNotificationContent());
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

    class DateViewHolder extends RecyclerView.ViewHolder{
        private TextView tv_date;

        public DateViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_date = itemView.findViewById(R.id.tv_date);
        }
    }
}
