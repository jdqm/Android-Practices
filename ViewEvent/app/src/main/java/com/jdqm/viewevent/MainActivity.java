package com.jdqm.viewevent;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.TextView;

import com.jdqm.viewevent.view.TestButton;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private TextView tvTest;

    private VelocityTracker vTracker = VelocityTracker.obtain();

    private TestButton testButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        testButton = findViewById(R.id.testButton);
        testButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                testButton.smoothScrollTo(1000, 500, 1000);
            }
        });
        tvTest = findViewById(R.id.tvTest);
        tvTest.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Thread.dumpStack();
                vTracker.addMovement(event);
                vTracker.computeCurrentVelocity(1000);
                float xVelocity = vTracker.getXVelocity();
                Log.d(TAG, "xVelocity: " + xVelocity);
                return true;
            }
        });
    }

    public void test(View view) {

        //两点可以确定一个矩形（left, top）(right, bottom)
        Log.d(TAG, "View top: " + tvTest.getTop());
        Log.d(TAG, "View left: " + tvTest.getLeft());
        Log.d(TAG, "View right: " + tvTest.getRight());
        Log.d(TAG, "View bottom: " + tvTest.getBottom());
        // x = left + translationX
        // y = top + translationY
        Log.d(TAG, "View x: " + tvTest.getX());
        Log.d(TAG, "View y: " + tvTest.getY());
        Log.d(TAG, "View translationX: " + tvTest.getTranslationX());
        Log.d(TAG, "View translationY: " + tvTest.getTranslationY());
        if (Build.VERSION.SDK_INT >= 21) {
            Log.d(TAG, "View translationZ: " + tvTest.getTranslationZ());
        }

        tvTest.setTranslationX(100);
        tvTest.invalidate();
        Log.d(TAG, "View x: " + tvTest.getX());
    }

    @Override
    protected void onDestroy() {
        //重置
        vTracker.clear();
        //将其归还给VelocityTracker池
        vTracker.recycle();
        super.onDestroy();
    }

    public void test2(View view) {
        Log.d(TAG, "X: " + testButton.getX());
        Log.d(TAG, "Y: " + testButton.getY());

        //testButton.smoothScrollTo(200, 0, 2000);

        //这两个方法都时移动View的内容而已
        //testButton.scrollTo(100, 0);
        //testButton.scrollBy(100, 0);

        //使用View动画移动View，只是影像移动,而View的位置参数,事件响应还在原来的位置
//        TranslateAnimation ta = new TranslateAnimation(0, 500, 0, 0);
//        ta.setDuration(1000);
//        ta.setInterpolator(new AccelerateDecelerateInterpolator());
//        ta.setFillAfter(true);
//        testButton.startAnimation(ta);

         //在xml中定义View动画
//        Animation animation = AnimationUtils.loadAnimation(this, R.anim.translation_x);
//        testButton.startAnimation(animation);


        //使用属性动画，真正改变View的位置参数，事件响应在新的位置
//        ObjectAnimator animator = ObjectAnimator.ofFloat(testButton, "translationX", 0, 500);
//        animator.setDuration(100);
//        animator.setInterpolator(new AccelerateDecelerateInterpolator());
//        animator.start();

        //通过修改布局参数来移动View,但是这种方式不能做到平滑
        final ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) testButton.getLayoutParams();
//        layoutParams.leftMargin += 200;
//        testButton.requestLayout();
//        //testButton.setLayoutParams(layoutParams);

        //当然，上面这种方式也可以将平移距离分割成很多小的单位，已达到平滑效果
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 1);
        valueAnimator.setDuration(1000);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Log.d(TAG, "onAnimationUpdate: " + animation.getAnimatedFraction());
                float distance = animation.getAnimatedFraction() * 200;
                layoutParams.leftMargin = (int)distance;
                testButton.requestLayout();
            }
        });
        valueAnimator.start();

    }
}
