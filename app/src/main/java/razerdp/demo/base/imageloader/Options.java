package razerdp.demo.base.imageloader;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.DrawableRes;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import razerdp.basepopup.R;
import razerdp.demo.base.interfaces.SimpleCallback;
import razerdp.demo.utils.UIHelper;
import razerdp.demo.widget.DPImageView;

/**
 * Created by 大灯泡 on 2019/8/7
 * <p>
 * Description：
 */
@SuppressLint("All")
public class Options implements Cloneable {
    private static final Options DEFAULT_OPTIONS = new Options();
    private static final ColorDrawable DEFAULT_LOADING = new ColorDrawable(UIHelper.getColor(R.color.color_loading));
    private static final Drawable DEFAULT_ERROR = UIHelper.getDrawable(R.drawable.ic_error_gray);
    public static Drawable TRANSPARENT = new ColorDrawable(Color.TRANSPARENT);

    static RequestOptions DEFAULT_REQUEST_OPTIONS = new RequestOptions()
            .placeholder(DEFAULT_LOADING)
            .error(DEFAULT_ERROR);

    private Options() {
    }

    public static Options get() {
        return DEFAULT_OPTIONS.clone();
    }

    Drawable error;
    Drawable loading;
    int topLeftRadius;
    int topRightRadius;
    int bottomLeftRadius;
    int bottomRightRadius;
    int radius;
    boolean cache = true;
    boolean circle = false;
    RequestOptions requestOptions = getRequestOption();

    public Options setError(@DrawableRes int error) {
        return setError(UIHelper.getDrawable(error));
    }

    public Options setLoading(@DrawableRes int loading) {
        return setLoading(UIHelper.getDrawable(loading));
    }

    public Options setError(Drawable error) {
        this.error = error;
        requestOptions.error(error);
        return this;
    }

    public Options setLoading(Drawable loading) {
        this.loading = loading;
        requestOptions.placeholder(loading);
        return this;
    }

    public Options setTopLeftRadius(int topLeftRadius) {
        this.topLeftRadius = topLeftRadius;
        return this;
    }

    public Options setTopRightRadius(int topRightRadius) {
        this.topRightRadius = topRightRadius;
        return this;
    }

    public Options setBottomLeftRadius(int bottomLeftRadius) {
        this.bottomLeftRadius = bottomLeftRadius;
        return this;
    }

    public Options setBottomRightRadius(int bottomRightRadius) {
        this.bottomRightRadius = bottomRightRadius;
        return this;
    }

    public Options setRadius(int radius) {
        this.radius = radius;
        return this;
    }

    public Options setCache(boolean cache) {
        this.cache = cache;
        return this;
    }

    public Options configRequestOption(SimpleCallback<RequestOptions> callback) {
        if (callback != null) {
            callback.onCall(requestOptions);
        }
        return this;
    }

    public Options setCircle(boolean circle) {
        this.circle = circle;
        return this;
    }

    public Options transparent(boolean transparentLoading, boolean transparentError) {
        if (transparentLoading) {
            setLoading(TRANSPARENT);
        }
        if (transparentError) {
            setError(TRANSPARENT);
        }
        return this;
    }

    boolean isRound() {
        return radius != 0 || topLeftRadius != 0 || topRightRadius != 0 || bottomLeftRadius != 0 || bottomRightRadius != 0;
    }

    public void loadImage(View target, Object from) {
        ImageLoaderManager.INSTANCE.loadImageInternal(target, from, this);
    }

    void applyRequestOption(View target, Object from) {

        if (target instanceof DPImageView) {
            if (((DPImageView) target).getShape() == DPImageView.SHAPE_OVAL) {
                requestOptions.circleCrop();
            } else {
                topLeftRadius = topLeftRadius == 0 ? ((DPImageView) target).getTopLeftRadius() : topLeftRadius;
                topRightRadius = topRightRadius == 0 ? ((DPImageView) target).getTopRightRadius() : topRightRadius;
                bottomLeftRadius = bottomLeftRadius == 0 ? ((DPImageView) target).getBottomLeftRadius() : bottomLeftRadius;
                bottomRightRadius = bottomRightRadius == 0 ? ((DPImageView) target).getBottomRightRadius() : bottomRightRadius;
                radius = radius == 0 ? ((DPImageView) target).getRadius() : radius;
            }
        }

        if (isRound()) {
            RoundedCornersTransformation.CornerType roundType = null;
            if (radius != 0) {
                roundType = RoundedCornersTransformation.CornerType.ALL;
            } else {
                roundType = RoundedCornersTransformation.getType(topLeftRadius, topRightRadius, bottomLeftRadius, bottomRightRadius);
            }
            if (roundType != null) {
                int r;
                if (radius != 0) {
                    r = radius;
                } else {
                    r = Math.max(topLeftRadius, topRightRadius);
                    r = Math.max(r, bottomLeftRadius);
                    r = Math.max(r, bottomRightRadius);
                }

                if (target instanceof ImageView) {
                    requestOptions.transform(new RoundedCornersTransformation(r, 0, roundType, ((ImageView) target).getScaleType()));
                } else {
                    requestOptions.transform(new RoundedCornersTransformation(r, 0, roundType, ImageView.ScaleType.FIT_CENTER));
                }

            }
        }

        requestOptions.placeholder(loading)
                .error(error)
                .skipMemoryCache(!cache)
                .diskCacheStrategy(cache ? DiskCacheStrategy.AUTOMATIC : DiskCacheStrategy.NONE);
    }

    @Override
    protected Options clone() {
        try {
            Options result = (Options) super.clone();
            result.error = DEFAULT_ERROR;
            result.loading = DEFAULT_LOADING;
            result.topLeftRadius = 0;
            result.topRightRadius = 0;
            result.bottomLeftRadius = 0;
            result.bottomRightRadius = 0;
            result.radius = 0;
            result.cache = true;
            result.requestOptions = getRequestOption();
            return result;
        } catch (Exception e) {
            return new Options();
        }
    }

    public static RequestOptions getRequestOption() {
        try {
            return DEFAULT_REQUEST_OPTIONS.clone();
        } catch (Exception e) {
            return new RequestOptions()
                    .placeholder(DEFAULT_LOADING)
                    .error(DEFAULT_ERROR);
        }
    }
}
