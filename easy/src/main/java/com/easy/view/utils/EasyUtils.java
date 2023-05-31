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
package com.easy.view.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.NonNull;

import com.easy.view.R;
import com.easy.view.listener.HexKeyboardListener;
import com.easy.view.listener.NumberKeyboardListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;

/**
 * Tool class
 *
 * @author llw
 * @since 2023/3/24
 */
public class EasyUtils {

    public static final String HOUR_MINUTE_SECOND = "HH:mm:ss";
    public static final String HOUR_MINUTE_SECOND_CN = "HH时mm分ss秒";

    /**
     * dp to px
     *
     * @param dpValue dp value
     * @return px value
     */
    public static int dp2px(Context context, final float dpValue) {
        final float scale = context.getApplicationContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * sp to px
     *
     * @param spValue sp value
     * @return px value
     */
    public static int sp2px(Context context, final float spValue) {
        final float fontScale = context.getApplicationContext().getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * 显示Hex键盘弹窗
     *
     * @param listener Hex keyboard key monitoring
     */
    public static void showHexKeyboardDialog(@NonNull Context context, @NonNull HexKeyboardListener listener) {
        BottomSheetDialog dialog = new BottomSheetDialog(context);
        //Get the layout view from the xml
        View view = LayoutInflater.from(context).inflate(R.layout.lay_hex_keyboard, null, false);
        //Click the button to trigger the interface callback
        view.findViewById(R.id.btn_a).setOnClickListener(v -> listener.onHex("A"));
        view.findViewById(R.id.btn_b).setOnClickListener(v -> listener.onHex("B"));
        view.findViewById(R.id.btn_c).setOnClickListener(v -> listener.onHex("C"));
        view.findViewById(R.id.btn_d).setOnClickListener(v -> listener.onHex("D"));
        view.findViewById(R.id.btn_e).setOnClickListener(v -> listener.onHex("E"));
        view.findViewById(R.id.btn_f).setOnClickListener(v -> listener.onHex("F"));
        view.findViewById(R.id.btn_0).setOnClickListener(v -> listener.onHex("0"));
        view.findViewById(R.id.btn_1).setOnClickListener(v -> listener.onHex("1"));
        view.findViewById(R.id.btn_2).setOnClickListener(v -> listener.onHex("2"));
        view.findViewById(R.id.btn_3).setOnClickListener(v -> listener.onHex("3"));
        view.findViewById(R.id.btn_4).setOnClickListener(v -> listener.onHex("4"));
        view.findViewById(R.id.btn_5).setOnClickListener(v -> listener.onHex("5"));
        view.findViewById(R.id.btn_6).setOnClickListener(v -> listener.onHex("6"));
        view.findViewById(R.id.btn_7).setOnClickListener(v -> listener.onHex("7"));
        view.findViewById(R.id.btn_8).setOnClickListener(v -> listener.onHex("8"));
        view.findViewById(R.id.btn_9).setOnClickListener(v -> listener.onHex("9"));
        view.findViewById(R.id.btn_del).setOnClickListener(v -> listener.onDelete());
        view.findViewById(R.id.btn_delete_all).setOnClickListener(v -> listener.onDeleteAll());
        view.findViewById(R.id.btn_complete).setOnClickListener(v -> {
            listener.onComplete();
            dialog.dismiss();
        });
        //Click outside does not disappear
        dialog.setCancelable(false);
        //Set content view
        dialog.setContentView(view);
        if (dialog.getWindow() != null) {
            //Set the popover background transparent
            WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
            params.dimAmount = 0.0f;
            dialog.getWindow().setAttributes(params);
        }
        dialog.show();
    }

    /**
     * 显示数字键盘弹窗
     *
     * @param listener 数字键盘监听
     */
    public static void showNumKeyboardDialog(@NonNull Context context, @NonNull NumberKeyboardListener listener) {
        BottomSheetDialog dialog = new BottomSheetDialog(context);
        //从xml中获取布局视图
        View view = LayoutInflater.from(context).inflate(R.layout.lay_number_keyboard, null, false);
        //单击按钮触发接口回调
        view.findViewById(R.id.btn_0).setOnClickListener(v -> listener.onNum("0"));
        view.findViewById(R.id.btn_1).setOnClickListener(v -> listener.onNum("1"));
        view.findViewById(R.id.btn_2).setOnClickListener(v -> listener.onNum("2"));
        view.findViewById(R.id.btn_3).setOnClickListener(v -> listener.onNum("3"));
        view.findViewById(R.id.btn_4).setOnClickListener(v -> listener.onNum("4"));
        view.findViewById(R.id.btn_5).setOnClickListener(v -> listener.onNum("5"));
        view.findViewById(R.id.btn_6).setOnClickListener(v -> listener.onNum("6"));
        view.findViewById(R.id.btn_7).setOnClickListener(v -> listener.onNum("7"));
        view.findViewById(R.id.btn_8).setOnClickListener(v -> listener.onNum("8"));
        view.findViewById(R.id.btn_9).setOnClickListener(v -> listener.onNum("9"));
        view.findViewById(R.id.btn_del).setOnClickListener(v -> listener.onDelete());
        view.findViewById(R.id.btn_complete).setOnClickListener(v -> {
            listener.onComplete();
            dialog.dismiss();
        });
        //点击外面消失
        dialog.setCancelable(true);
        //设置内容视图
        dialog.setContentView(view);
        if (dialog.getWindow() != null) {
            //设置弹出窗口背景透明
            WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
            params.dimAmount = 0.0f;
            dialog.getWindow().setAttributes(params);
        }
        dialog.setOnShowListener(dialog1 -> listener.onDialogShow());
        dialog.setOnCancelListener(dialog12 -> listener.onDialogDismiss());
        dialog.setOnDismissListener(dialog13 -> listener.onDialogDismiss());
        dialog.show();
    }
}
