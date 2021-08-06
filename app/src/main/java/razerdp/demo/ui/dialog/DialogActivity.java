package razerdp.demo.ui.dialog;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;
import razerdp.basepopup.R;
import razerdp.basepopup.databinding.ActivityDialogDemoBinding;
import razerdp.demo.base.baseactivity.BaseBindingActivity;
import razerdp.demo.popup.DemoPopup;
import razerdp.demo.utils.RomUtils;

/**
 * Created by 大灯泡 on 2020/8/17
 * <p>
 * Description：
 */
public class DialogActivity extends BaseBindingActivity<ActivityDialogDemoBinding> {
    DemoPopup demoPopup;
    TestDialog testDialog;
    TestDialog globalDialog;


    @Override
    protected void onHandleIntent(Intent intent) {

    }

    @Override
    public ActivityDialogDemoBinding onCreateViewBinding(LayoutInflater layoutInflater) {
        return ActivityDialogDemoBinding.inflate(layoutInflater);
    }

    @Override
    protected void onInitView(View decorView) {
        mBinding.showPopup.setOnClickListener(v -> showPopup());
        mBinding.showDialog.setOnClickListener(v -> showDialog());
        mBinding.showApplicationDialog.setOnClickListener(v -> showGlobalDialog());

    }

    void showPopup() {
        if (demoPopup == null) {
            demoPopup = new DemoPopup(this);
        }
        demoPopup.showPopupWindow();
    }

    void showDialog() {
        if (testDialog == null) {
            testDialog = new TestDialog(this);
        }
        testDialog.show();
    }

    @AfterPermissionGranted(1001)
    void showGlobalDialog() {
        if (globalDialog == null) {
            globalDialog = new TestDialog(getApplicationContext());
            if (RomUtils.isXiaomi()) {
                globalDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_TOAST);
            } else {
                globalDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
            }
        }
        if (EasyPermissions.hasPermissions(this, Manifest.permission.SYSTEM_ALERT_WINDOW)) {
            globalDialog.show();
        } else {
            EasyPermissions.requestPermissions(this, "请允许弹窗权限", 1001, Manifest.permission.SYSTEM_ALERT_WINDOW);
        }
    }


    static class TestDialog extends Dialog {
        TextView tvShow;
        DemoPopup mDemoPopup;

        TestDialog(@NonNull Context context) {
            super(context);
            setContentView(R.layout.view_dialog);
            tvShow = findViewById(R.id.tv_show);
            tvShow.setOnClickListener(v -> showPopup());
        }

        private void showPopup() {
            if (mDemoPopup == null) {
                mDemoPopup = new DemoPopup(this).setText("兼容性测试\n\nBttomSheetDialog内弹出BasePopup");
            }
            mDemoPopup.setPopupGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL);
            mDemoPopup.showPopupWindow(tvShow);
        }
    }

}
