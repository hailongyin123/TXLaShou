package com.txls.txlashou.wight;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;

import com.txls.txlashou.R;
import com.txls.txlashou.wight.citypickview.CanShow;
import com.txls.txlashou.wight.citypickview.wheel.OnWheelChangedListener;
import com.txls.txlashou.wight.citypickview.wheel.WheelView;
import com.txls.txlashou.wight.citypickview.wheel.adaters.ArrayWheelAdapter;

import java.util.HashMap;
import java.util.Map;


/**
 * &#x4f5c;&#x8005;&#xff1a;zhangjk
 */
public class TimePickerView implements CanShow, OnWheelChangedListener {

    private Context context;


    public View popview;

    private WheelView mViewYear;

    private WheelView mViewMonth;

    private WheelView mViewDay;


    /**
     * 所有年
     */
    protected String[] mYearDatas;

    /**
     * key - 年 value - 月
     */
    protected Map<String, String[]> mMonthDatas = new HashMap<String, String[]>();

    /**
     * key - 月values - 日
     */
    protected Map<String, String[]> mDayDatas = new HashMap<String, String[]>();
    /**
     * 当前年的名称
     */
    protected String mCurrentYearName;

    /**
     * 当月的名称
     */
    protected String mCurrentMonthName;

    /**
     * 当前日的名称
     */
    protected String mCurrentDayName = "";

    private OnTimeItemClickListener listener;

    public interface OnTimeItemClickListener {
        void onSelected(String... TimeSelected);
    }

    public void setOnCityItemClickListener(OnTimeItemClickListener listener) {
        this.listener = listener;
    }

    /**
     * Default text color
     */
    public static final int DEFAULT_TEXT_COLOR = 0xFF585858;

    /**
     * Default text size
     */
    public static final int DEFAULT_TEXT_SIZE = 18;

    // Text settings
    private int textColor = DEFAULT_TEXT_COLOR;
    private int textSize = DEFAULT_TEXT_SIZE;

    /**
     * 滚轮显示的item个数
     */
    private static final int DEF_VISIBLE_ITEMS = 5;

    // Count of visible items
    private int visibleItems = DEF_VISIBLE_ITEMS;

    /**
     * 滚轮是否循环滚动
     */
    boolean isCyclic = true;

    public TimePickerView(final Context context) {
        super();
        this.context = context;
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        popview = layoutInflater.inflate(R.layout.pop_citypicker, null);

        mViewYear = (WheelView) popview.findViewById(R.id.id_province);
        mViewMonth = (WheelView) popview.findViewById(R.id.id_city);
        mViewDay = (WheelView) popview.findViewById(R.id.id_district);


        /**
         * 初始化时间数据
         */
        initTimeData();
        // 添加change事件
        mViewYear.addChangingListener(this);
        // 添加change事件
        mViewMonth.addChangingListener(this);
        // 添加change事件
        mViewDay.addChangingListener(this);

    }

    private void initTimeData() {
        /**
         * 初始化年
         */
        mYearDatas = new String[66];
        for (int y = 0; y < 66; y++) {
            int year = 2000 + y;
            mYearDatas[y] = year + "";
        }
        /**
         * 初始化月
         */
        for (int y = 0; y < mYearDatas.length; y++) {
            String month[] = new String[12];
            for (int m = 0; m < 12; m++) {
                month[m] = (m + 1) + "";
            }
            mMonthDatas.put(mYearDatas[y], month);
        }
        /**
         * 初始化日
         */
        for (int i = 0; i < mYearDatas.length; i++) {
            int year = i + 2000;
            if (year % 4 == 0 && year % 100 != 0) {
                for (int j = 0; j < 12; j++) {
                    int month = j + 1;
                    if (month == 1 || month == 3 || month == 5 || month == 7
                            || month == 8 || month == 10 || month == 12) {
                        String[] day = new String[31];
                        for (int d = 0; d < 31; d++) {
                            day[d] = d + 1 + "";
                        }
                        mDayDatas.put(mYearDatas[i] + month + "", day);
                    } else if (month == 2) {
                        String[] day = new String[29];
                        for (int d = 0; d < 29; d++) {
                            day[d] = d + 1 + "";
                        }
                        mDayDatas.put(mYearDatas[i] + month + "", day);
                    } else {
                        String[] day = new String[30];
                        for (int d = 0; d < 30; d++) {
                            day[d] = d + 1 + "";
                        }
                        mDayDatas.put(mYearDatas[i] + month + "", day);
                    }
                }
            } else {
                for (int j = 0; j < 12; j++) {
                    int month = j + 1;
                    if (month == 1 || month == 3 || month == 5 || month == 7
                            || month == 8 || month == 10 || month == 12) {
                        String[] day = new String[31];
                        for (int d = 0; d < 31; d++) {
                            day[d] = d + 1 + "";
                        }
                        mDayDatas.put(mYearDatas[i] + month + "", day);
                    } else if (month == 2) {
                        String[] day = new String[28];
                        for (int d = 0; d < 28; d++) {
                            day[d] = d + 1 + "";
                        }
                        mDayDatas.put(mYearDatas[i] + month + "", day);
                    } else {
                        String[] day = new String[30];
                        for (int d = 0; d < 30; d++) {
                            day[d] = d + 1 + "";
                        }
                        mDayDatas.put(mYearDatas[i] + month + "", day);
                    }
                }
            }
        }
    }

    /**
     * 设置颜色
     *
     * @param color
     */
    public void setTextColor(int color) {
        this.textColor = color;
    }

    /**
     * 设置颜色
     *
     * @param colorString
     */
    public void setTextColor(String colorString) {
        this.textColor = Color.parseColor(colorString);
    }

    private int getTextColor() {
        return textColor;
    }

