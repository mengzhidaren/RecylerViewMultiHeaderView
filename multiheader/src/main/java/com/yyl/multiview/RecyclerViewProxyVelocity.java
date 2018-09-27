package com.yyl.multiview;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.Interpolator;
import android.widget.OverScroller;

/**
 * Created by yyl on 2018/6.
 * <p>
 * https://github.com/mengzhidaren
 *
 * @author yyl
 * 不想入侵RecyclerView的onTouchEvent 就换成这样了
 * 如果不想setOnTouchListener 就重写RecyclerView的onTouchEvent
 */
class RecyclerViewProxyVelocity {


    private RecyclerView recyclerView;

    private OnCallBackVelocity onCallBackVelocity;
    //模拟惯性衰减过程
    private final ViewFlinger mViewFlinger;

    private static final int INVALID_POINTER = -1;
    public static final int SCROLL_STATE_IDLE = 0;
    //    public static final int SCROLL_STATE_DRAGGING = 1;
    public static final int SCROLL_STATE_SETTLING = 2;


    private int mScrollState = SCROLL_STATE_IDLE;
    private int mScrollPointerId = INVALID_POINTER;
    private VelocityTracker mVelocityTracker;
    private int mMinFlingVelocity;
    private int mMaxFlingVelocity;

    public RecyclerViewProxyVelocity(Context context) {
        mViewFlinger = new ViewFlinger(context);
        ViewConfiguration vc = ViewConfiguration.get(context);
        mMinFlingVelocity = vc.getScaledMinimumFlingVelocity();
        mMaxFlingVelocity = vc.getScaledMaximumFlingVelocity();
    }

    public void detach() {
        setScrollState(SCROLL_STATE_IDLE);
        recyclerView.removeOnScrollListener(listener);
        recyclerView.setOnTouchListener(null);
        onCallBackVelocity = null;
        recyclerView = null;

    }

    public void attachView(RecyclerView recyclerView, OnCallBackVelocity onCallBackVelocity) {
        this.recyclerView = recyclerView;
        this.onCallBackVelocity = onCallBackVelocity;
        recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return onTouchEvent(event);
            }
        });
        recyclerView.addOnScrollListener(listener);
    }


    private RecyclerView.OnScrollListener listener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            if (newState == RecyclerView.SCROLL_STATE_IDLE && recyclerView.computeVerticalScrollOffset() == 0) { //到顶速度
                int velocity = mViewFlinger.getCurrVelocity();
                Log.i("RecyclerViewVelociy", "RecyclerViewTouch  到顶速度：" + velocity);
                if (velocity > 0 && onCallBackVelocity != null) {
                    onCallBackVelocity.callBackVelocity(-velocity);
                }
            }
        }
    };


    public boolean onTouchEvent(MotionEvent event) {
        //获取滑动至顶部停止时的速度值
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        boolean eventAddedToVelocityTracker = false;

        final MotionEvent vtev = MotionEvent.obtain(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                setScrollState(SCROLL_STATE_IDLE);
                mScrollPointerId = event.getPointerId(0);
                Log.i("RecyclerViewVelociy", "ACTION_DOWN： PointerId=" + mScrollPointerId);
                break;
            }
            case MotionEvent.ACTION_UP: {
                mVelocityTracker.addMovement(vtev);
                eventAddedToVelocityTracker = true;
                mVelocityTracker.computeCurrentVelocity(1000, mMaxFlingVelocity);
                float yVelocity = -mVelocityTracker.getYVelocity(mScrollPointerId);
                Log.i("RecyclerViewVelociy", "速度取值：" + (int) yVelocity + "   PointerId=" + mScrollPointerId);
                if (Math.abs(yVelocity) < mMinFlingVelocity) {
                    yVelocity = 0F;
                } else {
                    yVelocity = Math.max(-mMaxFlingVelocity, Math.min(yVelocity, mMaxFlingVelocity));
                }
                if (yVelocity != 0) {
                    mViewFlinger.fling((int) yVelocity);
                } else {
                    setScrollState(SCROLL_STATE_IDLE);
                }
                resetTouch();
                break;
            }
            case MotionEvent.ACTION_CANCEL: {
                eventAddedToVelocityTracker = true;
                resetTouch();
                break;
            }
        }
        if (!eventAddedToVelocityTracker) {
            mVelocityTracker.addMovement(vtev);
        }
        vtev.recycle();
        return false;
    }

    private void resetTouch() {
        if (mVelocityTracker != null) {
            mVelocityTracker.clear();
        }
    }

    private void setScrollState(int state) {
        if (state == mScrollState) {
            return;
        }
        mScrollState = state;
        if (state != SCROLL_STATE_SETTLING) {
            mViewFlinger.stop();
        }
    }


    //f(x) = (x-1)^5 + 1
    private static final Interpolator sQuinticInterpolator = new Interpolator() {
        @Override
        public float getInterpolation(float t) {
            t -= 1.0f;
            return t * t * t * t * t + 1.0f;
        }
    };

    /**
     * 这里模拟惯性衰减过程 主要获取惯性速度值传递给webView
     */
    private class ViewFlinger implements Runnable {

        //   private int mLastFlingY = 0;
        private OverScroller mScroller;
        private boolean mEatRunOnAnimationRequest = false;
        private boolean mReSchedulePostAnimationCallback = false;

        public ViewFlinger(Context context) {
            mScroller = new OverScroller(context, sQuinticInterpolator);
        }

        public int getCurrVelocity() {
            return (int) mScroller.getCurrVelocity();
        }

        @Override
        public void run() {
            disableRunOnAnimationRequests();
            final OverScroller scroller = mScroller;
            if (scroller.computeScrollOffset()) {
                postOnAnimation();
            }
            enableRunOnAnimationRequests();
        }

        public void fling(int velocityY) {
            //     mLastFlingY = 0;
            setScrollState(SCROLL_STATE_SETTLING);
            mScroller.fling(0, 0, 0, velocityY, Integer.MIN_VALUE, Integer.MAX_VALUE, Integer.MIN_VALUE, Integer.MAX_VALUE);
            postOnAnimation();
        }

        public void stop() {
            if (recyclerView != null)
                recyclerView.removeCallbacks(this);
            mScroller.abortAnimation();
        }

        private void disableRunOnAnimationRequests() {
            mReSchedulePostAnimationCallback = false;
            mEatRunOnAnimationRequest = true;
        }

        private void enableRunOnAnimationRequests() {
            mEatRunOnAnimationRequest = false;
            if (mReSchedulePostAnimationCallback) {
                postOnAnimation();
            }
        }

        private void postOnAnimation() {
            if (mEatRunOnAnimationRequest) {
                mReSchedulePostAnimationCallback = true;
            } else {
                if (recyclerView != null) {
                    recyclerView.removeCallbacks(this);
                    ViewCompat.postOnAnimation(recyclerView, this);
                }
            }
        }
    }
}
