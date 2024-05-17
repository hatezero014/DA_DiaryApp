package com.example.doan_diaryapp.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
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
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MonthStatisticAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final static int type_barchart = 1;
    private final static int type_emotion = 2;
    private List<Statistic> mStatistic;
    private Context context;

    public MonthStatisticAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<Statistic> list) {
        this.mStatistic = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (type_barchart == viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_barchart, parent, false);
            return new BarChartViewHolder(view);
        } else if (type_emotion == viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_emotion, parent, false);
            return new EmotionViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Statistic statistic = mStatistic.get(position);
        if (statistic == null) {
            return;
        }

        if (type_barchart == holder.getItemViewType()) {
            BarChartViewHolder barChartViewHolder = (BarChartViewHolder) holder;
            barChartViewHolder.setData(statistic.getYear(), statistic.getMonth());
        } else if (type_emotion == holder.getItemViewType()) {
            EmotionViewHolder emotionViewHolder = (EmotionViewHolder) holder;
            emotionViewHolder.setEmotion(statistic.getYear(), statistic.getMonth(), statistic.getEmotionType());
        }
    }

    @Override
    public int getItemCount() {
        if (mStatistic != null) {
            return mStatistic.size();
        }
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        Statistic statistic = mStatistic.get(position);
        if (statistic.getStatisticType() == 1)
            return type_barchart;
        else return type_emotion;
    }

    public class BarChartViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_trungbinh;
        private BarChart barChart;

        public BarChartViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_trungbinh = itemView.findViewById(R.id.tv_average_rating);
            barChart = itemView.findViewById(R.id.barChart);
        }

        private void setBarchartFormat(int trucX) {
            barChart.getAxisRight().setDrawLabels(false);

            XAxis xAxis = barChart.getXAxis();
            xAxis.setAxisMinimum(1f);
            xAxis.setAxisMaximum(trucX);
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            xAxis.setTextSize(14);
            xAxis.setLabelCount(10);
            xAxis.setGranularity(1f);


            YAxis yAxis = barChart.getAxisLeft();
            yAxis.setAxisMaximum(10f);
            yAxis.setAxisMinimum(0f);
            yAxis.setLabelCount(10);
            yAxis.setTextSize(14);
        }

        public void setData(int year, int month) {
            int trucX = getDayOfMonth(year, month);
            setBarchartFormat(trucX);

            EntryService entryService = new EntryService(context);
            List<Entry> entryList = entryService.getOverallScoreByMonthYear(month, year);

            Map<Integer,Integer> values = new HashMap<>();
            Map<Integer, Integer> counts = new HashMap<>();

            for (Entry entry:entryList){
                String date = entry.getDate().trim();
                String[] part = date.split("[:\\s-]");
                int d = Integer.parseInt(part[3]);
                int score = entry.getOverallScore();
                if (values.containsKey(d)) {
                    values.put(d, values.get(d) + score);
                    counts.put(d, counts.get(d) + 1);
                } else {
                    values.put(d, score);
                    counts.put(d, 1);
                }
            }

            List<Map.Entry<Integer, Integer>> entries = new ArrayList<>(values.entrySet());

            entries.sort(Comparator.comparingInt(Map.Entry::getKey));

            Map<Integer, Integer> sortedMap = new LinkedHashMap<>();

            for (Map.Entry<Integer, Integer> entry : entries) {
                sortedMap.put(entry.getKey(), entry.getValue());
            }

            float tb = 0f;
            List<BarEntry>barChartList = new ArrayList<>();
            Set<Integer> set = sortedMap.keySet();
            for(Integer key:set){
                float value = sortedMap.get(key);
                value = value/counts.get(key);
                tb+=value;
                barChartList.add(new BarEntry(key,value));
            }
            tb/=sortedMap.size();
            tv_trungbinh.setText(itemView.getResources().getString(R.string.average_rating)+": "+String.format("%.2f",tb));

            BarDataSet barDataSet = new BarDataSet(barChartList,null);
            barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);

            BarData barData = new BarData(barDataSet);
            barChart.setData(barData);
            barChart.notifyDataSetChanged();
            barChart.invalidate();
        }

    }

    private int getDayOfMonth(int year, int month) {
        int dayOfMonth = 0;
        switch (month){
            case 4:
            case 6:
            case 9:
            case 11:
                dayOfMonth = 30;
                break;
            case 2:
                if(isLeapYear(year))
                    dayOfMonth = 29;
                else dayOfMonth=28;
                break;
            default:
                dayOfMonth = 31;
                break;
        }
        return  dayOfMonth;
    }

    private boolean isLeapYear(int year) {
        return new GregorianCalendar().isLeapYear(year);
    }


    public class EmotionViewHolder extends RecyclerView.ViewHolder {
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
        public void setEmotion(int year, int month, String emotionType) {
            Map<String, Integer> emotionCount = new HashMap<>();

            if (emotionType.equals("Mood")) {
                emotionCount.clear();
                tv_emotion_type.setText(context.getString(R.string.mood));
                emotionCount = new Image().getMood(year, month, context);

            } else if (emotionType.equals("Activity")) {
                emotionCount.clear();
                tv_emotion_type.setText(context.getString(R.string.activity));
                emotionCount = new Image().getActivity(year, month, context);
            } else if (emotionType.equals("Partner")) {
                emotionCount.clear();
                tv_emotion_type.setText(context.getString(R.string.partner));
                emotionCount = new Image().getPartner(year, month, context);
            } else {
                emotionCount.clear();
                tv_emotion_type.setText(context.getString(R.string.weather));
                emotionCount = new Image().getWeather(year, month, context);
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