package com.example.doan_diaryapp.ui.collection;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doan_diaryapp.Adapter.EntryAdapter;
import com.example.doan_diaryapp.Models.Entry;
import com.example.doan_diaryapp.R;
import com.example.doan_diaryapp.RecordActivity;
import com.example.doan_diaryapp.Service.EntryPhotoService;
import com.example.doan_diaryapp.Service.EntryService;
import com.example.doan_diaryapp.Service.ImportantDayService;
import com.example.doan_diaryapp.databinding.FragmentCollectionBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CollectionFragment extends Fragment {
    private FragmentCollectionBinding binding;
    private CarouselAdapter carouselAdapter;
    private ListView mListView;
    private EntryAdapter mAdapter;
    private ImportantDayService importantDayService;

    private EntryPhotoService entryPhotoService;

    private void updateEntries() {
        List<Entry> entryList = importantDayService.getEntriesFromDatabaseQT();
        mAdapter.clear();
        mAdapter.addAll(entryList);
        mAdapter.notifyDataSetChanged();
    }

    private void updateView() {
        View rootView = getView();
        if (rootView != null) {
            RecyclerView recyclerView = rootView.findViewById(R.id.carousel_recycler_view);
            ViewGroup.LayoutParams layoutParams = recyclerView.getLayoutParams();
            layoutParams.height = (int) getResources().getDimension(R.dimen.recyclerview_height);
            recyclerView.setLayoutParams(layoutParams);
            entryPhotoService = new EntryPhotoService(getContext());
            ArrayList<CarouselModel> list = entryPhotoService.getPhotoFromDatabase();
            carouselAdapter = new CarouselAdapter(list, requireContext());
            recyclerView.setAdapter(carouselAdapter);

        }
    }


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentCollectionBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        ListViewDayQT(view);
        mListView = view.findViewById(R.id.ListDayQT);
        importantDayService = new ImportantDayService(getContext());
        List<Entry> entryList = importantDayService.getEntriesFromDatabaseQT();
        mAdapter = new EntryAdapter(getContext(), entryList);
        mListView.setAdapter(mAdapter);

        RecyclerView recyclerView = view.findViewById(R.id.carousel_recycler_view);
        ViewGroup.LayoutParams layoutParams = recyclerView.getLayoutParams();
        layoutParams.height = (int) getResources().getDimension(R.dimen.recyclerview_height);
        recyclerView.setLayoutParams(layoutParams);
        entryPhotoService = new EntryPhotoService(getContext());
        ArrayList<CarouselModel> list = entryPhotoService.getPhotoFromDatabase();
        carouselAdapter = new CarouselAdapter(list, requireContext());
        recyclerView.setAdapter(carouselAdapter);

        return view;
    }






    private void ListViewDayQT(View view)
    {
        mListView = view.findViewById(R.id.ListDayQT);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView textViewDate = view.findViewById(R.id.textViewDate);
                String dateText = textViewDate.getText().toString();
                String[] parts = dateText.split(", ");
                String formattedDate = parts[1].trim();
                String[] dateParts = formattedDate.split("-");
                int day = Integer.parseInt(dateParts[0]);
                int month = Integer.parseInt(dateParts[1]);
                int year = Integer.parseInt(dateParts[2]);
                Intent intent = new Intent(getActivity(), RecordActivity.class);
                intent.putExtra("Date", String.format(Locale.ENGLISH,
                        "%02d-%02d-%04d", day, month, year));
                startActivity(intent);
            }
        });

    }


    @Override
    public void onResume() {
        super.onResume();
        updateEntries();
        updateView();
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}