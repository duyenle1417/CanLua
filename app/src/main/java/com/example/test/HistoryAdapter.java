package com.example.test;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> {
    ArrayList<History> list;
    Context context;

    public HistoryAdapter(ArrayList<History> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public HistoryAdapter.HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_item, parent, false);
        HistoryViewHolder holder = new HistoryViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final HistoryViewHolder holder, final int position) {
        History customer = list.get(position);

        DecimalFormat decimalFormat = new DecimalFormat("###,###,###,###");
        decimalFormat.setRoundingMode(RoundingMode.UP);

        holder.textView_tengiong.setText(customer.getTenGiongLua());
        holder.textView_dongia.setText(decimalFormat.format(customer.getDonGia()));
        holder.textView_trubi.setText("" + customer.getBaoBi());
        holder.textView_sobao.setText("" + customer.getSoBao());
        holder.textView_sokg.setText("" + customer.getTongSoKG());
        holder.textView_tiencoc.setText(decimalFormat.format(customer.getTienCoc()));
        holder.textView_thanhtien.setText(decimalFormat.format(customer.getThanhTien()));
        holder.textView_date.setText(customer.getTimestamp());

        holder.ic_more_menu_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(context, "hello", Toast.LENGTH_SHORT).show();
                PopupMenu popup = new PopupMenu(context, holder.ic_more_menu_history);
                popup.getMenuInflater().inflate(R.menu.pop_up_history, popup.getMenu());
                popup.show();

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        //Toast.makeText(context, "" + item.getTitle(), Toast.LENGTH_SHORT).show();
                        switch (item.getItemId()) {
                            case R.id.menuEdit:
                                Intent intent = new Intent(context, PreviewActivity.class);
                                intent.putExtra("dateJoin", list.get(position).getDateJoin());
                                intent.putExtra("dateCreate", list.get(position).getTimestamp());
                                intent.putExtra("name", list.get(position).getHoTen());
                                intent.putExtra("phone", list.get(position).getSDT());
                                context.startActivity(intent);
                                ((HistoryActivity) context).overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
                                return true;
                            case R.id.menuDel:
                                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                builder.setMessage("Bạn có chắn chắc muốn xóa không?");
                                builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        deleteData(position);
                                        Toast.makeText(context, "Đã xóa thành công!", Toast.LENGTH_SHORT).show();
                                        notifyItemRemoved(position);
                                        notifyDataSetChanged();
                                    }
                                });

                                builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });
                                builder.show();
                                return true;
                            default:
                                throw new IllegalStateException("Unexpected value: " + item.getItemId());
                        }
                    }
                });
            }
        });

        holder.ic_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.section_expand.setVisibility(View.VISIBLE);
                holder.ic_more.setVisibility(View.GONE);
            }
        });

        holder.ic_less.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.section_expand.setVisibility(View.GONE);
                holder.ic_more.setVisibility(View.VISIBLE);
            }
        });

        holder.itemView.setTag(list.get(position).getID());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void deleteData(int position) {
        DatabaseHelper helper = new DatabaseHelper(context, DatabaseHelper.DATABASE_NAME, null, DatabaseHelper.DATABASE_VERSION);
        SQLiteDatabase db = helper.getWritableDatabase();
        String DELETE_HISTORY = "DELETE FROM " + DatabaseContract.HistoryTable.TABLE_NAME +
                " WHERE " + DatabaseContract.HistoryTable.COLUMN_TIMESTAMP + " LIKE '%" + list.get(position).getTimestamp() + "%'" +
                " AND " + DatabaseContract.HistoryTable.COLUMN_TENKH + " LIKE '%" + list.get(position).getHoTen() + "%'" +
                " AND " + DatabaseContract.HistoryTable._ID + " LIKE '%" + list.get(position).getID() + "%';";
        db.execSQL(DELETE_HISTORY);
        //db.delete(DatabaseContract.CustomerTable.TABLE_NAME, DatabaseContract.CustomerTable._ID + "=" + position, null);
    }

    public static class HistoryViewHolder extends RecyclerView.ViewHolder {
        public TextView textView_tengiong;
        public TextView textView_dongia;
        public TextView textView_trubi;
        public TextView textView_sobao;
        public TextView textView_sokg;
        public TextView textView_tiencoc;
        public TextView textView_thanhtien;
        public TextView textView_date;
        public ImageView ic_more_menu_history;
        public ImageView ic_more;
        public ImageView ic_less;
        CardView cardView;
        ConstraintLayout section_expand;

        public HistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            textView_tengiong = itemView.findViewById(R.id.history_item_tengiong_data);
            textView_dongia = itemView.findViewById(R.id.history_item_dongia_data);
            textView_trubi = itemView.findViewById(R.id.history_item_trubi_data);
            textView_sobao = itemView.findViewById(R.id.history_item_sobao_data);
            textView_sokg = itemView.findViewById(R.id.history_item_sokg_data);
            textView_tiencoc = itemView.findViewById(R.id.history_item_tiencoc_data);
            textView_thanhtien = itemView.findViewById(R.id.history_item_thanhtien_data);
            textView_date = itemView.findViewById(R.id.history_item_date_lb);
            ic_more_menu_history = itemView.findViewById(R.id.ic_more_menu_history);
            ic_more = itemView.findViewById(R.id.ic_expand_more);
            ic_less = itemView.findViewById(R.id.ic_expand_less);
            cardView = itemView.findViewById(R.id.home_item_card);
            section_expand = itemView.findViewById(R.id.expand_section);
        }
    }

    private void getHistoryAll() {
        DatabaseHelper helper = new DatabaseHelper(context, DatabaseHelper.DATABASE_NAME, null, DatabaseHelper.DATABASE_VERSION);
        SQLiteDatabase sqLiteDatabase = helper.getReadableDatabase();

        ArrayList<History> arraylist = new ArrayList<>();
        String GET_ALL_DATA = /*"SELECT * FROM " + DatabaseContract.HistoryTable.TABLE_NAME +
                " WHERE " + DatabaseContract.HistoryTable.COLUMN_TENKH + " LIKE '%" + DatabaseContract.HistoryTable.COLUMN_TENKH + "%'" +
                " AND " + DatabaseContract.HistoryTable.COLUMN_DATEJOIN + " LIKE '%" + DatabaseContract.HistoryTable.COLUMN_DATEJOIN + "%'" +
                " AND " + DatabaseContract.HistoryTable.COLUMN_SDT + " LIKE '%" + DatabaseContract.HistoryTable.COLUMN_SDT + "%'" +
                " ORDER BY " + DatabaseContract.HistoryTable.COLUMN_TIMESTAMP + " DESC;";*/
                "SELECT * FROM " + DatabaseContract.HistoryTable.TABLE_NAME +
                        " ORDER BY " + DatabaseContract.HistoryTable._ID + " DESC;";
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
                arraylist.add(history);
            } while (cursor.moveToNext());
        }

        //đưa dữ liệu vào customerlist và gọi adapter
        list.clear();
        list.addAll(arraylist);
        notifyDataSetChanged();
        cursor.close();
    }



}
