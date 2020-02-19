package razerdp.widget;

import android.animation.Animator;
import android.content.Context;
import android.util.Pair;
import android.view.View;
import android.view.animation.Animation;

import java.util.HashMap;
import java.util.Map;

import razerdp.basepopup.BaseLazyPopupWindow;
import razerdp.basepopup.BasePopupFlag;
import razerdp.basepopup.QuickPopupConfig;

/**
 * Created by 大灯泡 on 2018/8/23.
 * <p>
 * 快速popup
 */
public class QuickPopup extends BaseLazyPopupWindow {

    private QuickPopupConfig mConfig;

    public QuickPopup(Context context, QuickPopupConfig config, int w, int h) {
        super(context, w, h);
        mConfig = config;
        if (mConfig == null) {
            throw new NullPointerException("QuickPopupConfig must be not null!");
        }
    }

    @Override
    public void onInit(View contentView) {
        super.onInit(contentView);
        applyConfigSetting(mConfig);
    }

    protected <C extends QuickPopupConfig> void applyConfigSetting(C config) {
        if (config.getPopupBlurOption() != null) {
            setBlurOption(config.getPopupBlurOption());
        } else {
            setBlurBackgroundEnable((config.flag & BasePopupFlag.BLUR_BACKGROUND) != 0, config.getOnBlurOptionInitListener());
        }

        setPopupFadeEnable((config.flag & BasePopupFlag.FADE_ENABLE) != 0);

        applyClick();

        setOffsetX(config.getOffsetX());
        setOffsetY(config.getOffsetY());

        setClipChildren((config.flag & BasePopupFlag.CLIP_CHILDREN) != 0);

        setOutSideDismiss((config.flag & BasePopupFlag.OUT_SIDE_DISMISS) != 0);
        setOutSideTouchable((config.flag & BasePopupFlag.OUT_SIDE_TOUCHABLE) != 0);
        setPopupGravity(config.getGravity());
        setAlignBackground((config.flag & BasePopupFlag.ALIGN_BACKGROUND) != 0);
        setAlignBackgroundGravity(config.getAlignBackgroundGravity());
        setAutoLocatePopup((config.flag & BasePopupFlag.AUTO_LOCATED) != 0);
        setPopupWindowFullScreen((config.flag & BasePopupFlag.FULL_SCREEN) != 0);
        setOnDismissListener(config.getDismissListener());
        setBackground(config.getBackground());
        linkTo(config.getLinkedView());
        setMinWidth(config.getMinWidth());
        setMaxWidth(config.getMaxWidth());
        setMinHeight(config.getMinHeight());
        setMaxHeight(config.getMaxHeight());
    }

    private void applyClick() {
        HashMap<Integer, Pair<View.OnClickListener, Boolean>> eventsMap = mConfig.getListenersHolderMap();
        if (eventsMap == null || eventsMap.isEmpty()) return;
        for (Map.Entry<Integer, Pair<View.OnClickListener, Boolean>> entry : eventsMap.entrySet()) {
            int viewId = entry.getKey();
            final Pair<View.OnClickListener, Boolean> event = entry.getValue();
            View v = findViewById(viewId);
            if (v != null) {
                if (event.second) {
                    v.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (event.first != null) {
                                if (event.first instanceof OnQuickPopupClickListenerWrapper) {
                                    ((OnQuickPopupClickListenerWrapper) event.first).mQuickPopup = QuickPopup.this;
                                }
                                event.first.onClick(v);
                            }
                            dismiss();
                        }
                    });
                } else {
                    v.setOnClickListener(event.first);
                }
            }
        }
    }

    public QuickPopupConfig getConfig() {
        return mConfig;
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
        return createPopupById(mConfig.getContentViewLayoutid());
    }
}
