package com.example.test;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity
        implements AddCustomerDialog.AddCustomerDialogListener {

    FloatingActionButton btn_add_customer;
    RecyclerView recyclerView_customer;
    ArrayList<Customer> customerlist;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_layout);

        getView();

        //btn thêm vào customer mới - hiện dialog
        btn_add_customer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddCustomerDialog dialog = new AddCustomerDialog();
                dialog.show(getSupportFragmentManager(), "dialog add new customer");
            }
        });

    }

    private void getView() {
        btn_add_customer = findViewById(R.id.btn_add_customer);
        recyclerView_customer = findViewById(R.id.recycleview_customer);
        customerlist = new ArrayList<>();

        adapter = new CustomerAdapter(customerlist);
        recyclerView_customer.setAdapter(adapter);

        layoutManager = new LinearLayoutManager(this);
        recyclerView_customer.setLayoutManager(layoutManager);
    }

    @Override
    public void AddItemView(String name, String phone) {
        Customer customer = new Customer();
        customer.setHoTen(name);
        customer.setSDT(phone);

        customerlist.add(customer);
        adapter.notifyDataSetChanged();
    }
}
