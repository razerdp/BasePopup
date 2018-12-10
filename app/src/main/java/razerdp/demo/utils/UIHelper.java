package razerdp.demo.utils;

import android.graphics.Color;
import android.support.annotation.ColorRes;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;
import android.widget.Toast;

import razerdp.demo.app.AppContext;


/**
 * Created by 大灯泡 on 2017/4/18.
 */

public class UIHelper {


    public static int getColor(@ColorRes int colorResId) {
        try {
            return ContextCompat.getColor(AppContext.getAppContext(), colorResId);
        } catch (Exception e) {
            return Color.TRANSPARENT;
        }
    }

    public static void clearImageViewMemory(ImageView imageView) {
        if (imageView == null) {
            return;
        }
        imageView.setImageBitmap(null);
    }

    public static View cleanView(View v) {
        if (v == null) return null;
        ViewParent p = v.getParent();
        if (p instanceof ViewGroup) {
            ((ViewGroup) p).removeView(v);
        }
        return v;
    }


    public static void toast(@StringRes int textResId) {
        toast(StringUtil.getString(textResId));
    }

    public static void toast(String text) {
        toast(text, Toast.LENGTH_SHORT);
    }

    public static void toast(@StringRes int textResId, int duration) {
        toast(StringUtil.getString(textResId), duration);
    }

    public static void toast(String text, int duration) {
        Toast.makeText(AppContext.getAppContext(), text, duration).show();
    }

    public static int gradientColor(float fraction, int startColor, int endColor) {
        int redCurrent;
        int blueCurrent;
        int greenCurrent;
        int alphaCurrent;

        int redStart = Color.red(startColor);
        int blueStart = Color.blue(startColor);
        int greenStart = Color.green(startColor);
        int alphaStart = Color.alpha(startColor);

        int redEnd = Color.red(endColor);
        int blueEnd = Color.blue(endColor);
        int greenEnd = Color.green(endColor);
        int alphaEnd = Color.alpha(endColor);

        int redDifference = redEnd - redStart;
        int blueDifference = blueEnd - blueStart;
        int greenDifference = greenEnd - greenStart;
        int alphaDifference = alphaEnd - alphaStart;

        redCurrent = (int) (redStart + fraction * redDifference);
        blueCurrent = (int) (blueStart + fraction * blueDifference);
        greenCurrent = (int) (greenStart + fraction * greenDifference);
        alphaCurrent = (int) (alphaStart + fraction * alphaDifference);

        return Color.argb(alphaCurrent, redCurrent, greenCurrent, blueCurrent);
    }
}
