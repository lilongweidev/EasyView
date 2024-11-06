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
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

/**
 * 圆环进度条
 *
 * @author llw
 * @since 2023/4/3
 */
public class CircularProgressBar extends View {

    /**
     * 半径
     */
    private int mRadius;
    /**
     * 进度条宽度
     */
    private int mStrokeWidth;

    /**
     * 进度条背景颜色
     */
    private int mProgressbarBgColor;

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
     * 文字
     */
    private String mText;
    /**
     * 文字颜色
     */
    private int mTextColor;
    /**
     * 文字大小
     */
    private float mTextSize;
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
    /**
     * 是否逆时针绘制
     */
    private boolean isCounterClockwise = false;

    public CircularProgressBar(Context context) {
        this(context, null);
    }

    public CircularProgressBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircularProgressBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.CircularProgressBar);
        mRadius = array.getDimensionPixelSize(R.styleable.CircularProgressBar_radius, 80);
        mStrokeWidth = array.getDimensionPixelSize(R.styleable.CircularProgressBar_strokeWidth, 8);
        mProgressbarBgColor = array.getColor(R.styleable.CircularProgressBar_progressbarBackgroundColor, ContextCompat.getColor(context, R.color.teal_700));
        mProgressColor = array.getColor(R.styleable.CircularProgressBar_progressbarColor, ContextCompat.getColor(context, R.color.teal_200));
        mMaxProgress = array.getInt(R.styleable.CircularProgressBar_maxProgress, 100);
        mCurrentProgress = array.getInt(R.styleable.CircularProgressBar_progress, 0);
        String text = array.getString(R.styleable.CircularProgressBar_text);
        mText = text == null ? "" : text;
        mTextColor = array.getColor(R.styleable.CircularProgressBar_textColor, ContextCompat.getColor(context, R.color.black));
        mTextSize = array.getDimensionPixelSize(R.styleable.CircularProgressBar_textSize, (int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_SP, 14, getResources().getDisplayMetrics()));
        //是否渐变
        isGradient = array.getBoolean(R.styleable.CircularProgressBar_gradient, false);
        //渐变颜色数组
        CharSequence[] textArray = array.getTextArray(R.styleable.CircularProgressBar_gradientColorArray);
        if (textArray != null) {
            colorArray = new int[textArray.length];
            for (int i = 0; i < textArray.length; i++) {
                colorArray[i] = Color.parseColor((String) textArray[i]);
            }
        }
        // 开始角度
        mStartAngle = array.getInt(R.styleable.CircularProgressBar_customAngle, 0);
        // 是否逆时针绘制
        isCounterClockwise = array.getBoolean(R.styleable.CircularProgressBar_counterClockwise, false);
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
        //Set the measured width and height
        setMeasuredDimension(width, width);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int centerX = getWidth() / 2;
        RectF rectF = new RectF();
        rectF.left = mStrokeWidth / 2;
        rectF.top = mStrokeWidth / 2;
        rectF.right = centerX * 2 - mStrokeWidth / 2;
        rectF.bottom = centerX * 2 - mStrokeWidth / 2;

        //绘制进度条背景
        drawProgressbarBg(canvas, rectF);
        //绘制进度
        drawProgress(canvas, rectF);
        //绘制中心文本
        drawCenterText(canvas, centerX);
    }

    /**
     * 绘制进度条背景
     */
    private void drawProgressbarBg(Canvas canvas, RectF rectF) {
        Paint mPaint = new Paint();
        //画笔的填充样式，Paint.Style.STROKE 描边
        mPaint.setStyle(Paint.Style.STROKE);
        //圆弧的宽度
        mPaint.setStrokeWidth(mStrokeWidth);
        //抗锯齿
        mPaint.setAntiAlias(true);
        //画笔的颜色
        mPaint.setColor(mProgressbarBgColor);
        //画笔的样式 Paint.Cap.Round 圆形
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        //开始画圆弧
        canvas.drawArc(rectF, mStartAngle, mEndAngle, false, mPaint);
    }

    /**
     * 绘制进度
     */
    private void drawProgress(Canvas canvas, RectF rectF) {
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(mStrokeWidth);
        paint.setColor(mProgressColor);

        if (isGradient && colorArray != null) {
            paint.setShader(new LinearGradient(0, 0, rectF.right, rectF.top, colorArray, null, Shader.TileMode.MIRROR));
        }

        paint.setAntiAlias(true);
        paint.setStrokeCap(Paint.Cap.ROUND);
        if (!isAnimation) {
            mCurrentAngle = 360 * (mCurrentProgress / mMaxProgress);
        }

        // 根据 isCounterClockwise 调整绘制角度
        float startAngle = mStartAngle;
        float sweepAngle = mCurrentAngle;
        if (isCounterClockwise) {
            startAngle = mStartAngle - 360;
            sweepAngle = -mCurrentAngle;
        }

        canvas.drawArc(rectF, startAngle, sweepAngle, false, paint);
    }

    /**
     * 绘制中心文字
     */
    private void drawCenterText(Canvas canvas, int centerX) {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(mTextColor);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize(mTextSize);
        Rect textBounds = new Rect();
        paint.getTextBounds(mText, 0, mText.length(), textBounds);
        canvas.drawText(mText, centerX, textBounds.height() / 2 + getHeight() / 2, paint);
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
     * 设置文本
     */
    public void setText(String text) {
        mText = text;
    }

    /**
     * 设置文本的颜色
     */
    public void setTextColor(int color) {
        if (color <= 0) {
            throw new IllegalArgumentException("Color value can not be less than 0");
        }
        mTextColor = color;
    }

    /**
     * 设置文本的大小
     */
    public void setTextSize(float textSize) {
        if (textSize <= 0) {
            throw new IllegalArgumentException("textSize can not be less than 0");
        }
        mTextSize = textSize;
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
    }

    /**
     * 设置是否逆时针
     * @param counterClockwise
     */
    public void setCounterClockwise(boolean counterClockwise) {
        isCounterClockwise = counterClockwise;
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
