package razerdp.blur;

import android.view.View;

import java.lang.ref.WeakReference;

/**
 * Created by 大灯泡 on 2017/12/27.
 * <p>
 * 模糊配置
 */
public class PopupBlurOption {


    private static final float DEFAULT_BLUR_RADIUS = 6;
    private static final float DEFAULT_PRE_SCALE_RATIO = 0.6f;
    private static final long DEFAULT_ANIMATION_DURATION = 300;

    private WeakReference<View> mBlurView;
    private float mBlurRadius = DEFAULT_BLUR_RADIUS;
    private float mBlurPreScaleRatio = DEFAULT_PRE_SCALE_RATIO;
    private long mBlurInDuration = DEFAULT_ANIMATION_DURATION;
    private long mBlurOutDuration = DEFAULT_ANIMATION_DURATION;


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

    public long getBlurInDuration() {
        return mBlurInDuration;
    }

    public PopupBlurOption setBlurInDuration(long blurInDuration) {
        mBlurInDuration = blurInDuration;
        return this;
    }

    public long getBlurOutDuration() {
        return mBlurOutDuration;
    }

    public PopupBlurOption setBlurOutDuration(long blurOutDuration) {
        mBlurOutDuration = blurOutDuration;
        return this;
    }

    public boolean isAllowToBlur() {
        return getBlurView() != null;
    }
}
