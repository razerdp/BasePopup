package razerdp.basepopup;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import java.lang.ref.WeakReference;

import razerdp.widget.QuickPopup;

/**
 * Created by 大灯泡 on 2018/8/23.
 * <p>
 * 快速建立Popup的builder
 */
public class QuickPopupBuilder {

    private WeakReference<Context> mContextWeakReference;
    private QuickPopupConfig mConfig;
    private OnConfigApplyListener mOnConfigApplyListener;

    int width = ViewGroup.LayoutParams.WRAP_CONTENT;
    int height = ViewGroup.LayoutParams.WRAP_CONTENT;


    private QuickPopupBuilder(Context context) {
        mContextWeakReference = new WeakReference<>(context);
        mConfig = QuickPopupConfig.generateDefault();
    }

    public static QuickPopupBuilder with(Context context) {
        return new QuickPopupBuilder(context);
    }

    public QuickPopupBuilder contentView(int resId) {
        mConfig.contentViewLayoutid(resId);
        return this;
    }

    public QuickPopupBuilder width(int width) {
        this.width = width;
        return this;
    }

    public QuickPopupBuilder height(int height) {
        this.height = height;
        return this;
    }

    @Deprecated
    public QuickPopupBuilder wrapContentMode() {
        return width(ViewGroup.LayoutParams.WRAP_CONTENT)
                .height(ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    public QuickPopupBuilder setOnConfigApplyListener(OnConfigApplyListener onConfigApplyListener) {
        mOnConfigApplyListener = onConfigApplyListener;
        return this;
    }

    public final <C extends QuickPopupConfig> C getConfig() {
        return (C) mConfig;
    }

    public OnConfigApplyListener getOnConfigApplyListener() {
        return mOnConfigApplyListener;
    }

    public <C extends QuickPopupConfig> QuickPopupBuilder config(C quickPopupConfig) {
        if (quickPopupConfig == null) return this;
        if (quickPopupConfig != mConfig) {
            quickPopupConfig.contentViewLayoutid(mConfig.contentViewLayoutid);
        }
        this.mConfig = quickPopupConfig;
        return this;
    }

    public QuickPopup build() {
        return new QuickPopup(getContext(), mConfig, mOnConfigApplyListener, width, height);
    }

    public QuickPopup show() {
        return show(null);
    }

    public QuickPopup show(int anchorViewResid) {
        QuickPopup quickPopup = build();
        quickPopup.showPopupWindow(anchorViewResid);
        return quickPopup;
    }

    public QuickPopup show(View anchorView) {
        QuickPopup quickPopup = build();
        quickPopup.showPopupWindow(anchorView);
        return quickPopup;
    }

    public QuickPopup show(int x, int y) {
        QuickPopup quickPopup = build();
        quickPopup.showPopupWindow(x, y);
        return quickPopup;
    }


    public interface OnConfigApplyListener {
        void onConfigApply(QuickPopup basePopup, QuickPopupConfig config);
    }

    //-----------------------------------------tools-----------------------------------------
    private Context getContext() {
        return mContextWeakReference == null ? null : mContextWeakReference.get();
    }
}
