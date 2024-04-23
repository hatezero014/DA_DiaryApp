package com.example.doan_diaryapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doan_diaryapp.Models.EmojiInfo;
import com.example.doan_diaryapp.Models.Emotion;
import com.example.doan_diaryapp.Models.Statistic;
import com.example.doan_diaryapp.R;
import com.example.doan_diaryapp.Service.EmotionService;
import com.example.doan_diaryapp.Service.EntryService;
import com.example.doan_diaryapp.ShowEmojiActivity;
import com.example.doan_diaryapp.ui.image.Image;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class StatisticAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private static int type_linechart = 1;
    private static int type_emotion = 2;

    private Context mContext;

    public StatisticAdapter(Context context) {
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
        if(type_linechart == viewType){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chart,parent,false);
            return new LineChartViewHolder(view);
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

        if(type_linechart == holder.getItemViewType()){
            LineChartViewHolder lineChartViewHolder = (LineChartViewHolder) holder;
            lineChartViewHolder.setData(statistic.getYear(),statistic.getMonth());
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
            return type_linechart;
        else return type_emotion;
    }

    public class LineChartViewHolder extends RecyclerView.ViewHolder{
        private LineChart lineChart;
        private TextView tv_trungbinh;
        public LineChartViewHolder(@NonNull View itemView) {
            super(itemView);
            lineChart = itemView.findViewById(R.id.lineChart);
            tv_trungbinh = itemView.findViewById(R.id.tv_average_rating);
        }

        public void setFormatLinechart(int trucX) {
            lineChart.setDescription(null);
            lineChart.setScaleYEnabled(false); // tắt zoom trên cột Y
            lineChart.setDoubleTapToZoomEnabled(false); // tắt chạm 2 lần để zoom
            lineChart.setBackgroundColor(Color.parseColor("#00000000"));
            lineChart.setHighlightPerTapEnabled(false); // tắt highlight điểm
            lineChart.setHighlightPerDragEnabled(false); // same
            lineChart.setExtraBottomOffset(6); // chỉnh margin cạnh dưới
            lineChart.setExtraRightOffset(6);
            lineChart.getLegend().setEnabled(false); // tắt chú thích (cái màu xanh)
            lineChart.getAxisRight().setEnabled(false); // tắt cột Y bên phải
            // lineChart.getXAxis().setDrawGridLines(false); // tắt vẽ lưới
            // lineChart.getAxisLeft().setDrawGridLines(false); // tắt vẽ lưới

            XAxis xAxis = lineChart.getXAxis();
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
            xAxis.setAxisMaximum(trucX);
            xAxis.setLabelCount(8);
            xAxis.setGranularityEnabled(true);
            // test

            YAxis yAxis = lineChart.getAxisLeft();
            yAxis.setAxisLineColor(ContextCompat.getColor(mContext, R.color.statistics_grid));
            yAxis.setTextSize(14);
            yAxis.setTextColor(ContextCompat.getColor(mContext, R.color.md_theme_onSurfaceVariant));
            xAxis.setAxisLineColor(ContextCompat.getColor(mContext, R.color.statistics_grid));
            yAxis.setGridColor(ContextCompat.getColor(mContext, R.color.statistics_grid));
            yAxis.setXOffset(10); // khoảng cách giữa cột với số
            yAxis.setAxisMinimum(0f);
            yAxis.setAxisMaximum(10f);
            yAxis.setAxisLineWidth(2);
            yAxis.setLabelCount(10);
            yAxis.setGridLineWidth(2);
            // test
            yAxis.setDrawLabels(false);

        }

        public void setData(int year, int month) {
            int trucX = 0;
            if(month == 0)
                trucX = 12;
            else trucX = getDayOfMonth(year,month);

            setFormatLinechart(trucX);

            EntryService entryService = new EntryService(itemView.getContext());
            List<com.example.doan_diaryapp.Models.Entry> entryListM = entryService.getOverallScoreByMonthYear(month,year);
            List<com.example.doan_diaryapp.Models.Entry> entryListY = entryService.getOverallScoreByYear(year);

            Map<Integer,Integer> valuesM = new HashMap<>();
            for(com.example.doan_diaryapp.Models.Entry entry:entryListM){
                String date = entry.getDate().trim();
                String[] part = date.split("-");
                int d = Integer.parseInt(part[0]);
                int score = entry.getOverallScore();
                valuesM.put(d,score);
            }

            Map<Integer,Integer> valuesY = new HashMap<>();
            Map<Integer, Integer> counts = new HashMap<>();
            for(com.example.doan_diaryapp.Models.Entry entry:entryListY){
                String date = entry.getDate().trim();
                String[] part = date.split("-");
                int m = Integer.parseInt(part[1]);
                int score = entry.getOverallScore();
                if (valuesY.containsKey(m)) {
                    valuesY.put(m, valuesY.get(m) + score);
                    counts.put(m, counts.get(m) + 1);
                } else {
                    valuesY.put(m, score);
                    counts.put(m, 1);
                }
            }

            float tb = 0;
            List<Entry> entries = new ArrayList<>();
            Set<Integer> setM = valuesM.keySet();
            Set<Integer> setY = valuesY.keySet();
            if(trucX != 12){
                for(Integer key:setM){
                    int value = valuesM.get(key);
                    tb+=value;
                    entries.add((new Entry(key,value)));
                }
                tb/=valuesM.size();
            }
            else{
                for(Integer key:setY){
                    float value = valuesY.get(key);
                    value = value/counts.get(key);
                    tb+=value;
                    entries.add((new Entry(key,value)));
                }
                tb/=valuesY.size();
            }

            tv_trungbinh.setText(itemView.getResources().getString(R.string.average_rating)+": "+String.format("%.2f",tb));

            LineDataSet dataSet = new LineDataSet(entries,null);
            dataSet.setLineWidth(2); // chỉnh kích thước đường
            dataSet.setDrawValues(false); // tắt số trên điểm
            dataSet.setColor(Color.BLUE);
            dataSet.setCircleColor(Color.BLUE);
            dataSet.setCircleRadius(1);
            dataSet.setDrawCircleHole(false);

            LineData lineData = new LineData(dataSet);
            lineChart.setData(lineData);
            lineChart.invalidate();
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

        public void setEmotion(int year, int month, @NonNull String emotionType) {
            Map<String,Integer> emotionCount = new HashMap<>();

            if(emotionType.equals("Mood")){
                emotionCount.clear();
                tv_emotion_type.setText(context.getString(R.string.mood));
                emotionCount = new Image().getMood(year,month,context);

            }
            else if(emotionType.equals("Activity")){
                emotionCount.clear();
                tv_emotion_type.setText(context.getString(R.string.activity));
                emotionCount = new Image().getActivity(year,month,context);
            }
            else if(emotionType.equals("Partner")){
                emotionCount.clear();
                tv_emotion_type.setText(context.getString(R.string.partner));
                emotionCount = new Image().getPartner(year,month,context);
            }
            else {
                emotionCount.clear();
                tv_emotion_type.setText(context.getString(R.string.weather));
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
                    Intent intent = new Intent(v.getContext(),ShowEmojiActivity.class);
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
