package razerdp.basepopup;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import java.lang.ref.WeakReference;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.OnLifecycleEvent;
import razerdp.widget.QuickPopup;

/**
 * Created by 大灯泡 on 2018/8/23.
 * <p>
 * 快速建立Popup的builder
 */
public class QuickPopupBuilder implements LifecycleObserver {

    private QuickPopupConfig mConfig;
    private WeakReference<Object> ownerAnchorParent;

    private int width = 0;
    private int height = 0;

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    void onDestroy() {
        ownerAnchorParent = null;
    }

    private QuickPopupBuilder(Object obj) {
        ownerAnchorParent = new WeakReference<>(obj);
        mConfig = QuickPopupConfig.generateDefault();
        Activity activity = BasePopupHelper.findActivity(obj, false);
        if (activity instanceof LifecycleOwner) {
            ((LifecycleOwner) activity).getLifecycle().addObserver(this);
        } else {
            if (activity != null) {
                activity.getWindow().getDecorView().addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
                    @Override
                    public void onViewAttachedToWindow(View v) {

                    }

                    @Override
                    public void onViewDetachedFromWindow(View v) {
                        v.removeOnAttachStateChangeListener(this);
                        onDestroy();
                    }
                });
            }
        }
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
        Object anchor = ownerAnchorParent == null ? null : ownerAnchorParent.get();
        if (anchor instanceof Context) {
            return new QuickPopup((Context) anchor, width, height, mConfig);
        }
        if (anchor instanceof Fragment) {
            return new QuickPopup((Fragment) anchor, width, height, mConfig);
        }
        if (anchor instanceof Dialog) {
            return new QuickPopup((Dialog) anchor, width, height, mConfig);
        }
        throw new NullPointerException("宿主已经被销毁");
    }

    public QuickPopup show() {
        return show(null);
    }

    @Deprecated
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
}
