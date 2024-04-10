package com.example.doan_diaryapp.ui.collection;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.doan_diaryapp.databinding.CarouselLayoutBinding;

import java.util.ArrayList;

public class CarouselAdapter extends RecyclerView.Adapter<CarouselAdapter.ItemViewHolder> {

    private ArrayList<CarouselModel> list;

    private Context context;

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
        }

        public void bind(CarouselModel model) {
            binding.carouselImageView.setImageResource(model.getImageId());
        }
    }
}

