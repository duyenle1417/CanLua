package com.example.test;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class menuAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private ArrayList<itemMenu> arrayList;

    public menuAdapter(Context context, int layout, ArrayList<itemMenu> arrayList) {
        this.context = context;
        this.layout = layout;
        this.arrayList = arrayList;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    private class ViewHolder{
        TextView tv;
        ImageView img;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(layout, null);
            viewHolder = new ViewHolder();

            viewHolder.tv = (TextView) convertView.findViewById(R.id.tvicon);
            viewHolder.img = (ImageView) convertView.findViewById(R.id.imgicon);

            convertView.setTag(viewHolder);
        } else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tv.setText(arrayList.get(position).getNameItem());
        viewHolder.img.setImageResource(arrayList.get(position).getIcon());
        return convertView;
    }
}
