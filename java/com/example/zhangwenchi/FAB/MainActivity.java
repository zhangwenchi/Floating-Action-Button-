package com.example.zhangwenchi.FAB;

import android.animation.Animator;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewAnimationUtils;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends Activity {

    private FloatingActionsMenu menuMultipleActions;
    private ImageView ic_closed;
    private ImageView ic_compose;
    private View secondView;

    @Override
  protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_main);

      /*
      * This is simulating that if user read a email, we will change the activity
      * and get another FAB : back-to-top FAB
      * */
      final Button readbutton = (Button) findViewById(R.id.buttonTurn);
      readbutton.setOnClickListener(new OnClickListener() {
          @Override
          public void onClick(View view) {
              startActivity(new Intent(MainActivity.this, BackTopActivity.class));
          }
      });

      /*
      *  for test I create two email buttons dynamic,
      * assume that I get all the email address of the user and I konw all the icon name
      * */
      String[] emailAddress = {"Very very long email address","short address"};
      String[] iconName = {"test0","test2"};

      // this is the menu button
      menuMultipleActions = (FloatingActionsMenu) findViewById(R.id.multiple_actions);
      ic_closed = (ImageView) findViewById(R.id.closeAddButton);
      ic_compose = (ImageView) findViewById(R.id.composeAddButton);

      for(int i = 0;i < emailAddress.length; i++){
          final FloatingActionButton actionA = new FloatingActionButton(getBaseContext());
          actionA.setIconDrawable(getResources().getDrawable(getResources().getIdentifier(iconName[i] , "drawable", getPackageName())));
          actionA.xiushi(i+1);                 /// make the icon from rectangle to circle --> and draw it center of the icon
          actionA.setTitle(emailAddress[i]);
          actionA.setColorPressed(R.color.transparent);
          actionA.setSize(FloatingActionButton.SIZE_MINI);  // the email icon should be mini
          menuMultipleActions.addButton(actionA);            // add it to the menu
          actionA.setOnClickListener(new OnClickListener() {
              @Override
              public void onClick(View view) {
                  CircularAnim.fullActivity(MainActivity.this, view)
                          .colorOrImageRes(Color.parseColor("#FFFFFFFF"))        /// define the color of Water Ripple
                          .go(new CircularAnim.OnAnimationEndListener() {
                              @Override
                              public void onAnimationEnd() {
                                  startActivity(new Intent(MainActivity.this, SecondActivity.class));  /// go to the writing activity
                                  /// it's easy to give parameter to another activity by Bundle
                              }
                          });
              }
          });
      }

      /*
      *  the icon of menu button is the ImageView located at the right-bottom
      *  click the menu button to change the icon(by animationSet)
      * */
        secondView = findViewById(R.id.second);
      ic_closed.setVisibility(View.INVISIBLE);
      menuMultipleActions.getAddButton().setOnClickListener(new OnClickListener() {

          @Override
          public void onClick(View view) {
              menuMultipleActions.toggle();       // show the email icon

              int cx = ic_compose.getLeft() + ic_compose.getWidth()/2;
              int cy = ic_compose.getTop() + ic_compose.getHeight()/2;
              float finalRadius = (float) Math.hypot((double) cx, (double) cy);
              if (!menuMultipleActions.isExpanded()) {
                  expand(cx, cy, finalRadius);
              } else {
                  collapse(cx, cy, finalRadius);
              }
          }
      });
  }

    @Override
    public void onBackPressed() {
        int cx = ic_compose.getLeft() + ic_compose.getWidth()/2;
        int cy = ic_compose.getTop() + ic_compose.getHeight()/2;
        float finalRadius = (float) Math.hypot((double) cx, (double) cy);
        if(menuMultipleActions.isExpanded()){
            menuMultipleActions.toggle();
            expand(cx, cy, finalRadius);
        }
        else super.onBackPressed();
    }

    public void expand(int cx, int cy, float finalRadius){

        Animator mCircularReveal = ViewAnimationUtils.createCircularReveal(secondView, cx, cy, finalRadius, 0);
        mCircularReveal.setDuration(300).start();
        Handler handler = new Handler();     // I should set the secondView invisible after the animation over, I just wait sort of time.
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                secondView.setVisibility(View.INVISIBLE);
            }
        }, 280);

        final AlphaAnimation animation = new AlphaAnimation(1, 0);
        animation.setDuration(300);
        animation.setFillAfter(true);
        ic_closed.startAnimation(animation);                          // ic_closed disappear
        AlphaAnimation animation1 = new AlphaAnimation(0, 1);  // ic_compose start animation, rotate and alpha
        RotateAnimation animation2 = new RotateAnimation(90, 0, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);

        AnimationSet animationSet = new AnimationSet(true);
        animationSet.addAnimation(animation1);
        animationSet.addAnimation(animation2);
        animationSet.setDuration(300);
        animationSet.setFillAfter(true);
        ic_compose.startAnimation(animationSet);
    }

    public void collapse(int cx, int cy, float finalRadius){

        secondView.setVisibility(View.VISIBLE);
        Animator mCircularReveal = ViewAnimationUtils.createCircularReveal(secondView, cx, cy, 0, finalRadius);
        mCircularReveal.setDuration(300).start();
        final AlphaAnimation animation = new AlphaAnimation(0, 1);
        animation.setDuration(300);
        animation.setFillAfter(true);
        ic_closed.startAnimation(animation);
        AlphaAnimation animation1 = new AlphaAnimation(1, 0);
        RotateAnimation animation2 = new RotateAnimation(0, 90, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);

        AnimationSet animationSet = new AnimationSet(true);
        animationSet.addAnimation(animation1);
        animationSet.addAnimation(animation2);
        animationSet.setDuration(300);
        animationSet.setFillAfter(true);
        ic_compose.startAnimation(animationSet);
    }

    // the water ripple of the menu button, if clicked ,just like onBackPressed
    public void clickShadow(View view){
        onBackPressed();
    }
}


 /*
    * This code is to select the most common color in the small icon and use it to be the Ripple Effect color.
    * But at last we choose to use white for all kinds of icon
    * use the color by Color.parseColor(colorB) if the color type is string
    * */
