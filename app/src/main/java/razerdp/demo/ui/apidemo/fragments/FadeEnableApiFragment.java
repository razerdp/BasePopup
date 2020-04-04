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
 * fadeenable
 */
public class FadeEnableApiFragment extends ApiDemoFragment {
    DemoPopup mDemoPopup;
    boolean fadeEnable = true;

    @Override
    public int contentViewLayoutId() {
        return R.layout.api_demo_fade_enable;
    }

    @Override
    protected void onInitViews(View mRootView) {

    }

    @Override
    protected void onInitSettingPopup(@NonNull ApiDemoActivity.SimpleSelectorPopupConfig config) {
        config.setTitle("淡入淡出功能")
                .append("开启淡入淡出")
                .append("关闭淡入淡出");
    }

    @Override
    protected void onSettingPopupSelected(String selected, int index) {
        fadeEnable = index == 0;
    }


    @OnClick(R.id.tv_test)
    void show() {
        if (mDemoPopup == null) {
            mDemoPopup = new DemoPopup(this);
        }
        mDemoPopup.setPopupFadeEnable(fadeEnable)
                .showPopupWindow();

    }
}
