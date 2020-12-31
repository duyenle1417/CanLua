package com.example.test;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class PreviewActivity extends AppCompatActivity {
    String dateJoin, dateCreate, name, phone;
    EditText editText_tenLua, editText_dongia, editText_trubi, editText_tiencoc;
    TextView textView_tongbao, textView_tongkg, textView_thanhtien;
    //Toolbar previewToolbar;

    History history;
    //List<History> list;
    SQLiteDatabase sqLiteDatabase;
    int dongia, trubi;

    @Override

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.preview_layout);
        Intent intent = getIntent();
        dateJoin = intent.getStringExtra("dateJoin");
        dateCreate = intent.getStringExtra("dateCreate");
        name = intent.getStringExtra("name");
        phone = intent.getStringExtra("phone");
        getView();
        dongia = Integer.parseInt(editText_dongia.getText().toString());
        trubi = Integer.parseInt(editText_trubi.getText().toString());
    }

    private void getView() {
        editText_dongia = findViewById(R.id.edittext_dongia);
        editText_tenLua = findViewById(R.id.edittext_tenLua);
        editText_trubi = findViewById(R.id.edittext_trubi);
        editText_tiencoc = findViewById(R.id.edittext_tiencoc);
        textView_tongbao = findViewById(R.id.textView_info_tongbaolua);
        textView_tongkg = findViewById(R.id.textView_info_tongkylua);
        textView_thanhtien = findViewById(R.id.textView_info_thanhtien);
        Toolbar previewToolbar = findViewById(R.id.preview_toolbar);
        setSupportActionBar(previewToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        history = new History();
        getHistory();
    }



    private void AddMoney() {
        int sumOfBag = Integer.parseInt(textView_tongbao.getText().toString());
        int sumOfWeight = Integer.parseInt(textView_tongkg.getText().toString());

        double money = dongia * (sumOfWeight - ((1.0 / trubi) * sumOfBag));

        //làm tròn vì double có lỗi hiển thị => kết quả đúng
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###,###");
        decimalFormat.setRoundingMode(RoundingMode.UP);
        textView_thanhtien.setText(decimalFormat.format(money));
    }

    private void getHistory() {
        DatabaseHelper helper = new DatabaseHelper(this, DatabaseHelper.DATABASE_NAME, null, DatabaseHelper.DATABASE_VERSION);
        sqLiteDatabase = helper.getReadableDatabase();

        //ArrayList<History> arrayList = new ArrayList<>();
        String GET_DATA = "SELECT * FROM " + DatabaseContract.HistoryTable.TABLE_NAME +
                " WHERE " + DatabaseContract.HistoryTable.COLUMN_DATEJOIN + " LIKE '%" + dateJoin + "%'" +
                " AND " + DatabaseContract.HistoryTable.COLUMN_TIMESTAMP + " LIKE '%" + dateCreate + "%';";
        Cursor cursor = sqLiteDatabase.rawQuery(GET_DATA, null);

        if (cursor.moveToFirst() && cursor.getCount() >= 1) {
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
            //arrayList.add(history);

            editText_tenLua.setText(history.getTenGiongLua());
            editText_dongia.setText("" + history.getDonGia());
            editText_trubi.setText("" + history.getBaoBi());
            editText_tiencoc.setText("" + history.getTienCoc());
            textView_tongbao.setText("" + history.getSoBao());
            textView_tongkg.setText(String.format("%s", history.getTongSoKG()));
            textView_thanhtien.setText("" + history.getThanhTien());
        }

        cursor.close();
    }

    private void updateHistory() {
        DatabaseHelper helper = new DatabaseHelper(this, DatabaseHelper.DATABASE_NAME, null, DatabaseHelper.DATABASE_VERSION);
        sqLiteDatabase = helper.getWritableDatabase();
        String TenGiongLua;
        int DonGia, BaoBi, SoBao, ThanhTien, TienCoc;
        double TongSoKG;
        TenGiongLua = editText_tenLua.getText().toString();
        DonGia = Integer.parseInt(editText_dongia.getText().toString());
        BaoBi = Integer.parseInt(editText_trubi.getText().toString());
        TienCoc = Integer.parseInt(editText_tiencoc.getText().toString());
        SoBao = Integer.parseInt(textView_tongbao.getText().toString());
        TongSoKG = Double.parseDouble(textView_tongkg.getText().toString());
        ThanhTien = Integer.parseInt(textView_thanhtien.getText().toString());

        String UPDATE_HISTORY = "UPDATE " + DatabaseContract.HistoryTable.TABLE_NAME + " SET " + DatabaseContract.HistoryTable.COLUMN_TENGIONG + " = '" + TenGiongLua +"'"
                        + ", " + DatabaseContract.HistoryTable.COLUMN_DONGIA + " = '"+ DonGia +"'"
                        + ", " + DatabaseContract.HistoryTable.COLUMN_BAOBI + " = '"+ BaoBi +"'"
                        + ", " + DatabaseContract.HistoryTable.COLUMN_TIENCOC + " = '"+ TienCoc +"'"
                        + ", " + DatabaseContract.HistoryTable.COLUMN_TONGBAO + " = '"+ SoBao +"'"
                        + ", " + DatabaseContract.HistoryTable.COLUMN_TONGKG + " = '"+ TongSoKG +"'"
                        + ", " + DatabaseContract.HistoryTable.COLUMN_THANHTIEN + " = '"+ ThanhTien +"'"
                        + " WHERE " + DatabaseContract.HistoryTable.COLUMN_TIMESTAMP + " = '"+ dateCreate +"';";
        sqLiteDatabase.execSQL(UPDATE_HISTORY);
        Toast.makeText(this, "Đã sửa thành công", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        //updateHistory();
        Intent intent = new Intent(PreviewActivity.this, HistoryActivity.class);
        intent.putExtra("name", name);
        intent.putExtra("phone", phone);
        intent.putExtra("date", dateJoin);
        intent.putExtra("reload", "yes");
        PreviewActivity.this.startActivity(intent);
        PreviewActivity.this.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
        PreviewActivity.this.finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.preview, menu);
        setTitle(name + " - " + phone);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.OK:
                updateHistory();
                Toast.makeText(this, "Đã sửa thành công!", Toast.LENGTH_SHORT).show();
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}