//    String getColor(Drawable drawable) {
//        BitmapDrawable bd = (BitmapDrawable) drawable;
//        Bitmap bitmap = bd.getBitmap();
//        int r[] = new int[256];
//        int g[] = new int[256];
//        int b[] = new int[256];
//        for (int i = 0; i < bitmap.getWidth(); i++) {
//            for (int j = 0; j < bitmap.getHeight(); j++) {
//                int color = bitmap.getPixel(i, j);
//                r[Color.red(color)]++;
//                g[Color.green(color)]++;
//                b[Color.blue(color)]++;
//            }
//        }
//        int maxr = 0, maxg = 0, maxb = 0;
//
//        for (int i = 0; i < 230; i++) {
//            maxr = Math.max(maxr, r[i]);
//            maxg = Math.max(maxg, g[i]);
//            maxb = Math.max(maxb, b[i]);
//        }
//        int ar = 0, ag = 0, ab = 0;
//        for (int i = 0; i < 230; i++) {
//            if (r[i] == maxr) ar = i;
//            if (g[i] == maxg) ag = i;
//            if (b[i] == maxb) ab = i;
//        }
//        String ccccc = "#";
//        if (Integer.toHexString(ar).length() == 1) {
//            ccccc += ("0" + Integer.toHexString(ar));
//        } else ccccc += Integer.toHexString(ar);
//        if (Integer.toHexString(ag).length() == 1) {
//            ccccc += ("0" + Integer.toHexString(ag));
//        } else ccccc += Integer.toHexString(ag);
//        if (Integer.toHexString(ab).length() == 1) {
//            ccccc += ("0" + Integer.toHexString(ab));
//        } else ccccc += Integer.toHexString(ab);
////        System.out.println(ccccc);
//        return ccccc;
//    }




