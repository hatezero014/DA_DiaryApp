package com.example.doan_diaryapp.Adapter;

import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doan_diaryapp.R;

import java.util.List;

public class ImageRecordAdapter extends RecyclerView.Adapter<ImageRecordAdapter.ImageViewHolder> {
    private List<Integer> imageList;
    private SparseBooleanArray selectedItems;

    public ImageRecordAdapter(List<Integer> imageList) {
        this.imageList = imageList;
        this.selectedItems = new SparseBooleanArray();
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image_record, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        Integer imageRes = imageList.get(position);
        holder.imageView.setImageResource(imageRes);
        holder.imageView.setAlpha(selectedItems.get(position) ? 1.0f : 0.15f);
        holder.imageView.setOnClickListener(v -> {
            boolean isSelected = !selectedItems.get(position);
            selectedItems.put(position, isSelected);
            holder.imageView.setAlpha(isSelected ? 1.0f : 0.15f);
        });
    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }

    static class ImageViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }
}