package com.example.test;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> {
    ArrayList<History> list;
    Context context;

    public HistoryAdapter(Context context, ArrayList<History> obj) {
        list = obj;
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

        holder.textView_tengiong.setText(customer.getTenGiongLua());
        holder.textView_dongia.setText("" + customer.getDonGia());
        holder.textView_trubi.setText("" + customer.getBaoBi());
        holder.textView_sobao.setText("" + customer.getSoBao());
        holder.textView_sokg.setText("" + customer.getTongSoKG());
        holder.textView_tiencoc.setText("" + customer.getTienCoc());
        holder.textView_thanhtien.setText("" + customer.getThanhTien());
        holder.textView_date.setText(customer.getTimestamp());

        holder.ic_more_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(context, holder.ic_more_menu);
                popup.getMenuInflater().inflate(R.menu.pop_up, popup.getMenu());
                popup.show();

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        //Toast.makeText(context, "" + item.getTitle(), Toast.LENGTH_SHORT).show();
                        switch (item.getItemId()) {
                            case R.id.menuView:
                                Intent intent = new Intent(context, HistoryActivity.class);
                                intent.putExtra("name", list.get(position).getHoTen());
                                intent.putExtra("phone", list.get(position).getSDT());
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                context.startActivity(intent);
                                //HomeActivity.this.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
                                return true;
                            case R.id.menuEdit:
                                break;
                            case R.id.menuDel:
                                deleteData(position);
                                Log.e("delete TEST", "item ######");
                                ((HistoryActivity) context).finish();
                                context.startActivity(((HistoryActivity) context).getIntent());
                                ((HistoryActivity) context).overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
                                return true;
                            default:
                                throw new IllegalStateException("Unexpected value: " + item.getItemId());
                        }
                        return true;
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
        public ImageView ic_more_menu;
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
            ic_more_menu = itemView.findViewById(R.id.ic_more_menu_history);
            ic_more = itemView.findViewById(R.id.ic_expand_more);
            ic_less = itemView.findViewById(R.id.ic_expand_less);
            cardView = itemView.findViewById(R.id.home_item_card);
            section_expand = itemView.findViewById(R.id.expand_section);
        }
    }
}
