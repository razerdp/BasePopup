package razerdp.blur;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Build;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RSIllegalArgumentException;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.view.View;

import razerdp.util.log.LogTag;
import razerdp.util.log.LogUtil;

/**
 * Created by 大灯泡 on 2017/12/27.
 * <p>
 * 模糊处理类
 */
class BlurHelper {

    public static boolean renderScriptSupported() {
        return Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN_MR1;
    }

    public static Bitmap blur(Context context, View view, float scaledRatio, float radius) {
        return blur(context, getViewBitmap(view), scaledRatio, radius);
    }

    public static Bitmap blur(Context context, Bitmap origin, float scaledRatio, float radius) {
        if (renderScriptSupported()) {
            return renderScriptblur(context, origin, scaledRatio, radius);
        } else {
            return supportedBlur(context, origin, scaledRatio, radius);
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static Bitmap renderScriptblur(Context context, Bitmap origin, float scaledRatio, float radius) {
        if (origin == null || origin.isRecycled()) return null;
        radius = checkFloatRange(radius, 0, 20);
        scaledRatio = checkFloatRange(scaledRatio, 0, 1);

        final int originWidth = origin.getWidth();
        final int originHeight = origin.getHeight();
        LogUtil.trace(LogTag.i, "originWidth  >>  " + originWidth + "   originHeight  >>  " + originHeight);

        int scaledWidth = originWidth;
        int scaledHeight = originHeight;

        if (scaledRatio > 0) {
            scaledWidth = (int) (scaledWidth * scaledRatio);
            scaledHeight = (int) (scaledHeight * scaledRatio);
        }

        LogUtil.trace(LogTag.i, "scaledWidth  >>  " + scaledWidth + "   scaledHeight  >>  " + scaledHeight);

        Bitmap scaledBitmap = Bitmap.createScaledBitmap(origin, scaledWidth, scaledHeight, false);
        Bitmap result = Bitmap.createBitmap(scaledBitmap);

        if (scaledBitmap == null || scaledBitmap.isRecycled() || result == null || result.isRecycled()) {
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
        LogUtil.trace(LogTag.i, "resultWidth  >>  " + result.getWidth() + "   resultHeight  >>  " + result.getHeight());
        return result;
    }

    public static Bitmap supportedBlur(Context context, Bitmap origin, float scaledRatio, float radius) {
        if (origin == null || origin.isRecycled()) return null;
        radius = checkFloatRange(radius, 0, 20);
        scaledRatio = checkFloatRange(scaledRatio, 0, 1);

        final int originWidth = origin.getWidth();
        final int originHeight = origin.getHeight();
        LogUtil.trace(LogTag.i, "originWidth  >>  " + originWidth + "   originHeight  >>  " + originHeight);

        int scaledWidth = originWidth;
        int scaledHeight = originHeight;

        if (scaledRatio > 0) {
            scaledWidth = (int) (scaledWidth * scaledRatio);
            scaledHeight = (int) (scaledHeight * scaledRatio);
        }

        LogUtil.trace(LogTag.i, "scaledWidth  >>  " + scaledWidth + "   scaledHeight  >>  " + scaledHeight);

        Bitmap result = Bitmap.createScaledBitmap(origin, scaledWidth, scaledHeight, false);

        if (result == null || result.isRecycled()) {
            return null;
        }

        result = FastBlur.doBlur(result, (int) radius, false);
        origin.recycle();

        result = Bitmap.createScaledBitmap(result, originWidth, originHeight, false);
        LogUtil.trace(LogTag.i, "resultWidth  >>  " + result.getWidth() + "   resultHeight  >>  " + result.getHeight());
        return result;
    }

    private static Bitmap getViewBitmap(final View v) {
        if (v == null || v.getWidth() <= 0 || v.getHeight() <= 0) {
            LogUtil.trace(LogTag.e, "getViewBitmap  >>  宽或者高为空");
            return null;
        }
        Bitmap b = Bitmap.createBitmap(v.getWidth(), v.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        c.drawColor(Color.WHITE);
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
}
