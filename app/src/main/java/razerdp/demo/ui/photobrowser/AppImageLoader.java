package razerdp.demo.ui.photobrowser;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.widget.ImageView;


import java.io.File;

import razerdp.demo.base.imageloader.ImageLoaderManager;
import razerdp.demo.widget.bigimageviewer.view.ImageViewerLoader;

/**
 * Created by 大灯泡 on 2019/8/26
 * <p>
 * Description：
 */
public class AppImageLoader extends ImageViewerLoader {
    static final Drawable loadingDrawable = new ColorDrawable(Color.TRANSPARENT);

    @Override
    public void onLoad(ImageView iv, Uri uri) {
        ImageLoaderManager
                .INSTANCE
                .option()
                .setLoading(loadingDrawable)
                .loadImage(iv, uri);
    }

    public void onLoad(ImageView iv, File imageFile) {
        ImageLoaderManager
                .INSTANCE
                .option()
                .setLoading(loadingDrawable)
                .loadImage(iv, imageFile);
    }

}
