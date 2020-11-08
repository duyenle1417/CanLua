package com.example.test;

import android.content.Context;
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
    public void onBindViewHolder(@NonNull final CustomerViewHolder holder, int position) {
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
                        Toast.makeText(context, "" + item.getTitle(), Toast.LENGTH_SHORT).show();
                        return true;
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
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
