package razerdp.basepopup;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import razerdp.util.PopupUiUtils;
import razerdp.util.PopupUtils;
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
    private CheckAndCallAutoAnchorLocate mCheckAndCallAutoAnchorLocate;
    private WindowManagerProxy mWindowManagerProxy;

    private Flag mFlag = new Flag();

    private PopupDecorViewProxy(Context context) {
        this(context, null);
    }

    private PopupDecorViewProxy(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    private PopupDecorViewProxy(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public static PopupDecorViewProxy create(Context context, WindowManagerProxy windowManagerProxy, BasePopupHelper helper) {
        PopupDecorViewProxy result = new PopupDecorViewProxy(context);
        result.init(helper, windowManagerProxy);
        return result;
    }

    @SuppressLint("ClickableViewAccessibility")
    private void init(BasePopupHelper helper, WindowManagerProxy managerProxy) {
        mWindowManagerProxy = managerProxy;
        mHelper = helper;
        mHelper.registerKeyboardStateChangeListener(this);
        setClipChildren(mHelper.isClipChildren());
        mMaskLayout = PopupMaskLayout.create(getContext(), mHelper);
        if (mHelper.isInterceptTouchEvent()) {
            setLayoutParams(new ViewGroup.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
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
                                    View contentView = mTarget.findViewById(mHelper.getContentRootId());
                                    if (contentView == null) {
                                        mTarget.getGlobalVisibleRect(mTouchRect);
                                    } else {
                                        contentView.getGlobalVisibleRect(mTouchRect);
                                    }
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
            Activity act = PopupUtils.scanForActivity(getContext(), 50);
            if (act == null) return;
            checkAndClearDecorMaskLayout(act);
            addMaskToDecor(act.getWindow());
        }
    }

    private void checkAndClearDecorMaskLayout(Activity act) {
        if (act == null || act.getWindow() == null || act.getWindow().getDecorView() == null)
            return;
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
        mFlag.reset();
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
                measureWrappedDecorView(child, widthMeasureSpec, heightMeasureSpec);
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
        boolean isAlignAnchorMode = mHelper.getGravityMode() == BasePopupWindow.GravityMode.ALIGN_TO_ANCHOR;

        widthSize -= Math.max(0, childLeftMargin) + Math.max(0, childRightMargin);
        heightSize -= Math.max(0, childTopMargin) + Math.max(0, childBottomMargin);


        //以下针对突破屏幕的内容进行重新测量，不再针对wrap_content和match_parent进行区分
        boolean needCheckClipToScreen = mHelper.isClipToScreen() && mHelper.isShowAsDropDown();

        if (needCheckClipToScreen) {
            int restWidth;

            switch (gravity & Gravity.HORIZONTAL_GRAVITY_MASK) {
                case Gravity.RIGHT:
                    restWidth = isAlignAnchorMode ?
                            mHelper.getAnchorX() + mHelper.getAnchorWidth() - childLeftMargin - childRightMargin :
                            getScreenWidth() - (mHelper.getAnchorX() + mHelper.getAnchorWidth()) - childLeftMargin - childRightMargin;
                    break;
                case Gravity.LEFT:
                default:
                    restWidth = isAlignAnchorMode ?
                            getScreenWidth() - mHelper.getAnchorX() - childLeftMargin - childRightMargin :
                            mHelper.getAnchorX() - childLeftMargin - childRightMargin;
                    break;
            }

            if (restWidth <= 0) {
                Log.e(TAG, "BasePopup 可用展示空间小于或等于0，宽度将按原测量值设定，不进行调整适配");
                restWidth = widthSize;
                mFlag.compute(Flag.FLAG_REST_WIDTH_NOT_ENOUGHT);
            } else {
                restWidth = PopupUtils.range(restWidth, mHelper.getMinWidth(), restWidth);
            }
            if (widthSize > restWidth) {
                widthSize = restWidth;
                mFlag.compute(Flag.FLAG_REST_WIDTH_NOT_ENOUGHT);
            }
        }

        if (mHelper.getMaxWidth() > 0 && widthSize > mHelper.getMaxWidth()) {
            widthSize = mHelper.getMaxWidth();
        }


        if (needCheckClipToScreen) {
            //剩余可用空间
            int resetHeight;

            switch (gravity & Gravity.VERTICAL_GRAVITY_MASK) {
                case Gravity.TOP:
                    resetHeight = isAlignAnchorMode ?
                            getScreenHeight() - mHelper.getAnchorY() - childTopMargin - childBottomMargin :
                            mHelper.getAnchorY() - childTopMargin - childBottomMargin;
                    break;
                case Gravity.BOTTOM:
                default:
                    resetHeight = isAlignAnchorMode ?
                            mHelper.getAnchorY() + mHelper.getAnchorHeight() - childTopMargin - childBottomMargin :
                            getScreenHeight() - (mHelper.getAnchorY() + mHelper.getAnchorHeight()) - childTopMargin - childBottomMargin;
                    break;
            }

            if (resetHeight <= 0) {
                Log.e(TAG, "BasePopup 可用展示空间小于或等于0，高度将按原测量值设定，不进行调整适配");
                resetHeight = heightSize;
                mFlag.compute(Flag.FLAG_REST_HEIGHT_NOT_ENOUGHT);
            } else {
                resetHeight = PopupUtils.range(resetHeight, mHelper.getMinHeight(), resetHeight);
            }

            if (heightSize > resetHeight) {
                heightSize = resetHeight;
                mFlag.compute(Flag.FLAG_REST_HEIGHT_NOT_ENOUGHT);
            }
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
        if (mHelper.isInterceptTouchEvent()) {
            layoutWithIntercept(l, t, r, b);
        } else {
            layoutWithOutIntercept(l, t, r, b);
        }
    }

    private void layoutWithOutIntercept(int l, int t, int r, int b) {
        if (mFlag.contains(Flag.FLAG_WINDOW_PARAMS_FIT_REQUEST) && getLayoutParams() instanceof WindowManager.LayoutParams) {
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
    private void layoutWithIntercept(int l, int t, int r, int b) {
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

            boolean keepClipScreenTop = false;

            if (child == mMaskLayout) {
                child.layout(childLeft, childTop, childLeft + width, childTop + height);
            } else {
                boolean isRelativeToAnchor = mHelper.isShowAsDropDown();
                boolean isAlignAnchorMode = mHelper.getGravityMode() == BasePopupWindow.GravityMode.ALIGN_TO_ANCHOR;
                int anchorCenterX = mHelper.getAnchorX() + (mHelper.getAnchorWidth() >> 1);
                int anchorCenterY = mHelper.getAnchorY() + (mHelper.getAnchorHeight() >> 1);

                //不跟anchorView联系的情况下，gravity意味着在整个view中的方位
                //如果跟anchorView联系，gravity意味着以anchorView为中心的方位
                switch (gravity & Gravity.HORIZONTAL_GRAVITY_MASK) {
                    case Gravity.LEFT:
                        if (isRelativeToAnchor) {
                            childLeft = isAlignAnchorMode ? mHelper.getAnchorX() : mHelper.getAnchorX() - width;
                        }
                        break;
                    case Gravity.RIGHT:
                        if (isRelativeToAnchor) {
                            childLeft = isAlignAnchorMode ? mHelper.getAnchorX() + mHelper.getAnchorWidth() - width : mHelper.getAnchorX() + mHelper.getAnchorWidth();
                        } else {
                            childLeft = getMeasuredWidth() - width;
                        }
                        break;
                    case Gravity.CENTER_HORIZONTAL:
                        if (isRelativeToAnchor) {
                            childLeft = mHelper.getAnchorX();
                            offsetX += anchorCenterX - (childLeft + (width >> 1));
                        } else {
                            childLeft = ((r - l - width) >> 1);
                        }
                        break;
                    default:
                        if (isRelativeToAnchor) {
                            childLeft = mHelper.getAnchorX();
                        }
                        break;
                }
                childLeft = childLeft + childLeftMargin - childRightMargin;

                switch (gravity & Gravity.VERTICAL_GRAVITY_MASK) {
                    case Gravity.TOP:
                        if (isRelativeToAnchor) {
                            childTop = isAlignAnchorMode ? mHelper.getAnchorY() : mHelper.getAnchorY() - height;
                        }
                        break;
                    case Gravity.BOTTOM:
                        if (isRelativeToAnchor) {
                            keepClipScreenTop = true;
                            childTop = isAlignAnchorMode ? mHelper.getAnchorY() + mHelper.getAnchorHeight() - height : mHelper.getAnchorY() + mHelper.getAnchorHeight();
                        } else {
                            childTop = b - t - height;
                        }
                        break;
                    case Gravity.CENTER_VERTICAL:
                        if (isRelativeToAnchor) {
                            childTop = mHelper.getAnchorY() + mHelper.getAnchorHeight();
                            offsetY += anchorCenterY - (childTop + (height >> 1));
                        } else {
                            childTop = ((b - t - height) >> 1);
                        }
                        break;
                    default:
                        if (isRelativeToAnchor) {
                            keepClipScreenTop = true;
                            childTop = mHelper.getAnchorY() + mHelper.getAnchorHeight();
                        }
                        break;
                }
                childTop = childTop + childTopMargin - childBottomMargin;

                if (mHelper.isAutoLocatePopup()) {
                    int restHeight = getMeasuredHeight() - (mHelper.getAnchorY() + mHelper.getAnchorHeight()) + childTopMargin - childBottomMargin;
                    if (restHeight >= height) {
                        mFlag.compute(Flag.FLAG_ON_ANCHOR_BOTTOM);
                        postAnchorLocation(false);
                    } else {
                        mFlag.compute(Flag.FLAG_ON_ANCHOR_TOP);
                        offsetY -= mHelper.getAnchorHeight() + height;
                        postAnchorLocation(true);
                    }
                }

                int left = childLeft + offsetX;
                int top = childTop + offsetY + (mHelper.isFullScreen() ? 0 : -getStatusBarHeight());
                int right = left + width;
                int bottom = top + height;


                boolean isOverFlowScreen = left < 0 || top < 0 || right > getMeasuredWidth() || bottom > getMeasuredHeight();

                if (mHelper.isClipToScreen() && isOverFlowScreen) {
                    //将popupContentView限制在屏幕内，跟autoLocate有冲突，因此分开解决。
                    int windowLeft = 0;
                    int windowTop = keepClipScreenTop ? childTop : (mHelper.isFullScreen() ? 0 : getStatusBarHeight());
                    int windowRight = getMeasuredWidth();
                    int windowBottom = getMeasuredHeight();

                    if (mFlag.contains(Flag.FLAG_ANCHOR_AUTO_LOCATED_MASK)) {
                        //autoLocate意味着
                        switch (mFlag.flag & Flag.FLAG_ANCHOR_AUTO_LOCATED_MASK) {
                            case Flag.FLAG_ON_ANCHOR_TOP:
                                //如果是自动定位到上方，则可显示的window区域为[0,anchor顶部]
                                windowTop = 0;
                                windowBottom = mHelper.getAnchorY() - childTopMargin;
                                break;
                            case Flag.FLAG_ON_ANCHOR_BOTTOM:
                                //如果自动定位到下方，则可显示的window区域为[anchor底部，屏幕底部]
                                windowTop = mHelper.getAnchorY() + mHelper.getAnchorHeight() + childTopMargin;
                                break;
                        }
                    }

                    int tOffset = 0;


                    if (left < windowLeft) {
                        tOffset = windowLeft - left;
                        if (tOffset <= windowRight - right) {
                            left += tOffset;
                            right = left + width;
                        } else {
                            left = windowLeft;
                        }
                    }

                    if (right > windowRight) {
                        tOffset = right - windowRight;
                        if (tOffset <= left) {
                            //只需要移动left即可
                            left -= tOffset;
                            right = left + width;
                        } else {
                            right = windowRight;
                        }
                    }

                    if (top < windowTop) {
                        tOffset = windowTop - top;
                        if (tOffset <= windowBottom - bottom) {
                            top += tOffset;
                            bottom = top + height;
                        } else {
                            top = windowTop;
                        }
                    }

                    if (bottom > windowBottom) {
                        tOffset = bottom - windowBottom;
                        if (windowTop == 0) {
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


    public void fitWindowParams(WindowManager.LayoutParams params) {
        if (getMeasuredWidth() == 0 || getMeasuredHeight() == 0) {
            mFlag.compute(Flag.FLAG_WINDOW_PARAMS_FIT_REQUEST);
            return;
        }
        int offsetX = 0;
        int offsetY = 0;

        // offset需要自行计算，不采用系统方法了
        switch (mHelper.getPopupGravity() & Gravity.HORIZONTAL_GRAVITY_MASK) {
            case Gravity.LEFT:
                offsetX += mHelper.isShowAsDropDown() ? mHelper.getAnchorX() - getMeasuredWidth() : 0;
                break;
            case Gravity.RIGHT:
                offsetX += mHelper.isShowAsDropDown() ? mHelper.getAnchorX() + mHelper.getAnchorWidth() : getScreenWidth() - getMeasuredWidth();
                break;
            case Gravity.CENTER_HORIZONTAL:
                offsetX += mHelper.isShowAsDropDown() ? mHelper.getAnchorX() + ((mHelper.getAnchorWidth() - getMeasuredWidth()) >> 1)
                        : (getScreenWidth() - getMeasuredWidth()) >> 1;
                break;
            default:
                break;
        }
        offsetX = offsetX + childLeftMargin - childRightMargin;

        switch (mHelper.getPopupGravity() & Gravity.VERTICAL_GRAVITY_MASK) {
            case Gravity.TOP:
                offsetY += mHelper.isShowAsDropDown() ? mHelper.getAnchorY() - getMeasuredHeight() : 0;
                break;
            case Gravity.BOTTOM:
                offsetY += mHelper.isShowAsDropDown() ? mHelper.getAnchorY() + mHelper.getAnchorHeight() : getScreenHeight() - getMeasuredHeight();
                break;
            case Gravity.CENTER_VERTICAL:
                offsetY += mHelper.isShowAsDropDown() ? mHelper.getAnchorY() + ((mHelper.getAnchorHeight() - getMeasuredHeight()) >> 1) : (getScreenHeight() - getMeasuredHeight()) >> 1;
                break;
            default:
                break;
        }
        offsetY = offsetY + childTopMargin - childBottomMargin;

        PopupLogUtil.trace("fitWindowParams  ::  " +
                "{\nscreenWidth = " + getScreenWidth() +
                "\nscreenHeight = " + getScreenHeight() +
                "\nanchorX = " + mHelper.getAnchorX() +
                "\nanchorY = " + mHelper.getAnchorY() +
                "\nviewWidth = " + getMeasuredWidth() +
                "\nviewHeight = " + getMeasuredHeight() +
                "\noffsetX = " + offsetX +
                "\noffsetY = " + offsetY +
                "\n}");

        if (mHelper.isAutoLocatePopup()) {
            boolean isPositionMode = mHelper.getShowMode() == BasePopupHelper.ShowMode.POSITION;
            offsetY = isPositionMode ? 0 : offsetY;
            final boolean onTop = (getScreenHeight() - (mHelper.getAnchorY() + offsetY)) < getMeasuredHeight();
            if (onTop) {
                if (isPositionMode) {
                    offsetY += ((mHelper.getPopupGravity() & Gravity.VERTICAL_GRAVITY_MASK) == Gravity.CENTER_VERTICAL) ?
                            -getMeasuredHeight() >> 1 : -getMeasuredHeight();
                } else {
                    offsetY = -mHelper.getAnchorHeight() - getMeasuredHeight() - offsetY;
                }
                postAnchorLocation(true);
            } else {
                postAnchorLocation(false);
            }
        }
        params.x = offsetX + mHelper.getOffsetX();
        params.y = offsetY + mHelper.getOffsetY();
        mFlag.remove(Flag.FLAG_WINDOW_PARAMS_FIT_REQUEST);
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
        int screenWidth = PopupUiUtils.getScreenWidthCompat(getContext());
        PopupLogUtil.trace("autoSize  width = " + screenWidth);
        return screenWidth;
    }

    int getScreenHeight() {
        int screenHeight = PopupUiUtils.getScreenHeightCompat(getContext());
        PopupLogUtil.trace("autoSize  height = " + screenHeight);
        return screenHeight;
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
        if (!mHelper.isInterceptTouchEvent()) {
            if (mMaskLayout != null && mMaskLayout.getParent() != null) {
                ((ViewGroup) mMaskLayout.getParent()).removeViewInLayout(mMaskLayout);
            }
        }
        mHelper.registerKeyboardStateChangeListener(null);
        if (mCheckAndCallAutoAnchorLocate != null) {
            removeCallbacks(mCheckAndCallAutoAnchorLocate);
            mCheckAndCallAutoAnchorLocate = null;
        }
    }

    @Override
    public boolean shouldDelayChildPressedState() {
        return false;
    }


    public void updateLayout() {
        if (mMaskLayout != null) {
            mMaskLayout.update();
        }
        if (isLayoutRequested()) {
            requestLayout();
        }
    }

    private void removeAnchorLocationChecker() {
        if (mCheckAndCallAutoAnchorLocate != null) {
            removeCallbacks(mCheckAndCallAutoAnchorLocate);
        }
    }

    private void postAnchorLocation(boolean onTop) {
        if (mCheckAndCallAutoAnchorLocate == null) {
            mCheckAndCallAutoAnchorLocate = new CheckAndCallAutoAnchorLocate(onTop);
        } else {
            removeAnchorLocationChecker();
        }
        mCheckAndCallAutoAnchorLocate.onTop = onTop;
        postDelayed(mCheckAndCallAutoAnchorLocate, 32);
    }

    private final class CheckAndCallAutoAnchorLocate implements Runnable {
        private boolean onTop;
        private boolean hasCalled;

        public CheckAndCallAutoAnchorLocate(boolean onTop) {
            this.onTop = onTop;
        }

        @Override
        public void run() {
            if (mHelper == null || hasCalled) return;
            if (onTop) {
                mHelper.onAnchorTop();
            } else {
                mHelper.onAnchorBottom();
            }
            hasCalled = true;
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


    static class Flag {
        static final int IDLE = 0;
        static final int FLAG_REST_WIDTH_NOT_ENOUGHT = 0x00000001;
        static final int FLAG_REST_HEIGHT_NOT_ENOUGHT = 0x00000010;
        static final int FLAG_WINDOW_PARAMS_FIT_REQUEST = 0x00000100;

        static final int FLAG_ON_ANCHOR_TOP = 0x00001000;
        static final int FLAG_ON_ANCHOR_BOTTOM = 0x00010000;
        static final int FLAG_ANCHOR_AUTO_LOCATED_MASK = FLAG_ON_ANCHOR_TOP | FLAG_ON_ANCHOR_BOTTOM;

        int flag;

        void compute(int mFlag) {
            flag |= mFlag;
        }

        void remove(int mFlag) {
            flag &= ~mFlag;
        }

        void reset() {
            flag = IDLE;
        }

        boolean contains(int mFlag) {
            return (flag & mFlag) != 0;
        }
    }
}
