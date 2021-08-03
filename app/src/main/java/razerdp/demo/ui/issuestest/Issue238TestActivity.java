package razerdp.demo.ui.issuestest;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;

import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.viewbinding.ViewBinding;
import razerdp.basepopup.databinding.ActivityIssue238Binding;
import razerdp.demo.base.baseactivity.BaseActivity;
import razerdp.demo.base.baseactivity.BaseBindingActivity;
import razerdp.demo.popup.issue.PopupIssue238;
import razerdp.demo.widget.DPTextView;

/**
 * Created by 大灯泡 on 2020/2/26.
 */
public class Issue238TestActivity extends BaseBindingActivity<ActivityIssue238Binding> {
    PopupIssue238 mPopupIssue238;
    PopupIssue238 mPopupIssue238Ed;


    @Override
    protected void onHandleIntent(Intent intent) {

    }

    @Override
    public ActivityIssue238Binding onCreateViewBinding(LayoutInflater layoutInflater) {
        return ActivityIssue238Binding.inflate(layoutInflater);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onInitView(View decorView) {
        mBinding.tvShow.setOnClickListener(v -> show(v));
    }

    void show(View v) {
        boolean withEditText = mBinding.checkEdittext.isChecked();
        if (withEditText) {
            if (mPopupIssue238Ed == null) {
                mPopupIssue238Ed = new PopupIssue238(this, true);
            }
            mPopupIssue238Ed.showPopupWindow();
        } else {
            if (mPopupIssue238 == null) {
                mPopupIssue238 = new PopupIssue238(this, false);
            }
            mPopupIssue238.showPopupWindow();
        }
    }
}