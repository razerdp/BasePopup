package razerdp.basepopup;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.FrameLayout;

import razerdp.util.KeyboardUtils;
import razerdp.util.PopupUiUtils;
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
    private Rect popupRect = new Rect();
    private Rect anchorRect = new Rect();

    private int childLeftMargin;
    private int childTopMargin;
    private int childRightMargin;
    private int childBottomMargin;

    private int[] location = new int[2];
    private Rect lastKeyboardBounds = new Rect();
    private boolean isFirstLayoutComplete = false;
    private int layoutCount = 0;
    private OnClickListener emptyInterceptClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            //do nothing
            //主要是为了解决透传到蒙层的问题
        }
    };

    private PopupDecorViewProxy(Context context) {
        super(context);
    }

    PopupDecorViewProxy(Context context, BasePopupHelper helper) {
        this(context);
        getViewTreeObserver().addOnGlobalLayoutListener(this);
        init(helper);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void init(BasePopupHelper helper) {
        mHelper = helper;
        mHelper.mKeyboardStateChangeListener = this;
        setClipChildren(mHelper.isClipChildren());
        mMaskLayout = new PopupMaskLayout(getContext(), mHelper);
        setLayoutParams(new ViewGroup.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        addViewInLayout(mMaskLayout, -1, new ViewGroup.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        changedGravity = Gravity.NO_GRAVITY;
    }

    public void wrapPopupDecorView(final View target, WindowManager.LayoutParams params) {
        if (target == null) {
            throw new NullPointerException("contentView不能为空");
        }
        if (target.getParent() != null) {
            return;
        }

        //限制一个mask一个target，不允许多个target
        final int childCount = getChildCount();
        if (childCount >= 2) {
            removeViewsInLayout(1, childCount - 1);
        }

        mTarget = target;
        target.setOnClickListener(emptyInterceptClickListener);
        WindowManager.LayoutParams wp = new WindowManager.LayoutParams();
        wp.copyFrom(params);
        wp.x = 0;
        wp.y = 0;
        View contentView = findContentView(target);
        if (contentView != null) {
            LayoutParams lp = contentView.getLayoutParams();
            if (lp == null) {
                lp = new FrameLayout.LayoutParams(mHelper.getLayoutParams());
            } else {
                lp.width = mHelper.getLayoutParams().width;
                lp.height = mHelper.getLayoutParams().height;
                if (lp instanceof MarginLayoutParams) {
                    ((MarginLayoutParams) lp).leftMargin = mHelper.getLayoutParams().leftMargin;
                    ((MarginLayoutParams) lp).topMargin = mHelper.getLayoutParams().topMargin;
                    ((MarginLayoutParams) lp).rightMargin = mHelper.getLayoutParams().rightMargin;
                    ((MarginLayoutParams) lp).bottomMargin = mHelper.getLayoutParams().bottomMargin;
                }
            }

            View parent = (View) contentView.getParent();
            if (PopupUiUtils.isPopupBackgroundView(parent)) {
                //可能被包裹了一个backgroundview
                ViewGroup.LayoutParams p = parent.getLayoutParams();
                if (p == null) {
                    p = new ViewGroup.LayoutParams(lp);
                } else {
                    p.width = lp.width;
                    p.height = lp.height;
                }
                parent.setLayoutParams(p);
            }
            contentView.setLayoutParams(lp);

            //fixed #238  https://github.com/razerdp/BasePopup/issues/238
            if (contentView.isFocusable()) {
                if (contentView instanceof ViewGroup) {
                    ((ViewGroup) contentView).setDescendantFocusability(FOCUS_AFTER_DESCENDANTS);
                }
                PopupUiUtils.requestFocus(contentView);
            }

            if (mHelper.isAutoShowInputMethod()) {
                View focusTarget = mHelper.mAutoShowInputEdittext;
                if (focusTarget == null) {
                    focusTarget = contentView.findFocus();
                }
                KeyboardUtils.open(focusTarget == null ? contentView : focusTarget, 350);
            }
        }

        wp.width = mHelper.getLayoutParams().width;
        wp.height = mHelper.getLayoutParams().height;
        childLeftMargin = mHelper.getLayoutParams().leftMargin;
        childTopMargin = mHelper.getLayoutParams().topMargin;
        childRightMargin = mHelper.getLayoutParams().rightMargin;
        childBottomMargin = mHelper.getLayoutParams().bottomMargin;

        //添加decorView作为自己的子控件
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
        View result = root;
        while (!isContentView(result)) {
            result = rootGroup.getChildAt(0);
            if (result instanceof ViewGroup) {
                rootGroup = ((ViewGroup) result);
            } else {
                break;
            }
        }
        return result;
    }

    private boolean isContentView(View v) {
        return !PopupUiUtils.isPopupDecorView(v) &&
                !PopupUiUtils.isPopupBackgroundView(v) &&
                !PopupUiUtils.isPopupViewContainer(v);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        PopupLog.i("onMeasure", MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.getSize(heightMeasureSpec));
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
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
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
        if (mHelper.isWithAnchor()) {
            final Rect anchorBound = mHelper.getAnchorViewBound();
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
                    if (lp.width == LayoutParams.MATCH_PARENT) {
                        widthSize = rl;
                    }
                    if (mHelper.isFitsizable()) {
                        widthSize = Math.min(widthSize, rl);
                    }
                    break;
                case Gravity.RIGHT:
                    if (lp.width == LayoutParams.MATCH_PARENT) {
                        widthSize = rr;
                    }
                    if (mHelper.isFitsizable()) {
                        widthSize = Math.min(widthSize, rr);
                    }
                    break;
                default:
                    break;
            }

            switch (gravity & Gravity.VERTICAL_GRAVITY_MASK) {
                case Gravity.TOP:
                    if (lp.height == LayoutParams.MATCH_PARENT) {
                        heightSize = rt;
                    }
                    if (mHelper.isFitsizable()) {
                        heightSize = Math.min(heightSize, rt);
                    }
                    break;
                case Gravity.BOTTOM:
                    if (lp.height == LayoutParams.MATCH_PARENT) {
                        heightSize = rb;
                    }
                    if (mHelper.isFitsizable()) {
                        heightSize = Math.min(heightSize, rb);
                    }
                    break;
                default:
                    break;
            }
        }

        //如果跟anchor对齐大小
        if (mHelper.isAlignAnchorWidth()) {
            widthSize = mHelper.getAnchorViewBound().width();
            widthMode = MeasureSpec.EXACTLY;
        }

        if (mHelper.isAlignAnchorHeight()) {
            heightSize = mHelper.getAnchorViewBound().height();
            heightMode = MeasureSpec.EXACTLY;
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
        layoutInternal(l, t, r, b);
        layoutCount++;
        if (layoutCount >= 2) {
            isFirstLayoutComplete = true;
            layoutCount = 0;
        }
    }

    @SuppressLint("RtlHardcoded")
    private void layoutInternal(int l, int t, int r, int b) {
        final int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            if (child.getVisibility() == GONE) continue;
            int width = child.getMeasuredWidth();
            int height = child.getMeasuredHeight();

            int parentWidth = getMeasuredWidth();
            int parentHeight = getMeasuredHeight();

            int gravity = mHelper.getPopupGravity();

            int childLeft = l;
            int childTop = t;

            int offsetX = mHelper.getOffsetX();
            int offsetY = mHelper.getOffsetY();

            boolean delayLayoutMask = mHelper.isAlignBackground() && mHelper.getAlignBackgroundGravity() != Gravity.NO_GRAVITY;

            if (child == mMaskLayout) {
                child.layout(childLeft, childTop, childLeft + width, childTop + height);
            } else {
                //由于可以布局到navigationbar上，因此在layout的时候对于contentView需要减去navigationbar的高度
                parentHeight -= mHelper.getNavigationBarHeight();
                Rect anchorBound = mHelper.getAnchorViewBound();
                boolean isRelativeToAnchor = mHelper.isWithAnchor();
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
                            childTop = b - height;
                        }
                        break;
                    case Gravity.CENTER_VERTICAL:
                        if (isRelativeToAnchor) {
                            childTop = anchorBound.bottom;
                            offsetY += anchorBound.centerY() - (childTop + (height >> 1));
                        } else {
                            childTop = ((b - height) >> 1);
                        }
                        break;
                    default:
                        if (isRelativeToAnchor) {
                            childTop = anchorBound.bottom;
                        }
                        break;
                }

                childTop = childTop + childTopMargin - childBottomMargin - (mHelper.isOverlayStatusbar() ? 0 : PopupUiUtils.getStatusBarHeight());

                if (mHelper.isAutoLocatePopup() && mHelper.isWithAnchor()) {
                    int tRight = childLeft + width + offsetX;
                    int tBottom = childTop + height + offsetY;
                    int restWidth, restHeight;
                    // TODO: 2020/3/11 优化autolocate
                    /*switch (gravity & Gravity.HORIZONTAL_GRAVITY_MASK) {
                        case Gravity.RIGHT:
                            restWidth = isAlignAnchorMode ? anchorBound.right : r - anchorBound.right;
                            if (tRight > restWidth) {

                            }
                            break;
                        case Gravity.LEFT:
                        default:
                            break;
                    }*/
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
                            restHeight = isAlignAnchorMode ? anchorBound.bottom : parentHeight - anchorBound.bottom;

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
                int top = childTop + offsetY;
                int right = left + width;
                int bottom = top + height;

                boolean isOutsideScreen = left < 0 || top < 0 || right > parentWidth || bottom > parentHeight;

                if (isOutsideScreen) {
                    //水平调整
                    if (left < 0 && right > parentWidth) {
                        left = 0;
                        right = parentWidth;
                    } else {
                        int horizontalOffset = 0;
                        if (left < 0) {
                            horizontalOffset = -left;
                        } else if (right > parentWidth) {
                            horizontalOffset = Math.min(parentWidth - right, 0);
                        }
                        left = Math.max(left + horizontalOffset, 0);
                        right = Math.min(right + horizontalOffset, parentWidth);
                    }
                    //垂直调整
                    if (top < 0 && bottom > parentHeight) {
                        top = 0;
                        bottom = parentHeight;
                    } else {
                        int verticalOffset = 0;
                        if (top < 0) {
                            verticalOffset = -top;
                        } else if (bottom > parentHeight) {
                            verticalOffset = Math.min(parentHeight - bottom, 0);
                        }
                        top = Math.max(top + verticalOffset, 0);
                        bottom = Math.min(bottom + verticalOffset, parentHeight);
                    }
                }
                child.layout(left, top, right, bottom);
                if (delayLayoutMask) {
                    mMaskLayout.handleAlignBackground(mHelper.getAlignBackgroundGravity(), left, top, right, bottom);
                }
                if (isRelativeToAnchor) {
                    popupRect.set(left, top, right, bottom);
                    anchorRect.set(mHelper.getAnchorViewBound());
                    mHelper.onPopupLayout(popupRect, anchorRect);
                }
            }
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (mMaskLayout != null) {
            mMaskLayout.handleStart(-2);
        }
        if (mHelper != null) {
            mHelper.onAttachToWindow();
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean intercept = mHelper != null && mHelper.onInterceptTouchEvent(ev);
        if (intercept) return true;
        return super.onInterceptTouchEvent(ev);
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
        requestLayout();
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
        if (mHelper.isOutSideTouchable() && !mHelper.isOverlayStatusbar()) return;
        int offset = 0;
        boolean forceAdjust = (mHelper.flag & BasePopupFlag.KEYBOARD_FORCE_ADJUST) != 0;
        boolean process = forceAdjust || ((PopupUiUtils.getScreenOrientation() != Configuration.ORIENTATION_LANDSCAPE)
                && (mHelper.getSoftInputMode() == WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN ||
                mHelper.getSoftInputMode() == WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE));

        if (!process) return;
        View alignWhat = null;

        if ((mHelper.flag & BasePopupFlag.KEYBOARD_ALIGN_TO_VIEW) != 0) {
            if (mHelper.keybaordAlignViewId != 0) {
                alignWhat = mTarget.findViewById(mHelper.keybaordAlignViewId);
            }
        }

        if ((mHelper.flag & BasePopupFlag.KEYBOARD_ALIGN_TO_ROOT) != 0 || alignWhat == null) {
            alignWhat = mTarget;
        }

        boolean animate = (mHelper.flag & BasePopupFlag.KEYBOARD_ANIMATE_ALIGN) != 0;
        alignWhat.getLocationOnScreen(location);
        //自身或者指定view的bottom
        int bottom = location[1] + alignWhat.getHeight();

        if (isVisible && keyboardBounds.height() > 0) {
            offset = keyboardBounds.top - bottom;
            if (bottom <= keyboardBounds.top
                    && (mHelper.flag & BasePopupFlag.KEYBOARD_IGNORE_OVER_KEYBOARD) != 0
                    && lastKeyboardBounds.isEmpty()) {
                offset = 0;
            } else {
                //如果是有anchor，则考虑anchor的情况
                if (mHelper.isWithAnchor()) {
                    int gravity = PopupUiUtils.computeGravity(popupRect, anchorRect);
                    if ((gravity & Gravity.VERTICAL_GRAVITY_MASK) == Gravity.TOP) {
                        //显示在anchor顶部，则需要考虑anchor的高度
                        offset -= mHelper.getAnchorViewBound().height();
                    }
                }
            }
        }

        if (animate) {
            animateTranslate(mTarget, isVisible, offset);
        } else {
            mTarget.setTranslationY(isVisible ? mTarget.getTranslationY() + offset : 0);
        }

        if (isVisible) {
            lastKeyboardBounds.set(keyboardBounds);
        } else {
            lastKeyboardBounds.setEmpty();
        }
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
            mHelper.mKeyboardStateChangeListener = null;
        }
        if (mMaskLayout != null) {
            mMaskLayout.clear(destroy);
        }
        if (mTarget != null) {
            mTarget.setOnClickListener(null);
        }
        isFirstLayoutComplete = false;
        layoutCount = 0;
        getViewTreeObserver().removeOnGlobalLayoutListener(this);
        mHelper = null;
        mTarget = null;
    }
}
