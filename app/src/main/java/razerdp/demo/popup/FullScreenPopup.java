package razerdp.demo.popup;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.view.View;
import android.view.animation.Animation;

import razerdp.basepopup.BasePopupWindow;
import razerdp.basepopup.R;
import razerdp.demo.interpolator.OverShootInterpolator;

/**
 * Created by 大灯泡 on 2016/10/11.
 * <p>
 * 全屏的popup
 */

public class FullScreenPopup extends BasePopupWindow {

    public FullScreenPopup(Activity context) {
        super(context);
        /**全屏popup*/
        setPopupWindowFullScreen(true);
        setBlurBackgroundEnable(true);
    }

    @Override
    protected Animation onCreateShowAnimation() {
        return null;
    }

    @Override
    protected Animation onCreateDismissAnimation() {
        return null;
    }

    @Override
    public Animator onCreateShowAnimator() {
        AnimatorSet set;
        set = new AnimatorSet();
        ObjectAnimator transAnimator = ObjectAnimator.ofFloat(mDisplayAnimateView, "translationY", 250, 0).setDuration(600);
        transAnimator.setInterpolator(new OverShootInterpolator());
        ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(mDisplayAnimateView, "alpha", 0.4f, 1).setDuration(250 * 3 / 2);
        set.playTogether(transAnimator, alphaAnimator);
        return set;
    }

    @Override
    public Animator onCreateDismissAnimator() {
        AnimatorSet set;
        set = new AnimatorSet();
        ObjectAnimator transAnimator = ObjectAnimator.ofFloat(mDisplayAnimateView, "translationY", 0, 250).setDuration(600);
        transAnimator.setInterpolator(new OverShootInterpolator(-6));
        ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(mDisplayAnimateView, "alpha", 1f, 0).setDuration(800);
        set.playTogether(transAnimator, alphaAnimator);
        return set;
    }


    @Override
    public View onCreateContentView() {
        return createPopupById(R.layout.popup_fullscreen);
    }
}
