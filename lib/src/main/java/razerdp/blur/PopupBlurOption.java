package razerdp.blur;

import android.view.View;

import java.lang.ref.WeakReference;

/**
 * Created by 大灯泡 on 2017/12/27.
 * <p>
 * 模糊配置
 */
public class PopupBlurOption {


    private static final float DEFAULT_BLUR_RADIUS = 10;
    private static final float DEFAULT_PRE_SCALE_RATIO = 0.4f;
    private static final long DEFAULT_ANIMATION_DURATION = 300;

    private WeakReference<View> mBlurView;
    private float mBlurRadius = DEFAULT_BLUR_RADIUS;
    private float mBlurPreScaleRatio = DEFAULT_PRE_SCALE_RATIO;
    private long mDuration = DEFAULT_ANIMATION_DURATION;


    public PopupBlurOption() {

    }

    public View getBlurView() {
        if (mBlurView == null) return null;
        return mBlurView.get();
    }

    public PopupBlurOption setBlurView(View blurView) {
        mBlurView = new WeakReference<View>(blurView);
        return this;
    }

    public float getBlurRadius() {
        return mBlurRadius;
    }

    public PopupBlurOption setBlurRadius(float blurRadius) {
        mBlurRadius = blurRadius;
        return this;
    }

    public float getBlurPreScaleRatio() {
        return mBlurPreScaleRatio;
    }

    public PopupBlurOption setBlurPreScaleRatio(float blurPreScaleRatio) {
        mBlurPreScaleRatio = blurPreScaleRatio;
        return this;
    }

    public long getDuration() {
        return mDuration;
    }

    public PopupBlurOption setDuration(long duration) {
        mDuration = duration;
        return this;
    }

    public boolean isAllowToBlur() {
        return getBlurView() != null && BlurHelper.supportedBlur();
    }
}
