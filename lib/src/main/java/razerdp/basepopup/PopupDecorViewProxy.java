package razerdp.basepopup;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.os.Message;
import android.util.LayoutDirection;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;

import razerdp.util.KeyboardUtils;
import razerdp.util.PopupUiUtils;

/**
 * Created by 大灯泡 on 2017/12/25.
 * <p>
 * popupwindow的decorview代理，这里统筹位置、蒙层、事件等
 */
final class PopupDecorViewProxy extends ViewGroup implements KeyboardUtils.OnKeyboardChangeListener, BasePopupEvent.EventObserver, ClearMemoryObject {
    private static final String TAG = "PopupDecorViewProxy";
    //蒙层
    private PopupMaskLayout mMaskLayout;
    private BasePopupHelper mHelper;
    private View mTarget;
    private Rect popupRect = new Rect();
    private Rect anchorRect = new Rect();
    private Rect contentRect = new Rect();
    private Rect contentBounds = new Rect();
    private Rect touchableRect = new Rect();
    private Rect touchableRectOnNoKeyboard = new Rect();

    private int childLeftMargin;
    private int childTopMargin;
    private int childRightMargin;
    private int childBottomMargin;

    private boolean reMeasure;

    private int[] location = new int[2];
    private Rect lastKeyboardBounds = new Rect();
    private OnClickListener emptyInterceptClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            //do nothing
            //主要是为了解决透传到蒙层的问题
        }
    };
    private boolean isStatusBarVisible = true;

    Rect keyboardBoundsCache;
    boolean keyboardVisibleCache = false;
    int lastKeyboardOffset = 0;

    private PopupDecorViewProxy(Context context) {
        super(context);
    }

    PopupDecorViewProxy(Context context, BasePopupHelper helper) {
        this(context);
        isStatusBarVisible = PopupUiUtils.isStatusBarVisible(context);
        init(helper);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void init(BasePopupHelper helper) {
        mHelper = helper;
        mHelper.observerEvent(this, this);
        mHelper.mKeyboardStateChangeListener = this;
        setClipChildren(mHelper.isClipChildren());
        mMaskLayout = new PopupMaskLayout(getContext(), mHelper);
        setLayoutParams(new ViewGroup.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        addViewInLayout(mMaskLayout, -1, new ViewGroup.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
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
        LayoutParams wp = setupViewSize(target, params);
        addView(target, wp);
    }

    WindowManager.LayoutParams setupViewSize(View target, WindowManager.LayoutParams params) {
        WindowManager.LayoutParams wp = new WindowManager.LayoutParams();
        wp.copyFrom(params);
        wp.x = 0;
        wp.y = 0;
        View contentView = target.findViewById(mHelper.contentRootId);
        if (contentView != null) {
            if (!contentView.hasOnClickListeners()) {
                target.setOnClickListener(emptyInterceptClickListener);
            } else {
                target.setOnClickListener(null);
            }
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
                LayoutParams p = parent.getLayoutParams();
                if (p == null) {
                    p = new LayoutParams(lp);
                } else {
                    p.width = p.height = LayoutParams.MATCH_PARENT;
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
                KeyboardUtils.open(focusTarget == null ? contentView : focusTarget, mHelper.showKeybaordDelay);
            }
        }

        wp.width = mHelper.getLayoutParams().width;
        wp.height = mHelper.getLayoutParams().height;
        childLeftMargin = mHelper.getLayoutParams().leftMargin;
        childTopMargin = mHelper.getLayoutParams().topMargin;
        childRightMargin = mHelper.getLayoutParams().rightMargin;
        childBottomMargin = mHelper.getLayoutParams().bottomMargin;

        mHelper.refreshNavigationBarBounds();
        //添加decorView作为自己的子控件
        if (wp.width > 0) {
            wp.width += childLeftMargin + childRightMargin;
        }
        if (wp.height > 0) {
            wp.height += childTopMargin + childBottomMargin;
        }
        return wp;
    }


    @Override
    public void requestLayout() {
        reMeasure = true;
        super.requestLayout();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            //蒙层给最大值
            if (child == mMaskLayout) {
                measureChild(child,
                        adjustWidthMeasureSpec(widthMeasureSpec, BasePopupFlag.OVERLAY_MASK),
                        adjustHeightMeasureSpec(heightMeasureSpec, BasePopupFlag.OVERLAY_MASK));
            } else {
                measureWrappedDecorView(child,
                        adjustWidthMeasureSpec(widthMeasureSpec, BasePopupFlag.OVERLAY_CONTENT),
                        adjustHeightMeasureSpec(heightMeasureSpec, BasePopupFlag.OVERLAY_CONTENT));
            }
        }
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
    }

    private int adjustWidthMeasureSpec(int widthMeasureSpec, int overlayTarget) {
        if ((overlayTarget & (BasePopupFlag.OVERLAY_MASK | BasePopupFlag.OVERLAY_CONTENT)) == 0) {
            return widthMeasureSpec;
        }
        //由于statusbar只会出现在顶部，不会出现在左右，因此宽度跟statusbar没啥关系
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);

        if ((mHelper.overlayNavigationBarMode & overlayTarget) == 0) {
            int navigationBarGravity = mHelper.getNavigationBarGravity();
            int navigationBarSize = mHelper.getNavigationBarSize();
            if (navigationBarGravity == Gravity.LEFT || navigationBarGravity == Gravity.RIGHT) {
                widthSize -= navigationBarSize;
            }
        }
        return MeasureSpec.makeMeasureSpec(widthSize, widthMode);
    }

    private int adjustHeightMeasureSpec(int heightMeasureSpec, int overlayTarget) {
        if ((overlayTarget & (BasePopupFlag.OVERLAY_MASK | BasePopupFlag.OVERLAY_CONTENT)) == 0) {
            return heightMeasureSpec;
        }
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        if ((mHelper.overlayStatusBarMode & overlayTarget) == 0 && isStatusBarVisible) {
            heightSize -= PopupUiUtils.getStatusBarHeight();
        }
        if ((mHelper.overlayNavigationBarMode & overlayTarget) == 0) {
            int navigationBarGravity = mHelper.getNavigationBarGravity();
            int navigationBarSize = mHelper.getNavigationBarSize();
            if (navigationBarGravity == Gravity.TOP || navigationBarGravity == Gravity.BOTTOM) {
                heightSize -= navigationBarSize;
            }
        }
        return MeasureSpec.makeMeasureSpec(heightSize, heightMode);
    }

    private void measureWrappedDecorView(View mTarget, int widthMeasureSpec, int heightMeasureSpec) {
        if (mTarget == null || mTarget.getVisibility() == GONE) return;
        final LayoutParams lp = mTarget.getLayoutParams();

        widthMeasureSpec = getChildMeasureSpec(widthMeasureSpec, 0, lp.width);
        heightMeasureSpec = getChildMeasureSpec(heightMeasureSpec, 0, lp.height);


        int widthSize = reMeasure ? MeasureSpec.getSize(widthMeasureSpec) :
                Math.max(mTarget.getMeasuredWidth(), MeasureSpec.getSize(widthMeasureSpec));
        int heightSize = reMeasure ? MeasureSpec.getSize(heightMeasureSpec) :
                Math.max(mTarget.getMeasuredHeight(), MeasureSpec.getSize(heightMeasureSpec));

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        int gravity = mHelper.getPopupGravity();

        //针对关联anchorView和对齐模式的测量（如果允许resize）
        if (mHelper.isWithAnchor()) {
            final Rect anchorRect = mHelper.getAnchorViewBound();
            //剩余空间
            //etc.如果是相对于锚点，则剩余宽度为屏幕左侧到anchor左侧
            int rl = anchorRect.left;
            int rt = anchorRect.top;
            int rr = widthSize - anchorRect.right;
            int rb = heightSize - anchorRect.bottom;
            //如果是对齐到anchor的边，则需要修正
            if (mHelper.horizontalGravityMode == BasePopupWindow.GravityMode.ALIGN_TO_ANCHOR_SIDE) {
                rl = widthSize - anchorRect.left;
                rr = anchorRect.right;
            }
            //如果是对齐到anchor的边，则需要修正
            if (mHelper.verticalGravityMode == BasePopupWindow.GravityMode.ALIGN_TO_ANCHOR_SIDE) {
                rt = heightSize - anchorRect.top;
                rb = anchorRect.bottom;
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
        }

        if (mHelper.isAlignAnchorHeight()) {
            heightSize = mHelper.getAnchorViewBound().height();
        }

        if (mHelper.getMinWidth() > 0 && widthSize < mHelper.getMinWidth()) {
            // 父控件小于最小宽度，意味着content也小于，此时除了设置父控件最小宽度外，也要设置子控件
            adjustContentViewMeasure(mTarget, mHelper.getMinWidth(), 0);
            widthSize = mHelper.getMinWidth();
            widthMode = MeasureSpec.EXACTLY;
        }

        if (mHelper.getMaxWidth() > 0 && widthSize > mHelper.getMaxWidth()) {
            widthSize = mHelper.getMaxWidth();
        }

        if (mHelper.getMinHeight() > 0 && heightSize < mHelper.getMinHeight()) {
            // 父控件小于最小高度，意味着content也小于，此时除了设置父控件最小高度外，也要设置子控件
            adjustContentViewMeasure(mTarget, 0, mHelper.getMinHeight());
            heightSize = mHelper.getMinHeight();
            heightMode = MeasureSpec.EXACTLY;
        }

        if (mHelper.getMaxHeight() > 0 && heightSize > mHelper.getMaxHeight()) {
            heightSize = mHelper.getMaxHeight();
        }

        widthMeasureSpec = MeasureSpec.makeMeasureSpec(widthSize, widthMode);
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(heightSize, heightMode);

        mTarget.measure(widthMeasureSpec, heightMeasureSpec);
    }

    void adjustContentViewMeasure(View target, int width, int height) {
        if (target == null) return;
        View contentView = mTarget.findViewById(mHelper.contentRootId);
        if (contentView != null) {
            LayoutParams p = contentView.getLayoutParams();
            if (p != null) {
                if (width != 0) {
                    p.width = width;
                }
                if (height != 0) {
                    p.height = height;
                }
            }
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        getLocationOnScreen(location);
        layoutInternal(l, t, r, b);
    }

    @SuppressLint("RtlHardcoded")
    private void layoutInternal(int l, int t, int r, int b) {
        final int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            if (child.getVisibility() == GONE) continue;

            //不覆盖状态栏
            if ((mHelper.overlayStatusBarMode & (child == mMaskLayout ? BasePopupFlag.OVERLAY_MASK : BasePopupFlag.OVERLAY_CONTENT)) == 0) {
                t = t == 0 ? t + PopupUiUtils.getStatusBarHeight() : t;
            } else {
                t = 0;
            }

            int width = child.getMeasuredWidth();
            int height = child.getMeasuredHeight();

            //contentview显示的界限，需要考虑navigationbar
            contentBounds.set(l, t, r, b);

            //由于可以布局到navigationbar上，因此在layout的时候对于contentView需要减去navigationbar的高度
            //同时需要判断navigationbar的方向，在蛋疼的模拟器或者某些rom上，横屏的时候navigationbar能在左右
            final int navigationBarGravity = mHelper.getNavigationBarGravity();
            final int navigationBarSize;
            if ((mHelper.overlayNavigationBarMode & (child == mMaskLayout ? BasePopupFlag.OVERLAY_MASK : BasePopupFlag.OVERLAY_CONTENT)) == 0) {
                navigationBarSize = mHelper.getNavigationBarSize();
            } else {
                navigationBarSize = 0;
            }

            switch (navigationBarGravity) {
                case Gravity.LEFT:
                    contentBounds.left += navigationBarSize;
                    break;
                case Gravity.RIGHT:
                    contentBounds.right -= navigationBarSize;
                    break;
                case Gravity.TOP:
                    contentBounds.top += navigationBarSize;
                    break;
                case Gravity.BOTTOM:
                    contentBounds.bottom -= navigationBarSize;
                    break;
            }

            final int gravity = mHelper.getPopupGravity();

            int offsetX = mHelper.getOffsetX();
            int offsetY = mHelper.getOffsetY();

            boolean delayLayoutMask = mHelper.isAlignBackground() && mHelper.getAlignBackgroundGravity() != Gravity.NO_GRAVITY;

            if (child == mMaskLayout) {
                contentBounds.offset(mHelper.maskOffsetX, mHelper.maskOffsetY);
                child.layout(contentBounds.left,
                        contentBounds.top,
                        contentBounds.left + getMeasuredWidth(),
                        contentBounds.top + getMeasuredHeight());
            } else {
                anchorRect.set(mHelper.getAnchorViewBound());
                anchorRect.offset(-location[0], -location[1]);
                boolean isRelativeToAnchor = mHelper.isWithAnchor();
                boolean isHorizontalAlignAnchorSlide = mHelper.horizontalGravityMode == BasePopupWindow.GravityMode.ALIGN_TO_ANCHOR_SIDE;
                boolean isVerticalAlignAnchorSlide = mHelper.verticalGravityMode == BasePopupWindow.GravityMode.ALIGN_TO_ANCHOR_SIDE;

                //不跟anchorView联系的情况下，gravity意味着在整个decorView中的方位
                //如果跟anchorView联系，gravity意味着以anchorView为中心的方位
                if (mHelper.layoutDirection == LayoutDirection.RTL) {
                    offsetX = ~offsetX + 1;
                }
                switch (gravity & Gravity.HORIZONTAL_GRAVITY_MASK) {
                    case Gravity.LEFT:
                        if (isRelativeToAnchor) {
                            contentRect.left = isHorizontalAlignAnchorSlide ? anchorRect.left : anchorRect.left - width;
                        } else {
                            contentRect.left = contentBounds.left;
                        }
                        break;
                    case Gravity.RIGHT:
                        if (isRelativeToAnchor) {
                            contentRect.left = isHorizontalAlignAnchorSlide ? anchorRect.right - width : anchorRect.right;
                        } else {
                            contentRect.left = contentBounds.right - width;
                        }
                        break;
                    case Gravity.CENTER_HORIZONTAL:
                        if (isRelativeToAnchor) {
                            contentRect.left = anchorRect.left;
                            offsetX += anchorRect.centerX() - (contentRect.left + (width >> 1));
                        } else {
                            contentRect.left = contentBounds.left + ((contentBounds.width() - width) >> 1);
                        }
                        break;
                    default:
                        if (isRelativeToAnchor) {
                            contentRect.left = anchorRect.left;
                        } else {
                            contentRect.left = contentBounds.left;
                        }
                        break;
                }

                contentRect.left += childLeftMargin - childRightMargin;

                switch (gravity & Gravity.VERTICAL_GRAVITY_MASK) {
                    case Gravity.TOP:
                        if (isRelativeToAnchor) {
                            contentRect.top = isVerticalAlignAnchorSlide ? anchorRect.top : anchorRect.top - height;
                        } else {
                            contentRect.top = contentBounds.top;
                        }
                        break;
                    case Gravity.BOTTOM:
                        if (isRelativeToAnchor) {
                            contentRect.top = isVerticalAlignAnchorSlide ? anchorRect.bottom - height : anchorRect.bottom;
                        } else {
                            contentRect.top = contentBounds.bottom - height;
                        }
                        break;
                    case Gravity.CENTER_VERTICAL:
                        if (isRelativeToAnchor) {
                            contentRect.top = anchorRect.bottom;
                            offsetY += anchorRect.centerY() - (contentRect.top + (height >> 1));
                        } else {
                            contentRect.top = contentBounds.top + ((contentBounds.height() - height) >> 1);
                        }
                        break;
                    default:
                        if (isRelativeToAnchor) {
                            contentRect.top = anchorRect.bottom;
                        } else {
                            contentRect.top = contentBounds.top;
                        }
                        break;
                }

                contentRect.top = contentRect.top + childTopMargin - childBottomMargin;

                if (mHelper.isAutoLocatePopup() && mHelper.isWithAnchor()) {
                    int tBottom = contentRect.top + height + offsetY;
                    int restHeight;
                    switch (gravity & Gravity.VERTICAL_GRAVITY_MASK) {
                        case Gravity.TOP:
                            restHeight = isVerticalAlignAnchorSlide ? b - anchorRect.top : anchorRect.top;
                            if (height > restHeight) {
                                //需要移位
                                offsetY += isVerticalAlignAnchorSlide ? 0 : anchorRect.bottom - contentRect.top;
                                //如果自动定位到下方，则可显示的window区域为[anchor底部，屏幕底部]
                            }
                            break;
                        case Gravity.BOTTOM:
                        default:
                            restHeight = isVerticalAlignAnchorSlide ? anchorRect.bottom : contentBounds
                                    .height() - anchorRect.bottom;

                            if (height > restHeight) {
                                //需要移位
                                offsetY -= isVerticalAlignAnchorSlide ? 0 : tBottom - anchorRect.top;
                                //如果是自动定位到上方，则可显示的window区域为[0,anchor顶部]
                            }
                            break;
                    }
                }

                contentRect.set(contentRect.left,
                        contentRect.top,
                        contentRect.left + width,
                        contentRect.top + height);

                contentRect.offset(offsetX, offsetY);


                boolean isOutsideScreen = !contentBounds.contains(contentRect);

                if (isOutsideScreen) {
                    //水平调整
                    if (contentRect.left < contentBounds.left) {
                        contentRect.offsetTo(contentBounds.left, contentRect.top);
                    }
                    if (contentRect.right > contentBounds.right) {
                        int subWidth = contentRect.right - contentBounds.right;
                        if (subWidth > contentRect.left - contentBounds.left) {
                            contentRect.offsetTo(contentBounds.left, contentRect.top);
                            contentRect.right = contentBounds.right;
                        } else {
                            contentRect.offset(-subWidth, 0);
                        }
                    }
                    //垂直调整
                    if (contentRect.top < contentBounds.top) {
                        contentRect.offsetTo(contentRect.left, contentBounds.top);
                    }
                    if (contentRect.bottom > contentBounds.bottom) {
                        int subHeight = contentRect.bottom - contentBounds.bottom;
                        if (subHeight > contentRect.bottom - contentBounds.bottom) {
                            contentRect.offsetTo(contentRect.left, contentBounds.top);
                            contentRect.bottom = contentBounds.bottom;
                        } else {
                            contentRect.offset(0, -subHeight);
                        }
                    }
                }
                //可点击区域
                touchableRect.set(contentRect);
                touchableRect.left += childLeftMargin;
                touchableRect.top += childTopMargin;
                touchableRect.right -= childRightMargin;
                touchableRect.bottom -= childBottomMargin;
                touchableRectOnNoKeyboard.set(touchableRect);
                if (lastKeyboardOffset != 0) {
                    touchableRect.offset(0, lastKeyboardOffset);
                }

                child.layout(contentRect.left, contentRect.top, contentRect.right, contentRect.bottom);
                if (delayLayoutMask) {
                    mMaskLayout.handleAlignBackground(mHelper.getAlignBackgroundGravity(),
                            contentRect.left, contentRect.top, contentRect.right, contentRect.bottom);
                }
                if (isRelativeToAnchor) {
                    popupRect.set(contentRect);
                    mHelper.onPopupLayout(popupRect, anchorRect);
                }
                reMeasure = false;
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
    public boolean onTouchEvent(MotionEvent event) {
        boolean intercept = mHelper != null && mHelper.onTouchEvent(event);
        if (intercept) return true;
        return super.onTouchEvent(event);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        //由于margin的情况会导致contentView的parent(decorView)会消耗该事件，因此我们这里手动分发给mask
        if (mMaskLayout == null) {
            return super.dispatchTouchEvent(ev);
        }
        int x = (int) ev.getX();
        int y = (int) ev.getY();
        if (!touchableRect.contains(x, y)) {
            return mMaskLayout.dispatchTouchEvent(ev);
        }
        return super.dispatchTouchEvent(ev);
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
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        clear(true);
    }

    @Override
    public boolean shouldDelayChildPressedState() {
        return false;
    }


    public void updateLayout() {
        if (mMaskLayout != null) {
            mMaskLayout.update();
        }
        if (mTarget != null) {
            WindowManager.LayoutParams lp = (WindowManager.LayoutParams) mTarget.getLayoutParams();
            if (lp.width != mHelper.getLayoutParams().width || lp.height != mHelper.getLayoutParams().height) {
                setupViewSize(mTarget, (WindowManager.LayoutParams) mTarget.getLayoutParams());
            }
            requestLayout();
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

    @Override
    public void onEvent(Message msg) {
        if (msg.what == BasePopupEvent.EVENT_ALIGN_KEYBOARD && keyboardBoundsCache != null) {
            onKeyboardChange(keyboardBoundsCache, keyboardVisibleCache);
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        if (mHelper != null && mHelper.mPopupWindow != null) {
            mHelper.mPopupWindow.onWindowFocusChanged(this, hasWindowFocus);
        }
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
        if (keyboardBoundsCache == null) {
            keyboardBoundsCache = new Rect();
        }
        keyboardBoundsCache.set(keyboardBounds);
        keyboardVisibleCache = isVisible;
        View alignWhat = mHelper.keybaordAlignView;

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
            touchableRectOnNoKeyboard.set(touchableRect);
            lastKeyboardOffset = offset;
            touchableRect.offset(0, offset);
            lastKeyboardBounds.set(keyboardBounds);
        } else {
            lastKeyboardOffset = 0;
            touchableRect.set(touchableRectOnNoKeyboard);
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
            mHelper.removeEventObserver(this);
        }
        if (mMaskLayout != null) {
            mMaskLayout.clear(destroy);
        }
        if (mTarget != null) {
            mTarget.setOnClickListener(null);
        }
        mHelper = null;
        mTarget = null;
    }
}
