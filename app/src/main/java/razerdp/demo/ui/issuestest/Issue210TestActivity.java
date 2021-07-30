package razerdp.demo.ui.issuestest;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.viewbinding.ViewBinding;
import razerdp.basepopup.databinding.ActivityIssue210Binding;
import razerdp.demo.base.baseactivity.BaseActivity;
import razerdp.demo.popup.issue.PopupIssue210;

/**
 * Created by 大灯泡 on 2019/9/22.
 * <p>
 * 针对Issue：https://github.com/razerdp/BasePopup/issues/210
 */
public class Issue210TestActivity extends BaseActivity {
    TextView mTvBar;
    AppCompatCheckBox mCheckOutsideTouch;

    PopupIssue210 mPopupIssue210;

    @Override
    protected void onHandleIntent(Intent intent) {

    }

    @Override
    public ViewBinding onCreateViewBinding(LayoutInflater layoutInflater) {
        return ActivityIssue210Binding.inflate(layoutInflater);
    }

    @Override
    protected void onInitView(View decorView) {

    }

    void show(View v) {
        if (mPopupIssue210 == null) {
            mPopupIssue210 = new PopupIssue210(this);
        }
        mPopupIssue210.setOutSideTouchable(mCheckOutsideTouch.isChecked());
        mPopupIssue210.showPopupWindow(v);
    }

}
