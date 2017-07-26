package com.yyl.view.base;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yyl.view.R;

import java.util.ArrayList;

/**
 * Created by yuyunlong on 2017/7/12/012.
 */

public class AdapterDemo extends RecyclerView.Adapter<AdapterDemo.ViewHolder> {
    private ArrayList<String> arrayList = new ArrayList<>();

    public AdapterDemo() {
        init();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_base_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.textView.setText(arrayList.get(position));
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }


    public void init() {
        for (int i = 0; i < 50; i++) {
            arrayList.add("position =" + i);
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;

        public ViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.textView2);
        }


    }

}
