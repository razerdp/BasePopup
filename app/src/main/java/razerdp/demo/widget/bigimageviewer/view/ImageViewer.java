/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2018 Piasy
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package razerdp.demo.widget.bigimageviewer.view;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Looper;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.request.transition.Transition;
import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.github.chrisbanes.photoview.PhotoView;

import java.io.File;

import razerdp.demo.base.imageloader.GlideProgressManager;
import razerdp.demo.base.imageloader.ImageLoaderManager;
import razerdp.demo.utils.ImageUtil;
import razerdp.demo.utils.UIHelper;
import razerdp.demo.widget.CircleProgressView;
import razerdp.demo.widget.bigimageviewer.loader.glide.ImageDownloadTarget;

/**
 * 只针对glide使用
 */

@Keep
public class ImageViewer extends FrameLayout implements View.OnClickListener {
    static final long PROGRESS_BAR_DELAY = 500;
    static final long THUMBNAIL_BAR_DELAY = 500;
    private PhotoView mNormalView;
    private SubsamplingScaleImageView mLargeView;
    private CircleProgressView mProgressView;
    private OnClickListener mOnClickListener;
    private ImageLoadCallback cb;

    private View showingView;

    private Drawable mErrorDrawable;

    private boolean inLarge = false;

    Runnable showLoadingRunnable;

    private long thumbnailTime;

    public ImageViewer(@NonNull Context context) {
        this(context, null);
    }

