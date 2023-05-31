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
 * Hex 键盘监听
 *
 * @author llw
 * @since 2023/3/24
 */
public interface HexKeyboardListener {

    /**
     * Hex character
     * @param hex 0~9，A~F
     */
    void onHex(String hex);

    /**
     * delete
     */
    void onDelete();

    /**
     * Complete deletion
     */
    void onDeleteAll();

    /**
     * complete
     */
    void onComplete();
}
