package com.siberiadante.emotionapp.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by SiberiaDante
 * Describe: 不可横向滑动的ViewPager
 * Time: 2017/6/26
 * Email: 994537867@qq.com
 * GitHub: https://github.com/SiberiaDante
 * 博客园： http://www.cnblogs.com/shen-hua/
 */

public class NoHorizontalScrollerViewPager extends ViewPager {

    public NoHorizontalScrollerViewPager(Context context) {
        super(context);
    }

    public NoHorizontalScrollerViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 重写拦截事件，返回值设置为false，这时便不会横向滑动了。
     *
     * @param ev
     * @return
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return false;
    }

    /**
     * 重写拦截事件，返回值设置为false，这时便不会横向滑动了。
     *
     * @param ev
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return false;
    }
}
