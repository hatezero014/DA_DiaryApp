package com.example.doan_diaryapp.ui.Statistic;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.doan_diaryapp.Adapter.CustomAdapter;
import com.example.doan_diaryapp.Models.Custom;
import com.example.doan_diaryapp.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class CustomFragment extends Fragment {
    private RecyclerView recyclerView;
    private CustomAdapter customAdapter;
    private Spinner bMonth;
    private Spinner bYear;
    private Spinner aMonth;
    private Spinner aYear;
    private boolean isUpdating = false;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_custom, container, false);

        recyclerView = view.findViewById(R.id.rcv_thong_ke);
        bMonth = view.findViewById(R.id.spn_bmonth);
        bYear = view.findViewById(R.id.spn_byear);
        aMonth = view.findViewById(R.id.spn_amonth);
        aYear = view.findViewById(R.id.spn_ayear);

        updateSpinnerYear(view);
        updateSpinnerMonth(view);

        customAdapter = new CustomAdapter(view.getContext());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        customAdapter.setData(getListStatistic());
        recyclerView.setAdapter(customAdapter);

        return view;
    }

    private void updateSpinnerMonth(View view) {
        ArrayList<Integer> month = new ArrayList<>();
        for(int i=1;i<=12;i++){
            month.add(i);
        }

        ArrayAdapter<Integer> adapterMonth = new ArrayAdapter<>(view.getContext(), android.R.layout.simple_spinner_item,month);
        adapterMonth.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bMonth.setAdapter(adapterMonth);
        bMonth.setSelection(Calendar.getInstance().get(Calendar.MONTH));
        aMonth.setAdapter(adapterMonth);
        aMonth.setSelection(Calendar.getInstance().get(Calendar.MONTH));

        setOnItemSelectedListener(bMonth);
        setOnItemSelectedListener(aMonth);
    }

    private void setOnItemSelectedListener(Spinner spinner){
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                checkValidity();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private int prevBMonth, prevBYear, prevAMonth, prevAYear;

    private void checkValidity() {
        int bmonth = (Integer) bMonth.getSelectedItem();  // Tháng bắt đầu
        int byear = (Integer) bYear.getSelectedItem();  // Năm bắt đầu
        int amonth = (Integer) aMonth.getSelectedItem();  // Tháng kết thúc
        int ayear = (Integer) aYear.getSelectedItem();  // Năm kết thúc

        // Kiểm tra xem tháng và năm bắt đầu có lớn hơn tháng và năm kết thúc không
        if (byear > ayear || (byear == ayear && bmonth > amonth)) {
            Toast.makeText(getContext(), "Ngày bắt đầu không thể lớn hơn ngày kết thúc.", Toast.LENGTH_SHORT).show();
            // Đặt lại giá trị bắt đầu thành giá trị kết thúc
            bMonth.setSelection(prevBMonth);
            bYear.setSelection(prevBYear);
            isUpdating = true;
            return;
        }

        // Tính số tháng giữa các giá trị bắt đầu và kết thúc
        int totalMonths = (ayear - byear) * 12 + (amonth - bmonth);

        // Kiểm tra nếu số tháng vượt quá 12
        if (totalMonths > 12) {
            Toast.makeText(getContext(), "Khoảng cách giữa các giá trị không được vượt quá 13 tháng.", Toast.LENGTH_SHORT).show();
            // Đặt lại tháng bắt đầu sao cho khoảng cách không vượt quá 12 tháng
            bMonth.setSelection(prevBMonth);
            if (aMonth.getSelectedItemPosition() < 12) {
                bYear.setSelection(prevBYear);
            } else {
                bYear.setSelection(prevBYear);
            }
            isUpdating = true;
            return;
        }

        if(!isUpdating) {
            customAdapter.setData(getListStatistic());
            isUpdating = false;
        }
        else isUpdating = false;

        prevBMonth = bMonth.getSelectedItemPosition();
        prevBYear = bYear.getSelectedItemPosition();
        prevAMonth = aMonth.getSelectedItemPosition();
        prevAYear = aYear.getSelectedItemPosition();
    }

    private void updateSpinnerYear(View view) {
        ArrayList<Integer> year = new ArrayList<>();
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);

        for(int i=2020;i<=currentYear;i++){
            year.add(i);
        }

        ArrayAdapter<Integer> adapterYear = new ArrayAdapter<>(view.getContext(), android.R.layout.simple_spinner_item,year);
        adapterYear.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bYear.setAdapter(adapterYear);
        bYear.setSelection(adapterYear.getPosition(currentYear));
        aYear.setAdapter(adapterYear);
        aYear.setSelection(adapterYear.getPosition(currentYear));

        setOnItemSelectedListener(bYear);
        setOnItemSelectedListener(aYear);
    }

    private List<Custom> getListStatistic() {
        List<Custom>list = new ArrayList<>();

        int startMonth = (Integer)bMonth.getSelectedItem();
        int startYear = (Integer)bYear.getSelectedItem();
        int endMonth = (Integer)aMonth.getSelectedItem();
        int endYear = (Integer)aYear.getSelectedItem();

        Log.i("hell",String.valueOf(startMonth));

        list.add(new Custom(startYear, startMonth, endYear, endMonth, 1, null));
        list.add(new Custom(startYear, startMonth, endYear, endMonth, 2, "Mood"));
        list.add(new Custom(startYear, startMonth, endYear, endMonth, 2, "Activity"));
        list.add(new Custom(startYear, startMonth, endYear, endMonth, 2, "Partner"));
        list.add(new Custom(startYear, startMonth, endYear, endMonth, 2, "Weather"));

        return list;
    }
}