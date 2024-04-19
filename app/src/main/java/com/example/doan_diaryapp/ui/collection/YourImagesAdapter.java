package com.example.doan_diaryapp.ui.collection;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.doan_diaryapp.databinding.YourImagesBinding;

import java.util.ArrayList;

public class YourImagesAdapter extends RecyclerView.Adapter<YourImagesAdapter.ItemViewHolder> {

    private ArrayList<CarouselModel> list;
    private Context context;

    public YourImagesAdapter(ArrayList<CarouselModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ItemViewHolder(YourImagesBinding.inflate(LayoutInflater.from(context), parent, false));
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

        private YourImagesBinding binding;

        public ItemViewHolder(YourImagesBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(CarouselModel model) {
            Glide.with(context)
                    .load(model.getImagePath())
                    .into(binding.carouselImageView1);
        }
    }
}
