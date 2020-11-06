package com.example.test;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> {
    private static RecycleViewItemOnClick recycleViewItemOnClick;
    ArrayList<History> list;

    public HistoryAdapter(ArrayList<History> obj, RecycleViewItemOnClick recycleViewItemOnClick) {
        list = obj;
        this.recycleViewItemOnClick = recycleViewItemOnClick;
    }

    @NonNull
    @Override
    public HistoryAdapter.HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_item, parent, false);
        HistoryViewHolder holder = new HistoryViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder holder, int position) {
        History customer = list.get(position);

        holder.textView_tengiong.setText(customer.getTenGiongLua());
        holder.textView_dongia.setText(customer.getDonGia());
        holder.textView_trubi.setText(customer.getBaoBi());
        holder.textView_sobao.setText(customer.getSoBao());
        holder.textView_sokg.setText((int) customer.getTongSoKG());
        holder.textView_tiencoc.setText(customer.getTienCoc());
        holder.textView_thanhtien.setText(customer.getThanhTien());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class HistoryViewHolder extends RecyclerView.ViewHolder {
        public TextView textView_tengiong;
        public TextView textView_dongia;
        public TextView textView_trubi;
        public TextView textView_sobao;
        public TextView textView_sokg;
        public TextView textView_tiencoc;
        public TextView textView_thanhtien;
        CardView cardView;

        public HistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            textView_tengiong = itemView.findViewById(R.id.history_item_tengiong_data);
            textView_dongia = itemView.findViewById(R.id.history_item_dongia_data);
            textView_trubi = itemView.findViewById(R.id.history_item_trubi_data);
            textView_sobao = itemView.findViewById(R.id.history_item_sobao_data);
            textView_sokg = itemView.findViewById(R.id.history_item_sokg_data);
            textView_tiencoc = itemView.findViewById(R.id.history_item_tiencoc_data);
            textView_thanhtien = itemView.findViewById(R.id.history_item_thanhtien_data);
            /*cardView = itemView.findViewById(R.id.home_item_card);

            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    recycleViewItemOnClick.OnItemClicked(getAdapterPosition());
                }
            });*/
        }
    }
}
