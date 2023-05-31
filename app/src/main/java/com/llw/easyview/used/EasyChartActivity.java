package com.llw.easyview.used;

import com.llw.easyview.EasyActivity;
import com.llw.easyview.databinding.ActivityEasyChartBinding;

import java.util.HashMap;
import java.util.Map;

/**
 * 简易图表使用
 */
public class EasyChartActivity extends EasyActivity<ActivityEasyChartBinding> {

    @Override
    protected void onCreate() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Map<String, Integer> map = new HashMap<>();
        map.put("一月", 20);
        map.put("二月", 10);
        map.put("三月", 30);
        map.put("四月", 60);
        map.put("五月", 50);
        //设置数据
        binding.eChart.setData(map);
    }
}