package com.example.doan_diaryapp.ui.home;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.doan_diaryapp.R;
import com.example.doan_diaryapp.databinding.FragmentHomeBinding;
import com.example.doan_diaryapp.databinding.FragmentMonthBinding;


public class MonthFragment extends Fragment {

    private FragmentMonthBinding binding;
    private FragmentHomeBinding bindingHome;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = FragmentMonthBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}