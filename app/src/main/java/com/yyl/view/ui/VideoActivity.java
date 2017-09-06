package com.yyl.view.ui;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.yyl.multiview.LogUtils;
import com.yyl.multiview.RecyclerViewMultiHeader;
import com.yyl.multiview.OnVideoSmallCallBack;
import com.yyl.view.R;
import com.yyl.view.base.AdapterDemo;
import com.yyl.view.fragment.VideoFragment;

public class VideoActivity extends AppCompatActivity {
    String tag = "VideoActivity";
    Context context;
    TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_video);
        this.context = this;
        findViewById(R.id.changeFull).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onChangeFullScreen(!screenFullVideo);
            }
        });
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerViewMultiHeader = (RecyclerViewMultiHeader) findViewById(R.id.recyclerViewMultiHeader);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment, new VideoFragment()).commit();

        recyclerViewMultiHeader.attachToVideo(recyclerView);
        recyclerView.setAdapter(new AdapterDemo());
        recyclerViewMultiHeader.setOnVideoSmallCallBack(new OnVideoSmallCallBack() {

            @Override
            public void changeMiniScaleState(RecyclerViewMultiHeader viewMultiHeader, boolean isSmallMini) {
                LogUtils.i(tag, "isSmallMini=" + isSmallMini);
            }

            @Override
            public void onClickSmall(RecyclerViewMultiHeader viewMultiHeader) {
                recyclerView.stopScroll();
                recyclerView.smoothScrollToPosition(0);
            }
        });

        // recyclerViewMultiHeader.setScreenSmallDisable(true);


        title = (TextView) findViewById(R.id.title);


        title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private RecyclerView recyclerView;
    private RecyclerViewMultiHeader recyclerViewMultiHeader;

    @Override
    public void onBackPressed() {
        if (screenFullVideo) {
            onChangeFullScreen(false);
        } else {
            super.onBackPressed();
        }
    }


    public void onChangeFullScreen(boolean fullVideo) {
        if (fullVideo) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
        }
        LogUtils.i(tag, "onChangeFullScreen=" + fullVideo);
    }

    public boolean screenFullVideo;

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        screenFullVideo = newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE;
        if (screenFullVideo) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            if (getSupportActionBar() != null)
                getSupportActionBar().hide();
        } else {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            if (getSupportActionBar() != null)
                getSupportActionBar().show();
        }
        findViewById(R.id.title).setVisibility(screenFullVideo ? View.GONE : View.VISIBLE);
        LogUtils.i(tag, "onConfigurationChanged =" + screenFullVideo);
    }
}
