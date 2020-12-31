package com.example.test;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class SortAdapter extends BaseAdapter {
    Context context;
    List<Sort> SortList;
    LayoutInflater inflater;

    public SortAdapter(Context context, List<Sort> sortList) {
        this.context = context;
        this.SortList = sortList;
    }

    @Override
    public int getCount() {
        return Math.max(SortList.size(), 0);
    }

    @Override
    public Object getItem(int position) {
        return SortList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return CustomView(position, convertView, parent, false);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return CustomView(position, convertView, parent,true);
    }

    public  View CustomView(int position, View convertView, ViewGroup parent, boolean IsDropDown) {
         ViewHolder holder = new ViewHolder();
         if(convertView == null) {
             inflater = LayoutInflater.from(context);
             convertView = inflater.inflate(R.layout.spinner_sapxep, parent, false);
             holder.tv_option = convertView.findViewById(R.id.option_item);
             convertView.setTag(holder);
         } else {
             holder = (ViewHolder) convertView.getTag();
         }
         holder.tv_option.setText(SortList.get(position).getOption());

         return convertView;
    }

    class ViewHolder{
        TextView tv_option;
    }
}
