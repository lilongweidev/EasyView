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
package com.easy.view.chart;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

import com.easy.view.utils.EasyUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 简易图表
 */
public class EasyChart extends View {
    private Context mContext;

    private float mWidth;
    private float mHeight;
    /**
     * X、Y轴画笔
     */
    private Paint mXyPaint;
    /**
     * X、Y轴文字画笔
     */
    private Paint mXyTextPaint;
    /**
     * 图表内填充
     */
    private int mPadding;
    /**
     * 文字矩形
     */
    private final Rect mTextRect = new Rect();

    /**
     * X轴绘制刻度坐标列表
     */
    private List<Point> mXScalePoints;
    /**
     * Y轴绘制刻度坐标列表
     */
    private List<Point> mYScalePoints;
    /**
     * X轴刻度内容列表
     */
    private List<String> mXPointList;
    /**
     * Y轴刻度内容列表
     */
    private List<String> mYPointList;

    public EasyChart(Context context) {
        this(context, null);
    }

    public EasyChart(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EasyChart(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
//        @SuppressLint("CustomViewStyleable")
        initPaint();
    }

    /**
     * 初始化画笔
     */
    private void initPaint() {
        mXyPaint = new Paint();
        mXyPaint.setAntiAlias(true);
        mXyPaint.setColor(Color.BLACK);
        mXyPaint.setStrokeWidth(EasyUtils.dp2px(mContext, 1));
        mXyPaint.setStyle(Paint.Style.FILL);

        mXyTextPaint = new Paint();
        mXyTextPaint.setAntiAlias(true);
        mXyTextPaint.setStyle(Paint.Style.FILL);
        mXyTextPaint.setColor(Color.BLACK);
        mXyTextPaint.setTextSize(EasyUtils.sp2px(mContext, 12));
        mXyTextPaint.setTextAlign(Paint.Align.CENTER);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = 0;
        int height = 0;
        switch (MeasureSpec.getMode(widthMeasureSpec)) {
            case MeasureSpec.UNSPECIFIED:
            case MeasureSpec.AT_MOST:   //wrap_content
            case MeasureSpec.EXACTLY:   //match_parent
                width = MeasureSpec.getSize(widthMeasureSpec);
                mWidth = MeasureSpec.getSize(widthMeasureSpec);
                break;
        }
        height = (int) (width / 1.5);
        mHeight = height;
        mPadding = EasyUtils.dp2px(mContext, 16);
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //绘制 X 轴刻度
        drawXScale(canvas, getXPointList());
        //绘制 Y 轴刻度
        drawYScale(canvas, getYPointList());
        //绘制折线
        drawPolyline(canvas);
    }

    /**
     * 绘制折线
     *
     * @param canvas 画布
     */
    private void drawPolyline(Canvas canvas) {
        //根据数据计算出折线图的坐标点，即两点一线

//        map.put("六月", 40);

    }

    private List<String> getYPointList() {
        List<String> yPointList = new ArrayList<>();
        yPointList.add("0");
        yPointList.add("10");
        yPointList.add("20");
        yPointList.add("30");
        yPointList.add("40");
        yPointList.add("50");
        yPointList.add("60");
        return yPointList;
    }

    private List<String> getXPointList() {
        List<String> xPointList = new ArrayList<>();
        xPointList.add("一月");
        xPointList.add("二月");
        xPointList.add("三月");
        xPointList.add("四月");
        xPointList.add("五月");
        return xPointList;
    }

    /**
     * 绘制 X 轴刻度，包含X轴和文字刻度
     *
     * @param canvas 画布
     * @param scales X轴刻度列表
     */
    private void drawXScale(Canvas canvas, List<String> scales) {
        if (scales == null) return;
        //绘制X轴
        canvas.drawLine(mPadding * 2, mHeight - mPadding, mWidth - mPadding, mHeight - mPadding, mXyPaint);

        mXScalePoints = new ArrayList<>();
        float xValue = mWidth - mPadding * 3;
        float xPointMargin = xValue / scales.size();
        for (int i = 0; i < scales.size(); i++) {
            String content = scales.get(i);
            //获取绘制的文字边界
            mXyTextPaint.getTextBounds(content, 0, content.length(), mTextRect);
            //绘制的位置
            int offset = (mTextRect.top + mTextRect.bottom) / 2;
            //绘制文字，需要确定起始点的X、Y的坐标点
            float x = (float) (i * xPointMargin + xPointMargin);
            float y = (float) (mHeight - (mPadding / 2)) - offset;
            mXScalePoints.add(new Point(x, y));
            //绘制X轴文字
            canvas.drawText(content, x, y, mXyTextPaint);
        }
    }

    /**
     * 绘制 Y 轴刻度，包含Y轴和文字刻度
     *
     * @param canvas 画布
     * @param scales Y轴刻度列表
     */
    private void drawYScale(Canvas canvas, List<String> scales) {
        if (scales == null) return;
        //绘制Y轴
        canvas.drawLine(mPadding * 2, mHeight - mPadding, mPadding * 2, mPadding, mXyPaint);
        mYScalePoints = new ArrayList<>();
        //获取Y轴实际长度
        float yValue = mHeight - mPadding * 2;
        float yPointMargin = yValue / scales.size();
        for (int i = 0; i < scales.size(); i++) {
            String content = scales.get(i);
            //获取绘制的文字边界
            mXyTextPaint.getTextBounds(content, 0, content.length(), mTextRect);
            //绘制的位置
            int offset = (mTextRect.top + mTextRect.bottom) / 2;
            //绘制文字，需要确定起始点的X、Y的坐标点
            float x = mPadding;
            float y = (float) (mHeight - mPadding / 2) - i * yPointMargin + offset;
            mYScalePoints.add(new Point(x, y));
            //绘制Y轴文字
            canvas.drawText(content, x, y, mXyTextPaint);
        }
    }

    /**
     * 根据设置的数据进行X、Y绘制
     *
     * @param map Key：X轴坐标内容，Value：Y轴坐标内容
     */
    public void setData(Map<String, Integer> map) {
        mXPointList = new ArrayList<>();

        int min = 0;
        int max = 0;

        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            mXPointList.add(entry.getKey());
            min = Math.min(min, entry.getValue());
            max = Math.max(max, entry.getValue());
        }
        Log.d("TAG", "setData: min：" + min + "，max：" + max);
        mYPointList = new ArrayList<>();
        //添加Y轴刻度值
        mYPointList.add(String.valueOf(min));
        //根据最大值和数据量，得出每一个数据之间的刻度大小。
        //max / 10
//        max / map.size();
//        max / map.size() + 1;
    }

    /**
     * 根据Y轴最大值、数量获取Y轴的标准间隔
     * //
     */
//    private fun getYInterval(maxY: Int): Int {
//        val yIntervalCount = yAxisCount - 1
//        val rawInterval = maxY / yIntervalCount.toFloat()
//        val magicPower = floor(log10(rawInterval.toDouble()))
//        var magic = 10.0.pow(magicPower).toFloat()
//        if (magic == rawInterval) {
//            magic = rawInterval
//        } else {
//            magic *= 10
//        }
//        val rawStandardInterval = rawInterval / magic
//        val standardInterval = getStandardInterval(rawStandardInterval) * magic
//        return standardInterval.roundToInt()
//    }


//
//    /**
//     * 根据初始的归一化后的间隔,转化为目标的间隔
//     */
//    private fun getStandardInterval(x: Float): Float {
//        return when {
//            x <= 0.1f -> 0.1f
//            x <= 0.2f -> 0.2f
//            x <= 0.25f -> 0.25f
//            x <= 0.5f -> 0.5f
//            x <= 1f -> 1f
//        else -> getStandardInterval(x / 10) * 10
//        }
//    }
}
