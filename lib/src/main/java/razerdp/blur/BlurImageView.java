package razerdp.blur;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.os.Build;
import android.os.Looper;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;

import java.lang.ref.WeakReference;
import java.util.concurrent.atomic.AtomicBoolean;

import razerdp.blur.thread.ThreadPoolManager;
import razerdp.util.log.LogTag;
import razerdp.util.log.PopupLogUtil;

/**
 * Created by 大灯泡 on 2017/12/27.
 * <p>
 * 模糊imageview
 */
public class BlurImageView extends ImageView {
    private static final String TAG = "BlurImageView";

    private volatile boolean abortBlur = false;
    private WeakReference<PopupBlurOption> mBlurOption;
    private AtomicBoolean blurFinish = new AtomicBoolean(false);
    private volatile boolean isAnimating = false;
    private long startDuration;
    private CacheAction mCacheAction;


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
        setScaleType(ScaleType.MATRIX);
        setAlpha(0f);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            setBackground(null);
        } else {
            setBackgroundDrawable(null);
        }
    }

    public void applyBlurOption(PopupBlurOption option) {
        applyBlurOption(option, false);
    }

    private void applyBlurOption(PopupBlurOption option, boolean isOnUpdate) {
        if (option == null) return;
        mBlurOption = new WeakReference<PopupBlurOption>(option);
        View anchorView = option.getBlurView();
        if (anchorView == null) {
            PopupLogUtil.trace(LogTag.e, TAG, "模糊锚点View为空，放弃模糊操作...");
            destroy();
            return;
        }
        //因为考虑到实时更新位置（包括模糊也要实时）的原因，因此强制更新时模糊操作在主线程完成。
        if (option.isBlurAsync() && !isOnUpdate) {
            PopupLogUtil.trace(LogTag.i, TAG, "子线程blur");
            startBlurTask(anchorView);
        } else {
            try {
                PopupLogUtil.trace(LogTag.i, TAG, "主线程blur");
                if (!BlurHelper.renderScriptSupported()) {
                    PopupLogUtil.trace(LogTag.e, TAG, "不支持脚本模糊。。。最低支持api 17(Android 4.2.2)，将采用fastblur");
                }
                setImageBitmapOnUiThread(BlurHelper.blur(getContext(), anchorView, option.getBlurPreScaleRatio(), option.getBlurRadius(), option.isFullScreen()), isOnUpdate);
            } catch (Exception e) {
                PopupLogUtil.trace(LogTag.e, TAG, "模糊异常");
                e.printStackTrace();
                destroy();
            }
        }
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

    public void update() {
        if (getOption() != null) {
            applyBlurOption(getOption(), true);
        }
    }

    /**
     * alpha进场动画
     *
     * @param duration
     */
    public void start(long duration) {
        startDuration = duration;
        if (!blurFinish.get()) {
            if (mCacheAction == null) {
                mCacheAction = new CacheAction(new Runnable() {
                    @Override
                    public void run() {
                        start(startDuration);
                    }
                }, 0);
                PopupLogUtil.trace(LogTag.e, TAG, "缓存模糊动画，等待模糊完成");
            }
            return;
        }
        //干掉缓存的runnable
        if (mCacheAction != null) {
            mCacheAction.destroy();
            mCacheAction = null;
        }
        if (isAnimating) return;
        PopupLogUtil.trace(LogTag.i, TAG, "开始模糊alpha动画");
        isAnimating = true;
        if (duration > 0) {
            animate()
                    .alpha(1f)
                    .setDuration(duration)
                    .setInterpolator(new DecelerateInterpolator())
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            isAnimating = false;
                            animate().setListener(null);
                        }
                    })
                    .start();
        } else if (duration == -2) {
            animate()
                    .alpha(1f)
                    .setDuration(getOption() == null ? 500 : getOption().getBlurInDuration())
                    .setInterpolator(new DecelerateInterpolator())
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            isAnimating = false;
                            animate().setListener(null);
                        }
                    })
                    .start();
        } else {
            setAlpha(1f);
        }
    }

    /**
     * alpha退场动画
     */
    public void dismiss(long duration) {
        isAnimating = false;
        PopupLogUtil.trace(LogTag.i, TAG, "dismiss模糊imageview alpha动画");
        if (duration > 0) {
            animate()
                    .alpha(0f)
                    .setDuration(duration)
                    .setInterpolator(new DecelerateInterpolator())
                    .start();
        } else if (duration == -2) {
            animate()
                    .alpha(0f)
                    .setDuration(getOption() == null ? 500 : getOption().getBlurOutDuration())
                    .setInterpolator(new DecelerateInterpolator())
                    .start();
        } else {
            setAlpha(0f);
        }
    }

    /**
     * 子线程模糊
     *
     * @param anchorView
     */
    private void startBlurTask(View anchorView) {
        ThreadPoolManager.execute(new CreateBlurBitmapRunnable(BlurHelper.getViewBitmap(anchorView, getOption().isFullScreen())));
    }

    /**
     * 判断是否处于主线程，并进行设置bitmap
     *
     * @param blurBitmap
     */
    private void setImageBitmapOnUiThread(final Bitmap blurBitmap, final boolean isOnUpdate) {
        if (isUiThread()) {
            handleSetImageBitmap(blurBitmap, isOnUpdate);
        } else {
            post(new Runnable() {
                @Override
                public void run() {
                    handleSetImageBitmap(blurBitmap, isOnUpdate);
                }
            });
        }
    }

    /**
     * 设置bitmap，并进行后续处理（此方法必定运行在主线程）
     *
     * @param bitmap
     */
    private void handleSetImageBitmap(Bitmap bitmap, boolean isOnUpdate) {
        if (bitmap != null) {
            PopupLogUtil.trace(LogTag.i, "bitmap: 【" + bitmap.getWidth() + "," + bitmap.getHeight() + "】");
        }
        setAlpha(isOnUpdate ? 1f : 0f);
        setImageBitmap(bitmap);
        PopupBlurOption option = getOption();
        if (option != null && !option.isFullScreen()) {
            //非全屏的话，则需要将bitmap变化到对应位置
            View anchorView = option.getBlurView();
            if (anchorView == null) return;
            Rect rect = new Rect();
            anchorView.getGlobalVisibleRect(rect);
            Matrix matrix = getImageMatrix();
            matrix.setTranslate(rect.left, rect.top);
            setImageMatrix(matrix);
        }
        blurFinish.compareAndSet(false, true);
        PopupLogUtil.trace(LogTag.i, TAG, "设置成功：" + blurFinish.get());
        if (mCacheAction != null) {
            PopupLogUtil.trace(LogTag.i, TAG, "恢复缓存动画");
            mCacheAction.restore();
        }
    }

    private boolean isUiThread() {
        return Thread.currentThread() == Looper.getMainLooper().getThread();
    }

    public void destroy() {
        setImageBitmap(null);
        abortBlur = true;
        if (mBlurOption != null) {
            mBlurOption.clear();
            mBlurOption = null;
        }
        if (mCacheAction != null) {
            mCacheAction.destroy();
            mCacheAction = null;
        }
        blurFinish.set(false);
        isAnimating = false;
        startDuration = 0;
    }

    class CreateBlurBitmapRunnable implements Runnable {

        private Bitmap bitmap;

        CreateBlurBitmapRunnable(Bitmap bitmap) {
            this.bitmap = bitmap;
        }

        @Override
        public void run() {
            if (abortBlur || getOption() == null) {
                PopupLogUtil.trace(LogTag.e, TAG, "放弃模糊，可能是已经移除了布局");
                return;
            }
            PopupLogUtil.trace(LogTag.i, TAG, "子线程模糊执行");
            setImageBitmapOnUiThread(BlurHelper.blur(getContext(), bitmap, getOption().getBlurPreScaleRatio(), getOption().getBlurRadius()), false);
        }
    }

    class CacheAction {
        private static final long BLUR_TASK_WAIT_TIMEOUT = 1000;//图片模糊超时1秒
        Runnable action;
        long delay;
        final long startTime;

        public CacheAction(Runnable action, long delay) {
            this.action = action;
            this.delay = delay;
            this.startTime = System.currentTimeMillis();
        }

        public void restore() {
            if (isOverTime()) {
                PopupLogUtil.trace(LogTag.e, TAG, "模糊超时");
                destroy();
                return;
            }
            if (action != null) {
                post(action);
            }
        }

        boolean isOverTime() {
            return System.currentTimeMillis() - startTime > BLUR_TASK_WAIT_TIMEOUT;
        }

        public void destroy() {
            if (action != null) {
                removeCallbacks(action);
            }
            action = null;
            delay = 0;

        }


        public boolean matches(Runnable otherAction) {
            return otherAction == null && action == null
                    || action != null && action.equals(otherAction);
        }
    }
}
