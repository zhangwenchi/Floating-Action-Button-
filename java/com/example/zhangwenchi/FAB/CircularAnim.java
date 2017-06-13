package com.example.zhangwenchi.FAB;

/**
 * Created by zhangwc on 2017/6/3.
 */


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.ImageView;

public class CircularAnim {

    public interface OnAnimationEndListener {
        void onAnimationEnd();
    }

    public static class FullActivityBuilder {
        private Activity mActivity;
        private View mTriggerView;
        private float mStartRadius = 5;  // it odd to start with 0
        private int mColorOrImageRes;
        private long mDurationMills = 300;
        private OnAnimationEndListener mOnAnimationEndListener;
        private int mEnterAnim = android.R.anim.fade_in, mExitAnim = android.R.anim.fade_out;

        public FullActivityBuilder(Activity activity, View triggerView) {
            mActivity = activity;
            mTriggerView = triggerView;
        }

        // can set start Radius, start color , duration, and in & out animation
        public FullActivityBuilder startRadius(float startRadius) {
            mStartRadius = startRadius;
            return this;
        }

        public FullActivityBuilder colorOrImageRes(int colorOrImageRes) {
            mColorOrImageRes = colorOrImageRes;
            return this;
        }

        public FullActivityBuilder duration(long durationMills) {
            mDurationMills = durationMills;
            return this;
        }

        public FullActivityBuilder overridePendingTransition(int enterAnim, int exitAnim) {
            mEnterAnim = enterAnim;
            mExitAnim = exitAnim;
            return this;
        }

        public void go(OnAnimationEndListener onAnimationEndListener) {
            mOnAnimationEndListener = onAnimationEndListener;

            // this is the absolute coordinates on the screen
            int[] location = new int[2];
            mTriggerView.getLocationInWindow(location);
            // get the center of the view
            final int cx = location[0] + mTriggerView.getWidth() / 2;
            final int cy = location[1] + mTriggerView.getHeight() / 2;
            final ImageView view = new ImageView(mActivity);
            view.setScaleType(ImageView.ScaleType.CENTER_CROP);
            view.setBackgroundColor(mColorOrImageRes);

            // get the width & height besides the title & sth else
            final ViewGroup decorView = (ViewGroup) mActivity.getWindow().getDecorView();
            int w = decorView.getWidth();
            int h = decorView.getHeight();
            decorView.addView(view, w, h);

            // calculate the max(center pivot to the bounds)
            int maxW = Math.max(cx, w - cx);
            int maxH = Math.max(cy, h - cy);
            final int finalRadius = (int) Math.sqrt(maxW * maxW + maxH * maxH) + 1;

            try {
                Animator anim = ViewAnimationUtils.createCircularReveal(view, cx, cy, mStartRadius, finalRadius);
                final long finalDuration = mDurationMills;
                // startActivity()will delay the exit animation, so make a small balance
                anim.setDuration((long) (finalDuration * 0.9));
                anim.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        doOnEnd();

                        mActivity.overridePendingTransition(mEnterAnim, mExitAnim);

                        // this is not change the activity
                        mTriggerView.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (mActivity.isFinishing()) return; // if go to another activity

                                try {
                                    Animator anim = ViewAnimationUtils.createCircularReveal(view, cx, cy,
                                            finalRadius, mStartRadius);
                                    anim.setDuration(finalDuration);
                                    anim.addListener(new AnimatorListenerAdapter() {
                                        @Override
                                        public void onAnimationEnd(Animator animation) {
                                            super.onAnimationEnd(animation);
                                            try {
                                                decorView.removeView(view);
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    });
                                    anim.start();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    try {
                                        decorView.removeView(view);
                                    } catch (Exception e1) {
                                        e1.printStackTrace();
                                    }
                                }
                            }
                        }, 400);

                    }
                });
                anim.start();
            } catch (Exception e) {
                e.printStackTrace();
                doOnEnd();
            }
        }

        private void doOnEnd() {
            mOnAnimationEndListener.onAnimationEnd();
        }
    }

    // call this function outside, go to another activity.
    public static FullActivityBuilder fullActivity(Activity activity, View triggerView) {
        return new FullActivityBuilder(activity, triggerView);
    }
}
