package com.example.doan_diaryapp.Adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doan_diaryapp.Decorator.GridSpacingItemDecoration;
import com.example.doan_diaryapp.R;
import com.example.doan_diaryapp.RecordActivity;

import java.util.List;
import java.util.ArrayList;

public class ImageRecordAdapter extends RecyclerView.Adapter<ImageRecordAdapter.ImageViewHolder> {
    private final List<Integer> imageList;
    private final List<String> descList;
    private SparseBooleanArray selectedItems;
    private Context context; // Thêm context vào đây
    @Nullable List<Integer> ImageSelectedList;

    public ImageRecordAdapter(List<Integer> imageList, List<String> descList, @Nullable List<Integer> imageSelectedList, Context context) {
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image_record, parent, false);

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
        Integer imageRes = imageList.get(position);
        String desc = descList.get(position);
        if (position == getItemCount() - 1) {
            holder.imageView.setImageResource(imageRes);
            holder.imageView.setOnClickListener(v -> {
                showCustomDialog();
            });

            return;
        }
        holder.textView.setText(desc);
        holder.imageView.setImageResource(imageRes);
        holder.imageView.setAlpha(selectedItems.get(position) ? 1.0f : 0.35f);
        holder.textView.setAlpha(selectedItems.get(position) ? 1.0f : 0.35f);
        holder.imageView.setOnClickListener(v -> {
            boolean isSelected = !selectedItems.get(position);
            selectedItems.put(position, isSelected);
            holder.imageView.setAlpha(isSelected ? 1.0f : 0.35f);
            holder.textView.setAlpha(isSelected ? 1.0f : 0.35f);
        });
    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }

    static class ImageViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView;

        ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            textView = itemView.findViewById(R.id.textDesc);
        }
    }

    private void showCustomDialog() {
        Dialog dialog = new Dialog(context);

        LayoutInflater inflater = LayoutInflater.from(context);

        View dialogView = inflater.inflate(R.layout.dialog_icon, null);

        dialog.setContentView(dialogView);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = (int) (displayMetrics.widthPixels * 0.9);
        int height = (int) (displayMetrics.heightPixels * 0.75);

        if (dialog.getWindow() != null) {
            dialog.getWindow().setLayout(width, height);
            dialog.getWindow().setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.custom_background_diary));
            dialog.setCancelable(true);
        }

        Button btnOK = dialog.findViewById(R.id.btnRight);
        Button btnCancel = dialog.findViewById(R.id.btnLeft);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        RecyclerView recyclerView = dialogView.findViewById(R.id.recyclerView1);
        recyclerView.setLayoutManager(new GridLayoutManager(context, 4));

        ImageDialogAdapter adapter = new ImageDialogAdapter(imageList, descList, ImageSelectedList, context);
        recyclerView.setAdapter(adapter);

        // Thêm decoration cho RecyclerView
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(4, 60, false));

        // Hiển thị dialog
        dialog.show();
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