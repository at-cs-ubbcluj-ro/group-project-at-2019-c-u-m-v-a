package com.example.ratiu.atproject;

import android.arch.lifecycle.Observer;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final MeasurementViewModel viewModel=ViewModelProviders.of(this).get(MeasurementViewModel.class);
        final int[] limits=new int[3];
        limits[0]=100;limits[1]=500;limits[2]=1000;

        final EditText limit_low=findViewById(R.id.limit_low);
        limit_low.setText(String.valueOf(limits[0]));
        final EditText limit_med=findViewById(R.id.limit_med);
        limit_med.setText(String.valueOf(limits[1]));
        final EditText limit_high=findViewById(R.id.limit_high);
        limit_high.setText(String.valueOf(limits[2]));

        final RecyclerView list = findViewById(R.id.list_measurements);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        list.setLayoutManager(mLayoutManager);
        final ItemAdapter adapter=new ItemAdapter(viewModel.getMeasurements().getValue(),limits);
        list.setAdapter(adapter);

        viewModel.getMeasurements().observe(this, new Observer<List<List<Integer>>>() {
            @Override
            public void onChanged(@Nullable List<List<Integer>> integers) {
                adapter.setNewList(integers);
                list.setAdapter(adapter);
            }
        });

        limit_low.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean focused) {
                if(!focused) {
                    limits[0] = Integer.parseInt(limit_low.getText().toString());
                    adapter.setLimits(limits);
                }
            }
        });

        limit_med.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean focused) {
                if(!focused) {
                    limits[1] = Integer.parseInt(limit_med.getText().toString());
                    adapter.setLimits(limits);
                }
            }
        });

        limit_high.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean focused) {
                if(!focused) {
                    limits[2] = Integer.parseInt(limit_high.getText().toString());
                    adapter.setLimits(limits);
                }
            }
        });

        findViewById(R.id.id_tag).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapter.sortById();
            }
        });

        findViewById(R.id.value_tag).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapter.sortByValue();
            }
        });
    }
}
