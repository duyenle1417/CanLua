package com.example.test;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class PreviewActivity extends AppCompatActivity {
    String dateJoin, dateCreate;
    EditText
            editText_tenKH, editText_sdt, editText_tenLua,
            editText_dongia, editText_trubi, editText_tiencoc;
    TextView textView_tongbao, textView_tongkg, textView_thanhtien;

    History history;
    SQLiteDatabase sqLiteDatabase;

    @Override

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.preview_layout);
        Intent intent = getIntent();
        dateJoin = intent.getStringExtra("dateJoin");
        dateCreate = intent.getStringExtra("dateCreate");

        getView();
    }

    private void getView() {
        editText_tenKH = findViewById(R.id.edittext_tenKH);
        editText_sdt = findViewById(R.id.edittext_sdt);
        editText_dongia = findViewById(R.id.edittext_dongia);
        editText_tenLua = findViewById(R.id.edittext_tenLua);
        editText_trubi = findViewById(R.id.edittext_trubi);
        editText_tiencoc = findViewById(R.id.edittext_tiencoc);
        textView_tongbao = findViewById(R.id.textView_info_tongbaolua);
        textView_tongkg = findViewById(R.id.textView_info_tongkylua);
        textView_thanhtien = findViewById(R.id.textView_info_thanhtien);
        history = new History();
        getHistory();

        editText_tenKH.setText(history.getHoTen());
        editText_sdt.setText(history.getSDT());
        editText_tenLua.setText(history.getTenGiongLua());
        editText_dongia.setText("" + history.getDonGia());
        editText_trubi.setText("" + history.getBaoBi());
        editText_tiencoc.setText("" + history.getTienCoc());
        textView_tongbao.setText("" + history.getSoBao());
        textView_tongkg.setText(String.format("%s", history.getTongSoKG()));
        textView_thanhtien.setText("" + history.getThanhTien());
    }

    private void getHistory() {
        DatabaseHelper helper = new DatabaseHelper(this, DatabaseHelper.DATABASE_NAME, null, DatabaseHelper.DATABASE_VERSION);
        sqLiteDatabase = helper.getReadableDatabase();

        String GET_DATA = "SELECT * FROM " + DatabaseContract.HistoryTable.TABLE_NAME +
                " WHERE " + DatabaseContract.HistoryTable.COLUMN_DATEJOIN + " LIKE '%" + dateJoin + "%'" +
                " AND " + DatabaseContract.HistoryTable.COLUMN_TIMESTAMP + " LIKE '%" + dateCreate + "%';";
        Cursor cursor = sqLiteDatabase.rawQuery(GET_DATA, null);

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

        cursor.close();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(PreviewActivity.this, HistoryActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("name", editText_tenKH.getText());
        intent.putExtra("phone", editText_sdt.getText());
        intent.putExtra("id", history.getID());
        intent.putExtra("date", history.getDateJoin());
        PreviewActivity.this.startActivity(intent);
        PreviewActivity.this.finish();
    }
}
