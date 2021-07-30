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
import razerdp.demo.popup.issue.PopupIssue224;
import razerdp.demo.utils.StringUtil;
import razerdp.demo.widget.DPTextView;

/**
 * Created by 大灯泡 on 2020/4/6.
 */
public class Issue224TestActivity extends BaseActivity {
    DPTextView mTvShow;
    EditText mEdNum;
    LinearLayout testLayout;
    AppCompatCheckBox mCheckFitSize;

    PopupIssue224 mPopupIssue224;


    @Override
    protected void onHandleIntent(Intent intent) {

    }

    @Override
    public ViewBinding onCreateViewBinding(LayoutInflater layoutInflater) {
        return ActivityIssue224Binding.inflate(layoutInflater);
    }

    @Override
    protected void onInitView(View decorView) {

    }

    void show() {
        if (mPopupIssue224 == null) {
            mPopupIssue224 = new PopupIssue224(this);
            mPopupIssue224.setPopupGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL)
                    .setBackground(0)
                    .setOutSideDismiss(false)
                    .setOutSideTouchable(true)
                    .linkTo(testLayout);
            mPopupIssue224.getPopupWindow().setFocusable(false);
        }

        int value = StringUtil.toInt(mEdNum.getText().toString().trim());
        mPopupIssue224.setItemCount(value);
        mPopupIssue224.setFitSize(mCheckFitSize.isChecked());
        if (!mPopupIssue224.isShowing() && value > 0) {
            mPopupIssue224.showPopupWindow(testLayout);
        }
    }

}
