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
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CustomerAdapter extends RecyclerView.Adapter<CustomerAdapter.CustomerViewHolder> {

    private static RecycleViewItemOnClick recycleViewItemOnClick;
    Context context;
    ArrayList<Customer> list;

    /*public CustomerAdapter(ArrayList<Customer> obj, RecycleViewItemOnClick recycleViewItemOnClick) {
        list = obj;
        this.recycleViewItemOnClick = recycleViewItemOnClick;
    }*/

    public CustomerAdapter(Context context, ArrayList<Customer> list, RecycleViewItemOnClick recycleViewItemOnClick) {
        this.context = context;
        this.list = list;
        CustomerAdapter.recycleViewItemOnClick = recycleViewItemOnClick;
    }

    @NonNull
    @Override
    public CustomerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_item, parent, false);
        CustomerViewHolder holder = new CustomerViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final CustomerViewHolder holder, final int position) {
        Customer customer = list.get(position);

        holder.textView_name.setText(customer.getHoTen());
        holder.textView_phone.setText(customer.getSDT());
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
                                //reset HomeActivity và lấy dữ liệu từ DB
                                ((HomeActivity) context).finish();
                                context.startActivity(((HomeActivity) context).getIntent());
                                ((HomeActivity) context).overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
                                return true;
                            default:
                                throw new IllegalStateException("Unexpected value: " + item.getItemId());
                        }
                        return true;
                    }
                });
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
        String DELETE_CUSTOMER = "DELETE FROM " + DatabaseContract.CustomerTable.TABLE_NAME +
                " WHERE " + DatabaseContract.CustomerTable.COLUMN_TIMESTAMP + " LIKE '%" + list.get(position).getDate() + "%'" +
                " AND " + DatabaseContract.CustomerTable.COLUMN_TENKH + " LIKE '%" + list.get(position).getHoTen() + "%'" +
                " AND " + DatabaseContract.CustomerTable._ID + " LIKE '%" + list.get(position).getID() + "%';";
        db.execSQL(DELETE_CUSTOMER);

        String DELETE_HISTORY = "DELETE FROM " + DatabaseContract.HistoryTable.TABLE_NAME +
                " WHERE " + DatabaseContract.HistoryTable.COLUMN_DATEJOIN + " LIKE '%" + list.get(position).getDate() + "%'" +
                " AND " + DatabaseContract.HistoryTable.COLUMN_TENKH + " LIKE '%" + list.get(position).getHoTen() + "%'" +
                " AND " + DatabaseContract.HistoryTable.COLUMN_SDT + " LIKE '%" + list.get(position).getSDT() + "%'";
        db.execSQL(DELETE_HISTORY);
    }

    public static class CustomerViewHolder extends RecyclerView.ViewHolder {
        public TextView textView_name;
        public TextView textView_phone;
        public ImageView ic_more_menu;
        CardView cardView;

        public CustomerViewHolder(@NonNull View itemView) {
            super(itemView);
            textView_name = itemView.findViewById(R.id.home_item_name_tv);
            textView_phone = itemView.findViewById(R.id.home_item_phone_tv);
            ic_more_menu = itemView.findViewById(R.id.ic_more_menu);
            cardView = itemView.findViewById(R.id.home_item_card);

            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    recycleViewItemOnClick.OnItemClicked(getAdapterPosition());
                }
            });
        }
    }
}
