package razerdp.demo.ui.apidemo.fragments;

import android.view.View;

import androidx.annotation.NonNull;
import butterknife.OnClick;
import razerdp.basepopup.R;
import razerdp.demo.popup.DemoPopup;
import razerdp.demo.ui.apidemo.ApiDemoActivity;
import razerdp.demo.ui.apidemo.ApiDemoFragment;

/**
 * Created by 大灯泡 on 2020/4/4.
 * backpressenable
 */
public class BackPressEnableApiFragment extends ApiDemoFragment {
    DemoPopup mDemoPopup;
    boolean backPressEnable = true;

    @Override
    public int contentViewLayoutId() {
        return R.layout.api_demo_backpress_enable;
    }

    @Override
    protected void onInitViews(View mRootView) {

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


    @OnClick(R.id.tv_test)
    void show() {
        if (mDemoPopup == null) {
            mDemoPopup = new DemoPopup(this);
        }
        mDemoPopup.setBackPressEnable(backPressEnable)
                .showPopupWindow();

    }
}
