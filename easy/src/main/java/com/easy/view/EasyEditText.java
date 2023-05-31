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
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import androidx.core.content.ContextCompat;

import com.easy.view.listener.NumberKeyboardListener;
import com.easy.view.utils.EasyUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 简化输入框，可用于密码框、验证码框
 * @author llw
 * @since 2023/4/30
 */
public class EasyEditText extends View implements NumberKeyboardListener {

    /**
     * Context
     */
    private Context mContext;
    /**
     * 方框大小，因为它是相同的宽度和高度，它只需要一个值
     */
    private int mBoxWidth;
    /**
     * 方框背景颜色
     */
    private int mBoxBackgroundColor;
    /**
     * 方框默认描边颜色
     */
    private int mBoxStrokeColor;
    /**
     * 方框获取焦点描点颜色
     */
    private int mBoxFocusStrokeColor;
    /**
     * 方框描边大小
     */
    private final int mBoxStrokeWidth;
    /**
     * 文字颜色
     */
    private int mTextColor;
    /**
     * 文字大小
     */
    private float mTextSize;
    /**
     * 方框数量，最少4个 - 最多6个
     */
    private int mBoxNum;
    /**
     * 方框之间的间距
     */
    private int mBoxMargin = 4;
    /**
     * 方框画笔
     */
    private Paint mBoxPaint;
    /**
     * 方框描边画笔
     */
    private Paint mBoxStrokePaint;
    /**
     * 文字画笔
     */
    private Paint mTextPaint;
    /**
     * 文字矩形
     */
    private final Rect mTextRect = new Rect();
    /**
     * 方框圆角
     */
    private float mBoxCornerRadius = 8f;
    /**
     * 描边圆角
     */
    private float strokeRadius;

    /**
     * 输入长度
     */
    private final int mInputLength;
    /**
     * 输入数组
     */
    private final String[] inputArray;
    /**
     * 当前输入位置
     */
    private int currentInputPosition = 0;
    /**
     * 焦点边框列表
     */
    private final List<RectF> focusList = new ArrayList<>();
    /**
     * 是否获取焦点
     */
    private boolean isFocus = false;
    /**
     * 是否密文显示
     */
    private boolean ciphertext = false;
    /**
     * 密文显示 *
     */
    private String ciphertextContent = "*";

    public EasyEditText(Context context) {
        this(context, null);
    }

    public EasyEditText(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EasyEditText(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        @SuppressLint("CustomViewStyleable")
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.EasyEditText);
        mBoxWidth = (int) typedArray.getDimensionPixelSize(R.styleable.EasyEditText_boxWidth, 48);
        mBoxBackgroundColor = typedArray.getColor(R.styleable.EasyEditText_boxBackgroundColor, ContextCompat.getColor(context, R.color.white));
        mBoxStrokeColor = typedArray.getColor(R.styleable.EasyEditText_boxStrokeColor, ContextCompat.getColor(context, R.color.box_default_stroke_color));
        mBoxFocusStrokeColor = typedArray.getColor(R.styleable.EasyEditText_boxFocusStrokeColor, ContextCompat.getColor(context, R.color.box_default_stroke_color));
        mBoxStrokeWidth = (int) typedArray.getDimensionPixelSize(R.styleable.EasyEditText_boxStrokeWidth, 2);
        mTextColor = typedArray.getColor(R.styleable.EasyEditText_textColor, ContextCompat.getColor(context, R.color.tx_default_color));
        mTextSize = typedArray.getDimensionPixelSize(R.styleable.EasyEditText_textSize, (int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_SP, 14, getResources().getDisplayMetrics()));
        int number = typedArray.getInteger(R.styleable.EasyEditText_boxNum, 4);
        ciphertext = typedArray.getBoolean(R.styleable.EasyEditText_ciphertext, false);
        mBoxNum = (number > 6 || number < 4) ? 4 : number;
        mInputLength = mBoxNum;
        inputArray = new String[mInputLength];
        typedArray.recycle();
        //初始化画笔
        initPaint();
    }

    /**
     * Initializing brush
     */
    private void initPaint() {
        //设置边框画笔
        mBoxPaint = new Paint();
        mBoxPaint.setAntiAlias(true);// anti-aliasing
        mBoxPaint.setColor(mBoxBackgroundColor);//Set color
        mBoxPaint.setStyle(Paint.Style.FILL);//Style filling
        //设置描边画笔
        mBoxStrokePaint = new Paint();
        mBoxStrokePaint.setAntiAlias(true);
        mBoxStrokePaint.setColor(mBoxStrokeColor);
        mBoxStrokePaint.setStyle(Paint.Style.STROKE);//Style stroke
        mBoxStrokePaint.setStrokeWidth(mBoxStrokeWidth);//Stroke width
        //设置文字画笔
        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setStyle(Paint.Style.FILL);
        mTextPaint.setColor(mTextColor);
        mTextPaint.setTextSize(mTextSize);//Text size
        mTextPaint.setTextAlign(Paint.Align.CENTER);//Center the text
    }

