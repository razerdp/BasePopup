package razerdp.demo.ui.issuestest;

import android.content.Intent;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.appcompat.widget.AppCompatCheckBox;
import butterknife.BindView;
import butterknife.OnClick;
import razerdp.basepopup.R;
import razerdp.demo.base.baseactivity.BaseActivity;
import razerdp.demo.popup.issue.PopupIssue224;
import razerdp.demo.utils.StringUtil;
import razerdp.demo.widget.DPTextView;

/**
 * Created by 大灯泡 on 2020/4/6.
 */
public class Issue224TestActivity extends BaseActivity {
    @BindView(R.id.tv_show)
    DPTextView mTvShow;
    @BindView(R.id.ed_num)
    EditText mEdNum;
    @BindView(R.id.layout_test_container)
    LinearLayout testLayout;
    @BindView(R.id.check_fit_size)
    AppCompatCheckBox mCheckFitSize;

    PopupIssue224 mPopupIssue224;


    @Override
    protected void onHandleIntent(Intent intent) {

    }

    @Override
    public int contentViewLayoutId() {
        return R.layout.activity_issue_224;
    }

    @Override
    protected void onInitView(View decorView) {

    }

    @OnClick(R.id.tv_show)
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
