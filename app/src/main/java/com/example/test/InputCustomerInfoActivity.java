package com.example.test;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class InputCustomerInfoActivity extends AppCompatActivity {

    EditText editText_tengiong;
    EditText editText_dongia;
    EditText editText_trubi;
    EditText editText_tiencoc;
    String name, phone, dateCreate, datejoin;
    SQLiteDatabase sqLiteDatabase;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.input_customer_info_layout);
        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        phone = intent.getStringExtra("phone");
        datejoin = intent.getStringExtra("datejoin");
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        dateCreate = dateFormat.format(Calendar.getInstance().getTime());//time lấy khi mở layout nhập cân lúa
        getView();

    }

    private void getView() {
        editText_tengiong = findViewById(R.id.edittext_tenLua_info);
        editText_dongia = findViewById(R.id.edittext_dongia_info);
        editText_trubi = findViewById(R.id.edittext_trubi_info);
        editText_tiencoc = findViewById(R.id.edittext_tiencoc_info);

        //
        Toolbar toolbar = findViewById(R.id.info_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.input_info, menu);
        setTitle("");
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.OK:
                AddHistory();//vô DB
                Intent intent = new Intent(InputCustomerInfoActivity.this, InputWeightActivity.class);
                //id để update DB
                intent.putExtra("name", name);
                intent.putExtra("datejoin", datejoin);
                intent.putExtra("dateCreate", dateCreate);//tên file json
                InputCustomerInfoActivity.this.startActivity(intent);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(InputCustomerInfoActivity.this, HistoryActivity.class);
        intent.putExtra("name", name);
        intent.putExtra("phone", phone);
        InputCustomerInfoActivity.this.startActivity(intent);
        InputCustomerInfoActivity.this.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
        InputCustomerInfoActivity.this.startActivity(intent);
    }

    private void AddHistory() {
        DatabaseHelper helper = new DatabaseHelper(this, DatabaseHelper.DATABASE_NAME, null, DatabaseHelper.DATABASE_VERSION);
        sqLiteDatabase = helper.getWritableDatabase();

        ContentValues cv = new ContentValues();
        String tenGiong, donGia, truBi, tienCoc;
        tenGiong = editText_tengiong.getText().toString();
        donGia = editText_dongia.getText().toString();
        truBi = editText_trubi.getText().toString();
        tienCoc = editText_tiencoc.getText().toString();

        cv.put(DatabaseContract.HistoryTable.COLUMN_TENKH, name);
        cv.put(DatabaseContract.HistoryTable.COLUMN_SDT, phone);
        cv.put(DatabaseContract.HistoryTable.COLUMN_DATEJOIN, datejoin);
        cv.put(DatabaseContract.HistoryTable.COLUMN_TIMESTAMP, dateCreate);

        if (tenGiong.length() > 0)
            cv.put(DatabaseContract.HistoryTable.COLUMN_TENGIONG, tenGiong);
        else
            cv.put(DatabaseContract.HistoryTable.COLUMN_TENGIONG, "---");

        if (donGia.length() > 0)
            cv.put(DatabaseContract.HistoryTable.COLUMN_DONGIA, donGia);
        else
            cv.put(DatabaseContract.HistoryTable.COLUMN_DONGIA, "5000");

        if (truBi.length() > 0)
            cv.put(DatabaseContract.HistoryTable.COLUMN_BAOBI, truBi);
        else
            cv.put(DatabaseContract.HistoryTable.COLUMN_BAOBI, "1");

        if (tienCoc.length() > 0)
            cv.put(DatabaseContract.HistoryTable.COLUMN_TIENCOC, tienCoc);
        else
            cv.put(DatabaseContract.HistoryTable.COLUMN_TIENCOC, "0");

        cv.put(DatabaseContract.HistoryTable.COLUMN_TONGBAO, "0");
        cv.put(DatabaseContract.HistoryTable.COLUMN_TONGKG, "0");

        //
        sqLiteDatabase.insert(DatabaseContract.HistoryTable.TABLE_NAME, null, cv);
    }
}
