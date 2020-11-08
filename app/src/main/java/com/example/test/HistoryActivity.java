package com.example.test;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class HistoryActivity extends AppCompatActivity
        implements RecycleViewItemOnClick {

    FloatingActionButton btn_add_history;
    RecyclerView recyclerView_history;
    ArrayList<History> historyList;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;
    String name, phone;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history_layout);
        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        phone = intent.getStringExtra("phone");
        getView();

        btn_add_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HistoryActivity.this, InputCustomerInfoActivity.class);
                HistoryActivity.this.startActivity(intent);
            }
        });
    }

    private void getView() {
        btn_add_history = findViewById(R.id.btn_add_history);
        recyclerView_history = findViewById(R.id.recycleview_history);
        historyList = new ArrayList<>();

        adapter = new HistoryAdapter(historyList, this);
        recyclerView_history.setAdapter(adapter);

        layoutManager = new LinearLayoutManager(this);
        recyclerView_history.setLayoutManager(layoutManager);
        //
        Toolbar toolbar = findViewById(R.id.toolbar_history);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.history, menu);
        setTitle(name + " - " + phone);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void OnItemClicked(int posotion) {
        Intent intent = new Intent(HistoryActivity.this, InputCustomerInfoActivity.class);
        HistoryActivity.this.startActivity(intent);
    }
}
