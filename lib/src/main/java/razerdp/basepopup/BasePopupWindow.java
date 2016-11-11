package razerdp.basepopup;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.PopupWindow;

import java.lang.reflect.Field;

import razerdp.library.R;

/**
 * Created by 大灯泡 on 2016/1/14.
 * 抽象通用popupwindow的父类
 */
public abstract class BasePopupWindow implements BasePopup {
    private static final String TAG = "BasePopupWindow";
    //元素定义
    protected PopupWindow mPopupWindow;
    //popup视图
    private View mPopupView;
    private Activity mContext;
    protected View mAnimaView;
    protected View mDismissView;
    //是否自动弹出输入框(default:false)
    private boolean autoShowInputMethod = false;
    private OnDismissListener mOnDismissListener;
    //anima

    private Animation mShowAnimation;
    private Animator mShowAnimator;
    private Animation mExitAnimation;
    private Animator mExitAnimator;

    private boolean isExitAnimaPlaying = false;

    private boolean needPopupFadeAnima = true;

    public BasePopupWindow(Activity context) {
        initView(context, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    }

    public BasePopupWindow(Activity context, int w, int h) {
        initView(context, w, h);
    }

    private void initView(Activity context, int w, int h) {
        mContext = context;

        mPopupView = onCreatePopupView();
        mPopupView.setFocusableInTouchMode(true);
        //默认占满全屏
        mPopupWindow = new PopupWindow(mPopupView, w, h);
        //指定透明背景，back键相关
        mPopupWindow.setBackgroundDrawable(new ColorDrawable());
        mPopupWindow.setFocusable(true);
        mPopupWindow.setOutsideTouchable(true);
        //默认是渐入动画
        mPopupWindow.setAnimationStyle(R.style.PopupAnimaFade);

        //=============================================================为外层的view添加点击事件，并设置点击消失
        mAnimaView = initAnimaView();
        mDismissView = getClickToDismissView();
        if (mDismissView != null) {
            mDismissView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });
            if (mAnimaView != null) {
                mAnimaView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
            }
        }
        //=============================================================元素获取
        mShowAnimation = initShowAnimation();
        mShowAnimator = initShowAnimator();
        mExitAnimation = initExitAnimation();
        mExitAnimator = initExitAnimator();
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
    public View getInputView() {
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
        mPopupWindow.setAnimationStyle(needPopupFadeAnima ? R.style.PopupAnimaFade : 0);
    }

    public boolean getNeedPopupFade() {
        return this.needPopupFadeAnima;
    }

    /**
     * 设置popup的动画style
     */
    public void setPopupAnimaStyle(int animaStyleRes) {
        if (animaStyleRes > 0) {
            mPopupWindow.setAnimationStyle(animaStyleRes);
        }
    }

    //------------------------------------------showPopup-----------------------------------------------

    /**
     * 调用此方法时，PopupWindow将会显示在DecorView
     */
    public void showPopupWindow() {
        try {
            tryToShowPopup(0, null);
        } catch (Exception e) {
            Log.e(TAG, "show error");
            e.printStackTrace();
        }
    }

    public void showPopupWindow(int res) {
        try {
            tryToShowPopup(res, null);
        } catch (Exception e) {
            Log.e(TAG, "show error");
            e.printStackTrace();
        }
    }

    public void showPopupWindow(View v) {
        try {
            tryToShowPopup(0, v);
        } catch (Exception e) {
            Log.e(TAG, "show error");
            e.printStackTrace();
        }
    }

