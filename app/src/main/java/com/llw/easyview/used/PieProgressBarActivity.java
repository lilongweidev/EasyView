package com.llw.easyview.used;

import android.annotation.SuppressLint;
import android.widget.Toast;
import com.llw.easyview.EasyActivity;
import com.llw.easyview.R;
import com.llw.easyview.databinding.ActivityPieProgressBarBinding;

import java.util.Random;

/**
 * 饼状进度条使用示例
 */
public class PieProgressBarActivity extends EasyActivity<ActivityPieProgressBarBinding> {

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //是否渐变
        binding.cbGradient.setOnCheckedChangeListener((buttonView, isChecked) -> {
            binding.cbGradient.setText(isChecked ? "渐变" : "不渐变");
            binding.progress.setGradient(isChecked);
        });
        //开始角度
        binding.rg.setOnCheckedChangeListener((group, checkedId) -> {
            int angle = 0;
            switch (checkedId) {
                case R.id.rb_0:
                    angle = 0;
                    break;
                case R.id.rb_90:
                    angle = 90;
                    break;
                case R.id.rb_180:
                    angle = 180;
                    break;
                case R.id.rb_270:
                    angle = 270;
                    break;
            }
            binding.progress.setCustomAngle(angle);
        });
        binding.cbFlag.setOnCheckedChangeListener((buttonView, isChecked) -> {
            binding.progress.setCounterClockwise(isChecked);
            binding.cbFlag.setText(isChecked ? "逆时针" : "顺时针");
        });
        //设置随机进度值
        binding.btnSetProgress.setOnClickListener(v -> {
            int progress = Math.abs(new Random().nextInt() % 100);
            Toast.makeText(this, "" + progress, Toast.LENGTH_SHORT).show();
            binding.progress.setProgress(progress);
        });
        //设置0%进度值
        binding.btnSetProgress0.setOnClickListener(v -> binding.progress.setProgress(0));
        //设置100%进度值
        binding.btnSetProgress100.setOnClickListener(v -> binding.progress.setProgress(100));
    }
}