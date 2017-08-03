package com.txls.txlashou.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.txls.txlashou.R;
import com.txls.txlashou.wight.MyRecylerViewAdapter;
import com.txls.txlashou.wight.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.txls.txlashou.R.id.img_return;


/**
 * 作者：YHL
 * 时间： 2017/5/19 10:55
 */

public class MessageCenter extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private List<String> mDatas;
    private MyRecylerViewAdapter adapter;
    private SwipeRefreshLayout mRefreshLayout;
    private ImageView imgReturn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 初始化视图
        initView();
        // 初始化数据
        initData();
        // 下拉刷新
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                mDatas.clear();
                for (int i = 0; i <= 50; i++) {
                    mDatas.add("item---" + new Random().nextInt(30));
                }
                adapter.notifyDataSetChanged();
                mRefreshLayout.setRefreshing(false);
            }
        });
        adapter = new MyRecylerViewAdapter(this, mDatas);
        // item点击事件
        adapter.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(MessageCenter.this, position + "", 1000).show();
                Intent messageDetails = new Intent(MessageCenter.this,MessageDetailsActivity.class);
                startActivity(messageDetails);
            }
        });
        // 给RecyclerView绑定适配器
        mRecyclerView.setAdapter(adapter);
        // 添加分割线
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        // 添加和移除item时候的动态效果
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        // 布局管理器
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        // 上拉自动加载
        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int last = manager.findLastCompletelyVisibleItemPosition();
                int totalCount = manager.getItemCount();
                // last >= totalCount - 2表示剩余2个item是自动加载，可自己设置
                // dy>0表示向下滑动
                if (last >= totalCount - 2 && dy > 0) {

					/*
					 * 加载数据
					 */
                    List<String> datas = new ArrayList<String>();
                    for (int i = 0; i <= 10; i++) {
                        datas.add("load" + new Random().nextInt(10));
                    }
                    //addData()是在自定义的Adapter中自己添加的方法，用来给list添加数据
                    adapter.addData(datas);
                }
            }
        });
        imgReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    /*
     * 初始化视图
     */
    private void initView() {
        setContentView(R.layout.activity_message_center);
        mRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        mRecyclerView = (RecyclerView) findViewById(R.id.message_recycler);
        imgReturn = (ImageView) findViewById(img_return);
    }

    /*
     * 初始化数据
     */
    private void initData() {
        mDatas = new ArrayList<String>();
        for (int i = 0; i <= 50; i++) {
            mDatas.add("item---" + new Random().nextInt(30));
        }
    }


}
