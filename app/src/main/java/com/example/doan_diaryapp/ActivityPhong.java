package com.example.doan_diaryapp;

import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doan_diaryapp.Adapter.CirclesAdapter;
import com.example.doan_diaryapp.Models.Circle;
import com.example.doan_diaryapp.Models.Circles;

import java.util.ArrayList;
import java.util.List;

public class ActivityPhong extends BaseActivity {

    private RecyclerView listCircle;
    private CirclesAdapter circlesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phong);

        listCircle = findViewById(R.id.list_circle);

        circlesAdapter = new CirclesAdapter(this);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        listCircle.setLayoutManager(linearLayoutManager);

        listCircle.setNestedScrollingEnabled(false);

        circlesAdapter.setData(getListCircles());

        listCircle.setAdapter(circlesAdapter);

    }

    private List<Circles> getListCircles() {
        List<Circles> list = new ArrayList<>();

        for(int i=0;i<5;i++){
            List<Circle> listCircle = new ArrayList<>();
            switch (i){
                case 0:
                    for(int j=0;j<12;j++){
                        listCircle.add(new Circle(R.drawable.ic_circle1));
                    }
                    break;
                case 1:
                    for(int j=0;j<12;j++){
                        listCircle.add(new Circle(R.drawable.ic_circle2));
                    }
                    break;
                case 2:
                    for(int j=0;j<12;j++){
                        listCircle.add(new Circle(R.drawable.ic_circle3));
                    }
                    break;
                case 3:
                    for(int j=0;j<12;j++){
                        listCircle.add(new Circle(R.drawable.ic_circle4));
                    }
                    break;
                default:
                    for(int j=0;j<12;j++){
                        listCircle.add(new Circle(R.drawable.ic_circle5));
                    }
                    break;
            }
            list.add(new Circles(listCircle));
        }
        return list;
    }
}