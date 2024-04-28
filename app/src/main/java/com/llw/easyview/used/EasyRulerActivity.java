package com.llw.easyview.used;

import com.llw.easyview.EasyActivity;
import com.llw.easyview.databinding.ActivityEasyRulerBinding;

public class EasyRulerActivity extends EasyActivity<ActivityEasyRulerBinding> {

    @Override
    protected void onCreate() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        binding.ruler.setValue(170F, 80F, 250F, 1F);
    }
}