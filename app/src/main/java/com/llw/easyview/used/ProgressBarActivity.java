package com.llw.easyview.used;

import android.graphics.Color;
import android.widget.Toast;
import com.llw.easyview.EasyActivity;
import com.llw.easyview.databinding.ActivityProgressBarBinding;

import java.util.Random;

/**
 * 进度条使用示例
 */
public class ProgressBarActivity extends EasyActivity<ActivityProgressBarBinding> {

    @Override
    protected void onCreate() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //圆形进度条操作
        binding.btnSetProgress.setOnClickListener(v -> {
            int progress = Math.abs(new Random().nextInt() % 100);
            Toast.makeText(this, "" + progress, Toast.LENGTH_SHORT).show();
            binding.cpbTest.setText(progress + "%");
            binding.cpbTest.setProgress(progress);
            binding.cpbTest.setGradient(true);
            binding.cpbTest.setColorArray(new int[]{Color.RED,Color.YELLOW});
        });

    }
}