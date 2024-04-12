package com.example.doan_diaryapp.ui.collection;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doan_diaryapp.R;
import com.example.doan_diaryapp.databinding.FragmentCollectionBinding;

import java.util.ArrayList;
import java.util.List;

public class CollectionFragment extends Fragment {
    private FragmentCollectionBinding binding;
    private ArrayList<CarouselModel> list = new ArrayList<>();
    private CarouselAdapter carouselAdapter;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentCollectionBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        RecyclerView recyclerView = view.findViewById(R.id.carousel_recycler_view);
        ViewGroup.LayoutParams layoutParams = recyclerView.getLayoutParams();
        layoutParams.height = (int) getResources().getDimension(R.dimen.recyclerview_height);
        recyclerView.setLayoutParams(layoutParams);

        carouselAdapter = new CarouselAdapter(list, requireContext());
        recyclerView.setAdapter(carouselAdapter);

        list.add(new CarouselModel(R.drawable.i1));
        list.add(new CarouselModel(R.drawable.i2));
        list.add(new CarouselModel(R.drawable.i3));
        list.add(new CarouselModel(R.drawable.i4));
        list.add(new CarouselModel(R.drawable.i5));
        list.add(new CarouselModel(R.drawable.i1));
        list.add(new CarouselModel(R.drawable.i2));
        list.add(new CarouselModel(R.drawable.i3));
        list.add(new CarouselModel(R.drawable.i4));
        list.add(new CarouselModel(R.drawable.i5));


        ListView listView = view.findViewById(R.id.ListDayQT);
        listView.setAdapter(new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_2, android.R.id.text1, generateItemList()));


        return view;
    }


    private List<String> generateItemList() {
        List<String> itemList = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            itemList.add("Ngày quan trọng " + (i + 1) + "\nSự kiện xảy ra " + (i + 1));
        }
        return itemList;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}