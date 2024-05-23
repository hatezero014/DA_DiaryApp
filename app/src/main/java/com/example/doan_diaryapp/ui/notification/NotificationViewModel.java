package com.example.doan_diaryapp.ui.notification;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class NotificationViewModel extends ViewModel {
    private final MutableLiveData<String> mText;

    public NotificationViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is notification fragment");
    }

    public NotificationViewModel(MutableLiveData<String> mText) {
        this.mText = mText;
    }

    public LiveData<String> getText() {
        return mText;
    }
}
