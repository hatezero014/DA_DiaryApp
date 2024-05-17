package com.example.doan_diaryapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.doan_diaryapp.Models.Entry;
import com.example.doan_diaryapp.Models.ImportantDay;
import com.example.doan_diaryapp.Models.Language;
import com.example.doan_diaryapp.R;
import com.example.doan_diaryapp.Service.ImportantDayService;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class EntryAdapter extends ArrayAdapter<Entry> {

    private OnFavoriteClickListener favoriteClickListener;
    ImportantDayService importantDayService;


    public EntryAdapter(Context context, List<Entry> entries) {
        super(context, 0, entries);
    }
    public void setOnFavoriteClickListener(OnFavoriteClickListener listener) {
        this.favoriteClickListener = listener;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Entry entry = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_entry, parent, false);
        }

        TextView textViewDate = convertView.findViewById(R.id.textViewDate);
        TextView textViewNote = convertView.findViewById(R.id.textViewNote);
        ImageView actionFavorite = convertView.findViewById(R.id.action_favorite);


        String fullDate = getDayOfWeek(entry.getDate())+", "+entry.getDate();
        textViewDate.setText(fullDate);
        textViewNote.setText(entry.getNote());
        //actionFavorite.setImageResource(R.drawable.state_outlined_main_collection);

        importantDayService=new ImportantDayService(getContext());
        boolean checkAction = importantDayService.checkImportant(entry.getDate());
        if (checkAction) {
            actionFavorite.setImageResource(R.drawable.state_filled_record_star);
        } else {
            actionFavorite.setImageResource(R.drawable.state_outlined_record_star);
        }

        actionFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImportantDay importantDay = importantDayService.FindByDate(new ImportantDay(),entry.getDate());
                boolean isCheckFavorite = importantDayService.checkImportant(entry.getDate());
                if (isCheckFavorite) {
                    actionFavorite.setImageResource(R.drawable.state_outlined_record_star);
                    if (importantDay != null) {
                        importantDayService.DeleteById(ImportantDay.class, importantDay.getId());
                    }
                }
                else {
                    actionFavorite.setImageResource(R.drawable.state_filled_record_star);
                    if (importantDay == null) {
                        importantDayService.Add(new ImportantDay(entry.getDate()));
                    }
                }
            }
        });

        return convertView;
    }

    private static Language currentLanguage;

    public static void setCurrentLanguage(Language language) {
        currentLanguage = language;
    }

    private String getDayOfWeek(String date) {

        SimpleDateFormat format = new SimpleDateFormat("hh:mm:ss dd-MM-yyyy", Locale.getDefault());
        Date dateTime = null;
        try {
            dateTime = format.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Locale locale;
        if (currentLanguage != null && currentLanguage.getCode() != null) {
            locale = new Locale(currentLanguage.getCode());
        } else {
            locale = Locale.getDefault();
        }

        SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE", locale);
        return dayFormat.format(dateTime);
    }


    private List<Entry> entries;

    public void updateEntries(List<Entry> newEntries) {
        this.entries.clear();
        this.entries.addAll(newEntries);
        notifyDataSetChanged();
    }

    public interface OnFavoriteClickListener {
        void onFavoriteClick(Entry entry);
    }
}
