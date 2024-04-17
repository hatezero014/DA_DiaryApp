package com.example.doan_diaryapp.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doan_diaryapp.Models.Statistic;
import com.example.doan_diaryapp.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Random;

public class StatisticAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private static int type_linechart = 1;
    private static int type_emotion = 2;

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
            emotionViewHolder.setEmotion(statistic.getYear(),statistic.getMonth());
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

        public void setData(int year, int month) {
            ArrayList<String>values = new ArrayList<>();

            int trucX = 0;
            if(month == 0)
                trucX = 12;
            else trucX = getDayOfMonth(year,month);
            for (int i = 1; i <= trucX; i++) {
                values.add(String.valueOf(i));
            }

            XAxis xAxis = lineChart.getXAxis();
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            xAxis.setValueFormatter(new IndexAxisValueFormatter(values));
            xAxis.setLabelCount(6);
            xAxis.setTextColor(Color.GREEN);

    // Chia đều các cột và hiển thị các văn bản cách đều nhau
            xAxis.setGranularity(1f);
            xAxis.setGranularityEnabled(true);


            YAxis yAxis = lineChart.getAxisLeft();
            yAxis.setAxisMinimum(0f);
            yAxis.setAxisMaximum(10f);
            yAxis.setAxisLineWidth(1f);
            yAxis.setAxisLineColor(Color.BLACK);
            yAxis.setTextColor(Color.RED);
            yAxis.setLabelCount(10);

            float tb = 0;
            List<Entry> entries = new ArrayList<>();
            Random random = new Random();
            for(int i=1;i<trucX;i++){
                float x = random.nextFloat()*10f;
                tb+=x;
                entries.add(new Entry(i,x));
            }
            tb/=trucX;
            tv_trungbinh.setText(String.valueOf(tb));

            LineDataSet dataSet = new LineDataSet(entries,null);
            dataSet.setColor(Color.BLUE);

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

        private Spinner spinner;

        private ImageView img1;
        private TextView tv1;
        private ImageView img2;
        private TextView tv2;
        private ImageView img3;
        private TextView tv3;
        private ImageView img4;
        private TextView tv4;
        public EmotionViewHolder(@NonNull View itemView) {
            super(itemView);

            spinner = itemView.findViewById(R.id.spn_emotion);
            img1 = itemView.findViewById(R.id.imageView1);
            tv1 = itemView.findViewById(R.id.tv_img1);
            img2 = itemView.findViewById(R.id.imageView2);
            tv2 = itemView.findViewById(R.id.tv_img2);
            img3 = itemView.findViewById(R.id.imageView3);
            tv3 = itemView.findViewById(R.id.tv_img3);
            img4 = itemView.findViewById(R.id.imageView4);
            tv4 = itemView.findViewById(R.id.tv_img4);
        }



        public void setSpinner() {
            ArrayList<String> arrayEmotion = new ArrayList<>();
            arrayEmotion.add(itemView.getContext().getString(R.string.mood));
            arrayEmotion.add(itemView.getContext().getString(R.string.weather));
            arrayEmotion.add(itemView.getContext().getString(R.string.partner));
            arrayEmotion.add(itemView.getContext().getString(R.string.activity));

            ArrayAdapter arrayAdapter = new ArrayAdapter(itemView.getContext(), android.R.layout.simple_spinner_item, arrayEmotion){
                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    View v = super.getView(position, convertView, parent);
                    ((TextView) v).setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20); // Đặt kích thước chữ
                    return v;
                }

                @Override
                public View getDropDownView(int position, View convertView, ViewGroup parent) {
                    View v = super.getDropDownView(position, convertView, parent);
                    ((TextView) v).setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20); // Đặt kích thước chữ
                    return v;
                }
            };
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            spinner.setAdapter(arrayAdapter);
        }


        public void setEmotion(int year, int month) {
            setSpinner();
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    showEmotion(position);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }

                void showEmotion(int position){
                    img1.setImageDrawable(null);
                    img2.setImageDrawable(null);
                    img3.setImageDrawable(null);
                    img4.setImageDrawable(null);

                    switch (position){
                        case 0:
                            img1.setImageResource(R.drawable.emoji_acitivity_travel);
                            break;
                        case 1:
                            img2.setImageResource(R.drawable.emoji_activity_bake);
                            break;
                        case 2:
                            img3.setImageResource(R.drawable.emoji_activity_biking);
                            break;
                        case 3:
                            img4.setImageResource(R.drawable.emoji_activity_cook);
                            break;
                    }
                }
            });
        }
    }
}
