package com.example.doan_diaryapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

public class ChangeReminderTime extends BaseActivity {
    private TextView textView;

    private Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_change_reminder_time);

        textView = findViewById(R.id.timeisSet);
        button = findViewById(R.id.btnShowDiaLog);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDiaLog();
            }
        });
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void openDiaLog()
    {
        // Material Design Timepicker
        FragmentManager fragmentManager = getSupportFragmentManager();
        MaterialTimePicker picker = new MaterialTimePicker.Builder()
                        .setTimeFormat(TimeFormat.CLOCK_24H)
                        .setHour(9)
                        .setMinute(45)
                        .setTitleText("Select Appointment time")
                        .setInputMode(MaterialTimePicker.INPUT_MODE_CLOCK)
                        .build();
        picker.show(fragmentManager, "tag");
        /*   TimePickerDialog dialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                textView.setText(String.valueOf(hourOfDay) +"."+ String.valueOf(minute));
            }
        }, 15, 0, true);
        dialog.show();  */
    }
}