package com.example.tsquared.ViewPager;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import androidx.viewpager.widget.ViewPager;

public class CustomViewPager extends ViewPager{
    public CustomViewPager(Context context, AttributeSet attr){
        super(context, attr);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event){
        return false;
    }
}
