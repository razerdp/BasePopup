package razerdp.demo.ui.fullscreen;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.viewbinding.ViewBinding;
import razerdp.basepopup.BasePopupUnsafe;
import razerdp.basepopup.databinding.ActivityFullScreenBinding;
import razerdp.demo.base.baseactivity.BaseActivity;
import razerdp.demo.base.baseactivity.BaseBindingActivity;
import razerdp.demo.popup.DemoPopup;
import razerdp.demo.ui.ActivityLauncher;
import razerdp.demo.ui.friendcircle.FriendCircleActivity;

/**
 * Created by 大灯泡 on 2020/5/17.
 */
public class FullScreenActivity extends BaseBindingActivity<ActivityFullScreenBinding> {
    DemoPopup mDemoPopup;

    @Override
    protected void onHandleIntent(Intent intent) {

    }



    @Override
    public ActivityFullScreenBinding onCreateViewBinding(LayoutInflater layoutInflater) {
        return ActivityFullScreenBinding.inflate(layoutInflater);
    }

    @Override
    protected void onInitView(View decorView) {
        mBinding.tvTest.setOnClickListener(v -> show());
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mDemoPopup != null && mDemoPopup.isShowing()) {
            View decor = BasePopupUnsafe.INSTANCE.getBasePopupDecorViewProxy(mDemoPopup);
            if (decor != null) {
                decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
            }
        }
    }

    void show() {
        if (mDemoPopup == null) {
            mDemoPopup = new DemoPopup(this);
            mDemoPopup.setText("FullScreenTest");
            mDemoPopup.getTextView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ActivityLauncher.start(v.getContext(), FriendCircleActivity.class);
                }
            });
        }
        mDemoPopup.showPopupWindow();
    }
}
