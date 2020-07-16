package razerdp.util.animation;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;

public class RotationConfig extends BaseAnimationConfig<RotationConfig> {
    float from;
    float to;

    @Override
    void resetInternal() {
        from = to = 0;
        pivot(.5f, .5f);
    }

    public RotationConfig() {
        super(false, false);
        resetInternal();
    }

    RotationConfig(boolean resetParent, boolean resetInternal) {
        super(resetParent, resetInternal);
        resetInternal();
    }

    public RotationConfig from(float from) {
        this.from = from;
        return this;
    }

    public RotationConfig to(float to) {
        this.to = to;
        return this;
    }

    @Override
    protected Animation buildAnimation(boolean isRevert) {
        RotateAnimation rotateAnimation = new RotateAnimation(from,
                                                              to,
                                                              Animation.RELATIVE_TO_SELF,
                                                              pivotX,
                                                              Animation.RELATIVE_TO_SELF,
                                                              pivotY);
        deploy(rotateAnimation);
        return rotateAnimation;
    }

    @Override
    protected Animator buildAnimator(boolean isRevert) {
        final Animator rotate = ObjectAnimator.ofFloat(null, View.ROTATION, from, to);
        rotate.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                Object target = ((ObjectAnimator) animation).getTarget();
                if (target instanceof View) {
                    ((View) target).setPivotX(((View) target).getWidth() * pivotX);
                    ((View) target).setPivotY(((View) target).getHeight() * pivotY);
                }
            }
        });
        deploy(rotate);
        return rotate;
    }
}
