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

import com.easy.view.listener.HexKeyboardListener;
import com.easy.view.utils.EasyUtils;

import java.util.Arrays;

/**
 * Bluetooth address input box
 *
 * @author llw
 * @since 2023/3/24
 */
public class MacAddressEditText extends View implements HexKeyboardListener {


    /**
     * Context
     */
    private Context mContext;
    /**
     * The size of the box, because it's the same width and height, it only needs one value
     */
    private int mBoxWidth;
    /**
     * The background color of the box
     */
    private final int mBoxBackgroundColor;
    /**
     * The border color of the box
     */
    private final int mBoxStrokeColor;
    /**
     * Square stroke size
     */
    private final int mBoxStrokeWidth;
    /**
     * Text color
     */
    private final int mTextColor;
    /**
     * Text size
     */
    private final float mTextSize;
    /**
     * Separator
     */
    private final String mSeparator;

    /**
     * Number of boxes
     */
    private final int mBoxNum = 6;
    /**
     * The spacing of each box
     */
    private int mBoxMargin = 4;
    /**
     * Box Paint
     */
    private Paint mBoxPaint;
    /**
     * Box Stroke Paint
     */
    private Paint mBoxStrokePaint;
    /**
     * Text Paint
     */
    private Paint mTextPaint;
    /**
     * Text rectangle box
     */
    private final Rect mTextRect = new Rect();
    /**
     * Square with rounded corners
     */
    private float mBoxCornerRadius = 8f;
    /**
     * The default length of the Mac address is 12,which depends on the actual input.
     * If the MAC address is incomplete,remove a single character from the box,
     * for example, 01 02 0. The final result is 01 02.
     */
    private final int mMacLength = 6;
    /**
     * Mac address array
     */
    private final String[] macAddressArray = new String[mMacLength];
    /**
     * Input length
     */
    private final int mInputLength = 12;
    /**
     * Input array
     */
    private final String[] inputArray = new String[mInputLength];
    /**
     * Current input position
     */
    private int currentInputPosition = 0;
    /**
     * Operation identifiers -1: add, 0: delete, 1: delete all
     */
    private int flag = -1;

    /**
     * Constructor 1
     * is used in code, such as Java's new MacEditText(), Kotlin's MacEditText()
     */
    public MacAddressEditText(Context context) {
        this(context, null);
    }

    /**
     * Constructor 2
     * Automatically called when used in an xml layout file
     */
    public MacAddressEditText(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * Constructor 3
     * is not called automatically; it is called in the second constructor if there is a default style
     */
    public MacAddressEditText(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        //The drawing parameters of the View are set according to the style set
        @SuppressLint("CustomViewStyleable")
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MacAddressEditText);
        mBoxWidth = (int) typedArray.getDimensionPixelSize(R.styleable.MacAddressEditText_boxWidth, 48);
        mBoxBackgroundColor = typedArray.getColor(R.styleable.MacAddressEditText_boxBackgroundColor, ContextCompat.getColor(context, R.color.white));
        mBoxStrokeColor = typedArray.getColor(R.styleable.MacAddressEditText_boxStrokeColor, ContextCompat.getColor(context, R.color.box_default_stroke_color));
        mBoxStrokeWidth = (int) typedArray.getDimensionPixelSize(R.styleable.MacAddressEditText_boxStrokeWidth, 2);
        mTextColor = typedArray.getColor(R.styleable.MacAddressEditText_textColor, ContextCompat.getColor(context, R.color.tx_default_color));
        mTextSize = typedArray.getDimensionPixelSize(R.styleable.MacAddressEditText_textSize, (int) TypedValue
                        .applyDimension(TypedValue.COMPLEX_UNIT_SP, 14, getResources().getDisplayMetrics()));
        mSeparator = typedArray.getString(R.styleable.MacAddressEditText_separator);
        typedArray.recycle();
        //Initializing brush
        initPaint();
    }

    /**
     * Initializing brush
     */
    private void initPaint() {
        //Set the box brush
        mBoxPaint = new Paint();
        mBoxPaint.setAntiAlias(true);// anti-aliasing
        mBoxPaint.setColor(mBoxBackgroundColor);//Set color
        mBoxPaint.setStyle(Paint.Style.FILL);//Style filling
        //Set the box stroke brush
        mBoxStrokePaint = new Paint();
        mBoxStrokePaint.setAntiAlias(true);
        mBoxStrokePaint.setColor(mBoxStrokeColor);
        mBoxStrokePaint.setStyle(Paint.Style.STROKE);//Style stroke
        mBoxStrokePaint.setStrokeWidth(mBoxStrokeWidth);//Stroke width
        //Set text brush
        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setStyle(Paint.Style.FILL);
        mTextPaint.setColor(mTextColor);
        mTextPaint.setTextSize(mTextSize);//Text size
        mTextPaint.setTextAlign(Paint.Align.CENTER);//Center the text
    }

