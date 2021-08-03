package razerdp.demo.ui.issuestest;

import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;

import razerdp.basepopup.R;
import razerdp.basepopup.databinding.ActivityIssue369Binding;
import razerdp.demo.base.baseactivity.BaseBindingActivity;
import razerdp.demo.popup.DemoPopup;
import razerdp.demo.utils.UIHelper;
import razerdp.demo.utils.ViewUtil;

/**
 * Created by 大灯泡 on 2021/5/27.
 * <p>
 * https://github.com/razerdp/BasePopup/issues/369
 */
public class Issue369TestActivity extends BaseBindingActivity<ActivityIssue369Binding> {

    DemoPopup demoPopup;
    PopupWindow systemPopup;

    @Override
    protected void onHandleIntent(Intent intent) {

    }

    @Override
    public ActivityIssue369Binding onCreateViewBinding(LayoutInflater layoutInflater) {
        return ActivityIssue369Binding.inflate(layoutInflater);
    }

    @Override
    protected void onInitView(View decorView) {
        mBinding.showPopBt.setOnClickListener(v -> onViewClicked());
        mBinding.showSystemPopup.setOnClickListener(v -> onSystemViewClicked(v));
    }


    public void onViewClicked() {
        if (demoPopup == null) {
            demoPopup = new DemoPopup(this);
        }
        demoPopup.hideKeyboardOnShow(mBinding.checkHidekeyboard.isChecked());
        demoPopup.showPopupWindow();
    }

    public void onSystemViewClicked(View v) {
        if (systemPopup == null) {
            systemPopup = new PopupWindow(this);
            View contentView = ViewUtil.inflate(this, R.layout.popup_demo, null, false);
            contentView.setOnClickListener(v1 -> systemPopup.dismiss());
            systemPopup.setContentView(contentView);
            systemPopup.setWidth(UIHelper.dip2px(150));
            systemPopup.setHeight(UIHelper.dip2px(150));
        }
        systemPopup.showAtLocation(v, Gravity.CENTER, 0, 0);
    }
}
