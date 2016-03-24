package razerdp.demo.interpolator;

import android.view.animation.Interpolator;

/**
 * Created by 大灯泡 on 2016/1/28.
 * 一个自定义的插值器静态工厂
 * Thanks for the web:
 * http://inloop.github.io/interpolator/
 */
public class CustomInterpolatorFactory {

    private CustomInterpolatorFactory(){}

    public static Interpolator getSpringInterPolator(float factor){
        return new SpringInterpolator(factor);
    }
    public static Interpolator getSpringInterPolator(){
        return new SpringInterpolator();
    }

    public static Interpolator getAnticipateInterpolator(float factor){
        return new AnticipateInterpolator(factor);
    }
    public static Interpolator getAnticipateInterpolator(){
        return new AnticipateInterpolator();
    }

    public static Interpolator getAnticipateOverShootInterpolator(float factor){
        return new AnticipateOverShootInterpolator(factor);
    }
    public static Interpolator getAnticipateOverShootInterpolator(){
        return new AnticipateOverShootInterpolator();
    }

    public static Interpolator getJellyInterpolator(){
        return new JellyInterpolator();
    }


    public static Interpolator getOverShootInterpolator(float factor){
        return new OverShootInterpolator(factor);
    }
    public static Interpolator getOverShootInterpolator(){
        return new OverShootInterpolator();
    }
}
