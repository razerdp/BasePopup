package razerdp.basepopup;

import android.app.Activity;
import android.view.View;

import androidx.annotation.Keep;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.OnLifecycleEvent;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Created by 大灯泡 on 2019/5/13
 * <p>
 * Description：
 */
@Keep
public class BasePopupSupporterX implements BasePopupSupporter {
    @Override
    public View findDecorView(BasePopupWindow basePopupWindow, Activity activity) {
        View result = null;
        if (activity instanceof FragmentActivity) {
            try {
                FragmentActivity supportAct = (FragmentActivity) activity;
                List<Fragment> fragments = supportAct.getSupportFragmentManager().getFragments();
                for (Fragment fragment : fragments) {
                    if (fragment instanceof DialogFragment) {
                        DialogFragment d = ((DialogFragment) fragment);
                        if (d.getDialog() != null && d.getDialog().isShowing() && !d.isRemoving()) {
                            result = d.getView();
                            break;
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
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


    @Keep
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
