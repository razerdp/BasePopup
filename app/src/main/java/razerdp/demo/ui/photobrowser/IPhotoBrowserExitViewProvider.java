package razerdp.demo.ui.photobrowser;

import android.widget.ImageView;

/**
 * Created by 大灯泡 on 2019/8/22
 * <p>
 * Description：
 */
public interface IPhotoBrowserExitViewProvider {
    ImageView onGetTransitionExitView(ImageView from, int exitPosition);
}
