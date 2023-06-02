package com.easy.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.easy.view.R;
import com.easy.view.utils.EasyUtils;

public class EasyDialog extends Dialog {

    private DialogController mController;

//    public EasyDialog(@NonNull Context context) {
//        this(context);
//    }

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
        private DialogController.DialogParams dialogParams;

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
         * 对话框从屏幕顶部弹出
         *
         * @param isAnimation
         * @return Builder
         */
        public Builder fromTop(boolean isAnimation) {
            if (isAnimation) {
                dialogParams.mAnimation = R.style.dialog_from_top_anim;
            }
            dialogParams.mGravity = Gravity.TOP;
            return this;
        }

        /**
         * 对话框从屏幕底部弹出
         *
         * @param isAnimation
         * @return Builder
         */
        public Builder fromBottom(boolean isAnimation) {
            if (isAnimation) {
                dialogParams.mAnimation = R.style.dialog_from_bottom_anim;
            }
            dialogParams.mGravity = Gravity.BOTTOM;
            return this;
        }

        /**
         * 对话框从屏幕右侧弹出
         *
         * @param isAnimation
         * @return Builder
         */
        public Builder fromRight(boolean isAnimation) {
            if (isAnimation) {
                dialogParams.mAnimation = R.style.dialog_scale_anim;
            }
            dialogParams.mGravity = Gravity.RIGHT;
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

    public EasyDialog showTipDialog(final Context context,
                                    @NonNull String title,
                                    @NonNull String content,
                                    com.easy.view.dialog.OnCancelListener onCancelListener,
                                    OnConfirmListener onConfirmListener) {

        //通过构建者模式设置弹窗的相关参数
        EasyDialog.Builder builder = new EasyDialog.Builder(context)
                //添加默认出现动画
                .addDefaultAnimation()
                //设置内容视图
                .setContentView(R.layout.dialog_tip)
                //设置对话框可取消
                .setCancelable(true)
                //设置标题
                .setText(R.id.tv_title, title)
                //设置内容
                .setText(R.id.tv_content, content)
                //设置文字颜色
                .setTextColor(R.id.tv_confirm, ContextCompat.getColor(context, R.color.white))
                //设置弹窗宽高
                .setWidthAndHeight(EasyUtils.dp2px(context, 280), LinearLayout.LayoutParams.WRAP_CONTENT)
                //添加点击事件  取消
                .setOnClickListener(R.id.tv_cancel, v1 -> {
                    dismiss();
                })
                //添加点击事件  确定
                .setOnClickListener(R.id.tv_confirm, v2 -> {
                    if (onConfirmListener!=null) {
                        onConfirmListener.onConfirm();
                    }
                    dismiss();
                })
                //添加取消监听
                .setOnCancelListener(dialog -> {
                    if (onCancelListener != null) {
                        onCancelListener.onCancel();
                    }
                    dismiss();
                });
        //创建弹窗
        EasyDialog easyDialog = builder.create();
        //显示弹窗
        easyDialog.show();
        return this;
    }
}
