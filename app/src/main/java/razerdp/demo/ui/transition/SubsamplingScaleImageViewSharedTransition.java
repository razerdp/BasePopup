package razerdp.demo.ui.transition;

import android.animation.Animator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Point;
import android.graphics.PointF;
import android.os.Build;
import android.transition.Transition;
import android.transition.TransitionValues;
import android.util.AttributeSet;
import android.view.ViewGroup;

import androidx.annotation.RequiresApi;

import com.davemorrissey.labs.subscaleview.ImageViewState;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;

import razerdp.basepopup.R;

/**
 * Created by 大灯泡 on 2019/8/26
 * <p>
 * Description：大图浏览transition
 */
@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class SubsamplingScaleImageViewSharedTransition extends Transition {
    private static final String PROPNAME_STATE = "com.example:transition:state";
    private static final String PROPNAME_SIZE = "com.example:transition:size";
    private static final String PROPNAME_CENTER_X = "com.example:transition:center_x";
    private static final String PROPNAME_CENTER_Y = "com.example:transition:center_y";

    private static final int FIT_CENTER = 0;
    private static final int CENTER_CROP = 1;

    private int subsamplingScaleType;
    private int imageViewScaleType;
    private int direction;

    /**
     * For manual creation through code. Initializes transition with all default values and no specified targets.
     */
    public SubsamplingScaleImageViewSharedTransition() {
        imageViewScaleType = 0;
        direction = 0;
        subsamplingScaleType = 0;
    }

    /**
     * Created from XML, targets {@link SubsamplingScaleImageView} class.
     */
    public SubsamplingScaleImageViewSharedTransition(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SubsamplingScaleImageViewSharedTransition);
        direction = a.getInt(R.styleable.SubsamplingScaleImageViewSharedTransition_transitionDirection, 0);
        subsamplingScaleType = a.getInt(R.styleable.SubsamplingScaleImageViewSharedTransition_subsamplingImageViewScaleType, FIT_CENTER);
        imageViewScaleType = a.getInt(R.styleable.SubsamplingScaleImageViewSharedTransition_imageViewScaleType, FIT_CENTER);
        a.recycle();
        addTarget(SubsamplingScaleImageView.class);
    }

    @Override
    public void captureStartValues(TransitionValues transitionValues) {
        captureValues(transitionValues);
    }

    @Override
    public void captureEndValues(TransitionValues transitionValues) {
        captureValues(transitionValues);
    }

    private void captureValues(TransitionValues transitionValues) {
        if (transitionValues.view instanceof SubsamplingScaleImageView) {
            Point size = new Point(transitionValues.view.getWidth(), transitionValues.view.getHeight());
            transitionValues.values.put(PROPNAME_SIZE, size);
            SubsamplingScaleImageView ssiv = (SubsamplingScaleImageView) transitionValues.view;
            transitionValues.values.put(PROPNAME_STATE, ssiv.getState());
        }
    }

    @Override
    public Animator createAnimator(ViewGroup sceneRoot, TransitionValues startValues, TransitionValues endValues) {
        if (startValues == null || endValues == null) {
            return null;
        }

        ImageViewState subsamplingState = (ImageViewState) startValues.values.get(PROPNAME_STATE);
        Point startSize = (Point) startValues.values.get(PROPNAME_SIZE);
        Point endSize = (Point) endValues.values.get(PROPNAME_SIZE);
        if (startSize == null || endSize == null || subsamplingState == null || startSize.equals(endSize)) {
            return null;//missing some values, don't animate
        }
        final SubsamplingScaleImageView view = (SubsamplingScaleImageView) startValues.view;
        Point imgSize = new Point(view.getSWidth(), view.getSHeight());
        if (imgSize.x == 0 || imgSize.y == 0) {
            return null; //no image size, skip animation.
        }

        //resolve transition direction (enter or leaving), assumes enter if view gets larger
        boolean isEntering = direction == 0 ? (startSize.x < endSize.x || startSize.y < endSize.y) : direction == 1;

        final PointF centerFrom;
        float scaleFrom;
        float scaleTo;
        ValueAnimator valueAnimator;
        PointF centerTo = new PointF(imgSize.x / 2, imgSize.y / 2);

        if (isEntering) {
            centerFrom = new PointF(imgSize.x / 2, imgSize.y / 2);
            scaleFrom = getMinIfTrue(startSize.x / (float) imgSize.x, startSize.y / (float) imgSize.y,
                    imageViewScaleType == FIT_CENTER);
            scaleTo = getMinIfTrue(imgSize.x / (float) endSize.x, imgSize.y / (float) endSize.y,
                    subsamplingScaleType == FIT_CENTER);
        } else {
            centerFrom = subsamplingState.getCenter();
            scaleFrom = subsamplingState.getScale();
            scaleTo = getMinIfTrue(endSize.x / (float) imgSize.x, endSize.y / (float) imgSize.y,
                    imageViewScaleType == FIT_CENTER);

        }

        PropertyValuesHolder prop_scale = PropertyValuesHolder.ofFloat(PROPNAME_SIZE, scaleFrom, scaleTo);
        PropertyValuesHolder prop_center_x = PropertyValuesHolder.ofFloat(PROPNAME_CENTER_X, centerFrom.x, centerTo.x);
        PropertyValuesHolder prop_center_y = PropertyValuesHolder.ofFloat(PROPNAME_CENTER_Y, centerFrom.y, centerTo.y);

        valueAnimator = ValueAnimator.ofPropertyValuesHolder(prop_scale, prop_center_x, prop_center_y);

        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                PointF newCenter = new PointF((float) animation.getAnimatedValue(PROPNAME_CENTER_X), (float) animation
                        .getAnimatedValue(PROPNAME_CENTER_Y));
                view.setScaleAndCenter((float) animation.getAnimatedValue(PROPNAME_SIZE), newCenter);
            }
        });
        return valueAnimator;
    }

    /**
     * Does {@link Math#min(float, float)} or {@link Math#max(float, float)} depending on boolean.
     *
     * @param val1 value to compare
     * @param val2 value to compare
     * @param con  condition to check
     * @return If <code>con</code> return minimum of 2 values, otherwise return max.
     */
    private float getMinIfTrue(float val1, float val2, boolean con) {
        return con ? Math.min(val1, val2) : Math.max(val1, val2);
    }

    public SubsamplingScaleImageViewSharedTransition setSubsamplingScaleType(int subsamplingScaleType) {
        this.subsamplingScaleType = subsamplingScaleType;
        return this;
    }

    public SubsamplingScaleImageViewSharedTransition setImageViewScaleType(int imageViewScaleType) {
        this.imageViewScaleType = imageViewScaleType;
        return this;
    }

    public SubsamplingScaleImageViewSharedTransition setDirection(int direction) {
        this.direction = direction;
        return this;
    }
}