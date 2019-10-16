package com.mycheckins.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.mycheckins.R;
import com.mycheckins.interfac.OnRecyclerViewItemClickListener;
import com.mycheckins.model.CheckListBean;

import java.util.List;


public class CheckListAdapter extends RecyclerView.Adapter<CheckListAdapter.ViewHolder> {

    List<CheckListBean> ListBean;
    Context context;
    private OnRecyclerViewItemClickListener onRecyclerViewItemClickListener;


    public CheckListAdapter(Context context, List<CheckListBean> checkListBeans, OnRecyclerViewItemClickListener clickListenert) {
        this.context = context;
        this.ListBean = checkListBeans;
        onRecyclerViewItemClickListener=clickListenert;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.costum_leave_view, parent, false));

    }

    @Override
    public void onBindViewHolder( ViewHolder holder,  int position) {
        holder.setData(position);

    }

    @Override
    public int getItemCount() {
        return ListBean.size();

    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView mTitle;
        TextView mDate;
        TextView mDetails;
        TextView mLocation;


        ViewHolder( View view) {
            super(view);
            view.setOnClickListener(this);
            mTitle = (TextView) view.findViewById(R.id.tv_title);
            mDate = (TextView) view.findViewById(R.id.tv_date);
            mDetails = (TextView) view.findViewById(R.id.tv_details);
            mLocation = (TextView) view.findViewById(R.id.tv_locations);

        }

        public void setData(int position) {

            mTitle.setText("Title : "+ListBean.get(position).title);
            mDate.setText("Date : "+ListBean.get(position).data_time);
            mDetails.setText("Details : "+ListBean.get(position).detail);
            mLocation.setText("Place : "+ListBean.get(position).place);




        }
        @Override
        public void onClick(View view) {
            onRecyclerViewItemClickListener.onItemClick(CheckListAdapter.this, view, getLayoutPosition());
        }
    }
}
