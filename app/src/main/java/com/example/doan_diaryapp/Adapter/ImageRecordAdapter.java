package com.example.doan_diaryapp.Adapter;

import android.util.DisplayMetrics;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doan_diaryapp.R;

import java.util.List;
import java.util.ArrayList;

public class ImageRecordAdapter extends RecyclerView.Adapter<ImageRecordAdapter.ImageViewHolder> {
    private final  List<Integer> imageList;
    private SparseBooleanArray selectedItems;

    public ImageRecordAdapter(List<Integer> imageList, @Nullable List<Integer> imageSelectedList) {
        this.imageList = imageList;
        this.selectedItems = new SparseBooleanArray();
        if (imageSelectedList != null) {
            for (int index : imageSelectedList) {
                selectedItems.put(index, true);
            }
        }
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image_record, parent, false);

        DisplayMetrics displayMetrics = parent.getContext().getResources().getDisplayMetrics();
        int screenWidth = displayMetrics.widthPixels;

        int densityDpi = displayMetrics.densityDpi;

        int cardViewWidth = (screenWidth - (int)(72 * (densityDpi / 160.0))) / 5;
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.width = cardViewWidth;
        view.setLayoutParams(layoutParams);

        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        Integer imageRes = imageList.get(position);
        holder.imageView.setImageResource(imageRes);
        holder.imageView.setAlpha(selectedItems.get(position) ? 1.0f : 0.35f);
        holder.imageView.setOnClickListener(v -> {
            boolean isSelected = !selectedItems.get(position);
            selectedItems.put(position, isSelected);
            holder.imageView.setAlpha(isSelected ? 1.0f : 0.35f);
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

    public List<Integer> getSelectedItems() {
        List<Integer> result = new ArrayList<>();
        for (int i = 0; i < imageList.size(); i++) {
            if (selectedItems.get(i)) {
                result.add(i);
            }
        }
        return result;
    }
}