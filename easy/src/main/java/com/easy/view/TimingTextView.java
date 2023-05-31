/*
 * Copyright (C)  Llw, EasyView Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.easy.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.easy.view.listener.TimingListener;
import com.google.android.material.textview.MaterialTextView;

import java.util.Timer;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 计时TextView，可以正计时、倒计时
 */
public class TimingTextView extends MaterialTextView {

    /**
     * 时间单位
     */
    private int mUnit;
    /**
     * 计时最大值
     */
    private int mMax;
    /**
     * 是否倒计时
     */
    private boolean mCountDown;
    /**
     * 总时间
     */
    private int mTotal;
    /**
     * 是否计时中
     */
    private boolean mTiming;
    /**
     * 计时监听
     */
    private TimingListener listener;
    /**
     * 倒计时器
     */
    private CountDownTimer countDownTimer;

    public TimingTextView(Context context) {
        this(context, null);
    }

    public TimingTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TimingTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        @SuppressLint("CustomViewStyleable")
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.TimingTextView);
        mCountDown = typedArray.getBoolean(R.styleable.TimingTextView_countdown, false);
        mMax = typedArray.getInteger(R.styleable.TimingTextView_max, 60);
        mUnit = typedArray.getInt(R.styleable.TimingTextView_unit, 3);
        typedArray.recycle();
    }

    /**
     * 设置时间单位
     *
     * @param unit 1，2，3
     */
    public void setUnit(int unit) {
        if (unit <= 0 || unit > 3) {
            throw new IllegalArgumentException("unit value can only be between 1 and 3");
        }
        mUnit = unit;
    }

    /**
     * 设置最大时间值
     *
     * @param max 最大值
     */
    public void setMax(int max) {
        mMax = max;
    }

    /**
     * 设置是否为倒计时
     *
     * @param isCountDown true or false
     */
    public void setCountDown(boolean isCountDown) {
        mCountDown = isCountDown;
    }

    public void setListener(TimingListener listener) {
        this.listener = listener;
    }

    public boolean isTiming() {
        return mTiming;
    }

    /**
     * 开始
     */
    public void start() {
        switch (mUnit) {
            case 1:
                mTotal = mMax * 60 * 60 * 1000;
                break;
            case 2:
                mTotal = mMax * 60 * 1000;
                break;
            case 3:
                mTotal = mMax * 1000;
                break;
        }
        if (countDownTimer == null) {
            countDownTimer = new CountDownTimer(mTotal, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    int time = 0;
                    if (mCountDown) {
                        time = (int)  (millisUntilFinished / 1000);
                        setText(String.valueOf(time));
                    } else {
                        time = (int) (mTotal / 1000 - millisUntilFinished / 1000);
                    }
                    setText(String.valueOf(time));
                }

                @Override
                public void onFinish() {
                    //倒计时结束
                    end();
                }
            };
            mTiming = true;
            countDownTimer.start();
        }

    }

    /**
     * 计时结束
     */
    public void end() {
        mTotal = 0;
        mTiming = false;
        countDownTimer.cancel();
        countDownTimer = null;
        if (listener != null) {
            listener.onEnd();
        }
    }
}
