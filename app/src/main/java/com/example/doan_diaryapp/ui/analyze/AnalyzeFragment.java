package com.example.doan_diaryapp.ui.analyze;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.doan_diaryapp.databinding.FragmentAnalyzeBinding;

public class AnalyzeFragment extends Fragment {

    private FragmentAnalyzeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        AnalyzeViewModel notificationsViewModel =
                new ViewModelProvider(this).get(AnalyzeViewModel.class);

        binding = FragmentAnalyzeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}