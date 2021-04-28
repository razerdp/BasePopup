package razerdp.demo.ui.photobrowser;



import java.io.File;

import razerdp.demo.widget.bigimageviewer.loader.ImageLoader;

/**
 * Created by 大灯泡 on 2019/8/26
 * <p>
 * Description：
 */
public abstract class ImageLoaderCallbackAdapter implements ImageLoader.Callback {
    @Override
    public void onCacheHit(int imageType, File image) {

    }

    @Override
    public void onCacheMiss(int imageType, File image) {

    }

    @Override
    public void onStart() {

    }

    @Override
    public void onProgress(int progress) {

    }

    @Override
    public void onFinish() {

    }

    @Override
    public void onSuccess(File image) {

    }

    @Override
    public void onFail(Exception error) {

    }
}
