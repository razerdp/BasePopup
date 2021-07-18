package razerdp.demo.ui.transition;

import android.animation.Animator;
import android.animation.PropertyValuesHolder;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.PointF;
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

import androidx.annotation.RequiresApi;

import com.davemorrissey.labs.subscaleview.ImageViewState;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.github.chrisbanes.photoview.PhotoView;

import java.util.Map;

import razerdp.demo.widget.bigimageviewer.view.ImageViewer;
import razerdp.util.log.PopupLog;

/**
 * Created by 大灯泡 on 2021/4/1
 * <p>
 * Description：
 */
@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class ImageViewerTransition extends Transition {
    private static final String TAG = "ImageViewerTransition";

    private static final String SCALE_TYPE = "ImageViewerTransition:scaletype";
    private static final String BOUNDS = "ImageViewerTransition:bounds";
    private static final String MATRIX = "ImageViewerTransition:matrix";
    private static final String STATE = "ImageViewerTransition:state";
    private static final String SIZE = "ImageViewerTransition:size";
    private static final String CENTER_X = "ImageViewerTransition:transition:center_x";
    private static final String CENTER_Y = "ImageViewerTransition:transition:center_y";


    public ImageViewerTransition() {
        addTarget(ImageViewer.class);
        addTarget(ImageView.class);
    }

    public ImageViewerTransition(Context context, AttributeSet attrs) {
        super(context, attrs);
        addTarget(ImageViewer.class);
        addTarget(ImageView.class);
    }

    @Override
    public void captureStartValues(TransitionValues transitionValues) {
        captureValues(transitionValues);
    }

    @Override
    public void captureEndValues(TransitionValues transitionValues) {
        captureValues(transitionValues);
    }

    private void captureValues(TransitionValues transitionValues) {
        View v = transitionValues.view;
        if (v instanceof ImageViewer) {
            View showingView = ((ImageViewer) v).getShowingView();
            if (showingView instanceof SubsamplingScaleImageView) {
                captureValueForSSIV((SubsamplingScaleImageView) showingView, transitionValues);
            } else if (showingView instanceof ImageView) {
                captureValueForImageView((ImageView) showingView, transitionValues);
            }
        } else if (v instanceof ImageView) {
            captureValueForImageView((ImageView) v, transitionValues);
        }
    }

    void captureValueForSSIV(SubsamplingScaleImageView ssiv, TransitionValues transitionValues) {
        Point size = new Point(ssiv.getWidth(), ssiv.getHeight());
        transitionValues.values.put(SIZE, size);
        transitionValues.values.put(STATE, ssiv.getState());
    }

    void captureValueForImageView(ImageView iv, TransitionValues transitionValues) {
        Drawable drawable = iv.getDrawable();
        if (drawable == null) {
            return;
        }
        Map<String, Object> values = transitionValues.values;
        int left = iv.getLeft();
        int top = iv.getTop();
        int right = iv.getRight();
        int bottom = iv.getBottom();

        Rect bounds = new Rect(left, top, right, bottom);
        values.put(BOUNDS, bounds);
        values.put(SCALE_TYPE, iv.getScaleType());
        if (iv.getScaleType() == ImageView.ScaleType.MATRIX) {
            values.put(MATRIX, iv.getImageMatrix());
        }
    }

    @Override
    public Animator createAnimator(ViewGroup sceneRoot, TransitionValues startValues, TransitionValues endValues) {
        if (startValues == null ||
                endValues == null) {
            return null;
        }
        View startView = startValues.view;
        View endView = endValues.view;
        boolean isLarge = false;
        if (startView instanceof ImageViewer) {
            isLarge = ((ImageViewer) startView).getShowingView() instanceof SubsamplingScaleImageView;
        }
        if (endView instanceof ImageViewer) {
            isLarge = ((ImageViewer) endView).getShowingView() instanceof SubsamplingScaleImageView;
        }
        if (isLarge) {
            return SubsamplingScaleImageViewTransitionCreator.createAnimator(sceneRoot, startValues, endValues);
        } else {
            return ImageViewTransitionCreator.createAnimator(sceneRoot, startValues, endValues);
        }
    }


    static class SubsamplingScaleImageViewTransitionCreator {

        static Animator createAnimator(ViewGroup sceneRoot, TransitionValues startValues, TransitionValues endValues) {
            ImageViewState subsamplingState = (ImageViewState) startValues.values.get(STATE);
            Point startSize = (Point) startValues.values.get(SIZE);
            Point endSize = (Point) endValues.values.get(SIZE);
            if (startSize == null || endSize == null || subsamplingState == null || startSize.equals(endSize)) {
                return null;
            }
            final SubsamplingScaleImageView view = (SubsamplingScaleImageView) ((ImageViewer) startValues.view)
                    .getShowingView();
            Point imgSize = new Point(view.getSWidth(), view.getSHeight());
            if (imgSize.x == 0 || imgSize.y == 0) {
                return null;
            }

            boolean isEntering = startSize.x < endSize.x || startSize.y < endSize.y;

            final PointF centerFrom;
            float scaleFrom;
            float scaleTo;
            ValueAnimator valueAnimator;
            PointF centerTo = new PointF(imgSize.x / 2, imgSize.y / 2);

            if (isEntering) {
                centerFrom = new PointF(imgSize.x / 2, imgSize.y / 2);
                scaleFrom = getMinIfTrue(startSize.x / (float) imgSize.x, startSize.y / (float) imgSize.y,
                                         false);
                scaleTo = getMinIfTrue(imgSize.x / (float) endSize.x, imgSize.y / (float) endSize.y,
                                       false);
            } else {
                centerFrom = subsamplingState.getCenter();
                scaleFrom = subsamplingState.getScale();
                scaleTo = getMinIfTrue(endSize.x / (float) imgSize.x, endSize.y / (float) imgSize.y,
                                       false);

            }

            PropertyValuesHolder prop_scale = PropertyValuesHolder.ofFloat(SIZE, scaleFrom, scaleTo);
            PropertyValuesHolder prop_center_x = PropertyValuesHolder.ofFloat(CENTER_X, centerFrom.x, centerTo.x);
            PropertyValuesHolder prop_center_y = PropertyValuesHolder.ofFloat(CENTER_Y, centerFrom.y, centerTo.y);

            valueAnimator = ValueAnimator.ofPropertyValuesHolder(prop_scale, prop_center_x, prop_center_y);

            valueAnimator.addUpdateListener(animation -> {
                PointF newCenter = new PointF((float) animation.getAnimatedValue(CENTER_X), (float) animation
                        .getAnimatedValue(CENTER_Y));
                view.setScaleAndCenter((float) animation.getAnimatedValue(SIZE), newCenter);
            });
            return valueAnimator;
        }


        /**
         * Does {@link Math#min(float, float)} or {@link Math#max(float, float)} depending on boolean.
         *
         * @param val1        value to compare
         * @param val2        value to compare
         * @param isFitCenter condition to check
         * @return If <code>con</code> return minimum of 2 values, otherwise return max.
         */
        static float getMinIfTrue(float val1, float val2, boolean isFitCenter) {
            return isFitCenter ? Math.min(val1, val2) : Math.max(val1, val2);
        }
    }


    static class ImageViewTransitionCreator {
        static Animator createAnimator(ViewGroup sceneRoot, TransitionValues startValues, TransitionValues endValues) {
            Rect startBounds = (Rect) startValues.values.get(BOUNDS);
            Rect endBounds = (Rect) endValues.values.get(BOUNDS);
            ImageView imageView = null;
            if (endValues.view instanceof ImageViewer) {
                imageView = (ImageView) ((ImageViewer) endValues.view).getShowingView();
            } else if (endValues.view instanceof ImageView) {
                imageView = (ImageView) endValues.view;
            }
            if (imageView == null || startBounds == null || endBounds == null || startBounds.equals(endBounds)) {
                return null;
            }
            return createMatrixAnimator(imageView, startValues, endValues);
        }


        static void calculateMatrix(TransitionValues startValues, TransitionValues endValues, int imageWidth, int imageHeight, Matrix startMatrix, Matrix endMatrix) {
            if (startValues == null || endValues == null || startMatrix == null || endMatrix == null) {
                return;
            }
            Rect startBounds = (Rect) startValues.values.get(BOUNDS);
            Rect endBounds = (Rect) endValues.values.get(BOUNDS);

            ImageView.ScaleType startScaleType = (ImageView.ScaleType) startValues.values.get(SCALE_TYPE);
            ImageView.ScaleType endScaleType = (ImageView.ScaleType) endValues.values.get(SCALE_TYPE);

            if (startScaleType == ImageView.ScaleType.MATRIX) {
                startMatrix.set((Matrix) startValues.values.get(MATRIX));
            } else {
                startMatrix.set(getImageViewMatrix(startBounds, startScaleType, imageWidth, imageHeight));
            }

            if (endScaleType == ImageView.ScaleType.MATRIX) {
                endMatrix.set((Matrix) endValues.values.get(MATRIX));
            } else {
                //这里要计算的是如何给出的ImageView模拟出结束状态的ImageView
                endMatrix.set(getImageViewMatrix(endBounds, endScaleType, imageWidth, imageHeight));
            }
        }


        static ValueAnimator createMatrixAnimator(final ImageView imageView, final TransitionValues startValues, final TransitionValues endValues) {
            ValueAnimator animator = ValueAnimator.ofFloat(0f, 1f);

            final SparseArray<Pair<Matrix, Matrix>> matrixArray = new SparseArray<>(2);
            final ImageViewTransition.MatrixEvaluator evaluator = new ImageViewTransition.MatrixEvaluator();
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
                        calculateMatrix(startValues, endValues, drawable.getIntrinsicWidth(), drawable
                                .getIntrinsicHeight(), startMatrix, endMatrix);
                        matrixPair = new Pair<>(startMatrix, endMatrix);
                        matrixArray.put(key, matrixPair);
                    }
                    //计算中间矩阵
                    Matrix imageMatrix = evaluator.evaluate(animation.getAnimatedFraction(), matrixPair.first, matrixPair.second);
                    try {
                        imageView.setScaleType(ImageView.ScaleType.MATRIX);
                    } catch (Exception e) {
                        //ignore
                    }
                    imageView.setImageMatrix(imageMatrix);

                }
            });
            animator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                    try {
                        imageView.setScaleType(ImageView.ScaleType.MATRIX);
                    } catch (Exception e) {
                        //ignore
                    }
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    try {
                        imageView.setScaleType(scaleType);
                    } catch (Exception e) {
                        //ignore
                    }
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

        static Matrix getImageViewMatrix(Rect bounds, ImageView.ScaleType scaleType, int contentWidth, int contentHeight) {
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

        static final Matrix.ScaleToFit[] sS2FArray = {
                Matrix.ScaleToFit.FILL,
                Matrix.ScaleToFit.START,
                Matrix.ScaleToFit.CENTER,
                Matrix.ScaleToFit.END
        };

        boolean isFrescoView(View view) {
            return view.getClass().getName().startsWith("com.facebook.drawee");
        }
    }
}
