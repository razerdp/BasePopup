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

import android.Manifest;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.Keep;
import androidx.annotation.RequiresPermission;
import androidx.annotation.UiThread;
import androidx.appcompat.widget.AppCompatImageView;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.github.chrisbanes.photoview.PhotoView;

import java.io.File;
import java.io.FileNotFoundException;

import razerdp.basepopup.R;
import razerdp.demo.utils.ImageUtil;
import razerdp.demo.utils.UIHelper;
import razerdp.demo.widget.bigimageviewer.BigImageViewer;
import razerdp.demo.widget.bigimageviewer.indicator.ProgressIndicator;
import razerdp.demo.widget.bigimageviewer.loader.ImageLoader;
import razerdp.demo.widget.bigimageviewer.metadata.ImageInfoExtractor;
import razerdp.demo.widget.bigimageviewer.utils.DisplayOptimizeListener;
import razerdp.demo.widget.bigimageviewer.utils.ThreadedCallbacks;

/**
 * 修改自
 * https://github.com/Piasy/BigImageViewer/blob/master/BigImageViewer/src/main/java/com/github/piasy/biv/view/BigImageView.java
 * <p>
 * 只针对glide使用
 */

@Keep
public class ImageViewer extends FrameLayout implements ImageLoader.Callback {
    public static final int INIT_SCALE_TYPE_CENTER = 0;
    public static final int INIT_SCALE_TYPE_CENTER_CROP = 1;
    public static final int INIT_SCALE_TYPE_CENTER_INSIDE = 2;
    public static final int INIT_SCALE_TYPE_FIT_CENTER = 3;
    public static final int INIT_SCALE_TYPE_FIT_END = 4;
    public static final int INIT_SCALE_TYPE_FIT_START = 5;
    public static final int INIT_SCALE_TYPE_FIT_XY = 6;
    public static final int INIT_SCALE_TYPE_CUSTOM = 7;
    public static final int INIT_SCALE_TYPE_START = 8;

    public static final int DEFAULT_IMAGE_SCALE_TYPE = 3;
    public static final ImageView.ScaleType[] IMAGE_SCALE_TYPES = {
            ImageView.ScaleType.CENTER,
            ImageView.ScaleType.CENTER_CROP,
            ImageView.ScaleType.CENTER_INSIDE,
            ImageView.ScaleType.FIT_CENTER,
            ImageView.ScaleType.FIT_END,
            ImageView.ScaleType.FIT_START,
            ImageView.ScaleType.FIT_XY,
    };

    private final ImageLoader mImageLoader;
    private final ImageLoader.Callback mInternalCallback;

    private ImageViewerLoader mImageViewerLoader;

    private View mMainView;
    private View curShowingView;
    private ImageView mThumbnailView;
    private PhotoView mNormalView;
    private SubsamplingScaleImageView mSSIV;

    private View mProgressIndicatorView;
    private ImageView mFailureImageView;

    private ImageSaveCallback mImageSaveCallback;
    private ImageLoader.Callback mUserCallback;
    private File mCurrentImageFile;
    private Uri mUri;
    private Uri mThumbnail;
    private boolean inLarge;

    private OnClickListener mOnClickListener;
    private OnLongClickListener mOnLongClickListener;
    private OnClickListener mFailureImageClickListener = new OnClickListener() {
        @Override
        public void onClick(final View v) {
            // Retry loading when failure image is clicked
            if (mTapToRetry) {
                showImage(mThumbnail, mUri);
            }
            if (mOnClickListener != null) {
                mOnClickListener.onClick(v);
            }
        }
    };

    private ProgressIndicator mProgressIndicator;
    private DisplayOptimizeListener mDisplayOptimizeListener;
    private int mInitScaleType;
    private ImageView.ScaleType mThumbnailScaleType;
    private ImageView.ScaleType mFailureImageScaleType;
    private boolean mOptimizeDisplay;
    private boolean mTapToRetry;

    public ImageViewer(Context context) {
        this(context, null);
    }

