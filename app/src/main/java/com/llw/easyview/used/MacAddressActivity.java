package com.llw.easyview.used;

import com.llw.easyview.EasyActivity;
import com.llw.easyview.databinding.ActivityMacAddressBinding;

/**
 * MacAddress 使用示例
 */
public class MacAddressActivity extends EasyActivity<ActivityMacAddressBinding> {

    @Override
    protected void onCreate() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //蓝牙地址输入框
        binding.btnMac.setOnClickListener(v -> {
            String macAddress = binding.macEt.getMacAddress();
            if (macAddress.isEmpty()) {
                showMsg("请输入Mac地址");
                return;
            }
            binding.btnMac.setText(macAddress);
        });
    }
}