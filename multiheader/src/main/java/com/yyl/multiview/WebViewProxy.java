package com.yyl.multiview;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.ViewConfiguration;
import android.view.animation.Interpolator;
import android.webkit.WebView;
import android.widget.OverScroller;

/**
 * Created by yyl on 2018/6.
 * <p>
 * https://github.com/mengzhidaren
 *
 * @author yyl
 */
public class WebViewProxy extends WebView {
    public WebViewProxy(Context context) {
        super(context);
        init(context);
    }

    public WebViewProxy(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public WebViewProxy(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private int webViewBottomOffset = 1;
    private  WebViewProxyScrollBar scrollBar;

    private static final int INVALID_POINTER = -1;
    public static final int SCROLL_STATE_IDLE = 0;
    //    public static final int SCROLL_STATE_DRAGGING = 1;
    public static final int SCROLL_STATE_SETTLING = 2;

    private OnCallBackVelocity onCallBackVelocity;
    private int mScrollState = SCROLL_STATE_IDLE;
    private int mScrollPointerId = INVALID_POINTER;
    private VelocityTracker mVelocityTracker;
    private int mMinFlingVelocity;
    private int mMaxFlingVelocity;

    private final ViewFlinger mViewFlinger = new ViewFlinger();

    private void init(Context context) {
        final ViewConfiguration vc = ViewConfiguration.get(context);
        mMinFlingVelocity = vc.getScaledMinimumFlingVelocity();
        mMaxFlingVelocity = vc.getScaledMaximumFlingVelocity();
        setVerticalScrollBarEnabled(false);
    }

    public void attachView(WebViewProxyScrollBar scrollBar,OnCallBackVelocity onCallBackVelocity) {
        this.scrollBar=scrollBar;
        this.onCallBackVelocity = onCallBackVelocity;
    }


    public void detach() {
        this.onCallBackVelocity = null;
        this.scrollBar=null;
    }

    private boolean isToBottomState = false;

    //滑动是否到底了
    public boolean isToBottom() {
        return isToBottomState;
    }

    /**
     * @param webViewBottomOffset 最小滑动值
     */
    @Deprecated
    public void setWebViewBottomOffset(int webViewBottomOffset) {
        this.webViewBottomOffset = webViewBottomOffset;
    }

    @Override
    protected void onOverScrolled(int scrollX, int scrollY, boolean clampedX, boolean clampedY) {
        super.onOverScrolled(scrollX, scrollY, clampedX, clampedY);
        int extent = computeVerticalScrollExtent();//可见范围
        int offset = computeVerticalScrollOffset();//偏移范范围 到顶为0    offset == scrollY
        int range = computeVerticalScrollRange();//总范围

        isToBottomState = range - (extent + offset) <= webViewBottomOffset;
        if (clampedY && isToBottomState) {//到底
            int velocity = mViewFlinger.getCurrVelocity();
//            RecyclerViewMultiHeader.i("WebViewProxy", "到底速度：" + velocity);
            if (velocity > 0 && onCallBackVelocity != null)
                onCallBackVelocity.callBackVelocity(velocity);
        }
        if(scrollBar!=null){
            scrollBar.onProgressWebView(extent,offset,range);
        }
    }


    @Override
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
                break;
            }
            case MotionEvent.ACTION_UP: {
                mVelocityTracker.addMovement(vtev);
                eventAddedToVelocityTracker = true;
                mVelocityTracker.computeCurrentVelocity(1000, mMaxFlingVelocity);
                float yVelocity = -mVelocityTracker.getYVelocity(mScrollPointerId);
//                RecyclerViewMultiHeader.i("WebViewProxy", "速度取值：" + yVelocity);
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
        return super.onTouchEvent(event);
    }

    /**
     * 清空速度事件
     */
    private void resetTouch() {
        if (mVelocityTracker != null) {
            mVelocityTracker.clear();
        }
    }

    /**
     * 记录状态
     */
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
     * 这里模拟惯性衰减过程 主要获取惯性速度值传递给recyclerView
     */
    private class ViewFlinger implements Runnable {

        //   private int mLastFlingY = 0;
        private OverScroller mScroller;
        private boolean mEatRunOnAnimationRequest = false;
        private boolean mReSchedulePostAnimationCallback = false;

        public ViewFlinger() {
            mScroller = new OverScroller(getContext(), sQuinticInterpolator);
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
            removeCallbacks(this);
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
                removeCallbacks(this);
                ViewCompat.postOnAnimation(WebViewProxy.this, this);
            }
        }
    }

}
