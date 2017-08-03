package com.txls.txlashou.home;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.txls.txlashou.R;
import com.txls.txlashou.adapter.RepaymentNotesAdapter;
import com.txls.txlashou.bean.NotesBean;
import com.zwy.base.ZwyCommon;

import java.util.List;

public class RepaymentNotesActivity extends SuperActivity {
    //声明控件
    private ImageView img_return;
    private ListView listView;
    private List<NotesBean.ResponseBean.RepayRecordBean> data ;
    private NotesBean notesBean;
    private RepaymentNotesAdapter repaymentNotesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repayment_notes);
        notesBean = (NotesBean) getIntent().getSerializableExtra("notesData");
        if (getData() != null){
            repaymentNotesAdapter = new RepaymentNotesAdapter(this, getData());
            listView.setAdapter(repaymentNotesAdapter);
        }
    }

    private List<NotesBean.ResponseBean.RepayRecordBean> getData() {
        data = notesBean.getResponse().getRepayRecord();
        if(data != null){
            if (data.size() == 0){
                ZwyCommon.showToast(this,"还款记录数据为空");
                return null;
            }
        } else {
            ZwyCommon.showToast(this,"还款记录数据为空");
            return null;
        }
        return data;
    }

    @Override
    public void initView() {
        img_return = (ImageView) findViewById(R.id.img_return);
        listView = (ListView) findViewById(R.id.lv_repayment_notes);
        //监听
        img_return.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_return:
                finish();
                break;

        }

    }
}
