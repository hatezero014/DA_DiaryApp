package com.example.doan_diaryapp.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.example.doan_diaryapp.R;
import com.example.doan_diaryapp.databinding.FragmentHomeBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import android.widget.Toast;


public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        Fragment fragmentDay = new DayFragment();
        Fragment fragmentMonth = new MonthFragment();

        MaterialButtonToggleGroup DayMonth = view.findViewById(R.id.toggleGroup);
        DayMonth.addOnButtonCheckedListener(new MaterialButtonToggleGroup.OnButtonCheckedListener() {
            @Override
            public void onButtonChecked(MaterialButtonToggleGroup group, int checkedId, boolean isChecked) {
                if (isChecked) {
                    if (checkedId == R.id.ButtonDay) {
                        requireActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.dayandmonth, fragmentDay).commit();
                    } else {
                        requireActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.dayandmonth, fragmentMonth).commit();
                    }
                }
            }
        });


        Button buttonFragmentDay = view.findViewById(R.id.ButtonDay);
        buttonFragmentDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DayMonth.check(R.id.ButtonDay);
                requireActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.dayandmonth, fragmentDay).commit();
            }
        });

        Button buttonFragmentMonth = view.findViewById(R.id.ButtonMonth);
        buttonFragmentMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DayMonth.check(R.id.ButtonMonth);
                requireActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.dayandmonth, fragmentMonth).commit();
            }
        });

        return view;

    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


}


