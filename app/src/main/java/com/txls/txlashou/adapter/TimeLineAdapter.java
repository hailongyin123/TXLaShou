package com.txls.txlashou.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.txls.txlashou.R;
import com.txls.txlashou.bean.BillListBean;

import java.util.List;
import java.util.Map;

/**
 * 作者：YHL
 * 时间： 2017/2/7 10:03
 */

public class TimeLineAdapter extends BaseAdapter {
    private Context context;
    private List<BillListBean.ResponseBean.RepayPlanBean> list;
    private LayoutInflater inflater;

    public TimeLineAdapter(Context context, List<BillListBean.ResponseBean.RepayPlanBean> list) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (viewHolder == null) {
            inflater = LayoutInflater.from(parent.getContext());
            convertView = inflater.inflate(R.layout.repayment_list_item, null);
            viewHolder = new ViewHolder();
            viewHolder.topview = convertView.findViewById(R.id.view_top);
            viewHolder.bottomview = convertView.findViewById(R.id.view_bottom);
            viewHolder.tv_list_time = (TextView) convertView.findViewById(R.id.tv_list_time);
            viewHolder.iv_time_line_red = (ImageView) convertView.findViewById(R.id.iv_time_line_red);
            viewHolder.repayment_number = (TextView) convertView.findViewById(R.id.repayment_number);
            viewHolder.repayment_date = (TextView) convertView.findViewById(R.id.repayment_date);
            viewHolder.repayment_count = (TextView) convertView.findViewById(R.id.repayment_count);
            viewHolder.repayment = (TextView) convertView.findViewById(R.id.repayment);
            viewHolder.time_line = convertView.findViewById(R.id.view_bottom);
            viewHolder.repayment.setVisibility(View.INVISIBLE);
            convertView.setTag(viewHolder);
        } else {
            convertView.getTag();
        }
        if (position == 0) {
            viewHolder.topview .setVisibility(View.INVISIBLE);
            viewHolder.bottomview .setVisibility(View.VISIBLE);
        } else {
            viewHolder.topview.setVisibility(View.VISIBLE);
            viewHolder.bottomview .setVisibility(View.VISIBLE);
        }
        if (position == list.size() - 1) {
            viewHolder.bottomview .setVisibility(View.INVISIBLE);
        } else {
            viewHolder.bottomview .setVisibility(View.VISIBLE);
        }
        //添加数据
        viewHolder.tv_list_time.setText(list.get(position).getTimePoint());
        viewHolder.repayment_number.setText("¥" + list.get(position).getIssueRepayAmount() + "元");
        viewHolder.repayment_date.setText(list.get(position).getRepayTime());
        viewHolder.repayment_count.setText(list.get(position).getIssue());
        return convertView;
    }

    static class ViewHolder {
        public TextView tv_list_time;
        public ImageView iv_time_line_red;
        public TextView repayment_number;
        public TextView repayment_date;
        public TextView repayment_count;
        public TextView repayment;
        public View time_line;
        public View bottomview;
        public View topview;

    }
}
