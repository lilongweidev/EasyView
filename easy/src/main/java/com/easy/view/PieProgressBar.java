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

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

/**
 * 饼状进度条
 *
 * @author llw
 * @since 2023/5/31
 */
public class PieProgressBar extends View {

    /**
     * 半径
     */
    private int mRadius;
    /**
     * 进度条宽度
     */
    private int mStrokeWidth;
    /**
     * 进度条进度颜色
     */
    private int mProgressColor;
    /**
     * 开始角度
     */
    private int mStartAngle = 0;

    /**
     * 当前角度
     */
    private float mCurrentAngle = 0;
    /**
     * 结束角度
     */
    private int mEndAngle = 360;
    /**
     * 最大进度
     */
    private float mMaxProgress;
    /**
     * 当前进度
     */
    private float mCurrentProgress;
    /**
     * 是否渐变
     */
    private boolean isGradient;
    /**
     * 渐变颜色数组
     */
    private int[] colorArray;
    /**
     * 动画的执行时长
     */
    private long mDuration = 1000;
    /**
     * 是否执行动画
     */
    private boolean isAnimation = false;

    public PieProgressBar(Context context) {
        this(context, null);
    }

    public PieProgressBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PieProgressBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.PieProgressBar);
        mRadius = array.getDimensionPixelSize(R.styleable.PieProgressBar_radius, 80);
        mStrokeWidth = array.getDimensionPixelSize(R.styleable.PieProgressBar_strokeWidth, 8);
        mProgressColor = array.getColor(R.styleable.PieProgressBar_progressbarColor, ContextCompat.getColor(context, R.color.tx_default_color));
        mMaxProgress = array.getInt(R.styleable.PieProgressBar_maxProgress, 100);
        mCurrentProgress = array.getInt(R.styleable.PieProgressBar_progress, 0);
        //是否渐变
        isGradient = array.getBoolean(R.styleable.PieProgressBar_gradient, false);
        //渐变颜色数组
        CharSequence[] textArray = array.getTextArray(R.styleable.PieProgressBar_gradientColorArray);
        if (textArray != null) {
            colorArray = new int[textArray.length];
            for (int i = 0; i < textArray.length; i++) {
                colorArray[i] = Color.parseColor((String) textArray[i]);
            }
        }
        mStartAngle = array.getInt(R.styleable.PieProgressBar_customAngle, 0);
        array.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = 0;
        switch (MeasureSpec.getMode(widthMeasureSpec)) {
            case MeasureSpec.UNSPECIFIED:
            case MeasureSpec.AT_MOST:   //wrap_content
                width = mRadius * 2;
                break;
            case MeasureSpec.EXACTLY:   //match_parent
                width = MeasureSpec.getSize(widthMeasureSpec);
                break;
        }
        setMeasuredDimension(width, width);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int centerX = getWidth() / 2;
        @SuppressLint("DrawAllocation")
        RectF rectF = new RectF(0,0,centerX * 2,centerX * 2);
        //绘制描边
        drawStroke(canvas, centerX);
        //绘制进度
        drawProgress(canvas, rectF);
    }

    /**
     * 绘制描边
     *
     * @param canvas 画布
     * @param centerX 中心点
     */
    private void drawStroke(Canvas canvas, int centerX) {
        Paint paint = new Paint();
        paint.setColor(mProgressColor);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(mStrokeWidth);
        paint.setAntiAlias(true);
        canvas.drawCircle(centerX, centerX, mRadius - (mStrokeWidth / 2), paint);
    }

    /**
     * 绘制进度条背景
     */
    private void drawProgress(Canvas canvas, RectF rectF) {
        Paint paint = new Paint();
        //画笔的填充样式，Paint.Style.STROKE 描边
        paint.setStyle(Paint.Style.FILL);
        //抗锯齿
        paint.setAntiAlias(true);
        //画笔的颜色
        paint.setColor(mProgressColor);
        //是否设置渐变
        if (isGradient && colorArray != null) {
            paint.setShader(new RadialGradient(rectF.centerX(), rectF.centerY(), mRadius, colorArray, null, Shader.TileMode.MIRROR));
        }
        if (!isAnimation) {
            mCurrentAngle = 360 * (mCurrentProgress / mMaxProgress);
        }
        //开始画圆弧
        canvas.drawArc(rectF, mStartAngle, mCurrentAngle, true, paint);
    }

    /**
     * 设置角度
     * @param angle 角度
     */
    public void setCustomAngle(int angle) {
        if (angle >= 0 && angle < 90) {
            mStartAngle = 0;
        } else if (angle >= 90 && angle < 180) {
            mStartAngle = 90;
        } else if (angle >= 180 && angle < 270) {
            mStartAngle = 180;
        } else if (angle >= 270 && angle < 360) {
            mStartAngle = 270;
        } else if (angle >= 360) {
            mStartAngle = 0;
        }
        invalidate();
    }

    /**
     * 设置是否渐变
     */
    public void setGradient(boolean gradient) {
        isGradient = gradient;
        invalidate();
    }

    /**
     * 设置渐变的颜色
     */
    public void setColorArray(int[] colorArr) {
        if (colorArr == null) return;
        colorArray = colorArr;
    }

    /**
     * 设置当前进度
     */
    public void setProgress(float progress) {
        if (progress < 0) {
            throw new IllegalArgumentException("Progress value can not be less than 0");
        }
        if (progress > mMaxProgress) {
            progress = mMaxProgress;
        }
        mCurrentProgress = progress;
        mCurrentAngle = 360 * (mCurrentProgress / mMaxProgress);
        setAnimator(mStartAngle, mCurrentAngle);
    }

    /**
     * 设置动画
     *
     * @param start  开始位置
     * @param target 结束位置
     */
    private void setAnimator(float start, float target) {
        isAnimation = true;
        ValueAnimator animator = ValueAnimator.ofFloat(start, target);
        animator.setDuration(mDuration);
        animator.setTarget(mCurrentAngle);
        //动画更新监听
        animator.addUpdateListener(valueAnimator -> {
            mCurrentAngle = (float) valueAnimator.getAnimatedValue();
            invalidate();
        });
        animator.start();
    }
}