    /**
     * 测量
     *
     * @param widthMeasureSpec  Width measurement
     * @param heightMeasureSpec Height measurement
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = 0;
        int margin = EasyUtils.dp2px(mContext, mBoxMargin);
        switch (MeasureSpec.getMode(widthMeasureSpec)) {
            case MeasureSpec.UNSPECIFIED:
            case MeasureSpec.AT_MOST:   //wrap_content
                width = mBoxWidth * mBoxNum + margin * (mBoxNum - 1);
                break;
            case MeasureSpec.EXACTLY:   //match_parent
                width = MeasureSpec.getSize(widthMeasureSpec);
                mBoxWidth = (width - margin * (mBoxNum - 1)) / mBoxNum;
                break;
        }
        //设置测量后的值
        setMeasuredDimension(width, mBoxWidth);
    }

    /**
     * 绘制
     */
    @Override
    protected void onDraw(Canvas canvas) {
        //绘制边框
        drawBox(canvas);
        //绘制文字
        drawText(canvas);
    }

    /**
     * 绘制方框
     */
    private void drawBox(Canvas canvas) {
        //每个方框的间距
        int margin = EasyUtils.dp2px(mContext, mBoxMargin);
        int radius = EasyUtils.dp2px(mContext, mBoxCornerRadius);
        //Draw a rounded rectangle border
        float strokeWidth = mBoxStrokeWidth / 2;
        for (int i = 0; i < mBoxNum; i++) {
            //To draw a rectangular box, you need the positions of the left, top, right and bottom points
            float left = i * mBoxWidth + i * margin;
            float top = 0f;
            float right = (i + 1) * mBoxWidth + i * margin;
            float bottom = mBoxWidth;
            RectF rectF = new RectF(left, top, right, bottom);
            //画一个圆角矩形框
            canvas.drawRoundRect(rectF, radius, radius, mBoxPaint);
            RectF strokeRectF = new RectF(left + strokeWidth, top + strokeWidth, right - strokeWidth, bottom - strokeWidth);
            //添加到列表
            focusList.add(strokeRectF);
        }

        for (int i = 0; i < mBoxNum; i++) {
            strokeRadius = radius - strokeWidth;
            //根据当前绘制位置和是否获取焦点设置画笔颜色
            if (i <= currentInputPosition && isFocus) {
                mBoxStrokePaint.setColor(mBoxFocusStrokeColor);
            } else {
                mBoxStrokePaint.setColor(mBoxStrokeColor);
            }
            //绘制边框
            canvas.drawRoundRect(focusList.get(i), strokeRadius, strokeRadius, mBoxStrokePaint);
        }
    }

    /**
     * 绘制文字
     */
    private void drawText(Canvas canvas) {
        int boxMargin = EasyUtils.dp2px(mContext, mBoxMargin);
        for (int i = 0; i < inputArray.length; i++) {
            if (inputArray[i] != null) {
                //绘制的文字
                String content = ciphertext ? ciphertextContent : inputArray[i];
                //获取绘制的文字边界
                mTextPaint.getTextBounds(content, 0, content.length(), mTextRect);
                //绘制的位置
                int offset = (mTextRect.top + mTextRect.bottom) / 2;
                //绘制文字，需要确定起始点的X、Y的坐标点
                float x = (float) (getPaddingLeft() + mBoxWidth * i + boxMargin * i + mBoxWidth / 2);
                float y = (float) (getPaddingTop() + mBoxWidth / 2) - offset;
                //绘制文字
                canvas.drawText(content, x, y, mTextPaint);
            }
        }
    }

    /**
     * 触摸事件
     */
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event != null) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                //显示数字键盘
                EasyUtils.showNumKeyboardDialog(mContext, this);
                return true;
            }
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void onNum(String num) {
        if (currentInputPosition == mInputLength) return;
        inputArray[currentInputPosition] = num;
        currentInputPosition++;
        //Refresh View
        postInvalidate();
    }

    @Override
    public void onDelete() {
        if (currentInputPosition == 0) return;
        currentInputPosition--;
        inputArray[currentInputPosition] = null;
        //Refresh View
        postInvalidate();
    }

    @Override
    public void onComplete() {
        Log.d("TAG", "onComplete: " + getText());
    }

    @Override
    public void onDialogShow() {
        isFocus = true;
        postInvalidate();
    }

    @Override
    public void onDialogDismiss() {
        isFocus = false;
        postInvalidate();
    }

    /**
     * 设置输入框个数
     */
    public void setBoxNum(int num) {
        if (num < 4 || num > 6) {
            throw new IllegalArgumentException("The number of input boxes ranges from 4 to 6");
        }
        mBoxNum = num;
    }

    /**
     * 获取输入总长度
     */
    public int getBoxNum() {
        return mBoxNum;
    }

    /**
     * 设置是否密文
     * @param flag true 密文、false 明文
     */
    public void setCiphertext(boolean flag) {
        ciphertext = flag;
        postInvalidate();
    }

    /**
     * 设置密文时显示的内容
     * @param content 密文内容，默认是 *
     */
    public void setCiphertextContent(String content) {
        if (content == null) return;
        if (content.isEmpty()) return;
        if (content.length() > 1) return;
        ciphertextContent = content;
    }

    /**
     * 获取输入内容
     */
    public String getText() {
        StringBuilder builder = new StringBuilder();
        for (String number : inputArray) {
            if (number == null) continue;
            if (number.isEmpty()) continue;
            builder.append(number);
        }
        return builder.toString();
    }
}
