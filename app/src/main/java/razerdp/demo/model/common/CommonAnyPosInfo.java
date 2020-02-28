package razerdp.demo.model.common;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.view.MotionEvent;
import android.view.View;

import razerdp.basepopup.BasePopupWindow;
import razerdp.demo.model.DemoCommonUsageInfo;
import razerdp.demo.popup.DemoPopup;
import razerdp.demo.popup.options.PopupAnyPosOption;

/**
 * Created by 大灯泡 on 2019/9/20.
 */
public class CommonAnyPosInfo extends DemoCommonUsageInfo {
    public boolean outSideTouchable = false;
    public boolean blur = false;

    PopupAnyPosOption mPopupAnyPosOption;
    DemoPopup mDemoPopup;
    float x, y;

    public CommonAnyPosInfo() {
        title = "任意位置展示";
    }

    @Override
    public void toShow(View v) {
        v.setVisibility(View.VISIBLE);
        v.animate().alpha(.85f).start();
        v.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        x = event.getRawX();
                        y = event.getRawY();
                        break;
                    case MotionEvent.ACTION_UP:
                        x = y = 0;
                        break;
                }
                return false;
            }
        });
        v.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showPopup(v);
                return true;
            }
        });

    }

    private void showPopup(View v) {
        if (mDemoPopup == null) {
            mDemoPopup = new DemoPopup(v.getContext()).setText("任意位置显示");
            mDemoPopup.setOnDismissListener(new BasePopupWindow.OnDismissListener() {
                @Override
                public void onDismissAnimationStart() {
                    v.animate().alpha(0f).setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            v.setVisibility(View.INVISIBLE);
                            v.animate().setListener(null);
                        }
                    }).start();
                }

                @Override
                public void onDismiss() {

                }
            });
        }
        mDemoPopup.setBlurBackgroundEnable(blur)
                .showPopupWindow((int) x, (int) y);
    }

    @Override
    public void toOption(View v) {
        if (mPopupAnyPosOption == null) {
            mPopupAnyPosOption = new PopupAnyPosOption(v.getContext());
            mPopupAnyPosOption.setInfo(this);
        }
        mPopupAnyPosOption.showPopupWindow(v);
    }
}
