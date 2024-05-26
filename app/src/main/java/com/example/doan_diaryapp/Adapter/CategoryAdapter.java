package com.example.doan_diaryapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doan_diaryapp.Models.Category;
import com.example.doan_diaryapp.R;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>{
    private Context context;
    List<Category> categoryList;

    public CategoryAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<Category> list){
        this.categoryList = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        Category category = categoryList.get(position);
        if(category==null) return;
        String day = context.getResources().getString(R.string.notification_day);
        holder.tv.setText(day + ": " + category.getNameCategory());

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, RecyclerView.VERTICAL,false);
        holder.rcv.setLayoutManager(linearLayoutManager);

        NotificationAdapter notificationAdapter = new NotificationAdapter();
        notificationAdapter.setData(category.getNotificationList());
        holder.rcv.setAdapter(notificationAdapter);


        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                // Thay đổi trạng thái hiển thị checkbox
                notificationAdapter.setShowCheckboxes(true);
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder{
        TextView tv;
        RecyclerView rcv;
        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            tv = itemView.findViewById(R.id.tv_category);
            rcv = itemView.findViewById(R.id.rcv_notification);
        }
    }
}
