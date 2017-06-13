package com.example.zhangwenchi.FAB;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.util.Timer;
import java.util.TimerTask;

/*
*  this activity is just for test, to simulate the writing interface
*  here is a small icon scale from 0 to 1,
*  it is a add button, click it can upload attachment etc.
*  but not do this function
* */
public class SecondActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

//        ImageView image = (ImageView) findViewById(R.id.addButton);
//        final ScaleAnimation animation = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f, Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
//        animation.setDuration(600);
//        image.setAnimation(animation);
//        animation.start();
        final EditText editText = (EditText)findViewById(R.id.edit_text);
        Timer timer = new Timer();
        timer.schedule(new TimerTask()
        {
            public void run()
            {
                InputMethodManager inputManager = (InputMethodManager)editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.showSoftInput(editText, 0);
            }
            }, 400);
    }



}
