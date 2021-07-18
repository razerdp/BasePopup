package razerdp.demo.widget.bigimageviewer.view;

/**
 * Created by 大灯泡 on 2021/4/28
 * <p>
 * Description：
 */
public interface ImageLoadCallback {
    void onShowThumbnail();

    void onStart();

    void onSuccess();

    void onError();

    void onFinish();
}