    /**
     * 设置文字颜色
     *
     * @param textSp
     */
    public void setTextSize(int textSp) {
        this.textSize = textSp;
    }

    private int getTextSize() {
        return textSize;
    }

    /**
     * 设置滚轮显示个数
     *
     * @param count
     */
    public void setVisibleItems(int count) {
        this.visibleItems = count;
    }

    private int getVisibleItems() {
        return this.visibleItems;
    }

    /**
     * 设置滚轮是否循环滚动
     *
     * @param isCyclic
     */
    public void setIsCyclic(boolean isCyclic) {
        this.isCyclic = isCyclic;
    }

    private boolean getIsCyclic() {
        return this.isCyclic;
    }

    public void setTime(String time) {
        String yerarStr = null;
        String monthStr = null;
        for (int i = 0; i < mYearDatas.length; i++) {
            if ((time.substring(0, 4) + "").equals(mYearDatas[i])) {
                mViewYear.setCurrentItem(i);
                yerarStr = mYearDatas[i];
                break;
            }
        }
        String[] month = mMonthDatas.get(yerarStr);
        for (int i = 0; i < month.length; i++) {
            if ((Integer.parseInt(time.substring(4, 6)) + "").equals(month[i])) {
                mViewMonth.setCurrentItem(i);
                monthStr = month[i];
                break;
            }
        }
        String[] day = mDayDatas.get(yerarStr + monthStr);
        for (int i = 0; i < day.length; i++) {
            if ((Integer.parseInt(time.substring(6, 8)) + "").equals(day[i])) {
                mViewDay.setCurrentItem(i);
                break;
            }
        }
    }

    private void setUpData() {
        ArrayWheelAdapter arrayWheelAdapter = new ArrayWheelAdapter<String>(
                context, mYearDatas);
        mViewYear.setViewAdapter(arrayWheelAdapter);

        // 设置可见条目数量
        mViewYear.setVisibleItems(getVisibleItems());
        mViewMonth.setVisibleItems(getVisibleItems());
        mViewDay.setVisibleItems(getVisibleItems());
        mViewYear.setCyclic(getIsCyclic());
        mViewMonth.setCyclic(getIsCyclic());
        mViewDay.setCyclic(getIsCyclic());

        arrayWheelAdapter.setTextColor(getTextColor());
        arrayWheelAdapter.setTextSize(getTextSize());
        updateMonth();
        mCurrentYearName = mYearDatas[0];
    }

    /**
     * 根据当前的月，更新日WheelView的信息
     */
    private void updateDay() {
        int pCurrent = mViewMonth.getCurrentItem();
        mCurrentMonthName = mMonthDatas.get(mCurrentYearName)[pCurrent];
        String[] areas = mDayDatas.get(mCurrentYearName + mCurrentMonthName);
        if (areas == null) {
            areas = new String[30];
        }
        ArrayWheelAdapter districtWheel = new ArrayWheelAdapter<String>(
                context, areas);
        // 设置可见条目数量
        districtWheel.setTextColor(getTextColor());
        districtWheel.setTextSize(getTextSize());
        mViewDay.setViewAdapter(districtWheel);
        mViewDay.setCurrentItem(0);

        // 获取第一个区名称
        mCurrentDayName = areas[0];
    }
    /**
     * 根据当前的年，更新日WheelView的信息
     */
    private void updateMonth() {
        int pCurrent = mViewYear.getCurrentItem();
        mCurrentYearName = mYearDatas[pCurrent];
        String[] month = mMonthDatas.get(mCurrentYearName);
        if (month == null) {
            month = new String[12];
        }
        ArrayWheelAdapter monthWheel = new ArrayWheelAdapter<String>(context,
                month);
        // 设置可见条目数量
        monthWheel.setTextColor(getTextColor());
        monthWheel.setTextSize(getTextSize());
        mViewMonth.setViewAdapter(monthWheel);
        mViewMonth.setCurrentItem(0);
        updateDay();
        mCurrentMonthName = month[0];
    }

    @Override
    public void setType(int type) {
    }

    @Override
    public void show(String time) {
        if (!isShow()) {
            setUpData();
            setTime(time);
            //	popwindow.showAtLocation(popview, Gravity.BOTTOM, 0, 0);
        }
    }

    @Override
    public void hide() {
        if (isShow()) {
            //popwindow.dismiss();
        }
    }

    @Override
    public boolean isShow() {
        return false;
    }

    @Override
    public void onChanged(WheelView wheel, int oldValue, int newValue) {
        // TODO Auto-generated method stub
        if (wheel == mViewYear) {
            updateMonth();
        } else if (wheel == mViewMonth) {
            updateDay();
        } else if (wheel == mViewDay) {
            mCurrentDayName = mDayDatas.get(mCurrentYearName
                    + mCurrentMonthName)[newValue];
        }

    }

    public String getValue() {
        String monthString =null,dayString = null;
        int month = Integer.parseInt(mCurrentMonthName);
        if(month<10){
            monthString = "0"+month;
        }else{
            monthString = month+"";
        }
        int day = Integer.parseInt(mCurrentDayName);
        if(day<10){
            dayString = "0"+day;
        }else{
            dayString = day+"";
        }
        return mCurrentYearName + monthString + dayString;
    }
    public String getIntentTime() {
        String monthString = null,dayString = null;
        int month = Integer.parseInt(mCurrentMonthName);
        if(month<10){
            monthString = "0"+month;
        }else{
            monthString = month+"";
        }
        int day = Integer.parseInt(mCurrentDayName);
        if(day<10){
            dayString = "0"+day;
        }else{
            dayString = day+"";
        }
        return mCurrentYearName+"年" + monthString+"月"+ dayString +"日";
    }

    @Override
    public void show() {
    }
}
