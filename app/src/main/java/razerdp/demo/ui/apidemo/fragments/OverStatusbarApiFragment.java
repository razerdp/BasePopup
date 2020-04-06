package razerdp.demo.ui.apidemo.fragments;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import butterknife.BindView;
import butterknife.OnClick;
import razerdp.basepopup.R;
import razerdp.demo.popup.DemoPopup;
import razerdp.demo.ui.apidemo.ApiDemoActivity;
import razerdp.demo.ui.apidemo.ApiDemoFragment;
import razerdp.demo.utils.SpanUtil;

/**
 * Created by 大灯泡 on 2020/4/4.
 */
public class OverStatusbarApiFragment extends ApiDemoFragment {
    @BindView(R.id.tv_title)
    TextView tvTitle;

    DemoPopup mDemoPopup;
    boolean overStatusbar = true;

    @Override
    public int contentViewLayoutId() {
        return R.layout.api_demo_over_stausbar;
    }

    @Override
    protected void onInitViews(View mRootView) {
        SpanUtil.create("setOverlayStatusbar(boolean)\n\nsetPopupWindowFullScreen(boolean)")
                .append("setPopupWindowFullScreen(boolean)")
                .setTextSize(14)
                .setTextColorRes(R.color.text_black2)
                .setDeleteLine(true)
                .into(tvTitle);

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


    @OnClick(R.id.tv_test)
    void show() {
        if (mDemoPopup == null) {
            mDemoPopup = new DemoPopup(this);
        }
        mDemoPopup.setOverlayStatusbar(overStatusbar)
                .showPopupWindow();

    }
}
