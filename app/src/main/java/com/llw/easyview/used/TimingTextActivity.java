package com.llw.easyview.used;

import com.llw.easyview.EasyActivity;
import com.llw.easyview.databinding.ActivityTimingTextBinding;

/**
 * 计时文字使用示例
 */
public class TimingTextActivity extends EasyActivity<ActivityTimingTextBinding> {

    @Override
    protected void onCreate() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //计时文本操作
        binding.tvTiming.setListener(() -> {
            binding.tvTiming.setText("计时文字");
            binding.btnStart.setText("开始");
        });
        binding.cbFlag.setOnCheckedChangeListener((buttonView, isChecked) -> binding.cbFlag.setText(isChecked ? "倒计时" : "计时"));
        //计时按钮点击
        binding.btnStart.setOnClickListener(v -> {
            if (binding.tvTiming.isTiming()) {
                //停止计时
                binding.tvTiming.end();
                binding.btnStart.setText("开始");
            } else {
                binding.tvTiming.setMax(6);
                binding.tvTiming.setCountDown(binding.cbFlag.isChecked());
                binding.tvTiming.setUnit(3);//单位 秒
                //开始计时
                binding.tvTiming.start();
                binding.btnStart.setText("停止");
            }
        });
    }
}