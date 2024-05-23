package com.example.doan_diaryapp.Adapter;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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
import com.example.doan_diaryapp.Models.Emotion;
import com.example.doan_diaryapp.Models.Partner;
import com.example.doan_diaryapp.Models.SelectedItem;
import com.example.doan_diaryapp.Models.Weather;
import com.example.doan_diaryapp.R;
import com.example.doan_diaryapp.RecordActivity;
import com.example.doan_diaryapp.Service.ActivityService;
import com.example.doan_diaryapp.Service.EmotionService;
import com.example.doan_diaryapp.Service.PartnerService;
import com.example.doan_diaryapp.Service.WeatherService;

import java.util.List;
import java.util.ArrayList;

public class ImageRecordAdapter extends RecyclerView.Adapter<ImageRecordAdapter.ImageViewHolder> {
    private final List<Integer> imageList;
    private final List<String> descList;

    private List<Integer> imageListGetAll;
    private List<String> descListGetAll;
    private List<String> iconListGetAll;
    private List<Integer> isActiveItems;
    private SparseBooleanArray selectedItems;
    private Context context;
    String language;
    String type;
    RecordActivity activity;

    public ImageRecordAdapter(String type, String language, List<Integer> imageList, List<String> descList,
                              @Nullable List<Integer> imageSelectedList, @Nullable List<Integer> imageListGetAll,
                              @Nullable List<String> descListGetAll, @Nullable List<Integer> isActiveItems,
                              @Nullable List<String> iconListGetAll, Context context, RecordActivity activity) {
        this.type = type;
        this.imageList = imageList;
        this.descList = descList;
        this.selectedItems = new SparseBooleanArray();
        if (imageSelectedList != null) {
            for (int index : imageSelectedList) {
                selectedItems.put(index, true);
            }
        }
        this.imageListGetAll = imageListGetAll;
        this.descListGetAll = descListGetAll;
        this.isActiveItems = isActiveItems;
        this.context = context;
        this.language = language;
        this.iconListGetAll = iconListGetAll;
        this.activity = activity;
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
            holder.imageView.setBackgroundResource(R.drawable.circle_background);
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

        ImageDialogAdapter adapter = new ImageDialogAdapter(type, language, imageListGetAll, descListGetAll,
                                                                isActiveItems, iconListGetAll, context);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(4, 60, false));

        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                List<SelectedItem> list = adapter.getSelectedItems();
                List<Integer> listNotActive = new ArrayList<>();
                for (int i = 0; i < imageListGetAll.size(); i++) {
                    listNotActive.add(i);
                }

                for (int i = 0; i < list.size(); i++) {
                    SelectedItem item = list.get(i);
                    int position = item.getId();
                    listNotActive.remove(Integer.valueOf(position));
                    if (type.equals("Emotion")) {
                        EmotionService emotionService = new EmotionService(context);
                        Emotion emotion = emotionService.FindByIcon(Emotion.class, iconListGetAll.get(position));
                        if (language.equals("vi")) {
                            emotionService.UpdateById(new Emotion(emotion.getIcon(), emotion.getDescEn(),
                                    item.getText(), 1), emotion.getId());
                        }
                        else {
                            emotionService.UpdateById(new Emotion(emotion.getIcon(), item.getText(),
                                    emotion.getDescVi(), 1), emotion.getId());
                        }
                    }

                    if (type.equals("Weather")) {
                        WeatherService weatherService = new WeatherService(context);
                        Weather weather = weatherService.FindByIcon(Weather.class, iconListGetAll.get(position));
                        if (language.equals("vi")) {
                            weatherService.UpdateById(new Weather(weather.getIcon(), weather.getDescEn(),
                                    item.getText(), 1), weather.getId());
                        }
                        else {
                            weatherService.UpdateById(new Weather(weather.getIcon(), item.getText(),
                                    weather.getDescVi(), 1), weather.getId());
                        }
                    }

                    if (type.equals("Activity")) {
                        ActivityService activityService = new ActivityService(context);
                        com.example.doan_diaryapp.Models.Activity activity = activityService.FindByIcon(com.example.doan_diaryapp.Models.Activity.class, iconListGetAll.get(position));
                        if (language.equals("vi")) {
                            activityService.UpdateById(new com.example.doan_diaryapp.Models.Activity(activity.getIcon(), activity.getDescEn(),
                                    item.getText(), 1), activity.getId());
                        }
                        else {
                            activityService.UpdateById(new com.example.doan_diaryapp.Models.Activity(activity.getIcon(), item.getText(),
                                    activity.getDescVi(), 1), activity.getId());
                        }
                    }

                    if (type.equals("Partner")) {
                        PartnerService partnerService = new PartnerService(context);
                        Partner partner = partnerService.FindByIcon(Partner.class, iconListGetAll.get(position));
                        if (language.equals("vi")) {
                            partnerService.UpdateById(new Partner(partner.getIcon(), partner.getDescEn(),
                                    item.getText(), 1), partner.getId());
                        }
                        else {
                            partnerService.UpdateById(new Partner(partner.getIcon(), item.getText(),
                                    partner.getDescVi(), 1), partner.getId());
                        }
                    }
                }

