/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2017 razerdp
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package razerdp.basepopup;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.PopupWindow;

import java.lang.reflect.Field;

import razerdp.library.R;
import razerdp.util.InputMethodUtils;
import razerdp.util.SimpleAnimUtil;

/**
 * Created by 大灯泡 on 2016/1/14.
 * <p>
 * 抽象通用popupwindow的父类
 */
public abstract class BasePopupWindow implements BasePopup, PopupWindow.OnDismissListener, PopupController {
    private static final String TAG = "BasePopupWindow";
    //元素定义
    private PopupWindowProxy mPopupWindow;
    //popup视图
    private View mPopupView;
    private Activity mContext;
    protected View mAnimaView;
    protected View mDismissView;
    //是否自动弹出输入框(default:false)
    private boolean autoShowInputMethod = false;
    private OnDismissListener mOnDismissListener;
    private OnBeforeShowCallback mOnBeforeShowCallback;
    //anima
    private Animation mShowAnimation;
    private Animator mShowAnimator;
    private Animation mExitAnimation;
    private Animator mExitAnimator;

    private boolean isExitAnimaPlaying = false;
    private boolean needPopupFadeAnima = true;

    //option
    private int popupGravity = Gravity.NO_GRAVITY;
    private int offsetX;
    private int offsetY;
    private int popupViewWidth;
    private int popupViewHeight;
    //锚点view的location
    private int[] mAnchorViewLocation;
    //是否参考锚点
    private boolean relativeToAnchorView;
    //是否自动适配popup的位置
    private boolean isAutoLocatePopup;
    //showasdropdown
    private boolean showAtDown;
    //点击popup外部是否消失
    private boolean dismissWhenTouchOuside;

    private int popupLayoutid;

    public BasePopupWindow(Activity context) {
        initView(context, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    }

    public BasePopupWindow(Activity context, int w, int h) {
        initView(context, w, h);
    }

    private void initView(Activity context, int w, int h) {
        mContext = context;

        mPopupView = onCreatePopupView();
        mAnimaView = initAnimaView();
        if (mAnimaView != null) {
            popupLayoutid = mAnimaView.getId();
        }
        checkPopupAnimaView();

        //默认占满全屏
        mPopupWindow = new PopupWindowProxy(mPopupView, w, h, this);
        mPopupWindow.setOnDismissListener(this);
        setDismissWhenTouchOuside(true);

        preMeasurePopupView(w, h);

        //默认是渐入动画
        setNeedPopupFade(Build.VERSION.SDK_INT <= 22);

        //=============================================================为外层的view添加点击事件，并设置点击消失
        mDismissView = getClickToDismissView();
        if (mDismissView != null && !(mDismissView instanceof AdapterView)) {
            mDismissView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });
        }
        if (mAnimaView != null && !(mAnimaView instanceof AdapterView)) {
            mAnimaView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
        //=============================================================元素获取
        mShowAnimation = initShowAnimation();
        mShowAnimator = initShowAnimator();
        mExitAnimation = initExitAnimation();
        mExitAnimator = initExitAnimator();

        mAnchorViewLocation = new int[2];
    }

