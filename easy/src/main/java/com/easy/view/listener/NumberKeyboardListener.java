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
package com.easy.view.listener;

/**
 * 数字键盘监听
 *
 * @author llw
 * @since 2023/4/30
 */
public interface NumberKeyboardListener {

    /**
     * 数字字符
     * @param num 0~9
     */
    void onNum(String num);

    /**
     * 删除
     */
    void onDelete();

    /**
     * 完成
     */
    void onComplete();

    /**
     * 弹窗关闭
     */
    void onDialogDismiss();

    /**
     * 弹窗显示
     */
    void onDialogShow();
}
