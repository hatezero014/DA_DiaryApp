package com.example.doan_diaryapp.Adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doan_diaryapp.Models.EmojiInfo;
import com.example.doan_diaryapp.R;

import java.util.List;

public class EmojiAdapter extends RecyclerView.Adapter<EmojiAdapter.EmojiViewHolder>{
    public EmojiAdapter(Context mContext) {
        this.mContext = mContext;
    }

    private Context mContext;
    private List<EmojiInfo> emojiInfoList;

    public void setData(List<EmojiInfo> list){
        this.emojiInfoList = list;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public EmojiViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_emotion_card,parent,false);
        return new EmojiViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EmojiViewHolder holder, int position) {
        EmojiInfo emojiInfo = emojiInfoList.get(position);
        if(emojiInfo == null){
            return;
        }
        int imageResourceId = mContext.getResources().getIdentifier(emojiInfo.getIcon(), "drawable", mContext.getPackageName());
        Drawable drawable = mContext.getDrawable(imageResourceId);
        holder.imgEmoji.setImageDrawable(drawable);
        holder.tvCount.setText("x"+emojiInfo.getIconCount());
    }

    @Override
    public int getItemCount() {
        if(emojiInfoList != null) {
            return emojiInfoList.size();
        }
        return 0;
    }

    public class EmojiViewHolder extends RecyclerView.ViewHolder{
        private ImageView imgEmoji;
        private TextView tvCount;

        public EmojiViewHolder(@NonNull View itemView) {
            super(itemView);

            imgEmoji = itemView.findViewById(R.id.imgEmoji);
            tvCount = itemView.findViewById(R.id.tvCount);
        }
    }
}