                if (type.equals("Emotion")) {
                    for (int id : listNotActive) {
                        EmotionService emotionService = new EmotionService(context);
                        Emotion emotion = emotionService.FindByIcon(Emotion.class, iconListGetAll.get(id));
                        if (language.equals("vi")) {
                            emotionService.UpdateById(new Emotion(emotion.getIcon(), emotion.getDescEn(),
                                    emotion.getDescVi(), 0), emotion.getId());
                        }
                        else {
                            emotionService.UpdateById(new Emotion(emotion.getIcon(), emotion.getDescEn(),
                                    emotion.getDescVi(), 0), emotion.getId());
                        }
                    }
                    activity.updateRecyclerView1();
                }
                if (type.equals("Weather")) {
                    for (int id : listNotActive) {
                        WeatherService weatherService = new WeatherService(context);
                        Weather weather = weatherService.FindByIcon(Weather.class, iconListGetAll.get(id));
                        if (language.equals("vi")) {
                            weatherService.UpdateById(new Weather(weather.getIcon(), weather.getDescEn(),
                                    weather.getDescVi(), 0), weather.getId());
                        }
                        else {
                            weatherService.UpdateById(new Weather(weather.getIcon(), weather.getDescEn(),
                                    weather.getDescVi(), 0), weather.getId());
                        }
                    }
                    activity.updateRecyclerView4();
                }
                if (type.equals("Activity")) {
                    for (int id : listNotActive) {
                        ActivityService activityService = new ActivityService(context);
                        com.example.doan_diaryapp.Models.Activity activity = activityService.FindByIcon(com.example.doan_diaryapp.Models.Activity.class, iconListGetAll.get(id));
                        if (language.equals("vi")) {
                            activityService.UpdateById(new com.example.doan_diaryapp.Models.Activity(activity.getIcon(), activity.getDescEn(),
                                    activity.getDescVi(), 0), activity.getId());
                        }
                        else {
                            activityService.UpdateById(new com.example.doan_diaryapp.Models.Activity(activity.getIcon(), activity.getDescEn(),
                                    activity.getDescVi(), 0), activity.getId());
                        }
                    }
                    activity.updateRecyclerView2();
                }
                if (type.equals("Partner")) {
                    for (int id : listNotActive) {
                        PartnerService partnerService = new PartnerService(context);
                        Partner partner = partnerService.FindByIcon(Partner.class, iconListGetAll.get(id));
                        if (language.equals("vi")) {
                            partnerService.UpdateById(new Partner(partner.getIcon(), partner.getDescEn(),
                                    partner.getDescVi(), 0), partner.getId());
                        }
                        else {
                            partnerService.UpdateById(new Partner(partner.getIcon(), partner.getDescEn(),
                                    partner.getDescVi(), 0), partner.getId());
                        }
                    }
                    activity.updateRecyclerView3();
                }
                dialog.dismiss();
            }
        });

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