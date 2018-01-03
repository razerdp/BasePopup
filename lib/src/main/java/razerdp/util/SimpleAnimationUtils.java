package razerdp.util;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;

/**
 * Created by 大灯泡 on 2017/1/13.
 */

public class SimpleAnimationUtils {


    /**
     * 生成TranslateAnimation
     *
     * @param durationMillis 动画显示时间
     * @param start          初始位置
     */
    public static Animation getTranslateVerticalAnimation(int start, int end, int durationMillis) {
        Animation translateAnimation = new TranslateAnimation(0, 0, start, end);
        translateAnimation.setDuration(durationMillis);
        return translateAnimation;
    }

    /**
     * 生成TranslateAnimation
     *
     * @param durationMillis 动画显示时间
     * @param start          初始位置
     */
    public static Animation getTranslateVerticalAnimation(float start, float end, int durationMillis) {
        Animation translateAnimation = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0, Animation.RELATIVE_TO_PARENT, 0, Animation.RELATIVE_TO_PARENT, start, Animation.RELATIVE_TO_PARENT, end);
        translateAnimation.setDuration(durationMillis);
        return translateAnimation;
    }


    /**
     * 生成自定义ScaleAnimation
     */
    public static Animation getDefaultScaleAnimation(boolean in) {
        return getScaleAnimation(in ? 0 : 1, in ? 1 : 0, in ? 0 : 1, in ? 1 : 0, Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
    }


    /**
     * 生成ScaleAnimation
     * <p>
     * time=360
     */
    public static Animation getScaleAnimation(float fromX,
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
        scaleAnimation.setDuration(360);
        return scaleAnimation;
    }


    /**
     * 生成默认的AlphaAnimation
     */
    public static Animation getDefaultAlphaAnimation(boolean in) {
        Animation alphaAnimation = new AlphaAnimation(in ? 0 : 1, in ? 1 : 0);
        alphaAnimation.setDuration(360);
        alphaAnimation.setInterpolator(new AccelerateInterpolator());
        return alphaAnimation;
    }


    /**
     * 从下方滑动上来
     */
    public static AnimatorSet getDefaultSlideFromBottomAnimationSet(View mAnimaView) {
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


    public static abstract class AnimationListenerAdapter implements Animation.AnimationListener {
        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationEnd(Animation animation) {

        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    }

}
