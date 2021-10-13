package razerdp.basepopup;

import android.app.Dialog;
import android.content.Context;
import android.view.View;

import androidx.fragment.app.Fragment;

import razerdp.library.R;
import razerdp.util.PopupUtils;
import razerdp.widget.QuickPopup;

/**
 * Created by 大灯泡 on 2018/8/23.
 * <p>
 * 快速建立Popup的builder
 */
public class QuickPopupBuilder implements ClearMemoryObject {
    private static final String TAG = "QuickPopupBuilder";

    private QuickPopupConfig mConfig;
    private Object popupHost;

    private int width = 0;
    private int height = 0;

    private QuickPopupBuilder(Object obj) {
        popupHost = obj;
        mConfig = QuickPopupConfig.generateDefault();
    }

    public static QuickPopupBuilder with(Context context) {
        return new QuickPopupBuilder(context);
    }

    public static QuickPopupBuilder with(Fragment fragment) {
        return new QuickPopupBuilder(fragment);
    }

    public static QuickPopupBuilder with(Dialog dialog) {
        return new QuickPopupBuilder(dialog);
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


    public final QuickPopupConfig getConfig() {
        return mConfig;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
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
        if (popupHost instanceof Context) {
            return new QuickPopup((Context) popupHost, this);
        }
        if (popupHost instanceof Fragment) {
            return new QuickPopup((Fragment) popupHost, this);
        }
        if (popupHost instanceof Dialog) {
            return new QuickPopup((Dialog) popupHost, this);
        }
        throw new NullPointerException(PopupUtils.getString(R.string.basepopup_host_destroyed));
    }

    public QuickPopup show() {
        return show(null);
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

    @Override
    public void clear(boolean destroy) {
        popupHost = null;
        if (mConfig != null) {
            mConfig.clear(destroy);
        }
        mConfig = null;
    }
}
