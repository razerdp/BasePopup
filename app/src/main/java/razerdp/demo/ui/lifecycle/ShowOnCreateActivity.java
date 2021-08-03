package razerdp.demo.ui.lifecycle;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;
import razerdp.basepopup.databinding.ActivityShowoncreateBinding;
import razerdp.demo.base.baseactivity.BaseActivity;
import razerdp.demo.base.baseactivity.BaseBindingActivity;
import razerdp.demo.popup.PopupShowOnCreate;
import razerdp.demo.ui.ActivityLauncher;
import razerdp.util.log.PopupLog;

/**
 * Created by 大灯泡 on 2020/2/3.
 */
public class ShowOnCreateActivity extends BaseBindingActivity<ActivityShowoncreateBinding> {
    PopupShowOnCreate popupShowOnCreate;

    @Override
    protected void onHandleIntent(Intent intent) {

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View decor = getWindow().getDecorView();
        decor.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
            @Override
            public void onViewAttachedToWindow(View v) {
                PopupLog.e(TAG, v.getWindowToken());
            }

            @Override
            public void onViewDetachedFromWindow(View v) {
                PopupLog.e(TAG, v.getWindowToken());
            }
        });
        decor.post(new Runnable() {
            @Override
            public void run() {
                PopupLog.e(TAG, decor.getWindowToken());
            }
        });
        PopupLog.e(TAG, decor.getWindowToken());
        popupShowOnCreate = new PopupShowOnCreate(this);
        popupShowOnCreate.setOnErrorPrintListener(msg -> {
            mBinding.tvDesc.append("\n");
            mBinding.tvDesc.append(msg);
            mBinding.viewScroller.post(() -> {
                mBinding.viewScroller.fullScroll(View.FOCUS_DOWN);
            });
        });
        popupShowOnCreate.showPopupWindow();
        mBinding.btnOpenOwn.setOnClickListener(v -> openSelf());
        mBinding.btnShowPopup.setOnClickListener(v -> reShow());
    }

    @Override
    public ActivityShowoncreateBinding onCreateViewBinding(LayoutInflater layoutInflater) {
        return ActivityShowoncreateBinding.inflate(layoutInflater);
    }

    @Override
    protected void onInitView(View decorView) {
    }

    void openSelf() {
        ActivityLauncher.start(this, ShowOnCreateActivity.class);
    }

    void reShow() {
        if (popupShowOnCreate != null) {
            popupShowOnCreate.showPopupWindow();
        }
    }

}
