package com.example.doan_diaryapp.ui.collection;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doan_diaryapp.Adapter.EntryAdapter;
import com.example.doan_diaryapp.Models.Entry;
import com.example.doan_diaryapp.Models.Notification;
import com.example.doan_diaryapp.R;
import com.example.doan_diaryapp.RecordActivity;
import com.example.doan_diaryapp.Service.EntryPhotoService;
import com.example.doan_diaryapp.Service.EntryService;
import com.example.doan_diaryapp.Service.ImportantEntryService;
import com.example.doan_diaryapp.Service.NotificationService;
import com.example.doan_diaryapp.YourImagesInApp;
import com.example.doan_diaryapp.databinding.FragmentCollectionBinding;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;
import java.util.List;

public class CollectionFragment extends Fragment {
    private FragmentCollectionBinding binding;
    private CarouselAdapter carouselAdapter;
    private ListView mListView;

    private Button mButton;
    private EntryAdapter mAdapter;

    private EntryAdapter mAdapter1;
    private ImportantEntryService importantEntryService;

    private EntryPhotoService entryPhotoService;

    EntryService mEntryService;
    TextView tv_count_notification;



    private void updateView() {
        View view = binding.getRoot();
        ListViewDayQT(view);
        Button(view);
        updateListView(view);
        RecyclerView recyclerView = view.findViewById(R.id.carousel_recycler_view);
        ViewGroup.LayoutParams layoutParams = recyclerView.getLayoutParams();
        layoutParams.height = (int) getResources().getDimension(R.dimen.recyclerview_height);
        recyclerView.setLayoutParams(layoutParams);
        entryPhotoService = new EntryPhotoService(getContext());
        ArrayList<CarouselModel> list = entryPhotoService.getPhotoFromDatabase();
        carouselAdapter = new CarouselAdapter(list, requireContext());
        recyclerView.setAdapter(carouselAdapter);
        TextView textView1 = view.findViewById(R.id.text2);
        if (list.size() == 0) {
            textView1.setVisibility(View.VISIBLE);
        } else {
            textView1.setVisibility(View.GONE);
        }
        CountNotification();

    }


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentCollectionBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        ListViewDayQT(view);
        Button(view);

        RecyclerView recyclerView = view.findViewById(R.id.carousel_recycler_view);
        ViewGroup.LayoutParams layoutParams = recyclerView.getLayoutParams();
        layoutParams.height = (int) getResources().getDimension(R.dimen.recyclerview_height);
        recyclerView.setLayoutParams(layoutParams);
        entryPhotoService = new EntryPhotoService(getContext());
        ArrayList<CarouselModel> list = entryPhotoService.getPhotoFromDatabase();
        carouselAdapter = new CarouselAdapter(list, requireContext());
        recyclerView.setAdapter(carouselAdapter);

        TextView textView1 = view.findViewById(R.id.text2);
        if (list.size() == 0) {
            textView1.setVisibility(View.VISIBLE);
        } else {
            textView1.setVisibility(View.GONE);
        }


        tv_count_notification = view.findViewById(R.id.count_notification);
        mListView = view.findViewById(R.id.ListDayQT);
        importantEntryService = new ImportantEntryService(getContext());
        List<Entry> entryList = importantEntryService.getEntriesFromDatabaseQT();
        mAdapter = new EntryAdapter(getContext(), entryList);
        mListView.setAdapter(mAdapter);

        TextView textView = view.findViewById(R.id.text3);
        if (entryList.size() == 0) {
            textView.setVisibility(View.VISIBLE);
        } else {
            textView.setVisibility(View.GONE);
        }

        mAdapter.setOnFavoriteClickListener(new EntryAdapter.OnFavoriteClickListener() {
            @Override
            public void onFavoriteClick(Entry entry) {
                updateListView(view);
            }
        });
        CountNotification();

        return view;
    }

    private void updateListView(View view) {
        List<Entry> entryList = importantEntryService.getEntriesFromDatabaseQT();
        mAdapter.updateEntries1(entryList);
        TextView textView = view.findViewById(R.id.text3);
        if (entryList.size() == 0) {
            textView.setVisibility(View.VISIBLE);
        } else {
            textView.setVisibility(View.GONE);
        }
    }

    private void Button(View view)
    {
        mButton=view.findViewById(R.id.button);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), YourImagesInApp.class);
                startActivity(intent);
            }
        });
    }


    private void ListViewDayQT(View view)
    {
        mListView = view.findViewById(R.id.ListDayQT);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView textViewDate = view.findViewById(R.id.textViewID);
                if (textViewDate.length()!=0) {
                    String dateText = textViewDate.getText().toString();
                    Intent intent = new Intent(getActivity(), RecordActivity.class);
                    intent.putExtra("Date", dateText);
                    startActivity(intent);
                }
            }
        });

        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                TextView textViewDate = view.findViewById(R.id.textViewID);
                if (textViewDate.length() != 0) {
                    showAlertDialog(textViewDate.getText().toString(),view);
                    return true;
                }
                return false;
            }
        });

    }


    private void showAlertDialog(String date,View view) {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(requireContext());
        builder.setTitle(R.string.delete_diary)
                .setMessage(R.string.delete)
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mEntryService = new EntryService(getContext());
                        mEntryService.deleteDiary(date,getContext());
                        updateView();
                    }
                });
        builder.create().show();
    }

    @Override
    public void onResume() {
        super.onResume();
        updateView();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void CountNotification(){
        List<Notification> list = getListNotification();
        int countNotification = list.size();
        int length = String.valueOf(countNotification).length();
        if(countNotification==0 || list == null){
            tv_count_notification.setVisibility(View.GONE);
        }
        else if(length == 1){
            tv_count_notification.setPadding(20,3,20,3);
            tv_count_notification.setVisibility(View.VISIBLE);
            tv_count_notification.setText(String.valueOf(countNotification));
        }
        else if(length == 2){
            tv_count_notification.setPadding(13,3,13,3);
            tv_count_notification.setVisibility(View.VISIBLE);
            tv_count_notification.setText(String.valueOf(countNotification));
        }
        else if(length > 2){
            tv_count_notification.setPadding(6,3,6,3);
            tv_count_notification.setVisibility(View.VISIBLE);
            tv_count_notification.setText("99+");
        }

    }

    public List<Notification> getListNotification(){
        NotificationService notificationService = new NotificationService(getContext());
        List<Notification> list = notificationService.GetcountNotificationisnotRead(Notification.class);
        return list;

    }


}