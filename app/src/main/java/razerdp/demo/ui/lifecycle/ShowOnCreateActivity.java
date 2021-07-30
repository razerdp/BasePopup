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
import razerdp.demo.popup.PopupShowOnCreate;
import razerdp.demo.ui.ActivityLauncher;
import razerdp.util.log.PopupLog;

/**
 * Created by 大灯泡 on 2020/2/3.
 */
public class ShowOnCreateActivity extends BaseActivity {
    TextView mTvDesc;
    Button mBtnOpenOwn;
    Button mBtnShowPopup;
    PopupShowOnCreate popupShowOnCreate;
    ScrollView mViewScroller;

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
            if (mTvDesc == null) return;
            mTvDesc.append("\n");
            mTvDesc.append(msg);
            mViewScroller.post(() -> {
                if (mViewScroller != null) {
                    mViewScroller.fullScroll(View.FOCUS_DOWN);
                }
            });
        });
        popupShowOnCreate.showPopupWindow();

    }

    @Override
    public ViewBinding onCreateViewBinding(LayoutInflater layoutInflater) {
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
