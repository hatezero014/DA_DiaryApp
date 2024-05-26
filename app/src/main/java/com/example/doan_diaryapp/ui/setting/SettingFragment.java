package com.example.doan_diaryapp.ui.setting;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.provider.Settings;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.doan_diaryapp.AlarmReceiver;
import com.example.doan_diaryapp.ChangeLanguage;
import com.example.doan_diaryapp.ContactActivity;
import com.example.doan_diaryapp.Models.Language;
import com.example.doan_diaryapp.Models.Notification;
import com.example.doan_diaryapp.OpenPasscodeView;
import com.example.doan_diaryapp.R;
import com.example.doan_diaryapp.Service.LanguageService;
import com.example.doan_diaryapp.Service.NotificationService;
import com.google.android.material.divider.MaterialDivider;
import com.google.android.material.materialswitch.MaterialSwitch;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import java.util.Calendar;
import java.util.List;


public class SettingFragment extends Fragment {

    Dialog dialog;
    TextView textViewSubTheme, textViewSubLanguage, textViewNotificationAlarm;
    MaterialSwitch switchNotification, switchSecurity;
    LinearLayout layoutSecurity ,changePasswordButton, deletePasswordButton;
    private SharedPreferences sharedPreferences;
    private SharedPreferences sharedPreferences1;
    int selectedHour ;
    int selectedMinute;
    MaterialDivider div1, div2;

    private MaterialTimePicker materialTimePicker;
    private Calendar calendar;
    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;

    private LinearLayout layoutFeedback, layoutLanguage, layoutTheme, reminderTime;
    TextView tv_count_notification;

