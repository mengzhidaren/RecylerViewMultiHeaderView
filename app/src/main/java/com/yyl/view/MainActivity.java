package com.yyl.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.yyl.view.ui.HeaderViewActivity;
import com.yyl.view.ui.VideoActivity;
import com.yyl.view.ui.Web2Activity;
import com.yyl.view.ui.WebActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.video).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                   startActivity(new Intent(MainActivity.this, VideoActivity.class));
            }
        });
        findViewById(R.id.view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, HeaderViewActivity.class));
            }
        });
        findViewById(R.id.webView1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    startActivity(new Intent(MainActivity.this, WebActivity.class));
            }
        });
        findViewById(R.id.webView2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                   startActivity(new Intent(MainActivity.this, Web2Activity.class));
            }
        });
    }
}
