package com.llw.easyview.used;

import com.llw.easyview.EasyActivity;
import com.llw.easyview.databinding.ActivityEasyEdittextBinding;

/**
 * 简易输入框使用示例
 */
public class EasyEditTextActivity extends EasyActivity<ActivityEasyEdittextBinding> {

    @Override
    protected void onCreate() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        binding.cbFlag.setOnCheckedChangeListener((buttonView, isChecked) -> {
            binding.etContent.setCiphertext(isChecked);
            binding.cbFlag.setText(isChecked ? "密文" : "明文");
        });
        //输入框
        binding.btnGetContent.setOnClickListener(v -> {
            String content = binding.etContent.getText();
            if (content.isEmpty()) {
                showMsg("请输入内容");
                return;
            }
            if (content.length() < binding.etContent.getBoxNum()) {
                showMsg("请输入完整内容");
                return;
            }
            showMsg("输入内容为：" + content);
        });
    }
}