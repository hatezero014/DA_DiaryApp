package com.example.doan_diaryapp.Adapter;

import android.content.Context;
import android.graphics.Color;
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
import com.example.doan_diaryapp.R;
import com.example.doan_diaryapp.Service.EntryService;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CustomAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private static int type_linechart = 1;
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
        if(type_linechart == viewType){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chart,parent,false);
            return new CustomAdapter.LineChartViewHolder(view);
        }else if(type_emotion == viewType){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_emotion,parent,false);
            return new CustomAdapter.EmotionViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Custom statistic = mStatistic.get(position);
        if(statistic == null) {
            return;
        }

        if(type_linechart == holder.getItemViewType()){
            CustomAdapter.LineChartViewHolder lineChartViewHolder = (CustomAdapter.LineChartViewHolder) holder;
            lineChartViewHolder.setData(statistic.getByear(),statistic.getBmonth(),statistic.getAyear(),statistic.getAmonth());
        }else if( type_emotion == holder.getItemViewType()){
            CustomAdapter.EmotionViewHolder emotionViewHolder = (CustomAdapter.EmotionViewHolder) holder;
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

        public void setData(int byear, int bmonth, int ayear, int amonth) {
            ArrayList<Entry> entries = new ArrayList<>();
            ArrayList<Integer> colors = new ArrayList<>();
            ArrayList<String> labels = new ArrayList<>();

            setLineChartFormat(byear, bmonth, ayear, amonth,labels);

            EntryService entryService = new EntryService(itemView.getContext());
            List<com.example.doan_diaryapp.Models.Entry> entryList = entryService.getOverallScoreCustom(byear, bmonth, ayear, amonth);


            Calendar calendar = Calendar.getInstance();
            calendar.set(byear, bmonth - 1, 1);

            int index = 0;  // Chỉ số cho Entry

            while (calendar.get(Calendar.YEAR) < ayear ||
                    (calendar.get(Calendar.YEAR) == ayear && calendar.get(Calendar.MONTH) <= (amonth - 1))) {
                int month = calendar.get(Calendar.MONTH)+1;  // Chuyển tháng từ 0-based sang 1-based
                int year = calendar.get(Calendar.YEAR);

                // Thêm nhãn cho trục x
                labels.add(month + "/" + year);

                entries.add(new Entry(index, (float) (Math.random() * 100)));  // Sử dụng giá trị ngẫu nhiên
                index++;

                // Xác định màu cho mỗi tháng tùy theo năm
                if (year == byear) {
                    colors.add(0xFF0000FF);  // Màu xanh cho năm bắt đầu (2023)
                } else {
                    colors.add(0xFFFF0000);  // Màu đỏ cho năm kết thúc (2024)
                }

                calendar.add(Calendar.MONTH, 1);
            }
            LineDataSet dataSet = new LineDataSet(entries, "Dữ liệu hàng tháng");
            dataSet.setColors(colors);  // Áp dụng màu tùy theo năm

            LineData lineData = new LineData(dataSet);

            // Đặt dữ liệu cho LineChart
            lineChart.setData(lineData);
        }

        private void setLineChartFormat(int byear, int bmonth, int ayear, int amonth, ArrayList<String> labels) {
            int trucX = 0;
            if((ayear-byear)!=0){
                trucX = (ayear-byear)*12+(amonth-bmonth);
            }
            else trucX = Math.abs(amonth-bmonth);
            lineChart.setDescription(null);
            lineChart.setScaleYEnabled(false); // tắt zoom trên cột Y
            lineChart.setDoubleTapToZoomEnabled(false); // tắt chạm 2 lần để zoom
            lineChart.setBackgroundColor(Color.parseColor("#00000000"));
            lineChart.setHighlightPerTapEnabled(false); // tắt highlight điểm
            lineChart.setHighlightPerDragEnabled(false); // same
            lineChart.setExtraBottomOffset(6); // chỉnh margin cạnh dưới
            lineChart.setExtraRightOffset(6);
            lineChart.getLegend().setEnabled(false);// tắt chú thích (cái màu xanh)
            lineChart.getAxisRight().setDrawLabels(false);
            lineChart.getAxisRight().setAxisLineWidth(2);
            lineChart.getAxisRight().setDrawGridLines(false);
            lineChart.getAxisRight().setAxisLineColor(ContextCompat.getColor(mContext, R.color.statistics_grid));
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
            xAxis.setAxisMinimum(0f);
            xAxis.setAxisMaximum(trucX);
            xAxis.setLabelCount(trucX+1);
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


            xAxis.setValueFormatter(new ValueFormatter() {
                @Override
                public String getFormattedValue(float value) {
                    int index = (int) value;
                    if (index >= 0 && index < labels.size()) {
                        return labels.get(index);  // Trả lại nhãn cho giá trị x
                    } else {
                        return "";
                    }
                }
            });
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
