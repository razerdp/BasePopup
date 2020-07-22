package razerdp.demo.ui.issuestest;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

import butterknife.BindView;
import butterknife.OnClick;
import razerdp.basepopup.R;
import razerdp.demo.base.baseactivity.BaseActivity;
import razerdp.demo.popup.issue.PopupIssue236;
import razerdp.demo.widget.DPTextView;

/**
 * Created by 大灯泡 on 2019/11/24.
 * <p>
 * https://github.com/razerdp/BasePopup/issues/236
 */
public class Issue236TestActivity extends BaseActivity {
    @BindView(R.id.tv_show)
    DPTextView mTvShow;
    PopupIssue236 mPopupIssue236;

    @Override
    protected void onHandleIntent(Intent intent) {

    }

    @Override
    public int contentViewLayoutId() {
        return R.layout.activity_issue_236;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onInitView(View decorView) {
        final ViewConfiguration configuration = ViewConfiguration.get(this);
        mTvShow.setOnTouchListener(new View.OnTouchListener() {
            float x, y;
            boolean onMove;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        onMove = false;
                        x = event.getX();
                        y = event.getY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        float offsetX = event.getX() - x;
                        float offsetY = event.getY() - y;
                        if (Math.abs(offsetX) > configuration.getScaledTouchSlop() ||
                                Math.abs(offsetY) > configuration.getScaledTouchSlop()) {
                            v.offsetLeftAndRight((int) offsetX);
                            v.offsetTopAndBottom((int) offsetY);
                            onMove = true;
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        if (onMove) {
                            onMove = false;
                            return true;
                        }
                        break;
                }
                return false;
            }
        });
    }

    @OnClick(R.id.tv_show)
    void show(View v) {
        if (mPopupIssue236 == null) {
            mPopupIssue236 = new PopupIssue236(this);
        }
        mPopupIssue236.showPopupWindow(v);
    }

}
