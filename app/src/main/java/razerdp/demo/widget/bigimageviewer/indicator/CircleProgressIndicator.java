package razerdp.demo.widget.bigimageviewer.indicator;

import android.view.View;


import razerdp.basepopup.R;
import razerdp.demo.utils.ViewUtil;
import razerdp.demo.widget.CircleProgressView;
import razerdp.demo.widget.bigimageviewer.view.ImageViewer;

/**
 * Created by 大灯泡 on 2019/8/26
 * <p>
 * Description：
 */
public class CircleProgressIndicator implements ProgressIndicator {
    private CircleProgressView progressView;

    @Override
    public View getView(ImageViewer parent) {
        if (progressView == null) {
            progressView = (CircleProgressView) ViewUtil.inflate(parent.getContext(), R.layout.item_bigimageview_progress, parent, false);
        }
        return progressView;
    }

    @Override
    public void onStart() {
        progressView.start();
    }

    @Override
    public void onProgress(int progress) {
        progressView.setProgress(progress);
    }

    @Override
    public void onFinish() {
        progressView.finish(true);
    }
}
