package razerdp.demo.interpolator;

import android.view.animation.LinearInterpolator;

/**
 * Created by 大灯泡 on 2016/1/28.
 * The expression comes from web:
 * http://inloop.github.io/
 */
public class OverShootInterpolator extends LinearInterpolator {
    private float factor;

    public OverShootInterpolator() {
        this.factor = 2.0f;
    }

    public OverShootInterpolator(float factor) {
        this.factor = factor;
    }

    @Override
    public float getInterpolation(float input) {

        input -= 1.0;
        return (float) (input * input * ((factor + 1) * input + factor) + 1.0);
    }
}
