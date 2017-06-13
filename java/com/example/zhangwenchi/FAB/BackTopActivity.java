package com.example.zhangwenchi.FAB;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class BackTopActivity extends Activity {

    @InjectView(R.id.scrollview)
    mScrollView scrollview;
    @InjectView(R.id.fab)
    FABBackTop fab;
    @InjectView(R.id.ll_scrollView)
    LinearLayout llScrollView;
    @InjectView(R.id.iv_pic)
    ImageView ivPic;

    private Context mContext;
    private Activity mActivity;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_back_top);
        mContext = this;
        mActivity = this;
        ButterKnife.inject(this);
        initView();
        initListener();
    }

    private void initView() {
        fab.hide(false);
        fab.attachToScrollView(scrollview, new ScrollDirectionListener() {
            @Override
            public void onScrollDown() {
                fab.hide();
            }

            @Override
            public void onScrollUp() {
                fab.show();
            }
        }, new mScrollView.OnScrollChangedListener() {
            @Override
            public void onScrollChanged(ScrollView who, int l, int t, int oldl, int oldt) {
                if (t > ivPic.getHeight()) {
                    fab.show();
                } else {
                    fab.hide();
                }
            }
        });
    }

    private void initListener() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fab.setVisibility(View.INVISIBLE);
                scrollview.fullScroll(ScrollView.FOCUS_UP);
                new Handler().postDelayed(new Runnable(){
                    public void run() {
                        fab.setVisibility(View.VISIBLE);
                    }
                }, 300);
            }
        });
    }
}
