package razerdp.basepopup;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import razerdp.util.KeyboardUtils;
import razerdp.util.PopupUiUtils;
import razerdp.util.PopupUtils;
import razerdp.util.log.PopupLog;

/**
 * Created by 大灯泡 on 2017/12/25.
 * <p>
 * 旨在用来拦截keyevent、以及蒙层
 */
final class PopupDecorViewProxy extends ViewGroup implements KeyboardUtils.OnKeyboardChangeListener, ViewTreeObserver.OnGlobalLayoutListener, ClearMemoryObject {
    private static final String TAG = "PopupDecorViewProxy";
    //蒙层
    private PopupMaskLayout mMaskLayout;
    private BasePopupHelper mHelper;
    private View mTarget;
    private int changedGravity = Gravity.NO_GRAVITY;

    private int childLeftMargin;
    private int childTopMargin;
    private int childRightMargin;
    private int childBottomMargin;

    private WindowManagerProxy mWindowManagerProxy;
    private int[] location = new int[2];
    private int originY;
    private Flag mFlag = new Flag();
    private Rect lastKeyboardBounds = new Rect();
    private boolean isFirstLayoutComplete = false;
    private int layoutCount = 0;

    private PopupDecorViewProxy(Context context) {
        this(context, null);
    }

    private PopupDecorViewProxy(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    private PopupDecorViewProxy(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    PopupDecorViewProxy(Context context, WindowManagerProxy windowManagerProxy, BasePopupHelper helper) {
        this(context);
        getViewTreeObserver().addOnGlobalLayoutListener(this);
        init(windowManagerProxy, helper);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void init(WindowManagerProxy managerProxy, BasePopupHelper helper) {
        mWindowManagerProxy = managerProxy;
        mHelper = helper;
        mHelper.mKeyboardStateChangeListener = this;
        setClipChildren(mHelper.isClipChildren());
        mMaskLayout = new PopupMaskLayout(getContext(), mHelper);
        mFlag.flag = Flag.IDLE;
        if (!mHelper.isOutSideTouchable()) {
            setLayoutParams(new ViewGroup.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
            addViewInLayout(mMaskLayout, -1, new ViewGroup.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        } else {
            setLayoutParams(new ViewGroup.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
            Activity act = PopupUtils.getActivity(getContext());
            if (act == null) return;
            removeDecorMaskLayout(act);
            addMaskToDecor(act.getWindow());
        }
        changedGravity = Gravity.NO_GRAVITY;
    }

    private void removeDecorMaskLayout(Activity act) {
        if (act == null || act.getWindow() == null) {
            return;
        }
        View decorView = act.getWindow().getDecorView();
        if (decorView instanceof ViewGroup) {
            ViewGroup vg = ((ViewGroup) decorView);
            int childCount = vg.getChildCount();
            for (int i = childCount - 1; i >= 0; i--) {
                View child = vg.getChildAt(i);
                if (child instanceof PopupMaskLayout) {
                    vg.removeViewInLayout(child);
                }
            }
        }
    }

    private void addMaskToDecor(Window window) {
        View decorView = window == null ? null : window.getDecorView();
        if (!(decorView instanceof ViewGroup)) {
            if (mMaskLayout != null) {
                mMaskLayout.onDetachedFromWindow();
                mMaskLayout = null;
            }
            return;
        }
        ((ViewGroup) decorView).addView(mMaskLayout, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
    }

    public void wrapPopupDecorView(View target, WindowManager.LayoutParams params) {
        if (target == null) {
            throw new NullPointerException("contentView不能为空");
        }
        if (target.getParent() != null) {
            return;
        }

        //限制一个mask一个target，不允许多个target
        if (getChildCount() == 2) {
            removeViewsInLayout(1, 1);
        }

        mTarget = target;
        WindowManager.LayoutParams wp = new WindowManager.LayoutParams();
        wp.copyFrom(params);
        wp.x = 0;
        wp.y = 0;

        if (mHelper.getParaseFromXmlParams() == null) {
            View contentView = findContentView(target);

            if (contentView != null) {
                if (!mHelper.isCustomMeasure()) {
                    if (wp.width <= 0) {
                        wp.width = contentView.getMeasuredWidth() <= 0 ? mHelper.getPopupViewWidth() : contentView.getMeasuredWidth();
                    }
                    if (wp.height <= 0) {
                        wp.height = contentView.getMeasuredHeight() <= 0 ? mHelper.getPopupViewHeight() : contentView.getMeasuredHeight();
                    }
                } else {
                    wp.width = mHelper.getPopupViewWidth();
                    wp.height = mHelper.getPopupViewHeight();
                }
            }
        } else {
            if (target instanceof ViewGroup) {
                View child = ((ViewGroup) target).getChildAt(0);
                if (child != null) {
                    child.setLayoutParams(new FrameLayout.LayoutParams(mHelper.getParaseFromXmlParams()));
                }
            }

            wp.width = mHelper.getPopupViewWidth();
            wp.height = mHelper.getPopupViewHeight();
            childLeftMargin = mHelper.getParaseFromXmlParams().leftMargin;
            childTopMargin = mHelper.getParaseFromXmlParams().topMargin;
            childRightMargin = mHelper.getParaseFromXmlParams().rightMargin;
            childBottomMargin = mHelper.getParaseFromXmlParams().bottomMargin;
        }

        addView(target, wp);
    }

    /**
     * target或许不是contentview
     */
    private View findContentView(View root) {
        if (root == null) return null;
        if (!(root instanceof ViewGroup)) return root;
        ViewGroup rootGroup = (ViewGroup) root;
        if (rootGroup.getChildCount() <= 0) return root;
        String viewSimpleClassName = rootGroup.getClass().getSimpleName();
        View result = null;
        while (!isContentView(viewSimpleClassName)) {
            result = rootGroup.getChildAt(0);
            viewSimpleClassName = result.getClass().getSimpleName();
            if (result instanceof ViewGroup) {
                rootGroup = ((ViewGroup) result);
            } else {
                break;
            }
        }
        return result;
    }

    private boolean isContentView(String contentClassName) {
        return !TextUtils.equals(contentClassName, "PopupDecorView") &&
                !TextUtils.equals(contentClassName, "PopupViewContainer") &&
                !TextUtils.equals(contentClassName, "PopupBackgroundView");
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mFlag.flag &= ~Flag.FLAG_REST_WIDTH_NOT_ENOUGH;
        mFlag.flag &= ~Flag.FLAG_REST_HEIGHT_NOT_ENOUGH;
        PopupLog.i("onMeasure", MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.getSize(heightMeasureSpec));
        if (!mHelper.isOutSideTouchable()) {
            measureNormal(widthMeasureSpec, heightMeasureSpec);
        } else {
            measureOutSide(widthMeasureSpec, heightMeasureSpec);
        }
    }

    private void measureNormal(int widthMeasureSpec, int heightMeasureSpec) {
        final int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            //蒙层给最大值
            if (child == mMaskLayout) {
                measureChild(child, widthMeasureSpec, heightMeasureSpec);
            } else {
                measureWrappedDecorView(child, widthMeasureSpec, heightMeasureSpec);
            }
        }
        setMeasuredDimension(getAvaliableWidth(), getAvaliableHeight());
    }

    private void measureOutSide(int widthMeasureSpec, int heightMeasureSpec) {
        int maxWidth = 0;
        int maxHeight = 0;
        int childState = 0;

        final int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            if (child == mTarget) {
                measureWrappedDecorView(mTarget, widthMeasureSpec, heightMeasureSpec);
            } else {
                measureChild(child, widthMeasureSpec, heightMeasureSpec);
            }

            maxWidth = Math.max(maxWidth, child.getMeasuredWidth());
            maxHeight = Math.max(maxHeight, child.getMeasuredHeight());
            childState = combineMeasuredStates(childState, child.getMeasuredState());
        }
        setMeasuredDimension(resolveSizeAndState(maxWidth, widthMeasureSpec, childState), resolveSizeAndState(maxHeight, heightMeasureSpec,
                childState << MEASURED_HEIGHT_STATE_SHIFT));
    }


    private void measureWrappedDecorView(View mTarget, int widthMeasureSpec, int heightMeasureSpec) {
        if (mTarget == null || mTarget.getVisibility() == GONE) return;
        final LayoutParams lp = mTarget.getLayoutParams();

        widthMeasureSpec = getChildMeasureSpec(widthMeasureSpec, 0, lp.width);
        heightMeasureSpec = getChildMeasureSpec(heightMeasureSpec, 0, lp.height);


        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        int gravity = mHelper.getPopupGravity();
        boolean isAlignAnchorMode = mHelper.getGravityMode() == BasePopupWindow.GravityMode.ALIGN_TO_ANCHOR_SIDE;

        //针对关联anchorView和对齐模式的测量（如果允许resize）
        if (mHelper.isShowAsDropDown() && mHelper.isResizable()) {
            Rect anchorBound = mHelper.getAnchorViewBound();
            //剩余空间
            //etc.如果是相对于锚点，则剩余宽度为屏幕左侧到anchor左侧
            int rl = anchorBound.left;
            int rt = anchorBound.top;
            int rr = widthSize - anchorBound.right;
            int rb = heightSize - anchorBound.bottom;

            if (isAlignAnchorMode) {
                //如果是对齐到anchor的边，则需要修正
                //左边对齐，则剩余宽度为anchor的左边到屏幕右边
                rl = widthSize - anchorBound.left;
                rt = heightSize - anchorBound.top;
                rr = anchorBound.right;
                rb = anchorBound.bottom;
            }

            switch (gravity & Gravity.HORIZONTAL_GRAVITY_MASK) {
                case Gravity.LEFT:
                    widthSize = Math.min(widthSize, rl);
                    break;
                case Gravity.RIGHT:
                    widthSize = Math.min(widthSize, rr);
                    break;
                default:
                    break;
            }

            switch (gravity & Gravity.VERTICAL_GRAVITY_MASK) {
                case Gravity.TOP:
                    heightSize = Math.min(heightSize, rt);
                    break;
                case Gravity.BOTTOM:
                    heightSize = Math.min(heightSize, rb);
                    break;
                default:
                    break;
            }
        }


        if (mHelper.getMinWidth() > 0 && widthSize < mHelper.getMinWidth()) {
            widthSize = mHelper.getMinWidth();
        }

        if (mHelper.getMaxWidth() > 0 && widthSize > mHelper.getMaxWidth()) {
            widthSize = mHelper.getMaxWidth();
        }

        if (mHelper.getMinHeight() > 0 && heightSize < mHelper.getMinHeight()) {
            heightSize = mHelper.getMinHeight();
        }

        if (mHelper.getMaxHeight() > 0 && heightSize > mHelper.getMaxHeight()) {
            heightSize = mHelper.getMaxHeight();
        }

        widthMeasureSpec = MeasureSpec.makeMeasureSpec(widthSize, widthMode);
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(heightSize, heightMode);

        mTarget.measure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        PopupLog.i("onLayout", changed, l, t, r, b);
        changedGravity = Gravity.NO_GRAVITY;
        if (!mHelper.isOutSideTouchable()) {
            layoutNormal(l, t, r, b);
        } else {
            layoutOutSide(l, t, r, b);
        }
        layoutCount++;
        if (layoutCount >= 2) {
            isFirstLayoutComplete = true;
            layoutCount = 0;
        }
    }

    private void layoutOutSide(int l, int t, int r, int b) {
        if ((mFlag.flag & Flag.FLAG_WINDOW_PARAMS_FIT_REQUEST) != 0 && getLayoutParams() instanceof WindowManager.LayoutParams) {
            mWindowManagerProxy.updateViewLayout(this, getLayoutParams());
        }
        final int childCount = getChildCount();
        if (childCount > 0) {
            for (int i = 0; i < childCount; i++) {
                View child = getChildAt(i);
                child.layout(l, t, r, b);
                if (child == mTarget
                        && mMaskLayout != null
                        && mHelper.isAlignBackground()
                        && mHelper.getAlignBackgroundGravity() != Gravity.NO_GRAVITY) {
                    if (getLayoutParams() instanceof WindowManager.LayoutParams) {
                        WindowManager.LayoutParams p = ((WindowManager.LayoutParams) getLayoutParams());
                        l += p.x;
                        t += p.y;
                        r += p.x;
                        b += p.y;
                    }
                    mMaskLayout.handleAlignBackground(mHelper.getAlignBackgroundGravity(), l, t, r, b);
                }
            }
        }
    }

    @SuppressLint("RtlHardcoded")
    private void layoutNormal(int l, int t, int r, int b) {
        final int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            if (child.getVisibility() == GONE) continue;
            int width = child.getMeasuredWidth();
            int height = child.getMeasuredHeight();

            int gravity = mHelper.getPopupGravity();

            int childLeft = l;
            int childTop = t;

            int offsetX = mHelper.getOffsetX();
            int offsetY = mHelper.getOffsetY();

            boolean delayLayoutMask = mHelper.isAlignBackground() && mHelper.getAlignBackgroundGravity() != Gravity.NO_GRAVITY;

            if (child == mMaskLayout) {
                child.layout(childLeft, childTop, childLeft + width, childTop + height);
            } else {
                Rect anchorBound = mHelper.getAnchorViewBound();
                boolean isRelativeToAnchor = mHelper.isShowAsDropDown();
                boolean isAlignAnchorMode = mHelper.getGravityMode() == BasePopupWindow.GravityMode.ALIGN_TO_ANCHOR_SIDE;

                //不跟anchorView联系的情况下，gravity意味着在整个decorView中的方位
                //如果跟anchorView联系，gravity意味着以anchorView为中心的方位
                switch (gravity & Gravity.HORIZONTAL_GRAVITY_MASK) {
                    case Gravity.LEFT:
                        if (isRelativeToAnchor) {
                            childLeft = isAlignAnchorMode ? anchorBound.left : anchorBound.left - width;
                        }
                        break;
                    case Gravity.RIGHT:
                        if (isRelativeToAnchor) {
                            childLeft = isAlignAnchorMode ? anchorBound.right - width : anchorBound.right;
                        } else {
                            childLeft = r - width;
                        }
                        break;
                    case Gravity.CENTER_HORIZONTAL:
                        if (isRelativeToAnchor) {
                            childLeft = anchorBound.left;
                            offsetX += anchorBound.centerX() - (childLeft + (width >> 1));
                        } else {
                            childLeft = ((r - l - width) >> 1);
                        }
                        break;
                    default:
                        if (isRelativeToAnchor) {
                            childLeft = anchorBound.left;
                        }
                        break;
                }

                childLeft += childLeftMargin - childRightMargin;

                switch (gravity & Gravity.VERTICAL_GRAVITY_MASK) {
                    case Gravity.TOP:
                        if (isRelativeToAnchor) {
                            childTop = isAlignAnchorMode ? anchorBound.top : anchorBound.top - height;
                        }
                        break;
                    case Gravity.BOTTOM:
                        if (isRelativeToAnchor) {
                            childTop = isAlignAnchorMode ? anchorBound.bottom - height : anchorBound.bottom;
                        } else {
                            childTop = b - t - height;
                        }
                        break;
                    case Gravity.CENTER_VERTICAL:
                        if (isRelativeToAnchor) {
                            childTop = anchorBound.bottom;
                            offsetY += anchorBound.centerY() - (childTop + (height >> 1));
                        } else {
                            childTop = ((b - t - height) >> 1);
                        }
                        break;
                    default:
                        if (isRelativeToAnchor) {
                            childTop = anchorBound.bottom;
                        }
                        break;
                }
                childTop = childTop + childTopMargin - childBottomMargin;

                if (mHelper.isAutoLocatePopup()) {
                    int tBottom = childTop + height + offsetY + (mHelper.isFullScreen() ? 0 : -PopupUiUtils.getStatusBarHeight());
                    int restHeight;
                    switch (gravity & Gravity.VERTICAL_GRAVITY_MASK) {
                        case Gravity.TOP:
                            restHeight = isAlignAnchorMode ? b - anchorBound.top : anchorBound.top;
                            if (height > restHeight) {
                                //需要移位
                                offsetY += isAlignAnchorMode ? 0 : anchorBound.bottom - childTop;
                                //如果自动定位到下方，则可显示的window区域为[anchor底部，屏幕底部]
                                changedGravity = Gravity.BOTTOM;
                            }
                            break;
                        case Gravity.BOTTOM:
                        default:
                            restHeight = isAlignAnchorMode ? anchorBound.bottom : getMeasuredHeight() - anchorBound.bottom;

                            if (height > restHeight) {
                                //需要移位
                                offsetY -= isAlignAnchorMode ? 0 : tBottom - anchorBound.top;
                                //如果是自动定位到上方，则可显示的window区域为[0,anchor顶部]
                                changedGravity = Gravity.TOP;
                            }
                            break;
                    }
                }

                int left = childLeft + offsetX;
                int top = childTop + offsetY + (mHelper.isFullScreen() ? 0 : -PopupUiUtils.getStatusBarHeight());
                int right = left + width;
                int bottom = top + height;

                boolean isOverScreen = left < 0 || top < 0 || right > getMeasuredWidth() || bottom > getMeasuredHeight();

                if (isOverScreen) {
                    //水平调整
                    if (left < 0 && right > getMeasuredWidth()) {
                        left = 0;
                        right = getMeasuredWidth();
                    } else {
                        int horizontalOffset = 0;
                        if (left < 0) {
                            horizontalOffset = -left;
                        } else if (right > getMeasuredWidth()) {
                            horizontalOffset = Math.min(getMeasuredWidth() - right, 0);
                        }
                        left = left + horizontalOffset < 0 ? 0 : left + horizontalOffset;
                        right = right + horizontalOffset > getMeasuredWidth() ? getMeasuredWidth() : right + horizontalOffset;
                    }
                    //垂直调整
                    if (top < 0 && bottom > getMeasuredHeight()) {
                        top = 0;
                        bottom = getMeasuredHeight();
                    } else {
                        int verticalOffset = 0;
                        if (top < 0) {
                            verticalOffset = -top;
                        } else if (bottom > getMeasuredHeight()) {
                            verticalOffset = Math.min(getMeasuredHeight() - bottom, 0);
                        }
                        top = top + verticalOffset < 0 ? 0 : top + verticalOffset;
                        bottom = bottom + verticalOffset > getMeasuredHeight() ? getMeasuredHeight() : bottom + verticalOffset;
                    }
                }
                child.layout(left, top, right, bottom);
                if (delayLayoutMask) {
                    mMaskLayout.handleAlignBackground(mHelper.getAlignBackgroundGravity(), left, top, right, bottom);
                }
            }

        }
    }

    public void fitWindowParams(WindowManager.LayoutParams params) {
        if (getMeasuredWidth() == 0 || getMeasuredHeight() == 0) {
            mFlag.flag |= Flag.FLAG_WINDOW_PARAMS_FIT_REQUEST;
            return;
        }
        int offsetX = 0;
        int offsetY = 0;
        int gravity = mHelper.getPopupGravity();
        boolean isAlignAnchorMode = mHelper.getGravityMode() == BasePopupWindow.GravityMode.ALIGN_TO_ANCHOR_SIDE;
        Rect anchorBound = mHelper.getAnchorViewBound();

        // offset需要自行计算，不采用系统方法了
        switch (mHelper.getPopupGravity() & Gravity.HORIZONTAL_GRAVITY_MASK) {
            case Gravity.LEFT:
                if (mHelper.isShowAsDropDown()) {
                    offsetX += isAlignAnchorMode ? anchorBound.left : anchorBound.left - getMeasuredWidth();
                }
                break;
            case Gravity.RIGHT:
                if (mHelper.isShowAsDropDown()) {
                    offsetX += isAlignAnchorMode ? anchorBound.right - getMeasuredWidth() : anchorBound.right;
                } else {
                    offsetX += getAvaliableWidth() - getMeasuredWidth();
                }
                break;
            case Gravity.CENTER_HORIZONTAL:
                offsetX += mHelper.isShowAsDropDown() ? anchorBound.left + ((anchorBound.width() - getMeasuredWidth()) >> 1)
                        : (getAvaliableWidth() - getMeasuredWidth()) >> 1;
                break;
            default:
                break;
        }
        offsetX = offsetX + childLeftMargin - childRightMargin;

        switch (mHelper.getPopupGravity() & Gravity.VERTICAL_GRAVITY_MASK) {
            case Gravity.TOP:
                if (mHelper.isShowAsDropDown()) {
                    offsetY += isAlignAnchorMode ? anchorBound.top : anchorBound.top - getMeasuredHeight();
                }
                break;
            case Gravity.BOTTOM:
                if (mHelper.isShowAsDropDown()) {
                    offsetY += isAlignAnchorMode ? anchorBound.bottom - getMeasuredHeight() : anchorBound.bottom;
                } else {
                    offsetY += getAvaliableHeight() - getMeasuredHeight();
                }
                break;
            case Gravity.CENTER_VERTICAL:
                offsetY += mHelper.isShowAsDropDown() ? anchorBound.top + ((anchorBound.height() - getMeasuredHeight()) >> 1) : (getAvaliableHeight() - getMeasuredHeight()) >> 1;
                break;
            default:
                break;
        }
        offsetY = offsetY + childTopMargin - childBottomMargin;

        PopupLog.i("fitWindowParams  ::  " +
                "{\n\t\tscreenWidth = " + getAvaliableWidth() +
                "\n\t\tscreenHeight = " + getAvaliableHeight() +
                "\n\t\tanchor = " + anchorBound.toString() +
                "\n\t\tviewWidth = " + getMeasuredWidth() +
                "\n\t\tviewHeight = " + getMeasuredHeight() +
                "\n\t\toffsetX = " + offsetX +
                "\n\t\toffsetY = " + offsetY +
                "\n}");

        if (mHelper.isAutoLocatePopup()) {
            int tBottom = offsetY + (mHelper.isFullScreen() ? 0 : -PopupUiUtils.getStatusBarHeight());
            int restHeight;
            switch (gravity & Gravity.VERTICAL_GRAVITY_MASK) {
                case Gravity.TOP:
                    restHeight = isAlignAnchorMode ? getAvaliableHeight() - anchorBound.top : anchorBound.top;
                    if (getMeasuredHeight() > restHeight) {
                        //需要移位
                        offsetY += isAlignAnchorMode ? 0 : anchorBound.top + mHelper.getMinHeight() - offsetY;
                        changedGravity = Gravity.BOTTOM;
                    }
                    break;
                case Gravity.BOTTOM:
                default:
                    restHeight = isAlignAnchorMode ? anchorBound.bottom : getAvaliableHeight() - anchorBound.bottom;

                    if (getMeasuredHeight() > restHeight) {
                        //需要移位
                        offsetY -= isAlignAnchorMode ? 0 : tBottom - anchorBound.top;
                        changedGravity = Gravity.TOP;
                    }
                    break;
            }
        }

        params.x = offsetX + mHelper.getOffsetX();
        params.y = offsetY + mHelper.getOffsetY();
        originY = params.y;
        mFlag.flag &= ~Flag.FLAG_WINDOW_PARAMS_FIT_REQUEST;
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
        if (mHelper != null) {
            return mHelper.onInterceptTouchEvent(ev);
        }
        return false;
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        boolean intercept = mHelper != null && mHelper.onDispatchKeyEvent(event);
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
                    if (mHelper != null) {
                        PopupLog.i(TAG, "dispatchKeyEvent: >>> onBackPressed");
                        return mHelper.onBackPressed();
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
        if (mHelper != null) {
            if (mHelper.onTouchEvent(event)) {
                return true;
            }
        }
        final int x = (int) event.getX();
        final int y = (int) event.getY();

        if ((event.getAction() == MotionEvent.ACTION_DOWN)
                && ((x < 0) || (x >= getWidth()) || (y < 0) || (y >= getHeight()))) {
            if (mHelper != null) {
                PopupLog.i(TAG, "onTouchEvent:[ACTION_DOWN] >>> onOutSideTouch");
                return mHelper.onOutSideTouch();
            }
        } else if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
            if (mHelper != null) {
                PopupLog.i(TAG, "onTouchEvent:[ACTION_OUTSIDE] >>> onOutSideTouch");
                return mHelper.onOutSideTouch();
            }
        }
        return super.onTouchEvent(event);
    }

    int getAvaliableWidth() {
        int result = PopupUiUtils.getScreenWidthCompat();
        if (PopupUiUtils.hasNavigationBar(mHelper.popupWindow.getContext()) &&
                PopupUiUtils.getScreenRotation() == Surface.ROTATION_90) {
            result -= PopupUiUtils.getNavigationBarHeight();
        }
        PopupLog.i(result);
        return result;
    }

    int getAvaliableHeight() {
        int result = PopupUiUtils.getScreenHeightCompat();
        if (PopupUiUtils.hasNavigationBar(mHelper.popupWindow.getContext()) &&
                PopupUiUtils.getScreenRotation() == Surface.ROTATION_0) {
            result -= PopupUiUtils.getNavigationBarHeight();
        }
        PopupLog.i(result);
        return result;
    }


    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        clear(true);
    }

    @Override
    public boolean shouldDelayChildPressedState() {
        return false;
    }


    public void updateLayout() {
        if (mHelper != null) {
            mHelper.onUpdate();
        }
        if (mMaskLayout != null) {
            mMaskLayout.update();
        }
        if (isLayoutRequested()) {
            requestLayout();
        }
    }


    //layout之后
    @Override
    public void onGlobalLayout() {
        if (changedGravity != Gravity.NO_GRAVITY && mHelper.isAutoLocatePopup() && isFirstLayoutComplete) {
            mHelper.onAutoLocationChange(mHelper.popupGravity, changedGravity);
        }
    }

    @Override
    protected void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        View v = this;
        if (getContext() instanceof Activity) {
            v = ((Activity) getContext()).getWindow().getDecorView();
        }
        v.post(new Runnable() {
            @Override
            public void run() {
                updateLayout();
            }
        });
    }

    //-----------------------------------------keyboard-----------------------------------------

    @Override
    public void onKeyboardChange(Rect keyboardBounds, boolean isVisible) {
        int offset = 0;
        boolean forceAdjust = (mHelper.flag & BasePopupFlag.KEYBOARD_FORCE_ADJUST) != 0;
        boolean process = forceAdjust || ((PopupUiUtils.getScreenOrientation() != Configuration.ORIENTATION_LANDSCAPE)
                && (mHelper.getSoftInputMode() == WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN ||
                mHelper.getSoftInputMode() == WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE));

        if (!process) return;
        View anchor = null;

        if ((mHelper.flag & BasePopupFlag.KEYBOARD_ALIGN_TO_VIEW) != 0) {
            if (mHelper.keybaordAlignViewId != 0) {
                anchor = mTarget.findViewById(mHelper.keybaordAlignViewId);
            }
        }

        if ((mHelper.flag & BasePopupFlag.KEYBOARD_ALIGN_TO_ROOT) != 0 || anchor == null) {
            anchor = mTarget;
        }

        boolean animate = (mHelper.flag & BasePopupFlag.KEYBOARD_ANIMATE_ALIGN) != 0;

        anchor.getLocationOnScreen(location);
        int bottom = location[1] + anchor.getHeight();

        if (isVisible && keyboardBounds.height() > 0) {
            offset = keyboardBounds.top - bottom;
            if (bottom <= keyboardBounds.top
                    && (mHelper.flag & BasePopupFlag.KEYBOARD_IGNORE_OVER_KEYBOARD) != 0
                    && lastKeyboardBounds.isEmpty()) {
                offset = 0;
            }

        }

        if (mHelper.isOutSideTouchable()) {
            if ((mFlag.flag & Flag.FLAG_WINDOW_PARAMS_FIT_REQUEST) != 0) return;
            //移动window
            final WindowManager.LayoutParams p = PopupUtils.cast(getLayoutParams(), WindowManager.LayoutParams.class);
            if (p != null) {
                if (animate) {
                    animateTranslateWithOutside(p, isVisible, offset);
                } else {
                    p.y = isVisible ? p.y - offset : originY;
                    mWindowManagerProxy.updateViewLayoutOriginal(this, p);
                }
            }
        } else {
            if (animate) {
                animateTranslate(mTarget, isVisible, offset);
            } else {
                mTarget.setTranslationY(isVisible ? mTarget.getTranslationY() + offset : 0);
            }
        }
        if (isVisible) {
            lastKeyboardBounds.set(keyboardBounds);
        } else {
            lastKeyboardBounds.setEmpty();
        }
    }


    ValueAnimator valueAnimator;

    private void animateTranslateWithOutside(final WindowManager.LayoutParams p, boolean isVisible, int offset) {
        if (valueAnimator != null) {
            valueAnimator.cancel();
        }
        if (isVisible) {
            valueAnimator = ValueAnimator.ofInt(p.y, p.y + offset);
            valueAnimator.setDuration(300);
        } else {
            valueAnimator = ValueAnimator.ofInt(p.y, originY);
            valueAnimator.setDuration(200);
        }
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                p.y = (int) animation.getAnimatedValue();
                mWindowManagerProxy.updateViewLayoutOriginal(PopupDecorViewProxy.this, p);
            }
        });
        valueAnimator.start();
    }

    private void animateTranslate(View target, boolean isVisible, int offset) {
        target.animate().cancel();
        if (isVisible) {
            target.animate().translationYBy(offset).setDuration(300).start();
        } else {
            target.animate().translationY(0).setDuration(200).start();
        }
    }

    @Override
    public void clear(boolean destroy) {
        if (mHelper != null) {
            if (mHelper.isOutSideTouchable()) {
                if (mMaskLayout != null && mMaskLayout.getParent() != null) {
                    ((ViewGroup) mMaskLayout.getParent()).removeViewInLayout(mMaskLayout);
                }
            }
            mHelper.mKeyboardStateChangeListener = null;
        }
        mHelper = null;
        getViewTreeObserver().removeOnGlobalLayoutListener(this);
        mTarget = null;
    }

    static class Flag {
        static final int IDLE = 0;
        static final int FLAG_REST_WIDTH_NOT_ENOUGH = 0x00000001;
        static final int FLAG_REST_HEIGHT_NOT_ENOUGH = 0x00000010;
        static final int FLAG_WINDOW_PARAMS_FIT_REQUEST = 0x00000100;

        int flag;

    }
}
