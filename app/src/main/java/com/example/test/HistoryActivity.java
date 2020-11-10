package com.example.test;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class HistoryActivity extends AppCompatActivity {

    FloatingActionButton btn_add_history;
    RecyclerView recyclerView_history;
    TextView textView_notify_empty_history;
    ArrayList<History> historyList;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;
    SQLiteDatabase sqLiteDatabase;
    String name, phone, datejoin, reload;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history_layout);
        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        phone = intent.getStringExtra("phone");
        datejoin = intent.getStringExtra("date");
        reload = intent.getStringExtra("reload");
        getView();

        //chuyển sang layout nhập thông tin cân lúa
        btn_add_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HistoryActivity.this, InputCustomerInfoActivity.class);
                intent.putExtra("name", name);
                intent.putExtra("phone", phone);
                intent.putExtra("datejoin", datejoin);
                HistoryActivity.this.startActivity(intent);
                HistoryActivity.this.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
                HistoryActivity.this.finish();
            }
        });

        //recreate acitivity
        if (reload == "yes") {
            this.finish();
            this.overridePendingTransition(0, 0);
            startActivity(getIntent());
            this.overridePendingTransition(0, 0);
        }
    }

    private void getView() {
        btn_add_history = findViewById(R.id.btn_add_history);
        recyclerView_history = findViewById(R.id.recycleview_history);
        textView_notify_empty_history = findViewById(R.id.history_notify_empty_recycleview);
        historyList = new ArrayList<>();

        adapter = new HistoryAdapter(this, historyList);
        recyclerView_history.setAdapter(adapter);

        layoutManager = new LinearLayoutManager(this);
        recyclerView_history.setLayoutManager(layoutManager);
        //
        Toolbar toolbar = findViewById(R.id.toolbar_history);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                delete((Integer) viewHolder.itemView.getTag());
            }
        }).attachToRecyclerView(recyclerView_history);

        //lấy dữ liệu từ DB
        getHistoryAll();
        if (historyList.isEmpty()) {
            textView_notify_empty_history.setVisibility(View.VISIBLE);
        } else {
            textView_notify_empty_history.setVisibility(View.GONE);
        }
    }

    private void delete(Integer tag) {
        DatabaseHelper helper = new DatabaseHelper(this, DatabaseHelper.DATABASE_NAME, null, DatabaseHelper.DATABASE_VERSION);
        SQLiteDatabase db = helper.getWritableDatabase();
        db.delete(DatabaseContract.HistoryTable.TABLE_NAME,
                DatabaseContract.HistoryTable._ID + "=" + tag, null);
        getHistoryAll();
        if (historyList.isEmpty()) {
            textView_notify_empty_history.setVisibility(View.VISIBLE);
        } else {
            textView_notify_empty_history.setVisibility(View.GONE);
        }
    }

    private void getHistoryAll() {
        DatabaseHelper helper = new DatabaseHelper(this, DatabaseHelper.DATABASE_NAME, null, DatabaseHelper.DATABASE_VERSION);
        sqLiteDatabase = helper.getReadableDatabase();

        ArrayList<History> list = new ArrayList<>();
        String GET_ALL_DATA = "SELECT * FROM " + DatabaseContract.HistoryTable.TABLE_NAME +
                " WHERE " + DatabaseContract.HistoryTable.COLUMN_TENKH + " LIKE '%" + name + "%'" +
                " AND " + DatabaseContract.HistoryTable.COLUMN_DATEJOIN + " LIKE '%" + datejoin + "%'" +
                " AND " + DatabaseContract.HistoryTable.COLUMN_SDT + " LIKE '%" + phone + "%'" +
                " ORDER BY " + DatabaseContract.HistoryTable.COLUMN_TIMESTAMP + " DESC;";
        Cursor cursor = sqLiteDatabase.rawQuery(GET_ALL_DATA, null);

        if (cursor.moveToFirst()) {
            do {
                History history = new History();
                history.setID(cursor.getInt(cursor.getColumnIndex(DatabaseContract.HistoryTable._ID)));
                history.setHoTen(cursor.getString(cursor.getColumnIndex(DatabaseContract.HistoryTable.COLUMN_TENKH)));
                history.setSDT(cursor.getString(cursor.getColumnIndex(DatabaseContract.HistoryTable.COLUMN_SDT)));
                history.setTenGiongLua(cursor.getString(cursor.getColumnIndex(DatabaseContract.HistoryTable.COLUMN_TENGIONG)));
                history.setDonGia(cursor.getInt(cursor.getColumnIndex(DatabaseContract.HistoryTable.COLUMN_DONGIA)));
                history.setBaoBi(cursor.getInt(cursor.getColumnIndex(DatabaseContract.HistoryTable.COLUMN_BAOBI)));
                history.setSoBao(cursor.getInt(cursor.getColumnIndex(DatabaseContract.HistoryTable.COLUMN_TONGBAO)));
                history.setTongSoKG(cursor.getDouble(cursor.getColumnIndex(DatabaseContract.HistoryTable.COLUMN_TONGKG)));
                history.setTienCoc(cursor.getInt(cursor.getColumnIndex(DatabaseContract.HistoryTable.COLUMN_TIENCOC)));
                history.setThanhTien(cursor.getInt(cursor.getColumnIndex(DatabaseContract.HistoryTable.COLUMN_THANHTIEN)));
                history.setTimestamp(cursor.getString(cursor.getColumnIndex(DatabaseContract.HistoryTable.COLUMN_TIMESTAMP)));
                history.setDateJoin(cursor.getString(cursor.getColumnIndex(DatabaseContract.HistoryTable.COLUMN_DATEJOIN)));
                list.add(history);
            } while (cursor.moveToNext());
        }

        //đưa dữ liệu vào customerlist và gọi adapter
        historyList.clear();
        historyList.addAll(list);
        adapter.notifyDataSetChanged();
        cursor.close();
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
        return false;
    }
}