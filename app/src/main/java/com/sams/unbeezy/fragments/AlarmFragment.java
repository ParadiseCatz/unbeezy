package com.sams.unbeezy.fragments;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.google.gson.Gson;
import com.sams.unbeezy.AddAlarmActivity;
import com.sams.unbeezy.R;
import com.sams.unbeezy.alarm.AlarmReceiver;
import com.sams.unbeezy.controllers.AlarmFragmentController;
import com.sams.unbeezy.models.AlarmModel;

import java.util.Calendar;
import java.util.List;

import static android.app.Activity.RESULT_OK;

/**
 * Created by kennethhalim on 2/12/18.
 */

public class AlarmFragment extends Fragment {
    public static final int NEW_ALARM_REQUEST = 1;
    private static String LOG_TAG = "AlarmFragment";

    AlarmModel[] alarmsArray;
    LayoutInflater inflater;
    LinearLayout alarmListView;

    Gson gson = new Gson();

    AlarmFragmentController controller;
    private static AlarmFragment _instance;
    public AlarmFragment() {
        controller = new AlarmFragmentController(this);
    }

    public static AlarmFragment getInstance() {
        if(_instance == null) {
            _instance = new AlarmFragment();
        }
        return _instance;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(
                R.layout.fragment_alarm, container, false);

        FloatingActionButton alarmFAB = rootView.findViewById(R.id.alarm_insert_fab);
        alarmFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),AddAlarmActivity.class);
                startActivityForResult(intent, NEW_ALARM_REQUEST);
            }
        });
        alarmListView = rootView.findViewById(R.id.alarm_list);

        return rootView;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == NEW_ALARM_REQUEST) {
            if (resultCode == RESULT_OK) {
                String intentString = data.getStringExtra("newAlarm");
                AlarmModel newAlarm = gson.fromJson(intentString,AlarmModel.class);
                controller.addData(newAlarm);
                Log.d(LOG_TAG, String.format("Hour: %d", newAlarm.getHour()));
                Log.d(LOG_TAG, String.format("Minute: %d", newAlarm.getMinute()));
            }
        }
    }

    public void onToggleClicked(View view) {
        if(((ToggleButton) view).isChecked()) {
            Log.d(LOG_TAG, "Alarm On");
//            newAlarm.switchOn();
//            Calendar calendar = Calendar.getInstance();
//            calendar.set(Calendar.HOUR_OF_DAY, alarmTimePicker.getCurrentHour());
//            calendar.set(Calendar.MINUTE, alarmTimePicker.getCurrentMinute());
//            Intent myIntent = new Intent(AddAlarmActivity.this, AlarmReceiver.class);
//            pendingIntent = PendingIntent.getBroadcast(AddAlarmActivity.this, 0, myIntent, 0);
//            alarmManager.set(AlarmManager.RTC, calendar.getTimeInMillis(), pendingIntent);
        } else {
//            newAlarm.switchOff();
//            alarmManager.cancel(pendingIntent);
//            setAlarmText("");
            Log.d(LOG_TAG, "Alarm Off");
        }
    }

    public void updateLayout(final List<AlarmModel> alarmsArray) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adaptLinearLayout(alarmListView, alarmsArray);
            }
        });
    }

    private void adaptLinearLayout(LinearLayout layout, List<AlarmModel> alarmsArray) {
        layout.removeAllViews();
        Log.d("NEWADAPTOR", gson.toJson(alarmsArray));
        int height = 0;
        for (AlarmModel item : alarmsArray) {
            View inflated = inflateLayout(item, layout);
            layout.addView(inflated);
            height += inflated.getMeasuredHeight();
        }
        layout.getLayoutParams().height = height;
    }

    private View inflateLayout(AlarmModel model, ViewGroup parent) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View inflated = inflater.inflate(R.layout.component_alarms_list_view, parent, false);
        TextView alarmTime = inflated.findViewById(R.id.alarm_time);
        alarmTime.setText(String.format("%d:%d", model.getHour(), model.getMinute()));

        return inflated;
    }
}
