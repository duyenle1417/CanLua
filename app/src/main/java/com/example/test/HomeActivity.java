package com.example.test;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    FloatingActionButton btn_add_customer;
    RecyclerView recyclerView_customer;
    List<History> customerlist;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_layout);

        getView();

    }

    private void getView() {
        btn_add_customer = findViewById(R.id.btn_add_customer);
        recyclerView_customer = findViewById(R.id.recycleview_customer);
        customerlist = new ArrayList<>();
    }
}
