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

    public void setOnImageClickListener(OnImageClickListener onImageClickListener) {
        this.onImageClickListener = onImageClickListener;
    }

    private OnImageClickListener onImageClickListener;



    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new ItemViewHolder(YourImagesBinding.inflate(LayoutInflater.from(context), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        CarouselModel model = carouselModelList.get(position);
        holder.bind(model);
    }

    @Override
    public int getItemCount() {

        return carouselModelList.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {

        private YourImagesBinding binding;

        public ItemViewHolder(YourImagesBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            binding.imageView5.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(onImageClickListener!=null){
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            // Gọi phương thức của giao diện xử lý sự kiện click
                            onImageClickListener.onImageClick(position);
                        }
                    }
                }
            });
        }

        public void bind(CarouselModel model) {
            Glide.with(context)
                    .load(model.getImagePath())
                    .into(binding.imageView5);
        }
    }

    public interface OnImageClickListener {
        void onImageClick(int position);
    }
}