    private final ActivityResultLauncher<Intent> requestExactAlarmPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    if (alarmManager.canScheduleExactAlarms()) {
                        setAlarm();
                    } else {
                        Toast.makeText(getContext(), "Permission denied", Toast.LENGTH_SHORT).show();
                    }
                }
            });

    private void setAlarm() {
        Intent intent = new Intent(getContext(), AlarmReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(getContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        if (calendar != null) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            Toast.makeText(getContext(), "Alarm Set Successfully", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "Please select a time first", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting, container, false);

        createNotificationChannel();

        textViewSubTheme = view.findViewById(R.id.textviewSubTheme);
        textViewSubLanguage = view.findViewById(R.id.textviewSubLanguage);
        switchNotification = view.findViewById(R.id.switch_notification);
        textViewNotificationAlarm = view.findViewById(R.id.notification_alarm_clock);
        switchSecurity = view.findViewById(R.id.switch_security);
        layoutSecurity = view.findViewById(R.id.layout_Security);
        changePasswordButton = view.findViewById(R.id.change_PIN);
        deletePasswordButton = view.findViewById(R.id.delete_PIN);
        div1 = view.findViewById(R.id.div1);
        div2 = view.findViewById(R.id.div2);
        layoutFeedback = view.findViewById(R.id.layoutFeedback);
        layoutLanguage = view.findViewById(R.id.layoutLanguage);
        layoutTheme = view.findViewById(R.id.layoutTheme);
        tv_count_notification = view.findViewById(R.id.count_notification3);
        reminderTime = view.findViewById(R.id.remindertime);

        onLayoutThemeClick(layoutTheme);
        onLayoutLanguageClick(layoutLanguage);
        onLayoutFeedbackClick(layoutFeedback);

        sharedPreferences = requireContext().getSharedPreferences("Passcode", Context.MODE_PRIVATE);
        sharedPreferences1 = requireContext().getSharedPreferences("com.example.doan_diaryapp.NOTIFICATION_PREFS", Context.MODE_PRIVATE);

        layoutSecurity.setVisibility(View.VISIBLE);
        changePasswordButton.setVisibility(View.GONE);
        deletePasswordButton.setVisibility(View.GONE);
        div1.setVisibility(View.GONE);
        div2.setVisibility(View.GONE);

        if (sharedPreferences1.contains("hour") && sharedPreferences1.contains("minute")) {
            selectedHour = sharedPreferences1.getInt("hour", 0);
            selectedMinute = sharedPreferences1.getInt("minute", 0);
            handleSelectedTime(selectedHour, selectedMinute);
        }

        initLanguage();
        customDialog();
        setupSwitchNotification();
        setUpSwitchSecurity();
        CountNotification();
        reminderTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!switchNotification.isChecked()) return;
                openDiaLog();
            }
        });

        return view;
    }

    private void setupSwitchNotification() {
        switchNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isChecked = switchNotification.isChecked();
                SharedPreferences.Editor editor = sharedPreferences1.edit();

                if (isChecked) {
                    openDiaLog();
                    switchNotification.setChecked(true);
                    editor.putBoolean("notification_switch_state", true);
                } else {
                    textViewNotificationAlarm.setText(null);
                    switchNotification.setChecked(false);
                    editor.putBoolean("notification_switch_state", false);
                    cancelAlarm();
                    editor.remove("hour");
                    editor.remove("minute");
                }

                editor.apply();

            }
        });
    }

    private void cancelAlarm() {
        if (pendingIntent != null) {
            alarmManager.cancel(pendingIntent);
            pendingIntent.cancel();
            Toast.makeText(getContext(), "Alarm Canceled", Toast.LENGTH_SHORT).show();
        }
    }

    public void onLayoutThemeClick(LinearLayout layoutTheme)
    {
        layoutTheme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
            }
        });
    }

    public void onLayoutLanguageClick(LinearLayout layoutLanguage) {
        layoutLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ChangeLanguage.class);
                startActivity(intent);
                initLanguage();
            }
        });
    }

    void initLanguage() {
        LanguageService languageService = new LanguageService(getContext());
        Language language = languageService.FindById(Language.class, 1);
        if (language.getIsActive() == 1) {
            textViewSubLanguage.setText(getString(R.string.language_vi));
        }
        else {
            textViewSubLanguage.setText(getString(R.string.language_en));
        }
    }

    public void onLayoutFeedbackClick(LinearLayout layoutFeedback) {
        layoutFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ContactActivity.class);
                startActivity(intent);
            }
        });
    }

    public void customDialog() {
        dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.dialog_display_mode);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(getContext().getDrawable(R.drawable.custom_dialog_display_mode));
        dialog.setCancelable(true);
        Button btnCancel = dialog.findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        RadioButton rBtnLight = dialog.findViewById(R.id.rBtnLight);
        RadioButton rBtnDark = dialog.findViewById(R.id.rBtnDark);
        RadioButton rBtnSystem = dialog.findViewById(R.id.rBtnSystem);

        int displayMode = requireContext().getSharedPreferences("MODE", Context.MODE_PRIVATE).getInt("displayMode", 2);
        if (displayMode == 0) {
            rBtnLight.setChecked(true);
            textViewSubTheme.setText(getString(R.string.display_mode_light));
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
        else if (displayMode == 1){
            rBtnDark.setChecked(true);
            textViewSubTheme.setText(getString(R.string.display_mode_dark));
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
        else {
            rBtnSystem.setChecked(true);
            textViewSubTheme.setText(getString(R.string.display_mode_system));
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        }
        rBtnLight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                SharedPreferences.Editor editor = requireContext().getSharedPreferences("MODE", Context.MODE_PRIVATE).edit();
                editor.putInt("displayMode", 0);
                editor.apply();
                textViewSubTheme.setText(getString(R.string.display_mode_light));
                dialog.dismiss();
            }
        });

        rBtnDark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                SharedPreferences.Editor editor = requireContext().getSharedPreferences("MODE", Context.MODE_PRIVATE).edit();
                editor.putInt("displayMode", 1);
                editor.apply();
                textViewSubTheme.setText(getString(R.string.display_mode_dark));
                dialog.dismiss();
            }
        });

        rBtnSystem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                SharedPreferences.Editor editor = requireContext().getSharedPreferences("MODE", Context.MODE_PRIVATE).edit();
                editor.putInt("displayMode", 2);
                editor.apply();
                textViewSubTheme.setText(getString(R.string.display_mode_system));
                dialog.dismiss();
            }
        });
    }

    private void setUpSwitchSecurity()
    {
        switchSecurity.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    Intent intent = new Intent(getContext(), OpenPasscodeView.class);
                    intent.putExtra("action", "create");
                    startActivity(intent);
                    //finish();
                }
            }
        });
        changePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), OpenPasscodeView.class);
                intent.putExtra("action", "change");
                startActivity(intent);

            }
        });

        deletePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), OpenPasscodeView.class);
                intent.putExtra("action", "delete");
                startActivity(intent);

            }
        });
    }

    private boolean isPasscodeVerified = false;
    private boolean previousSwitchState = false;

    @Override
    public void onResume() {
        super.onResume();
        previousSwitchState = switchSecurity.isChecked();
        Bundle bundle = getArguments();
        boolean receivedBoolean = (Boolean) bundle.get("myBooleanKey");
        //boolean receivedBoolean = getActivity().getIntent().getBooleanExtra("myBooleanKey", true);
        String savedPasscode = sharedPreferences.getString("passcode", null);
        if (!isPasscodeVerified && receivedBoolean) {


            if (savedPasscode != null) {
                layoutSecurity.setVisibility(View.VISIBLE);
                changePasswordButton.setVisibility(View.GONE);
                deletePasswordButton.setVisibility(View.GONE);
                div1.setVisibility(View.GONE);
                div2.setVisibility(View.GONE);
                switchSecurity.setChecked(true);


                Intent intent = new Intent(getContext(), OpenPasscodeView.class);
                intent.putExtra("action", "verify");
                startActivity(intent);
            }
        }
        else {
            switchSecurity.setChecked(false);

            if(savedPasscode!=null) {
                layoutSecurity.setVisibility(View.GONE);
                changePasswordButton.setVisibility(View.VISIBLE);
                deletePasswordButton.setVisibility(View.VISIBLE);
                div1.setVisibility(View.GONE);
                div2.setVisibility(View.VISIBLE);
            }
            else {
                layoutSecurity.setVisibility(View.VISIBLE);
                changePasswordButton.setVisibility(View.GONE);
                deletePasswordButton.setVisibility(View.GONE);
                div1.setVisibility(View.GONE);
                div2.setVisibility(View.GONE);
            }

        }
        isPasscodeVerified = true;
        boolean isNotificationSwitchChecked = sharedPreferences1.getBoolean("notification_switch_state", false);
        switchNotification.setChecked(isNotificationSwitchChecked);
        CountNotification();
    }

    private void openDiaLog()
    {
        calendar = Calendar.getInstance();
        int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
        int currentMinute = calendar.get(Calendar.MINUTE);

        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        materialTimePicker = new MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .setHour(currentHour)
                .setMinute(currentMinute)
                .setTitleText(R.string.select_reminder_time)
                .setInputMode(MaterialTimePicker.INPUT_MODE_CLOCK)
                .build();
        materialTimePicker.addOnPositiveButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int hour = materialTimePicker.getHour();
                int minute = materialTimePicker.getMinute();
                SharedPreferences.Editor editor = sharedPreferences1.edit();
                editor.putInt("hour", hour);
                editor.putInt("minute", minute);
                editor.apply();
                calendar.set(Calendar.HOUR_OF_DAY, hour);
                calendar.set(Calendar.MINUTE, minute);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);

                String period = (hour >= 12) ? "PM" : "AM";
                if (hour > 12) hour -= 12;
                textViewNotificationAlarm.setText(String.format("%02d:%02d %s", hour, minute, period));
                checkAndRequestExactAlarmPermission();
            }

        });
        materialTimePicker.addOnNegativeButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sharedPreferences1.edit();
                editor.putBoolean("notification_switch_state", false);
                editor.apply();
                switchNotification.setChecked(false);
            }
        });
        materialTimePicker.addOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                SharedPreferences.Editor editor = sharedPreferences1.edit();
                editor.putBoolean("notification_switch_state", false);
                editor.apply();
                switchNotification.setChecked(false);
            }
        });

        materialTimePicker.show(fragmentManager, getString(R.string.id_AlarmReceiver));

    }

    private void checkAndRequestExactAlarmPermission() {
        alarmManager = (AlarmManager) requireContext().getSystemService(Context.ALARM_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (alarmManager.canScheduleExactAlarms()) {
                setAlarm();
            } else {
                Intent intent = new Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM);
                requestExactAlarmPermissionLauncher.launch(intent);
            }
        } else {
            setAlarm();
        }
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.alarm_clock);
            String description = getString(R.string.alarm_clock_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(getString(R.string.id_AlarmReceiver), name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = requireContext().getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void handleSelectedTime(int hour, int minute) {
        String selectedTime = String.format("%02d:%02d", hour, minute);
        textViewNotificationAlarm.setText(selectedTime);
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