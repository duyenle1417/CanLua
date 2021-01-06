package com.example.test;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity
        implements AddCustomerDialog.AddCustomerListener, RecycleViewItemOnClick {

    FloatingActionButton btn_add_customer;
    RecyclerView recyclerView_customer;
    TextView textView_notify_empty_customer;
    ArrayList<Customer> customerlist;
    CustomerAdapter adapter;
    RecyclerView.LayoutManager layoutManager;
    SQLiteDatabase sqLiteDatabase;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ListView listView, header;
    ArrayList<itemMenu> arrayList;
    menuAdapter adapterMenu;
    Toolbar toolbar;
    ArrayList<HeaderNavigation> arrayListHeader;
    adapterHeaderNavigation adapterHeader;
    Switch switch_name, switch_time;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_layout);

        getView();
        actionToolbar();
        actionMenu();
        actionDialog();
        actionHeader();

        //btn thêm vào customer mới - hiện dialog
        btn_add_customer.setOnClickListener(new View.OnClickListener() {
            AddCustomerDialog dialog;
            @Override
            public void onClick(View v) {
                if(switch_name.isChecked()) {
                    dialog = new AddCustomerDialog(HomeActivity.this, customerlist, true);
                } else {
                    dialog = new AddCustomerDialog(HomeActivity.this, customerlist, false);
                }
                dialog.show(getSupportFragmentManager(), "dialog add new customer");
            }
        });

        switch_name.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    getCustomerAllName();
                    switch_time.setChecked(false);
                }
            }
        });

        switch_time.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    getCustomerAll();
                    switch_name.setChecked(false);
                }
            }
        });

    }

    //header cho navigation drawer - hiển thị tên ap vầ version
    private void actionHeader() {
        arrayListHeader = new ArrayList<>();
        arrayListHeader.add(new HeaderNavigation("Cân Lúa", "v1.0.0"));
        adapterHeader = new adapterHeaderNavigation(this, R.layout.line_header, arrayListHeader);
        header.setAdapter(adapterHeader);
    }

    //menu navigation drawer
    private void actionDialog() {
        final Dialog dialog = new Dialog(this);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    //thông tin app
                    case 0:
                        dialog.setContentView(R.layout.dialog_info);
                        dialog.show();
                        Button cancel = dialog.findViewById(R.id.btnCancel);
                        cancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.cancel();
                            }
                        });
                        break;
                    //giới thiệu app
                    case 1:
                        dialog.setContentView(R.layout.dialog_introduce);
                        dialog.show();
                        Button cancel1 = dialog.findViewById(R.id.btnCancel);
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

    //menu item cho navigation drawer
    private void actionMenu() {
        arrayList = new ArrayList<>();
        arrayList.add(new itemMenu("Thông tin", R.drawable.ic_info));
        arrayList.add(new itemMenu("Giới thiệu về App", R.drawable.ic_guide));
        arrayList.add(new itemMenu("Hướng dẫn sử dụng", R.drawable.ic_use));
        adapterMenu = new menuAdapter(this, R.layout.line_item_menu, arrayList);
        listView.setAdapter(adapterMenu);
    }

    //set hamburger icon để mở navigation drawer
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


    //ánh xạ view và lấy data từ DB về hiển thị trên recycleview
    private void getView() {
        //ánh xạ view
        btn_add_customer = findViewById(R.id.btn_add_customer);
        recyclerView_customer = findViewById(R.id.recycleview_customer);
        textView_notify_empty_customer = findViewById(R.id.home_notify_empty_recycleview);
        toolbar = findViewById(R.id.toolbar_home);
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigationView);
        listView = findViewById(R.id.navigationListview);
        header = findViewById(R.id.navigationHeader);
        switch_name = findViewById(R.id.switch_name);
        switch_time = findViewById(R.id.switch_time);
        customerlist = new ArrayList<>();


        //adapter và layoutmanager cho recycleview
        adapter = new CustomerAdapter(this, customerlist, this);

        layoutManager = new LinearLayoutManager(this);

        //adapter = new HomeAdapter(this, getAllItems(), this);
        recyclerView_customer.setLayoutManager(layoutManager);
        recyclerView_customer.setAdapter(adapter);

        //swipe item để xóa
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                delete((Integer) viewHolder.itemView.getTag());
                if (switch_name.isChecked())
                    getCustomerAllName();
                else
                    getCustomerAll();
                Toast.makeText(HomeActivity.this, "Đã xóa thành công!", Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(recyclerView_customer);


        if (switch_name.isChecked())
            getCustomerAllName();
        else
            getCustomerAll();//lấy dữ liệu từ DB table customer
    }


    //lấy dữ liệu từ DB vô ArrayList
    public void getCustomerAll() {
        DatabaseHelper helper = new DatabaseHelper(this, DatabaseHelper.DATABASE_NAME, null, DatabaseHelper.DATABASE_VERSION);
        sqLiteDatabase = helper.getReadableDatabase();

        ArrayList<Customer> list = new ArrayList<>();
        String GET_ALL_DATA = "SELECT * FROM " + DatabaseContract.CustomerTable.TABLE_NAME +
                " ORDER BY " + DatabaseContract.CustomerTable._ID + " DESC;";
        Cursor cursor = sqLiteDatabase.rawQuery(GET_ALL_DATA, null);

        if (cursor.moveToFirst()) {
            do {
                Customer customer = new Customer();
                customer.setID(Integer.parseInt(cursor.getString(cursor.getColumnIndex(DatabaseContract.CustomerTable._ID))));
                customer.setHoTen(cursor.getString(cursor.getColumnIndex(DatabaseContract.CustomerTable.COLUMN_TENKH)));
                customer.setSDT(cursor.getString(cursor.getColumnIndex(DatabaseContract.CustomerTable.COLUMN_SDT)));
                customer.setDate(cursor.getString(cursor.getColumnIndex(DatabaseContract.CustomerTable.COLUMN_TIMESTAMP)));
                list.add(customer);
            } while (cursor.moveToNext());
        }

        //đưa dữ liệu vào customerlist và gọi adapter
        customerlist.clear();
        customerlist.addAll(list);
        //recyclerView_customer.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        cursor.close();
        if (customerlist.size() != 0) {
            textView_notify_empty_customer.setVisibility(View.GONE);
        } else {
            textView_notify_empty_customer.setVisibility(View.VISIBLE);
        }
    }

    public void getCustomerAllName() {
        DatabaseHelper helper = new DatabaseHelper(this, DatabaseHelper.DATABASE_NAME, null, DatabaseHelper.DATABASE_VERSION);
        sqLiteDatabase = helper.getReadableDatabase();

        ArrayList<Customer> list = new ArrayList<>();
        String GET_ALL_DATA = "SELECT * FROM " + DatabaseContract.CustomerTable.TABLE_NAME +
                " ORDER BY " + DatabaseContract.CustomerTable.COLUMN_TENKH + " ASC;";
        Cursor cursor = sqLiteDatabase.rawQuery(GET_ALL_DATA, null);

        if (cursor.moveToFirst()) {
            do {
                Customer customer = new Customer();
                customer.setID(Integer.parseInt(cursor.getString(cursor.getColumnIndex(DatabaseContract.CustomerTable._ID))));
                customer.setHoTen(cursor.getString(cursor.getColumnIndex(DatabaseContract.CustomerTable.COLUMN_TENKH)));
                customer.setSDT(cursor.getString(cursor.getColumnIndex(DatabaseContract.CustomerTable.COLUMN_SDT)));
                customer.setDate(cursor.getString(cursor.getColumnIndex(DatabaseContract.CustomerTable.COLUMN_TIMESTAMP)));
                list.add(customer);
            } while (cursor.moveToNext());
        }

        //đưa dữ liệu vào customerlist và gọi adapter
        customerlist.clear();
        customerlist.addAll(list);
        //recyclerView_customer.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        cursor.close();
        if (customerlist.size() != 0) {
            textView_notify_empty_customer.setVisibility(View.GONE);
        } else {
            textView_notify_empty_customer.setVisibility(View.VISIBLE);
        }
    }

    //cardview clicked
    @Override
    public void OnItemClicked(int position) {
        Intent intent = new Intent(HomeActivity.this, HistoryActivity.class);
        intent.putExtra("name", customerlist.get(position).getHoTen());
        intent.putExtra("phone", customerlist.get(position).getSDT());
        intent.putExtra("date", customerlist.get(position).getDate());
        HomeActivity.this.startActivity(intent);
        HomeActivity.this.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
    }

    ///@Override
    public void ApplyChange(String name, String phone) {
        //reset lại activity đồng thời lấy lại view mới
        this.finish();
        startActivity(getIntent());
        HomeActivity.this.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
    }

    private void delete(Integer id) {
        DatabaseHelper helper = new DatabaseHelper(this, DatabaseHelper.DATABASE_NAME, null, DatabaseHelper.DATABASE_VERSION);
        SQLiteDatabase db = helper.getWritableDatabase();
        db.delete(DatabaseContract.CustomerTable.TABLE_NAME,
                DatabaseContract.CustomerTable._ID + "=" + id, null);
        getCustomerAll();
        if (customerlist.isEmpty()) {
            textView_notify_empty_customer.setVisibility(View.VISIBLE);
        } else {
            textView_notify_empty_customer.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search, menu);

        SearchView searchView = (SearchView) menu.findItem(R.id.menuSearch).getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                adapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (switch_name.isChecked())
                    getCustomerAllName();
                else
                    getCustomerAll();
                adapter.getFilter().filter(newText);
                Log.e("AAAA", newText);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }
}