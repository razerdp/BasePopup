package razerdp.demo.ui.issuestest;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

import razerdp.basepopup.databinding.ActivityIssue236Binding;
import razerdp.demo.base.baseactivity.BaseBindingActivity;
import razerdp.demo.popup.issue.PopupIssue236;
import razerdp.demo.widget.DPTextView;

/**
 * Created by 大灯泡 on 2019/11/24.
 * <p>
 * https://github.com/razerdp/BasePopup/issues/236
 */
public class Issue236TestActivity extends BaseBindingActivity<ActivityIssue236Binding> {
    PopupIssue236 mPopupIssue236;

    @Override
    protected void onHandleIntent(Intent intent) {

    }

    @Override
    public ActivityIssue236Binding onCreateViewBinding(LayoutInflater layoutInflater) {
        return ActivityIssue236Binding.inflate(layoutInflater);
    }


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onInitView(View decorView) {
        final ViewConfiguration configuration = ViewConfiguration.get(this);
        mBinding.tvShow.setOnClickListener(v -> show(v));
        mBinding.tvShow.setOnTouchListener(new View.OnTouchListener() {
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

    void show(View v) {
        if (mPopupIssue236 == null) {
            mPopupIssue236 = new PopupIssue236(this);
        }
        mPopupIssue236.showPopupWindow(v);
    }

}
