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

    private int width = 0;
    private int height = 0;


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


    public final QuickPopupConfig getConfig() {
        return mConfig;
    }

    public QuickPopupBuilder config(QuickPopupConfig quickPopupConfig) {
        if (quickPopupConfig == null) return this;
        if (quickPopupConfig != mConfig) {
            quickPopupConfig.contentViewLayoutid(mConfig.contentViewLayoutid);
        }
        this.mConfig = quickPopupConfig;
        return this;
    }

    public QuickPopup build() {
        return new QuickPopup(getContext(), mConfig, width, height);
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

    //-----------------------------------------tools-----------------------------------------
    private Context getContext() {
        return mContextWeakReference == null ? null : mContextWeakReference.get();
    }
}
