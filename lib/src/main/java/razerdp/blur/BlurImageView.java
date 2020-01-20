package razerdp.blur;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.os.Build;
import android.os.Looper;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;

import java.util.concurrent.atomic.AtomicBoolean;

import razerdp.blur.thread.ThreadPoolManager;
import razerdp.util.log.PopupLog;

/**
 * Created by 大灯泡 on 2017/12/27.
 * <p>
 * 模糊imageview
 */
public class BlurImageView extends ImageView {
    private static final String TAG = "BlurImageView";

    private volatile boolean abortBlur = false;
    private PopupBlurOption mBlurOption;
    private AtomicBoolean blurFinish = new AtomicBoolean(false);
    private volatile boolean isAnimating = false;
    private long startDuration;
    private CacheAction mCacheAction;
    private CacheAction mAttachedCache;
    private boolean isAttachedToWindow = false;


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
        mBlurOption = option;
        View anchorView = option.getBlurView();
        if (anchorView == null) {
            PopupLog.e(TAG, "模糊锚点View为空，放弃模糊操作...");
            destroy();
            return;
        }
        //因为考虑到实时更新位置（包括模糊也要实时）的原因，因此强制更新时模糊操作在主线程完成。
        if (option.isBlurAsync() && !isOnUpdate) {
            PopupLog.i(TAG, "子线程blur");
            startBlurTask(anchorView);
        } else {
            try {
                PopupLog.i(TAG, "主线程blur");
                if (!BlurHelper.renderScriptSupported()) {
                    PopupLog.e(TAG, "不支持脚本模糊。。。最低支持api 17(Android 4.2.2)，将采用fastblur");
                }
                setImageBitmapOnUiThread(BlurHelper.blur(getContext(), anchorView, option.getBlurPreScaleRatio(), option.getBlurRadius(), option.isFullScreen()), isOnUpdate);
            } catch (Exception e) {
                PopupLog.e(TAG, "模糊异常", e);
                e.printStackTrace();
                destroy();
            }
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        abortBlur = true;
    }

    public void update() {
        if (mBlurOption != null) {
            applyBlurOption(mBlurOption, true);
        }
    }

    /**
     * alpha进场动画
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
                PopupLog.e(TAG, "缓存模糊动画，等待模糊完成");
            }
            return;
        }
        //干掉缓存的runnable
        if (mCacheAction != null) {
            mCacheAction.destroy();
            mCacheAction = null;
        }
        if (isAnimating) return;
        PopupLog.i(TAG, "开始模糊alpha动画");
        isAnimating = true;
        if (duration > 0) {
            startAlphaInAnimation(duration);
        } else if (duration == -2) {
            startAlphaInAnimation(mBlurOption == null ? 500 : mBlurOption.getBlurInDuration());
        } else {
            setImageAlpha(255);
        }
    }

    private void startAlphaInAnimation(long duration) {
        ValueAnimator valueAnimator = ValueAnimator.ofInt(0, 255);
        valueAnimator.setDuration(duration);
        valueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                isAnimating = false;
            }
        });
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                setImageAlpha((Integer) animation.getAnimatedValue());
            }
        });
        valueAnimator.start();
    }

    /**
     * alpha退场动画
     */
    public void dismiss(long duration) {
        isAnimating = false;
        PopupLog.i(TAG, "dismiss模糊imageview alpha动画");
        if (duration > 0) {
            startAlphaOutAnimation(duration);
        } else if (duration == -2) {
            startAlphaOutAnimation(mBlurOption == null ? 500 : mBlurOption.getBlurOutDuration());
        } else {
            setImageAlpha(0);
        }
    }

    private void startAlphaOutAnimation(long duration) {
        ValueAnimator valueAnimator = ValueAnimator.ofInt(255, 0);
        valueAnimator.setDuration(duration);
        valueAnimator.setInterpolator(new AccelerateInterpolator());
        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                isAnimating = false;
            }
        });
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                setImageAlpha((Integer) animation.getAnimatedValue());
            }
        });
        valueAnimator.start();
    }

    /**
     * 子线程模糊
     *
     * @param anchorView
     */
    private void startBlurTask(View anchorView) {
        ThreadPoolManager.execute(new CreateBlurBitmapRunnable(anchorView));
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
            if (!isAttachedToWindow) {
                mAttachedCache = new CacheAction(new Runnable() {
                    @Override
                    public void run() {
                        handleSetImageBitmap(blurBitmap, isOnUpdate);
                    }
                }, 0);
            } else {
                post(new Runnable() {
                    @Override
                    public void run() {
                        handleSetImageBitmap(blurBitmap, isOnUpdate);
                    }
                });
            }
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        isAttachedToWindow = true;
        if (mAttachedCache != null) {
            mAttachedCache.forceRestore();
        }
    }

    /**
     * 设置bitmap，并进行后续处理（此方法必定运行在主线程）
     *
     * @param bitmap
     */
    private void handleSetImageBitmap(Bitmap bitmap, boolean isOnUpdate) {
        if (bitmap != null) {
            PopupLog.i("bitmap: 【" + bitmap.getWidth() + "," + bitmap.getHeight() + "】");
        }

        setImageAlpha(isOnUpdate ? 255 : 0);
        setImageBitmap(bitmap);
        PopupBlurOption option = mBlurOption;
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
        PopupLog.i(TAG, "设置成功：" + blurFinish.get());
        if (mCacheAction != null) {
            PopupLog.i(TAG, "恢复缓存动画");
            mCacheAction.restore();
        }
        if (mAttachedCache != null) {
            mAttachedCache.destroy();
            mAttachedCache = null;
        }
    }

    private boolean isUiThread() {
        return Thread.currentThread() == Looper.getMainLooper().getThread();
    }

    public void destroy() {
        setImageBitmap(null);
        abortBlur = true;
        if (mBlurOption != null) {
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

        private int outWidth;
        private int outHeight;
        private Bitmap mBitmap;

        CreateBlurBitmapRunnable(View target) {
            outWidth = target.getWidth();
            outHeight = target.getHeight();
            mBitmap = BlurHelper.getViewBitmap(target, mBlurOption.getBlurPreScaleRatio(), mBlurOption.isFullScreen());
        }

        @Override
        public void run() {
            if (abortBlur || mBlurOption == null) {
                PopupLog.e(TAG, "放弃模糊，可能是已经移除了布局");
                return;
            }
            PopupLog.i(TAG, "子线程模糊执行");
            setImageBitmapOnUiThread(BlurHelper.blur(getContext(),
                    mBitmap,
                    outWidth,
                    outHeight,
                    mBlurOption.getBlurRadius()),
                    false);
        }
    }

    class CacheAction {
        private static final long BLUR_TASK_WAIT_TIMEOUT = 1000;//图片模糊超时1秒
        Runnable action;
        long delay;
        final long startTime;

        CacheAction(Runnable action, long delay) {
            this.action = action;
            this.delay = delay;
            this.startTime = System.currentTimeMillis();
        }

        void restore() {
            if (isOverTime()) {
                PopupLog.e(TAG, "模糊超时");
                destroy();
                return;
            }
            if (action != null) {
                post(action);
            }
        }

        void forceRestore() {
            if (action != null) {
                post(action);
            }
        }

        boolean isOverTime() {
            return System.currentTimeMillis() - startTime > BLUR_TASK_WAIT_TIMEOUT;
        }

        void destroy() {
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
