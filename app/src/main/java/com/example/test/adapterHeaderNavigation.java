package com.example.test;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class adapterHeaderNavigation extends BaseAdapter {
    private Context context;
    private int layout;
    private ArrayList<HeaderNavigation> arrayList;

    public adapterHeaderNavigation(Context context, int layout, ArrayList<HeaderNavigation> arrayList) {
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
        TextView headerName, headerVersion;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(layout, null);
            viewHolder = new ViewHolder();

            viewHolder.headerName = (TextView) convertView.findViewById(R.id.tvCanLua);
            viewHolder.headerVersion = (TextView) convertView.findViewById(R.id.tvVersion);

            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.headerName.setText(arrayList.get(position).getHeaderName());
        viewHolder.headerVersion.setText(arrayList.get(position).getHeaderVersion());
        return convertView;
    }
}