    private void checkPopupAnimaView() {
        //处理popupview与animaview相同的情况
        //当popupView与animaView相同的时候，处理位置信息会出问题，因此这里需要对mAnimaView再包裹一层
        if (mPopupView != null && mAnimaView != null && mPopupView == mAnimaView) {
            try {
                mPopupView = new FrameLayout(getContext());
                if (popupLayoutid == 0) {
                    ((FrameLayout) mPopupView).addView(mAnimaView);
                } else {
                    mAnimaView = View.inflate(getContext(), popupLayoutid, (FrameLayout) mPopupView);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void preMeasurePopupView(int w, int h) {
        if (mPopupView != null) {
            //修复可能出现的android 4.3的measure空指针问题
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                int contentViewHeight = ViewGroup.LayoutParams.MATCH_PARENT;
                final ViewGroup.LayoutParams layoutParams = mPopupView.getLayoutParams();
                if (layoutParams != null && layoutParams.height == ViewGroup.LayoutParams.WRAP_CONTENT) {
                    contentViewHeight = ViewGroup.LayoutParams.WRAP_CONTENT;
                }
                ViewGroup.LayoutParams p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, contentViewHeight);
                mPopupView.setLayoutParams(p);
            }
            mPopupView.measure(w, h);
            popupViewWidth = mPopupView.getMeasuredWidth();
            popupViewHeight = mPopupView.getMeasuredHeight();
            mPopupView.setFocusableInTouchMode(true);
        }
    }


    //------------------------------------------抽象-----------------------------------------------

    /**
     * PopupWindow展示出来后，需要执行动画的View.一般为蒙层之上的View
     */
    protected abstract Animation initShowAnimation();

    /**
     * 设置一个点击后触发dismiss PopupWindow的View，一般为蒙层
     */
    public abstract View getClickToDismissView();

    /**
     * 设置展示动画View的属性动画
     */
    protected Animator initShowAnimator() {
        return null;
    }

    /**
     * 设置一个拥有输入功能的View，一般为EditTextView
     */
    public EditText getInputView() {
        return null;
    }

    /**
     * 设置PopupWindow销毁时的退出动画
     */
    protected Animation initExitAnimation() {
        return null;
    }

    /**
     * 设置PopupWindow销毁时的退出属性动画
     */
    protected Animator initExitAnimator() {
        return null;
    }

    /**
     * popupwindow是否需要淡入淡出
     */
    public void setNeedPopupFade(boolean needPopupFadeAnima) {
        this.needPopupFadeAnima = needPopupFadeAnima;
        setPopupAnimaStyle(needPopupFadeAnima ? R.style.PopupAnimaFade : 0);
    }

    public boolean getNeedPopupFade() {
        return needPopupFadeAnima;
    }

    /**
     * 设置popup的动画style
     */
    public void setPopupAnimaStyle(int animaStyleRes) {
        mPopupWindow.setAnimationStyle(animaStyleRes);
    }

    //------------------------------------------showPopup-----------------------------------------------

    /**
     * 调用此方法时，PopupWindow将会显示在DecorView
     */
    public void showPopupWindow() {
        if (checkPerformShow(null)) {
            this.showAtDown = false;
            this.relativeToAnchorView = false;
            tryToShowPopup(null);
        }
    }

    /**
     * 调用此方法时，PopupWindow左上角将会与anchorview左上角对齐
     *
     * @param anchorViewResid
     */
    public void showPopupWindow(int anchorViewResid) {
        View v = mContext.findViewById(anchorViewResid);
        showPopupWindow(v);
    }

    /**
     * 调用此方法时，PopupWindow左上角将会与anchorview左上角对齐
     *
     * @param v
     */
    public void showPopupWindow(View v) {
        if (checkPerformShow(v)) {
            this.showAtDown = true;
            this.relativeToAnchorView = true;
            tryToShowPopup(v);
        }
    }

    //------------------------------------------Methods-----------------------------------------------
    private void tryToShowPopup(View v) {
        try {
            int offset[];
            //传递了view
            if (v != null) {
                offset = calcuateOffset(v);
                if (showAtDown) {
                    mPopupWindow.showAsDropDown(v, offset[0], offset[1]);
                } else {
                    mPopupWindow.showAtLocation(v, popupGravity, offset[0], offset[1]);
                }
            } else {
                //什么都没传递，取顶级view的id
                mPopupWindow.showAtLocation(mContext.findViewById(android.R.id.content), popupGravity, offsetX, offsetY);
            }
            if (mShowAnimation != null && mAnimaView != null) {
                mAnimaView.clearAnimation();
                mAnimaView.startAnimation(mShowAnimation);
            }
            if (mShowAnimation == null && mShowAnimator != null && mAnimaView != null) {
                mShowAnimator.start();
            }
            //自动弹出键盘
            if (autoShowInputMethod && getInputView() != null) {
                getInputView().requestFocus();
                InputMethodUtils.showInputMethod(getInputView(), 150);
            }
        } catch (Exception e) {
            Log.e(TAG, "show error");
            e.printStackTrace();
        }
    }


    /**
     * 暂时还不是很稳定，需要进一步测试优化
     *
     * @param anchorView
     * @return
     * @see #showPopupWindow(View)
     */
    private int[] calcuateOffset(View anchorView) {
        int[] offset = {offsetX, offsetY};
        anchorView.getLocationOnScreen(mAnchorViewLocation);
        if (isAutoLocatePopup) {
            final boolean onTop = (getScreenHeight() - (mAnchorViewLocation[1] + offset[1]) < getHeight());
            if (onTop) {
                offset[1] = -anchorView.getHeight() - getHeight() - offset[1];
                showOnTop(mPopupView);
            } else {
                showOnDown(mPopupView);
            }
        }
        return offset;

    }

    /**
     * PopupWindow是否需要自适应输入法，为输入法弹出让出区域
     *
     * @param needAdjust <br>
     *                   ture for "SOFT_INPUT_ADJUST_RESIZE" mode<br>
     *                   false for "SOFT_INPUT_ADJUST_NOTHING" mode
     */
    public void setAdjustInputMethod(boolean needAdjust) {
        if (needAdjust) {
            mPopupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        } else {
            mPopupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        }
    }

    /**
     * 当PopupWindow展示的时候，这个参数决定了是否自动弹出输入法
     * 如果使用这个方法，您必须保证通过 <strong>getInputView()<strong/>得到一个EditTextView
     */
    public void setAutoShowInputMethod(boolean autoShow) {
        this.autoShowInputMethod = autoShow;
        if (autoShow) {
            setAdjustInputMethod(true);
        } else {
            setAdjustInputMethod(false);
        }
    }

    /**
     * 这个参数决定点击返回键是否可以取消掉PopupWindow
     */
    public void setBackPressEnable(boolean backPressEnable) {
        if (backPressEnable) {
            mPopupWindow.setBackgroundDrawable(new ColorDrawable());
        } else {
            mPopupWindow.setBackgroundDrawable(null);
        }
    }

    /**
     * 这个方法封装了LayoutInflater.from(context).inflate，方便您设置PopupWindow所用的xml
     *
     * @param resId reference of layout
     * @return root View of the layout
     */
    public View createPopupById(int resId) {
        if (resId != 0) {
            popupLayoutid = resId;
            return LayoutInflater.from(mContext).inflate(resId, null);
        } else {
            return null;
        }
    }

    protected View findViewById(int id) {
        if (mPopupView != null && id != 0) {
            return mPopupView.findViewById(id);
        }
        return null;
    }

    /**
     * 是否允许popupwindow覆盖屏幕（包含状态栏）
     */
    public void setPopupWindowFullScreen(boolean needFullScreen) {
        fitPopupWindowOverStatusBar(needFullScreen);
    }

    /**
     * 这个方法用于简化您为View设置OnClickListener事件，多个View将会使用同一个点击事件
     */
    protected void setViewClickListener(View.OnClickListener listener, View... views) {
        for (View view : views) {
            if (view != null && listener != null) {
                view.setOnClickListener(listener);
            }
        }
    }

    private void fitPopupWindowOverStatusBar(boolean needFullScreen) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            try {
                Field mLayoutInScreen = PopupWindow.class.getDeclaredField("mLayoutInScreen");
                mLayoutInScreen.setAccessible(true);
                mLayoutInScreen.set(mPopupWindow, needFullScreen);
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
    //------------------------------------------Getter/Setter-----------------------------------------------

    /**
     * PopupWindow是否处于展示状态
     */
    public boolean isShowing() {
        return mPopupWindow.isShowing();
    }

    public OnDismissListener getOnDismissListener() {
        return mOnDismissListener;
    }

    public void setOnDismissListener(OnDismissListener onDismissListener) {
        mOnDismissListener = onDismissListener;
    }

    public OnBeforeShowCallback getOnBeforeShowCallback() {
        return mOnBeforeShowCallback;
    }

    public void setOnBeforeShowCallback(OnBeforeShowCallback mOnBeforeShowCallback) {
        this.mOnBeforeShowCallback = mOnBeforeShowCallback;
    }

    public void setShowAnimation(Animation showAnimation) {
        if (mShowAnimation != null && mAnimaView != null) {
            mAnimaView.clearAnimation();
            mShowAnimation.cancel();
        }
        if (showAnimation != null && showAnimation != mShowAnimation) {
            mShowAnimation = showAnimation;
        }
    }

    public Animation getShowAnimation() {
        return mShowAnimation;
    }

    public void setShowAnimator(Animator showAnimator) {
        if (mShowAnimator != null) mShowAnimator.cancel();
        if (showAnimator != null && showAnimator != mShowAnimator) {
            mShowAnimator = showAnimator;
        }
    }

    public Animator getShowAnimator() {
        return mShowAnimator;
    }

    public void setExitAnimation(Animation exitAnimation) {
        if (mExitAnimation != null && mAnimaView != null) {
            mAnimaView.clearAnimation();
            mExitAnimation.cancel();
        }
        if (exitAnimation != null && exitAnimation != mExitAnimation) {
            mExitAnimation = exitAnimation;
        }
    }

    public Animation getExitAnimation() {
        return mExitAnimation;
    }

    public void setExitAnimator(Animator exitAnimator) {
        if (mExitAnimator != null) mExitAnimator.cancel();
        if (exitAnimator != null && exitAnimator != mExitAnimator) {
            mExitAnimator = exitAnimator;
        }
    }

    public Animator getExitAnimator() {
        return mExitAnimator;
    }

    public Activity getContext() {
        return mContext;
    }

    /**
     * 获取popupwindow的根布局
     *
     * @return
     */
    public View getPopupWindowView() {
        return mPopupView;
    }

    /**
     * 获取popupwindow实例
     *
     * @return
     */
    public PopupWindow getPopupWindow() {
        return mPopupWindow;
    }

    public int getOffsetX() {
        return offsetX;
    }

    /**
     * 设定x位置的偏移量(中心点在popup的左上角)
     * <p>
     *
     * @param offsetX
     */
    public void setOffsetX(int offsetX) {
        this.offsetX = offsetX;
    }

    public int getOffsetY() {
        return offsetY;
    }

    /**
     * 设定y位置的偏移量(中心点在popup的左上角)
     *
     * @param offsetY
     */
    public void setOffsetY(int offsetY) {
        this.offsetY = offsetY;
    }

    public int getPopupGravity() {
        return popupGravity;
    }

    /**
     * 设置参考点，一般情况下，参考对象指的不是指定的view，而是它的windoToken，可以看作为整个screen
     *
     * @param popupGravity
     */
    public void setPopupGravity(int popupGravity) {
        this.popupGravity = popupGravity;
    }

    public boolean isRelativeToAnchorView() {
        return relativeToAnchorView;
    }

    /**
     * 是否参考锚点view，如果是true，则会显示到跟指定view的x,y一样的位置(如果空间足够的话)
     *
     * @param relativeToAnchorView
     * @deprecated
     */
    public void setRelativeToAnchorView(boolean relativeToAnchorView) {
        setShowAtDown(true);
        this.relativeToAnchorView = relativeToAnchorView;
    }

    public boolean isAutoLocatePopup() {
        return isAutoLocatePopup;
    }

    public void setAutoLocatePopup(boolean autoLocatePopup) {
        setShowAtDown(true);
        isAutoLocatePopup = autoLocatePopup;
    }

    /**
     * 获取poupwindow的高度，当popupwindow没show出来的时候高度会是0，此时则返回pre measure的高度，不一定精准
     *
     * @return
     */
    public int getHeight() {
        int height = mPopupView.getHeight();
        return height <= 0 ? popupViewHeight : height;
    }

    /**
     * 获取poupwindow的宽度，当popupwindow没show出来的时候高度会是0，此时则返回pre measure的宽度，不一定精准
     *
     * @return
     */
    public int getWidth() {
        int width = mPopupView.getWidth();
        return width <= 0 ? popupViewWidth : width;
    }

    public boolean isShowAtDown() {
        return showAtDown;
    }

    /**
     * 决定使用showAtLocation还是showAsDropDown
     * decide showAtLocation/showAsDropDown
     *
     * @param showAtDown
     * @deprecated
     */
    public void setShowAtDown(boolean showAtDown) {
        this.showAtDown = showAtDown;
    }

    /**
     * 点击外部是否消失
     * <p>
     * dismiss popup when touch ouside from popup
     *
     * @param dismissWhenTouchOuside true for dismiss
     */
    public void setDismissWhenTouchOuside(boolean dismissWhenTouchOuside) {
        this.dismissWhenTouchOuside = dismissWhenTouchOuside;
        if (dismissWhenTouchOuside) {
            //指定透明背景，back键相关
            mPopupWindow.setFocusable(true);
            mPopupWindow.setOutsideTouchable(true);
            mPopupWindow.setBackgroundDrawable(new ColorDrawable());
        } else {
            mPopupWindow.setFocusable(false);
            mPopupWindow.setOutsideTouchable(false);
            mPopupWindow.setBackgroundDrawable(null);
        }

    }

    public boolean isDismissWhenTouchOuside() {
        return dismissWhenTouchOuside;
    }

    //------------------------------------------状态控制-----------------------------------------------

    /**
     * 取消一个PopupWindow，如果有退出动画，PopupWindow的消失将会在动画结束后执行
     */
    public void dismiss() {
        try {
            mPopupWindow.dismiss();
        } catch (Exception e) {
            Log.e(TAG, "dismiss error");
        }
    }

    @Override
    public boolean onBeforeDismiss() {
        return checkPerformDismiss();
    }

    @Override
    public boolean callDismissAtOnce() {
        boolean hasAnima = false;
        if (mExitAnimation != null && mAnimaView != null) {
            if (!isExitAnimaPlaying) {
                mExitAnimation.setAnimationListener(mAnimationListener);
                mAnimaView.clearAnimation();
                mAnimaView.startAnimation(mExitAnimation);
                isExitAnimaPlaying = true;
                hasAnima = true;
            }
        } else if (mExitAnimator != null) {
            if (!isExitAnimaPlaying) {
                mExitAnimator.removeListener(mAnimatorListener);
                mExitAnimator.addListener(mAnimatorListener);
                mExitAnimator.start();
                isExitAnimaPlaying = true;
                hasAnima = true;
            }
        }
        //如果有动画，则不立刻执行dismiss
        return !hasAnima;
    }

    /**
     * 直接消掉popup而不需要动画
     */
    public void dismissWithOutAnima() {
        if (!checkPerformDismiss()) return;
        try {
            if (mExitAnimation != null && mAnimaView != null) mAnimaView.clearAnimation();
            if (mExitAnimator != null) mExitAnimator.removeAllListeners();
            mPopupWindow.callSuperDismiss();
        } catch (Exception e) {
            Log.e(TAG, "dismiss error");
        }
    }


    private boolean checkPerformDismiss() {
        boolean callDismiss = true;
        if (mOnDismissListener != null) {
            callDismiss = mOnDismissListener.onBeforeDismiss();
        }
        return callDismiss && !isExitAnimaPlaying;
    }

    private boolean checkPerformShow(View v) {
        boolean result = true;
        if (mOnBeforeShowCallback != null) {
            result = mOnBeforeShowCallback.onBeforeShow(mPopupView, v, this.mShowAnimation != null || this.mShowAnimator != null);
        }
        return result;
    }

    //------------------------------------------Anima-----------------------------------------------

    private Animator.AnimatorListener mAnimatorListener = new Animator.AnimatorListener() {
        @Override
        public void onAnimationStart(Animator animation) {
            isExitAnimaPlaying = true;
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            mPopupWindow.callSuperDismiss();
            isExitAnimaPlaying = false;
        }

        @Override
        public void onAnimationCancel(Animator animation) {
            isExitAnimaPlaying = false;
        }

        @Override
        public void onAnimationRepeat(Animator animation) {

        }
    };

    private Animation.AnimationListener mAnimationListener = new Animation.AnimationListener() {
        @Override
        public void onAnimationStart(Animation animation) {
            isExitAnimaPlaying = true;
        }

        @Override
        public void onAnimationEnd(Animation animation) {
            mPopupWindow.callSuperDismiss();
            isExitAnimaPlaying = false;
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    };

    /**
     * 生成TranslateAnimation
     *
     * @param durationMillis 动画显示时间
     * @param start          初始位置
     */
    protected Animation getTranslateAnimation(int start, int end, int durationMillis) {
        return SimpleAnimUtil.getTranslateAnimation(start, end, durationMillis);
    }

    /**
     * 生成ScaleAnimation
     * <p>
     * time=300
     */
    protected Animation getScaleAnimation(float fromX,
                                          float toX,
                                          float fromY,
                                          float toY,
                                          int pivotXType,
                                          float pivotXValue,
                                          int pivotYType,
                                          float pivotYValue) {
        return SimpleAnimUtil.getScaleAnimation(fromX, toX, fromY, toY, pivotXType, pivotXValue, pivotYType, pivotYValue);
    }

    /**
     * 生成自定义ScaleAnimation
     */
    protected Animation getDefaultScaleAnimation() {
        return SimpleAnimUtil.getDefaultScaleAnimation();
    }

    /**
     * 生成默认的AlphaAnimation
     */
    protected Animation getDefaultAlphaAnimation() {
        return SimpleAnimUtil.getDefaultAlphaAnimation();
    }

    /**
     * 从下方滑动上来
     */
    protected AnimatorSet getDefaultSlideFromBottomAnimationSet() {
        return SimpleAnimUtil.getDefaultSlideFromBottomAnimationSet(mAnimaView);
    }

    /**
     * 获取屏幕高度(px)
     */
    public int getScreenHeight() {
        return getContext().getResources().getDisplayMetrics().heightPixels;
    }

    /**
     * 获取屏幕宽度(px)
     */
    public int getScreenWidth() {
        return getContext().getResources().getDisplayMetrics().widthPixels;
    }

    //------------------------------------------callback-----------------------------------------------
    protected void showOnTop(View mPopupView) {

    }

    protected void showOnDown(View mPopupView) {

    }

    @Override
    public void onDismiss() {
        if (mOnDismissListener != null) {
            mOnDismissListener.onDismiss();
        }
        isExitAnimaPlaying = false;
    }


    //------------------------------------------Interface-----------------------------------------------
    public interface OnBeforeShowCallback {
        /**
         * <b>return ture for perform show</b>
         *
         * @param popupRootView The rootView of popup,it's usually be your layout
         * @param anchorView    The anchorView whitch popup show
         * @param hasShowAnima  Check if show your popup with anima?
         * @return
         */
        boolean onBeforeShow(View popupRootView, View anchorView, boolean hasShowAnima);


    }

    public static abstract class OnDismissListener implements PopupWindow.OnDismissListener {
        /**
         * <b>return ture for perform dismiss</b>
         *
         * @return
         */
        public boolean onBeforeDismiss() {
            return true;
        }
    }
}
