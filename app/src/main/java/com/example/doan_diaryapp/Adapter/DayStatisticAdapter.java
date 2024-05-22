package com.example.doan_diaryapp.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doan_diaryapp.Models.Entry;
import com.example.doan_diaryapp.Models.Statistic;
import com.example.doan_diaryapp.R;
import com.example.doan_diaryapp.Service.EntryService;
import com.example.doan_diaryapp.ShowEmojiActivity;
import com.example.doan_diaryapp.ui.image.Image;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DayStatisticAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private List<Statistic>statisticList;
    private final static int type_barchart = 1;
    private final static int type_emotion = 2;
    private final Context context;
    private final String date;

    public DayStatisticAdapter(Context context, String date) {
        this.context = context;
        this.date = date;
    }

    public void setData(List<Statistic>list){
        this.statisticList = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(type_barchart == viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_barchart, parent, false);
            return new BarChartViewHolder(view);
        }
        else if (type_emotion == viewType){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_emotion,parent,false);
            return new EmotionViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Statistic statistic = statisticList.get(position);
        if(statistic == null)
            return;

        if(type_barchart == holder.getItemViewType()){
            BarChartViewHolder barChartViewHolder = (BarChartViewHolder) holder;
            barChartViewHolder.setData(date);
        }
        else if(type_emotion == holder.getItemViewType()){
            EmotionViewHolder emotionViewHolder = (EmotionViewHolder) holder;
            emotionViewHolder.setEmotion(date, statistic.getEmotionType());
        }
    }

    @Override
    public int getItemCount() {
        if(statisticList != null)
            return statisticList.size();
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        Statistic statistic = statisticList.get(position);
        if(statistic.getStatisticType() == 1)
            return type_barchart;
        else return type_emotion;
    }

    public class BarChartViewHolder extends RecyclerView.ViewHolder{
        private TextView tv_average;
        private BarChart barChart;
        public BarChartViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_average = itemView.findViewById(R.id.tv_average_rating);
            barChart = itemView.findViewById(R.id.barChart);
        }

        private void setBarChartFormat() {
            barChart.getAxisRight().setDrawLabels(false);
            barChart.setDescription(null);
            barChart.setScaleYEnabled(false); // tắt zoom trên cột Y
            barChart.setDoubleTapToZoomEnabled(false); // tắt chạm 2 lần để zoom
            barChart.setBackgroundColor(Color.parseColor("#00000000"));
            barChart.setHighlightPerTapEnabled(false); // tắt highlight điểm
            barChart.setHighlightPerDragEnabled(false); // same
            barChart.setExtraBottomOffset(6); // chỉnh margin cạnh dưới
            barChart.setExtraRightOffset(6);
            barChart.getAxisRight().setDrawLabels(false);
            barChart.getAxisRight().setAxisLineWidth(2);
            barChart.getAxisRight().setDrawGridLines(false);
            barChart.getAxisRight().setAxisLineColor(ContextCompat.getColor(context, R.color.statistics_grid));

            XAxis xAxis = barChart.getXAxis();
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            xAxis.setTextSize(14);
            xAxis.setLabelCount(10);
            xAxis.setGranularity(1f);
            xAxis.setAxisLineWidth(2);
            xAxis.setGridLineWidth(2);
            xAxis.setYOffset(6);
            xAxis.setTextColor(ContextCompat.getColor(context, R.color.md_theme_onSurfaceVariant));
            xAxis.setAxisLineColor(ContextCompat.getColor(context, R.color.statistics_grid));
            xAxis.setGridColor(ContextCompat.getColor(context, R.color.statistics_grid));

            YAxis yAxis = barChart.getAxisLeft();
            yAxis.setAxisMaximum(10f);
            yAxis.setAxisMinimum(0f);
            yAxis.setLabelCount(10);
            yAxis.setTextSize(14);
            yAxis.setGranularity(1f);
            yAxis.setAxisLineWidth(2);
            yAxis.setGridLineWidth(2);
            yAxis.setAxisLineColor(ContextCompat.getColor(context, R.color.statistics_grid));
            yAxis.setTextColor(ContextCompat.getColor(context, R.color.md_theme_onSurfaceVariant));
            yAxis.setGridColor(ContextCompat.getColor(context, R.color.statistics_grid));

        }

        public void setData(String date) {
            setBarChartFormat();

            EntryService entryService = new EntryService(context);
            String[]part = date.split("/");
            int day = Integer.parseInt(part[0]);
            int month = Integer.parseInt(part[1]);
            int year = Integer.parseInt(part[2]);
            List<Entry>entryList = entryService.getOverallScoreByDayMonthYear(day,month,year);

            List<BarEntry> barEntryList = new ArrayList<>();
            int index = 0;

            float tb = 0;
            for(Entry entry:entryList){
                int score = entry.getOverallScore();
                barEntryList.add(new BarEntry(index,score));
                tb+=score;
                index++;
            }
            tb/= entryList.size();

            tv_average.setText(itemView.getResources().getString(R.string.average_rating)+": "+String.format("%.2f",tb));

            BarDataSet barDataSet = new BarDataSet(barEntryList,date);
            barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
            barDataSet.setValueTextColor(ContextCompat.getColor(context, R.color.md_theme_onSurfaceVariant));

            BarData barData = new BarData(barDataSet);
            barChart.setData(barData);
            barChart.notifyDataSetChanged();
            barChart.invalidate();
        }
    }

    public class EmotionViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_emotion_type;
        private ImageView img1;
        private TextView tv1;
        private ImageView img2;
        private TextView tv2;
        private ImageView img3;
        private TextView tv3;
        private ImageView img4;
        private TextView tv4;

        private Button btn_viewall;

        public EmotionViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_emotion_type = itemView.findViewById(R.id.tv_emotion_type);

            img1 = itemView.findViewById(R.id.imageView1);
            tv1 = itemView.findViewById(R.id.tv_img1);
            img2 = itemView.findViewById(R.id.imageView2);
            tv2 = itemView.findViewById(R.id.tv_img2);
            img3 = itemView.findViewById(R.id.imageView3);
            tv3 = itemView.findViewById(R.id.tv_img3);
            img4 = itemView.findViewById(R.id.imageView4);
            tv4 = itemView.findViewById(R.id.tv_img4);
            btn_viewall = itemView.findViewById(R.id.btn_display_all);
        }

        @SuppressLint("SetTextI18n")
        public void setEmotion(String date, String emotionType) {
            String[]part = date.split("/");
            int day = Integer.parseInt(part[0]);
            int month = Integer.parseInt(part[1]);
            int year = Integer.parseInt(part[2]);

            Map<String, Integer> emotionCount = new HashMap<>();

            if (emotionType.equals("Mood")) {
                emotionCount.clear();
                tv_emotion_type.setText(context.getString(R.string.mood));
                emotionCount = new Image().getMoodByDate(year, month, day, context);

            } else if (emotionType.equals("Activity")) {
                emotionCount.clear();
                tv_emotion_type.setText(context.getString(R.string.activity));
                emotionCount = new Image().getActivityByDate(year, month, day, context);
            } else if (emotionType.equals("Partner")) {
                emotionCount.clear();
                tv_emotion_type.setText(context.getString(R.string.partner));
                emotionCount = new Image().getPartnerByDate(year, month, day, context);
            } else {
                emotionCount.clear();
                tv_emotion_type.setText(context.getString(R.string.weather));
                emotionCount = new Image().getWeatherByDate(year, month, day, context);
            }

            List<Map.Entry<String, Integer>> sortedList = new ArrayList<>(emotionCount.entrySet());
            sortedList.sort(Map.Entry.<String, Integer>comparingByValue().reversed());

            for (int i = 0; i < 4 && i < sortedList.size(); i++) {
                String icon = sortedList.get(i).getKey();
                int imageResourceId = context.getResources().getIdentifier(icon, "drawable", context.getPackageName());
                Drawable drawable = context.getDrawable(imageResourceId);
                if (i == 0) {
                    img1.setImageDrawable(drawable);
                    tv1.setText("x" + sortedList.get(i).getValue());
                } else if (i == 1) {
                    img2.setImageDrawable(drawable);
                    tv2.setText("x" + sortedList.get(i).getValue());
                } else if (i == 2) {
                    img3.setImageDrawable(drawable);
                    tv3.setText("x" + sortedList.get(i).getValue());
                } else {
                    img4.setImageDrawable(drawable);
                    tv4.setText("x" + sortedList.get(i).getValue());
                }
            }

            btn_viewall.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), ShowEmojiActivity.class);
                    ArrayList<String> dataList = new ArrayList<>();
                    for (Map.Entry<String, Integer> entry : sortedList) {
                        dataList.add(entry.getKey() + "," + entry.getValue());
                    }
                    intent.putStringArrayListExtra("sortedData", dataList);
                    v.getContext().startActivity(intent);
                }
            });
        }
    }
}
