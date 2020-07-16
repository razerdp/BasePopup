package razerdp.demo.ui.rtl;

import android.content.Intent;
import android.view.Gravity;
import android.view.View;

import butterknife.BindView;
import butterknife.OnClick;
import razerdp.basepopup.R;
import razerdp.demo.base.baseactivity.BaseActivity;
import razerdp.demo.popup.DemoPopup;
import razerdp.util.PopupUtils;

import static android.view.Gravity.END;
import static android.view.Gravity.START;

/**
 * Created by 大灯泡 on 2020/7/11.
 * rtl demo activity
 */
public class RTLActivity extends BaseActivity {

    DemoPopup mDemoPopup;

    @BindView(R.id.rtl_root)
    View rootView;

    @Override
    protected void onHandleIntent(Intent intent) {

    }

    @Override
    public int contentViewLayoutId() {
        return R.layout.activity_rtl_demo;
    }

    @Override
    protected void onInitView(View decorView) {

    }

    @OnClick(R.id.tv_left_top)
    void leftTopClick(View v) {
        showPopup(v, END);
    }

    @OnClick(R.id.tv_right_top)
    void rightTopClick(View v) {
        showPopup(v, START);
    }

    void showPopup(View v, int gravity) {
        if (mDemoPopup == null) {
            mDemoPopup = new DemoPopup(this);
            mDemoPopup.setLayoutDirection(rootView.getLayoutDirection());
        }
        mDemoPopup.setPopupGravity(Gravity.BOTTOM | gravity);
        mDemoPopup.setText("当前BasePopup的Gravity：\n" + PopupUtils.gravityToString(mDemoPopup.getPopupGravity()));
        mDemoPopup.showPopupWindow(v);
    }

}
