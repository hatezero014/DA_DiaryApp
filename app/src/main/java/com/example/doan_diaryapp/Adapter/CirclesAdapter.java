package com.example.doan_diaryapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doan_diaryapp.Models.Circles;
import com.example.doan_diaryapp.R;

import java.util.List;

public class CirclesAdapter extends RecyclerView.Adapter<CirclesAdapter.CirclesViewHolder>{
    private Context mContext;
    private List<Circles> mCircles;


    public CirclesAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void setData(List<Circles> list){
        this.mCircles = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CirclesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_circles,parent,false);
        return new CirclesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CirclesViewHolder holder, int position) {
        Circles circles = mCircles.get(position);
        if(circles == null)
            return;

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext,RecyclerView.HORIZONTAL,false);
        holder.rcvCircle.setLayoutManager(linearLayoutManager);
        holder.rcvCircle.setNestedScrollingEnabled(false);

        CircleAdapter circleAdapter = new CircleAdapter();
        circleAdapter.setData(circles.getmCircle());

        holder.rcvCircle.setAdapter(circleAdapter);
    }

    @Override
    public int getItemCount() {
        if(mCircles != null)
            return mCircles.size();
        return 0;
    }

    public class CirclesViewHolder extends RecyclerView.ViewHolder{
        private RecyclerView rcvCircle;
        public CirclesViewHolder(@NonNull View itemView) {
            super(itemView);
            this.rcvCircle = itemView.findViewById(R.id.rcv_circle);
        }


    }
}
