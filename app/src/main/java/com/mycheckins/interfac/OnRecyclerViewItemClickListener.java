package com.mycheckins.interfac;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;


public interface OnRecyclerViewItemClickListener {
    void onItemClick(RecyclerView.Adapter adapter, View v, int position);
}
