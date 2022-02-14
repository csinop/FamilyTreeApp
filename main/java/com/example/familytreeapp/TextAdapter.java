package com.example.familytreeapp;

import static java.lang.String.valueOf;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

public class TextAdapter<T> extends BaseAdapter {

    final List<T> dataList = new ArrayList<>();
    private boolean[] selected;

    public void setDataList(List<T> data){
        if(data != null){
            this.dataList.clear();
            if(data.size() > 0){
                this.dataList.addAll(data);
            }
            notifyDataSetChanged();
        }
    }

    public void setSelected(boolean[] selected){
        if(selected != null){
            this.selected = new boolean[selected.length];
            System.arraycopy(selected, 0, this.selected, 0, selected.length);
            notifyDataSetChanged();
        }
    }
    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public T getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
            convertView.setTag(new ViewHolder(convertView.findViewById(R.id.textItem)));
        }
        ViewHolder holder = (ViewHolder) convertView.getTag();
        final T item = getItem(position);

        holder.getInfo().setText(valueOf(item).substring(valueOf(item).lastIndexOf('/')+1));

        if(selected != null && selected[position]){
            holder.getInfo().setBackgroundColor(Color.GRAY);
        }else{
            holder.getInfo().setBackgroundColor(Color.WHITE);
        }
        return convertView;
    }
}
