package com.example.andreea.helloworld.ui;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.andreea.helloworld.database.SeriesDB;
import com.example.andreea.helloworld.R;
import com.example.andreea.helloworld.SeriesAdapter;
import com.example.andreea.helloworld.model.Series;
import com.example.andreea.helloworld.remote.ApiUtils;
import com.example.andreea.helloworld.remote.SeriesService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

public class MainActivity extends AppCompatActivity {

    private static final String DATABASE_NAME = "series_db";
    private SeriesDB seriesDatabase;
    private List<Series> series;

    private RecyclerView recyclerView;
    private SeriesService seriesService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        seriesDatabase = Room.databaseBuilder(getApplicationContext(),
                SeriesDB.class, DATABASE_NAME).fallbackToDestructiveMigration()
                .build();

        recyclerView = findViewById(R.id.recyclerView);

        seriesService = ApiUtils.getSeriesService();

//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                series = seriesDatabase.daoAccess().getAllSeries();
//                Log.d("MainActivity", series.toString());
//                SeriesAdapter seriesAdapter = new SeriesAdapter(series);
//                recyclerView.