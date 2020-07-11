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

    public AlphaConfig() {
    }

    public AlphaConfig from(@FloatRange(from = 0, to = 1) float from) {
        alphaFrom = from;
        return this;
    }

    public AlphaConfig to(@FloatRange(from = 0, to = 1) float to) {
        alphaTo = to;
        return this;
    }

    public AlphaConfig from(int from) {
        alphaFrom = (float) (from / 255) + 0.5f;
        return this;
    }

    public AlphaConfig to(int to) {
        alphaFrom = (float) (to / 255) + 0.5f;
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
        AlphaAnimation animation = new AlphaAnimation(alphaFrom, alphaTo);
        deploy(animation);
        return animation;
    }

    @Override
    protected Animator buildAnimator(boolean isRevert) {
        Animator animator = ObjectAnimator.ofFloat(null, View.ALPHA, alphaFrom, alphaTo);
        deploy(animator);
        return animator;
    }
}

