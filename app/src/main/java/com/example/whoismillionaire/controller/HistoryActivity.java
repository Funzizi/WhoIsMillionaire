package com.example.whoismillionaire.controller;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Adapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.example.whoismillionaire.R;
import com.example.whoismillionaire.model.History;

import java.util.ArrayList;
import java.util.List;

public class HistoryActivity extends AppCompatActivity {
    private DatabaseHistory historyData;
    private ListView lvHistory;
    private List<History> list = new ArrayList<>();
    private Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        lvHistory = findViewById(R.id.lv_history);
        historyData = new DatabaseHistory(HistoryActivity.this);
        list = historyData.getHistory();
        adapter = new HistoryAdapter(HistoryActivity.this, R.layout.item_history, list);
        lvHistory.setAdapter((ListAdapter) adapter);
    }
}