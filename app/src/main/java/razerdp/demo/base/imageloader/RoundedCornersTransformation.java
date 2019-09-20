package razerdp.demo.base.imageloader;

/**
 * https://github.com/wasabeef/glide-transformations/blob/master/transformations/src/main/java/jp/wasabeef/glide/transformations/RoundedCornersTransformation.java
 */

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.RectF;
import android.graphics.Shader;
import android.view.Gravity;

import androidx.annotation.NonNull;
import androidx.annotation.Px;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.load.resource.bitmap.TransformationUtils;

import java.security.MessageDigest;

import static android.widget.ImageView.ScaleType;

public class RoundedCornersTransformation extends BitmapTransformation {

    private static final int VERSION = 1;
    private static final String ID = "jp.wasabeef.glide.transformations.RoundedCornersTransformation." + VERSION;

    public enum CornerType {
        ALL,//四个角
        TOP_LEFT,//左上
        TOP_RIGHT,//右上
        BOTTOM_LEFT,//左下
        BOTTOM_RIGHT,//右下
        TOP,//顶部
        BOTTOM,//底部
        LEFT,//左边
        RIGHT,//右边
        OTHER_TOP_LEFT,//除了左上都圆角
        OTHER_TOP_RIGHT,//除了右上都圆角
        OTHER_BOTTOM_LEFT,//除了左下都圆角
        OTHER_BOTTOM_RIGHT,//除了右下都圆角
        DIAGONAL_FROM_TOP_LEFT,//左上和右下对角圆角
        DIAGONAL_FROM_TOP_RIGHT//右上和左下对角圆角
    }

    public static CornerType getType(int topLeftRadius, int topRightRadius, int bottomLeftRadius, int bottomRightRadius) {
        CornerType result = null;
        int gravity = Gravity.NO_GRAVITY;
        if (topLeftRadius != 0) {
            gravity |= Gravity.TOP | Gravity.LEFT;
        }

        if (topRightRadius != 0) {
            gravity |= Gravity.TOP | Gravity.RIGHT;
        }

        if (bottomLeftRadius != 0) {
            gravity |= Gravity.BOTTOM | Gravity.LEFT;
        }

        if (bottomRightRadius != 0) {
            gravity |= Gravity.BOTTOM | Gravity.RIGHT;
        }

        switch (gravity & Gravity.VERTICAL_GRAVITY_MASK) {
            case Gravity.TOP:
                result = RoundedCornersTransformation.CornerType.TOP;
                break;
            case Gravity.BOTTOM:
                result = RoundedCornersTransformation.CornerType.BOTTOM;
                break;
            case Gravity.TOP | Gravity.BOTTOM:
                result = RoundedCornersTransformation.CornerType.ALL;
                break;
        }

        switch (gravity & Gravity.HORIZONTAL_GRAVITY_MASK) {
            case Gravity.LEFT:
                if (result == null || result == RoundedCornersTransformation.CornerType.ALL) {
                    result = RoundedCornersTransformation.CornerType.LEFT;
                } else if (result == RoundedCornersTransformation.CornerType.TOP) {
                    result = RoundedCornersTransformation.CornerType.TOP_LEFT;
                } else {
                    result = RoundedCornersTransformation.CornerType.BOTTOM_LEFT;
                }
                break;
            case Gravity.RIGHT:
                if (result == null || result == RoundedCornersTransformation.CornerType.ALL) {
                    result = RoundedCornersTransformation.CornerType.RIGHT;
                } else if (result == RoundedCornersTransformation.CornerType.TOP) {
                    result = RoundedCornersTransformation.CornerType.TOP_RIGHT;
                } else {
                    result = RoundedCornersTransformation.CornerType.BOTTOM_RIGHT;
                }
                break;
            case Gravity.LEFT | Gravity.RIGHT:
                if (result == null || result == RoundedCornersTransformation.CornerType.ALL) {
                    result = RoundedCornersTransformation.CornerType.ALL;
                } else if (result == RoundedCornersTransformation.CornerType.TOP) {
                    result = RoundedCornersTransformation.CornerType.TOP;
                } else {
                    result = RoundedCornersTransformation.CornerType.BOTTOM;
                }
                break;
        }
        return result;

    }

    private float radius;
    private float diameter;
    private float margin;
    private CornerType cornerType;

    private ScaleType scaleType;

    private void setCanvasBitmapDensity(@NonNull Bitmap toTransform, @NonNull Bitmap canvasBitmap) {
        canvasBitmap.setDensity(toTransform.getDensity());
    }

    @Override
    protected Bitmap transform(@NonNull BitmapPool pool, @NonNull Bitmap toTransform, int outWidth, int outHeight) {

        Bitmap bitmap;
        switch (scaleType) {
            case CENTER_CROP:
                bitmap = TransformationUtils.centerCrop(pool, toTransform, outWidth, outHeight);
                break;
            case CENTER_INSIDE:
                bitmap = TransformationUtils.centerInside(pool, toTransform, outWidth, outHeight);
                break;
            case FIT_CENTER:
            default:
                bitmap = TransformationUtils.fitCenter(pool, toTransform, outWidth, outHeight);
                break;
        }

        return roundCrop(pool, bitmap);
    }

