package com.example.doan_diaryapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.icu.text.CaseMap;
import android.net.Uri;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.example.doan_diaryapp.Models.Entry;
import com.example.doan_diaryapp.Models.EntryPhoto;
import com.example.doan_diaryapp.Models.ImportantDay;
import com.example.doan_diaryapp.Models.Language;
import com.example.doan_diaryapp.R;
import com.example.doan_diaryapp.Service.EntryPhotoService;
import com.example.doan_diaryapp.Service.ImportantDayService;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;



public class EntryAdapter extends ArrayAdapter<Entry> {

    private OnFavoriteClickListener favoriteClickListener;

    ImportantDayService importantDayService;
    List<String> entryPhotoList;

    EntryPhotoService entryPhotoService;


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
        TextView textViewId = convertView.findViewById(R.id.textViewID);
        TextView textView = convertView.findViewById(R.id.textView);
        ImageView actionFavorite = convertView.findViewById(R.id.action_favorite);
        ImageView actionShare = convertView.findViewById(R.id.action_share);

        assert entry != null;
        textViewId.setText(entry.getDate());


        if (entry.getDate().length() == 19) {

            String Title = entry.getTitle();

            if (Title.length()>20) {
                Title = Title.substring(0, Math.min(Title.length(), 20))+"...";
            } else
            {
                Title = Title.substring(0, Math.min(Title.length(), 20));
            }

            if (Title.length() == 0) {
                Title = entry.getNote();
                if (Title.length()>20) {
                    Title = Title.substring(0, Math.min(Title.length(), 20))+"...";
                } else
                {
                    Title = Title.substring(0, Math.min(Title.length(), 20));
                }
                if (Title.length() == 0) {
                    Title = "(Chưa có chủ đề)";
                }
            }

            String Note = entry.getNote();
            if (Note.length()>25) {
                Note = Note.substring(0, Math.min(Note.length(), 25))+"...";
            } else
            {
                Note = Note.substring(0, Math.min(Note.length(), 25));
            }
            if (Note.length() == 0) {
                Note = "(Chưa có nội dung)";
            }



            String time=entry.getDate();
            time = time.substring(0, Math.min(time.length(), 8));

            textViewDate.setText(Title);
            textViewNote.setText(time);
            textView.setText(Note);

            int color = ContextCompat.getColor(getContext(), R.color.md_theme_onSurfaceVariant);
            textViewNote.setTextColor(color);
            textViewDate.setVisibility(View.VISIBLE);
            actionFavorite.setVisibility(View.VISIBLE);
            actionShare.setVisibility(View.VISIBLE);
            textView.setVisibility(View.VISIBLE);
            textViewNote.setTypeface(null, Typeface.NORMAL);
            textViewNote.setGravity(Gravity.END);
        } else {

            textViewDate.setText("");
            textViewNote.setText(getDayOfWeek(entry.getNote()));

            textViewNote.setTextColor(Color.parseColor("#005138"));
            textViewDate.setVisibility(View.GONE);
            actionFavorite.setVisibility(View.GONE);
            textView.setVisibility(View.GONE);
            actionShare.setVisibility(View.GONE);
            textViewNote.setTypeface(textViewNote.getTypeface(), Typeface.BOLD);
            textViewNote.setGravity(Gravity.CENTER);
        }



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

                if (favoriteClickListener != null) {
                    favoriteClickListener.onFavoriteClick(entry);
                }
            }
        });


        actionShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                entryPhotoService = new EntryPhotoService(getContext());
                entryPhotoList = entryPhotoService.getAllPhoto(entry.getId());
                String titleShare = "[" + entry.getTitle() + "]" + "\n" + entry.getNote();
                shareImage(entryPhotoList,titleShare);
            }
        });


        return convertView;
    }


    private void shareImage(List<String> entryPhotoList, String titleShare) {
        if (entryPhotoList.isEmpty()) {

            shareText(titleShare);
        } else {

            ArrayList<Uri> uriList = new ArrayList<>();


            Bitmap[] bitmaps = new Bitmap[entryPhotoList.size()];
            for (int i = 0; i < entryPhotoList.size(); i++) {
                Bitmap bitmap = BitmapFactory.decodeFile(entryPhotoList.get(i));
                if (bitmap != null) {
                    bitmaps[i] = bitmap;
                }
            }


            Bitmap combinedBitmap = combineImages(bitmaps);


            File tempFile = saveBitmapToFile(getContext(), combinedBitmap);


            if (tempFile != null) {
                Uri uri = FileProvider.getUriForFile(getContext(), "com.example.doan_diaryapp", tempFile);
                uriList.add(uri);
                shareImageAndText(uri, titleShare);
            }
        }
    }

    private void shareText(String titleShare) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, titleShare);
        intent.setType("text/plain");
        getContext().startActivity(Intent.createChooser(intent, "Share Via"));
    }




    private Bitmap combineImages(Bitmap[] bitmaps) {
        int maxWidth = 0;
        int totalHeight = 0;


        for (Bitmap bitmap : bitmaps) {
            if (bitmap != null) {
                maxWidth = Math.max(maxWidth, bitmap.getWidth());
                totalHeight += bitmap.getHeight();
            }
        }


        Bitmap result = Bitmap.createBitmap(maxWidth, totalHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(result);


        int y = 0;
        for (Bitmap bitmap : bitmaps) {
            if (bitmap != null) {
                canvas.drawBitmap(bitmap, new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight()), new Rect(0, y, bitmap.getWidth(), y + bitmap.getHeight()), null);
                y += bitmap.getHeight();
            }
        }

        return result;
    }


    private File saveBitmapToFile(Context context, Bitmap bitmap) {
        File file = null;
        try {
            File cacheDir = new File(context.getCacheDir(), "images");
            if (!cacheDir.exists()) {
                cacheDir.mkdirs();
            }
            file = new File(cacheDir, "combined_image.jpg");
            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fos);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }


    private void shareImageAndText(Uri uri, String titleShare) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        intent.putExtra(Intent.EXTRA_TEXT, titleShare);
        intent.putExtra(Intent.EXTRA_SUBJECT, "Image Sub");
        intent.setType("image/*");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        getContext().startActivity(Intent.createChooser(intent, "Share Via"));
    }



    private static Language currentLanguage;

    public static void setCurrentLanguage(Language language) {
        currentLanguage = language;
    }

    private String getDayOfWeek(String date) {

        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
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
        return dayFormat.format(dateTime) + ", " + date;
    }


    private List<Entry> entries;

    public void updateEntries(List<Entry> newEntries) {
        this.entries.clear();
        this.entries.addAll(newEntries);
        notifyDataSetChanged();
    }

    public void updateEntries1(List<Entry> newEntries) {
        clear(); // Clear existing entries
        addAll(newEntries); // Add new entries
        notifyDataSetChanged(); // Notify adapter of data change
    }

    public interface OnFavoriteClickListener {
        void onFavoriteClick(Entry entry);
    }


}
