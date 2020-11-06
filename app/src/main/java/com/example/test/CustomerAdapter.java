package com.example.test;

import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CustomerAdapter extends RecyclerView.Adapter<CustomerAdapter.CustomerViewHolder> {

    ArrayList<Customer> list;

    public CustomerAdapter(ArrayList<Customer> obj) {
        list = obj;
    }

    @NonNull
    @Override
    public CustomerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_item, parent, false);
        CustomerViewHolder holder = new CustomerViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomerViewHolder holder, int position) {
        Customer customer = list.get(position);

        holder.textView_name.setText(customer.getHoTen());
        holder.textView_phone.setText(customer.getSDT());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class CustomerViewHolder extends RecyclerView.ViewHolder {
        public TextView textView_name;
        public TextView textView_phone;

        public CustomerViewHolder(@NonNull View itemView) {
            super(itemView);
            textView_name = itemView.findViewById(R.id.home_item_name_tv);
            textView_phone = itemView.findViewById(R.id.home_item_phone_tv);
        }
    }
}
