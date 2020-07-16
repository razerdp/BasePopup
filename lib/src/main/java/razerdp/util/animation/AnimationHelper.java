package razerdp.util.animation;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Created by 大灯泡 on 2020/6/11
 * <p>
 * Description：动画构建
 */
@SuppressWarnings("rawtypes")
public class AnimationHelper {

    private AnimationHelper() {
    }


    public static AnimationBuilder asAnimation() {
        return new AnimationBuilder();
    }

    public static AnimatorBuilder asAnimator() {
        return new AnimatorBuilder();
    }


    public static class AnimationBuilder extends AnimationApi<AnimationBuilder> {


        public Animation toShow() {
            return toShow(null);
        }

        public Animation toShow(@Nullable OnAnimationCreateListener onAnimationCreateListener) {
            AnimationSet set = new AnimationSet(false);
            if (configs != null) {
                for (int i = 0; i < configs.size(); i++) {
                    BaseAnimationConfig config = configs.valueAt(i);
                    Animation childAnim = config.$buildAnimation(false);
                    if (childAnim.isFillEnabled()) {
                        set.setFillEnabled(true);
                    }
                    if (childAnim.getFillBefore()) {
                        set.setFillBefore(true);
                    }
                    if (childAnim.getFillAfter()) {
                        set.setFillAfter(true);
                    }
                    if (onAnimationCreateListener != null) {
                        onAnimationCreateListener.onAnimationCreated(childAnim);
                    }
                    set.addAnimation(childAnim);
                }
                if (onAnimationCreateListener != null) {
                    onAnimationCreateListener.onAnimationCreateFinish(set);
                }
            }
            return set;
        }


        public Animation toDismiss() {
            return toDismiss(null);
        }

        public Animation toDismiss(@Nullable OnAnimationCreateListener animationCreatListener) {
            AnimationSet set = new AnimationSet(false);
            if (configs != null) {
                for (int i = 0; i < configs.size(); i++) {
                    BaseAnimationConfig config = configs.valueAt(i);
                    Animation childAnim = config.$buildAnimation(true);
                    if (childAnim.isFillEnabled()) {
                        set.setFillEnabled(true);
                    }
                    if (childAnim.getFillBefore()) {
                        set.setFillBefore(true);
                    }
                    if (childAnim.getFillAfter()) {
                        set.setFillAfter(true);
                    }
                    if (animationCreatListener != null) {
                        animationCreatListener.onAnimationCreated(childAnim);
                    }
                    set.addAnimation(childAnim);
                }
                if (animationCreatListener != null) {
                    animationCreatListener.onAnimationCreateFinish(set);
                }
            }
            return set;
        }
    }

    public static class AnimatorBuilder extends AnimationApi<AnimatorBuilder> {

        public Animator toShow() {
            return toShow(null);
        }

        public Animator toShow(@Nullable OnAnimatorCreateListener onAnimatorCreateListener) {
            AnimatorSet set = new AnimatorSet();
            if (configs != null) {
                for (int i = 0; i < configs.size(); i++) {
                    BaseAnimationConfig config = configs.valueAt(i);
                    Animator childAnimator = config.$buildAnimator(false);
                    if (onAnimatorCreateListener != null) {
                        onAnimatorCreateListener.onAnimatorCreated(childAnimator);
                    }
                    set.playTogether(childAnimator);
                }
                if (onAnimatorCreateListener != null) {
                    onAnimatorCreateListener.onAnimatorCreateFinish(set);
                }
            }
            return set;
        }

        public Animator toDismiss() {
            return toDismiss(null);
        }

        public Animator toDismiss(@Nullable OnAnimatorCreateListener onAnimatorCreateListener) {
            AnimatorSet set = new AnimatorSet();
            if (configs != null) {
                for (int i = 0; i < configs.size(); i++) {
                    BaseAnimationConfig config = configs.valueAt(i);
                    Animator childAnimator = config.$buildAnimator(true);
                    if (onAnimatorCreateListener != null) {
                        onAnimatorCreateListener.onAnimatorCreated(childAnimator);
                    }
                    set.playTogether(childAnimator);
                }
                if (onAnimatorCreateListener != null) {
                    onAnimatorCreateListener.onAnimatorCreateFinish(set);
                }
            }
            return set;
        }
    }

    public static abstract class OnAnimationCreateListener {
        public abstract void onAnimationCreated(@NonNull Animation animation);

        public void onAnimationCreateFinish(@NonNull AnimationSet animationSet) {

        }
    }

    public static abstract class OnAnimatorCreateListener {
        public abstract void onAnimatorCreated(@NonNull Animator animator);

        public void onAnimatorCreateFinish(@NonNull AnimatorSet animatorSet) {

        }
    }
}
