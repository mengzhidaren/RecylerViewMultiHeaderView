package com.yyl.view.ui;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.yyl.multiview.RecyclerViewMultiHeader;
import com.yyl.multiview.WebViewProxy;
import com.yyl.multiview.WebViewProxyScrollBar;
import com.yyl.view.R;
import com.yyl.view.base.AdapterDemo;
import com.yyl.view.fragment.WebFragment;


public class Web2Activity extends AppCompatActivity {
    private WebViewProxy webView;
    RecyclerViewMultiHeader recyclerViewMultiHeader;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web2);
        setTitle("webView嵌入布局");
        recyclerView = findViewById(R.id.recyclerView);
        recyclerViewMultiHeader = findViewById(R.id.recyclerViewMultiHeader);
        webView = findViewById(R.id.webView2);
        setWebSeting();
        recyclerViewMultiHeader.setRequestFullWeb(true);
        recyclerViewMultiHeader.attachToWebView(recyclerView, webView,(WebViewProxyScrollBar)findViewById(R.id.scrollBar));
        recyclerView.setAdapter(new AdapterDemo());
        webView.loadUrl("https://toutiao.io/c/java");
    }

    @Override
    protected void onResume() {
        super.onResume();
        webView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        webView.onPause();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        webView.destroy();
    }


    public void test1Click(View view) {
        recyclerViewMultiHeader.detach();
    }

    public void test2Click(View view) {
        recyclerViewMultiHeader.attachToWebView(recyclerView, webView,(WebViewProxyScrollBar)findViewById(R.id.scrollBar));
        recyclerViewMultiHeader.reAttachRefresh();
    }


    private void setWebSeting() {
        WebSettings settings = webView.getSettings();
//        settings.setDefaultTextEncodingName("UTF-8");
        settings.setSupportZoom(false);
        settings.setDisplayZoomControls(false);
        settings.setJavaScriptEnabled(true);
        settings.setBlockNetworkImage(true);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        settings.setAppCacheEnabled(false);
        settings.setSaveFormData(false);
        settings.setDomStorageEnabled(true);
        // 自适应屏幕
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        webView.setWebViewClient(new WebFragment.MyWebViewClient());
        WebSettings webSetting = settings;
//        webSetting.setJavaScriptCanOpenWindowsAutomatically(true);
        webSetting.setAllowFileAccess(true);
        webSetting.setBuiltInZoomControls(true);
        webSetting.setUseWideViewPort(true);
        webSetting.setSupportMultipleWindows(true);
        // webSetting.setLoadWithOverviewMode(true);
        // webSetting.setDatabaseEnabled(true);
        webSetting.setGeolocationEnabled(true);
//        webSetting.setAppCacheMaxSize(Long.MAX_VALUE);
//        // webSetting.setPageCacheCapacity(IX5WebSettings.DEFAULT_CACHE_CAPACITY);
//        webSetting.setPluginState(WebSettings.PluginState.ON_DEMAND);
        // webSetting.setRenderPriority(WebSettings.RenderPriority.HIGH);
    }


}
