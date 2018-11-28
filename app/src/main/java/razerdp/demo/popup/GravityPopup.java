package razerdp.demo.popup;

import android.app.Activity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;

import razerdp.basepopup.BasePopupWindow;
import razerdp.basepopup.R;
import razerdp.demo.utils.ToastUtils;

/**
 * Created by 大灯泡 on 2016/1/15.
 * gravityPopup
 */
public class GravityPopup extends BasePopupWindow implements View.OnClickListener {

    public GravityPopup(Activity context) {
        super(context, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        setPopupFadeEnable(true);
        bindEvent();
    }


    @Override
    protected Animation onCreateShowAnimation() {
        return getDefaultScaleAnimation();
    }

    @Override
    protected Animation onCreateDismissAnimation() {
        return getDefaultScaleAnimation(false);
    }

    @Override
    public View onCreateContentView() {
        return createPopupById(R.layout.popup_gravity);
    }

    private void bindEvent() {
        findViewById(R.id.tx_1).setOnClickListener(this);
        findViewById(R.id.tx_2).setOnClickListener(this);
        findViewById(R.id.tx_3).setOnClickListener(this);
    }

    @Override
    public void showPopupWindow(View anchorView) {
        initAnimate();
        super.showPopupWindow(anchorView);
    }

    private void initAnimate() {

        int gravity = getPopupGravity();

        float in_fromX = 0;
        float in_toX = 0;
        float in_fromY = 0;
        float in_toY = 0;

        float exit_fromX = 0;
        float exit_toX = 0;
        float exit_fromY = 0;
        float exit_toY = 0;

        switch (gravity & Gravity.HORIZONTAL_GRAVITY_MASK) {
            case Gravity.LEFT:
            case Gravity.START:
                in_fromX = 1f;
                exit_toX = 1f;
                break;
            case Gravity.RIGHT:
            case Gravity.END:
                in_toX = 1f;
                exit_fromX = 1f;
                break;
            case Gravity.CENTER_HORIZONTAL:
                break;
            default:
                break;
        }
        switch (gravity & Gravity.VERTICAL_GRAVITY_MASK) {
            case Gravity.TOP:
                in_fromY = 1f;
                exit_toY = 1f;
                break;
            case Gravity.BOTTOM:
                in_fromY = -1f;
                exit_toY = -1f;
                break;
            case Gravity.CENTER_VERTICAL:
                break;
            default:
                break;
        }
        setShowAnimation(createTranslateAnimate(in_fromX, in_toX, in_fromY, in_toY));
        setDismissAnimation(createTranslateAnimate(exit_fromX, exit_toX, exit_fromY, exit_toY));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tx_1:
                ToastUtils.ToastMessage(getContext(), "click tx_1");
                break;
            case R.id.tx_2:
                ToastUtils.ToastMessage(getContext(), "click tx_2");
                break;
            case R.id.tx_3:
                ToastUtils.ToastMessage(getContext(), "click tx_3");
                break;
            default:
                break;
        }

    }

    public Animation createTranslateAnimate(float fromX, float toX, float fromY, float toY) {
        Animation result = new TranslateAnimation(Animation.RELATIVE_TO_PARENT,
                fromX,
                Animation.RELATIVE_TO_PARENT,
                toX,
                Animation.RELATIVE_TO_PARENT,
                fromY,
                Animation.RELATIVE_TO_PARENT,
                toY);
        result.setInterpolator(new DecelerateInterpolator());
        result.setDuration(360);
        return result;
    }
}
