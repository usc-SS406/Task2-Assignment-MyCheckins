package com.mycheckins.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mycheckins.DeleteRecordActivity;
import com.mycheckins.R;
import com.mycheckins.adapter.CheckListAdapter;
import com.mycheckins.database.DatabaseHelper;
import com.mycheckins.interfac.OnRecyclerViewItemClickListener;
import com.mycheckins.model.CheckListBean;

import java.util.ArrayList;
import java.util.List;


public class ListUIFragment extends Fragment implements OnRecyclerViewItemClickListener {


	private Activity mActivity;
	private List<CheckListBean> mCheckinBeanList = null;
	private Context context;
	private RecyclerView mRecyclerView;
	private DatabaseHelper databaseHelper;
	private TextView mNorecordFoundTV;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_checklist, container, false);
		return rootView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		mActivity=getActivity();
		initViews();
		initVariables();
	}


	protected void initViews() {
		View view=getView();
		mRecyclerView = (RecyclerView) view.findViewById(R.id.rv_checklist);
		mNorecordFoundTV=(TextView)view.findViewById(R.id.tv_norecordfound);
		//view.findViewById(R.id.iv_header_left).setOnClickListener(this);
		databaseHelper=new DatabaseHelper(getActivity());
		LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mActivity);
		mRecyclerView.setLayoutManager(linearLayoutManager);

		mCheckinBeanList=new ArrayList<>();
		try {

			Cursor cursor= databaseHelper.getAllData();
			if(cursor!=null && cursor.getCount()>0) {
				mNorecordFoundTV.setVisibility(View.GONE);
				for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
					CheckListBean bean=new CheckListBean();
					bean.title=cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_TITLE));
					bean.place=cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_PLACE));
					bean.detail=cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_DETAILS));
					bean.data_time=cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_DATE));
					bean.latlong=cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_LATLONG));
					bean.id=cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_ID));
					mCheckinBeanList.add(bean);
				}
			}
			else
				mNorecordFoundTV.setVisibility(View.VISIBLE);

		} catch (Exception e) {
			e.printStackTrace();
		}

		CheckListAdapter customAdapter = new CheckListAdapter(mActivity,mCheckinBeanList,this);
		mRecyclerView.setAdapter(customAdapter);

	}


	protected void initVariables() {

	}



	@Override
	public void onItemClick(RecyclerView.Adapter adapter, View v, int position)
	{
		CheckListBean listBean=mCheckinBeanList.get(position);
		Intent intent=new Intent(getActivity(), DeleteRecordActivity.class);
		intent.putExtra("title",listBean.title);
		intent.putExtra("date",listBean.data_time);
		intent.putExtra("place",listBean.place);
		intent.putExtra("latlon",listBean.latlong);
		intent.putExtra("details",listBean.detail);
		intent.putExtra("id",listBean.id);
		startActivity(intent);

	}
}