    //------------------------------------------Methods-----------------------------------------------
    private void tryToShowPopup(int res, View v) throws Exception {
        //传递了view
        if (res == 0 && v != null) {
            mPopupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);
        }
        //传递了res
        if (res != 0 && v == null) {
            mPopupWindow.showAtLocation(mContext.findViewById(res), Gravity.CENTER, 0, 0);
        }
        //什么都没传递，取顶级view的id
        if (res == 0 && v == null) {
            mPopupWindow.showAtLocation(mContext.findViewById(android.R.id.content), Gravity.CENTER, 0, 0);
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
        if (mOnDismissListener != null) {
            mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    mOnDismissListener.onDismiss();
                }
            });
        }
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

    public Animation getShowAnimation(){
        return mShowAnimation;
    }

    public void setShowAnimator(Animator showAnimator) {
        if (mShowAnimator != null) mShowAnimator.cancel();
        if (showAnimator != null && showAnimator != mShowAnimator) {
            mShowAnimator = showAnimator;
        }
    }

    public Animator getShowAnimator(){
        return mShowAnimator;
    }

    public void setExitAnimation(Animation exitAnimation){
        if (mExitAnimation != null && mAnimaView != null) {
            mAnimaView.clearAnimation();
            mExitAnimation.cancel();
        }
        if (exitAnimation != null && exitAnimation != mExitAnimation) {
            mExitAnimation = exitAnimation;
        }
    }

    public Animation getExitAnimation(){
        return mExitAnimation;
    }

    public void setExitAnimator(Animator exitAnimator) {
        if (mExitAnimator != null) mExitAnimator.cancel();
        if (exitAnimator != null && exitAnimator != mExitAnimator) {
            mExitAnimator = exitAnimator;
        }
    }

    public Animator getExitAnimator(){
        return mExitAnimator;
    }

    public Context getContext() {
        return mContext;
    }

    public View getPopupWindowView() {
        return mPopupView;
    }



    //------------------------------------------状态控制-----------------------------------------------

    /**
     * 取消一个PopupWindow，如果有退出动画，PopupWindow的消失将会在动画结束后执行
     */
    public void dismiss() {
        try {
            if (mExitAnimation != null && mAnimaView != null) {
                if (!isExitAnimaPlaying) {
                    mExitAnimation.setAnimationListener(mAnimationListener);
                    mAnimaView.clearAnimation();
                    mAnimaView.startAnimation(mExitAnimation);
                    isExitAnimaPlaying = true;
                }
            } else if (mExitAnimator != null) {
                if (!isExitAnimaPlaying) {
                    mExitAnimator.removeListener(mAnimatorListener);
                    mExitAnimator.addListener(mAnimatorListener);
                    mExitAnimator.start();
                    isExitAnimaPlaying = true;
                }
            } else {
                mPopupWindow.dismiss();
            }
        } catch (Exception e) {
            Log.d(TAG, "dismiss error");
        }
    }

    /**
     * 直接消掉popup而不需要动画
     */
    public void dismissWithOutAnima() {
        try {
            if (mExitAnimation != null && mAnimaView != null) mAnimaView.clearAnimation();
            if (mExitAnimator != null) mExitAnimator.removeAllListeners();
            mPopupWindow.dismiss();
        } catch (Exception e) {
            Log.d(TAG, "dismiss error");
        }
    }
    //------------------------------------------Anima-----------------------------------------------

    private Animator.AnimatorListener mAnimatorListener = new Animator.AnimatorListener() {
        @Override
        public void onAnimationStart(Animator animation) {

        }

        @Override
        public void onAnimationEnd(Animator animation) {
            mPopupWindow.dismiss();
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

        }

        @Override
        public void onAnimationEnd(Animation animation) {
            mPopupWindow.dismiss();
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
        Animation translateAnimation = new TranslateAnimation(0, 0, start, end);
        translateAnimation.setDuration(durationMillis);
        translateAnimation.setFillEnabled(true);
        translateAnimation.setFillAfter(true);
        return translateAnimation;
    }

    /**
     * 生成ScaleAnimation
     */
    protected Animation getScaleAnimation(float fromX,
                                          float toX,
                                          float fromY,
                                          float toY,
                                          int pivotXType,
                                          float pivotXValue,
                                          int pivotYType,
                                          float pivotYValue) {
        Animation scaleAnimation = new ScaleAnimation(fromX, toX, fromY, toY, pivotXType, pivotXValue, pivotYType,
                                                      pivotYValue
        );
        scaleAnimation.setDuration(300);
        scaleAnimation.setFillEnabled(true);
        scaleAnimation.setFillAfter(true);
        return scaleAnimation;
    }

    /**
     * 生成自定义ScaleAnimation
     */
    protected Animation getDefaultScaleAnimation() {
        Animation scaleAnimation = new ScaleAnimation(0f, 1f, 0f, 1f, Animation.RELATIVE_TO_SELF, 0.5f,
                                                      Animation.RELATIVE_TO_SELF, 0.5f
        );
        scaleAnimation.setDuration(300);
        scaleAnimation.setInterpolator(new AccelerateInterpolator());
        scaleAnimation.setFillEnabled(true);
        scaleAnimation.setFillAfter(true);
        return scaleAnimation;
    }

    /**
     * 生成默认的AlphaAnimation
     */
    protected Animation getDefaultAlphaAnimation() {
        Animation alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
        alphaAnimation.setDuration(300);
        alphaAnimation.setInterpolator(new AccelerateInterpolator());
        alphaAnimation.setFillEnabled(true);
        alphaAnimation.setFillAfter(true);
        return alphaAnimation;
    }

    /**
     * 从下方滑动上来
     */
    protected AnimatorSet getDefaultSlideFromBottomAnimationSet() {
        AnimatorSet set = null;
        set = new AnimatorSet();
        if (mAnimaView != null) {
            set.playTogether(
                    ObjectAnimator.ofFloat(mAnimaView, "translationY", 250, 0).setDuration(400),
                    ObjectAnimator.ofFloat(mAnimaView, "alpha", 0.4f, 1).setDuration(250 * 3 / 2)
                            );
        }
        return set;
    }

    //------------------------------------------Interface-----------------------------------------------
    public interface OnDismissListener {
        void onDismiss();
    }
}
