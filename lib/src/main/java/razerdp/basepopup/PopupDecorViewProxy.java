package razerdp.basepopup;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Rect;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import razerdp.util.PopupUiUtils;
import razerdp.util.log.LogTag;
import razerdp.util.log.PopupLogUtil;

/**
 * Created by 大灯泡 on 2017/12/25.
 * <p>
 * 旨在用来拦截keyevent、以及蒙层
 */
final class PopupDecorViewProxy extends ViewGroup implements PopupKeyboardStateChangeListener {
    private static final String TAG = "PopupDecorViewProxy";
    //模糊层
    private PopupMaskLayout mMaskLayout;
    private BasePopupHelper mHelper;
    private View mTarget;
    private Rect mTouchRect = new Rect();

    private int childLeftMargin;
    private int childTopMargin;
    private int childRightMargin;
    private int childBottomMargin;

    private static int statusBarHeight;

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

    @SuppressLint("ClickableViewAccessibility")
    private void init(BasePopupHelper helper) {
        mHelper = helper;
        mHelper.registerKeyboardStateChangeListener(this);
        setClipChildren(mHelper.isClipChildren());
        if (mHelper.isInterceptTouchEvent()) {
            setLayoutParams(new ViewGroup.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
            mMaskLayout = PopupMaskLayout.create(getContext(), mHelper);
            addViewInLayout(mMaskLayout, -1, new ViewGroup.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
            mMaskLayout.setOnTouchListener(new OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            return mHelper.isDismissWhenTouchOutside();
                        case MotionEvent.ACTION_UP:
                            if (mHelper.isDismissWhenTouchOutside()) {
                                int x = (int) event.getX();
                                int y = (int) event.getY();
                                if (mTarget != null) {
                                    mTarget.getGlobalVisibleRect(mTouchRect);
                                }
                                if (!mTouchRect.contains(x, y)) {
                                    mHelper.onOutSideTouch();
                                }
                            }
                            break;
                    }
                    return false;
                }
            });
        } else {
            setLayoutParams(new ViewGroup.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        }
    }


    public void addPopupDecorView(View target, WindowManager.LayoutParams params) {
        if (target == null) {
            throw new NullPointerException("contentView不能为空");
        }
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
            wp.width = mHelper.getPopupViewWidth();
            wp.height = mHelper.getPopupViewHeight();
            childLeftMargin = mHelper.getParaseFromXmlParams().leftMargin;
            childTopMargin = mHelper.getParaseFromXmlParams().topMargin;
            childRightMargin = mHelper.getParaseFromXmlParams().rightMargin;
            childBottomMargin = mHelper.getParaseFromXmlParams().bottomMargin;
        }

