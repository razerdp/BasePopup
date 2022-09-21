package razerdp.demo.ui.issuestest;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;

import androidx.annotation.Nullable;

import razerdp.basepopup.R;
import razerdp.basepopup.databinding.ActivityIssue474Binding;
import razerdp.demo.base.baseactivity.BaseBindingActivity;
import razerdp.demo.popup.DemoPopup;
import razerdp.demo.utils.UIHelper;
import razerdp.demo.utils.ViewUtil;
import razerdp.util.log.PopupLog;

/**
 * Created by 大灯泡 on 2021/5/27.
 * <p>
 * https://github.com/razerdp/BasePopup/issues/474
 */
public class Issue474TestActivity extends BaseBindingActivity<ActivityIssue474Binding> {

    DemoPopup demoPopup;
    PopupWindow systemPopup;

    @Override
    protected void onHandleIntent(Intent intent) {

    }

    @Override
    public ActivityIssue474Binding onCreateViewBinding(LayoutInflater layoutInflater) {
        return ActivityIssue474Binding.inflate(layoutInflater);
    }

    @Override
    protected void onInitView(View decorView) {
        mBinding.showPopBt.setOnClickListener(v -> onViewClicked());
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onViewClicked();
    }

    public void onViewClicked() {
        PopupLog.i("11");
        if (demoPopup == null) {
            demoPopup = new DemoPopup(this);
        }
        demoPopup.showPopupWindow();
        demoPopup.dismiss();
    }
}
