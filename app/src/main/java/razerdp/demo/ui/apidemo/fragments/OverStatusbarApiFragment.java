package razerdp.demo.ui.apidemo.fragments;

import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import razerdp.basepopup.R;
import razerdp.basepopup.databinding.ApiDemoOverStausbarBinding;
import razerdp.demo.popup.DemoPopup;
import razerdp.demo.ui.apidemo.ApiDemoActivity;
import razerdp.demo.ui.apidemo.ApiDemoFragment;
import razerdp.demo.utils.SpanUtil;

/**
 * Created by 大灯泡 on 2020/4/4.
 */
public class OverStatusbarApiFragment extends ApiDemoFragment<ApiDemoOverStausbarBinding> {
    DemoPopup mDemoPopup;
    boolean overStatusbar = true;

    @Override
    public ApiDemoOverStausbarBinding onCreateViewBinding(LayoutInflater layoutInflater) {
        return ApiDemoOverStausbarBinding.inflate(layoutInflater);
    }

    @Override
    protected void onInitViews(View mRootView) {
        mViewBinding.tvTest.setOnClickListener(v -> this.show());
        SpanUtil.create("setOverlayStatusbar(boolean)\n\nsetPopupWindowFullScreen(boolean)")
                .append("setPopupWindowFullScreen(boolean)")
                .setTextSize(14)
                .setTextColorRes(R.color.text_black2)
                .setDeleteLine(true)
                .into(mViewBinding.tvTitle);

    }

    @Override
    protected void onInitSettingPopup(@NonNull ApiDemoActivity.SimpleSelectorPopupConfig config) {
        config.setTitle("是否覆盖状态栏")
                .append("true")
                .append("false");
    }

    @Override
    protected void onSettingPopupSelected(String selected, int index) {
        overStatusbar = index == 0;
    }


    void show() {
        if (mDemoPopup == null) {
            mDemoPopup = new DemoPopup(this);
        }
        mDemoPopup.setOverlayStatusbar(overStatusbar)
                .showPopupWindow();

    }
}
