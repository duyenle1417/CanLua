package com.example.test;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CustomerAdapter extends RecyclerView.Adapter<CustomerAdapter.CustomerViewHolder> {

    ArrayList<History> list;

    public CustomerAdapter(ArrayList<History> obj) {
        list = obj;
    }

    @NonNull
    @Override
    public CustomerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomerViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
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
