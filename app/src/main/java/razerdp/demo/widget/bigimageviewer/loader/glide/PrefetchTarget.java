package razerdp.demo.widget.bigimageviewer.loader.glide;

import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.request.Request;
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.bumptech.glide.util.Util;

import java.io.File;

public class PrefetchTarget implements Target<File> {

    private final int width;
    private final int height;

    private Request request;

    @SuppressWarnings("WeakerAccess")
    public PrefetchTarget() {
        this(SIZE_ORIGINAL, SIZE_ORIGINAL);
    }

    @SuppressWarnings("WeakerAccess")
    private PrefetchTarget(int width, int height) {
        this.width = width;
        this.height = height;
    }

    @Override
    public void onResourceReady(@NonNull File resource, @Nullable Transition<? super File> transition) {
        // not interested in result
    }

    /**
     * Immediately calls the given callback with the sizes given in the constructor.
     *
     * @param cb {@inheritDoc}
     */
    @Override
    public final void getSize(@NonNull SizeReadyCallback cb) {
        if (!Util.isValidDimensions(width, height)) {
            throw new IllegalArgumentException(
                    "Width and height must both be > 0 or Target#SIZE_ORIGINAL, but given" + " width: "
                            + width + " and height: " + height + ", either provide dimensions in the constructor"
                            + " or call override()");
        }
        cb.onSizeReady(width, height);
    }

    @Override
    public void removeCallback(@NonNull SizeReadyCallback cb) {
        // Do nothing, we never retain a reference to the callback.
    }

    @Override
    public void setRequest(@Nullable Request request) {
        this.request = request;
    }

    @Override
    @Nullable
    public Request getRequest() {
        return request;
    }

    @Override
    public void onLoadCleared(@Nullable Drawable placeholder) {
        // Do nothing.
    }

    @Override
    public void onLoadStarted(@Nullable Drawable placeholder) {
        // Do nothing.
    }

    @Override
    public void onLoadFailed(@Nullable Drawable errorDrawable) {
        // Do nothing.
    }

    @Override
    public void onStart() {
        // Do nothing.
    }

    @Override
    public void onStop() {
        // Do nothing.
    }

    @Override
    public void onDestroy() {
        // Do nothing.
    }
}
