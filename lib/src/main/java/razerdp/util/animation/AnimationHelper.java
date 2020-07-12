package razerdp.util.animation;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.res.Resources;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.Interpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;

import androidx.annotation.FloatRange;
import androidx.annotation.NonNull;

import java.util.HashSet;
import java.util.Set;
import java.util.zip.DeflaterOutputStream;

import razerdp.util.log.PopupLog;

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
            AnimationSet set = new AnimationSet(false);
            if (configs != null) {
                for (int i = 0; i < configs.size(); i++) {
                    BaseAnimationConfig config = configs.valueAt(i);
                    set.addAnimation(config.$buildAnimation(false));
                }
            }
            return set;
        }


        public Animation toDismiss() {
            AnimationSet set = new AnimationSet(false);
            if (configs != null) {
                for (int i = 0; i < configs.size(); i++) {
                    BaseAnimationConfig config = configs.valueAt(i);
                    set.addAnimation(config.$buildAnimation(true));
                }
            }
            return set;
        }
    }

    public static class AnimatorBuilder extends AnimationApi<AnimatorBuilder> {

        public Animator toShow() {
            AnimatorSet set = new AnimatorSet();
            if (configs != null) {
                for (int i = 0; i < configs.size(); i++) {
                    BaseAnimationConfig config = configs.valueAt(i);
                    set.playTogether(config.$buildAnimator(false));
                }
            }
            return set;
        }

        public Animator toDismiss() {
            AnimatorSet set = new AnimatorSet();
            if (configs != null) {
                for (int i = 0; i < configs.size(); i++) {
                    BaseAnimationConfig config = configs.valueAt(i);
                    set.playTogether(config.$buildAnimator(true));
                }
            }
            return set;
        }
    }


}
