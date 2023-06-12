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

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.easy.view.R;

/**
 * 简易弹窗
 *
 * @author llw
 * @since 2023/6/3
 */
public class EasyDialog extends Dialog {

    private final DialogController mController;

    public EasyDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        mController = new DialogController(this, getWindow());
    }

    public void setText(int viewId, CharSequence text) {
        mController.setText(viewId, text);
    }

    public View getView(int viewId) {
        return mController.getView(viewId);
    }

    public void setOnClickListener(int viewId, View.OnClickListener onClickListener) {
        mController.setOnClickListener(viewId, onClickListener);
    }

    /**
     * EasyDialog 构建器
     */
    public static class Builder {
        //弹窗参数类
        private final DialogController.DialogParams dialogParams;

        public Builder(Context context) {
            this(context, R.style.EasyDialog);
        }

        public Builder(Context context, int themeResId) {
            dialogParams = new DialogController.DialogParams(context, themeResId);
        }

        /**
         * 设置对话框内容视图
         *
         * @param view 视图
         * @return Builder
         */
        public Builder setContentView(View view) {
            dialogParams.mView = view;
            dialogParams.mLayoutResId = 0;
            return this;
        }

        /**
         * 设置对话框内容视图
         *
         * @param layoutId 布局id
         * @return Builder
         */
        public Builder setContentView(int layoutId) {
            dialogParams.mView = null;
            dialogParams.mLayoutResId = layoutId;
            return this;
        }

        /**
         * 设置文本
         *
         * @param viewId 视图id
         * @param text   字符内容
         * @return Builder
         */
        public Builder setText(int viewId, CharSequence text) {
            dialogParams.mTextArray.put(viewId, text);
            return this;
        }

        /**
         * 设置文本颜色
         *
         * @param viewId 视图id
         * @param color  文字颜色
         * @return Builder
         */
        public Builder setTextColor(int viewId, int color) {
            dialogParams.mTextColorArray.put(viewId, color);
            return this;
        }

        /**
         * 设置背景
         *
         * @param viewId   视图id
         * @param drawable 资源
         * @return Builder
         */
        public Builder setBackground(int viewId, Drawable drawable) {
            dialogParams.mBgResArray.put(viewId, drawable);
            return this;
        }

        /**
         * 设置背景颜色
         *
         * @param viewId 视图id
         * @param color  颜色
         * @return Builder
         */
        public Builder setBackgroundColor(int viewId, int color) {
            dialogParams.mBgColorArray.put(viewId, color);
            return this;
        }

        /**
         * 设置图标
         *
         * @param viewId 视图id
         * @return Builder
         */
        public Builder setIcon(int viewId, Icon icon) {
            dialogParams.mIconArray.put(viewId, icon);
            return this;
        }

        /**
         * 设置图片
         *
         * @param viewId 视图id
         * @param bitmap 位图
         * @return Builder
         */
        public Builder setBitmap(int viewId, Bitmap bitmap) {
            dialogParams.mBitmapArray.put(viewId, bitmap);
            return this;
        }

        /**
         * 设置图片
         *
         * @param viewId   视图id
         * @param drawable 图片
         * @return Builder
         */
        public Builder setDrawable(int viewId, Drawable drawable) {
            dialogParams.mDrawableArray.put(viewId, drawable);
            return this;
        }

        /**
         * 设置图片
         *
         * @param viewId 视图id
         * @param resId  图片资源id
         * @return Builder
         */
        public Builder setImageRes(int viewId, int resId) {
            dialogParams.mImageResArray.put(viewId, resId);
            return this;
        }

        /**
         * 设置对话框宽度占满屏幕
         *
         * @return Builder
         */
        public Builder fullWidth() {
            dialogParams.mWidth = ViewGroup.LayoutParams.MATCH_PARENT;
            return this;
        }

        /**
         * 设置对话框高度占满屏幕
         *
         * @return Builder
         */
        public Builder fullHeight() {
            dialogParams.mHeight = ViewGroup.LayoutParams.MATCH_PARENT;
            return this;
        }

        /**
         * 设置对话框宽高
         *
         * @param width  宽
         * @param height 高
         * @return Builder
         */
        public Builder setWidthAndHeight(int width, int height) {
            dialogParams.mWidth = width;
            dialogParams.mHeight = height;
            return this;
        }

        /**
         * 设置对话框宽高和宽高边距
         *
         * @param width        宽
         * @param height       高
         * @param widthMargin  宽边距
         * @param heightMargin 高边距
         * @return Builder
         */
        public Builder setWidthAndHeightMargin(int width, int height, int widthMargin, int heightMargin) {
            dialogParams.mWidth = width;
            dialogParams.mHeight = height;
            dialogParams.mWidthMargin = widthMargin;
            dialogParams.mHeightMargin = heightMargin;
            return this;
        }

        /**
         * 设置动画
         *
         * @param styleAnimation 对话框动画
         * @return Builder
         */
        public Builder setAnimation(int styleAnimation) {
            dialogParams.mAnimation = styleAnimation;
            return this;
        }

        /**
         * 添加默认动画
         *
         * @return Builder
         */
        public Builder addDefaultAnimation() {
            dialogParams.mAnimation = R.style.dialog_scale_anim;
            return this;
        }

        /**
         * 添加自定义动画
         * @param gravity 弹窗位置  关系到使用什么动画
         * @param isAnimation 是否开启动画
         * @return Builder
         */
        public Builder addCustomAnimation(int gravity, boolean isAnimation) {
            switch (gravity) {
                case Gravity.TOP:
                    dialogParams.mGravity = Gravity.TOP;
                    dialogParams.mAnimation = R.style.dialog_from_top_anim;
                    break;
                case Gravity.RIGHT:
                    dialogParams.mGravity = Gravity.RIGHT;
                    dialogParams.mAnimation = R.style.dialog_from_right_anim;
                    break;
                case Gravity.BOTTOM:
                    dialogParams.mGravity = Gravity.BOTTOM;
                    dialogParams.mAnimation = R.style.dialog_from_bottom_anim;
                    break;
                case Gravity.LEFT:
                    dialogParams.mGravity = Gravity.LEFT;
                    dialogParams.mAnimation = R.style.dialog_from_left_anim;
                    break;
                default:
                    dialogParams.mGravity = Gravity.CENTER;
                    dialogParams.mAnimation = R.style.dialog_scale_anim;
                    break;
            }
            if (!isAnimation) {
                dialogParams.mAnimation = 0;
            }
            return this;
        }

        /**
         * 设置对话框是否可取消，默认为true。
         *
         * @return Builder
         */
        public Builder setCancelable(boolean cancelable) {
            dialogParams.mCancelable = cancelable;
            return this;
        }

        /**
         * 设置点击事件监听
         *
         * @param viewId          视图id
         * @param onClickListener 点击事件
         * @return Builder
         */
        public Builder setOnClickListener(int viewId, View.OnClickListener onClickListener) {
            dialogParams.mClickArray.put(viewId, onClickListener);
            return this;
        }

        /**
         * 设置长按事件监听
         *
         * @param viewId              视图id
         * @param onLongClickListener 长按事件
         * @return Builder
         */
        public Builder setOnLongClickListener(int viewId, View.OnLongClickListener onLongClickListener) {
            dialogParams.mLongClickArray.put(viewId, onLongClickListener);
            return this;
        }

        /**
         * 设置取消事件监听
         *
         * @param onCancelListener 取消事件监听
         * @return Builder
         */
        public Builder setOnCancelListener(OnCancelListener onCancelListener) {
            dialogParams.mOnCancelListener = onCancelListener;
            return this;
        }

        /**
         * 设置隐藏事件监听
         *
         * @param onDismissListener 隐藏事件监听
         * @return Builder
         */
        public Builder setOnDismissListener(OnDismissListener onDismissListener) {
            dialogParams.mOnDismissListener = onDismissListener;
            return this;
        }

        /**
         * 设置键监听
         *
         * @param onKeyListener 键监听事件
         * @return Builder
         */
        public Builder setOnKeyListener(OnKeyListener onKeyListener) {
            dialogParams.mOnKeyListener = onKeyListener;
            return this;
        }

        /**
         * 创建弹窗
         *
         * @return EasyDialog
         */
        public EasyDialog create() {
            //通过上下文和主题样式设置弹窗
            final EasyDialog dialog = new EasyDialog(dialogParams.mContext, dialogParams.mThemeResId);
            //应用弹窗控制器
            dialogParams.apply(dialog.mController);
            //设置对话框是否可取消
            dialog.setCancelable(dialogParams.mCancelable);
            if (dialogParams.mCancelable) {
                //设置取消在触摸外面
                dialog.setCanceledOnTouchOutside(true);
            }
            dialog.setOnCancelListener(dialogParams.mOnCancelListener);
            dialog.setOnDismissListener(dialogParams.mOnDismissListener);
            if (dialogParams.mOnKeyListener != null) {
                dialog.setOnKeyListener(dialogParams.mOnKeyListener);
            }
            return dialog;
        }

        /**
         * 显示弹窗
         *
         * @return dialog
         */
        public EasyDialog show() {
            final EasyDialog dialog = create();
            dialog.show();
            return dialog;
        }
    }
}
