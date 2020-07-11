package razerdp.util.animation;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;

public class ScaleConfig extends BaseAnimationConfig<ScaleConfig> {
    float scaleFromX = 1;
    float scaleFromY = 1;
    float scaleToX = 1;
    float scaleToY = 1;


    public ScaleConfig scale(float from, float to) {
        scaleFromX = scaleFromY = from;
        scaleToX = scaleToY = to;
        return this;
    }

    public ScaleConfig from(Direction... from) {
        if (from != null) {
            pivotX = pivotY = 0;
            int flag = Gravity.NO_GRAVITY;
            for (Direction direction : from) {
                flag |= direction.gravity;
            }
            if (contains(flag, Gravity.LEFT)) {
                pivotX += 0;
            }
            if (contains(flag, Gravity.RIGHT)) {
                pivotX += 1;
            }
            if (contains(flag, Gravity.CENTER_HORIZONTAL)) {
                pivotX += 0.5f;
            }
            if (contains(flag, Gravity.TOP)) {
                pivotY += 0;
            }
            if (contains(flag, Gravity.BOTTOM)) {
                pivotY += 1;
            }
            if (contains(flag, Gravity.CENTER_VERTICAL)) {
                pivotY += 0.5f;
            }
        }
        return this;
    }

    public ScaleConfig to(Direction... to) {
        if (to != null) {
            if (scaleToX == 0) {
                scaleToX = 1;
            }
            if (scaleToY == 0) {
                scaleToY = 1;
            }
            pivotX2 = pivotY2 = 0;
            int flag = Gravity.NO_GRAVITY;
            for (Direction direction : to) {
                flag |= direction.gravity;
            }
            if (contains(flag, Gravity.LEFT)) {
                pivotX2 += 0;
            }
            if (contains(flag, Gravity.RIGHT)) {
                pivotX2 += 1;
            }
            if (contains(flag, Gravity.CENTER_HORIZONTAL)) {
                pivotX2 += 0.5f;
            }
            if (contains(flag, Gravity.TOP)) {
                pivotY2 += 0;
            }
            if (contains(flag, Gravity.BOTTOM)) {
                pivotY2 += 1;
            }
            if (contains(flag, Gravity.CENTER_VERTICAL)) {
                pivotY2 += 0.5f;
            }
        }
        return this;
    }

    public ScaleConfig scaleX(float from, float to) {
        scaleFromX = from;
        scaleToX = to;
        return this;
    }

    public ScaleConfig sclaeY(float from, float to) {
        scaleFromY = from;
        scaleToY = to;
        return this;
    }

    @Override
    public String toString() {
        return "ScaleConfig{" +
                "scaleFromX=" + scaleFromX +
                ", scaleFromY=" + scaleFromY +
                ", scaleToX=" + scaleToX +
                ", scaleToY=" + scaleToY +
                '}';
    }

    /**
     * 0 = fromx
     * 1 = tox
     * 2 = fromy
     * 3 = toy
     * 4 = pivotx
     * 5 = pivoty
     */
    float[] values(boolean isRevert) {
        float[] result = new float[6];
        result[0] = isRevert ? scaleToX : scaleFromX;
        result[1] = isRevert ? scaleFromX : scaleToX;
        result[2] = isRevert ? scaleToY : scaleFromY;
        result[3] = isRevert ? scaleFromY : scaleToY;
        result[4] = isRevert ? pivotX2 : pivotX;
        result[5] = isRevert ? pivotY2 : pivotY;
        return result;
    }

    @Override
    protected Animation buildAnimation(boolean isRevert) {
        float[] values = values(isRevert);
        Animation animation = new ScaleAnimation(values[0], values[1], values[2], values[3],
                                                 Animation.RELATIVE_TO_PARENT, values[4],
                                                 Animation.RELATIVE_TO_PARENT, values[5]);
        deploy(animation);
        return animation;
    }

    @Override
    protected Animator buildAnimator(boolean isRevert) {
        final float[] values = values(isRevert);
        AnimatorSet animatorSet = new AnimatorSet();
        final Animator scaleX = ObjectAnimator.ofFloat(null, View.SCALE_X, values[0], values[1]);
        final Animator scaleY = ObjectAnimator.ofFloat(null, View.SCALE_Y, values[2], values[3]);
        scaleX.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                Object target = ((ObjectAnimator) animation).getTarget();
                if (target instanceof View) {
                    ((View) target).setPivotX(((View) target).getWidth() * values[4]);
                }
            }
        });
        scaleY.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                Object target = ((ObjectAnimator) animation).getTarget();
                if (target instanceof View) {
                    ((View) target).setPivotY(((View) target).getHeight() * values[5]);
                }
            }
        });
        animatorSet.playTogether(scaleX, scaleY);
        deploy(animatorSet);
        return animatorSet;
    }

    boolean contains(int what, int flag) {
        return (what & flag) == flag;
    }
}

