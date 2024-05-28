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
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.doan_diaryapp.Adapter.CustomAdapter;
import com.example.doan_diaryapp.Models.Custom;
import com.example.doan_diaryapp.Models.Entry;
import com.example.doan_diaryapp.R;
import com.example.doan_diaryapp.Service.EntryService;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class CustomFragment extends Fragment {
    private RecyclerView recyclerView;
    private CustomAdapter customAdapter;
    private AutoCompleteTextView bMonth;
    private AutoCompleteTextView bYear;
    private AutoCompleteTextView aMonth;
    private AutoCompleteTextView aYear;
    private Button btn_search;
    private TextView tv_statistic_custom;
    private EntryService entryService;
    private int prevMonth, prevYear, afMonth, afYear;
    private int index = 1;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_custom, container, false);

        recyclerView = view.findViewById(R.id.rcv_thong_ke);
        bMonth = view.findViewById(R.id.act_bmonth);
        bYear = view.findViewById(R.id.act_byear);
        aMonth = view.findViewById(R.id.act_amonth);
        aYear = view.findViewById(R.id.act_ayear);
        btn_search = view.findViewById(R.id.btn_search);
        tv_statistic_custom = view.findViewById(R.id.tv_statistic_custom);

        prevMonth = Calendar.getInstance().get(Calendar.MONTH) + 1;
        prevYear = Calendar.getInstance().get(Calendar.YEAR);
        afMonth = Calendar.getInstance().get(Calendar.MONTH) + 1;
        afYear = Calendar.getInstance().get(Calendar.YEAR);

        updateSpinnerYear(view, aYear, aMonth);
        updateSpinnerYear(view, bYear, bMonth);
        updateSpinnerMonth(view, aYear, aMonth);
        updateSpinnerMonth(view, bYear, bMonth);

        setOnClickListener(btn_search);

        entryService = new EntryService(getContext());

        customAdapter = new CustomAdapter(view.getContext());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        customAdapter.setData(getListStatistic());
        recyclerView.setAdapter(customAdapter);

        return view;
    }

    public void setOnClickListener(Button button)
    {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkValidity()){
                    recyclerView.setVisibility(View.VISIBLE);
                    tv_statistic_custom.setVisibility(View.GONE);
                    customAdapter.setData(getListStatistic());
                }
                else
                {
                    customAdapter.clearData();
                    recyclerView.setVisibility(View.GONE);
                    tv_statistic_custom.setVisibility(View.VISIBLE);
                    tv_statistic_custom.setText(R.string.over_date);
                }
            }
        });
    }

    private void updateSpinnerMonth(View container, AutoCompleteTextView year, AutoCompleteTextView month) {
        ArrayList<Integer> arrayMonth;

        String sYear = year.getText().toString();
        int iYear = Integer.parseInt(sYear);

        int curYear = Calendar.getInstance().get(Calendar.YEAR);
        int curMonth = Calendar.getInstance().get(Calendar.MONTH) + 1;

        if (month == aMonth) {
            if (iYear == curYear) {
                arrayMonth = new ArrayList<>();
                for (int i = 1; i <= curMonth; i++) {
                    arrayMonth.add(i);
                }
            } else {
                arrayMonth = new ArrayList<>();
                for (int i = 1; i <= 12; i++) {
                    arrayMonth.add(i);
                }
            }
        } else {
            if (iYear == afYear) {
                arrayMonth = new ArrayList<>();
                for (int i = 1; i <= afMonth; i++) {
                    arrayMonth.add(i);
                }
            } else {
                arrayMonth = new ArrayList<>();
                for (int i = 1; i <= 12; i++) {
                    arrayMonth.add(i);
                }
            }
        }

        ArrayAdapter<Integer> arrayAdapter = new ArrayAdapter<>(container.getContext(), R.layout.item_drop_down, arrayMonth);
        month.setAdapter(arrayAdapter);
        if(index==1){
            month.setText(String.valueOf(arrayMonth.get(arrayMonth.size() - 1)), false);
        }
        else{
            if((iYear == curYear || bYear.getText().toString().equals(aYear.getText().toString()))){
                if(Integer.parseInt(bMonth.getText().toString()) > Integer.parseInt(aMonth.getText().toString())){
                    bMonth.setText(aMonth.getText().toString(), false);
                }
            }
            else if(iYear == curYear && year == aYear && Integer.parseInt(aMonth.getText().toString()) > curMonth){
                aMonth.setText(String.valueOf(curMonth), false);
            }
        }

        month.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int selectedMonth = arrayMonth.get(position);
                if (month == aMonth) {
                    afMonth = selectedMonth;
                    index = 2;

                    // Cập nhật lại bMonth
                    updateSpinnerMonth(container, bYear, bMonth);

                    // Logic điều chỉnh giá trị tháng bắt đầu và tháng kết thúc
                    String sBMonth = bMonth.getText().toString();
                    int bMonthValue = Integer.parseInt(sBMonth);

                    if (Integer.parseInt(bYear.getText().toString()) == afYear && afMonth < bMonthValue) {
                        bMonth.setText(String.valueOf(afMonth), false);
                    }
                } else if (month == bMonth) {
                    index = 2;
                    bMonth.setText(String.valueOf(selectedMonth), false);
                }
            }
        });
    }

    private void updateSpinnerYear(View container, AutoCompleteTextView year, AutoCompleteTextView month) {
        ArrayList<Integer> arrayYear;
        int curYear = Calendar.getInstance().get(Calendar.YEAR);
        if(year == aYear){
            arrayYear = new ArrayList<>();
            for(int i = 2020; i <= curYear; i++){
                arrayYear.add(i);
            }
        }
        else {
            arrayYear = new ArrayList<>();
            for(int i = 2020; i <= afYear; i++){
                arrayYear.add(i);
            }
        }

        ArrayAdapter<Integer> arrayAdapter = new ArrayAdapter<>(container.getContext(),R.layout.item_drop_down, arrayYear);
        year.setAdapter(arrayAdapter);
        if(index == 1){
            year.setText(String.valueOf(arrayYear.get(arrayYear.size() - 1)), false);
        }

        year.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int selectedYear = arrayYear.get(position);
                if(year == aYear){
                    String sAYear = aYear.getText().toString();
                    afYear = Integer.parseInt(sAYear);
                    index = 2;
                    updateSpinnerYear(container, bYear, bMonth);

                    String selectedbYear = bYear.getText().toString();
                    int startYear = Integer.parseInt(selectedbYear);
                    String selectedaYear = aYear.getText().toString();
                    int endYear = Integer.parseInt(selectedaYear);

                    if(startYear > endYear)
                        bYear.setText(selectedaYear,false);

                    updateSpinnerMonth(container, aYear, aMonth);
                    updateSpinnerMonth(container, bYear, bMonth);
                }
                else {
                    index = 2;
                    updateSpinnerMonth(container, bYear, bMonth);
                }
            }
        });
    }

    private boolean checkValidity() {
        String selectedbYear = bYear.getText().toString();
        int byear = Integer.parseInt(selectedbYear);
        String selectedbMonth = bMonth.getText().toString();
        int bmonth = Integer.parseInt(selectedbMonth);
        String selectedaYear = aYear.getText().toString();
        int ayear = Integer.parseInt(selectedaYear);
        String selectedaMonth = aMonth.getText().toString();
        int amonth = Integer.parseInt(selectedaMonth);

        // Kiểm tra xem tháng và năm bắt đầu có lớn hơn tháng và năm kết thúc không
        if (byear > ayear || (byear == ayear && bmonth > amonth)) {
            Toast.makeText(getContext(), getString(R.string.noti_custom_date), Toast.LENGTH_SHORT).show();
            // Đặt lại giá trị bắt đầu thành giá trị kết thúc
            return false;
        }

        // Tính số tháng giữa các giá trị bắt đầu và kết thúc
        int totalMonths = (ayear - byear) * 12 + (amonth - bmonth);

        // Kiểm tra nếu số tháng vượt quá 12
        if (totalMonths > 12) {
            Toast.makeText(getContext(), getString(R.string.noti_custom_distance), Toast.LENGTH_SHORT).show();

            return false;
        }

        return true;
    }

    private List<Custom> getListStatistic() {
        List<Custom>list = new ArrayList<>();

        String selectedbYear = bYear.getText().toString();
        int startYear = Integer.parseInt(selectedbYear);
        String selectedbMonth = bMonth.getText().toString();
        int startMonth = Integer.parseInt(selectedbMonth);
        String selectedaYear = aYear.getText().toString();
        int endYear = Integer.parseInt(selectedaYear);
        String selectedaMonth = aMonth.getText().toString();
        int endMonth = Integer.parseInt(selectedaMonth);


        list.add(new Custom(startYear, startMonth, endYear, endMonth, 1, null));
        list.add(new Custom(startYear, startMonth, endYear, endMonth, 2, "Mood"));
        list.add(new Custom(startYear, startMonth, endYear, endMonth, 2, "Activity"));
        list.add(new Custom(startYear, startMonth, endYear, endMonth, 2, "Partner"));
        list.add(new Custom(startYear, startMonth, endYear, endMonth, 2, "Weather"));

        return list;
    }

    @Override
    public void onResume() {
        super.onResume();
        Init();
    }

    public void Init(){
        View view = getView();
        setOnClickListener(btn_search);

        updateSpinnerYear(view, aYear, aMonth);
        updateSpinnerYear(view, bYear, bMonth);
        updateSpinnerMonth(view, aYear, aMonth);
        updateSpinnerMonth(view, bYear, bMonth);

        customAdapter = new CustomAdapter(view.getContext());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        customAdapter.setData(getListStatistic());
        recyclerView.setAdapter(customAdapter);

        customAdapter.notifyDataSetChanged();

    }
}