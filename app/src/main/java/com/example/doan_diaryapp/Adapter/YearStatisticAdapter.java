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
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class YearStatisticAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private static int type_barchart = 1;
    private static int type_emotion = 2;

    private Context mContext;

    public YearStatisticAdapter(Context context) {
        mContext = context;
    }

    private List<Statistic> mStatistic;

    public void setData(List<Statistic> statistic){
        this.mStatistic = statistic;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(type_barchart == viewType){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_barchart,parent,false);
            return new BarChartViewHolder(view);
        }else if(type_emotion == viewType){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_emotion,parent,false);
            return new EmotionViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Statistic statistic = mStatistic.get(position);
        if(statistic == null) {
            return;
        }

        if(type_barchart == holder.getItemViewType()){
            BarChartViewHolder barChartViewHolder = (BarChartViewHolder) holder;
            barChartViewHolder.setData(statistic.getYear());
        }else if( type_emotion == holder.getItemViewType()){
            EmotionViewHolder emotionViewHolder = (EmotionViewHolder) holder;
            emotionViewHolder.setEmotion(statistic.getYear(),statistic.getMonth(),statistic.getEmotionType());
        }
    }

    @Override
    public int getItemCount() {
        if(mStatistic != null)
            return mStatistic.size();
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        Statistic statistic = mStatistic.get(position);
        if(statistic.getStatisticType() == 1)
            return type_barchart;
        else return type_emotion;
    }

    public class BarChartViewHolder extends RecyclerView.ViewHolder{
        private BarChart barChart;
        private TextView tv_trungbinh;
        public BarChartViewHolder(@NonNull View itemView) {
            super(itemView);
            barChart = itemView.findViewById(R.id.barChart);
            tv_trungbinh = itemView.findViewById(R.id.tv_average_rating);
        }

        public void setFormatBarchart() {
            barChart.setDescription(null);
            barChart.setScaleYEnabled(false); // tắt zoom trên cột Y
            barChart.setDoubleTapToZoomEnabled(false); // tắt chạm 2 lần để zoom
            barChart.setBackgroundColor(Color.parseColor("#00000000"));
            barChart.setHighlightPerTapEnabled(false); // tắt highlight điểm
            barChart.setHighlightPerDragEnabled(false); // same
            barChart.setExtraBottomOffset(6); // chỉnh margin cạnh dưới
            barChart.setExtraRightOffset(6);
            barChart.getLegend().setEnabled(false);// tắt chú thích (cái màu xanh)
            barChart.getAxisRight().setDrawLabels(false);
            barChart.getAxisRight().setAxisLineWidth(2);
            barChart.getAxisRight().setDrawGridLines(false);
            barChart.getAxisRight().setAxisLineColor(ContextCompat.getColor(mContext, R.color.statistics_grid));
            // lineChart.getXAxis().setDrawGridLines(false); // tắt vẽ lưới
            // lineChart.getAxisLeft().setDrawGridLines(false); // tắt vẽ lưới

            XAxis xAxis = barChart.getXAxis();
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            xAxis.setTextSize(14);
            xAxis.setTextColor(ContextCompat.getColor(mContext, R.color.md_theme_onSurfaceVariant));
            xAxis.setAxisLineColor(ContextCompat.getColor(mContext, R.color.statistics_grid));
            xAxis.setGridColor(ContextCompat.getColor(mContext, R.color.statistics_grid));
            xAxis.setAxisLineWidth(2);
            xAxis.setGranularity(1f);
            xAxis.setGridLineWidth(2);
            xAxis.setYOffset(6); // thêm khoảng cách giữa cột với số
            xAxis.setAxisMinimum(1f);
            xAxis.setAxisMaximum(12f);
            xAxis.setLabelCount(8);
            xAxis.setGranularityEnabled(true);
            // test

            YAxis yAxis = barChart.getAxisLeft();
            yAxis.setAxisLineColor(ContextCompat.getColor(mContext, R.color.statistics_grid));
            yAxis.setTextSize(14);
            yAxis.setTextColor(ContextCompat.getColor(mContext, R.color.md_theme_onSurfaceVariant));
            yAxis.setGridColor(ContextCompat.getColor(mContext, R.color.statistics_grid));
            yAxis.setXOffset(10); // khoảng cách giữa cột với số
            yAxis.setAxisMinimum(0f);
            yAxis.setAxisMaximum(10f);
            yAxis.setAxisLineWidth(2);
            yAxis.setLabelCount(10);
            yAxis.setGridLineWidth(2);
            yAxis.setGranularity(1f);
        }

        public void setData(int year) {
            setFormatBarchart();

            EntryService entryService = new EntryService(mContext);
            List<Entry> entryList = entryService.getOverallScoreByYear(year);

            //lấy dữ liệu năm
            Map<String,Integer> dayValues = new HashMap<>();
            Map<String, Integer> dayCounts = new HashMap<>();
            for(Entry entry:entryList){
                String date = entry.getDate().trim();
                String[] part = date.split("[:\\s-]");
                int d = Integer.parseInt(part[3]);
                int m = Integer.parseInt(part[4]);
                String day = d+"-"+m;
                int score = entry.getOverallScore();
                if (dayValues.containsKey(day)) {
                    dayValues.put(day, dayValues.get(day) + score);
                    dayCounts.put(day, dayCounts.get(day) + 1);
                } else {
                    dayValues.put(day, score);
                    dayCounts.put(day, 1);
                }
            }

            Map<Integer,Float> values = new HashMap<>();
            Map<Integer,Integer> counts = new HashMap<>();

            Set<String> setD = dayValues.keySet();

            for(String day: setD){
                String[] part = day.split("-");
                int m = Integer.parseInt(part[1]);
                float score = (float)dayValues.get(day)/dayCounts.get(day);

                if (values.containsKey(m)) {
                    values.put(m, values.get(m) + score);
                    counts.put(m, counts.get(m) + 1);
                } else {
                    values.put(m, score);
                    counts.put(m, 1);
                }
            }



            List<Map.Entry<Integer, Float>> entries = new ArrayList<>(values.entrySet());

            entries.sort(Comparator.comparingInt(Map.Entry::getKey));

            Map<Integer, Float> sortedMap = new LinkedHashMap<>();

            for (Map.Entry<Integer, Float> entry : entries) {
                sortedMap.put(entry.getKey(), entry.getValue());
            }

            //thêm dữ liệu vào biểu đồ
            float tb = 0;
            List<BarEntry> barEntries = new ArrayList<>();
            Set<Integer> set = sortedMap.keySet();

            for(Integer key:set){
                float value = sortedMap.get(key);
                value = value/counts.get(key);
                tb+=value;
                barEntries.add((new BarEntry(key,value)));
            }
            tb/=sortedMap.size();


            tv_trungbinh.setText(itemView.getResources().getString(R.string.average_rating)+": "+String.format("%.2f",tb));

            BarDataSet dataSet = new BarDataSet(barEntries,null);
            dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
            dataSet.setValueTextColor(ContextCompat.getColor(mContext, R.color.md_theme_onSurfaceVariant));

            BarData barData = new BarData(dataSet);
            barChart.setData(barData);
            barChart.notifyDataSetChanged();
            barChart.invalidate();
        }
    }

    public class EmotionViewHolder extends RecyclerView.ViewHolder{
        private Context context;
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
            context = itemView.getContext();
            tv_emotion_type = itemView.findViewById(R.id.tv_most_choosen);

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
        public void setEmotion(int year, int month, @NonNull String emotionType) {
            Map<String,Integer> emotionCount = new HashMap<>();

            if(emotionType.equals("Mood")){
                emotionCount.clear();
                tv_emotion_type.setText(context.getString(R.string.most_recorded_mood));
                emotionCount = new Image().getMood(year,month,context);

            }
            else if(emotionType.equals("Activity")){
                emotionCount.clear();
                tv_emotion_type.setText(context.getString(R.string.most_recorded_activity));
                emotionCount = new Image().getActivity(year,month,context);
            }
            else if(emotionType.equals("Partner")){
                emotionCount.clear();
                tv_emotion_type.setText(context.getString(R.string.most_recorded_partner));
                emotionCount = new Image().getPartner(year,month,context);
            }
            else {
                emotionCount.clear();
                tv_emotion_type.setText(context.getString(R.string.most_recorded_weather));
                emotionCount = new Image().getWeather(year,month,context);
            }

            List<Map.Entry<String, Integer>> sortedList = new ArrayList<>(emotionCount.entrySet());
            sortedList.sort(Map.Entry.<String, Integer>comparingByValue().reversed());

            for(int i=0;i<4&&i<sortedList.size();i++) {
                String icon = sortedList.get(i).getKey();
                int imageResourceId = context.getResources().getIdentifier(icon, "drawable", context.getPackageName());
                Drawable drawable = context.getDrawable(imageResourceId);
                if(i == 0){
                    img1.setImageDrawable(drawable);
                    tv1.setText("x"+sortedList.get(i).getValue());
                }
                else if(i==1){
                    img2.setImageDrawable(drawable);
                    tv2.setText("x"+sortedList.get(i).getValue());
                }
                else if(i==2){
                    img3.setImageDrawable(drawable);
                    tv3.setText("x"+sortedList.get(i).getValue());
                }
                else {img4.setImageDrawable(drawable);
                    tv4.setText("x"+sortedList.get(i).getValue());}
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
