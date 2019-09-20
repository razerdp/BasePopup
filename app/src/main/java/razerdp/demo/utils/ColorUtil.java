package razerdp.demo.utils;

import android.graphics.Color;

import androidx.annotation.FloatRange;
import androidx.core.graphics.ColorUtils;


/**
 * Created by 大灯泡 on 2019/4/22
 * <p>
 * Description：
 */
public class ColorUtil {
    public static final float DEFAULT_BRIGHTNESS = 0.75f;

    public static int gradientColor(float fraction, int startColor, int endColor) {
        int startA = (startColor >> 24) & 0xff;
        int startR = (startColor >> 16) & 0xff;
        int startG = (startColor >> 8) & 0xff;
        int startB = startColor & 0xff;

        int endA = (endColor >> 24) & 0xff;
        int endR = (endColor >> 16) & 0xff;
        int endG = (endColor >> 8) & 0xff;
        int endB = endColor & 0xff;

        int currentA = (startA + (int) (fraction * (endA - startA))) << 24;
        int currentR = (startR + (int) (fraction * (endR - startR))) << 16;
        int currentG = (startG + (int) (fraction * (endG - startG))) << 8;
        int currentB = startB + (int) (fraction * (endB - startB));

        return currentA | currentR | currentG | currentB;
    }

    public static boolean isDark(int color) {
        return isDark(Color.red(color), Color.green(color), Color.blue(color));
    }

    /**
     * 根据RGB值判断 深色与浅色
     */
    public static boolean isDark(int r, int g, int b) {
        return !(r * 0.299 + g * 0.578 + b * 0.114 >= 192);
    }

    public static int brightnessColor(int color, @FloatRange(from = 0f) float brightness) {
        if (color == Color.TRANSPARENT) return color;
        int alpha = Color.alpha(color);
        float[] hslArray = new float[3];

        ColorUtils.colorToHSL(color, hslArray);
        hslArray[2] = hslArray[2] * brightness;

        int result = ColorUtils.HSLToColor(hslArray);
        if (result == Color.BLACK) {
            result = Color.parseColor("#575757");
        } else if (result == Color.WHITE) {
            result = Color.parseColor("#EAEAEA");
        }

        return Color.argb(alpha, Color.red(result), Color.green(result), Color.blue(result));
    }

    public static int alphaColor(@FloatRange(from = 0f, to = 1f) float alpha, int sourceColor) {
        alpha = NumberUtils.range(alpha, 0f, 1f);

        int R = Color.red(sourceColor);
        int G = Color.green(sourceColor);
        int B = Color.blue(sourceColor);

        int A = (int) (alpha * 255.0f + 0.5f);

        return Color.argb(A, R, G, B);

    }
}