    public ImageViewer(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ImageViewer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray array = context.getTheme()
                .obtainStyledAttributes(attrs, R.styleable.ImageViewer, defStyleAttr, 0);
        mInitScaleType = array.getInteger(R.styleable.ImageViewer_initScaleType,
                INIT_SCALE_TYPE_FIT_CENTER);

        if (array.hasValue(R.styleable.ImageViewer_failureImage)) {
            int scaleTypeIndex = array.getInteger(
                    R.styleable.ImageViewer_failureImageInitScaleType,
                    DEFAULT_IMAGE_SCALE_TYPE);
            mFailureImageScaleType = scaleType(scaleTypeIndex);
            Drawable mFailureImageDrawable = array.getDrawable(
                    R.styleable.ImageViewer_failureImage);
            setFailureImage(mFailureImageDrawable);
        }
        if (array.hasValue(R.styleable.ImageViewer_thumbnailScaleType)) {
            int scaleTypeIndex = array.getInteger(
                    R.styleable.ImageViewer_thumbnailScaleType,
                    DEFAULT_IMAGE_SCALE_TYPE);
            mThumbnailScaleType = scaleType(scaleTypeIndex);
        }

        mOptimizeDisplay = array.getBoolean(R.styleable.ImageViewer_optimizeDisplay, true);
        mTapToRetry = array.getBoolean(R.styleable.ImageViewer_tapToRetry, true);

        array.recycle();

        if (isInEditMode()) {
            mImageLoader = null;
        } else {
            mImageLoader = BigImageViewer.imageLoader();
        }
        mInternalCallback = ThreadedCallbacks.create(ImageLoader.Callback.class, this);

        mNormalView = new PhotoView(context);
        mNormalView.setScaleType(scaleType(mInitScaleType));
        mNormalView.setMaximumScale(4f);
        mNormalView.setZoomTransitionDuration(350);
        addView(mNormalView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        curShowingView = mMainView = mNormalView;
        inLarge = false;

    }

    private void checkImageViewLoader() {
        if (mImageViewerLoader == null) {
            mImageViewerLoader = new ImageViewerLoader();
        }
    }

    public static ImageView.ScaleType scaleType(int value) {
        if (0 <= value && value < IMAGE_SCALE_TYPES.length) {
            return IMAGE_SCALE_TYPES[value];
        }
        return IMAGE_SCALE_TYPES[DEFAULT_IMAGE_SCALE_TYPE];
    }

    @Override
    public void setOnClickListener(final OnClickListener listener) {
        mOnClickListener = listener;
        if (mMainView != null) {
            mMainView.setOnClickListener(listener);
        }
    }

    @Override
    public void setOnLongClickListener(OnLongClickListener listener) {
        mOnLongClickListener = listener;
        if (mMainView != null) {
            mMainView.setOnLongClickListener(listener);
        }
    }

    public void setImageViewLoader(ImageViewerLoader loader) {
        if (loader == null) {
            return;
        }
        mImageViewerLoader = loader;
    }

    public void setFailureImageInitScaleType(ImageView.ScaleType scaleType) {
        mFailureImageScaleType = scaleType;
    }

    public void setFailureImage(Drawable failureImage) {
        // Failure image is not set
        if (failureImage == null) {
            return;
        }

        if (mFailureImageView == null) {
            // Init failure image
            mFailureImageView = new ImageView(getContext());
            mFailureImageView.setVisibility(GONE);
            mFailureImageView.setOnClickListener(mFailureImageClickListener);

            if (mFailureImageScaleType != null) {
                mFailureImageView.setScaleType(mFailureImageScaleType);
            }

            addView(mFailureImageView);
        }

        mFailureImageView.setImageDrawable(failureImage);
    }

    public void setInitScaleType(int initScaleType) {
        if (mSSIV == null) {
            return;
        }

        mInitScaleType = initScaleType;
        switch (initScaleType) {
            case INIT_SCALE_TYPE_CENTER_CROP:
                mSSIV.setMinimumScaleType(SubsamplingScaleImageView.SCALE_TYPE_CENTER_CROP);
                break;
            case INIT_SCALE_TYPE_CUSTOM:
                mSSIV.setMinimumScaleType(SubsamplingScaleImageView.SCALE_TYPE_CUSTOM);
                break;
            case INIT_SCALE_TYPE_START:
                mSSIV.setMinimumScaleType(SubsamplingScaleImageView.SCALE_TYPE_START);
                break;
            case INIT_SCALE_TYPE_CENTER_INSIDE:
            default:
                mSSIV.setMinimumScaleType(SubsamplingScaleImageView.SCALE_TYPE_CENTER_INSIDE);
                break;
        }
        if (mDisplayOptimizeListener != null) {
            mDisplayOptimizeListener.setInitScaleType(initScaleType);
        }
    }

    public void setThumbnailScaleType(ImageView.ScaleType scaleType) {
        mThumbnailScaleType = scaleType;
    }

    public void setOptimizeDisplay(boolean optimizeDisplay) {
        if (mSSIV == null) {
            return;
        }

        mOptimizeDisplay = optimizeDisplay;
        if (mOptimizeDisplay) {
            mDisplayOptimizeListener = new DisplayOptimizeListener(mSSIV);
            mSSIV.setOnImageEventListener(mDisplayOptimizeListener);
        } else {
            mDisplayOptimizeListener = null;
            mSSIV.setOnImageEventListener(null);
        }
    }

    public void setTapToRetry(boolean tapToRetry) {
        mTapToRetry = tapToRetry;
    }

    public void setImageSaveCallback(ImageSaveCallback imageSaveCallback) {
        mImageSaveCallback = imageSaveCallback;
    }

    public void setProgressIndicator(ProgressIndicator progressIndicator) {
        mProgressIndicator = progressIndicator;
    }

    public void setImageLoaderCallback(ImageLoader.Callback imageLoaderCallback) {
        mUserCallback = imageLoaderCallback;
    }

    public File getCurrentImageFile() {
        return mCurrentImageFile;
    }

    @RequiresPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    public void saveImageIntoGallery() {
        if (mCurrentImageFile == null) {
            if (mImageSaveCallback != null) {
                mImageSaveCallback.onFail(new IllegalStateException("image not downloaded yet"));
            }

            return;
        }

        try {
            String result = MediaStore.Images.Media.insertImage(getContext().getContentResolver(),
                    mCurrentImageFile.getAbsolutePath(), mCurrentImageFile.getName(), "");
            if (mImageSaveCallback != null) {
                if (!TextUtils.isEmpty(result)) {
                    mImageSaveCallback.onSuccess(result);
                } else {
                    mImageSaveCallback.onFail(new RuntimeException("saveImageIntoGallery fail"));
                }
            }
        } catch (FileNotFoundException e) {
            if (mImageSaveCallback != null) {
                mImageSaveCallback.onFail(e);
            }
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();

        mImageLoader.cancel(hashCode());

    }

    public void showImage(Uri uri) {
        showImage(Uri.EMPTY, uri);
    }

    public void showImage(final Uri thumbnail, final Uri uri) {
        mThumbnail = thumbnail;
        mUri = uri;

        clearThumbnailAndProgressIndicator();
        mImageLoader.loadImage(hashCode(), uri, mInternalCallback);

        if (mFailureImageView != null) {
            mFailureImageView.setVisibility(GONE);
        }
    }

    public void cancel() {
        mImageLoader.cancel(hashCode());
    }

    public SubsamplingScaleImageView getSSIV() {
        return mSSIV;
    }

    public View getShowingImageView() {
        return curShowingView;
    }

    public boolean isLarge() {
        return inLarge;
    }

    @Override
    public void onCacheHit(final int imageType, File image) {
        mCurrentImageFile = image;
        doShowImage(imageType, image);

        if (mUserCallback != null) {
            mUserCallback.onCacheHit(imageType, image);
        }
    }

    @Override
    public void onCacheMiss(final int imageType, final File image) {
        mCurrentImageFile = image;
        doShowImage(imageType, image);

        if (mUserCallback != null) {
            mUserCallback.onCacheMiss(imageType, image);
        }
    }

    @Override
    public void onStart() {
        checkImageViewLoader();
        if (mThumbnail != Uri.EMPTY) {
            if (mThumbnailView == null) {
                mThumbnailView = new AppCompatImageView(getContext());
                mThumbnailView.setScaleX(0.5f);
                mThumbnailView.setScaleY(0.5f);
            }
            if (mThumbnailView.getParent() == null) {
                addView(mThumbnailView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            }
            mImageViewerLoader.onLoad(mThumbnailView, mThumbnail);
            curShowingView = mThumbnailView;
        }

        if (mProgressIndicator != null) {
            if (mProgressIndicatorView == null) {
                mProgressIndicatorView = mProgressIndicator.getView(ImageViewer.this);
            }
            if (mProgressIndicatorView != null && mProgressIndicatorView.getParent() == null) {
                LayoutParams p = new LayoutParams(UIHelper.dip2px(80), UIHelper.dip2px(80));
                p.gravity = Gravity.CENTER;
                addView(mProgressIndicatorView, p);
            }
            mProgressIndicator.onStart();
        }

        if (mUserCallback != null) {
            mUserCallback.onStart();
        }
    }

    @Override
    public void onProgress(final int progress) {
        if (mProgressIndicator != null) {
            mProgressIndicator.onProgress(progress);
        }
        if (mUserCallback != null) {
            mUserCallback.onProgress(progress);
        }
    }

    @Override
    public void onFinish() {
        doOnFinish();
        if (mUserCallback != null) {
            mUserCallback.onFinish();
        }
    }

    @Override
    public void onSuccess(final File image) {
        if (mUserCallback != null) {
            mUserCallback.onSuccess(image);
        }
    }

    @Override
    public void onFail(Exception error) {
        showFailImage();

        if (mUserCallback != null) {
            mUserCallback.onFail(error);
        }
    }

    @UiThread
    private void doOnFinish() {
        if (mOptimizeDisplay) {
            clearThumbnailAndProgressIndicator();
            if (mMainView != null) {
                mMainView.setScaleX(0.5f);
                mMainView.setScaleY(0.5f);
                mMainView.animate().scaleX(1f).scaleY(1f).setDuration(500).start();
            }
        } else {
            if (mProgressIndicator != null) {
                mProgressIndicator.onFinish();
            }
            clearThumbnailAndProgressIndicator();
        }
    }

    @UiThread
    private void doShowImage(final int imageType, final File image) {
        inLarge = detectInLargeMode(image, ImageUtil.getImageType(image));
        if (!inLarge) {
            curShowingView = mMainView = mNormalView;
        } else {
            if (mSSIV == null) {
                mSSIV = new SubsamplingScaleImageView(getContext());
            }
            curShowingView = mMainView = mSSIV;
        }

        if (mMainView == null) {
            onFail(new RuntimeException("Image type not supported: "
                    + ImageInfoExtractor.typeName(imageType)));
            return;
        }

        mMainView.setOnClickListener(mOnClickListener);
        mMainView.setOnLongClickListener(mOnLongClickListener);


        if (mMainView instanceof SubsamplingScaleImageView) {
            removeViewInLayout(mNormalView);
            mSSIV = (SubsamplingScaleImageView) mMainView;

            mSSIV.setMinimumTileDpi(160);

            setOptimizeDisplay(mOptimizeDisplay);
            setInitScaleType(mInitScaleType);

            mSSIV.setImage(ImageSource.uri(Uri.fromFile(image)));
        } else {
            if (mSSIV != null) {
                removeViewInLayout(mSSIV);
            }
            mImageViewerLoader.onLoad((ImageView) mMainView, image);
        }
        if (mMainView.getParent() == null) {
            addView(mMainView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        }
        if (mFailureImageView != null) {
            mFailureImageView.setVisibility(GONE);
        }
    }

    @UiThread
    private void showFailImage() {
        // Failure image is not set
        if (mFailureImageView == null) {
            return;
        }
        if (mMainView != null) {
            removeView(mMainView);
        }

        mFailureImageView.setVisibility(VISIBLE);
        clearThumbnailAndProgressIndicator();
    }

    private void clearThumbnailAndProgressIndicator() {
        if (mThumbnailView != null) {
            removeView(mThumbnailView);
        }
        if (mProgressIndicatorView != null) {
            removeView(mProgressIndicatorView);
        }
    }

    public boolean detectInLargeMode(File imageFile, ImageUtil.ImageType imageType) {
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
}
