package razerdp.basepopup;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import razerdp.util.log.LogTag;
import razerdp.util.log.PopupLogUtil;

/**
 * Created by 大灯泡 on 2017/12/25.
 * <p>
 * 旨在用来拦截keyevent
 */
public class PopupDecorViewProxy extends ViewGroup {
    private static final String TAG = "HackPopupDecorView";
    private PopupTouchController mPopupTouchController;

    public PopupDecorViewProxy(Context context) {
        super(context);
    }

    public PopupDecorViewProxy(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PopupDecorViewProxy(Context context, AttributeSet attrs, int defStyleAttr) {
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
        if (mPopupTouchController != null) {
            return mPopupTouchController.onInterceptTouchEvent(ev);
        }
        return false;
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        boolean intercept = mPopupTouchController != null && mPopupTouchController.onDispatchKeyEvent(event);
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
                    if (mPopupTouchController != null) {
                        PopupLogUtil.trace(LogTag.i, TAG, "dispatchKeyEvent: >>> onBackPressed");
                        return mPopupTouchController.onBackPressed();
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
        if (mPopupTouchController != null) {
            if (mPopupTouchController.onTouchEvent(event)) {
                return true;
            }
        }
        final int x = (int) event.getX();
        final int y = (int) event.getY();

        if ((event.getAction() == MotionEvent.ACTION_DOWN)
                && ((x < 0) || (x >= getWidth()) || (y < 0) || (y >= getHeight()))) {
            if (mPopupTouchController != null) {
                PopupLogUtil.trace(LogTag.i, TAG, "onTouchEvent:[ACTION_DOWN] >>> onOutSideTouch");
                return mPopupTouchController.onOutSideTouch();
            }
        } else if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
            if (mPopupTouchController != null) {
                PopupLogUtil.trace(LogTag.i, TAG, "onTouchEvent:[ACTION_OUTSIDE] >>> onOutSideTouch");
                return mPopupTouchController.onOutSideTouch();
            }
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (mOnAttachListener != null) {
            mOnAttachListener.onAttachtoWindow();
        }
    }

    ViewGroup.LayoutParams addPopupDecorView(View view, ViewGroup.LayoutParams sourcePopupDecorViewParams, BasePopupHelper helper, PopupTouchController controller) {
        this.mPopupTouchController = controller;
        /**
         * 此时的params是WindowManager.LayoutParams，需要留意强转问题
         * popup内部有scrollChangeListener，会有params强转为WindowManager.LayoutParams的情况
         */
        sourcePopupDecorViewParams = applyHelper(sourcePopupDecorViewParams, helper);
        ViewGroup.LayoutParams decorViewLayoutParams;
        if (sourcePopupDecorViewParams instanceof WindowManager.LayoutParams) {
            WindowManager.LayoutParams wp = new WindowManager.LayoutParams();
            wp.copyFrom((WindowManager.LayoutParams) sourcePopupDecorViewParams);
            decorViewLayoutParams = wp;
        } else {
            // FIXME: 2018/1/23 可能会导致cast exception
            //{#52}https://github.com/razerdp/BasePopup/issues/52
            decorViewLayoutParams = new ViewGroup.LayoutParams(sourcePopupDecorViewParams);
        }

        if (helper != null) {
            decorViewLayoutParams.width = helper.getPopupViewWidth();
            decorViewLayoutParams.height = helper.getPopupViewHeight();
        }
        addView(view, decorViewLayoutParams);

        return sourcePopupDecorViewParams;
    }

    /**
     * 应用helper的设置到popup的params
     *
     * @param params
     * @return
     */
    private ViewGroup.LayoutParams applyHelper(ViewGroup.LayoutParams params, BasePopupHelper helper) {
        if (!(params instanceof WindowManager.LayoutParams) || helper == null) {
            return params;
        }
        WindowManager.LayoutParams p = (WindowManager.LayoutParams) params;
        if (!helper.isInterceptTouchEvent()) {
            PopupLogUtil.trace(LogTag.i, TAG, "applyHelper  >>>  不拦截事件");
            p.flags |= WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;
            p.flags |= WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH;
        }
        if (helper.isFullScreen()) {
            PopupLogUtil.trace(LogTag.i, TAG, "applyHelper  >>>  全屏");
            p.flags |= WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;

            // FIXME: 2017/12/27 全屏跟SOFT_INPUT_ADJUST_RESIZE冲突，暂时没有好的解决方案
            p.softInputMode = WindowManager.LayoutParams.SOFT_INPUT_STATE_UNCHANGED;
        }
        return p;
    }

    private OnAttachListener mOnAttachListener;

    public OnAttachListener getOnAttachListener() {
        return mOnAttachListener;
    }

    public void setOnAttachListener(OnAttachListener onAttachListener) {
        mOnAttachListener = onAttachListener;
    }

    interface OnAttachListener {
        void onAttachtoWindow();
    }
}
