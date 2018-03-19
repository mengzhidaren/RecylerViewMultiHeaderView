package com.yyl.view.ui;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.yyl.multiview.RecyclerViewMultiHeader;
import com.yyl.view.R;
import com.yyl.view.base.AdapterDemo;
import com.yyl.view.fragment.WebFragment;

public class Web2Activity extends AppCompatActivity {
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web2);
        setTitle("webView嵌入布局");
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerViewMultiHeader = (RecyclerViewMultiHeader) findViewById(R.id.recyclerViewMultiHeader);
        webView = (WebView) findViewById(R.id.webView2);
        setWebSeting();
        recyclerViewMultiHeader.setRequestFullWeb(true);
        recyclerViewMultiHeader.attachToWebView(recyclerView, webView);
        recyclerView.setAdapter(new AdapterDemo());
        webView.loadUrl("https://toutiao.io/c/java");
    }

    private RecyclerView recyclerView;
    private RecyclerViewMultiHeader recyclerViewMultiHeader;


    private void setWebSeting() {
        WebSettings settings = webView.getSettings();
        settings.setDefaultTextEncodingName("UTF-8");
        settings.setSupportZoom(false);
        settings.setDisplayZoomControls(false);
        settings.setJavaScriptEnabled(true);
        settings.setBlockNetworkImage(true);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        settings.setAppCacheEnabled(false);
        settings.setSaveFormData(false);
        // 自适应屏幕
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        webView.setWebViewClient(new WebFragment.MyWebViewClient());
    }


}
