package razerdp.util.animation;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

import androidx.annotation.FloatRange;

public class AlphaConfig extends BaseAnimationConfig<AlphaConfig> {
    float alphaFrom;
    float alphaTo;
    boolean changed;

    @Override
    void resetInternal() {
        alphaFrom = 0f;
        alphaTo = 1f;
        changed = false;
    }

    public AlphaConfig() {
        super(false, false);
        resetInternal();
    }

    AlphaConfig(boolean resetParent, boolean resetInternal) {
        super(resetParent, resetInternal);
        resetInternal();
    }

    public AlphaConfig from(@FloatRange(from = 0, to = 1) float from) {
        alphaFrom = from;
        changed = true;
        return this;
    }

    public AlphaConfig to(@FloatRange(from = 0, to = 1) float to) {
        alphaTo = to;
        changed = true;
        return this;
    }

    public AlphaConfig from(int from) {
        alphaFrom = (float) (Math.max(0, Math.min(255, from)) / 255) + 0.5f;
        changed = true;
        return this;
    }

    public AlphaConfig to(int to) {
        alphaFrom = (float) (Math.max(0, Math.min(255, to)) / 255) + 0.5f;
        changed = true;
        return this;
    }

    @Override
    public String toString() {
        return "AlphaConfig{" +
                "alphaFrom=" + alphaFrom +
                ", alphaTo=" + alphaTo +
                '}';
    }

    @Override
    protected Animation buildAnimation(boolean isRevert) {
        AlphaAnimation animation = new AlphaAnimation((isRevert && !changed) ? alphaTo : alphaFrom,
                (isRevert && !changed) ? alphaFrom : alphaTo);
        deploy(animation);
        return animation;
    }

    @Override
    protected Animator buildAnimator(boolean isRevert) {
        Animator animator = ObjectAnimator.ofFloat(null,
                View.ALPHA,
                (isRevert && !changed) ? alphaTo : alphaFrom,
                (isRevert && !changed) ? alphaFrom : alphaTo);
        deploy(animator);
        return animator;
    }

    //------------------default
    public static final AlphaConfig IN = new AlphaConfig(true, true) {
        @Override
        void resetInternal() {
            super.resetInternal();
            from(0f);
            to(1f);
        }

    };

    public static final AlphaConfig OUT = new AlphaConfig(true, true) {
        @Override
        void resetInternal() {
            super.resetInternal();
            from(1f);
            to(0f);
        }

    };
}

