package razerdp.basepopup;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.view.Gravity;
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
 * 旨在用来拦截keyevent、以及蒙层
 */
final class PopupDecorViewProxy extends ViewGroup {
    private static final String TAG = "HackPopupDecorView";
    private PopupTouchController mPopupTouchController;
    //模糊层
    private PopupMaskLayout mMaskLayout;
    private BasePopupHelper mHelper;
    private View mTarget;

    private PopupDecorViewProxy(Context context) {
        this(context, null);
    }

    private PopupDecorViewProxy(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    private PopupDecorViewProxy(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public static PopupDecorViewProxy create(Context context, BasePopupHelper helper) {
        PopupDecorViewProxy result = new PopupDecorViewProxy(context);
        result.init(helper);
        return result;
    }

    private void init(BasePopupHelper helper) {
        mHelper = helper;
        setClipChildren(false);
        setClipToPadding(false);
        setLayoutParams(new ViewGroup.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        mMaskLayout = PopupMaskLayout.create(getContext(), mHelper);
        addViewInLayout(mMaskLayout, -1, new ViewGroup.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        mMaskLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mHelper.onOutSideTouch();
            }
        });
    }

    public void addPopupDecorView(View target) {
        if (target == null) {
            throw new NullPointerException("contentView不能为空");
        }
        if (getChildCount() == 2) {
            removeViewsInLayout(1, 1);
        }

        final ViewGroup.LayoutParams layoutParams = target.getLayoutParams();
        final int height;
        final int width;
        if (layoutParams != null) {
            width = layoutParams.width;
            height = layoutParams.height;
        } else {
            width = mHelper.getPopupViewWidth();
            height = mHelper.getPopupViewHeight();
        }
        addView(target, width, height);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            //蒙层给最大值
            if (child == mMaskLayout) {
                measureChild(child, MeasureSpec.makeMeasureSpec(getScreenWidth(), MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(getScreenHeight(), MeasureSpec.EXACTLY));
            } else {
                measureChild(child, widthMeasureSpec, heightMeasureSpec);
            }
        }
        setMeasuredDimension(getScreenWidth(), getScreenHeight());
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (!changed) {
            return;
        }
        final int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            if (child.getVisibility() == GONE) continue;
            int width = child.getMeasuredWidth();
            int height = child.getMeasuredHeight();

            int gravity = mHelper.getPopupGravity();

            int childLeft = child.getLeft();
            int childTop = child.getTop();

            int offsetX = mHelper.getOffsetX();
            int offsetY = mHelper.getOffsetY();

            boolean delayLayoutMask = mHelper.isAlignBackground();

            if (child == mMaskLayout) {
                if (!delayLayoutMask) {
                    child.layout(childLeft, childTop, childLeft + width, childTop + height);
                }
                continue;
            } else {
                boolean isRelativeToAnchor = mHelper.isShowAsDropDown();
                int anchorCenterX = 0;
                int anchorCenterY = 0;
                int targetCenterX = childLeft + (width >> 1);
                int targetCenterY = childTop + (height >> 1);
                if (isRelativeToAnchor) {
                    anchorCenterX = mHelper.getAnchorX() + (mHelper.getAnchorViewWidth() >> 1);
                    anchorCenterY = mHelper.getAnchorY() + (mHelper.getAnchorHeight() >> 1);
                }
                //不跟anchorView联系的情况下，gravity意味着在整个view中的方位
                //如果跟anchorView联系，gravity意味着以anchorView为中心的方位
                switch (gravity & Gravity.HORIZONTAL_GRAVITY_MASK) {
                    case Gravity.LEFT:
                    case Gravity.START:
                        if (isRelativeToAnchor) {
                            offsetX += mHelper.getAnchorX() - width;
                        }
                        break;
                    case Gravity.RIGHT:
                    case Gravity.END:
                        if (isRelativeToAnchor) {
                            offsetX += mHelper.getAnchorX() + mHelper.getAnchorViewWidth();
                        }
                        break;
                    case Gravity.CENTER_HORIZONTAL:
                        if (isRelativeToAnchor) {
                            offsetX += anchorCenterX - targetCenterX;
                        } else {
                            childLeft = (r - l - width) >> 1;
                            targetCenterX = childLeft + (width >> 1);
                        }
                        break;
                    default:
                        break;
                }

                switch (gravity & Gravity.VERTICAL_GRAVITY_MASK) {
                    case Gravity.TOP:
                        if (isRelativeToAnchor) {
                            offsetY += mHelper.getAnchorY() - height;
                        }
                        break;
                    case Gravity.BOTTOM:
                        if (isRelativeToAnchor) {
                            offsetY += mHelper.getAnchorY() + mHelper.getAnchorHeight();
                        } else {
                            childTop = b - t - height;
                            targetCenterY = childTop + (height >> 1);
                        }
                        break;
                    case Gravity.CENTER_VERTICAL:
                        if (isRelativeToAnchor) {
                            offsetY += anchorCenterY - targetCenterY;
                        } else {
                            childTop = (b - t - height) >> 1;
                            targetCenterY = childTop + (height >> 1);
                        }
                        break;
                    default:
                        break;
                }
            }
            if (delayLayoutMask) {
                mMaskLayout.layout(0, childTop + offsetY, width, childTop + offsetY + height);
            }
            child.layout(childLeft + offsetX, childTop + offsetY, childLeft + offsetX + width, childTop + offsetY + height);
        }
    }


    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (mMaskLayout != null) {
            mMaskLayout.handleStart(-2);
        }
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

    int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }
}
