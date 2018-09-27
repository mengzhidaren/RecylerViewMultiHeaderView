package com.yyl.view.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.webkit.WebView;

import com.yyl.multiview.RecyclerViewMultiHeader;
import com.yyl.multiview.WebViewProxy;
import com.yyl.view.R;
import com.yyl.view.base.AdapterDemo;
import com.yyl.view.fragment.WebFragment;

public class WebActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        setTitle("WebFragment嵌入布局");
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerViewMultiHeader = (RecyclerViewMultiHeader) findViewById(R.id.recyclerViewMultiHeader);
        //  recyclerViewMultiHeader.attachToWebView(recyclerView,);
        recyclerView.setAdapter(new AdapterDemo());
        getSupportFragmentManager().beginTransaction().add(R.id.fragment, new WebFragment()).commit();

    }

    private RecyclerView recyclerView;
    private RecyclerViewMultiHeader recyclerViewMultiHeader;

    public void setWebView(WebViewProxy webView) {
        recyclerViewMultiHeader.attachToWebView(recyclerView, webView);
    }


}
