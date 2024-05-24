package com.example.doan_diaryapp.ui.setting;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SettingViewModel extends ViewModel {
    private MutableLiveData<String> mText;

    public SettingViewModel() {
        this.mText = new MutableLiveData<>();
        mText.setValue("This is setting fragment");
    }

    public SettingViewModel(MutableLiveData<String> mText) {
        this.mText = mText;
    }

    public LiveData<String> getText(){
        return mText;
    }
}
