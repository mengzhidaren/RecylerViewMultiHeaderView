package com.yyl.view.ui;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;

import com.yyl.multiview.LogUtils;
import com.yyl.multiview.RecyclerViewMultiHeader;
import com.yyl.multiview.impl.ScreenChangeCallBack;
import com.yyl.multiview.impl.ScreenChangeSmallCallBack;
import com.yyl.multiview.impl.ScreenSmallOnClick;
import com.yyl.view.R;
import com.yyl.view.base.AdapterDemo;
import com.yyl.view.fragment.VideoFragment;

public class VideoActivity extends AppCompatActivity implements ScreenChangeCallBack {
    String tag = "VideoActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_video);
        findViewById(R.id.changeFull).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setVideoFullState(!screenFullVideo);
            }
        });
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerViewMultiHeader = (RecyclerViewMultiHeader) findViewById(R.id.recyclerViewMultiHeader);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment, new VideoFragment()).commit();

        recyclerViewMultiHeader.attachToVideo(recyclerView, this);
        recyclerView.setAdapter(new AdapterDemo());

        recyclerViewMultiHeader.setScreenSmallOnClick(new ScreenSmallOnClick() {
            @Override
            public void onClick() {
                recyclerView.stopScroll();
                recyclerView.smoothScrollToPosition(0);
            }
        });

        recyclerViewMultiHeader.setScreenChangeSmallCallBack(new ScreenChangeSmallCallBack() {
            @Override
            public void changeMiniScaleState(boolean isSmallMini) {
                LogUtils.i(tag, "isSmallMini=" + isSmallMini);
            }
        });

        // recyclerViewMultiHeader.setScreenSmallDisable(true);
    }

    private RecyclerView recyclerView;
    private RecyclerViewMultiHeader recyclerViewMultiHeader;

    @Override
    public void onBackPressed() {
        if (screenFullVideo) {
            setVideoFullState(false);
        } else {
            super.onBackPressed();
        }
    }

    public boolean screenFullVideo;

    public void setVideoFullState(boolean fullVideo) {
        recyclerViewMultiHeader.onChangeFullScreen(fullVideo);
    }


    @Override
    public void onChangeFullScreen(boolean fullVideo) {
        screenFullVideo = fullVideo;
        if (fullVideo) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
            if (getSupportActionBar() != null)
                getSupportActionBar().hide();
        } else {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            if (getSupportActionBar() != null)
                getSupportActionBar().show();
        }
        findViewById(R.id.title).setVisibility(fullVideo ? View.GONE : View.VISIBLE);
    }
}
