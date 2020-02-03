package razerdp.demo.ui.lifecycle;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.OnClick;
import razerdp.basepopup.BasePopupComponentManager;
import razerdp.basepopup.R;
import razerdp.demo.base.baseactivity.BaseActivity;
import razerdp.demo.popup.PopupShowOnCreate;
import razerdp.demo.ui.ActivityLauncher;

/**
 * Created by 大灯泡 on 2020/2/3.
 */
public class ShowOnCreateActivity extends BaseActivity {
    @BindView(R.id.tv_desc)
    TextView mTvDesc;
    @BindView(R.id.btn_open_own)
    Button mBtnOpenOwn;
    @BindView(R.id.btn_show_popup)
    Button mBtnShowPopup;
    PopupShowOnCreate popupShowOnCreate;
    @BindView(R.id.view_scroller)
    ScrollView mViewScroller;

    @Override
    protected void onHandleIntent(Intent intent) {

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        popupShowOnCreate = new PopupShowOnCreate(this);
        popupShowOnCreate.setOnErrorPrintListener(msg -> {
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
    public int contentViewLayoutId() {
        return R.layout.activity_showoncreate;
    }

    @Override
    protected void onInitView(View decorView) {
        mTvDesc.append("是否有AndroidX组件：" + BasePopupComponentManager.getInstance().hasComponent());
    }

    @OnClick(R.id.btn_open_own)
    void openSelf() {
        ActivityLauncher.start(this, ShowOnCreateActivity.class);
    }

    @OnClick(R.id.btn_show_popup)
    void reShow() {
        if (popupShowOnCreate != null) {
            popupShowOnCreate.showPopupWindow();
        }
    }

}
