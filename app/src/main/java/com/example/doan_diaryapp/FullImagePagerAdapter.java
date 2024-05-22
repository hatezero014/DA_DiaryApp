// FullImagePagerAdapter.java
package com.example.doan_diaryapp;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;

public class FullImagePagerAdapter extends FragmentStateAdapter {

    private ArrayList<String> imagePaths;

    public FullImagePagerAdapter(@NonNull FragmentActivity fragmentActivity, ArrayList<String> imagePaths) {
        super(fragmentActivity);
        this.imagePaths = imagePaths;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return FullImageFragment.newInstance(imagePaths.get(position));
    }

    @Override
    public int getItemCount() {
        return imagePaths.size();
    }
}
