package razerdp.demo.ui.rtl;

import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;

import androidx.viewbinding.ViewBinding;
import razerdp.basepopup.databinding.ActivityRtlDemoBinding;
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

    View rootView;

    @Override
    protected void onHandleIntent(Intent intent) {

    }

    @Override
    public ViewBinding onCreateViewBinding(LayoutInflater layoutInflater) {
        return ActivityRtlDemoBinding.inflate(layoutInflater);
    }

    @Override
    protected void onInitView(View decorView) {

    }

    void leftTopClick(View v) {
        showPopup(v, END);
    }

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
