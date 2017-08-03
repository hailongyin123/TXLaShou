package com.txls.txlashou.home;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.txls.txlashou.R;

import java.util.ArrayList;

/**
 * 首次启动引导页
 */
public class GuideActivity extends SuperActivity {
    private ViewPager viewPager;
    /**
     * 装分页显示的view的数组
     */
    private ArrayList<View> pageViews;
    //包裹滑动图片的LinearLayout
    private ViewGroup viewPics;
    private final int SPLASH_DISPLAY_LENGHT = 3000; // 延迟3秒
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private TextView start_app;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        preferences = getSharedPreferences("phone", Context.MODE_PRIVATE);

        if (preferences.getBoolean("firststart", true)) {
            editor = preferences.edit();
            // 将登录标志位设置为false，下次登录时不再显示引导页面
            editor.putBoolean("firststart", false);
            editor.commit();
        } else {
            Intent intent = new Intent();
            intent.setClass(GuideActivity.this, WelcomeActivity.class);
            GuideActivity.this.startActivity(intent);
            GuideActivity.this.finish();

        }

    }

    @Override
    public void initView() {
        //将要分页显示的View装入数组中
        LayoutInflater inflater = getLayoutInflater();
        pageViews = new ArrayList<View>();
        pageViews.add(inflater.inflate(R.layout.viewpager_one, null));
        pageViews.add(inflater.inflate(R.layout.viewpager_two, null));
        pageViews.add(inflater.inflate(R.layout.viewpager_three, null));

        //从指定的XML文件加载视图
        viewPics = (ViewGroup) inflater.inflate(R.layout.activity_guide, null);
        viewPager = (ViewPager) viewPics.findViewById(R.id.guidePages);
        start_app = (TextView) viewPics.findViewById(R.id.start_app);
        setContentView(viewPics);
        //设置viewpager的适配器和监听事件
        viewPager.setAdapter(new GuidePageAdapter());
        viewPager.setOnPageChangeListener(new GuidePageChangeListener());
        start_app.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.start_app:
                Intent mIntent = new Intent();
                mIntent.setClass(GuideActivity.this, WelcomeActivity.class);
                GuideActivity.this.startActivity(mIntent);
                GuideActivity.this.finish();
                break;
        }
    }
    class GuidePageAdapter extends PagerAdapter {
        //销毁position位置的界面
        @Override
        public void destroyItem(View v, int position, Object arg2) {
            // TODO Auto-generated method stub
            ((ViewPager)v).removeView(pageViews.get(position));

        }
        @Override
        public void finishUpdate(View arg0) {
            // TODO Auto-generated method stub

        }
        //获取当前窗体界面数
        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return pageViews.size();
        }

        //初始化position位置的界面
        @Override
        public Object instantiateItem(View v, int position) {
            // TODO Auto-generated method stub
            ((ViewPager) v).addView(pageViews.get(position));
            return pageViews.get(position);
        }
        // 判断是否由对象生成界面
        @Override
        public boolean isViewFromObject(View v, Object arg1) {
            // TODO Auto-generated method stub
            return v == arg1;
        }
        @Override
        public void startUpdate(View arg0) {
            // TODO Auto-generated method stub
        }

        @Override
        public int getItemPosition(Object object) {
            // TODO Auto-generated method stub
            return super.getItemPosition(object);
        }

        @Override
        public void restoreState(Parcelable arg0, ClassLoader arg1) {
            // TODO Auto-generated method stub
        }

        @Override
        public Parcelable saveState() {
            // TODO Auto-generated method stub
            return null;
        }
    }

    class GuidePageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onPageSelected(int position) {
            // TODO Auto-generated method stub
            if (position == 2){
                start_app.setVisibility(View.VISIBLE);
            }else{
                start_app.setVisibility(View.GONE);
            }
        }
    }
}