    public ImageViewer(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ImageViewer(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    void init() {
        mNormalView = new PhotoView(getContext());
        mNormalView.setMaximumScale(4f);
        mNormalView.setZoomTransitionDuration(350);
        mNormalView.setOnClickListener(this);
        addViewInLayout(mNormalView,
                        -1,
                        new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT),
                        true);
        mProgressView = new CircleProgressView(getContext());
        FrameLayout.LayoutParams p = new FrameLayout.LayoutParams(UIHelper.dip2px(80), UIHelper.dip2px(80));
        p.gravity = Gravity.CENTER;
        addViewInLayout(mProgressView, -1, p, true);
        requestLayout();
        showingView = mNormalView;
    }


    void initLargeView() {
        if (mLargeView != null) {
            if (mLargeView.getParent() == null) {
                addViewInLayout(mLargeView,
                                -1,
                                new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT),
                                true);
            }
            return;
        }
        mLargeView = new SubsamplingScaleImageView(getContext());
        mLargeView.setOnClickListener(this);
        mLargeView.setMinimumTileDpi(160);
        mLargeView.setMinimumScaleType(SubsamplingScaleImageView.SCALE_TYPE_CENTER_INSIDE);
        addViewInLayout(mLargeView,
                        -1,
                        new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT),
                        true);
    }


    public void loadImage(Uri pic, @Nullable Uri thumbnail) {
        displayThumbnail(thumbnail);
        if (cb != null) {
            cb.onShowThumbnail();
        }
        GlideProgressManager.getsInstance()
                            .loadImage(pic.hashCode(), pic, new ImageDownloadTarget(pic
                                                                                            .toString()) {
                                @Override
                                public void onDownloadStart() {
                                    if (mProgressView != null) {
                                        showLoadingRunnable = () -> {
                                            if (mProgressView != null) {
                                                mProgressView.start();
                                            }
                                            showLoadingRunnable = null;
                                        };
                                        postDelayed(showLoadingRunnable, PROGRESS_BAR_DELAY);
                                    }
                                    runOnUI(() -> {
                                        if (cb != null) {
                                            cb.onStart();
                                        }
                                    });

                                }

                                @Override
                                public void onResourceReady(@NonNull File resource, Transition<? super File> transition) {
                                    super.onResourceReady(resource, transition);
                                    runOnUI(() -> {
                                        if (showLoadingRunnable != null) {
                                            removeCallbacks(showLoadingRunnable);
                                        }
                                        displayImage(resource);
                                        if (cb != null) {
                                            cb.onSuccess();
                                        }
                                    });
                                }


                                @Override
                                public void onLoadFailed(Drawable errorDrawable) {
                                    super.onLoadFailed(errorDrawable);
                                    runOnUI(() -> {
                                        if (showLoadingRunnable != null) {
                                            removeCallbacks(showLoadingRunnable);
                                        }
                                        displayErrorDrawable();
                                        if (cb != null) {
                                            cb.onError();
                                        }
                                    });
                                }

                                @Override
                                public void onProgress(int progress) {
                                    runOnUI(() -> {
                                        if (mProgressView == null) return;
                                        mProgressView.setProgress(progress);
                                    });
                                }

                                @Override
                                public void onDownloadFinish() {
                                    runOnUI(() -> {
                                        if (showLoadingRunnable != null) {
                                            removeCallbacks(showLoadingRunnable);
                                        }
                                        if (mProgressView != null) {
                                            mProgressView.finish(true);
                                        }
                                        if (cb != null) {
                                            cb.onFinish();
                                        }
                                    });
                                }
                            });
    }

    void displayThumbnail(Uri thumbnail) {
        if (thumbnail == null || thumbnail == Uri.EMPTY) return;
        mNormalView.setScaleX(.5f);
        mNormalView.setScaleY(.5f);
        ImageLoaderManager.INSTANCE
                .loadImage(mNormalView, thumbnail);
        showingView = mNormalView;
        thumbnailTime=System.currentTimeMillis();
    }

    void displayImage(File image) {
        boolean cacheHit = System.currentTimeMillis() - thumbnailTime <=THUMBNAIL_BAR_DELAY;
        if (mProgressView != null) {
            mProgressView.finish(true);
        }
        this.inLarge = detectInLargeMode(image, ImageUtil.getImageType(image));
        if (inLarge) {
            removeViewInLayout(mNormalView);
            initLargeView();
            mLargeView.setImage(ImageSource.uri(Uri.fromFile(image)));
            showingView = mLargeView;
        } else {
            if (mLargeView != null) {
                removeViewInLayout(mLargeView);
            }
            if (cacheHit){
                mNormalView.setScaleX(1f);
                mNormalView.setScaleY(1f);
            }
            ImageLoaderManager
                    .INSTANCE
                    .option()
                    .setLoading(new ColorDrawable(Color.TRANSPARENT))
                    .loadImage(mNormalView, image);
            showingView = mNormalView;
            if (!cacheHit) {
                mNormalView.animate()
                           .scaleX(1f)
                           .scaleY(1f)
                           .setStartDelay(300)
                           .start();
            }
        }
        requestLayout();
    }

    void displayErrorDrawable() {
        if (mLargeView != null && mLargeView.getParent() != null) {
            removeViewInLayout(mLargeView);
        }
        if (mNormalView.getParent() == null) {
            addViewInLayout(mNormalView,
                            -1,
                            new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT),
                            true);
        }
        mNormalView.setScaleX(1);
        mNormalView.setScaleY(1);
        ImageLoaderManager.INSTANCE
                .loadImage(mNormalView, mErrorDrawable);
        requestLayout();
        showingView = mNormalView;
    }

    public ImageViewer setImageLoadCallback(ImageLoadCallback cb) {
        this.cb = cb;
        return this;
    }

    public View getShowingView() {
        return showingView;
    }

    public void setErrorDrawable(Drawable drawable) {
        this.mErrorDrawable = drawable;
    }

    boolean detectInLargeMode(File imageFile, ImageUtil.ImageType imageType) {
        if (imageFile == null || !imageFile.exists()) return false;
        if (imageType == ImageUtil.ImageType.GIF) return false;
        int screenWidth = UIHelper.getScreenWidth();
        int screenHeight = UIHelper.getScreenHeight();

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imageFile.getPath(), options);
        int outHeight = options.outHeight;
        int outWidth = options.outWidth;

        return outWidth > (screenWidth * 1.5f) || outHeight > (screenHeight * 1.5f);

    }

    @Override
    public void setOnClickListener(@Nullable OnClickListener l) {
        this.mOnClickListener = l;
    }

    @Override
    public void onClick(View v) {
        if (mOnClickListener != null) {
            mOnClickListener.onClick(v);
        }
    }

    void runOnUI(Runnable runnable) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            runnable.run();
        } else {
            post(runnable);
        }
    }

}
