package razerdp.blur;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RSIllegalArgumentException;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.util.TypedValue;
import android.view.View;

import razerdp.util.log.LogTag;
import razerdp.util.log.PopupLogUtil;

/**
 * Created by 大灯泡 on 2017/12/27.
 * <p>
 * 模糊处理类
 */
public class BlurHelper {
    private static final String TAG = "BlurHelper";
    private static int statusBarHeight = 0;

    public static boolean renderScriptSupported() {
        return Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN_MR1;
    }

    public static Bitmap blur(Context context, View view, float scaledRatio, float radius) {
        return blur(context, view, scaledRatio, radius, true);
    }

    public static Bitmap blur(Context context, View view, float scaledRatio, float radius, boolean fullScreen) {
        return blur(context, getViewBitmap(view, fullScreen), scaledRatio, radius);
    }

    public static Bitmap blur(Context context, Bitmap origin, float scaledRatio, float radius) {
        if (renderScriptSupported()) {
            PopupLogUtil.trace(LogTag.i, TAG, "脚本模糊");
            return renderScriptblur(context, origin, scaledRatio, radius);
        } else {
            PopupLogUtil.trace(LogTag.i, TAG, "快速模糊");
            scaledRatio = checkFloatRange(scaledRatio / 8, 1, scaledRatio);
            return fastBlur(context, origin, scaledRatio, radius);
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static Bitmap renderScriptblur(Context context, Bitmap origin, float scaledRatio, float radius) {
        if (origin == null || origin.isRecycled()) return null;
        radius = checkFloatRange(radius, 0, 20);
        scaledRatio = checkFloatRange(scaledRatio, 0, 1);

        final int originWidth = origin.getWidth();
        final int originHeight = origin.getHeight();
        PopupLogUtil.trace(LogTag.i, "originWidth  >>  " + originWidth + "   originHeight  >>  " + originHeight);

        int scaledWidth = originWidth;
        int scaledHeight = originHeight;

        if (scaledRatio > 0) {
            scaledWidth = (int) (scaledWidth * scaledRatio);
            scaledHeight = (int) (scaledHeight * scaledRatio);
        }

        PopupLogUtil.trace(LogTag.i, "scaledWidth  >>  " + scaledWidth + "   scaledHeight  >>  " + scaledHeight);

        Bitmap scaledBitmap = Bitmap.createScaledBitmap(origin, scaledWidth, scaledHeight, false);
        Bitmap result = Bitmap.createBitmap(scaledBitmap);

        if (scaledBitmap.isRecycled() || result == null || result.isRecycled()) {
            return null;
        }

        RenderScript renderScript = RenderScript.create(context);
        Allocation blurInput = Allocation.createFromBitmap(renderScript, scaledBitmap);
        Allocation blurOutput = Allocation.createFromBitmap(renderScript, result);

        ScriptIntrinsicBlur blur = null;
        try {
            blur = ScriptIntrinsicBlur.create(renderScript, blurInput.getElement());
        } catch (RSIllegalArgumentException e) {
            if (e.getMessage().contains("Unsuported element type")) {
                blur = ScriptIntrinsicBlur.create(renderScript, Element.U8_4(renderScript));
            }
        }

        if (blur == null) return null;

        blur.setRadius(radius);
        blur.setInput(blurInput);
        blur.forEach(blurOutput);
        blurOutput.copyTo(result);

        //释放
        renderScript.destroy();
        blurInput.destroy();
        blurOutput.destroy();
        scaledBitmap.recycle();
        origin.recycle();

        result = Bitmap.createScaledBitmap(result, originWidth, originHeight, false);
        PopupLogUtil.trace(LogTag.i, "resultWidth  >>  " + result.getWidth() + "   resultHeight  >>  " + result.getHeight());
        return result;
    }

    public static Bitmap fastBlur(Context context, Bitmap origin, float scaledRatio, float radius) {
        if (origin == null || origin.isRecycled()) return null;
        radius = checkFloatRange(radius, 0, 20);
        scaledRatio = checkFloatRange(scaledRatio, 0, 1);

        final int originWidth = origin.getWidth();
        final int originHeight = origin.getHeight();
        PopupLogUtil.trace(LogTag.i, "originWidth  >>  " + originWidth + "   originHeight  >>  " + originHeight);

        int scaledWidth = originWidth;
        int scaledHeight = originHeight;

        if (scaledRatio > 0) {
            scaledWidth = (int) (scaledWidth * scaledRatio);
            scaledHeight = (int) (scaledHeight * scaledRatio);
        }

        PopupLogUtil.trace(LogTag.i, "scaledWidth  >>  " + scaledWidth + "   scaledHeight  >>  " + scaledHeight);

        Bitmap result = Bitmap.createScaledBitmap(origin, scaledWidth, scaledHeight, false);

        if (result == null || result.isRecycled()) {
            return null;
        }

        result = FastBlur.doBlur(result, (int) radius, false);
        origin.recycle();

        result = Bitmap.createScaledBitmap(result, originWidth, originHeight, false);
        PopupLogUtil.trace(LogTag.i, "resultWidth  >>  " + result.getWidth() + "   resultHeight  >>  " + result.getHeight());
        return result;
    }

    public static Bitmap getViewBitmap(final View v, boolean fullScreen) {
        if (v == null || v.getWidth() <= 0 || v.getHeight() <= 0) {
            PopupLogUtil.trace(LogTag.e, "getViewBitmap  >>  宽或者高为空");
            return null;
        }
        if (statusBarHeight <= 0) statusBarHeight = getStatusBarHeight(v.getContext());
        Bitmap b = Bitmap.createBitmap(v.getWidth(), v.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        if (v.getBackground() == null) {
            //背景为空，则填充colorBackground背景
            TypedValue tv = new TypedValue();
            v.getContext().getTheme().resolveAttribute(android.R.attr.colorBackground, tv, true);
            if (tv.type >= TypedValue.TYPE_FIRST_COLOR_INT && tv.type <= TypedValue.TYPE_LAST_COLOR_INT) {
                c.drawColor(tv.data);
            } else {
                c.drawColor(Color.parseColor("#FAFAFA"));
            }
        }
        // FIXME: 2018/1/16 有争议，是否需要绘制背景？
        /*else {
            Drawable bgDrawable = v.getBackground();
            if (bgDrawable != null) {
                bgDrawable.draw(c);
            }
        }*/
        if (fullScreen) {
            if (statusBarHeight > 0 && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && v.getContext() instanceof Activity) {
                int statusBarColor = ((Activity) v.getContext()).getWindow().getStatusBarColor();
                Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
                p.setColor(statusBarColor);
                Rect rect = new Rect(0, 0, v.getWidth(), statusBarHeight);
                c.drawRect(rect, p);
            }
        }
        v.draw(c);
        return b;
    }

    private static float checkFloatRange(float originValue, float rangeFrom, float rangeTo) {
        if (originValue < rangeFrom) {
            originValue = rangeFrom;
        } else if (originValue > rangeTo) {
            originValue = rangeTo;
        }
        return originValue;
    }


    private static int getStatusBarHeight(Context context) {
        if (context == null) return 0;
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }
}
