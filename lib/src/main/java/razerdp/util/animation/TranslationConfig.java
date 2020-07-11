package razerdp.util.animation;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.util.Property;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

import razerdp.util.PopupUtils;
import razerdp.util.log.PopupLog;

public class TranslationConfig extends BaseAnimationConfig<TranslationConfig> {
    private static final String TAG = "TranslationConfig";
    float fromX, toX, fromY, toY;
    boolean isPercentageFromX, isPercentageToX, isPercentageFromY, isPercentageToY;


    public TranslationConfig from(Direction... directions) {
        if (directions != null) {
            fromX = fromY = 0;
            int flag = Gravity.NO_GRAVITY;
            for (Direction direction : directions) {
                flag |= direction.gravity;
            }

            PopupLog.i(TAG, "from", PopupUtils.gravityToString(flag));
            if (contains(flag, Gravity.LEFT)) {
                fromX(fromX - 1, true);
            }
            if (contains(flag, Gravity.RIGHT)) {
                fromX(fromX + 1, true);
            }
            if (contains(flag, Gravity.CENTER_HORIZONTAL)) {
                fromX(fromX + 0.5f, true);
            }
            if (contains(flag, Gravity.TOP)) {
                fromY(fromY - 1, true);
            }
            if (contains(flag, Gravity.BOTTOM)) {
                fromY(fromY + 1, true);
            }
            if (contains(flag, Gravity.CENTER_VERTICAL)) {
                fromY(fromY + 0.5f, true);
            }
            isPercentageFromX = isPercentageFromY = isPercentageToX = isPercentageToY = true;
        }
        return this;
    }

    public TranslationConfig to(Direction... directions) {
        if (directions != null) {
            toX = toY = 0;
            int flag = Gravity.NO_GRAVITY;
            for (Direction direction : directions) {
                flag |= direction.gravity;
            }
            PopupLog.i(TAG, "to", PopupUtils.gravityToString(flag));
            if (contains(flag, Gravity.LEFT)) {
                toX += -1;
            }
            if (contains(flag, Gravity.RIGHT)) {
                toX += 1;
            }
            if (contains(flag, Gravity.CENTER_HORIZONTAL)) {
                toX += .5f;
            }
            if (contains(flag, Gravity.TOP)) {
                toY += -1;
            }
            if (contains(flag, Gravity.BOTTOM)) {
                toY += 1;
            }
            if (contains(flag, Gravity.CENTER_VERTICAL)) {
                toY += .5f;
            }
            isPercentageFromX = isPercentageFromY = isPercentageToX = isPercentageToY = true;
        }
        return this;
    }

    public TranslationConfig fromX(float fromX) {
        fromX(fromX, true);
        return this;
    }

    public TranslationConfig toX(float toX) {
        toX(toX, true);
        return this;
    }

    public TranslationConfig fromY(float fromY) {
        fromY(fromY, true);
        return this;
    }

    public TranslationConfig toY(float toY) {
        toY(toY, true);
        return this;
    }

    public TranslationConfig fromX(int fromX) {
        fromX(fromX, false);
        return this;
    }

    public TranslationConfig toX(int toX) {
        toX(toX, false);
        return this;
    }

    public TranslationConfig fromY(int fromY) {
        fromY(fromY, false);
        return this;
    }

    public TranslationConfig toY(int toY) {
        toY(toY, false);
        return this;
    }

    TranslationConfig fromX(float fromX, boolean percentage) {
        this.isPercentageFromX = percentage;
        this.fromX = fromX;
        return this;
    }

    TranslationConfig toX(float toX, boolean percentage) {
        isPercentageToX = percentage;
        this.toX = toX;
        return this;
    }

    TranslationConfig fromY(float fromY, boolean percentage) {
        isPercentageFromY = percentage;
        this.fromY = fromY;
        return this;
    }

    TranslationConfig toY(float toY, boolean percentage) {
        isPercentageToY = percentage;
        this.toY = toY;
        return this;
    }

    @Override
    public String toString() {
        return "TranslationConfig{" +
                "fromX=" + fromX +
                ", toX=" + toX +
                ", fromY=" + fromY +
                ", toY=" + toY +
                ", isPercentageFromX=" + isPercentageFromX +
                ", isPercentageToX=" + isPercentageToX +
                ", isPercentageFromY=" + isPercentageFromY +
                ", isPercentageToY=" + isPercentageToY +
                '}';
    }

    @Override
    protected Animation buildAnimation(boolean isRevert) {
        Animation animation = new TranslateAnimation(isPercentageFromX ? Animation.RELATIVE_TO_SELF : Animation.ABSOLUTE,
                                                     fromX,
                                                     isPercentageToX ? Animation.RELATIVE_TO_SELF : Animation.ABSOLUTE,
                                                     toX,
                                                     isPercentageFromY ? Animation.RELATIVE_TO_SELF : Animation.ABSOLUTE,
                                                     fromY,
                                                     isPercentageToY ? Animation.RELATIVE_TO_SELF : Animation.ABSOLUTE,
                                                     toY);
        deploy(animation);
        return animation;
    }

    @Override
    protected Animator buildAnimator(boolean isRevert) {
        AnimatorSet animatorSet = new AnimatorSet();

        Property<View, Float> TRANSLATION_X = (isPercentageFromX && isPercentageToY) ? new FloatPropertyCompat<View>(View.TRANSLATION_X.getName()) {
            @Override
            public void setValue(View object, float value) {
                object.setTranslationX(object.getWidth() * value);
            }

            @Override
            public Float get(View object) {
                return object.getTranslationX();
            }
        } : View.TRANSLATION_X;
        Property<View, Float> TRANSLATION_Y = (isPercentageFromY && isPercentageToY) ? new FloatPropertyCompat<View>(View.TRANSLATION_Y.getName()) {
            @Override
            public void setValue(View object, float value) {
                object.setTranslationY(object.getHeight() * value);
            }

            @Override
            public Float get(View object) {
                return object.getTranslationY();
            }
        } : View.TRANSLATION_Y;
        ObjectAnimator translationX = ObjectAnimator.ofFloat(null, TRANSLATION_X, fromX, toX);
        ObjectAnimator translationY = ObjectAnimator.ofFloat(null, TRANSLATION_Y, fromY, toY);
        animatorSet.playTogether(translationX, translationY);
        deploy(animatorSet);
        return animatorSet;
    }

    boolean contains(int what, int flag) {
        return (what & flag) == flag;
    }

}
