package com.example.doan_diaryapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.doan_diaryapp.Models.Entry;
import com.example.doan_diaryapp.R;

import java.util.List;

public class EntryAdapter extends ArrayAdapter<Entry> {

    public EntryAdapter(Context context, List<Entry> entries) {
        super(context, 0, entries);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Entry entry = getItem(position);


        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_entry, parent, false);
        }


        TextView textViewDate = convertView.findViewById(R.id.textViewDate);
        TextView textViewNote = convertView.findViewById(R.id.textViewNote);


        textViewDate.setText(entry.getDate());
        textViewNote.setText(entry.getNote());

        return convertView;
    }

    private List<Entry> entries;

    public void updateEntries(List<Entry> newEntries) {
        this.entries.clear();
        this.entries.addAll(newEntries);
        notifyDataSetChanged();
    }
}
