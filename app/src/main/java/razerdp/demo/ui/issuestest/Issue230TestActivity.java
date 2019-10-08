package razerdp.demo.ui.issuestest;

import android.content.Intent;
import android.view.View;

import butterknife.BindView;
import butterknife.OnClick;
import razerdp.basepopup.R;
import razerdp.demo.base.baseactivity.BaseActivity;
import razerdp.demo.popup.issue.PopupIssue230;
import razerdp.demo.widget.DPTextView;

/**
 * Created by 大灯泡 on 2019/10/8.
 * <p>
 * https://github.com/razerdp/BasePopup/issues/230
 */
public class Issue230TestActivity extends BaseActivity {
    @BindView(R.id.tv_show)
    DPTextView mTvShow;

    @Override
    protected void onHandleIntent(Intent intent) {

    }

    @Override
    public int contentViewLayoutId() {
        return R.layout.activity_issue_230;
    }

    @Override
    protected void onInitView(View decorView) {

    }

    @OnClick(R.id.tv_show)
    void show() {
        new PopupIssue230(this).showPopupWindow();
    }

}
