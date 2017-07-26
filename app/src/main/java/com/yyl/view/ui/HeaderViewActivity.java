package com.yyl.view.ui;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.yyl.multiview.RecyclerViewMultiHeader;
import com.yyl.view.R;
import com.yyl.view.base.AdapterDemo;

public class HeaderViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_header_view);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerViewMultiHeader = (RecyclerViewMultiHeader) findViewById(R.id.recyclerViewMultiHeader);
        recyclerViewMultiHeader.attachToHeader(recyclerView);
        recyclerView.setAdapter(new AdapterDemo());

        findViewById(R.id.headerBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v, "headerBtn  onClick", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    private RecyclerView recyclerView;
    private RecyclerViewMultiHeader recyclerViewMultiHeader;
}
