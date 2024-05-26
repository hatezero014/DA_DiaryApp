package com.example.doan_diaryapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doan_diaryapp.Models.Notification;
import com.example.doan_diaryapp.R;
import com.example.doan_diaryapp.Service.NotificationService;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationHolder> {
    List<Notification> notificationList;
    private boolean showCheckboxes = false;
    public void setShowCheckboxes(boolean show) {
        showCheckboxes = show;
        notifyDataSetChanged();
    }


    public void setData(List<Notification> list){
        notificationList =list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public NotificationHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.new_layout_notification, parent, false);
        return new NotificationHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationHolder holder, int position) {
        Notification notification = notificationList.get(position);
        if(notification==null) return;
        Context context = holder.imageView.getContext();
        if(notification.getIsRead() == 1){
            holder.imageView.setVisibility(View.VISIBLE);
            holder.cardView.setCardBackgroundColor(ContextCompat.getColor(context, R.color.background_cardview_notification));
            NotificationService notificationService = new NotificationService(context);
            notificationService.UpdateById(new Notification(notification.getTime(), notification.getDay(), notification.getContent(), notification.getSub(), 0), notification.getId() );
        }
//        holder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                holder.checkBox.setVisibility(View.VISIBLE);
//                return false;
//            }
//        });

        if(notification.getIsRead() == 0){
            holder.imageView.setVisibility(View.GONE);
        }
        holder.imageView.setImageResource(R.drawable.icon_blue_notification);
        holder.Date.setText(notification.getTime());

        if(notification.getContent() == 1){
            holder.textView.setText(R.string.notification_1);
        }
        else if(notification.getContent() == 2){
            holder.textView.setText(R.string.notification_2);
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
            holder.textView.setText(textNotification);
        }
        else if(notification.getContent() == 4){
            holder.textView.setText(R.string.notification_4);
        }
        else if(notification.getContent() == 5){
            holder.textView.setText(R.string.notification_5);
        }
        else if(notification.getContent() == 6){
            holder.textView.setText(R.string.notification_6);
        }
        else if(notification.getContent() == 7){
            String textNotification = context.getString(R.string.notification_7) + notification.getSub();
            holder.textView.setText(textNotification);
        }
        else if(notification.getContent() == 8){
            String textNotification = context.getString(R.string.notification_8_1) + " " + notification.getSub() + " " + context.getString(R.string.notification_8_2);
            holder.textView.setText(textNotification);
        }

        if(showCheckboxes) {
            holder.checkBox.setVisibility(View.VISIBLE);
        } else {
            holder.checkBox.setVisibility(View.GONE);
        }

        // Xử lý long click listener cho cardView để hiển thị checkbox
        holder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                setShowCheckboxes(true);
//                holder.checkBox.setChecked(true);
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return notificationList.size();
    }

    class NotificationHolder extends RecyclerView.ViewHolder{
        CardView cardView;
        ImageView imageView;
        TextView textView;
        TextView Date;
        CheckBox checkBox;
        public NotificationHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.icon_blue_noti);
            textView = itemView.findViewById(R.id.new_details);
            Date = itemView.findViewById(R.id.currrentTime);
            cardView = itemView.findViewById(R.id.card_view_notification);
            checkBox = itemView.findViewById(R.id.checkbox);
        }

    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }


}
