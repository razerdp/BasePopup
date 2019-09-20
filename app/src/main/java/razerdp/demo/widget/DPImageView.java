package razerdp.demo.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatImageView;

import razerdp.basepopup.R;


/**
 * Created by 大灯泡 on 2019/4/23
 * <p>
 * Description：带边框的imageview
 */
public class DPImageView extends AppCompatImageView {
    private static final String TAG = "DPImageView";

    public static final int SHAPE_OVAL = 1;
    public static final int SHAPE_RECTANGE = 2;

    private static final int DEFAULT_BORDER_WIDTH = 0;
    private static final int DEFAULT_BORDER_COLOR = Color.BLACK;
    private static final boolean DEFAULT_BORDER_OVERLAY = true;

    private final RectF mDrawableRect = new RectF();
    private final RectF mBorderRect = new RectF();

    private Path mPath = new Path();

    private Paint mBorderPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);

    private int mBorderColor = DEFAULT_BORDER_COLOR;
    private int mBorderWidth = DEFAULT_BORDER_WIDTH;
    private boolean isOverLay = DEFAULT_BORDER_OVERLAY;
    private boolean mBorderIgnorePadding = false;

    int radius = 0;
    int topLeftRadius = radius;
    int topRightRadius = radius;
    int bottomLeftRadius = radius;
    int bottomRightRadius = radius;

    float[] radii = new float[]{topLeftRadius,
            topLeftRadius,
            topRightRadius,
            topRightRadius,
            bottomRightRadius,
            bottomRightRadius,
            bottomLeftRadius,
            bottomLeftRadius
    };
    float[] matrixValues = new float[9];
    private int shape = SHAPE_RECTANGE;

    public DPImageView(Context context) {
        this(context, null);
    }

    public DPImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DPImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.DPImageView);
        mBorderWidth = a.getDimensionPixelSize(R.styleable.DPImageView_biv_border_width, mBorderWidth);
        mBorderColor = a.getColor(R.styleable.DPImageView_biv_border_color, mBorderColor);
        isOverLay = a.getBoolean(R.styleable.DPImageView_biv_border_overlay, isOverLay);
        mBorderIgnorePadding = a.getBoolean(R.styleable.DPImageView_biv_border_ignore_padding, mBorderIgnorePadding);

        shape = a.getInt(R.styleable.DPImageView_biv_shape, shape);

        radius = a.getDimensionPixelOffset(R.styleable.DPImageView_biv_corner_radius, radius);
        topLeftRadius = a.getDimensionPixelOffset(R.styleable.DPImageView_biv_corner_topLeftRadius, topLeftRadius);
        topRightRadius = a.getDimensionPixelOffset(R.styleable.DPImageView_biv_corner_topRightRadius, topRightRadius);
        bottomLeftRadius = a.getDimensionPixelOffset(R.styleable.DPImageView_biv_corner_bottomLeftRadius, bottomLeftRadius);
        bottomRightRadius = a.getDimensionPixelOffset(R.styleable.DPImageView_biv_corner_bottomRightRadius, bottomRightRadius);

        a.recycle();

        mBorderPaint.setStyle(Paint.Style.STROKE);
        mBorderPaint.setAntiAlias(true);
        mBorderPaint.setColor(mBorderColor);
        mBorderPaint.setStrokeWidth(mBorderWidth);
    }

    @Override
    public void setPressed(boolean pressed) {
        super.setPressed(pressed);
        if (isClickable()) {
            if (pressed) {
                setColorFilter(0x80FFFFFF);
            } else {
                clearColorFilter();
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        calculateDrawableBounds();

        if (mBorderWidth <= 0 || mDrawableRect.isEmpty()) {
            if (!drawInEditMode(canvas)) {
                super.onDraw(canvas);
            }
            return;
        }
        if (!isOverLay) {
            drawBorder(canvas);
        }
        if (!drawInEditMode(canvas)) {
            super.onDraw(canvas);
        }

        if (isOverLay) {
            drawBorder(canvas);
        }
    }

    private boolean drawInEditMode(Canvas canvas) {
        if (!(getDrawable() instanceof BitmapDrawable) || shape != SHAPE_OVAL || !isInEditMode())
            return false;

        Paint mBitmapPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        int width = getWidth() - getPaddingLeft() - getPaddingRight();
        int height = getHeight() - getPaddingTop() - getPaddingBottom();
        mBitmapPaint.setShader(initBitmapShader((BitmapDrawable) getDrawable()));
        canvas.drawCircle(mDrawableRect.centerX(),
                mDrawableRect.centerY(),
                Math.min((mDrawableRect.height() - mBorderWidth) / 2.0f, (mDrawableRect.width() - mBorderWidth) / 2.0f),
                mBitmapPaint);
        return true;
    }

    /**
     * 获取ImageView中资源图片的Bitmap，利用Bitmap初始化图片着色器,通过缩放矩阵将原资源图片缩放到铺满整个绘制区域，避免边界填充
     */
    private BitmapShader initBitmapShader(BitmapDrawable drawable) {
        Bitmap bitmap = drawable.getBitmap();
        Matrix matrix = new Matrix();
        BitmapShader bitmapShader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);

        float scale;
        float dx = 0;
        float dy = 0;

        matrix.set(null);

        if (bitmap.getWidth() * mDrawableRect.height() > mDrawableRect.width() * bitmap.getHeight()) {
            scale = mDrawableRect.height() / (float) bitmap.getHeight();
            dx = (mDrawableRect.width() - bitmap.getWidth() * scale) * 0.5f;
        } else {
            scale = mDrawableRect.width() / (float) bitmap.getWidth();
            dy = (mDrawableRect.height() - bitmap.getHeight() * scale) * 0.5f;
        }

        matrix.setScale(scale, scale);
        matrix.postTranslate((int) (dx + 0.5f) + mDrawableRect.left, (int) (dy + 0.5f) + mDrawableRect.top);

        bitmapShader.setLocalMatrix(matrix);

        return bitmapShader;
    }

    private void drawBorder(Canvas canvas) {
        mPath.rewind();
        switch (shape) {
            case SHAPE_OVAL:
                mPath.addCircle(mDrawableRect.centerX(),
                        mDrawableRect.centerY(),
                        Math.min((mBorderRect.height() - mBorderWidth) / 2.0f, (mBorderRect.width() - mBorderWidth) / 2.0f),
                        Path.Direction.CCW);
                break;
            case SHAPE_RECTANGE:
                if (radius != 0) {
                    mPath.addRoundRect(mBorderRect, radius, radius, Path.Direction.CCW);
                } else if (topLeftRadius > 0 || topRightRadius > 0 || bottomLeftRadius > 0 || bottomRightRadius > 0) {
                    setToRadii();
                    mPath.addRoundRect(mBorderRect, radii, Path.Direction.CCW);
                } else {
                    mPath.addRect(mBorderRect, Path.Direction.CCW);
                }
                break;
        }
        canvas.drawPath(mPath, mBorderPaint);
    }

    private void setToRadii() {
        radii[0] = topLeftRadius;
        radii[1] = topLeftRadius;

        radii[2] = topRightRadius;
        radii[3] = topRightRadius;

        radii[4] = bottomRightRadius;
        radii[5] = bottomRightRadius;

        radii[6] = bottomLeftRadius;
        radii[7] = bottomLeftRadius;
    }

    private void calculateDrawableBounds() {
        if (getDrawable() == null) {
            mDrawableRect.setEmpty();
            return;
        }


        int availableWidth = getWidth() - getPaddingLeft() - getPaddingRight();
        int availableHeight = getHeight() - getPaddingTop() - getPaddingBottom();

        int sideLength = Math.min(availableWidth, availableHeight);

        float left = getPaddingLeft() + (availableWidth - sideLength) / 2f;
        float top = getPaddingTop() + (availableHeight - sideLength) / 2f;

        mDrawableRect.set(left, top, left + sideLength, top + sideLength);

        if (!mBorderIgnorePadding) {
            mBorderRect.set(mDrawableRect);
        } else {
            availableWidth = getWidth();
            availableHeight = getHeight();

            sideLength = Math.min(availableWidth, availableHeight);

            left = (availableWidth - sideLength) / 2f;
            top = (availableHeight - sideLength) / 2f;
            mBorderRect.set(left, top, left + sideLength, top + sideLength);
        }

    }

    public DPImageView setShape(int shape) {
        this.shape = shape;
        invalidate();
        return this;
    }

    public DPImageView setBorderColor(int mBorderColor) {
        this.mBorderColor = mBorderColor;
        mBorderPaint.setColor(mBorderColor);
        invalidate();
        return this;
    }

    public DPImageView setBorderWidth(int mBorderWidth) {
        this.mBorderWidth = mBorderWidth;
        mBorderPaint.setStrokeWidth(mBorderWidth);
        invalidate();
        return this;
    }

    public DPImageView setOverLay(boolean overLay) {
        isOverLay = overLay;
        invalidate();
        return this;
    }

    public DPImageView setRadius(int radius) {
        this.radius = radius;
        invalidate();
        return this;
    }

    public DPImageView setTopLeftRadius(int topLeftRadius) {
        this.topLeftRadius = topLeftRadius;
        invalidate();
        return this;
    }

    public DPImageView setTopRightRadius(int topRightRadius) {
        this.topRightRadius = topRightRadius;
        invalidate();
        return this;
    }

    public DPImageView setBottomLeftRadius(int bottomLeftRadius) {
        this.bottomLeftRadius = bottomLeftRadius;
        invalidate();
        return this;
    }

    public DPImageView setBottomRightRadius(int bottomRightRadius) {
        this.bottomRightRadius = bottomRightRadius;
        invalidate();
        return this;
    }

    public int getRadius() {
        return radius;
    }

    public int getTopLeftRadius() {
        return topLeftRadius;
    }

    public int getTopRightRadius() {
        return topRightRadius;
    }

    public int getBottomLeftRadius() {
        return bottomLeftRadius;
    }

    public int getBottomRightRadius() {
        return bottomRightRadius;
    }

    public int getShape() {
        return shape;
    }

    public DPImageView setBorderIgnorePadding(boolean mBorderIgnorePadding) {
        this.mBorderIgnorePadding = mBorderIgnorePadding;
        invalidate();
        return this;
    }

//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        switch (event.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//                setColorFilter(0xBFFFFFFF);
//                break;
//            case MotionEvent.ACTION_UP:
//            case MotionEvent.ACTION_SCROLL:
//            case MotionEvent.ACTION_OUTSIDE:
//            case MotionEvent.ACTION_CANCEL:
//                setColorFilter(null);
//                break;
//        }
//        return super.onTouchEvent(event);
//    }

}
