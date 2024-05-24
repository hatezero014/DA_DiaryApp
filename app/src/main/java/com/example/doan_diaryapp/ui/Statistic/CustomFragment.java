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

        setOnClickListener(btn_search);

        updateSpinnerYear(view);
        updateSpinnerMonth(view);

        entryService = new EntryService(getContext());

        customAdapter = new CustomAdapter(view.getContext());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        customAdapter.setData(getListStatistic());
        recyclerView.setAdapter(customAdapter);

        String selectedbYear = bYear.getText().toString();
        int startYear = Integer.parseInt(selectedbYear);
        String selectedbMonth = bMonth.getText().toString();
        int startMonth = Integer.parseInt(selectedbMonth);
        String selectedaYear = aYear.getText().toString();
        int endYear = Integer.parseInt(selectedaYear);
        String selectedaMonth = aMonth.getText().toString();
        int endMonth = Integer.parseInt(selectedaMonth);

        List<Entry> entryList = entryService.getOverallScoreCustom(startYear,startMonth,endYear,endMonth);
        if(entryList.isEmpty()){
            recyclerView.setVisibility(View.GONE);
            tv_statistic_custom.setVisibility(View.VISIBLE);
            tv_statistic_custom.setText(R.string.no_data_custom);
        }
        else{
            recyclerView.setVisibility(View.VISIBLE);
            tv_statistic_custom.setVisibility(View.GONE);
        }

        return view;
    }

    private void updateSpinnerMonth(View view) {
        ArrayList<Integer> month = new ArrayList<>();
        for(int i=1;i<=12;i++){
            month.add(i);
        }

        ArrayAdapter<Integer> adapterMonth = new ArrayAdapter<>(view.getContext(), R.layout.item_drop_down, month);
        bMonth.setAdapter(adapterMonth);
        bMonth.setText(String.valueOf(Calendar.getInstance().get(Calendar.MONTH)+1),false);
        aMonth.setAdapter(adapterMonth);
        aMonth.setText(String.valueOf(Calendar.getInstance().get(Calendar.MONTH)+1),false);
    }

    public void setOnClickListener(Button button)
    {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkValidity()){
                    if(checkData()){
                        recyclerView.setVisibility(View.VISIBLE);
                        tv_statistic_custom.setVisibility(View.GONE);
                        customAdapter.setData(getListStatistic());
                    }
                    else{
                        return;
                    }
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

        boolean isFeature_b = isFutureDate(byear, bmonth);
        boolean isFeature_a = isFutureDate(ayear, amonth);

        if(isFeature_a || isFeature_b){
            Toast.makeText(getContext(),getString(R.string.noti_month_selected),Toast.LENGTH_SHORT).show();

            return false;
        }

        return true;
    }

    public boolean isFutureDate(int year, int month) {
        // Get the current date
        Calendar currentDate = Calendar.getInstance();

        // Create a calendar object for the given date
        Calendar givenDate = Calendar.getInstance();
        givenDate.set(Calendar.YEAR, year);
        givenDate.set(Calendar.MONTH, month - 1); // Calendar.MONTH is zero-based (0 = January)

        // Compare the dates
        return givenDate.after(currentDate);
    }

    private void updateSpinnerYear(View view) {
        ArrayList<Integer> year = new ArrayList<>();
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);

        for(int i=2020;i<=currentYear;i++){
            year.add(i);
        }

        ArrayAdapter<Integer> adapterYear = new ArrayAdapter<>(view.getContext(), R.layout.item_drop_down,year);
        bYear.setAdapter(adapterYear);
        aYear.setAdapter(adapterYear);

        setDefaultYear(adapterYear);
    }

    private void setDefaultYear(ArrayAdapter<Integer> adapterYear) {
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        boolean yearExists = false;
        for (int i = 0; i < adapterYear.getCount(); i++) {
            if (adapterYear.getItem(i) == currentYear) {
                yearExists = true;
                break;
            }
        }
        if (!yearExists) {
            adapterYear.add(currentYear);
            adapterYear.notifyDataSetChanged();
        }
        aYear.setText(String.valueOf(currentYear),false);
        bYear.setText(String.valueOf(currentYear),false);
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

        updateSpinnerYear(view);
        updateSpinnerMonth(view);

        customAdapter = new CustomAdapter(view.getContext());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        customAdapter.setData(getListStatistic());
        recyclerView.setAdapter(customAdapter);

        customAdapter.notifyDataSetChanged();

        boolean check = checkData();
    }

    public boolean checkData(){
        String selectedbYear = bYear.getText().toString();
        int startYear = Integer.parseInt(selectedbYear);
        String selectedbMonth = bMonth.getText().toString();
        int startMonth = Integer.parseInt(selectedbMonth);
        String selectedaYear = aYear.getText().toString();
        int endYear = Integer.parseInt(selectedaYear);
        String selectedaMonth = aMonth.getText().toString();
        int endMonth = Integer.parseInt(selectedaMonth);

        List<Entry> entryList = entryService.getOverallScoreCustom(startYear,startMonth,endYear,endMonth);
        if(entryList.isEmpty()){
            recyclerView.setVisibility(View.GONE);
            tv_statistic_custom.setVisibility(View.VISIBLE);
            tv_statistic_custom.setText(R.string.no_data_custom);
            return false;
        }
        else{
            recyclerView.setVisibility(View.VISIBLE);
            tv_statistic_custom.setVisibility(View.GONE);
            return true;
        }
    }
}