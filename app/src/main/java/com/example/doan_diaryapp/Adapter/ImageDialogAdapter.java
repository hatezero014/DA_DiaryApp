package com.example.doan_diaryapp.Adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.util.DisplayMetrics;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doan_diaryapp.Decorator.GridSpacingItemDecoration;
import com.example.doan_diaryapp.R;

import java.util.ArrayList;
import java.util.List;

public class ImageDialogAdapter extends RecyclerView.Adapter<ImageDialogAdapter.ImageViewHolder> {
    private final List<Integer> imageList;
    private final List<String> descList;
    private SparseBooleanArray selectedItems;
    private Context context; // Thêm context vào đây
    @Nullable List<Integer> ImageSelectedList;

    public ImageDialogAdapter(List<Integer> imageList, List<String> descList, @Nullable List<Integer> imageSelectedList, Context context) {
        this.imageList = imageList;
        this.descList = descList;
        this.selectedItems = new SparseBooleanArray();
        if (imageSelectedList != null) {
            for (int index : imageSelectedList) {
                selectedItems.put(index, true);
            }
        }
        ImageSelectedList = imageSelectedList;
        this.context = context;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image_dialog, parent, false);

        DisplayMetrics displayMetrics = parent.getContext().getResources().getDisplayMetrics();
        int densityDpi = displayMetrics.densityDpi;

        int cardViewWidth = (displayMetrics.widthPixels - (72 * densityDpi / 160)) * 180 / 1000;

        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.width = cardViewWidth;
        view.setLayoutParams(layoutParams);

        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        if (position == getItemCount() - 1) {
            return;
        }
        Integer imageRes = imageList.get(position);
        String desc = descList.get(position);
        holder.editText.setText(desc);
        holder.imageView.setImageResource(imageRes);
        holder.imageTopIcon.setImageResource(selectedItems.get(position) ? R.drawable.check_box : R.drawable.check_box_outline_blank);
        holder.imageView.setOnClickListener(v -> {
            boolean isSelected = !selectedItems.get(position);
            selectedItems.put(position, isSelected);
            holder.imageTopIcon.setImageResource(selectedItems.get(position) ? R.drawable.check_box : R.drawable.check_box_outline_blank);
        });
    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }

    static class ImageViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView, imageTopIcon;
        EditText editText;

        ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            editText = itemView.findViewById(R.id.textDesc);
            imageTopIcon = itemView.findViewById(R.id.iconTopRight);
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