package com.txls.txlashou.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.txls.txlashou.R;

import java.util.List;
import java.util.Map;

/**
 * 作者：YHL
 * 时间： 2017/2/8 13:59
 */

public class RepaymentNowAdapter extends BaseAdapter{
    private Context context;
    private List<Map<String, Object>> list;
    private LayoutInflater inflater;

    public RepaymentNowAdapter(Context context, List<Map<String, Object>> list) {
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
        if (viewHolder == null){
            inflater = LayoutInflater.from(parent.getContext());
            convertView = inflater.inflate(R.layout.repayment_now_item,null);
            viewHolder = new ViewHolder();
            viewHolder.repayment_time = (TextView) convertView.findViewById(R.id.repayment_time);
            viewHolder.cb_repayment = (CheckBox) convertView.findViewById(R.id.cb_repayment);
            viewHolder.repayment_number = (TextView) convertView.findViewById(R.id.repayment_number);
            viewHolder.repayment_date = (TextView) convertView.findViewById(R.id.repayment_date);
            viewHolder.repayment_count = (TextView) convertView.findViewById(R.id.repayment_count);
            viewHolder.repayment = (TextView) convertView.findViewById(R.id.repayment);
            convertView.setTag(viewHolder);
        }else{
            convertView.getTag();
        }
        return convertView;
    }

    static class ViewHolder {
        public CheckBox cb_repayment;
        public TextView repayment_time;
        public TextView repayment_number;
        public TextView repayment_date;
        public TextView repayment_count;
        public TextView repayment;

    }
}
