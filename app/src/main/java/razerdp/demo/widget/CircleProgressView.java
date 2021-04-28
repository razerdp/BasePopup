package razerdp.demo.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

import razerdp.basepopup.R;
import razerdp.demo.utils.NumberUtils;
import razerdp.demo.utils.ToolUtil;
import razerdp.demo.utils.UIHelper;


/**
 * Created by 大灯泡 on 2019/5/16
 * <p>
 * Description：
 */
public class CircleProgressView extends View implements View.OnClickListener {
    private static final String TAG = "CircleProgressView";


    private int circleSize;
    private int textSize;
    private int circleColor;
    private int textColor;
    private int strokeWidth;
    private int strokeColor;
    private int strokeMargin;


    private Paint circlePaint;
    private Paint strokePaint;
    private Paint textPaint;

    private RectF circleRect;

    private volatile int currentPresent;

    private boolean isFailed;

    private int defaultWidth;
    private int defaultHeight;

    private AlphaAnimation exitAnimation;
    private AlphaAnimation enterAnimation;

    private boolean isLoading;
    private OnFailedClickListener onFailedClickListener;


    public CircleProgressView(Context context) {
        this(context, null);
    }

    public CircleProgressView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray a = context.getTheme()
                .obtainStyledAttributes(
                        attrs,
                        R.styleable.CircleProgressView,
                        0,
                        0
                );
        circleSize = a.getDimensionPixelSize(
                R.styleable.CircleProgressView_inner_circle_size,
                0
        );
        textSize = a.getDimensionPixelSize(R.styleable.CircleProgressView_inner_text_size, 0);
        circleColor = a.getColor(R.styleable.CircleProgressView_inner_circle_color, 0);
        textColor = a.getColor(R.styleable.CircleProgressView_inner_text_color, 0);
        strokeWidth = a.getDimensionPixelSize(R.styleable.CircleProgressView_stroke_width, 0);
        strokeColor = a.getColor(R.styleable.CircleProgressView_stroke_color, 0);
        strokeMargin = a.getDimensionPixelSize(
                R.styleable.CircleProgressView_stroke_margin,
                0
        );
        currentPresent = a.getInt(R.styleable.CircleProgressView_current_progress, 0);
        a.recycle();
        initDefaultValueWhenEmpty(context);
        buildAnimation();
        initPaint();
    }

    private void initDefaultValueWhenEmpty(Context context) {
        if (circleSize == 0) circleSize = UIHelper.dip2px(30f);
        if (textSize == 0) textSize = UIHelper.sp2px(14);
        if (circleColor == 0) circleColor = 0xafffffff;
        if (textColor == 0) textColor = Color.WHITE;
        if (strokeWidth == 0) strokeWidth = UIHelper.dip2px(2f);
        if (strokeColor == 0) strokeColor = 0xafffffff;
        if (strokeMargin == 0) strokeMargin = UIHelper.dip2px(5f);
    }

    private void buildAnimation() {
        exitAnimation = new AlphaAnimation(1.0f, 0.0f);
        exitAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
        exitAnimation.setDuration(500);
        //        exitAnimation.setFillAfter(true);
        exitAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                currentPresent = 100;
                postInvalidate();
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                setVisibility(GONE);
                if (!isFailed) reset();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        enterAnimation = new AlphaAnimation(0.0f, 1.0f);
        enterAnimation.setDuration(500);
        enterAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
        //        enterAnimation.setFillAfter(true);
        enterAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                setVisibility(VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void initPaint() {
        circlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        circlePaint.setColor(circleColor);
        circlePaint.setStyle(Paint.Style.FILL);

        strokePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        strokePaint.setColor(strokeColor);
        strokePaint.setStyle(Paint.Style.STROKE);
        strokePaint.setStrokeWidth(strokeWidth);

        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(textColor);
        textPaint.setTextSize(textSize);
        textPaint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        if (defaultWidth == 0) defaultWidth = w;
        if (defaultHeight == 0) defaultHeight = h;

        float horizontal_padding = (float) (getPaddingLeft() + getPaddingRight());
        float vertical_padding = (float) (getPaddingBottom() + getPaddingTop());

        float width = (float) w - horizontal_padding;
        float height = (float) h - vertical_padding;

        int strokeSpace = strokeMargin + strokeWidth + 1;

        if (circleRect == null) {
            circleRect = new RectF(
                    getPaddingLeft() + strokeSpace,
                    getPaddingTop() + strokeSpace,
                    getPaddingLeft() + width - strokeSpace,
                    getPaddingTop() + height - strokeSpace
            );
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (defaultWidth == 0) defaultWidth = getWidth();
        if (defaultHeight == 0) defaultHeight = getHeight();

        if (circleRect == null) {
            int strokeSpace = strokeMargin + strokeWidth + 1;
            circleRect = new RectF(
                    getPaddingLeft() + strokeSpace,
                    getPaddingTop() + strokeSpace,
                    getPaddingLeft() + getWidth() - strokeSpace,
                    getPaddingTop() + getHeight() - strokeSpace
            );
        }

        if (!isFailed) {
            //画饼图
            canvas.drawArc(circleRect, -90, currentPresent * 3.6f, true, circlePaint);
            //画线
            int radius = (int) Math.max(circleRect.width() / 2, circleRect.height() / 2);
            canvas.drawCircle(
                    circleRect.centerX(),
                    circleRect.centerY(),
                    radius + strokeMargin,
                    strokePaint
            );

            //文字，保证文字居中
            Paint.FontMetricsInt fontMetrics = textPaint.getFontMetricsInt();
            int baseline = (int) (circleRect.top + (circleRect.bottom - circleRect.top - fontMetrics.bottom + fontMetrics.top) / 2 - fontMetrics.top);
            textPaint.setTextAlign(Paint.Align.CENTER);
            canvas.drawText(currentPresent + "%", circleRect.centerX(), baseline, textPaint);
        } else {
            //文字，保证文字居中
            Paint.FontMetricsInt fontMetrics = textPaint.getFontMetricsInt();
            int baseline = (int) (circleRect.top + (circleRect.bottom - circleRect.top - fontMetrics.bottom + fontMetrics.top) / 2 - fontMetrics.top);
            textPaint.setTextAlign(Paint.Align.CENTER);
            canvas.drawText("加载失败，点我重新加载", getWidth() >> 1, baseline, textPaint);
        }
    }

    public void reset() {
        isFailed = false;
        currentPresent = 0;
        if (defaultWidth != 0 && defaultHeight != 0 && getLayoutParams().width != defaultWidth) {
            getLayoutParams().width = defaultWidth;
            getLayoutParams().height = defaultHeight;
            setLayoutParams(getLayoutParams());
        }
        textPaint.setTextSize(textSize);
        onFailedClickListener = null;
        callInvalidate();
    }

    public void start() {
        if (isLoading) return;
        isLoading = true;
        post(new Runnable() {
            @Override
            public void run() {
                reset();
                if (getAnimation() != null) {
                    clearAnimation();
                }
                startAnimation(enterAnimation);
            }
        });
    }

    public void finish(boolean needAnima) {
        isLoading = false;
        if (needAnima) {
            post(new Runnable() {
                @Override
                public void run() {
                    if (getAnimation() != null) {
                        clearAnimation();
                    }
                    startAnimation(exitAnimation);
                }
            });
        } else {
            post(new Runnable() {
                @Override
                public void run() {
                    setVisibility(GONE);
                    reset();
                }
            });
        }
    }

    public void setFailed() {
        post(new Runnable() {
            @Override
            public void run() {
                isFailed = true;
                isLoading = false;
                if (getAnimation() != null) clearAnimation();
                if (defaultWidth != 0) {
                    int screenWidth = UIHelper.getScreenWidth();
                    getLayoutParams().width = defaultWidth * 3 >= screenWidth ? screenWidth : defaultWidth * 3;
                    setLayoutParams(getLayoutParams());
                }
                textPaint.setTextSize(30);
                setOnClickListener(CircleProgressView.this);
                callInvalidate();
                setAlpha(1.0f);
                setVisibility(VISIBLE);
            }
        });
    }

    private void callInvalidate() {
        if (ToolUtil.isMainThread()) {
            invalidate();
        } else {
            postInvalidate();
        }
    }

    /**
     * =============================================================
     * setter/getter
     */
    public int getCircleSize() {
        return circleSize;
    }

    public void setCircleSize(int circleSize) {
        this.circleSize = circleSize;
        postInvalidate();
    }

    public int getTextSize() {
        return textSize;
    }

    public void setTextSize(int textSize) {
        this.textSize = textSize;
        postInvalidate();
    }

    public int getCircleColor() {
        return circleColor;
    }

    public void setCircleColor(int circleColor) {
        this.circleColor = circleColor;
        postInvalidate();
    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
        postInvalidate();
    }

    public int getStrokeWidth() {
        return strokeWidth;
    }

    public void setStrokeWidth(int strokeWidth) {
        this.strokeWidth = strokeWidth;
        postInvalidate();
    }

    public int getStrokeColor() {
        return strokeColor;
    }

    public void setStrokeColor(int strokeColor) {
        this.strokeColor = strokeColor;
        postInvalidate();
    }

    public int getStrokeMargin() {
        return strokeMargin;
    }

    public void setStrokeMargin(int strokeMargin) {
        this.strokeMargin = strokeMargin;
        postInvalidate();
    }

    public Paint getCirclePaint() {
        return circlePaint;
    }

    public void setCirclePaint(Paint circlePaint) {
        this.circlePaint = circlePaint;
        postInvalidate();
    }

    public Paint getStrokePaint() {
        return strokePaint;
    }

    public void setStrokePaint(Paint strokePaint) {
        this.strokePaint = strokePaint;
        postInvalidate();
    }

    public int getCurrentPresent() {
        return currentPresent;
    }

    public void setProgress(int currentPresent) {
        if (!isLoading) return;
        if (currentPresent != this.currentPresent) {
            this.currentPresent = NumberUtils.range(currentPresent, 0, 100);
            callInvalidate();
        }
    }

    public boolean isLoading() {
        return isLoading;
    }

    public boolean isFailed() {
        return isFailed;
    }


    @Override
    public void onClick(View v) {

    }

    public interface OnFailedClickListener {
        void onFailedClick(View v);
    }
}
