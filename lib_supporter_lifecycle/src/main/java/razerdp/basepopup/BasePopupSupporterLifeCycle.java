package razerdp.basepopup;

import android.app.Activity;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.OnLifecycleEvent;
import android.view.View;

import java.lang.ref.WeakReference;

/**
 * Created by 大灯泡 on 2019/5/13
 * <p>
 * Description：
 */
public class BasePopupSupporterLifeCycle implements BasePopupSupporter {

    @Override
    public View findDecorView(BasePopupWindow basePopupWindow, Activity activity) {
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
            popupWindow.removeListener();
            removeLifeCycle(popupWindow, popupWindow.getContext());
        }


        BasePopupWindow getPopup() {
            if (mBasePopupWindow == null) return null;
            return mBasePopupWindow.get();
        }
    }
}
