package razerdp.basepopup;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

import java.lang.ref.WeakReference;

import razerdp.util.log.PopupLog;

/**
 * Created by 大灯泡 on 2019/5/13
 * <p>
 * Description：组件管理器
 */
public final class BasePopupComponentManager {

    private static Application mApplicationContext;

    BasePopupComponentProxy proxy;
    private WeakReference<Activity> mTopActivity;
    private int account = 0;


    class BasePopupComponentProxy implements BasePopupComponent {
        private BasePopupComponent IMPL;
        private static final String IMPL_X = "razerdp.basepopup.BasePopupComponentX";


        BasePopupComponentProxy(Context context) {
            try {
                if (isClassExist(IMPL_X)) {
                    IMPL = ((BasePopupComponent) Class.forName(IMPL_X).newInstance());
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            PopupLog.i(IMPL);
        }

        @Override
        public View findDecorView(BasePopupWindow basePopupWindow, Activity activity) {
            if (IMPL == null) return null;
            return IMPL.findDecorView(basePopupWindow, activity);
        }

        @Override
        public BasePopupWindow attachLifeCycle(BasePopupWindow basePopupWindow, Object owner) {
            if (IMPL == null) return basePopupWindow;
            if (basePopupWindow.lifeCycleObserver != null) return basePopupWindow;
            IMPL.attachLifeCycle(basePopupWindow, owner);
            return basePopupWindow;
        }

        @Override
        public BasePopupWindow removeLifeCycle(BasePopupWindow basePopupWindow, Object owner) {
            if (IMPL == null) return basePopupWindow;
            if (basePopupWindow.lifeCycleObserver == null) return basePopupWindow;
            IMPL.removeLifeCycle(basePopupWindow, owner);
            return basePopupWindow;
        }
    }


    private static class SingleTonHolder {
        private static BasePopupComponentManager INSTANCE = new BasePopupComponentManager();
    }


    private BasePopupComponentManager() {

    }

    void init(Context context) {
        if (proxy != null || mApplicationContext != null) return;
        Reflection.unseal(context);
        mApplicationContext = (Application) context.getApplicationContext();
        regLifeCallback();
        proxy = new BasePopupComponentProxy(context);
    }

    public Activity getTopActivity() {
        return mTopActivity == null ? null : mTopActivity.get();
    }

    public boolean isAppOnBackground() {
        PopupLog.i("isAppOnBackground", account <= 0);
        return account <= 0;
    }

    private void regLifeCallback() {
        mApplicationContext.registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

            }

            @Override
            public void onActivityStarted(Activity activity) {
                account++;
            }

            @Override
            public void onActivityResumed(Activity activity) {
                mTopActivity = new WeakReference<>(activity);
            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {
                account--;
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {

            }
        });
    }

    public static BasePopupComponentManager getInstance() {
        return SingleTonHolder.INSTANCE;
    }

    public static Application getApplication() {
        return mApplicationContext;
    }

    private boolean isClassExist(String name) {
        try {
            Class.forName(name);
        } catch (Exception ex) {
            return false;
        }
        return true;
    }
}
