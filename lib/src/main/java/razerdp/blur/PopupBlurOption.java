package razerdp.blur;

import android.text.TextUtils;
import android.view.View;
import android.view.ViewParent;

import java.lang.ref.WeakReference;

/**
 * Created by 大灯泡 on 2017/12/27.
 * <p>
 * 模糊配置
 */
public class PopupBlurOption {


    private static final float DEFAULT_BLUR_RADIUS = 10;
    private static final float DEFAULT_PRE_SCALE_RATIO = 0.15f;
    private static final long DEFAULT_ANIMATION_DURATION = 500;
    private static final boolean DEFAULT_BLUR_ASYNC = true;//默认子线程blur

    private WeakReference<View> mBlurView;
    private float mBlurRadius = DEFAULT_BLUR_RADIUS;
    private float mBlurPreScaleRatio = DEFAULT_PRE_SCALE_RATIO;
    private long mBlurInDuration = DEFAULT_ANIMATION_DURATION;
    private long mBlurOutDuration = DEFAULT_ANIMATION_DURATION;
    private boolean mBlurAsync = DEFAULT_BLUR_ASYNC;
    private boolean mFullScreen = true;


    public PopupBlurOption() {

    }

    public View getBlurView() {
        if (mBlurView == null) return null;
        return mBlurView.get();
    }

    public PopupBlurOption setBlurView(View blurView) {
        mBlurView = new WeakReference<View>(blurView);
        boolean isDecorView = false;
        if (blurView != null) {
            ViewParent parent = blurView.getParent();
            if (parent != null) {
                isDecorView = TextUtils.equals(parent.getClass().getName(),
                                               "com.android.internal.policy.DecorView");
            }
            if (!isDecorView) {
                isDecorView = blurView.getId() == android.R.id.content;
            }
            if (!isDecorView) {
                isDecorView = TextUtils.equals(blurView.getClass().getName(),
                                               "com.android.internal.policy.DecorView");
            }
        }
        setFullScreen(isDecorView);
        return this;
    }

    public float getBlurRadius() {
        return mBlurRadius;
    }

    public PopupBlurOption setBlurRadius(float blurRadius) {
        if (blurRadius <= 0) {
            blurRadius = 0.1f;
        } else if (blurRadius > 25) {
            blurRadius = 25;
        }
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
        return mBlurInDuration < 0 ? DEFAULT_ANIMATION_DURATION : mBlurInDuration;
    }

    public PopupBlurOption setBlurInDuration(long blurInDuration) {
        mBlurInDuration = blurInDuration;
        return this;
    }

    public long getBlurOutDuration() {
        return mBlurOutDuration < 0 ? DEFAULT_ANIMATION_DURATION : mBlurOutDuration;
    }

    public PopupBlurOption setBlurOutDuration(long blurOutDuration) {
        mBlurOutDuration = blurOutDuration;
        return this;
    }

    public boolean isBlurAsync() {
        return mBlurAsync;
    }

    public PopupBlurOption setBlurAsync(boolean blurAsync) {
        mBlurAsync = blurAsync;
        return this;
    }

    public boolean isFullScreen() {
        return mFullScreen;
    }

    public PopupBlurOption setFullScreen(boolean fullScreen) {
        mFullScreen = fullScreen;
        return this;
    }

    public boolean isAllowToBlur() {
        return getBlurView() != null;
    }

    public final void clear() {
        if (mBlurView != null) {
            mBlurView.clear();
        }
        mBlurView = null;
    }
}
