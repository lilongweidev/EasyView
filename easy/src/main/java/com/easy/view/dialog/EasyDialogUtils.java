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
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.easy.view.R;
import com.easy.view.databinding.DialogSelectBinding;
import com.easy.view.dialog.adapter.StringAdapter;
import com.easy.view.dialog.listener.OnCancelListener;
import com.easy.view.dialog.listener.OnConfirmListener;
import com.easy.view.dialog.listener.OnItemClickListener;
import com.easy.view.dialog.listener.OnSelectListener;
import com.easy.view.utils.EasyUtils;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.List;

/**
 * 简易弹窗工具类 帮助您快速使用
 *
 * @author llw
 * @since 2023/6/7
 */
public class EasyDialogUtils {

    @SuppressLint("StaticFieldLeak")
    private static EasyDialog tipDialog;

    /**
     * 显示提示弹窗
     *
     * @param context           上下文
     * @param title             弹窗标题
     * @param content           弹窗内容
     * @param onCancelListener  取消按钮监听
     * @param onConfirmListener 确定按钮监听
     */
    public static void showTipDialog(final Context context,
                                     @NonNull String title,
                                     @NonNull String content,
                                     OnCancelListener onCancelListener,
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
                    if (onCancelListener != null) {
                        onCancelListener.onCancel();
                    }
                    tipDialog.dismiss();
                })
                //添加点击事件  确定
                .setOnClickListener(R.id.tv_confirm, v2 -> {
                    if (onConfirmListener != null) {
                        onConfirmListener.onConfirm();
                    }
                    tipDialog.dismiss();
                });
        //创建弹窗
        tipDialog = builder.create();
        //显示弹窗
        tipDialog.show();
    }

    /**
     * 显示选择弹窗 （String）
     *
     * @param context          上下文
     * @param title            弹窗标题
     * @param strings          弹窗内容列表
     * @param onSelectListener 选择监听，回调选择的内容
     */
    public static void showSelectDialog(final Context context,
                                        String title,
                                        List<String> strings,
                                        OnSelectListener onSelectListener) {
        BottomSheetDialog dialog = new BottomSheetDialog(context, R.style.BottomSheetDialogStyle);
        DialogSelectBinding binding = DialogSelectBinding.inflate(LayoutInflater.from(context));
        StringAdapter stringAdapter = new StringAdapter(strings);
        //设置标题
        binding.toolbar.setTitle(title);
        //设置关闭弹窗
        binding.toolbar.setNavigationOnClickListener(v -> dialog.dismiss());
        //设置Rv
        binding.rvString.setLayoutManager(new LinearLayoutManager(context));
        //添加Android自带的分割线
        //binding.rvString.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));
        //添加自定义分割线
        DividerItemDecoration divider = new DividerItemDecoration(context, DividerItemDecoration.VERTICAL);
        divider.setDrawable(ContextCompat.getDrawable(context, R.drawable.custom_divider));
        binding.rvString.addItemDecoration(divider);
        //列表适配器Item点击
        stringAdapter.setOnItemClickListener((view, position) -> {
            if (onSelectListener != null) {
                //返回点击的内容
                onSelectListener.onItemClick(strings.get(position));
            }
            dialog.dismiss();
        });
        binding.rvString.setAdapter(stringAdapter);
        dialog.setContentView(binding.getRoot());
        dialog.show();
    }
}
