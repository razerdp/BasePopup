package razerdp.demo.ui.issuestest;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.view.View;

import butterknife.BindView;
import butterknife.OnClick;
import razerdp.basepopup.R;
import razerdp.demo.base.baseactivity.BaseActivity;
import razerdp.demo.services.DelayDemoService;
import razerdp.demo.utils.UIHelper;
import razerdp.demo.utils.rx.RxHelper;
import razerdp.demo.widget.DPTextView;

/**
 * Created by 大灯泡 on 2020/02/11.
 * <p>
 * https://github.com/razerdp/BasePopup/issues/242
 */
public class Issue242TestActivity extends BaseActivity {
    @BindView(R.id.tv_show)
    DPTextView mTvShow;


    @Override
    protected void onHandleIntent(Intent intent) {

    }

    @Override
    public int contentViewLayoutId() {
        return R.layout.activity_issue_242;
    }

    @Override
    protected void onInitView(View decorView) {

    }


    @OnClick(R.id.tv_show)
    void show() {
        UIHelper.toast("切换到后台，并start service，5s后回到该activity");
        Intent intent = new Intent(this, DelayDemoService.class);
        startService(intent);
        moveTaskToBack(true);

        RxHelper.delay(5000, data -> {
            ActivityManager am = (ActivityManager) self().getSystemService(Context.ACTIVITY_SERVICE);
            am.moveTaskToFront(getTaskId(), ActivityManager.MOVE_TASK_WITH_HOME);
        });
    }

    @Override
    protected void onResume() {
        Intent intent = new Intent(this, DelayDemoService.class);
        stopService(intent);
        super.onResume();
    }
}
