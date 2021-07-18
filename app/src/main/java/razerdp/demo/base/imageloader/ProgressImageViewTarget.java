package razerdp.demo.base.imageloader;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.request.target.ImageViewTarget;
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.bumptech.glide.request.transition.Transition;

/**
 * Created by 大灯泡 on 2019/5/6
 * <p>
 * Description：
 */
public final class ProgressImageViewTarget extends ImageViewTarget<Drawable> {

    private final String mUrl;
    private GlideProgressManager.ProgressListener mProgressListener;
    private boolean callFinish;

    public ProgressImageViewTarget(ImageView view, String url, GlideProgressManager.ProgressListener mProgressListener) {
        super(view);
        this.mUrl = url;
        this.mProgressListener = mProgressListener;
    }


    @Override
    public void onLoadStarted(@Nullable Drawable placeholder) {
        super.onLoadStarted(placeholder);
        callFinish = false;
        GlideProgressManager.expect(mUrl, mProgressListener);
    }

    @Override
    public void onLoadCleared(@Nullable Drawable placeholder) {
        super.onLoadCleared(placeholder);
        callFinish();
        GlideProgressManager.forget(mUrl);
        mProgressListener = null;
    }

    @Override
    public void onLoadFailed(@Nullable Drawable errorDrawable) {
        super.onLoadFailed(errorDrawable);
        callFinish();
        GlideProgressManager.forget(mUrl);
        mProgressListener = null;
    }

    @Override
    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
        super.onResourceReady(resource, transition);
        callFinish();
        GlideProgressManager.forget(mUrl);
        mProgressListener = null;
    }

    @Override
    public void getSize(@NonNull SizeReadyCallback cb) {
        super.getSize(cb);
    }


    @Override
    protected void setResource(@Nullable Drawable resource) {
        view.setImageDrawable(resource);
    }

    void callFinish() {
        if (callFinish) return;
        if (mProgressListener != null) {
            mProgressListener.onDownloadFinish();
        }
        callFinish = true;
    }

}
