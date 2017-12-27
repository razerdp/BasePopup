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

import razerdp.blur.thread.ThreadPoolManager;
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            setBackground(null);
        } else {
            setBackgroundDrawable(null);
        }
    }

    public void attachBlurOption(PopupBlurOption option) {
        mBlurOption = new WeakReference<PopupBlurOption>(option);
        View anchorView = option.getBlurView();
        if (anchorView == null) {
            LogUtil.trace(LogTag.e, TAG, "模糊锚点View为空，放弃模糊操作...");
            return;
        }
        final Bitmap blurBitmap = BlurHelper.blur(getContext(), anchorView, option.getBlurPreScaleRatio(), option.getBlurRadius());
        if (blurBitmap != null) {
            LogUtil.trace("blurBitmap不为空");
            setImageBitmap(blurBitmap);
        } else {
            LogUtil.trace("blurBitmap为空");
        }
//        startBlurTask(anchorView);
    }

    PopupBlurOption getOption() {
        if (mBlurOption == null) return null;
        return mBlurOption.get();
    }


    private void startBlurTask(View anchorView) {
        if (!BlurHelper.supportedBlur()) {
            LogUtil.trace(LogTag.e, TAG, "不支持渲染，至少需要api 17");
            return;
        }
        ThreadPoolManager.execute(new CreateBlurBitmapRunnable(anchorView));
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        abortBlur = true;
    }

    public void start(long duration) {
        LogUtil.trace(LogTag.i, TAG, "开始模糊imageview alpha动画");
        animate()
                .alpha(1)
                .setDuration(duration)
                .setInterpolator(new DecelerateInterpolator())
                .start();
    }

    public void dismiss(long duration) {
        LogUtil.trace(LogTag.i, TAG, "dismiss模糊imageview alpha动画");
        animate()
                .alpha(0)
                .setDuration(duration)
                .setInterpolator(new DecelerateInterpolator())
                .start();
    }

    class CreateBlurBitmapRunnable implements Runnable {

        private View anchorView;

        public CreateBlurBitmapRunnable(View anchorView) {
            this.anchorView = anchorView;
        }

        @Override
        public void run() {
            if (abortBlur || getOption() == null) {
                LogUtil.trace(LogTag.e, TAG, "放弃模糊，可能是已经移除了布局");
                return;
            }
            PopupBlurOption option = getOption();
            final Bitmap blurBitmap = BlurHelper.blur(getContext(), anchorView, option.getBlurPreScaleRatio(), option.getBlurRadius());
            post(new Runnable() {
                @Override
                public void run() {
                    if (blurBitmap != null) {
                        LogUtil.trace("blurBitmap不为空");
                        setAlpha(0);
                        setImageBitmap(blurBitmap);
                    } else {
                        LogUtil.trace("blurBitmap为空");
                    }
//                    Rect rect=new Rect();
//                    anchorView.getGlobalVisibleRect(rect);
                }
            });
        }
    }
}
