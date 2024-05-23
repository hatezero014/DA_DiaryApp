package com.example.doan_diaryapp.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doan_diaryapp.Decorator.CustomDialogText;
import com.example.doan_diaryapp.Models.Activity;
import com.example.doan_diaryapp.Models.Emotion;
import com.example.doan_diaryapp.Models.Partner;
import com.example.doan_diaryapp.Models.SelectedItem;
import com.example.doan_diaryapp.Models.Weather;
import com.example.doan_diaryapp.R;
import com.example.doan_diaryapp.Service.ActivityService;
import com.example.doan_diaryapp.Service.EmotionService;
import com.example.doan_diaryapp.Service.PartnerService;
import com.example.doan_diaryapp.Service.WeatherService;

import java.util.ArrayList;
import java.util.List;

public class ImageDialogAdapter extends RecyclerView.Adapter<ImageDialogAdapter.ImageViewHolder> {
    private final List<Integer> imageList;
    private List<String> descList;
    private final List<String> iconList;
    private final SparseBooleanArray isActiveItems;
    String language;
    String type;
    Context context;

    public ImageDialogAdapter(String type, String language, @Nullable List<Integer> imageList,
                              @Nullable List<String> descList, @Nullable List<Integer> imageIsActiveList,
                              @Nullable List<String> iconList, Context context) {
        this.imageList = imageList;
        if (descList != null) {
            this.descList = new ArrayList<>(descList);
        }
        this.isActiveItems = new SparseBooleanArray();
        if (imageIsActiveList != null) {
            for (int index : imageIsActiveList) {
                isActiveItems.put(index, true);
            }
        }
        this.type = type;
        this.language = language;
        this.context = context;
        this.iconList = iconList;
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
        Integer imageRes = imageList.get(position);
        String desc = descList.get(position);
        holder.editText.setText(desc);
        holder.imageView.setImageResource(imageRes);
        holder.imageTopIcon.setImageResource(isActiveItems.get(position) ? R.drawable.check_box : R.drawable.check_box_outline_blank);
        holder.editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int adapterPosition = holder.getAdapterPosition();
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    descList.set(adapterPosition, s.toString());
                }
            }
        });

        holder.editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomDialogText dialog = new CustomDialogText(context, holder.editText);
                dialog.setCancelable(false);
                dialog.show();
                dialog.setEditTextText(holder.editText.getText().toString());
            }
        });

        holder.imageView.setOnClickListener(v -> {
            boolean isSelected = !isActiveItems.get(position);
            isActiveItems.put(position, isSelected);
            holder.imageTopIcon.setImageResource(isActiveItems.get(position) ? R.drawable.check_box : R.drawable.check_box_outline_blank);
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

    public List<SelectedItem> getSelectedItems() {
        List<SelectedItem> result = new ArrayList<>();
        for (int i = 0; i < imageList.size(); i++) {
            if (isActiveItems.get(i)) {
                String text = descList.get(i);
                result.add(new SelectedItem(i, text));
            }
        }
        return result;
    }
}