package razerdp.widget;

import android.app.Dialog;
import android.content.Context;
import android.util.Pair;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import razerdp.basepopup.BasePopupFlag;
import razerdp.basepopup.BasePopupWindow;
import razerdp.basepopup.QuickPopupBuilder;
import razerdp.basepopup.QuickPopupConfig;

/**
 * Created by 大灯泡 on 2018/8/23.
 * <p>
 * 快速popup
 */
public class QuickPopup extends BasePopupWindow {

    private QuickPopupConfig mConfig;
    private QuickPopupBuilder mBuilder;


    public QuickPopup(Fragment fragment, QuickPopupBuilder builder) {
        super(fragment, builder.getWidth(), builder.getHeight());
        mBuilder = builder;
        mConfig = builder.getConfig();
        if (mConfig == null) {
            throw new NullPointerException("QuickPopupConfig must be not null!");
        }
        setContentView(mConfig.getContentViewLayoutid());
    }

    public QuickPopup(Dialog dialog, QuickPopupBuilder builder) {
        super(dialog, builder.getWidth(), builder.getHeight());
        mBuilder = builder;
        mConfig = builder.getConfig();
        if (mConfig == null) {
            throw new NullPointerException("QuickPopupConfig must be not null!");
        }
        setContentView(mConfig.getContentViewLayoutid());
    }

    public QuickPopup(Context context, QuickPopupBuilder builder) {
        super(context, builder.getWidth(), builder.getHeight());
        mBuilder = builder;
        mConfig = builder.getConfig();
        if (mConfig == null) {
            throw new NullPointerException("QuickPopupConfig must be not null!");
        }
        setContentView(mConfig.getContentViewLayoutid());
    }


    @Override
    public void onViewCreated(View contentView) {
        super.onViewCreated(contentView);
        applyConfigSetting(mConfig);
    }

    protected <C extends QuickPopupConfig> void applyConfigSetting(C config) {
        if (config.getPopupBlurOption() != null) {
            setBlurOption(config.getPopupBlurOption());
        } else {
            setBlurBackgroundEnable((config.flag & BasePopupFlag.BLUR_BACKGROUND) != 0, config.getOnBlurOptionInitListener());
        }
        setPopupFadeEnable((config.flag & BasePopupFlag.FADE_ENABLE) != 0);
        for (Map.Entry<String, Object> entry : config.getInvokeParams().entrySet()) {
            String methodName = entry.getKey();
            Method method = config.getMethod(methodName);
            if (method != null) {
                try {
                    method.invoke(this, entry.getValue());
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
        applyClick();

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

    @Nullable
    public QuickPopupConfig getConfig() {
        return mConfig;
    }

    @Override
    public void onDestroy() {
        if (mBuilder != null) {
            mBuilder.clear(true);
        }
        mBuilder = null;
        mConfig = null;
        super.onDestroy();
    }
}
