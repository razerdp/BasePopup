package razerdp.demo.base.imageloader;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.MediaMetadataRetriever;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.Px;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;

import java.security.MessageDigest;

import razerdp.util.log.PopupLog;

import static com.bumptech.glide.load.resource.bitmap.VideoDecoder.FRAME_OPTION;

/**
 * Created by 大灯泡 on 2019/4/10.
 * <p>
 * 图片加载单例 啊啊啊啊。。。好多重载，这时候有个kotlin多好啊噗
 */
@SuppressLint("All")
public enum ImageLoaderManager {
    INSTANCE;
    private static final String TAG = "ImageLoaderManager";

    public void clearMemory(Context context) {
        Glide.get(context).clearMemory();
    }

    public static Glide getGlide(Context context) {
        return Glide.get(context);
    }

    public Options option() {
        return Options.get();
    }

    public void loadImage(View target, Object from) {
        loadImageInternal(target, from, Options.get());
    }

    public void loadRoundImage(View target, Object from, @Px int radius) {
        loadImageInternal(target, from, Options.get().setRadius(radius));
    }

    void loadImageInternal(View target, Object from, Options options) {
        //不用CircleImageView的原因在于：对loading支持不友好
        //1 - loading或者error图会被生成新的圆形bitmap，造成大量内存占用（滑动部分）
        //2 - 有时候出现加载了loading后无法加载原来的图片的问题
        //3 - 必须glide针对支持
        //因此采取Glide原生的方式

        options.applyRequestOption(target, from);

        if (target instanceof Target) {
            Glide.with(target).load(from).apply(options.requestOptions).into((Target) target);
        } else {
            if (target instanceof ImageView) {
                Glide.with(target).load(from).apply(options.requestOptions).into((ImageView) target);
            }
        }
    }

    public void loadBitmap(Context context, String imageUrl, final OnLoadBitmapListener listener) {
        PopupLog.i("下载图片", "url=" + imageUrl);
        RequestOptions options = Options.DEFAULT_REQUEST_OPTIONS.diskCacheStrategy(DiskCacheStrategy.ALL).skipMemoryCache(false);
        Glide.with(context)
                .asBitmap()
                .load(imageUrl)
                .apply(options)
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onLoadFailed(@Nullable Drawable errorDrawable) {
                        super.onLoadFailed(errorDrawable);
                        if (listener != null) {
                            listener.onFailed(new IllegalArgumentException("download failed"));
                        }
                    }

                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        if (listener != null) {
                            listener.onSuccess(resource);
                        }
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }
                });
    }

    public void loadVideoScreenshot(final Context context, String uri, ImageView imageView, long frameTimeMicros) {
        RequestOptions requestOptions = RequestOptions.frameOf(frameTimeMicros)
                .set(FRAME_OPTION, MediaMetadataRetriever.OPTION_CLOSEST)
                .transform(new BitmapTransformation() {
                    @Override
                    protected Bitmap transform(@NonNull BitmapPool pool, @NonNull Bitmap toTransform, int outWidth, int outHeight) {
                        return toTransform;
                    }

                    @Override
                    public void updateDiskCacheKey(MessageDigest messageDigest) {
                        try {
                            messageDigest.update((context.getPackageName() + "RotateTransform").getBytes("utf-8"));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
        Glide.with(context).load(uri).apply(requestOptions).into(imageView);
    }

    public interface OnLoadBitmapListener {
        void onSuccess(Bitmap bitmap);

        void onFailed(Exception e);
    }

    public static abstract class OnLoadBitmapListenerAdapter implements OnLoadBitmapListener {

        @Override
        public void onSuccess(Bitmap bitmap) {

        }

        @Override
        public void onFailed(Exception e) {

        }
    }

}
