package razerdp.demo.ui.issuestest;

import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.viewbinding.ViewBinding;
import razerdp.basepopup.databinding.ActivityIssue224Binding;
import razerdp.demo.base.baseactivity.BaseActivity;
import razerdp.demo.base.baseactivity.BaseBindingActivity;
import razerdp.demo.popup.issue.PopupIssue224;
import razerdp.demo.utils.StringUtil;
import razerdp.demo.widget.DPTextView;

/**
 * Created by 大灯泡 on 2020/4/6.
 */
public class Issue224TestActivity extends BaseBindingActivity<ActivityIssue224Binding> {
    PopupIssue224 mPopupIssue224;


    @Override
    protected void onHandleIntent(Intent intent) {

    }

    @Override
    public ActivityIssue224Binding onCreateViewBinding(LayoutInflater layoutInflater) {
        return ActivityIssue224Binding.inflate(layoutInflater);
    }

    @Override
    protected void onInitView(View decorView) {
        mBinding.tvShow.setOnClickListener(v -> show());

    }

    void show() {
        if (mPopupIssue224 == null) {
            mPopupIssue224 = new PopupIssue224(this);
            mPopupIssue224.setPopupGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL)
                    .setBackground(0)
                    .setOutSideDismiss(false)
                    .setOutSideTouchable(true)
                    .linkTo(mBinding.layoutTestContainer);
            mPopupIssue224.getPopupWindow().setFocusable(false);
        }

        int value = StringUtil.toInt(mBinding.edNum.getText().toString().trim());
        mPopupIssue224.setItemCount(value);
        if (!mPopupIssue224.isShowing() && value > 0) {
            mPopupIssue224.showPopupWindow(mBinding.layoutTestContainer);
        }
    }

}
