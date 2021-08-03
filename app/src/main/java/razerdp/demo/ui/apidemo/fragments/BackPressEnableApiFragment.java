package razerdp.demo.ui.apidemo.fragments;

import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.viewbinding.ViewBinding;
import razerdp.basepopup.databinding.ApiDemoBackpressEnableBinding;
import razerdp.demo.popup.DemoPopup;
import razerdp.demo.ui.apidemo.ApiDemoActivity;
import razerdp.demo.ui.apidemo.ApiDemoFragment;

/**
 * Created by 大灯泡 on 2020/4/4.
 * backpressenable
 */
public class BackPressEnableApiFragment extends ApiDemoFragment<ApiDemoBackpressEnableBinding> {
    DemoPopup mDemoPopup;
    boolean backPressEnable = true;

    @Override
    public ApiDemoBackpressEnableBinding onCreateViewBinding(LayoutInflater layoutInflater) {
        return ApiDemoBackpressEnableBinding.inflate(layoutInflater);
    }

    @Override
    protected void onInitViews(View mRootView) {
        mViewBinding.tvTest.setOnClickListener(v -> show());
    }

    @Override
    protected void onInitSettingPopup(@NonNull ApiDemoActivity.SimpleSelectorPopupConfig config) {
        config.setTitle("是否允许返回键Dismiss")
                .append("true")
                .append("false");
    }

    @Override
    protected void onSettingPopupSelected(String selected, int index) {
        backPressEnable = index == 0;
    }


    void show() {
        if (mDemoPopup == null) {
            mDemoPopup = new DemoPopup(this);
        }
        mDemoPopup.setBackPressEnable(backPressEnable)
                .showPopupWindow();

    }
}
