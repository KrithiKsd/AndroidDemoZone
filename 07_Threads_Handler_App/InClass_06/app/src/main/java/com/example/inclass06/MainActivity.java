package com.example.inclass06;

/*
a. Assignment #:InCLass05
b. File Name:MainActivity.java
c. Full name of the Student 1:Krithika Kasaragod
*/

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.SeekBar;
import android.widget.Toast;

import com.example.inclass06.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding activityMainBinding;
    int complexity = 0;
    ExecutorService threadPool;
    Handler handler;
    ArrayAdapter<Double> adapter;
    ArrayList<Double> list_Returned;


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(activityMainBinding.getRoot());
        setTitle(getString(R.string.title_main_activity));
        list_Returned = new ArrayList<>();
        activityMainBinding.tvTimes.setText(String.format(getString(R.string.label_times),0));

        handler = new Handler(message -> {
            double avg = 0.0;
            switch (message.what) {

                case DoWork.STATUS_START:
                    activityMainBinding.btnGenerate.setEnabled(false);
                    activityMainBinding.seekBar.setEnabled(false);
                    activityMainBinding.progressBar.setVisibility(View.VISIBLE);
                    activityMainBinding.tvProgressResult.setVisibility(View.VISIBLE);
                    activityMainBinding.progressBar.setProgress(0);
                    activityMainBinding.tvAverageResult.setText(getString(R.string.label_zero));
                    activityMainBinding.tvProgressResult.setText(getString(R.string.label_zero_slash) + complexity);
                    if (list_Returned.size() > 0) {
                        activityMainBinding.lstAverage.setVisibility(View.INVISIBLE);
                    }
                    break;

                case DoWork.STATUS_PROGRESS:
                    activityMainBinding.progressBar.setMax(complexity);
                    activityMainBinding.progressBar.setProgress((message.getData().getInt(DoWork.PROGRESS_KEY)) + 1);
                    int res = (message.getData().getInt(DoWork.PROGRESS_KEY)) + 1;
                    String result = res + getString(R.string.label_slash) + complexity;
                    activityMainBinding.tvProgressResult.setVisibility(View.VISIBLE);
                    activityMainBinding.tvProgressResult.setText(result);

                    double gen_numb = (message.getData().getDouble(DoWork.UPDATE_VALUE));
                    activityMainBinding.tvAverage.setVisibility(View.VISIBLE);
                    activityMainBinding.tvAverageResult.setVisibility(View.VISIBLE);
                    avg = (avg + gen_numb) / res;
                    list_Returned = (ArrayList<Double>) message.getData().getSerializable(DoWork.UPDATE_LIST);
                    activityMainBinding.tvAverageResult.setText(String.valueOf(avg));
                    activityMainBinding.lstAverage.setVisibility(View.VISIBLE);
                    adapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1,
                            android.R.id.text1, list_Returned);
                    activityMainBinding.lstAverage.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    break;

                case DoWork.STATUS_STOP:
                    activityMainBinding.btnGenerate.setEnabled(true);
                    activityMainBinding.seekBar.setEnabled(true);

                    break;
            }

            return false;
        });

        threadPool = Executors.newFixedThreadPool(2);


        activityMainBinding.seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int value, boolean b) {

                activityMainBinding.tvTimes.setText(String.format(getString(R.string.label_times),value));
                complexity = value;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                activityMainBinding.tvTimes.setText(String.format(getString(R.string.label_times),0));
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        activityMainBinding.btnGenerate.setOnClickListener(view -> {
            if (complexity == 0) {
                Toast.makeText(MainActivity.this, getString(R.string.error_complexity), Toast.LENGTH_SHORT).show();
            }

            threadPool.execute(new DoWork(complexity));

        });
    }

    class DoWork implements Runnable {
        static final int STATUS_START = 0X00;
        static final int STATUS_PROGRESS = 0X01;
        static final int STATUS_STOP = 0X02;
        static final String PROGRESS_KEY = "PROGRESS_KEY";
        static final String UPDATE_VALUE = "UPDATE_VALUE";
        static final String UPDATE_LIST = "UPDATE_LIST";

        int complexity;

        DoWork(int complexity) {
            this.complexity = complexity;
        }

        @Override
        public void run() {
            ArrayList<Double> list = new ArrayList<>();
            double genNum;
            Message startMessage = new Message();
            startMessage.what = STATUS_START;
            handler.sendMessage(startMessage);

            for (int i = 0; i < complexity; i++) {

                genNum = HeavyWork.getNumber();
                Message progressMessage = new Message();
                progressMessage.what = STATUS_PROGRESS;
                Bundle bundle = new Bundle();
                bundle.putInt(PROGRESS_KEY, i);
                bundle.putDouble(UPDATE_VALUE, genNum);
                list.add(genNum);
                bundle.putSerializable(UPDATE_LIST, list);
                progressMessage.setData(bundle);
                handler.sendMessage(progressMessage);
            }

            Message stopMessage = new Message();
            stopMessage.what = STATUS_STOP;
            handler.sendMessage(stopMessage);

        }
    }
}