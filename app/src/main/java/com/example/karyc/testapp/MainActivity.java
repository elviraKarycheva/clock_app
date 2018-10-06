package com.example.karyc.testapp;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.karyc.testapp.databinding.ActivityMainBinding;

import java.util.Locale;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    int lastHour;
    int lastMinute;
    Random random = new Random();
    int hour = 12;
    int minute = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        binding.messageView.setText(R.string.string_message);
        updateTimeLabel();
        binding.clockView.setListener(new ClockViewListener() {
            @Override
            public void onHourChanged(int hour) {
                lastHour = hour;
            }

            @Override
            public void onMinuteChanged(int minute) {
                lastMinute = minute;
            }
        });

        binding.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (compare(hour) == true && lastMinute == minute) {
                    binding.messageView.setText(R.string.string_true);
                    android.os.Handler handler = new android.os.Handler();
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            minute = random.nextInt(60);
                            hour = random.nextInt(24);
                            updateTimeLabel();
                            binding.messageView.setText(R.string.string_message);
                        }
                    }, 5000);

                } else {
                    binding.messageView.setText(R.string.string_false);
                }
            }
        });
    }

    private void updateTimeLabel() {
        String hourString = String.format(Locale.getDefault(), "%02d", hour);
        String minutesString = String.format(Locale.getDefault(), "%02d", minute);
        binding.timeView.setText(getResources().getString(R.string.time_template, hourString, minutesString));
    }

    private boolean compare(int hour) {
        if (lastHour == hour || (hour - lastHour) == 12) {
            return true;
        } else {
            return false;
        }
    }
}
