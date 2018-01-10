package razerdp.basepopup;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import java.lang.ref.WeakReference;

import razerdp.blur.BlurImageView;
import razerdp.util.log.LogTag;
import razerdp.util.log.PopupLogUtil;

/**
 * Created by 大灯泡 on 2017/12/25.
 * <p>
 * 旨在用来拦截keyevent
 */
public class HackPopupDecorView extends ViewGroup {
    private static final String TAG = "HackPopupDecorView";
    private PopupController mPopupController;
    private WeakReference<BlurImageView> mBlurImageViewWeakReference;

    public HackPopupDecorView(Context context) {
        super(context);
    }

    public HackPopupDecorView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HackPopupDecorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int childCount = getChildCount();
        if (childCount <= 0) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        } else {
            int maxWidth = 0;
            int maxHeight = 0;

            for (int i = 0; i < childCount; i++) {
                View child = getChildAt(i);
                measureChild(child, widthMeasureSpec, heightMeasureSpec);
                maxWidth = Math.max(maxWidth, child.getMeasuredWidth());
                maxHeight = Math.max(maxHeight, child.getMeasuredHeight());
            }
            setMeasuredDimension(maxWidth, maxHeight);
        }
        PopupLogUtil.trace(LogTag.d, TAG, "onMeasure");
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        final int childCount = getChildCount();
        if (childCount <= 0) {
            return;
        } else {
            for (int i = 0; i < childCount; i++) {
                View child = getChildAt(i);
                child.layout(l, t, r, b);
            }
        }
        PopupLogUtil.trace(LogTag.d, TAG, "onLayout");

    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return false;
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        boolean intercept = getPopupController() != null && getPopupController().onDispatchKeyEvent(event);
        if (intercept) return true;
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            if (getKeyDispatcherState() == null) {
                return super.dispatchKeyEvent(event);
            }

            if (event.getAction() == KeyEvent.ACTION_DOWN && event.getRepeatCount() == 0) {
                final KeyEvent.DispatcherState state = getKeyDispatcherState();
                if (state != null) {
                    state.startTracking(event, this);
                }
                return true;
            } else if (event.getAction() == KeyEvent.ACTION_UP) {
                final KeyEvent.DispatcherState state = getKeyDispatcherState();
                if (state != null && state.isTracking(event) && !event.isCanceled()) {
                    if (getPopupController() != null) {
                        PopupLogUtil.trace(LogTag.i, TAG, "dispatchKeyEvent: >>> onBackPressed");
                        return getPopupController().onBackPressed();
                    }
                }
            }
            return super.dispatchKeyEvent(event);
        } else {
            return super.dispatchKeyEvent(event);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (getPopupController() != null) {
            if (getPopupController().onTouchEvent(event)) {
                return true;
            }
        }
        final int x = (int) event.getX();
        final int y = (int) event.getY();

        if ((event.getAction() == MotionEvent.ACTION_DOWN)
                && ((x < 0) || (x >= getWidth()) || (y < 0) || (y >= getHeight()))) {
            if (getPopupController() != null) {
                PopupLogUtil.trace(LogTag.i, TAG, "onTouchEvent:[ACTION_DOWN] >>> onOutSideTouch");
                return getPopupController().onOutSideTouch();
            }
        } else if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
            if (getPopupController() != null) {
                PopupLogUtil.trace(LogTag.i, TAG, "onTouchEvent:[ACTION_OUTSIDE] >>> onOutSideTouch");
                return getPopupController().onOutSideTouch();
            }
        }
        return super.onTouchEvent(event);
    }

    public PopupController getPopupController() {
        return mPopupController;
    }

    public void setPopupController(PopupController popupController) {
        mPopupController = popupController;
        if (mPopupController instanceof BasePopupWindow) {
            ((BasePopupWindow) mPopupController).setOnInnerPopupWIndowStateListener(new InnerPopupWindowStateListener());
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        startBlurAnima();
    }

    public void addBlurImageview(BlurImageView blurImageView) {
        if (blurImageView == null) return;
        mBlurImageViewWeakReference = new WeakReference<BlurImageView>(blurImageView);
    }

    public void startBlurAnima() {
        startBlurAnima(-2);
    }

    public void startBlurAnima(long duration) {
        if (getBlurImageView() == null) return;
        getBlurImageView().start(duration);
    }

    public void dismissBlurAnima() {
        dismissBlurAnima(-2);
    }

    public void dismissBlurAnima(long duration) {
        if (getBlurImageView() == null) return;
        getBlurImageView().dismiss(duration);
    }

    BlurImageView getBlurImageView() {
        if (mBlurImageViewWeakReference == null) return null;
        return mBlurImageViewWeakReference.get();
    }

    class InnerPopupWindowStateListener extends razerdp.basepopup.InnerPopupWindowStateListener {

        @Override
        void onAnimaDismissStart() {
            dismissBlurAnima();
        }

        @Override
        void onWithAnimaDismiss() {
            dismissBlurAnima(0);
        }
    }
}
