package com.example.test;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
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
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

public class CustomerAdapter extends RecyclerView.Adapter<CustomerAdapter.CustomerViewHolder> implements Filterable {

    private static RecycleViewItemOnClick recycleViewItemOnClick;
    private FragmentManager fragmentManager;
    Context context;
    List<Customer> list;
    List<Customer> SearchList;
    private EditText tenKH;
    private EditText sdtKH;

    public CustomerAdapter(Context context, List<Customer> list, RecycleViewItemOnClick recycleViewItemOnClick) {
        this.context = context;
        this.list = list;
        this.SearchList = list;
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
        final Customer customer = list.get(position);
        //databaseHelper = new DatabaseHelper(context, "canlua.db", null, 2);

        holder.textView_name.setText(customer.getHoTen());
        holder.textView_phone.setText(customer.getSDT());
        holder.textView_date.setText(customer.getDate());
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
                                intent.putExtra("date", list.get(position).getDate());
                                intent.putExtra("id", list.get(position).getID());
                                context.startActivity(intent);
                                ((HomeActivity) context).overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
                                return true;
                            case R.id.menuEdit:
                                final AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
                                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                View view = inflater.inflate(R.layout.dialog_edit_customer, null);

                                tenKH = (EditText) view.findViewById(R.id.edittext_tenKH_dialogedit);
                                sdtKH = (EditText) view.findViewById(R.id.edittext_sdt_dialogedit);;

                                final String ten = customer.getHoTen();
                                String sdt = customer.getSDT();

                                tenKH.setText(ten);
                                if(sdt.equals("---")){
                                    sdtKH.setText("");
                                }else
                                    sdtKH.setText(sdt);

                                builder1.setView(view)
                                        .setTitle("Sửa thông tin")
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                updateData(position);
                                                Toast.makeText(context, "Đã sửa thành công!", Toast.LENGTH_SHORT).show();
                                                getAll();
                                            }
                                        })
                                        .setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                            }
                                        });
                                builder1.show();
                                return true;
                            case R.id.menuDel:
                                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                builder.setMessage("Bạn có chắn chắc muốn xóa " + list.get(position).getHoTen() + " không?");
                                builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        deleteData(position);
                                        Toast.makeText(context, "Đã xóa thành công!" , Toast.LENGTH_SHORT).show();
                                        getAll();
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
                        //return true;
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
    }

   @Override
    public Filter getFilter() {
        return SearchFilter;
    }

    private Filter SearchFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<Customer> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(SearchList);
            }else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (Customer item : SearchList) {
                    if (item.getHoTen().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            list.clear();
            list.addAll((Collection<? extends Customer>) results.values);
            notifyDataSetChanged();
        }
    };

    public static class CustomerViewHolder extends RecyclerView.ViewHolder {
        public TextView textView_name;
        public TextView textView_phone;
        public TextView textView_date;
        public ImageView ic_more_menu;
        CardView cardView;

        public CustomerViewHolder(@NonNull View itemView) {
            super(itemView);
            textView_name = itemView.findViewById(R.id.home_item_name_tv);
            textView_phone = itemView.findViewById(R.id.home_item_phone_tv);
            textView_date = itemView.findViewById(R.id.home_item_date);
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

    public void updateData(int position) {
        DatabaseHelper helper = new DatabaseHelper(context, DatabaseHelper.DATABASE_NAME, null, DatabaseHelper.DATABASE_VERSION);
        SQLiteDatabase db = helper.getWritableDatabase();
        String UPDATE_CUSTOMER = "UPDATE " + DatabaseContract.CustomerTable.TABLE_NAME + " SET " + DatabaseContract.CustomerTable.COLUMN_TENKH + " = '" + tenKH.getText().toString() +"'"
                        + " , " + DatabaseContract.CustomerTable.COLUMN_SDT + " = '"+ sdtKH.getText().toString() +"'"
                       + " WHERE " + DatabaseContract.CustomerTable._ID + " = '" + list.get(position).getID() + "';";
        db.execSQL(UPDATE_CUSTOMER);
        Toast.makeText(context, "Đã sửa thành công", Toast.LENGTH_SHORT).show();
    }

    private void getAll() {
        DatabaseHelper helper = new DatabaseHelper(context, DatabaseHelper.DATABASE_NAME, null, DatabaseHelper.DATABASE_VERSION);
        SQLiteDatabase sqLiteDatabase = helper.getReadableDatabase();

        ArrayList<Customer> arrayList = new ArrayList<>();
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
                arrayList.add(customer);
            } while (cursor.moveToNext());
        }
        list.clear();
        list.addAll(arrayList);
        notifyDataSetChanged();
        cursor.close();

    }

}

