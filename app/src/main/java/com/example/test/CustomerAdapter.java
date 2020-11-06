package com.example.test;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CustomerAdapter extends RecyclerView.Adapter<CustomerAdapter.CustomerViewHolder> {

    ArrayList<Customer> list;
    private static RecycleViewItemOnClick recycleViewItemOnClick;

    public CustomerAdapter(ArrayList<Customer> obj, RecycleViewItemOnClick recycleViewItemOnClick) {
        list = obj;
        this.recycleViewItemOnClick = recycleViewItemOnClick;
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
