package com.example.doan_diaryapp.ui.notification;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doan_diaryapp.Adapter.CategoryAdapter;
import com.example.doan_diaryapp.Models.Category;
import com.example.doan_diaryapp.Models.DayDistinct;
import com.example.doan_diaryapp.Models.Notification;
import com.example.doan_diaryapp.R;
import com.example.doan_diaryapp.Service.NotificationService;
import java.util.ArrayList;
import java.util.List;

public class NotificationFragment extends Fragment {

    private RecyclerView recyclerView;
    private CategoryAdapter categoryAdapter;
    private TextView textView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_notification, container, false);

        // Initialize views
        recyclerView = view.findViewById(R.id.recyclerView);
        textView = view.findViewById(R.id.tv_content_null);
        categoryAdapter = new CategoryAdapter(requireContext()); // Use requireContext()

        // Set layout manager
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);

        // Get data and set adapter
        categoryAdapter.setData(getListCategory());
        recyclerView.setAdapter(categoryAdapter);

        // Handle system bar insets (if needed)
        // Consider using a dedicated method for handling insets in your fragment

        return view;
    }

    private List<Category> getListCategory() {
        NotificationService notificationService = new NotificationService(requireContext()); // Use requireContext()
        List<Category> categoryList = new ArrayList<>();
        List<DayDistinct> days = notificationService.DayDistinct(DayDistinct.class);
        if (days.size() == 0) {
            textView.setVisibility(View.VISIBLE);
        }
        for (DayDistinct day : days) {
            String temp = day.getDay();
            String whereClause = "Day = ?";
            String[] whereArgs = new String[]{temp};
            ArrayList<Notification> notifications = notificationService.GetAllOrderByDESC(Notification.class, "Id desc", whereClause, whereArgs);
            categoryList.add(new Category(temp, notifications));
        }
        return categoryList;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
