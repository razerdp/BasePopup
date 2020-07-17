package razerdp.util.animation;

import android.animation.Animator;
import android.content.res.Resources;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Interpolator;

import androidx.annotation.FloatRange;

import razerdp.util.log.PopupLog;

public abstract class BaseAnimationConfig<T> {
    protected String TAG = this.getClass().getSimpleName();
    static final long DEFAULT_DURATION = Resources.getSystem().getInteger(android.R.integer.config_mediumAnimTime);
    static final Interpolator DEFAULT_INTERPOLATOR = new AccelerateDecelerateInterpolator();
    Interpolator interpolator = DEFAULT_INTERPOLATOR;
    long duration = DEFAULT_DURATION;
    float pivotX;
    float pivotY;
    float pivotX2;
    float pivotY2;
    boolean fillBefore;
    boolean fillAfter = true;
    final boolean mResetParent;
    final boolean mResetInternal;

    BaseAnimationConfig(boolean resetParent, boolean resetInternal) {
        this.mResetParent = resetParent;
        this.mResetInternal = resetInternal;
    }

    void reset() {
        duration = DEFAULT_DURATION;
        interpolator = DEFAULT_INTERPOLATOR;
        pivotX = pivotY = pivotY2 = 0;
        fillBefore = false;
        fillAfter = true;
    }

    void resetInternal() {

    }

    public T interpolator(Interpolator interpolator) {
        this.interpolator = interpolator;
        return (T) this;
    }

    public T duration(long duration) {
        this.duration = duration;
        return (T) this;
    }

    public T pivot(@FloatRange(from = 0, to = 1) float x, @FloatRange(from = 0, to = 1) float y) {
        pivotX = x;
        pivotY = y;
        return (T) this;
    }

    T pivot2(@FloatRange(from = 0, to = 1) float x, @FloatRange(from = 0, to = 1) float y) {
        pivotX2 = x;
        pivotY2 = y;
        return (T) this;
    }


    public T pivotX(@FloatRange(from = 0, to = 1) float x) {
        pivotX = x;
        return (T) this;
    }

    public T pivotY(@FloatRange(from = 0, to = 1) float y) {
        pivotY = y;
        return (T) this;
    }

    public T fillBefore(boolean fillBefore) {
        this.fillBefore = fillBefore;
        return (T) this;
    }

    public T fillAfter(boolean fillAfter) {
        this.fillAfter = fillAfter;
        return (T) this;
    }


    void deploy(Animation animation) {
        if (animation == null) return;
        animation.setFillBefore(fillBefore);
        animation.setFillAfter(fillAfter);
        animation.setDuration(duration);
        animation.setInterpolator(interpolator);
    }

    void deploy(Animator animator) {
        if (animator == null) return;
        animator.setDuration(duration);
        animator.setInterpolator(interpolator);
    }

    void log() {
        if (PopupLog.isOpenLog()) {
            PopupLog.i(TAG, $toString(), this.toString());
        }
    }

    public String $toString() {
        return "BaseConfig{" +
                "interpolator=" + (interpolator == null ? "null" : interpolator.getClass().getSimpleName()) +
                ", duration=" + duration +
                ", pivotX=" + pivotX +
                ", pivotY=" + pivotY +
                ", fillBefore=" + fillBefore +
                ", fillAfter=" + fillAfter +
                '}';
    }

    final Animation $buildAnimation(boolean isRevert) {
        log();
        Animation animation = buildAnimation(isRevert);
        if (mResetParent) {
            reset();
        }
        if (mResetInternal) {
            resetInternal();
        }
        return animation;
    }

    final Animator $buildAnimator(boolean isRevert) {
        log();
        Animator animator = buildAnimator(isRevert);
        if (mResetParent) {
            reset();
        }
        if (mResetInternal) {
            resetInternal();
        }
        return animator;
    }

    protected abstract Animation buildAnimation(boolean isRevert);

    protected abstract Animator buildAnimator(boolean isRevert);

    protected int key() {
        return String.valueOf(this.getClass()).hashCode();
    }


}

