package com.yyl.multiview;

/**
 * Created by yuyunlong on 2017/7/24/024.
 */

public interface OnVideoSmallCallBack {
    void changeMiniScaleState(RecyclerViewMultiHeader viewMultiHeader, boolean isSmallMini);

    void onClickSmall(RecyclerViewMultiHeader viewMultiHeader);

    void onScrollChanged(int translation);
}
