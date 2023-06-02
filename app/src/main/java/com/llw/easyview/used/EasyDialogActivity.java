package com.llw.easyview.used;

import android.widget.LinearLayout;

import androidx.core.content.ContextCompat;

import com.easy.view.dialog.EasyDialog;
import com.easy.view.utils.EasyUtils;
import com.llw.easyview.EasyActivity;
import com.llw.easyview.R;
import com.llw.easyview.databinding.ActivityEasyDialogBinding;

/**
 * 简易弹窗使用
 */
public class EasyDialogActivity extends EasyActivity<ActivityEasyDialogBinding> {

    private EasyDialog easyDialog;

    @Override
    protected void onCreate() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        binding.btnShow.setOnClickListener(v -> {
            //通过构建者模式设置弹窗的相关参数
            EasyDialog.Builder builder = new EasyDialog.Builder(EasyDialogActivity.this)
                    //添加默认出现动画
                    .addDefaultAnimation()
                    //设置内容视图
                    .setContentView(R.layout.dialog_warm_tip)
                    //设置对话框可取消
                    .setCancelable(true)
                    //设置标题
                    .setText(R.id.tv_title, "温馨提示")
                    //设置内容
                    .setText(R.id.tv_content, "您今天还没有搞钱，请记得搞钱！")
                    //设置文字颜色
                    .setTextColor(R.id.tv_confirm, ContextCompat.getColor(EasyDialogActivity.this, R.color.white))
                    //设置背景资源
                    .setBackground(R.id.tv_confirm, ContextCompat.getDrawable(EasyDialogActivity.this, R.drawable.shape_confirm_bg))
                    //设置弹窗宽高
                    .setWidthAndHeight(EasyUtils.dp2px(EasyDialogActivity.this, 320), LinearLayout.LayoutParams.WRAP_CONTENT)
                    //添加点击事件  取消
                    .setOnClickListener(R.id.tv_cancel, v1 -> {
                        easyDialog.dismiss();
                    })
                    //添加点击事件  确定
                    .setOnClickListener(R.id.tv_confirm, v2 -> {
                        showMsg("我知道了！");
                        easyDialog.dismiss();
                    })
                    //添加取消监听
                    .setOnCancelListener(dialog -> {
                        showMsg("弹窗取消了");
                    })
                    //弹窗消失监听
                    .setOnDismissListener(dialog -> {
                        showMsg("弹窗消失了");
                    });
            //创建弹窗
            easyDialog = builder.create();
            //显示弹窗
            easyDialog.show();
        });
    }
}