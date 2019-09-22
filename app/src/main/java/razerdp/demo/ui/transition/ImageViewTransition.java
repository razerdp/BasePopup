package razerdp.demo.ui.transition;

import android.animation.Animator;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.transition.Transition;
import android.transition.TransitionValues;
import android.util.AttributeSet;
import android.util.Pair;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.Map;

import androidx.annotation.RequiresApi;

/**
 * Created by 大灯泡 on 2019/8/22
 * <p>
 * Description：
 */
@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class ImageViewTransition extends Transition {
    private static final String PROPNAME_SCALE_TYPE = "hw:changeImageTransform:scaletype";
    private static final String PROPNAME_BOUNDS = "hw:changeImageTransform:bounds";
    private static final String PROPNAME_MATRIX = "hw:changeImageTransform:matrix";

    public ImageViewTransition() {
        addTarget(ImageView.class);
    }

    public ImageViewTransition(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void captureStartValues(TransitionValues transitionValues) {
        captureBoundsAndInfo(transitionValues);
    }

    @Override
    public void captureEndValues(TransitionValues transitionValues) {
        captureBoundsAndInfo(transitionValues);
    }

    private void captureBoundsAndInfo(TransitionValues transitionValues) {
        View view = transitionValues.view;
        if (!(view instanceof ImageView) || view.getVisibility() != View.VISIBLE || isFrescoView(view)) {
            return;
        }
        ImageView imageView = (ImageView) view;
        Drawable drawable = imageView.getDrawable();
        if (drawable == null) {
            return;
        }
        Map<String, Object> values = transitionValues.values;
        int left = view.getLeft();
        int top = view.getTop();
        int right = view.getRight();
        int bottom = view.getBottom();

        Rect bounds = new Rect(left, top, right, bottom);
        values.put(PROPNAME_BOUNDS, bounds);
        values.put(PROPNAME_SCALE_TYPE, imageView.getScaleType());
        if (imageView.getScaleType() == ImageView.ScaleType.MATRIX) {
            values.put(PROPNAME_MATRIX, imageView.getImageMatrix());
        }
    }

    protected void calculateMatrix(TransitionValues startValues, TransitionValues endValues, int imageWidth, int imageHeight, Matrix startMatrix, Matrix endMatrix) {
        if (startValues == null || endValues == null || startMatrix == null || endMatrix == null) {
            return;
        }
        Rect startBounds = (Rect) startValues.values.get(PROPNAME_BOUNDS);
        Rect endBounds = (Rect) endValues.values.get(PROPNAME_BOUNDS);

        ImageView.ScaleType startScaleType = (ImageView.ScaleType) startValues.values.get(PROPNAME_SCALE_TYPE);
        ImageView.ScaleType endScaleType = (ImageView.ScaleType) endValues.values.get(PROPNAME_SCALE_TYPE);

        if (startScaleType == ImageView.ScaleType.MATRIX) {
            startMatrix.set((Matrix) startValues.values.get(PROPNAME_MATRIX));
        } else {
            startMatrix.set(getImageViewMatrix(startBounds, startScaleType, imageWidth, imageHeight));
        }

        if (endScaleType == ImageView.ScaleType.MATRIX) {
            endMatrix.set((Matrix) endValues.values.get(PROPNAME_MATRIX));
        } else {
            //这里要计算的是如何给出的ImageView模拟出结束状态的ImageView
            endMatrix.set(getImageViewMatrix(endBounds, endScaleType, imageWidth, imageHeight));
        }
    }

    @Override
    public Animator createAnimator(ViewGroup sceneRoot, TransitionValues startValues, TransitionValues endValues) {
        if (startValues == null || endValues == null || !(endValues.view instanceof ImageView)) {
            return null;
        }
        Rect startBounds = (Rect) startValues.values.get(PROPNAME_BOUNDS);
        Rect endBounds = (Rect) endValues.values.get(PROPNAME_BOUNDS);
        final ImageView imageView = (ImageView) endValues.view;
        if (startBounds == null || endBounds == null) {
            return null;
        }
        if (startBounds.equals(endBounds)) {
            return null;
        }
        return createMatrixAnimator(imageView, startValues, endValues);
    }

    private ValueAnimator createMatrixAnimator(final ImageView imageView, final TransitionValues startValues, final TransitionValues endValues) {
        ValueAnimator animator = ValueAnimator.ofFloat(0f, 1f);

        final SparseArray<Pair<Matrix, Matrix>> matrixArray = new SparseArray<>(2);
        final MatrixEvaluator evaluator = new MatrixEvaluator();
        final ImageView.ScaleType scaleType = imageView.getScaleType();
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                //没有内容，无需做矩阵动画
                if (imageView.getDrawable() == null) {
                    return;
                }
                if (!(imageView.getDrawable() instanceof BitmapDrawable)) {
                    return;
                }
                BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
                if (drawable.getIntrinsicWidth() == 0 || drawable.getIntrinsicHeight() == 0) {
                    return;
                }
                //是否已经计算过了
                int key = drawable.hashCode();
                Pair<Matrix, Matrix> matrixPair = matrixArray.get(key);
                if (matrixPair == null) {
                    //计算对应的变化矩阵
                    Matrix startMatrix = new Matrix();
                    Matrix endMatrix = new Matrix();
                    calculateMatrix(startValues, endValues, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), startMatrix, endMatrix);
                    matrixPair = new Pair<>(startMatrix, endMatrix);
                    matrixArray.put(key, matrixPair);
                }
                //计算中间矩阵
                Matrix imageMatrix = evaluator.evaluate(animation.getAnimatedFraction(), matrixPair.first, matrixPair.second);
//                imageView.setScaleType(ImageView.ScaleType.MATRIX);
                imageView.setImageMatrix(imageMatrix);

            }
        });
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
//                imageView.setScaleType(ImageView.ScaleType.MATRIX);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                imageView.setScaleType(scaleType);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        return animator;
    }

    public static class MatrixEvaluator implements TypeEvaluator<Matrix> {

        float[] mTempStartValues = new float[9];

        float[] mTempEndValues = new float[9];

        Matrix mTempMatrix = new Matrix();

        @Override
        public Matrix evaluate(float fraction, Matrix startValue, Matrix endValue) {
            startValue.getValues(mTempStartValues);
            endValue.getValues(mTempEndValues);
            for (int i = 0; i < 9; i++) {
                float diff = mTempEndValues[i] - mTempStartValues[i];
                mTempEndValues[i] = mTempStartValues[i] + (fraction * diff);
            }
            mTempMatrix.setValues(mTempEndValues);
            return mTempMatrix;
        }
    }

    private Matrix getImageViewMatrix(Rect bounds, ImageView.ScaleType scaleType, int contentWidth, int contentHeight) {
        Matrix matrix = new Matrix();
        final int dwidth = contentWidth;
        final int dheight = contentHeight;

        final int vwidth = bounds.width();
        final int vheight = bounds.height();

        final boolean fits = (dwidth < 0 || vwidth == dwidth)
                && (dheight < 0 || vheight == dheight);

        if (dwidth <= 0 || dheight <= 0 || ImageView.ScaleType.FIT_XY == scaleType) {
            //默认Matrix
        } else {
            // We need to do the scaling ourself, so have the drawable
            // use its native size.
            if (ImageView.ScaleType.MATRIX == scaleType) {
                //调用方处理
                throw new RuntimeException("ImageView.ScaleType.MATRIX == scaleType!!");
            } else if (fits) {
                // The bitmap fits exactly, no transform needed.
            } else if (ImageView.ScaleType.CENTER == scaleType) {
                // Center bitmap in view, no scaling.
                matrix.setTranslate(Math.round((vwidth - dwidth) * 0.5f),
                        Math.round((vheight - dheight) * 0.5f));
            } else if (ImageView.ScaleType.CENTER_CROP == scaleType) {

                float scale;
                float dx = 0, dy = 0;


                if (dwidth * vheight > vwidth * dheight) {
                    scale = (float) vheight / (float) dheight;
                    dx = (vwidth - dwidth * scale) * 0.5f;
                } else {
                    scale = (float) vwidth / (float) dwidth;
                    dy = (vheight - dheight * scale) * 0.5f;
                }

                matrix.setScale(scale, scale);
                matrix.postTranslate(Math.round(dx), Math.round(dy));
            } else if (ImageView.ScaleType.CENTER_INSIDE == scaleType) {
                float scale;
                float dx;
                float dy;

                if (dwidth <= vwidth && dheight <= vheight) {
                    scale = 1.0f;
                } else {
                    scale = Math.min((float) vwidth / (float) dwidth,
                            (float) vheight / (float) dheight);
                }

                dx = Math.round((vwidth - dwidth * scale) * 0.5f);
                dy = Math.round((vheight - dheight * scale) * 0.5f);

                matrix.setScale(scale, scale);
                matrix.postTranslate(dx, dy);
            } else {
                // Generate the required transform.
                RectF tempSrc = new RectF();
                RectF tempDst = new RectF();
                tempSrc.set(0, 0, dwidth, dheight);
                tempDst.set(0, 0, vwidth, vheight);

                matrix.setRectToRect(tempSrc, tempDst, scaleTypeToScaleToFit(scaleType));
            }
        }
        return matrix;
    }

    private static Matrix.ScaleToFit scaleTypeToScaleToFit(ImageView.ScaleType st) {
        // ScaleToFit enum to their corresponding Matrix.ScaleToFit values
        return sS2FArray[st.ordinal() - 1];
    }

    private static final Matrix.ScaleToFit[] sS2FArray = {
            Matrix.ScaleToFit.FILL,
            Matrix.ScaleToFit.START,
            Matrix.ScaleToFit.CENTER,
            Matrix.ScaleToFit.END
    };

    public boolean isFrescoView(View view) {
        return view.getClass().getName().startsWith("com.facebook.drawee");
    }
}