package com.dongnao.lsn_14_recycleview_scroll;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;


public class MyRecyclerView extends RecyclerView {
    private int touchSlop;
    private Context mContext;
    private int INVALID_POINTER = -1;
    private int scrollPointerId = INVALID_POINTER;
    private int initialTouchX;
    private int initialTouchY;

    public MyRecyclerView(Context context) {
        this(context, null);
    }

    public MyRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        ViewConfiguration vc = ViewConfiguration.get(context);
        touchSlop = vc.getScaledEdgeSlop();
        mContext = context;
    }



    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        if (e == null) {
            return false;
        }
        int action = MotionEventCompat.getActionMasked(e);
        int actionIndex = MotionEventCompat.getActionIndex(e);
        switch (action) {
            case MotionEvent.ACTION_DOWN :
                scrollPointerId = MotionEventCompat.getPointerId(e, 0);
                initialTouchX = Math.round(e.getX() + 0.5f);
                initialTouchY = Math.round(e.getY() + 0.5f);
                return super.onInterceptTouchEvent(e);
            case MotionEvent.ACTION_POINTER_DOWN:
                scrollPointerId = MotionEventCompat.getPointerId(e, actionIndex);
                initialTouchX = Math.round(MotionEventCompat.getX(e, actionIndex) + 0.5f);
                initialTouchY = Math.round(MotionEventCompat.getY(e, actionIndex) + 0.5f);
                return super.onInterceptTouchEvent(e);
            case MotionEvent.ACTION_MOVE:
                int index = MotionEventCompat.findPointerIndex(e, scrollPointerId);
                if (index < 0) {
                    return false;
                }
                int x = Math.round(MotionEventCompat.getX(e, index) + 0.5f);
                int y = Math.round(MotionEventCompat.getY(e, index) + 0.5f);
                if (getScrollState() != SCROLL_STATE_DRAGGING ) {
                    int dx = x - initialTouchX;
                    int dy = y - initialTouchY;
                    boolean startScroll = false;


                    if(getLayoutManager().canScrollHorizontally() && Math.abs(dy) > touchSlop &&
                            ( Math.abs(dx) > Math.abs(dy))) {
                        startScroll = true;
                    }
                    if(getLayoutManager().canScrollVertically() && Math.abs(dy) > touchSlop &&
                            ( Math.abs(dy) > Math.abs(dx))) {
                        startScroll = true;
                    }


                    //如下条件，结合成一个条件， 前者条件已经是判断未纵向移动了，那么后面补上横向移动就行
                    if(getLayoutManager().canScrollVertically() && Math.abs(dy) > touchSlop &&
                            (getLayoutManager().canScrollHorizontally() || Math.abs(dy) > Math.abs(dx))) {
                        startScroll = true;
                    }
                    return startScroll && super.onInterceptTouchEvent(e);
                }
                return super.onInterceptTouchEvent(e);
            default:
                return super.onInterceptTouchEvent(e);
        }
    }
}
