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

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.os.Build;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import androidx.annotation.RequiresApi;

/**
 * 弹窗控制
 *
 * @author llw
 * @since 2023/6/3
 */
public class DialogController {
    //对话弹窗
    private final EasyDialog mEasyDialog;
    //窗口
    private final Window mWindow;
    //帮助类
    private DialogViewHelper mViewHelper;

    public DialogController(EasyDialog easyDialog, Window window) {
        mEasyDialog = easyDialog;
        mWindow = window;
    }

    public void setDialogViewHelper(DialogViewHelper dialogViewHelper) {
        mViewHelper = dialogViewHelper;
    }

    public void setText(int viewId, CharSequence text) {
        mViewHelper.setText(viewId, text);
    }

    public void setTextColor(int viewId, int color) {
        mViewHelper.setTextColor(viewId, color);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void setImageIcon(int viewId, Icon icon) {
        mViewHelper.setImageIcon(viewId, icon);
    }

    public void setImageBitmap(int viewId, Bitmap bitmap) {
        mViewHelper.setImageBitmap(viewId, bitmap);
    }

    public void setImageDrawable(int viewId, Drawable drawable) {
        mViewHelper.setImageDrawable(viewId, drawable);
    }

    public void setImageResource(int viewId, int resId) {
        mViewHelper.setImageResource(viewId, resId);
    }

    public View getView(int viewId) {
        return mViewHelper.getSubView(viewId);
    }

    public void setBackgroundColor(int viewId, int color) {
        mViewHelper.setBackgroundColor(viewId, color);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void setBackground(int viewId, Drawable drawable) {
        mViewHelper.setBackground(viewId, drawable);
    }

    public void setOnClickListener(int viewId, View.OnClickListener onClickListener) {
        mViewHelper.setOnClickListener(viewId, onClickListener);
    }

    public EasyDialog getDialog() {
        return mEasyDialog;
    }

    public Window getWindow() {
        return mWindow;
    }

    /**
     * 弹窗参数类
     */
    @SuppressLint("UseSparseArrays")
    public static class DialogParams {
        //上下文
        public Context mContext;
        //主题资源Id
        public int mThemeResId;
        //点击其他区域弹窗是否取消
        public boolean mCancelable;
        //弹窗取消监听
        public DialogInterface.OnCancelListener mOnCancelListener;
        //弹窗隐藏监听
        public DialogInterface.OnDismissListener mOnDismissListener;
        //键值监听
        public DialogInterface.OnKeyListener mOnKeyListener;
        //文本颜色
        public SparseArray<Integer> mTextColorArray = new SparseArray<>();
        //背景颜色
        public SparseArray<Integer> mBgColorArray = new SparseArray<>();
        //背景资源
        public SparseArray<Drawable> mBgResArray = new SparseArray<>();
        //存放文本
        public SparseArray<CharSequence> mTextArray = new SparseArray<>();
        //存放点击事件
        public SparseArray<View.OnClickListener> mClickArray = new SparseArray<>();
        //存放长按点击事件
        public SparseArray<View.OnLongClickListener> mLongClickArray = new SparseArray<>();
        //存放对话框图标
        public SparseArray<Icon> mIconArray = new SparseArray<>();
        //存放对话框图片
        public SparseArray<Bitmap> mBitmapArray = new SparseArray<>();
        //存放对话框图片
        public SparseArray<Drawable> mDrawableArray = new SparseArray<>();
        //存放对话框图标资源文件
        public SparseArray<Integer> mImageResArray = new SparseArray<>();
        //对话框布局资源id
        public int mLayoutResId;
        //对话框的内容view
        public View mView;
        //对话框宽度
        public int mWidth;
        //对话框高度
        public int mHeight;
        //对话框垂直外边距
        public int mHeightMargin;
        //对话框横向外边距
        public int mWidthMargin;
        //对话框出现动画
        public int mAnimation;
        //对话框显示位置，默认居中显示
        public int mGravity = Gravity.CENTER;

        public DialogParams(Context context, int themeResId) {
            mContext = context;
            mThemeResId = themeResId;
        }

        /**
         * 应用
         */
        public void apply(DialogController controller) {
            DialogViewHelper helper = null;

            if (mView != null) {
                helper = new DialogViewHelper(mView);
            } else if (mLayoutResId != 0) {
                helper = new DialogViewHelper(mContext, mLayoutResId);
            }

            //如果helper为null，则mLayoutResId为0，没有设置弹窗xml
            if (helper == null) {
                throw new IllegalArgumentException("Please set layout!");
            }
            //为对话框设置内容视图
            controller.getDialog().setContentView(helper.getContentView());
            //设置DialogViewHelper辅助类
            controller.setDialogViewHelper(helper);
            //设置文本
            for (int i = 0; i < mTextArray.size(); i++) {
                controller.setText(mTextArray.keyAt(i), mTextArray.valueAt(i));
            }
            //设置文本颜色
            for (int i = 0; i < mTextColorArray.size(); i++) {
                controller.setTextColor(mTextColorArray.keyAt(i), mTextColorArray.valueAt(i));
            }
            //设置图标
            for (int i = 0; i < mIconArray.size(); i++) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    controller.setImageIcon(mIconArray.keyAt(i), mIconArray.valueAt(i));
                }
            }
            //设置图片
            for (int i = 0; i < mBitmapArray.size(); i++) {
                controller.setImageBitmap(mBitmapArray.keyAt(i), mBitmapArray.valueAt(i));
            }
            //设置图片
            for (int i = 0; i < mDrawableArray.size(); i++) {
                controller.setImageDrawable(mDrawableArray.keyAt(i), mDrawableArray.valueAt(i));
            }
            //设置图片
            for (int i = 0; i < mImageResArray.size(); i++) {
                controller.setImageResource(mImageResArray.keyAt(i), mImageResArray.valueAt(i));
            }
            //设置背景颜色
            for (int i = 0; i < mBgColorArray.size(); i++) {
                controller.setBackgroundColor(mBgColorArray.keyAt(i), mBgColorArray.valueAt(i));
            }
            //设置背景资源
            for (int i = 0; i < mBgResArray.size(); i++) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    controller.setBackground(mBgResArray.keyAt(i), mBgResArray.valueAt(i));
                }
            }
            //设置点击
            for (int i = 0; i < mClickArray.size(); i++) {
                controller.setOnClickListener(mClickArray.keyAt(i), mClickArray.valueAt(i));
            }
            //配置自定义效果，底部弹出，宽高，动画，全屏
            Window window = controller.getWindow();
            //显示位置
            window.setGravity(mGravity);
            if (mAnimation != 0) {
                //设置动画
                window.setWindowAnimations(mAnimation);
            }
            //设置布局参数，主要是宽高和边距
            WindowManager.LayoutParams params = window.getAttributes();
            params.width = mWidth;
            params.height = mHeight;
            params.verticalMargin = mHeightMargin;
            params.horizontalMargin = mWidthMargin;
            window.setAttributes(params);
        }
    }
}
