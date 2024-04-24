package com.example.doan_diaryapp.ui.collection;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.doan_diaryapp.R;
import com.example.doan_diaryapp.databinding.YourImagesBinding;

import java.util.ArrayList;
import java.util.List;

public class YourImagesAdapter extends RecyclerView.Adapter<YourImagesAdapter.ItemViewHolder> {

    private List<ImageModel> imageModels;
    private Context context;

    public YourImagesAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<ImageModel> imageModels)
    {
        this.imageModels = imageModels;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.your_images, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        ImageModel carouselModel = imageModels.get(position);
        if(carouselModel == null) return;
        holder.imageView.setImageResource(carouselModel.getImage());
    }

    @Override
    public int getItemCount() {

        if(imageModels==null) return 0;
        return imageModels.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;


        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView5);
        }
    }
}
