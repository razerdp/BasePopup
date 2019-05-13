package razerdp.widget;

import android.animation.Animator;
import android.content.Context;
import android.util.Pair;
import android.view.View;
import android.view.animation.Animation;

import java.util.HashMap;
import java.util.Map;

import razerdp.basepopup.BasePopupFlag;
import razerdp.basepopup.BasePopupWindow;
import razerdp.basepopup.QuickPopupBuilder.OnConfigApplyListener;
import razerdp.basepopup.QuickPopupConfig;

/**
 * Created by 大灯泡 on 2018/8/23.
 * <p>
 * 快速popup
 */
public class QuickPopup extends BasePopupWindow {

    private QuickPopupConfig mConfig;
    private OnConfigApplyListener mOnConfigApplyListener;

    private QuickPopup(Context context) {
        super(context);
    }

    private QuickPopup(Context context, boolean delayInit) {
        super(context, delayInit);
    }

    private QuickPopup(Context context, int width, int height) {
        super(context, width, height);
    }

    private QuickPopup(Context context, int width, int height, boolean delayInit) {
        super(context, width, height, delayInit);
    }

    public QuickPopup(Context context, QuickPopupConfig config, OnConfigApplyListener onConfigApplyListener, int w, int h) {
        super(context, w, h, true);
        mConfig = config;
        mOnConfigApplyListener = onConfigApplyListener;
        if (mConfig != null) {
            delayInit();
        } else {
            throw new NullPointerException("QuickPopupConfig must be not null!");
        }
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
        setClipToScreen((config.flag & BasePopupFlag.CLIP_TO_SCREEN) != 0);

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
        setKeepSize((config.flag & BasePopupFlag.KEEP_SIZE) != 0);

        if (mOnConfigApplyListener != null) {
            mOnConfigApplyListener.onConfigApply(this, config);
        }
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
