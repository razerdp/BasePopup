package razerdp.demo.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.StringRes;
import androidx.core.content.ContextCompat;

import razerdp.demo.app.AppContext;
import razerdp.demo.utils.rx.RxCall;
import razerdp.demo.utils.rx.RxHelper;


/**
 * Created by 大灯泡 on 2017/4/18.
 */

public class UIHelper {

    public static final int DP1 = UIHelper.dip2px(1);
    public static final int DP2 = UIHelper.dip2px(2);
    public static final int DP3 = UIHelper.dip2px(3);
    public static final int DP4 = UIHelper.dip2px(4);
    public static final int DP5 = UIHelper.dip2px(5);
    public static final int DP6 = UIHelper.dip2px(6);
    public static final int DP7 = UIHelper.dip2px(7);
    public static final int DP8 = UIHelper.dip2px(8);
    public static final int DP9 = UIHelper.dip2px(9);
    public static final int DP10 = UIHelper.dip2px(10);
    public static final int DP11 = UIHelper.dip2px(11);
    public static final int DP12 = UIHelper.dip2px(12);
    public static final int DP13 = UIHelper.dip2px(13);
    public static final int DP14 = UIHelper.dip2px(14);
    public static final int DP15 = UIHelper.dip2px(15);
    public static final int DP16 = UIHelper.dip2px(16);
    public static final int DP17 = UIHelper.dip2px(17);
    public static final int DP18 = UIHelper.dip2px(18);
    public static final int DP19 = UIHelper.dip2px(19);
    public static final int DP20 = UIHelper.dip2px(20);
    public static final int DP21 = UIHelper.dip2px(21);
    public static final int DP22 = UIHelper.dip2px(22);
    public static final int DP23 = UIHelper.dip2px(23);
    public static final int DP24 = UIHelper.dip2px(24);


    private static int statusBarHeight;

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
        if (ToolUtil.isMainThread()) {
            Toast.makeText(AppContext.getAppContext(), text, duration).show();
        } else {
            RxHelper.runOnUiThread((RxCall<Void>) data -> toast(text, duration));
        }
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(float dpValue) {
        final float scale = Resources.getSystem().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(float pxValue) {
        final float scale = Resources.getSystem().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static int sp2px(float spValue) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                spValue, Resources.getSystem().getDisplayMetrics());
    }

    public static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }

    public static int getStatusBarHeight(Context context) {
        checkStatusBarHeight(context);
        return statusBarHeight;
    }

    private static void checkStatusBarHeight(Context context) {
        if (statusBarHeight != 0 || context == null) return;
        int result = 0;
        //获取状态栏高度的资源id
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        statusBarHeight = result;
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

    public static Drawable getDrawable(@DrawableRes int drawableId) {
        try {
            return ContextCompat.getDrawable(AppContext.getAppContext(), drawableId);
        } catch (Exception e) {
            return null;
        }
    }
}
