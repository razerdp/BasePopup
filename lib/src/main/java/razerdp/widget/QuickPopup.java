package razerdp.widget;

import android.animation.Animator;
import android.content.Context;
import android.view.View;
import android.view.animation.Animation;

import razerdp.basepopup.BasePopupWindow;
import razerdp.basepopup.QuickPopupConfig;

/**
 * Created by 大灯泡 on 2018/8/23.
 * <p>
 * 快速popup
 */
public class QuickPopup extends BasePopupWindow {

    private QuickPopupConfig mConfig;
    private View content;

    public QuickPopup(Context context, QuickPopupConfig config, View contentView, int w, int h) {
        super(context, w, h, false);
        mConfig = config;
        content = contentView;
        if (mConfig != null) {
            callInit(context, w, h);
        } else {
            throw new NullPointerException("QuickPopupConfig must be not null!");
        }
        applyConfigSetting();
    }

    private void applyConfigSetting() {

    }


    @Override
    protected Animation onCreateShowAnimation() {
        return mConfig.getShowAnimation();
    }

    @Override
    protected Animation onCreateDismissAnimation() {
        return mConfig.getDismissAnimation();
    }

    @Override
    protected Animator onCreateDismissAnimator() {
        return mConfig.getDismissAnimator();
    }

    @Override
    protected Animator onCreateShowAnimator() {
        return mConfig.getShowAnimator();
    }

    @Override
    public View onCreateContentView() {
        return content;
    }
}
