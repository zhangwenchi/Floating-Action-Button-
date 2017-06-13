package com.example.zhangwenchi.FAB;

/**
 * Created by zhangwc on 2017/6/10.
 */
import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;
public class mScrollView extends ScrollView {

    public interface OnScrollChangedListener {
        void onScrollChanged(ScrollView who, int l, int t, int oldl, int oldt);
    }

    private OnScrollChangedListener mOnScrollChangedListener;

    public mScrollView(Context context) {
        super(context);
    }

    public mScrollView(Context context, AttributeSet attrs) {

        super(context, attrs);
    }

    public mScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (mOnScrollChangedListener != null) {
            mOnScrollChangedListener.onScrollChanged(this, l, t, oldl, oldt);
        }
    }

    public void setOnScrollChangedListener(OnScrollChangedListener listener) {
        mOnScrollChangedListener = listener;
    }

    @Override
    public void fling(int velocityY) {
        super.fling(velocityY);
    }

    @Override
    public boolean fullScroll(int direction){
        super.fullScroll(direction);
        return true;
    }
}
