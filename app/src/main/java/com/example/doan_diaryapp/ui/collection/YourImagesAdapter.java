package com.example.doan_diaryapp.ui.collection;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.doan_diaryapp.Models.EntryPhoto;
import com.example.doan_diaryapp.R;
import com.example.doan_diaryapp.databinding.CarouselLayoutBinding;
import com.example.doan_diaryapp.databinding.YourImagesBinding;

import java.util.ArrayList;
import java.util.List;

public class YourImagesAdapter extends RecyclerView.Adapter<YourImagesAdapter.ItemViewHolder> {

    public YourImagesAdapter(ArrayList<CarouselModel> carouselModelList, Context context) {
        this.carouselModelList = carouselModelList;
        this.context = context;
    }

    private ArrayList<CarouselModel> carouselModelList;
    private Context context;



    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.your_images, parent, false);
//        return new ItemViewHolder(view);
        return new ItemViewHolder(YourImagesBinding.inflate(LayoutInflater.from(context), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
//        CarouselModel entryPhoto = carouselModelList.get(position);
//        if(entryPhoto == null) return;
//        holder.imageView.setImageResource(Integer.parseInt(entryPhoto.getImagePath()));
        CarouselModel model = carouselModelList.get(position);
        holder.bind(model);
    }

    @Override
    public int getItemCount() {

//        if(carouselModelList==null) return 0;
        return carouselModelList.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
//        ImageView imageView;
//
//
//        public ItemViewHolder(@NonNull View itemView) {
//            super(itemView);
//            imageView = itemView.findViewById(R.id.imageView5);
//        }
        private YourImagesBinding binding;

        public ItemViewHolder(YourImagesBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(CarouselModel model) {
            Glide.with(context)
                    .load(model.getImagePath())
                    .into(binding.imageView5);
        }
    }
}
