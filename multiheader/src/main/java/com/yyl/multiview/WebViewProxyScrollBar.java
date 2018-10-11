package com.yyl.multiview;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import java.lang.ref.WeakReference;
/**
 * Created by yyl on 2018/10.
 * <p>
 * https://github.com/mengzhidaren
 *
 * @author yyl
 */
public class WebViewProxyScrollBar extends View {
    private int progressColor= Color.argb(220,80,80,80);
    private int webRange = 0;
    private int fristRecyclerRange = 0;
    private int thumbHeight = 100;
    private Paint paint = new Paint();

    private double progress;


    public WebViewProxyScrollBar(Context context) {
        super(context);
        init(context,null);
    }

    public WebViewProxyScrollBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);
    }

    public WebViewProxyScrollBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs);
    }

    private AnimationHandler animationHandler = new AnimationHandler(new WeakReference<>(this));

    private void init(Context context, AttributeSet attrs) {
        if (attrs != null) {
            final TypedArray a = context.obtainStyledAttributes(
                    attrs, R.styleable.WebViewProxyScrollBar);
            thumbHeight = a.getDimensionPixelSize(R.styleable.WebViewProxyScrollBar_progressHeight, thumbHeight);
            progressColor = a.getColor(R.styleable.WebViewProxyScrollBar_progressColor, progressColor);
            progress = a.getFloat(R.styleable.WebViewProxyScrollBar_progressValue, 0f);
            a.recycle();
        }

        //设置实心
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(progressColor);
    }


    public void onProgressWebView(int extent, int offset, int range) {
        webRange = range;
        if (fristRecyclerRange != 0) {
            int totaleRange = fristRecyclerRange + range;
            if (totaleRange > 0) {
                progress = (double) offset / totaleRange;
                invalidate();
                startAnim();
            }
        }
    }

    //  offset  偏移范范围 到顶为0
    //  range   总范围
    public void onProgressRecyclerView(int extent, int offset, int range, boolean fristVisiable) {
        if (fristRecyclerRange == 0 || !fristVisiable) {
            fristRecyclerRange = range;
        }
        int totaleRange = fristRecyclerRange + webRange;
        if (totaleRange > 0 && fristRecyclerRange > 0) {
            //webView占有比例
            double scaleWeb = (double) (webRange - extent) / totaleRange;
            // RecyclerView当前的比例
            double scaleRecycler = (double) (fristVisiable ? offset : (offset + extent)) / fristRecyclerRange;
            progress = scaleWeb + (1D - scaleWeb) * scaleRecycler;
            invalidate();
            startAnim();
        }
    }

    private void startAnim() {
        setAlpha(1f);
        animationHandler.sendEmptyMessage(1);
    }


    private void hideAnim() {
        ObjectAnimator.ofFloat(this, "alpha", getAlpha(), 0f)
                .setDuration(200)
                .start();
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        //  super.onDraw(canvas);
        int height = getHeight();
        int width = getWidth();
        int top = (int) ((height - thumbHeight) * progress);
//        canvas.drawRoundRect(new RectF(1, top, width, thumbHeight + top), round, round, paint);
        canvas.drawRect(new RectF(0, top, width, thumbHeight + top), paint);
    }

    private static class AnimationHandler extends Handler {
        WeakReference<WebViewProxyScrollBar> weakReference;

        public AnimationHandler(WeakReference<WebViewProxyScrollBar> weakReference) {
            this.weakReference = weakReference;
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    removeMessages(2);
                    sendEmptyMessageDelayed(2,500);
                    break;
                case 2:
                    WebViewProxyScrollBar scrollBar=weakReference.get();
                    if (scrollBar!=null){
                        scrollBar.hideAnim();
                    }
                    break;
            }
        }

    }

}
