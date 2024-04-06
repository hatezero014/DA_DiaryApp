package com.example.doan_diaryapp.ui.Statistic;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.doan_diaryapp.R;

import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ByMonthFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ByMonthFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;



    public ByMonthFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ByMonthFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ByMonthFragment newInstance(String param1, String param2) {
        ByMonthFragment fragment = new ByMonthFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        LayoutInflater lf = getActivity().getLayoutInflater();
        View view =  lf.inflate(R.layout.fragment_by_month, container, false); //pass the correct layout name for the fragment

        TextView text = (TextView) view.findViewById(R.id.yearCurrent);
        text.setText("Just another sample");
        return inflater.inflate(R.layout.fragment_by_month,container,false);
    }

}