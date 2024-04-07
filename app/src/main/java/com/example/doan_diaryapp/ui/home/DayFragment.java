package com.example.doan_diaryapp.ui.home;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.doan_diaryapp.R;
import com.example.doan_diaryapp.databinding.FragmentHomeBinding;
import com.example.doan_diaryapp.databinding.FragmentDayBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class DayFragment extends Fragment {

    private FragmentDayBinding binding;


    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_day, container, false);

        return view;

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}