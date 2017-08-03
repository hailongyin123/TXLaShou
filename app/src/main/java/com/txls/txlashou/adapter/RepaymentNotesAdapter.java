package com.txls.txlashou.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.txls.txlashou.R;
import com.txls.txlashou.bean.NotesBean;

import java.util.List;

/**
 * 作者：YHL
 * 时间： 2017/2/7 10:03
 */

public class RepaymentNotesAdapter extends BaseAdapter {
    private Context context;
    private List<NotesBean.ResponseBean.RepayRecordBean> list;
    private LayoutInflater inflater;

    public RepaymentNotesAdapter(Context context, List<NotesBean.ResponseBean.RepayRecordBean> list) {
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
            convertView = inflater.inflate(R.layout.repayment_notes_item,null);
            viewHolder = new ViewHolder();
            viewHolder.auto_pay = (TextView) convertView.findViewById(R.id.auto_pay);
            viewHolder.repayment_number = (TextView) convertView.findViewById(R.id.repayment_number);
            viewHolder.repayment_date = (TextView) convertView.findViewById(R.id.repayment_date);
            viewHolder.repayment = (TextView) convertView.findViewById(R.id.repayment);
            convertView.setTag(viewHolder);
        }else{
            convertView.getTag();
        }
        //添加数据
        if (list.get(position).getRepayType() == 1){
            viewHolder.auto_pay.setText("自动还款");
        }
        if (list.get(position).getRepayType() == 2){
            viewHolder.auto_pay.setText("手动还款");
        }
        viewHolder.repayment_number.setText(list.get(position).getRepayAmount()+"元");
        viewHolder.repayment_date.setText(list.get(position).getRepayTime()+"");
        viewHolder.repayment.setText("银行卡尾号"+list.get(position).getBankNo());
        return convertView;
    }

    static class ViewHolder {
        public TextView auto_pay;
        public TextView repayment_number;
        public TextView repayment_date;
        public TextView repayment;

    }
}
