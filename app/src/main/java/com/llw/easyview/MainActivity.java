package com.llw.easyview;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.easy.view.CircularProgressBar;
import com.easy.view.MacAddressEditText;
import com.easy.view.TimingTextView;
import com.easy.view.listener.TimingListener;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //蓝牙地址输入框
        MacAddressEditText macEt = findViewById(R.id.mac_et);
        Button btnMac = findViewById(R.id.btn_mac);
        btnMac.setOnClickListener(v -> {
            String macAddress = macEt.getMacAddress();
            if (macAddress.isEmpty()) {
                Toast.makeText(this, "请输入Mac地址", Toast.LENGTH_SHORT).show();
                return;
            }
            btnMac.setText(macAddress);
        });
        //圆形进度条操作
        CircularProgressBar cpbTest = findViewById(R.id.cpb_test);
        Button btnSetProgress = findViewById(R.id.btn_set_progress);
        btnSetProgress.setOnClickListener(v -> {
            int progress = Math.abs(new Random().nextInt() % 100);
            Toast.makeText(this, "" + progress, Toast.LENGTH_SHORT).show();
            cpbTest.setText(progress + "%");
            cpbTest.setProgress(progress);
        });
        //计时文本操作
        TimingTextView tvTiming = findViewById(R.id.tv_timing);
        CheckBox cbFlag = findViewById(R.id.cb_flag);
        Button btnStart = findViewById(R.id.btn_start);
        tvTiming.setListener(new TimingListener() {
            @Override
            public void onEnd() {
                tvTiming.setText("计时文字");
                btnStart.setText("开始");
            }
        });
        cbFlag.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                cbFlag.setText(isChecked ? "倒计时" : "计时");
            }
        });
        //计时按钮点击
        btnStart.setOnClickListener(v -> {
            if (tvTiming.isTiming()) {
                //停止计时
                tvTiming.end();
                btnStart.setText("开始");
            } else {
                tvTiming.setMax(6);
                tvTiming.setCountDown(cbFlag.isChecked());
                tvTiming.setUnit(3);//单位 秒
                //开始计时
                tvTiming.start();
                btnStart.setText("停止");
            }
        });
    }
}