package razerdp.basepopup;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Message;
import android.util.LayoutDirection;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
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
    //蒙层
    private PopupMaskLayout mMaskLayout;
    private int childBottomMargin;

    BasePopupHelper mHelper;
    private View mTarget;
    private Rect popupRect = new Rect();
    private Rect lastPopupRect = new Rect();
    private Rect anchorRect = new Rect();
    private Rect contentRect = new Rect();
    private Rect contentBounds = new Rect();
    private Rect touchableRect = new Rect();
    private Rect touchableRectOnNoKeyboard = new Rect();

    private int childLeftMargin;
    private int childTopMargin;
    private int childRightMargin;

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
    Point lastKeyboardOffset = new Point();

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
        setLayoutParams(new ViewGroup.LayoutParams(LayoutParams.MATCH_PARENT,
                                                   LayoutParams.MATCH_PARENT));
        addViewInLayout(mMaskLayout,
                        -1,
                        new ViewGroup.LayoutParams(LayoutParams.MATCH_PARENT,
                                                   LayoutParams.MATCH_PARENT));
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

    /**
     * 我们实际上操作的是被系统PopupWindow包裹的View
     * 我们要做的是将我们contentview的layoutparam重新设置到其parent中
     */
    WindowManager.LayoutParams setupViewSize(View target, WindowManager.LayoutParams params) {
        WindowManager.LayoutParams wp = new WindowManager.LayoutParams();
        wp.copyFrom(params);
        wp.x = 0;
        wp.y = 0;
        View contentView = target.findViewById(mHelper.contentRootId);
        wp.width = mHelper.getLayoutParams().width;
        wp.height = mHelper.getLayoutParams().height;
        childLeftMargin = mHelper.getLayoutParams().leftMargin;
        childTopMargin = mHelper.getLayoutParams().topMargin;
        childRightMargin = mHelper.getLayoutParams().rightMargin;
        childBottomMargin = mHelper.getLayoutParams().bottomMargin;
        mHelper.refreshNavigationBarBounds();

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
                    if (lp.width == LayoutParams.MATCH_PARENT) {
                        ((MarginLayoutParams) lp).leftMargin = childLeftMargin;
                        ((MarginLayoutParams) lp).rightMargin = childRightMargin;
                    }
                    if (lp.height == LayoutParams.MATCH_PARENT) {
                        ((MarginLayoutParams) lp).topMargin = mHelper.getLayoutParams().topMargin;
                        ((MarginLayoutParams) lp).bottomMargin = mHelper.getLayoutParams().bottomMargin;
                    }
                }
            }

            View parent = (View) contentView.getParent();
            // 如果是background，则要求其填满decor
            if (PopupUiUtils.isPopupBackgroundView(parent)) {
                LayoutParams p = parent.getLayoutParams();
                if (p == null) {
                    p = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
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
                KeyboardUtils.open(focusTarget == null ? contentView : focusTarget,
                                   mHelper.showKeybaordDelay);
            }
        }
        return wp;
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
                             adjustHeightMeasureSpec(heightMeasureSpec,
                                                     BasePopupFlag.OVERLAY_MASK));
            } else {
                measureWrappedDecorView(child,
                                        adjustWidthMeasureSpec(widthMeasureSpec,
                                                               BasePopupFlag.OVERLAY_CONTENT),
                                        adjustHeightMeasureSpec(heightMeasureSpec,
                                                                BasePopupFlag.OVERLAY_CONTENT));
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

    /**
     * measure contentview
     * 由于target的layoutparam实际上是windowmanager#layoutparams，不包含margin的问题，但我们需要考虑margin的问题
     */
    private void measureWrappedDecorView(View mTarget, int widthMeasureSpec, int heightMeasureSpec) {
        if (mTarget == null || mTarget.getVisibility() == GONE) return;
        final LayoutParams lp = mTarget.getLayoutParams();

        final int parentWidth = MeasureSpec.getSize(widthMeasureSpec);
        final int parentHeight = MeasureSpec.getSize(heightMeasureSpec);

        boolean reMeasureContentView = mHelper.isFitsizable();

        // 根据parent决定child大小
        int childWidthMeasureSpec = getChildMeasureSpec(widthMeasureSpec, 0, lp.width);
        int childHeightMeasureSpec = getChildMeasureSpec(heightMeasureSpec, 0, lp.height);

        int widthSize = MeasureSpec.getSize(childWidthMeasureSpec);
        int heightSize = MeasureSpec.getSize(childHeightMeasureSpec);
        int widthMode = MeasureSpec.getMode(childWidthMeasureSpec);
        int heightMode = MeasureSpec.getMode(childHeightMeasureSpec);

        int gravity = mHelper.getPopupGravity();

        //针对关联anchorView和对齐模式的测量（如果允许resize）
        if (mHelper.isWithAnchor()) {
            final Rect anchorRect = mHelper.getAnchorViewBound();
            //剩余空间
            //etc.如果是相对于锚点，则剩余宽度为屏幕左侧到anchor左侧
            int rl = anchorRect.left;
            int rt = anchorRect.top;
            int rr = parentWidth - anchorRect.right;
            int rb = parentHeight - anchorRect.bottom;
            //如果是对齐到anchor的边，则需要修正
            if (mHelper.horizontalGravityMode == BasePopupWindow.GravityMode.ALIGN_TO_ANCHOR_SIDE) {
                rl = parentWidth - anchorRect.left;
                rr = anchorRect.right;
            }
            //如果是对齐到anchor的边，则需要修正
            if (mHelper.verticalGravityMode == BasePopupWindow.GravityMode.ALIGN_TO_ANCHOR_SIDE) {
                rt = parentHeight - anchorRect.top;
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
            reMeasureContentView = true;
        }

        if (mHelper.isAlignAnchorHeight()) {
            heightSize = mHelper.getAnchorViewBound().height();
            reMeasureContentView = true;
        }

        if (mHelper.getMinWidth() > 0 && widthSize < mHelper.getMinWidth()) {
            widthSize = mHelper.getMinWidth();
            widthMode = MeasureSpec.EXACTLY;
            reMeasureContentView = true;
        }

        if (mHelper.getMaxWidth() > 0 && widthSize > mHelper.getMaxWidth()) {
            widthSize = mHelper.getMaxWidth();
            reMeasureContentView = true;
        }

        if (mHelper.getMinHeight() > 0 && heightSize < mHelper.getMinHeight()) {
            heightSize = mHelper.getMinHeight();
            heightMode = MeasureSpec.EXACTLY;
            reMeasureContentView = true;
        }

        if (mHelper.getMaxHeight() > 0 && heightSize > mHelper.getMaxHeight()) {
            heightSize = mHelper.getMaxHeight();
            reMeasureContentView = true;
        }

        childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(widthSize, widthMode);
        childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(heightSize, heightMode);

        mTarget.measure(childWidthMeasureSpec, childHeightMeasureSpec);
        View contentView = mTarget.findViewById(mHelper.contentRootId);
        if (contentView != null && reMeasureContentView) {
            contentView.measure(childWidthMeasureSpec, childHeightMeasureSpec);
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
                // 除了match_parent外，margin都是等同于offset
                boolean useWidthMarginAsOffset = mHelper.getLayoutParams().width != LayoutParams.MATCH_PARENT;
                boolean useHeightMarginAsOffset = mHelper.getLayoutParams().height != LayoutParams.MATCH_PARENT;

                //不跟anchorView联系的情况下，gravity意味着在整个decorView中的方位
                //如果跟anchorView联系，gravity意味着以anchorView为中心的方位
                if (mHelper.layoutDirection == LayoutDirection.RTL) {
                    offsetX = ~offsetX + 1;
                }
                switch (gravity & Gravity.HORIZONTAL_GRAVITY_MASK) {
                    case Gravity.RIGHT:
                        if (isRelativeToAnchor) {
                            contentRect.left = isHorizontalAlignAnchorSlide ? anchorRect.right - width : anchorRect.right;
                        } else {
                            contentRect.left = contentBounds.right - width;
                        }
                        if (useWidthMarginAsOffset) {
                            if (isRelativeToAnchor && !isHorizontalAlignAnchorSlide) {
                                // 在anchor右侧，只跟leftmargin相关
                                offsetX += childLeftMargin;
                            } else {
                                offsetX -= childRightMargin;
                            }
                        }
                        break;
                    case Gravity.CENTER_HORIZONTAL:
                        if (isRelativeToAnchor) {
                            contentRect.left = anchorRect.left;
                            offsetX += anchorRect.centerX() - (contentRect.left + (width >> 1));
                        } else {
                            contentRect.left = contentBounds.left + ((contentBounds.width() - width) >> 1);
                        }
                        if (useWidthMarginAsOffset) {
                            offsetX += childLeftMargin - childRightMargin;
                        }
                        break;
                    case Gravity.LEFT:
                    default:
                        if (isRelativeToAnchor) {
                            contentRect.left = isHorizontalAlignAnchorSlide ? anchorRect.left : anchorRect.left - width;
                        } else {
                            contentRect.left = contentBounds.left;
                        }
                        if (useWidthMarginAsOffset) {
                            if (isRelativeToAnchor && !isHorizontalAlignAnchorSlide) {
                                // 在anchor左侧，只跟rightmargin相关
                                offsetX += -childRightMargin;
                            } else {
                                offsetX += childLeftMargin;
                            }
                        }
                        break;
                }

                switch (gravity & Gravity.VERTICAL_GRAVITY_MASK) {
                    case Gravity.TOP:
                        if (isRelativeToAnchor) {
                            contentRect.top = isVerticalAlignAnchorSlide ? anchorRect.top : anchorRect.top - height;
                        } else {
                            contentRect.top = contentBounds.top;
                        }
                        if (useHeightMarginAsOffset) {
                            if (isRelativeToAnchor && !isVerticalAlignAnchorSlide) {
                                // 在anchor上方，只跟bottommargin相关
                                offsetY += -childBottomMargin;
                            } else {
                                offsetY += childTopMargin;
                            }
                        }
                        break;
                    case Gravity.CENTER_VERTICAL:
                        if (isRelativeToAnchor) {
                            contentRect.top = anchorRect.bottom;
                            offsetY += anchorRect.centerY() - (contentRect.top + (height >> 1));
                        } else {
                            contentRect.top = contentBounds.top + ((contentBounds.height() - height) >> 1);
                        }
                        if (useHeightMarginAsOffset) {
                            offsetY += childTopMargin - childBottomMargin;
                        }
                        break;
                    case Gravity.BOTTOM:
                    default:
                        if (isRelativeToAnchor) {
                            contentRect.top = isVerticalAlignAnchorSlide ? anchorRect.bottom - height : anchorRect.bottom;
                        } else {
                            contentRect.top = contentBounds.bottom - height;
                        }
                        if (useHeightMarginAsOffset) {
                            if (isRelativeToAnchor && !isVerticalAlignAnchorSlide) {
                                // 在anchor下方，只跟topmargin相关
                                offsetY += childTopMargin;
                            } else {
                                offsetY += -childBottomMargin;
                            }
                        }
                        break;
                }

                // 处理相对模式下位置不足的情况，显示在镜像位置
                if (mHelper.isAutoMirror() && mHelper.isWithAnchor()) {
                    switch (gravity & Gravity.VERTICAL_GRAVITY_MASK) {
                        case Gravity.TOP:
                            if (isVerticalAlignAnchorSlide) break;
                            // 显示在上方时，剩余空间为[0,anchor顶部]
                            if (height > anchorRect.top) {
                                offsetY += anchorRect.bottom - contentRect.top;
                            }
                            break;
                        case Gravity.BOTTOM:
                        default:
                            if (isVerticalAlignAnchorSlide) break;
                            // 显示在底部，剩余空间为[anchor.bottom，屏幕底部]
                            if (height > contentBounds.bottom - anchorRect.bottom) {
                                int popupContentBottom = contentRect.top + height + offsetY;
                                offsetY -= popupContentBottom - anchorRect.top;
                            }
                            break;
                    }
                    switch (gravity & Gravity.HORIZONTAL_GRAVITY_MASK) {
                        case Gravity.RIGHT:
                            if (isHorizontalAlignAnchorSlide) break;
                            // 显示在右边，剩余空间为[anchor.right,屏幕右方]
                            if (width > contentBounds.right - anchorRect.right) {
                                int popupContentRight = contentRect.left + width + offsetX;
                                offsetX += anchorRect.left - popupContentRight;
                            }
                            break;
                        case Gravity.LEFT:
                            if (isHorizontalAlignAnchorSlide) break;
                            // 显示在左边，剩余空间为[0,anchor.left]
                            if (width > anchorRect.left) {
                                offsetX += anchorRect.right - contentRect.left;
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
                touchableRect.offset(lastKeyboardOffset.x, lastKeyboardOffset.y);

                child.layout(contentRect.left,
                             contentRect.top,
                             contentRect.right,
                             contentRect.bottom);
                if (delayLayoutMask) {
                    mMaskLayout.handleAlignBackground(mHelper.getAlignBackgroundGravity(),
                                                      contentRect.left,
                                                      contentRect.top,
                                                      contentRect.right,
                                                      contentRect.bottom);
                }
                if (isRelativeToAnchor) {
                    popupRect.set(contentRect);
                    mHelper.onPopupLayout(popupRect, anchorRect);
                }
                if (!lastPopupRect.equals(contentRect)) {
                    mHelper.onSizeChange(lastPopupRect.width(),
                                         lastPopupRect.height(),
                                         contentRect.width(),
                                         contentRect.height());
                    lastPopupRect.set(contentRect);
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

        int offsetX;
        int offsetY;
        final int keyboardGravity = mHelper.keyboardGravity;
        boolean animate = (mHelper.flag & BasePopupFlag.KEYBOARD_ANIMATE_ALIGN) != 0;
        alignWhat.getLocationOnScreen(location);
        //自身或者指定view的bottom
        final int left = location[0];
        final int top = location[1];
        final int right = left + alignWhat.getWidth();
        final int bottom = top + alignWhat.getHeight();
        final int centerX = (left + right) >> 1;
        final int centerY = (top + bottom) >> 1;

        switch (keyboardGravity & Gravity.HORIZONTAL_GRAVITY_MASK) {
            case Gravity.LEFT:
                offsetX = -left;
                break;
            case Gravity.RIGHT:
                offsetX = keyboardBounds.right - right;
                break;
            case Gravity.CENTER_HORIZONTAL:
                offsetX = keyboardBounds.centerX() - centerX;
                break;
            default:
                offsetX = 0;
                break;
        }
        switch (keyboardGravity & Gravity.VERTICAL_GRAVITY_MASK) {
            case Gravity.TOP:
                offsetY = -top;
                break;
            case Gravity.BOTTOM:
                offsetY = keyboardBounds.top - bottom;
                break;
            case Gravity.CENTER_VERTICAL:
                offsetY = (keyboardBounds.top >> 1) - centerY;
                break;
            default:
                offsetY = 0;
                break;
        }

        if (isVisible && keyboardBounds.height() > 0) {
            if ((mHelper.flag & BasePopupFlag.KEYBOARD_IGNORE_OVER_KEYBOARD) != 0) {
                // 忽略basepopup在键盘上方
                if (bottom <= keyboardBounds.height() && lastKeyboardBounds.isEmpty()) {
                    offsetY = 0;
                }
            } else {
                if (mHelper.isWithAnchor()) {
                    //如果是有anchor，则考虑anchor的情况
                    int gravity = PopupUiUtils.computeGravity(popupRect, anchorRect);
                    if ((gravity & Gravity.VERTICAL_GRAVITY_MASK) == Gravity.TOP) {
                        //显示在anchor顶部，则需要考虑anchor的高度
                        offsetY -= mHelper.getAnchorViewBound().height();
                    }
                }
            }
            offsetX += mHelper.keyboardOffsetX;
            offsetY += mHelper.keyboardOffsetY;
        } else {
            offsetX = offsetY = 0;
        }

        if (animate) {
            animateTranslate(mTarget, offsetX, offsetY);
        } else {
            mTarget.setTranslationX(isVisible ? mTarget.getTranslationX() + offsetX : offsetX);
            mTarget.setTranslationY(isVisible ? mTarget.getTranslationY() + offsetY : offsetY);
        }

        if (isVisible) {
            touchableRectOnNoKeyboard.set(touchableRect);
            lastKeyboardOffset.set(offsetX, offsetY);
            touchableRect.offset(offsetX, offsetY);
            lastKeyboardBounds.set(keyboardBounds);
        } else {
            lastKeyboardOffset.set(offsetX, offsetY);
            touchableRect.set(touchableRectOnNoKeyboard);
            lastKeyboardBounds.setEmpty();
        }
    }

    private void animateTranslate(View target, int offsetX, int offsetY) {
        target.animate().cancel();
        ViewPropertyAnimator anim = target.animate();
        if (offsetX != 0) {
            anim.translationXBy(offsetX);
        } else {
            anim.translationX(0);
        }
        if (offsetY != 0) {
            anim.translationYBy(offsetY);
        } else {
            anim.translationY(0);
        }
        anim.start();
    }

    @Override
    public void clear(boolean destroy) {
        if (mHelper != null) {
            mHelper.showFlag = 0;
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
