package razerdp.basepopup;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;

import java.lang.ref.WeakReference;

import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.OnLifecycleEvent;

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
        if (!(owner instanceof LifecycleOwner) || basePopupWindow.lifeCycleObserver != null) {
            return basePopupWindow;
        }
        ((LifecycleOwner) owner).getLifecycle().addObserver(new BasePopupLifeCycleHolder(basePopupWindow));
        return basePopupWindow;
    }

    @Override
    public BasePopupWindow removeLifeCycle(BasePopupWindow basePopupWindow, Object owner) {
        if (!(owner instanceof LifecycleOwner) || basePopupWindow.lifeCycleObserver == null) {
            return basePopupWindow;
        }
        ((LifecycleOwner) owner).getLifecycle().removeObserver((LifecycleObserver) basePopupWindow.lifeCycleObserver);
        basePopupWindow.lifeCycleObserver = null;
        return basePopupWindow;
    }


    private class BasePopupLifeCycleHolder implements LifecycleObserver {
        WeakReference<BasePopupWindow> mBasePopupWindow;

        BasePopupLifeCycleHolder(BasePopupWindow target) {
            this.mBasePopupWindow = new WeakReference<>(target);
            target.lifeCycleObserver = this;
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
        protected void onActivityDestroy() {
            BasePopupWindow popupWindow = getPopup();
            if (popupWindow == null) return;
            if (popupWindow.isShowing()) {
                popupWindow.forceDismiss();
            }
            if (popupWindow.getContext() instanceof LifecycleOwner) {
                removeLifeCycle(popupWindow, popupWindow.getContext());
            }
        }


        BasePopupWindow getPopup() {
            if (mBasePopupWindow == null) return null;
            return mBasePopupWindow.get();
        }
    }
}