    public RoundedCornersTransformation(@Px int radius, @Px int margin) {
        this(radius, margin, CornerType.ALL, ScaleType.FIT_CENTER);
    }

    public RoundedCornersTransformation(@Px int radius, @Px int margin, CornerType cornerType, ScaleType scaleType) {
        this.radius = radius;
        this.diameter = this.radius * 2;
        this.margin = margin;
        this.cornerType = cornerType;
        this.scaleType = scaleType;

    }

    private Bitmap roundCrop(BitmapPool pool, Bitmap source) {
        if (source == null) return null;

        Bitmap result = pool.get(source.getWidth(), source.getHeight(), Bitmap.Config.ARGB_8888);

        result.setHasAlpha(true);

        setCanvasBitmapDensity(source, result);

        Canvas canvas = new Canvas(result);
        canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
        Paint paint = new Paint();
        paint.setAntiAlias(true);

        paint.setShader(new BitmapShader(source, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));

        drawRoundRect(canvas, paint, source.getWidth(), source.getHeight());
        return result;
    }

    private void drawRoundRect(Canvas canvas, Paint paint, float width, float height) {
        float right = width - margin;
        float bottom = height - margin;

        switch (cornerType) {
            case ALL:
                canvas.drawRoundRect(new RectF(margin, margin, right, bottom), radius, radius, paint);
                break;
            case TOP_LEFT:
                drawTopLeftRoundRect(canvas, paint, right, bottom);
                break;
            case TOP_RIGHT:
                drawTopRightRoundRect(canvas, paint, right, bottom);
                break;
            case BOTTOM_LEFT:
                drawBottomLeftRoundRect(canvas, paint, right, bottom);
                break;
            case BOTTOM_RIGHT:
                drawBottomRightRoundRect(canvas, paint, right, bottom);
                break;
            case TOP:
                drawTopRoundRect(canvas, paint, right, bottom);
                break;
            case BOTTOM:
                drawBottomRoundRect(canvas, paint, right, bottom);
                break;
            case LEFT:
                drawLeftRoundRect(canvas, paint, right, bottom);
                break;
            case RIGHT:
                drawRightRoundRect(canvas, paint, right, bottom);
                break;
            case OTHER_TOP_LEFT:
                drawOtherTopLeftRoundRect(canvas, paint, right, bottom);
                break;
            case OTHER_TOP_RIGHT:
                drawOtherTopRightRoundRect(canvas, paint, right, bottom);
                break;
            case OTHER_BOTTOM_LEFT:
                drawOtherBottomLeftRoundRect(canvas, paint, right, bottom);
                break;
            case OTHER_BOTTOM_RIGHT:
                drawOtherBottomRightRoundRect(canvas, paint, right, bottom);
                break;
            case DIAGONAL_FROM_TOP_LEFT:
                drawDiagonalFromTopLeftRoundRect(canvas, paint, right, bottom);
                break;
            case DIAGONAL_FROM_TOP_RIGHT:
                drawDiagonalFromTopRightRoundRect(canvas, paint, right, bottom);
                break;
            default:
                canvas.drawRoundRect(new RectF(margin, margin, right, bottom), radius, radius, paint);
                break;
        }
    }

    private void drawTopLeftRoundRect(Canvas canvas, Paint paint, float right, float bottom) {
        canvas.drawRoundRect(new RectF(margin, margin, margin + diameter, margin + diameter), radius,
                radius, paint);
        canvas.drawRect(new RectF(margin, margin + radius, margin + radius, bottom), paint);
        canvas.drawRect(new RectF(margin + radius, margin, right, bottom), paint);
    }

    private void drawTopRightRoundRect(Canvas canvas, Paint paint, float right, float bottom) {
        canvas.drawRoundRect(new RectF(right - diameter, margin, right, margin + diameter), radius,
                radius, paint);
        canvas.drawRect(new RectF(margin, margin, right - radius, bottom), paint);
        canvas.drawRect(new RectF(right - radius, margin + radius, right, bottom), paint);
    }

    private void drawBottomLeftRoundRect(Canvas canvas, Paint paint, float right, float bottom) {
        canvas.drawRoundRect(new RectF(margin, bottom - diameter, margin + diameter, bottom), radius,
                radius, paint);
        canvas.drawRect(new RectF(margin, margin, margin + diameter, bottom - radius), paint);
        canvas.drawRect(new RectF(margin + radius, margin, right, bottom), paint);
    }

    private void drawBottomRightRoundRect(Canvas canvas, Paint paint, float right, float bottom) {
        canvas.drawRoundRect(new RectF(right - diameter, bottom - diameter, right, bottom), radius,
                radius, paint);
        canvas.drawRect(new RectF(margin, margin, right - radius, bottom), paint);
        canvas.drawRect(new RectF(right - radius, margin, right, bottom - radius), paint);
    }