        addView(target, wp);
    }

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
        if (mHelper.isInterceptTouchEvent()) {
            measureWithIntercept(widthMeasureSpec, heightMeasureSpec);
        } else {
            measureWithOutIntercept(widthMeasureSpec, heightMeasureSpec);
        }
    }

    private void measureWithIntercept(int widthMeasureSpec, int heightMeasureSpec) {
        final int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            //蒙层给最大值
            if (child == mMaskLayout) {
                measureChild(child, MeasureSpec.makeMeasureSpec(getScreenWidth(), MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(getScreenHeight(), MeasureSpec.EXACTLY));
            } else {
                final LayoutParams lp = child.getLayoutParams();
                final int childWidthMeasureSpec = getChildMeasureSpec(widthMeasureSpec,
                        childLeftMargin + childRightMargin, lp.width);
                int fixedHeightMeasureSpec = heightMeasureSpec;
                //针对match_parent
                if (mHelper.isClipToScreen() && mHelper.isShowAsDropDown() && lp.height == LayoutParams.MATCH_PARENT) {
                    int mode = MeasureSpec.getMode(heightMeasureSpec);
                    int restContentHeight = getScreenHeight() - (mHelper.getAnchorY() + mHelper.getAnchorHeight()) - childTopMargin - childBottomMargin;
                    if (restContentHeight == 0) {
                        restContentHeight = MeasureSpec.getSize(heightMeasureSpec);
                    }
                    fixedHeightMeasureSpec = MeasureSpec.makeMeasureSpec(restContentHeight, mode);
                }
                final int childHeightMeasureSpec = getChildMeasureSpec(fixedHeightMeasureSpec,
                        childTopMargin + childBottomMargin, lp.height);

                child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
            }
        }
        setMeasuredDimension(getScreenWidth(), getScreenHeight());
    }

    private void measureWithOutIntercept(int widthMeasureSpec, int heightMeasureSpec) {
        int maxWidth = 0;
        int maxHeight = 0;
        int childState = 0;

        final int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            if (child.getVisibility() != GONE ) {
                if (child==mTarget) {
                    final LayoutParams lp = child.getLayoutParams();
                    int fixedHeightMeasureSpec = heightMeasureSpec;
                    if (mHelper.isClipToScreen() && mHelper.isShowAsDropDown() && lp.height == LayoutParams.MATCH_PARENT) {
                        int mode = MeasureSpec.getMode(heightMeasureSpec);
                        int restContentHeight = getScreenHeight() - (mHelper.getAnchorY() + mHelper.getAnchorHeight()) - childTopMargin - childBottomMargin;
                        if (restContentHeight == 0) {
                            restContentHeight = MeasureSpec.getSize(heightMeasureSpec);
                        }
                        fixedHeightMeasureSpec = MeasureSpec.makeMeasureSpec(restContentHeight, mode);
                    }
                    final int childHeightMeasureSpec = getChildMeasureSpec(fixedHeightMeasureSpec,
                            childTopMargin + childBottomMargin, lp.height);

                    child.measure(widthMeasureSpec, childHeightMeasureSpec);
                }else {
                    measureChild(child, widthMeasureSpec, heightMeasureSpec);
                }
                maxWidth = Math.max(maxWidth, child.getMeasuredWidth());
                maxHeight = Math.max(maxHeight, child.getMeasuredHeight());
                childState = combineMeasuredStates(childState, child.getMeasuredState());
            }
        }
        setMeasuredDimension(resolveSizeAndState(maxWidth, widthMeasureSpec, childState), resolveSizeAndState(maxHeight, heightMeasureSpec,
                childState << MEASURED_HEIGHT_STATE_SHIFT));
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (mHelper.isInterceptTouchEvent()) {
            layoutWithIntercept(l, t, r, b);
        } else {
            layoutWithOutIntercept(l, t, r, b);
        }
    }

    private void layoutWithOutIntercept(int l, int t, int r, int b) {
        final int childCount = getChildCount();
        if (childCount > 0) {
            for (int i = 0; i < childCount; i++) {
                View child = getChildAt(i);
                child.layout(l, t, r, b);
            }
        }
    }

    private boolean hasCallLocation;

    private void layoutWithIntercept(int l, int t, int r, int b) {
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

            boolean delayLayoutMask = mHelper.isAlignBackground() && mHelper.getAlignBackgroundGravity() != Gravity.NO_GRAVITY;

            boolean keepClipScreenTop = false;

            if (child == mMaskLayout) {
                child.layout(childLeft, childTop, childLeft + width, childTop + height);
            } else {
                boolean isRelativeToAnchor = mHelper.isShowAsDropDown();
                int anchorCenterX = mHelper.getAnchorX() + (mHelper.getAnchorViewWidth() >> 1);
                int anchorCenterY = mHelper.getAnchorY() + (mHelper.getAnchorHeight() >> 1);
                //不跟anchorView联系的情况下，gravity意味着在整个view中的方位
                //如果跟anchorView联系，gravity意味着以anchorView为中心的方位
                switch (gravity & Gravity.HORIZONTAL_GRAVITY_MASK) {
                    case Gravity.LEFT:
                    case Gravity.START:
                        if (isRelativeToAnchor) {
                            childLeft = mHelper.getAnchorX() - width + childLeftMargin;
                        } else {
                            childLeft += childLeftMargin;
                        }
                        break;
                    case Gravity.RIGHT:
                    case Gravity.END:
                        if (isRelativeToAnchor) {
                            childLeft = mHelper.getAnchorX() + mHelper.getAnchorViewWidth() + childLeftMargin;
                        } else {
                            childLeft = getMeasuredWidth() - width - childRightMargin;
                        }
                        break;
                    case Gravity.CENTER_HORIZONTAL:
                        if (isRelativeToAnchor) {
                            childLeft = mHelper.getAnchorX();
                            offsetX += anchorCenterX - (childLeft + (width >> 1));
                        } else {
                            childLeft = ((r - l - width) >> 1) + childLeftMargin - childRightMargin;
                        }
                        break;
                    default:
                        if (isRelativeToAnchor) {
                            childLeft = mHelper.getAnchorX() + childLeftMargin;
                        }
                        break;
                }

                switch (gravity & Gravity.VERTICAL_GRAVITY_MASK) {
                    case Gravity.TOP:
                        if (isRelativeToAnchor) {
                            childTop = mHelper.getAnchorY() - height + childTopMargin;
                        } else {
                            childTop += childTopMargin;
                        }
                        break;
                    case Gravity.BOTTOM:
                        if (isRelativeToAnchor) {
                            keepClipScreenTop = true;
                            childTop = mHelper.getAnchorY() + mHelper.getAnchorHeight() + childTopMargin;
                        } else {
                            childTop = b - t - height - childBottomMargin;
                        }
                        break;
                    case Gravity.CENTER_VERTICAL:
                        if (isRelativeToAnchor) {
                            childTop = mHelper.getAnchorY() + mHelper.getAnchorHeight();
                            offsetY += anchorCenterY - (childTop + (height >> 1));
                        } else {
                            childTop = ((b - t - height) >> 1) + childTopMargin - childBottomMargin;
                        }
                        break;
                    default:
                        if (isRelativeToAnchor) {
                            keepClipScreenTop = true;
                            childTop = mHelper.getAnchorY() + mHelper.getAnchorHeight() + childTopMargin;
                        } else {
                            childTop += childTopMargin;
                        }
                        break;
                }

                int left = childLeft + offsetX;
                int top = childTop + offsetY + (mHelper.isFullScreen() ? 0 : -getStatusBarHeight());
                int right = left + width;
                int bottom = top + height;

                //-1:onTop
                //0:non
                //1:onBottom
                int adjustAutoLocatedResult = 0;
                if (mHelper.isAutoLocatePopup()) {
                    //针对高度超过屏幕的适配（一屏幕一半为准）
                    boolean onTop = false;
                    if (height >= getFixedMeasureHeight()) {
                        onTop = anchorCenterY >= (getFixedMeasureHeight() >> 1);
                    } else {
                        int showContentHeight = Math.abs(getFixedMeasureHeight() - top);
                        onTop = bottom > getFixedMeasureHeight() && showContentHeight < top && showContentHeight < height;
                    }
                    if (onTop) {
                        adjustAutoLocatedResult = -1;
                        top += -(mHelper.getAnchorHeight() + height);
                        bottom = top + height;
                        if (!hasCallLocation) {
                            hasCallLocation = true;
                            mHelper.onAnchorTop();
                        }
                    } else {
                        adjustAutoLocatedResult = 1;
                        if (!hasCallLocation) {
                            hasCallLocation = true;
                            mHelper.onAnchorBottom();
                        }
                    }
                }

                boolean isOverFlowScreen = left < 0 || top < 0 || right > getMeasuredWidth() || bottom > getFixedMeasureHeight();
                if (mHelper.isClipToScreen() && isOverFlowScreen) {
                    //将popupContentView限制在屏幕内，跟autoLocate有冲突，因此分开解决。
                    int screenLeft = 0;
                    int screenTop = keepClipScreenTop ? childTop : 0;
                    int screenRight = getMeasuredWidth();
                    int screenBottom = getFixedMeasureHeight();

                    if (adjustAutoLocatedResult != 0) {
                        //adjust的情况下，isRelativeToAnchor必定为true
                        switch (adjustAutoLocatedResult) {
                            case -1:
                                //自动定位到top，则bottom相当于上移
                                screenTop = 0;
                                screenBottom = childTop - mHelper.getAnchorHeight();
                                break;
                            case 1:
                                if (height >= getFixedMeasureHeight()) {
                                    screenTop = childTop;
                                }
                                break;
                        }
                    }

                    int tOffset = 0;
                    if (left < screenLeft) {
                        tOffset = screenLeft - left;
                        if (tOffset <= screenRight - right) {
                            left += tOffset;
                            right = left + width;
                        } else {
                            left = screenLeft;
                        }
                    }

                    if (right > screenRight) {
                        tOffset = right - screenRight;
                        if (tOffset <= left) {
                            //只需要移动left即可
                            left -= tOffset;
                            right = left + width;
                        } else {
                            right = screenRight;
                        }
                    }

                    if (top < screenTop) {
                        tOffset = screenTop - top;
                        if (tOffset <= screenBottom - bottom) {
                            top += tOffset;
                            bottom = top + height;
                        } else {
                            top = screenTop;
                        }
                    }

                    if (bottom > screenBottom) {
                        tOffset = bottom - screenBottom;
                        if (screenTop == 0) {
                            //如果screenTop没有限制，则可以上移，否则只能移到限定的screenTop下
                            top -= tOffset;
                            bottom = top + height;
                        } else {
                            bottom = top + height;
                        }
                    }
                }
                child.layout(left, top, right, bottom);
                if (delayLayoutMask) {
                    mMaskLayout.handleAlignBackground(mHelper.getAlignBackgroundGravity(), left, top, right, bottom);
                }
            }

        }
    }

    private int getFixedMeasureHeight() {
        return mHelper.isFullScreen() ? getMeasuredHeight() : getMeasuredHeight() - getStatusBarHeight();
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
                        PopupLogUtil.trace(LogTag.i, TAG, "dispatchKeyEvent: >>> onBackPressed");
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
                PopupLogUtil.trace(LogTag.i, TAG, "onTouchEvent:[ACTION_DOWN] >>> onOutSideTouch");
                return mHelper.onOutSideTouch();
            }
        } else if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
            if (mHelper != null) {
                PopupLogUtil.trace(LogTag.i, TAG, "onTouchEvent:[ACTION_OUTSIDE] >>> onOutSideTouch");
                return mHelper.onOutSideTouch();
            }
        }
        return super.onTouchEvent(event);
    }

    int getScreenWidth() {
        return PopupUiUtils.getScreenWidthCompat(getContext());
    }

    int getScreenHeight() {
        return PopupUiUtils.getScreenHeightCompat(getContext());
    }

    private int getStatusBarHeight() {
        checkStatusBarHeight(getContext());
        return statusBarHeight;
    }

    private void checkStatusBarHeight(Context context) {
        if (statusBarHeight != 0 || context == null) return;
        int result = 0;
        //获取状态栏高度的资源id
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        statusBarHeight = result;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mHelper.registerKeyboardStateChangeListener(null);
    }

    @Override
    public boolean shouldDelayChildPressedState() {
        return false;
    }


    public void updateLayout() {
        if (mMaskLayout!=null) {
            mMaskLayout.update();
        }
        if (isLayoutRequested()) {
            requestLayout();
        }
    }

    //-----------------------------------------keyboard-----------------------------------------
    private Rect viewRect = new Rect();
    private int offset;

    @Override
    public void onKeyboardChange(int keyboardHeight, boolean isVisible) {
        if (mHelper.getSoftInputMode() == WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN ||
                mHelper.getSoftInputMode() == WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE) {
            View focusView = findFocus();
            if (focusView == null) return;
            focusView.getGlobalVisibleRect(viewRect);
            if (isVisible) {
                int keyboardTop = getScreenHeight() - keyboardHeight;
                int targetBottomOffset = mTarget.getBottom() - keyboardTop;
                boolean alignToDecorView = targetBottomOffset > 0 && viewRect.top >= targetBottomOffset;
                if (alignToDecorView) {
                    offset = targetBottomOffset;
                } else {
                    if (viewRect.bottom > keyboardTop) {
                        offset = viewRect.bottom - keyboardTop;
                    }
                }
            } else {
                offset = 0;
            }
            if (mHelper.getEventInterceptor() != null) {
                int customOffset = mHelper.getEventInterceptor().onKeyboardChangeResult(keyboardHeight, isVisible, offset);
                if (customOffset != 0) {
                    offset = customOffset;
                }
            }
            mTarget.animate()
                    .translationY(-offset)
                    .setDuration(300)
                    .start();
            PopupLogUtil.trace("onKeyboardChange : isVisible = " + isVisible + "  offset = " + offset);
        }
    }
}
