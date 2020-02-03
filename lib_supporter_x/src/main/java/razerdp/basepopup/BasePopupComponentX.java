package razerdp.basepopup;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;

import java.lang.ref.WeakReference;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleOwner;

/**
 * Created by 大灯泡 on 2019/5/13
 * <p>
 * Description：
 */
public class BasePopupComponentX implements BasePopupComponent {
    @Override
    public View findDecorView(BasePopupWindow basePopupWindow, Activity activity) {
        if (basePopupWindow.mAttached == null) return null;
        Object object = basePopupWindow.mAttached.get();
        if (object instanceof DialogFragment) {
            DialogFragment d = ((DialogFragment) object);
            if (d.getDialog() != null && d.getDialog().isShowing() && !d.isRemoving()) {
                return d.getView();
            }
        }
        if (object instanceof Dialog) {
            Dialog d = (Dialog) object;
            if (d.isShowing() && d.getWindow() != null) {
                return d.getWindow().getDecorView();
            }
        }
        return null;
    }

    @Override
    public BasePopupWindow attachLifeCycle(BasePopupWindow basePopupWindow, Object owner) {
        if (!(owner instanceof LifecycleOwner)) {
            return basePopupWindow;
        }
        ((LifecycleOwner) owner).getLifecycle().addObserver(new BasePopupLifeCycleHolder(basePopupWindow));
        return basePopupWindow;
    }

    private class BasePopupLifeCycleHolder implements LifecycleEventObserver {
        WeakReference<BasePopupWindow> mBasePopupWindow;

        BasePopupLifeCycleHolder(BasePopupWindow target) {
            this.mBasePopupWindow = new WeakReference<>(target);
        }

        BasePopupWindow getPopup() {
            if (mBasePopupWindow == null) return null;
            return mBasePopupWindow.get();
        }

        @Override
        public void onStateChanged(@NonNull LifecycleOwner source, @NonNull Lifecycle.Event event) {
            BasePopupWindow popupWindow = getPopup();
            if (event == Lifecycle.Event.ON_DESTROY) {
                if (popupWindow == null) {
                    source.getLifecycle().removeObserver(this);
                    mBasePopupWindow = null;
                    return;
                }
                if (popupWindow.isShowing()) {
                    popupWindow.forceDismiss();
                }
                popupWindow.onDestroy();
                mBasePopupWindow.clear();
                mBasePopupWindow = null;
                source.getLifecycle().removeObserver(this);
            }
        }
    }
}
