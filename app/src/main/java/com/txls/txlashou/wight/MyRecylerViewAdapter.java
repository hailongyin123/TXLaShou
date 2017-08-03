package com.txls.txlashou.wight;

import android.content.Context;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.txls.txlashou.R;

import java.util.List;

public class MyRecylerViewAdapter extends Adapter<MyViewHolder> {
	private Context mContext;
	private List<String> mDatas;
	private OnItemClickListener mClickListener;

	//添加数据集合
	public void addData(List<String> datas) {
		mDatas.addAll(datas);
		notifyDataSetChanged();
	}

	public MyRecylerViewAdapter(Context context, List<String> datas) {
		this.mContext = context;
		this.mDatas = datas;
	}

	@Override
	public int getItemCount() {
		return mDatas.size();
	}

	@Override
	public void onBindViewHolder(MyViewHolder arg0, int arg1) {
		final int pos = arg1;
		arg0.textViewTime.setText(mDatas.get(arg1)+"");
		arg0.textViewTitle.setText(mDatas.get(arg1)+"");
		// arg0.rootView.setOnClickListener(new OnClickListener() {
		// @Override
		// public void onClick(View v) {
		// Toast.makeText(mContext, pos + "", 1000).show();
		// }
		// });
	}

	@Override
	public MyViewHolder onCreateViewHolder(ViewGroup arg0, int arg1) {
		View view = LayoutInflater.from(mContext).inflate(R.layout.message_center_item, arg0, false);
		MyViewHolder holder = new MyViewHolder(view, mClickListener);
		return holder;
	}

	public void setOnItemClickListener(OnItemClickListener listener) {
		this.mClickListener = listener;
	}
}
