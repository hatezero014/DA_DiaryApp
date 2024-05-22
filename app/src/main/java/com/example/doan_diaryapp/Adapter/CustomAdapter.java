package com.example.doan_diaryapp.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doan_diaryapp.Models.Custom;
import com.example.doan_diaryapp.Models.Entry;
import com.example.doan_diaryapp.R;
import com.example.doan_diaryapp.Service.EntryService;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class CustomAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private static int type_barchart = 1;
    private static int type_emotion = 2;

    private Context mContext;

    public CustomAdapter(Context mContext) {
        this.mContext = mContext;
    }

    private List<Custom> mStatistic;

    public void setData(List<Custom> statistic){
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
        Custom statistic = mStatistic.get(position);
        if(statistic == null) {
            return;
        }

        if(type_barchart == holder.getItemViewType()){
            BarChartViewHolder barChartViewHolder = (BarChartViewHolder) holder;
            barChartViewHolder.setData(statistic.getByear(),statistic.getBmonth(),statistic.getAyear(),statistic.getAmonth());
        }else if( type_emotion == holder.getItemViewType()){
            EmotionViewHolder emotionViewHolder = (EmotionViewHolder) holder;
            emotionViewHolder.setEmotion(statistic.getByear(),statistic.getBmonth(),statistic.getAyear(),statistic.getAmonth(),statistic.getEmotionType());
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
        Custom statistic = mStatistic.get(position);
        if(statistic.getStatisticType() == 1)
            return type_barchart;
        else return type_emotion;
    }

    public class BarChartViewHolder extends RecyclerView.ViewHolder {
        private BarChart barChart;
        private TextView tv_trungbinh;

        public BarChartViewHolder(@NonNull View itemView) {
            super(itemView);
            barChart = itemView.findViewById(R.id.barChart);
            tv_trungbinh = itemView.findViewById(R.id.tv_average_rating);
        }

        private void setBarChartFormat(int trucX) {
            barChart.getAxisRight().setDrawLabels(false);
            barChart.setDragEnabled(true);

            XAxis xAxis = barChart.getXAxis();
            xAxis.setAxisMinimum(0);
            xAxis.setAxisMaximum(trucX);
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            xAxis.setTextSize(14);
            xAxis.setLabelCount(trucX + 1);
            xAxis.setGranularity(1f);

            YAxis yAxis = barChart.getAxisLeft();
            yAxis.setAxisMaximum(10f);
            yAxis.setAxisMinimum(0f);
            yAxis.setLabelCount(10);
            yAxis.setTextSize(14);
            yAxis.setGranularity(1f);
        }

        public void setData(int byear, int bmonth, int ayear, int amonth) {
            int trucX = 0;
            if ((ayear - byear) != 0) {
                trucX = (ayear - byear) * 12 + (amonth - bmonth);
            } else trucX = Math.abs(amonth - bmonth);

            ArrayList<String> labels = new ArrayList<>();
            setBarChartFormat(trucX);

            EntryService entryService = new EntryService(mContext);
            List<Entry> entryList = entryService.getOverallScoreCustom(byear, bmonth, ayear, amonth);

            Map<String,Integer> dmyValues = new HashMap<>();
            Map<String,Integer> dmyCounts = new HashMap<>();
            for(Entry entry : entryList){
                String date = entry.getDate().trim();
                String[] part = date.split("[:\\s-]");
                int d = Integer.parseInt(part[3]);
                int m = Integer.parseInt(part[4]);
                int y = Integer.parseInt(part[5]);
                String day = d+"-"+m+"-"+y;
                int score = entry.getOverallScore();
                if (dmyValues.containsKey(day)) {
                    dmyValues.put(day, dmyValues.get(day) + score);
                    dmyCounts.put(day, dmyCounts.get(day) + 1);
                } else {
                    dmyValues.put(day, score);
                    dmyCounts.put(day, 1);
                }
            }

            Map<Integer,Float> values = new HashMap<>();
            Map<Integer,Integer> counts = new HashMap<>();

            Set<String> key = dmyValues.keySet();

            for(String day : key){
                String[] part = day.split("-");
                String skey;
                if(part[1].length() == 1){
                    skey = part[2]+"0"+part[1];
                }else skey = part[2]+part[1];

                int ikey = Integer.parseInt(skey);
                float score = (float)dmyValues.get(day)/dmyCounts.get(day);

                if (values.containsKey(ikey)) {
                    values.put(ikey, values.get(ikey) + score);
                    counts.put(ikey, counts.get(ikey) + 1);
                } else {
                    values.put(ikey, score);
                    counts.put(ikey, 1);
                }
            }

            List<Map.Entry<Integer, Float>> entries = new ArrayList<>(values.entrySet());

            entries.sort(Comparator.comparingInt(Map.Entry::getKey));

            Map<Integer, Float> sortedMap = new LinkedHashMap<>();

            for (Map.Entry<Integer, Float> entry : entries) {
                sortedMap.put(entry.getKey(), entry.getValue());
            }

            // Tạo calendar cho thời gian bắt đầu và kết thúc
            Calendar start = Calendar.getInstance();
            start.set(byear, bmonth - 1, 1);  // Calendar sử dụng tháng từ 0-11, do đó cần -1
            Calendar end = Calendar.getInstance();
            end.set(ayear, amonth - 1, 1);  // Tương tự, tháng -1


            //thêm dữ liệu vào biểu đồ
            float tb = 0;
            List<BarEntry> barEntries = new ArrayList<>();
            int index = 0;

            // Duyệt qua từng tháng từ thời gian bắt đầu đến thời gian kết thúc
            while (start.before(end) || start.equals(end)) {
                int month = start.get(Calendar.MONTH) + 1; // Lấy tháng (0-11) + 1 để đúng định dạng mm
                int year = start.get(Calendar.YEAR);

                // Định dạng mm-yyyy và thêm vào labels
                labels.add(String.format("%02d/%d", month, year));

                // Tăng tháng lên 1
                start.add(Calendar.MONTH, 1);

                String skey = String.format("%d%02d",year,month);
                int ikey = Integer.parseInt(skey);
                if(sortedMap.containsKey(ikey)){
                    float value = sortedMap.get(ikey);
                    value = value/counts.get(ikey);
                    barEntries.add(new BarEntry(index,value));
                    tb+=value;
                }

                index++;
            }
            tb/=sortedMap.size();
            tv_trungbinh.setText(itemView.getResources().getString(R.string.average_rating)+": "+String.format("%.2f",tb));

            BarDataSet barDataSet = new BarDataSet(barEntries, null);
            barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);

            BarData barData = new BarData(barDataSet);
            barChart.setData(barData);
            barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(labels));
            barChart.notifyDataSetChanged();
            if(trucX>5)
                barChart.setVisibleXRangeMaximum(5f);
            barChart.invalidate();
        }
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

        public void setEmotion(int byear, int bmonth, int ayear, int amonth, String emotionType) {

        }
    }
}
