package com.txls.txlashou.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;


/**
 * @author YHL
 * @version V1.0
 * @Description: 封装ListAdapter
 * @date 2017/1/23 15:29:56
 */
public abstract class BaseListAdapter<T, Holder extends BaseListAdapter.ViewHolder> extends BaseAdapter {
    private ArrayList<T> mList = new ArrayList<T>();


    public void addList(List<T> list) {
        if (list != null) {
            mList.addAll(list);
        }
    }

    public void clear() {
        mList.clear();
    }

    public void add(T data) {
        if (data != null) {
            mList.add(data);
        }
    }

    public void remove(T data) {
        mList.remove(data);
    }

    public void remove(int position) {
        mList.remove(position);
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public T getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    @SuppressWarnings("unchecked")
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder;
        if (convertView == null) {
            holder = onCreateViewHolder(parent, position);
            convertView = holder.itemView;
            convertView.setTag(holder);
        } else {    
            holder = (Holder) convertView.getTag();
        }
        onBindViewHolder(holder, position);
        return convertView;
    }

    public static class ViewHolder {
        View itemView;

        public View findViewByID(int id){
            return itemView.findViewById(id);
        }

        public ViewHolder(View itemView) {
            this.itemView = itemView;
        }
    }

    /**
     * 创建ViewHolder
     *
     * @param parent   父布局
     * @param position 当前位置
     * @return Viewholder
     */
    protected abstract Holder onCreateViewHolder(ViewGroup parent, int position);

    /**
     * 绑定数据
     *
     * @param viewHolder 当前viewHolder
     * @param position   当前位置
     */
    protected abstract void onBindViewHolder(Holder viewHolder, int position);
}
