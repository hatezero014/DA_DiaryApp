package com.example.doan_diaryapp.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doan_diaryapp.Models.Circle;
import com.example.doan_diaryapp.R;

import java.util.List;

public class CircleAdapter extends RecyclerView.Adapter<CircleAdapter.CircleViewHolder> {
    private List<Circle> mCircle;

    public void setData(List<Circle> list){
        this.mCircle = list;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public CircleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_circle,parent,false);

        return new CircleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CircleViewHolder holder, int position) {
        Circle circle = mCircle.get(position);
        if(circle == null){
            return;
        }

        holder.imgCircle.setImageResource(circle.getResourceId());
    }

    @Override
    public int getItemCount() {
        if(mCircle != null)
            return mCircle.size();
        return 0;
    }

    public class CircleViewHolder extends RecyclerView.ViewHolder{

        private ImageView imgCircle;
        public CircleViewHolder(@NonNull View itemView) {
            super(itemView);

            imgCircle = itemView.findViewById(R.id.img_circle);
        }
    }
}
