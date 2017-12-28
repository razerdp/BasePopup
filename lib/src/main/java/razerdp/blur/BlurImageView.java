package razerdp.blur;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;

import java.lang.ref.WeakReference;

import razerdp.util.log.LogTag;
import razerdp.util.log.LogUtil;

/**
 * Created by 大灯泡 on 2017/12/27.
 * <p>
 * 模糊imageview
 */
public class BlurImageView extends ImageView {
    private static final String TAG = "BlurImageView";

    private volatile boolean abortBlur = false;
    private WeakReference<PopupBlurOption> mBlurOption;
    private Canvas mBlurCanvas;


    public BlurImageView(Context context) {
        this(context, null);
    }

    public BlurImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BlurImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setFocusable(false);
        setFocusableInTouchMode(false);
        setScaleType(ScaleType.FIT_XY);
        setAlpha(0f);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            setBackground(null);
        } else {
            setBackgroundDrawable(null);
        }
    }

    public void attachBlurOption(PopupBlurOption option) {
        try {

            mBlurOption = new WeakReference<PopupBlurOption>(option);
            View anchorView = option.getBlurView();
            if (anchorView == null) {
                LogUtil.trace(LogTag.e, TAG, "模糊锚点View为空，放弃模糊操作...");
                return;
            }
            if (!BlurHelper.renderScriptSupported()) {
                LogUtil.trace(LogTag.e, TAG, "不支持脚本模糊。。。最低支持api 17(Android 4.2.2)，将采用fastblur");
            }
            final Bitmap blurBitmap = BlurHelper.blur(getContext(), anchorView, option.getBlurPreScaleRatio(), option.getBlurRadius());
            if (blurBitmap != null) {
                LogUtil.trace("blurBitmap不为空");
                setImageBitmap(blurBitmap);
            } else {
                LogUtil.trace("blurBitmap为空");
            }
        } catch (Exception e) {
            LogUtil.trace(LogTag.e, TAG, "模糊异常");
            e.printStackTrace();
        }
//        startBlurTask(anchorView);
    }

    PopupBlurOption getOption() {
        if (mBlurOption == null) return null;
        return mBlurOption.get();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        abortBlur = true;
    }

    public void start(long duration) {
        LogUtil.trace(LogTag.i, TAG, "开始模糊imageview alpha动画");
        if (duration > 0) {
            animate()
                    .alpha(1f)
                    .setDuration(duration)
                    .setInterpolator(new DecelerateInterpolator())
                    .start();
        } else if (duration == -1) {
            animate()
                    .alpha(1f)
                    .setDuration(getOption() == null ? 300 : getOption().getBlurInDuration())
                    .setInterpolator(new DecelerateInterpolator())
                    .start();
        } else {
            setAlpha(1f);
        }
    }

    public void dismiss(long duration) {
        LogUtil.trace(LogTag.i, TAG, "dismiss模糊imageview alpha动画");
        if (duration > 0) {
            animate()
                    .alpha(0f)
                    .setDuration(duration)
                    .setInterpolator(new DecelerateInterpolator())
                    .start();
        } else if (duration == -1) {
            animate()
                    .alpha(0f)
                    .setDuration(getOption() == null ? 300 : getOption().getBlurOutDuration())
                    .setInterpolator(new DecelerateInterpolator())
                    .start();
        } else {
            setAlpha(0f);
        }
    }
}
