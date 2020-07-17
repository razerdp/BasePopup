package razerdp.util.animation;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;

public class ScaleConfig extends BaseAnimationConfig<ScaleConfig> {
    float scaleFromX = 0;
    float scaleFromY = 0;
    float scaleToX = 1;
    float scaleToY = 1;
    boolean changeFrom;
    boolean changeTo;

    @Override
    void resetInternal() {
        scaleFromX = 0;
        scaleFromY = 0;
        scaleToX = 1;
        scaleToY = 1;
        changeFrom = false;
        changeTo = false;
        pivot(.5f, .5f);
        pivot2(.5f, .5f);
    }

    public ScaleConfig() {
        super(false, false);
        resetInternal();
    }

    ScaleConfig(boolean resetParent, boolean resetInternal) {
        super(resetParent, resetInternal);
        resetInternal();
    }

    public ScaleConfig scale(float from, float to) {
        scaleFromX = scaleFromY = from;
        scaleToX = scaleToY = to;
        changeFrom = changeTo = true;
        return this;
    }

    public ScaleConfig from(Direction... from) {
        if (from != null) {
            if (!changeFrom) {
                scaleFromX = scaleFromY = 1;
            }
            int flag = 0;
            for (Direction direction : from) {
                flag |= direction.flag;
            }
            if (Direction.isDirectionFlag(Direction.LEFT, flag)) {
                pivotX = 0;
                scaleFromX = changeFrom ? scaleFromX : 0;
            }
            if (Direction.isDirectionFlag(Direction.RIGHT, flag)) {
                pivotX = 1;
                scaleFromX = changeFrom ? scaleFromX : 0;
            }
            if (Direction.isDirectionFlag(Direction.CENTER_HORIZONTAL, flag)) {
                pivotX = 0.5f;
                scaleFromX = changeFrom ? scaleFromX : 0;
            }
            if (Direction.isDirectionFlag(Direction.TOP, flag)) {
                pivotY = 0;
                scaleFromY = changeFrom ? scaleFromY : 0;
            }
            if (Direction.isDirectionFlag(Direction.BOTTOM, flag)) {
                pivotY = 1;
                scaleFromY = changeFrom ? scaleFromY : 0;
            }
            if (Direction.isDirectionFlag(Direction.CENTER_VERTICAL, flag)) {
                pivotY = 0.5f;
                scaleFromY = changeFrom ? scaleFromY : 0;
            }
        }
        return this;
    }

    public ScaleConfig to(Direction... to) {
        if (to != null) {
            if (!changeTo) {
                scaleToX = scaleToY = 1;
            }
            int flag = 0;
            for (Direction direction : to) {
                flag |= direction.flag;
            }
            if (Direction.isDirectionFlag(Direction.LEFT, flag)) {
                pivotX2 = 0;
            }
            if (Direction.isDirectionFlag(Direction.RIGHT, flag)) {
                pivotX2 = 1;
            }
            if (Direction.isDirectionFlag(Direction.CENTER_HORIZONTAL, flag)) {
                pivotX2 = 0.5f;
            }
            if (Direction.isDirectionFlag(Direction.TOP, flag)) {
                pivotY2 = 0;
            }
            if (Direction.isDirectionFlag(Direction.BOTTOM, flag)) {
                pivotY2 = 1;
            }
            if (Direction.isDirectionFlag(Direction.CENTER_VERTICAL, flag)) {
                pivotY2 = 0.5f;
            }
        }
        return this;
    }

    public ScaleConfig scaleX(float from, float to) {
        scaleFromX = from;
        scaleToX = to;
        changeFrom = true;
        return this;
    }

    public ScaleConfig sclaeY(float from, float to) {
        scaleFromY = from;
        scaleToY = to;
        changeTo = true;
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
                Animation.RELATIVE_TO_SELF, values[4],
                Animation.RELATIVE_TO_SELF, values[5]);
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

    //------------------default
    public static final ScaleConfig LEFT_TO_RIGHT = new ScaleConfig(true, true) {
        @Override
        void resetInternal() {
            super.resetInternal();
            from(Direction.LEFT);
            to(Direction.RIGHT);
        }
    };
    public static final ScaleConfig RIGHT_TO_LEFT = new ScaleConfig(true, true) {
        @Override
        void resetInternal() {
            super.resetInternal();
            from(Direction.RIGHT);
            to(Direction.LEFT);
        }
    };
    public static final ScaleConfig TOP_TO_BOTTOM = new ScaleConfig(true, true) {
        @Override
        void resetInternal() {
            super.resetInternal();
            from(Direction.TOP);
            to(Direction.BOTTOM);
        }
    };
    public static final ScaleConfig BOTTOM_TO_TOP = new ScaleConfig(true, true) {
        @Override
        void resetInternal() {
            super.resetInternal();
            from(Direction.BOTTOM);
            to(Direction.TOP);
        }
    };

    public static final ScaleConfig CENTER = new ScaleConfig(true, true) {
        @Override
        void resetInternal() {
            super.resetInternal();
            from(Direction.CENTER);
            to(Direction.CENTER);
        }
    };
}