    private void drawTopRoundRect(Canvas canvas, Paint paint, float right, float bottom) {
        canvas.drawRoundRect(new RectF(margin, margin, right, margin + diameter), radius, radius,
                paint);
        canvas.drawRect(new RectF(margin, margin + radius, right, bottom), paint);
    }

    private void drawBottomRoundRect(Canvas canvas, Paint paint, float right, float bottom) {
        canvas.drawRoundRect(new RectF(margin, bottom - diameter, right, bottom), radius, radius,
                paint);
        canvas.drawRect(new RectF(margin, margin, right, bottom - radius), paint);
    }

    private void drawLeftRoundRect(Canvas canvas, Paint paint, float right, float bottom) {
        canvas.drawRoundRect(new RectF(margin, margin, margin + diameter, bottom), radius, radius,
                paint);
        canvas.drawRect(new RectF(margin + radius, margin, right, bottom), paint);
    }

    private void drawRightRoundRect(Canvas canvas, Paint paint, float right, float bottom) {
        canvas.drawRoundRect(new RectF(right - diameter, margin, right, bottom), radius, radius, paint);
        canvas.drawRect(new RectF(margin, margin, right - radius, bottom), paint);
    }

    private void drawOtherTopLeftRoundRect(Canvas canvas, Paint paint, float right, float bottom) {
        canvas.drawRoundRect(new RectF(margin, bottom - diameter, right, bottom), radius, radius,
                paint);
        canvas.drawRoundRect(new RectF(right - diameter, margin, right, bottom), radius, radius, paint);
        canvas.drawRect(new RectF(margin, margin, right - radius, bottom - radius), paint);
    }

    private void drawOtherTopRightRoundRect(Canvas canvas, Paint paint, float right, float bottom) {
        canvas.drawRoundRect(new RectF(margin, margin, margin + diameter, bottom), radius, radius,
                paint);
        canvas.drawRoundRect(new RectF(margin, bottom - diameter, right, bottom), radius, radius,
                paint);
        canvas.drawRect(new RectF(margin + radius, margin, right, bottom - radius), paint);
    }

    private void drawOtherBottomLeftRoundRect(Canvas canvas, Paint paint, float right, float bottom) {
        canvas.drawRoundRect(new RectF(margin, margin, right, margin + diameter), radius, radius,
                paint);
        canvas.drawRoundRect(new RectF(right - diameter, margin, right, bottom), radius, radius, paint);
        canvas.drawRect(new RectF(margin, margin + radius, right - radius, bottom), paint);
    }

    private void drawOtherBottomRightRoundRect(Canvas canvas, Paint paint, float right,
                                               float bottom) {
        canvas.drawRoundRect(new RectF(margin, margin, right, margin + diameter), radius, radius,
                paint);
        canvas.drawRoundRect(new RectF(margin, margin, margin + diameter, bottom), radius, radius,
                paint);
        canvas.drawRect(new RectF(margin + radius, margin + radius, right, bottom), paint);
    }

    private void drawDiagonalFromTopLeftRoundRect(Canvas canvas, Paint paint, float right,
                                                  float bottom) {
        canvas.drawRoundRect(new RectF(margin, margin, margin + diameter, margin + diameter), radius,
                radius, paint);
        canvas.drawRoundRect(new RectF(right - diameter, bottom - diameter, right, bottom), radius,
                radius, paint);
        canvas.drawRect(new RectF(margin, margin + radius, right - diameter, bottom), paint);
        canvas.drawRect(new RectF(margin + diameter, margin, right, bottom - radius), paint);
    }

    private void drawDiagonalFromTopRightRoundRect(Canvas canvas, Paint paint, float right,
                                                   float bottom) {
        canvas.drawRoundRect(new RectF(right - diameter, margin, right, margin + diameter), radius,
                radius, paint);
        canvas.drawRoundRect(new RectF(margin, bottom - diameter, margin + diameter, bottom), radius,
                radius, paint);
        canvas.drawRect(new RectF(margin, margin, right - radius, bottom - radius), paint);
        canvas.drawRect(new RectF(margin + radius, margin + radius, right, bottom), paint);

    }

    @Override
    public String toString() {
        return "RoundedTransformation(radius=" + radius + ", margin=" + margin + ", diameter="
                + diameter + ", cornerType=" + cornerType.name() + ")";
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof RoundedCornersTransformation &&
                ((RoundedCornersTransformation) o).radius == radius &&
                ((RoundedCornersTransformation) o).diameter == diameter &&
                ((RoundedCornersTransformation) o).margin == margin &&
                ((RoundedCornersTransformation) o).cornerType == cornerType;
    }

    @Override
    public int hashCode() {
        return (int) (ID.hashCode() + radius * 10000 + diameter * 1000 + margin * 100 + cornerType.ordinal() * 10);
    }

    @Override
    public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {
        messageDigest.update((ID + radius + diameter + margin + cornerType).getBytes(CHARSET));
    }
}
