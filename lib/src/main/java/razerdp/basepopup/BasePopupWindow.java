package razerdp.basepopup;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
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
    protected View mPopupView;
    protected View mAnimaView;
    protected View mDismissView;
    protected Activity mContext;
    //是否自动弹出输入框(default:false)
    private boolean autoShowInputMethod = false;
    private OnDismissListener mOnDismissListener;
    //anima
    protected Animation curExitAnima;
    protected Animator curExitAnimator;
    protected Animation curAnima;
    protected Animator curAnimator;

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

        mPopupView = getPopupView();
        mPopupView.setFocusableInTouchMode(true);
        //默认占满全屏
        mPopupWindow = new PopupWindow(mPopupView, w, h);
        //指定透明背景，back键相关
        mPopupWindow.setBackgroundDrawable(new ColorDrawable());
        mPopupWindow.setFocusable(true);
        mPopupWindow.setOutsideTouchable(true);
        //无需动画
        mPopupWindow.setAnimationStyle(R.style.PopupAnimaFade);

        //=============================================================为外层的view添加点击事件，并设置点击消失
        mAnimaView = getAnimaView();
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
        curAnima = getShowAnimation();
        curAnimator = getShowAnimator();
        curExitAnima = getExitAnimation();
        curExitAnimator = getExitAnimator();
    }

    //------------------------------------------抽象-----------------------------------------------

    /**
     * PopupWindow展示出来后，需要执行动画的View.一般为蒙层之上的View
     * @return
     */
    protected abstract Animation getShowAnimation();

    /**
     * 设置一个点击后触发dismiss PopupWindow的View，一般为蒙层
     * @return
     */
    protected abstract View getClickToDismissView();

    /**
     * 设置展示动画View的属性动画
     * @return
     */
    public Animator getShowAnimator() { return null; }

    /**
     * 设置一个拥有输入功能的View，一般为EditTextView
     * @return
     */
    public View getInputView() { return null; }

    /**
     * 设置PopupWindow销毁时的退出动画
     * @return
     */
    public Animation getExitAnimation() {
        return null;
    }

    /**
     * 设置PopupWindow销毁时的退出属性动画
     * @return
     */
    public Animator getExitAnimator() {
        return null;
    }

    /**
     * popupwindow是否需要淡入淡出
     * @param needPopupFadeAnima
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
     * @param animaStyleRes
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
        if (curAnima != null && mAnimaView != null) {
            mAnimaView.clearAnimation();
            mAnimaView.startAnimation(curAnima);
        }
        if (curAnima == null && curAnimator != null && mAnimaView != null) {
            curAnimator.start();
        }
        //自动弹出键盘
        if (autoShowInputMethod && getInputView() != null) {
            getInputView().requestFocus();
            InputMethodUtils.showInputMethod(getInputView(), 150);
        }
    }

    /**
     * PopupWindow是否需要自适应输入法，为输入法弹出让出区域
     * @param needAdjust <br>
     *                   ture for "SOFT_INPUT_ADJUST_RESIZE" mode<br>
     *                   false for "SOFT_INPUT_ADJUST_NOTHING" mode
     */
    public void setAdjustInputMethod(boolean needAdjust) {
        if (needAdjust) {
            mPopupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        }
        else {
            mPopupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        }
    }

    /**
     * 当PopupWindow展示的时候，这个参数决定了是否自动弹出输入法
     * 如果使用这个方法，您必须保证通过 <strong>getInputView()<strong/>得到一个EditTextView
     * @param autoShow
     */
    public void setAutoShowInputMethod(boolean autoShow) {
        this.autoShowInputMethod = autoShow;
        if (autoShow) {
            setAdjustInputMethod(true);
        }
        else {
            setAdjustInputMethod(false);
        }
    }

    /**
     * 这个参数决定点击返回键是否可以取消掉PopupWindow
     * @param backPressEnable
     */
    public void setBackPressEnable(boolean backPressEnable) {
        if (backPressEnable) {
            mPopupWindow.setBackgroundDrawable(new ColorDrawable());
        }
        else {
            mPopupWindow.setBackgroundDrawable(null);
        }
    }

    /**
     * 这个方法封装了LayoutInflater.from(context).inflate，方便您设置PopupWindow所用的xml
     * @param resId reference of layout
     * @return root View of the layout
     */
    public View getPopupViewById(int resId) {
        if (resId != 0) {
            return LayoutInflater.from(mContext).inflate(resId, null);
        }
        else {
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
     * 这个方法用于简化您为View设置OnClickListener事件，多个View将会使用同一个点击事件
     * @param listener
     * @param views
     */
    protected void setViewClickListener(View.OnClickListener listener, View... views) {
        for (View view : views) {
            if (view != null && listener != null) {
                view.setOnClickListener(listener);
            }
        }
    }

    //------------------------------------------Getter/Setter-----------------------------------------------

    /**
     * PopupWindow是否处于展示状态
     * @return
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

    //------------------------------------------状态控制-----------------------------------------------

    /**
     * 取消一个PopupWindow，如果有退出动画，PopupWindow的消失将会在动画结束后执行
     */
    public void dismiss() {
        try {
            if (curExitAnima != null && mAnimaView != null) {
                if (!isExitAnimaPlaying) {
                    curExitAnima.setAnimationListener(mAnimationListener);
                    mAnimaView.clearAnimation();
                    mAnimaView.startAnimation(curExitAnima);
                    isExitAnimaPlaying = true;
                }
            }
            else if (curExitAnimator != null) {
                if (!isExitAnimaPlaying) {
                    curExitAnimator.removeListener(mAnimatorListener);
                    curExitAnimator.addListener(mAnimatorListener);
                    curExitAnimator.start();
                    isExitAnimaPlaying = true;
                }
            }
            else {
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
            if (curExitAnima != null && mAnimaView != null) mAnimaView.clearAnimation();
            if (curExitAnimator != null) curExitAnimator.removeAllListeners();
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
     * @param start 初始位置
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
    protected Animation getScaleAnimation(float fromX, float toX, float fromY, float toY, int pivotXType, float pivotXValue, int pivotYType, float pivotYValue) {
        Animation scaleAnimation = new ScaleAnimation(fromX, toX, fromY, toY, pivotXType, pivotXValue, pivotYType,
                pivotYValue);
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
                Animation.RELATIVE_TO_SELF, 0.5f);
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
            set.playTogether(ObjectAnimator.ofFloat(mAnimaView, "translationY", 250, 0).setDuration(400),
                    ObjectAnimator.ofFloat(mAnimaView, "alpha", 0.4f, 1).setDuration(250 * 3 / 2));
        }
        return set;
    }

    //------------------------------------------Interface-----------------------------------------------
    public interface OnDismissListener {
        void onDismiss();
    }
}
