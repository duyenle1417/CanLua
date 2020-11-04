package com.example.test;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.test.DatabaseContract.*;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "canlua.db";
    public static final int DATABASE_VERSION = 1;

    public DatabaseHelper(@Nullable Context context, @Nullable String name,
                          @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_HISTORY_TABLE = "CREATE TABLE " +
                HistoryTable.TABLE_NAME + " (" +
                HistoryTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                HistoryTable.COLUMN_TENKH + " VARCHAR, " +
                HistoryTable.COLUMN_SDT + " VARCHAR, " +
                HistoryTable.COLUMN_TENGIONG + " VARCHAR, " +
                HistoryTable.COLUMN_DONGIA + " MONEY, " +
                HistoryTable.COLUMN_BAOBI + " INTEGER, " +
                HistoryTable.COLUMN_TONGBAO + " INTEGER, " +
                HistoryTable.COLUMN_TONGKG + " DECIMAL, " +
                HistoryTable.COLUMN_THANHTIEN + " MONEY, " +
                HistoryTable.COLUMN_TIENCOC + " MONEY, " +
                HistoryTable.COLUMN_TIMESTAMP + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                ");";

        db.execSQL(CREATE_HISTORY_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + HistoryTable.TABLE_NAME);
        onCreate(db);
    }

    public void addData(History obj){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(HistoryTable.COLUMN_TENKH,obj.getHoTen());
        cv.put(HistoryTable.COLUMN_SDT,obj.getSDT());
        cv.put(HistoryTable.COLUMN_TENGIONG,obj.getTenGiongLua());
        cv.put(HistoryTable.COLUMN_DONGIA,obj.getDonGia());
        cv.put(HistoryTable.COLUMN_BAOBI,obj.getBaoBi());
        cv.put(HistoryTable.COLUMN_TONGBAO,obj.getSoBao());
        cv.put(HistoryTable.COLUMN_TONGKG,obj.getTongSoKG());
        cv.put(HistoryTable.COLUMN_THANHTIEN,obj.getThanhTien());
        cv.put(HistoryTable.COLUMN_TIENCOC,obj.getTienCoc());

        db.insert(HistoryTable.TABLE_NAME, null, cv);
    }

    public History getData(int ID){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(HistoryTable.TABLE_NAME,
                new String[] { HistoryTable._ID,
                        HistoryTable.COLUMN_TENKH,
                        HistoryTable.COLUMN_SDT,
                        HistoryTable.COLUMN_TENGIONG,
                        HistoryTable.COLUMN_DONGIA,
                        HistoryTable.COLUMN_BAOBI,
                        HistoryTable.COLUMN_TONGBAO,
                        HistoryTable.COLUMN_TONGKG,
                        HistoryTable.COLUMN_THANHTIEN,
                        HistoryTable.COLUMN_TIENCOC},
                HistoryTable._ID+ "=?",
                new String[] { String.valueOf(ID) },
                null,
                null,
                null,
                null);

        History history = new History();
        history.setID(Integer.parseInt(cursor.getString(0)));
        history.setHoTen(cursor.getString(1));
        history.setSDT(cursor.getString(2));
        history.setTenGiongLua(cursor.getString(3));
        history.setDonGia(Integer.parseInt(cursor.getString(4)));
        history.setBaoBi(Integer.parseInt(cursor.getString(5)));
        history.setSoBao(Integer.parseInt(cursor.getString(6)));
        history.setTongSoKG(Double.parseDouble(cursor.getString(7)));
        history.setThanhTien(Integer.parseInt(cursor.getString(8)));
        history.setTienCoc(Integer.parseInt(cursor.getString(9)));
        return history;
    }

    public List<History> getAllData(){
        List<History> historyList = new ArrayList<>();
        String GET_ALL_DATA = "SELECT * FROM " + HistoryTable.TABLE_NAME;

        SQLiteDatabase db = this.getReadableDatabase();
        //Cursor cursor = db.rawQuery(GET_ALL_DATA,null);

        Cursor cursor = db.query(HistoryTable.TABLE_NAME,
                new String[] { HistoryTable._ID,
                        HistoryTable.COLUMN_TENKH,
                        HistoryTable.COLUMN_SDT,
                        HistoryTable.COLUMN_TENGIONG,
                        HistoryTable.COLUMN_DONGIA,
                        HistoryTable.COLUMN_BAOBI,
                        HistoryTable.COLUMN_TONGBAO,
                        HistoryTable.COLUMN_TONGKG,
                        HistoryTable.COLUMN_THANHTIEN,
                        HistoryTable.COLUMN_TIENCOC},
                null,
                null,
                null,
                null,
                HistoryTable.COLUMN_TIMESTAMP + "DESC");

        if (cursor.moveToFirst()) {
            do {
                History history = new History();
                history.setID(Integer.parseInt(cursor.getString(0)));
                history.setHoTen(cursor.getString(1));
                history.setSDT(cursor.getString(2));
                history.setTenGiongLua(cursor.getString(3));
                history.setDonGia(Integer.parseInt(cursor.getString(4)));
                history.setBaoBi(Integer.parseInt(cursor.getString(5)));
                history.setSoBao(Integer.parseInt(cursor.getString(6)));
                history.setTongSoKG(Double.parseDouble(cursor.getString(7)));
                history.setThanhTien(Integer.parseInt(cursor.getString(8)));
                history.setTienCoc(Integer.parseInt(cursor.getString(9)));
            } while (cursor.moveToNext());
        }
        return historyList;
    }

    public int updateData(History obj){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(HistoryTable.COLUMN_TENKH,obj.getHoTen());
        cv.put(HistoryTable.COLUMN_SDT,obj.getSDT());
        cv.put(HistoryTable.COLUMN_TENGIONG,obj.getTenGiongLua());
        cv.put(HistoryTable.COLUMN_DONGIA,obj.getDonGia());
        cv.put(HistoryTable.COLUMN_BAOBI,obj.getBaoBi());
        cv.put(HistoryTable.COLUMN_TIENCOC,obj.getTienCoc());

        return db.update(HistoryTable.TABLE_NAME, cv, HistoryTable._ID + " = ?",
                new String[]{String.valueOf(obj.getID())});
    }

    public void deleteData(History obj){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(HistoryTable.TABLE_NAME,HistoryTable._ID + " = ?",
                new String[]{String.valueOf(obj.getID())});
        db.close();
    }
}
