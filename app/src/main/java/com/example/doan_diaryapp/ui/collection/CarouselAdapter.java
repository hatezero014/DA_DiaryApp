package com.example.doan_diaryapp.ui.collection;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.doan_diaryapp.FullImageView;
import com.example.doan_diaryapp.FullImageView1;
import com.example.doan_diaryapp.Models.EntryPhoto;
import com.example.doan_diaryapp.Models.ImportantDay;
import com.example.doan_diaryapp.R;
import com.example.doan_diaryapp.RecordActivity;
import com.example.doan_diaryapp.Service.EntryPhotoService;
import com.example.doan_diaryapp.Service.EntryService;
import com.example.doan_diaryapp.YourImagesInApp;
import com.example.doan_diaryapp.databinding.CarouselLayoutBinding;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;
import java.util.Locale;

public class CarouselAdapter extends RecyclerView.Adapter<CarouselAdapter.ItemViewHolder> {

    private ArrayList<CarouselModel> list;
    private Context context;
    EntryPhotoService entryPhotoService;
    private ArrayList<CarouselModel> imageList;

    public CarouselAdapter(ArrayList<CarouselModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ItemViewHolder(CarouselLayoutBinding.inflate(LayoutInflater.from(context), parent, false));
    }


    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        CarouselModel model = list.get(position);
        holder.bind(model);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class ItemViewHolder extends RecyclerView.ViewHolder {
        private CarouselLayoutBinding binding;

        public ItemViewHolder(CarouselLayoutBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            // Set OnClickListener for the whole item view
            imageList = getListData();
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        Intent intent = new Intent(context, FullImageView1.class);
                        CarouselModel clickedItem = list.get(position);

                        ArrayList<String> imagePaths = new ArrayList<>();
                        for (CarouselModel model : imageList) {
                            imagePaths.add(model.getImagePath());
                        }
                        intent.putExtra("pos", position);
                        intent.putStringArrayListExtra("images", imagePaths);
                        context.startActivity(intent);
                    }
                }
            });
        }

        public void bind(CarouselModel model) {
            Glide.with(context)
                    .load(model.getImagePath())
                    .into(binding.carouselImageView);
        }
    }

    private void showDaily(String imagePath) {
        Intent intent = new Intent(context, RecordActivity.class);
        entryPhotoService = new EntryPhotoService(context);
        String date = entryPhotoService.getDate(imagePath);
        intent.putExtra("Date", date);
        context.startActivity(intent);
    }

    private ArrayList<CarouselModel> getListData() {
        EntryPhotoService entryPhotoService = new EntryPhotoService(context);
        ArrayList<CarouselModel> entryPhotos = entryPhotoService.getPhotoFromDatabase();
        return entryPhotos;
    }

}
