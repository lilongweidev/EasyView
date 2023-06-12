package com.llw.easyview.used;

import android.annotation.SuppressLint;
import android.view.Gravity;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

import androidx.core.content.ContextCompat;

import com.easy.view.dialog.EasyDialogUtils;
import com.easy.view.dialog.EasyDialog;
import com.easy.view.dialog.listener.OnSelectListener;
import com.easy.view.utils.EasyUtils;
import com.llw.easyview.EasyActivity;
import com.llw.easyview.R;
import com.llw.easyview.databinding.ActivityEasyDialogBinding;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 简易弹窗使用
 */
public class EasyDialogActivity extends EasyActivity<ActivityEasyDialogBinding> implements View.OnClickListener, CompoundButton.OnCheckedChangeListener, RadioGroup.OnCheckedChangeListener {

    private EasyDialog easyDialog;
    private EasyDialog.Builder builder;
    private int mGravity;
    private boolean mIsAnimation;
    private boolean mIsCancelable;
    private final String[] stringArr = {"富强", "民主", "文明", "和谐", "自由", "平等", "公正", "法治", "爱国", "敬业", "诚信", "友善"};
    List<String> stringList;
    @Override
    protected void onCreate() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        builder = new EasyDialog.Builder(EasyDialogActivity.this);
        stringList = new ArrayList<>(Arrays.asList(stringArr));
        initView();
    }

    private void initView() {
        binding.btnShow.setOnClickListener(this);
        binding.btnShowTip.setOnClickListener(this);
        binding.btnShowSelect.setOnClickListener(this);
        binding.rgGravity.setOnCheckedChangeListener(this);
        binding.cbAnimation.setOnCheckedChangeListener(this);
        binding.cbCancelable.setOnCheckedChangeListener(this);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (builder != null) {
            switch (checkedId) {
                case R.id.rb_top:
                    mGravity = Gravity.TOP;
                    break;
                case R.id.rb_right:
                    mGravity = Gravity.RIGHT;
                    break;
                case R.id.rb_bottom:
                    mGravity = Gravity.BOTTOM;
                    break;
                case R.id.rb_left:
                    mGravity = Gravity.LEFT;
                    break;
                case R.id.rb_center:
                    mGravity = Gravity.CENTER;
                    break;
            }
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (builder != null) {
            switch (buttonView.getId()) {
                case R.id.cb_animation:
                    mIsAnimation = isChecked;
                    break;
                case R.id.cb_cancelable:
                    mIsCancelable = isChecked;
                    break;
            }
        }
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_show:
                showDialog();
                break;
            case R.id.btn_show_tip:
                EasyDialogUtils.showTipDialog(EasyDialogActivity.this, "温馨提示", "端午又要调休！",
                        () -> showMsg("取消"), () -> showMsg("确定"));
                break;
            case R.id.btn_show_select:
                EasyDialogUtils.showSelectDialog(EasyDialogActivity.this, "社会主义核心价值观",
                        stringList, this::showMsg);
                break;
        }
    }

    /**
     * 显示弹窗
     */
    private void showDialog() {
        builder.setContentView(R.layout.dialog_warm_tip)
                //添加自定义动画
                .addCustomAnimation(mGravity, mIsAnimation)
                //设置对话框可取消
                .setCancelable(mIsCancelable)
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
    }

}