    /**
     * Measurement of View
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
        //Set the measured width and height
        setMeasuredDimension(width, mBoxWidth);
    }

    /**
     * Drawing of View
     */
    @Override
    protected void onDraw(Canvas canvas) {
        //Draw box
        drawBox(canvas);
        //Draw Mac address
        drawMacAddress(canvas);
    }

    /**
     * Draw box
     */
    private void drawBox(Canvas canvas) {
        //The spacing of each box
        int margin = EasyUtils.dp2px(mContext, mBoxMargin);
        for (int i = 0; i < mBoxNum; i++) {
            //To draw a rectangular box, you need the positions of the left, top, right and bottom points
            float left = i * mBoxWidth + i * margin;
            float top = 0f;
            float right = (i + 1) * mBoxWidth + i * margin;
            float bottom = mBoxWidth;
            RectF rectF = new RectF(left, top, right, bottom);
            //Draw a rounded rectangle box
            int radius = EasyUtils.dp2px(mContext, mBoxCornerRadius);
            canvas.drawRoundRect(rectF, radius, radius, mBoxPaint);
            //Draw a rounded rectangle border
            float strokeWidth = mBoxStrokeWidth / 2;
            RectF strokeRectF = new RectF(left + strokeWidth, top + strokeWidth, right - strokeWidth, bottom - strokeWidth);
            float strokeRadius = radius - strokeWidth;
            canvas.drawRoundRect(strokeRectF, strokeRadius, strokeRadius, mBoxStrokePaint);
        }
    }

    /**
     * Draw Mac address
     */
    private void drawMacAddress(Canvas canvas) {
        int boxMargin = EasyUtils.dp2px(mContext, mBoxMargin);
        for (int i = 0; i < macAddressArray.length; i++) {
            if (macAddressArray[i] != null) {
                //Drawn text
                String content = macAddressArray[i];
                //Gets the drawn text boundary
                mTextPaint.getTextBounds(content, 0, content.length(), mTextRect);
                //Drawn position
                int offset = (mTextRect.top + mTextRect.bottom) / 2;
                //Draw text, need to determine the starting point of X, Y coordinate points
                float x = (float) (getPaddingLeft() + mBoxWidth * i + boxMargin * i + mBoxWidth / 2);
                float y = (float) (getPaddingTop() + mBoxWidth / 2) - offset;
                //Draw text
                canvas.drawText(content, x, y, mTextPaint);
            }
        }
    }


    /**
     * Touch event
     */
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event != null) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                //Displays the Hex keyboard Dialog
                EasyUtils.showHexKeyboardDialog(mContext, this);
                return true;
            }
        }
        return super.onTouchEvent(event);
    }

    /**
     * Handles Mac address drawing
     */
    private void processMacDraw() {
        if (flag == 1) {    //Complete deletion
            currentInputPosition = 0;
            Arrays.fill(inputArray, null);
            Arrays.fill(macAddressArray, "");
        } else {    //Add or remove
            String hex = "";
            int hexPos = 0;
            for (String input : inputArray) {
                if (input == null) {
                    input = "";
                }
                hex = hex + input;
                macAddressArray[hexPos] = hex;
                if (hex.length() == 2) {
                    hexPos++;
                    hex = "";
                }
            }
        }
        //Refresh View
        postInvalidate();
    }

    @Override
    public void onHex(String hex) {
        //The input length must be 12
        if (currentInputPosition == mInputLength) return;
        inputArray[currentInputPosition] = hex;
        currentInputPosition++;
        flag = -1;
        processMacDraw();   //Draw on add
    }

    @Override
    public void onDelete() {
        if (currentInputPosition == 0) return;
        currentInputPosition--;
        inputArray[currentInputPosition] = null;
        flag = 0;
        processMacDraw();   //Draw on delete
    }

    @Override
    public void onDeleteAll() {
        flag = 1;
        processMacDraw();   //Draw when all is deleted
    }


    @Override
    public void onComplete() {
        Log.d("TAG", "onComplete: " + getMacAddress());
    }

    /**
     * Obtaining a Mac address
     *
     * @return Mac address
     */
    public String getMacAddress() {
        StringBuilder builder = new StringBuilder();
        for (String macAddress : macAddressArray) {
            if (macAddress == null) continue;
            if (macAddress.isEmpty()) continue;
            if (builder.toString().isEmpty()) {
                builder.append(macAddress);
            } else {
                builder.append(mSeparator == null ? ":" : mSeparator).append(macAddress);
            }
        }
        return builder.toString();
    }
}
