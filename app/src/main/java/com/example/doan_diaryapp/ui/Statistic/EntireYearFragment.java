package com.example.doan_diaryapp.ui.Statistic;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.doan_diaryapp.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;


public class EntireYearFragment extends Fragment {

    private LineChart lineChart;

    private List<String> values;

    ArrayList<Integer> imageList;

    private ImageView imageView1;
    private ImageView imageView2;
    private ImageView imageView3;
    private TextView title_year;
    private TextView tb_year;

    public EntireYearFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_entire_year, container, false);

        lineChart = view.findViewById(R.id.line_chart);

        imageView1 = view.findViewById(R.id.img1);
        imageView2 = view.findViewById(R.id.img2);
        imageView3 = view.findViewById(R.id.img3);
        title_year = view.findViewById(R.id.title_year);
        tb_year = view.findViewById(R.id.tb_year);

        title_year.setText(getResources().getString(R.string.your_mood_chart));

        values = new ArrayList<>();
        for (int i = 1; i <= 12; i++) {
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
        yAxis.setTextColor(Color.YELLOW);
        yAxis.setLabelCount(10);

        float tb = 0;
        List<Entry> entries = new ArrayList<>();
        Random random = new Random();
        for(int i=0;i<12;i++){
            float x = random.nextFloat()*10f;
            tb+=x;
            entries.add(new Entry(i,x));
        }
        tb/=12;
        tb_year.setText(String.valueOf(tb));

        LineDataSet dataSet = new LineDataSet(entries,null);
        dataSet.setColor(Color.BLUE);

        LineData lineData = new LineData(dataSet);
        lineChart.setData(lineData);
        lineChart.invalidate();

        imageList = new ArrayList<>();
        getImage();

        return view;
    }

    private void getImage() {
        imageList.add(R.drawable.ic_circle3);
        imageList.add(R.drawable.ic_circle4);
        imageList.add(R.drawable.ic_circle5);

        int dem = 4;
        Map<Integer,Integer> count = new HashMap<>();
        for(Integer i : imageList){
            count.put(i,dem);
            dem++;
        }

        List<Map.Entry<Integer, Integer>> list = new ArrayList<>(count.entrySet());
        list.sort(Map.Entry.comparingByValue(Collections.reverseOrder()));

        // Lấy ra 2 hình ảnh có biến dem cao nhất
        imageView1.setImageResource(list.get(0).getKey());
        imageView2.setImageResource(list.get(1).getKey());
    }
}