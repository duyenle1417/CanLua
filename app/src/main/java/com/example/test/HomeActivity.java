package com.example.test;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity
        implements AddCustomerDialog.AddCustomerDialogListener, RecycleViewItemOnClick {

    FloatingActionButton btn_add_customer;
    RecyclerView recyclerView_customer;
    ArrayList<Customer> customerlist;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ListView listView, header;
    ArrayList<itemMenu> arrayList;
    menuAdapter adapterMenu;
    Toolbar toolbar;
    ArrayList<HeaderNavigation> arrayListHeader;
    adapterHeaderNavigation adapterHeader;

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

        anhXa();
        actionToolbar();
        actionMenu();
        actionDialog();
        actionHeader();
    }

    private void actionHeader() {
        arrayListHeader = new ArrayList<>();
        arrayListHeader.add(new HeaderNavigation("Cân Lúa", "v1.0.0"));
        adapterHeader = new adapterHeaderNavigation(this, R.layout.line_header, arrayListHeader);
        header.setAdapter(adapterHeader);
    }

    private void actionDialog() {
        final Dialog dialog = new Dialog(this);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        dialog.setContentView(R.layout.dialog_info);
                        dialog.show();
                        Button cancel = (Button) dialog.findViewById(R.id.btnCancel);
                        cancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.cancel();
                            }
                        });
                        break;
                    case 1:
                        dialog.setContentView(R.layout.dialog_introduce);
                        dialog.show();
                        Button cancel1 = (Button) dialog.findViewById(R.id.btnCancel);
                        cancel1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.cancel();
                            }
                        });
                        break;

                }
            }
        });
    }

    private void actionMenu() {
        arrayList = new ArrayList<>();
        arrayList.add(new itemMenu("Thông tin", R.drawable.ic_info));
        arrayList.add(new itemMenu("Giới thiệu về App", R.drawable.ic_guide));
        arrayList.add(new itemMenu("Hướng dẫn sử dụng", R.drawable.ic_use));
        adapterMenu = new menuAdapter(this, R.layout.line_item_menu, arrayList);
        listView.setAdapter(adapterMenu);
    }

    private void actionToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(R.drawable.ic_menu);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
    }

    private void anhXa() {
        toolbar = (Toolbar) findViewById(R.id.toolbar_home);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        navigationView = (NavigationView) findViewById(R.id.navigationView);
        listView = (ListView) findViewById(R.id.navigationListview);
        header = (ListView) findViewById(R.id.navigationHeader);
        //btnAdd = findViewById(R.id.btn_add_customer);;
    }

    private void getView() {
        btn_add_customer = findViewById(R.id.btn_add_customer);
        recyclerView_customer = findViewById(R.id.recycleview_customer);
        customerlist = new ArrayList<>();

        //adapter = new CustomerAdapter(customerlist, this);
        adapter = new CustomerAdapter(getApplicationContext(),customerlist, this);
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

    @Override
    public void OnItemClicked(int position) {
        Intent intent = new Intent(HomeActivity.this, HistoryActivity.class);
        intent.putExtra("name", customerlist.get(position).getHoTen());
        intent.putExtra("phone", customerlist.get(position).getSDT());
        HomeActivity.this.startActivity(intent);
        HomeActivity.this.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
    }
}
