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
package com.easy.view.dialog;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.os.Build;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import java.lang.ref.WeakReference;

/**
 * 弹窗视图帮助类
 *
 * @author llw
 * @since 2023/6/3
 */
public class DialogViewHelper {

    //内容视图
    private final View mView;
    //弱应用视图
    private final SparseArray<WeakReference<View>> mSubViews;

    /**
     * 构造方法
     *
     * @param view 视图
     */
    public DialogViewHelper(View view) {
        //初始化
        mSubViews = new SparseArray<>();
        //获取弹窗视图
        mView = view;
    }

    /**
     * 构造方法
     *
     * @param context     上下文
     * @param layoutResId 布局资源id
     */
    public DialogViewHelper(Context context, int layoutResId) {
        //初始化
        mSubViews = new SparseArray<>();
        //获取弹窗视图
        mView = LayoutInflater.from(context).inflate(layoutResId, null);
    }

    /**
     * 获取弹窗内容视图
     *
     * @return mView
     */
    public View getContentView() {
        return mView;
    }

    /**
     * 获取子视图
     *
     * @param viewId 视图id
     * @return view
     */
    public View getSubView(int viewId) {
        //通过视图id得到弱引用视图
        WeakReference<View> viewWeakReference = mSubViews.get(viewId);
        View view = null;
        //如果弱引用视图不为空，说明有对应的xml文件，则对view进行赋值
        if (viewWeakReference != null) {
            view = viewWeakReference.get();
        }
        //如果view为空，则说明上面的弱引用列表数据不存在，通过findViewById方式重新赋值，不为空再放到数组里
        if (view == null) {
            view = mView.findViewById(viewId);
            if (view != null) {
                mSubViews.put(viewId, new WeakReference<>(view));
            }
        }
        return view;
    }

    /**
     * 设置文本
     *
     * @param viewId 视图id
     * @param text   字符
     */
    public void setText(int viewId, CharSequence text) {
        TextView tv = (TextView) getSubView(viewId);
        if (tv != null) {
            tv.setText(text);
        }
    }

    /**
     * 设置文本
     *
     * @param viewId 视图id
     * @param color  颜色
     */
    public void setTextColor(int viewId, int color) {
        TextView tv = (TextView) getSubView(viewId);
        if (tv != null) {
            tv.setTextColor(color);
        }
    }

    /**
     * 设置图标
     *
     * @param viewId 视图id
     * @param icon   图标
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void setImageIcon(int viewId, Icon icon) {
        ImageView iv = (ImageView) getSubView(viewId);
        if (iv != null) {
            iv.setImageIcon(icon);
        }
    }

    /**
     * 设置图片
     *
     * @param viewId 视图id
     * @param resId  图标资源id
     */
    public void setImageResource(int viewId, int resId) {
        ImageView iv = (ImageView) getSubView(viewId);
        if (iv != null) {
            iv.setImageResource(resId);
        }
    }

    /**
     * 设置图片
     *
     * @param viewId   视图id
     * @param drawable 图
     */
    public void setImageDrawable(int viewId, Drawable drawable) {
        ImageView iv = (ImageView) getSubView(viewId);
        if (iv != null) {
            iv.setImageDrawable(drawable);
        }
    }

    /**
     * 设置图片
     *
     * @param viewId 视图id
     * @param bitmap 图片
     */
    public void setImageBitmap(int viewId, Bitmap bitmap) {
        ImageView iv = (ImageView) getSubView(viewId);
        if (iv != null) {
            iv.setImageBitmap(bitmap);
        }
    }

    /**
     * 设置背景颜色
     *
     * @param viewId 视图id
     * @param color  颜色
     */
    public void setBackgroundColor(int viewId, int color) {
        View view = getSubView(viewId);
        if (view != null) {
            view.setBackgroundColor(color);
        }
    }

    /**
     * 设置背景
     *
     * @param viewId   视图id
     * @param drawable drawable
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void setBackground(int viewId, Drawable drawable) {
        View view = getSubView(viewId);
        if (view != null) {
            view.setBackground(drawable);
        }
    }

    /**
     * 设置点击监听
     *
     * @param viewId          视图id
     * @param onClickListener 点击监听
     */
    public void setOnClickListener(int viewId, View.OnClickListener onClickListener) {
        View view = getSubView(viewId);
        if (view != null) {
            view.setOnClickListener(onClickListener);
        }
    }
}
