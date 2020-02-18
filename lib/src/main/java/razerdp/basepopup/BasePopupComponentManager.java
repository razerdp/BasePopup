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

    private static volatile Application mApplicationContext;
    static final BasePopupComponentProxy proxy;
    private WeakReference<Activity> mTopActivity;
    private boolean unLockSuccess;

    static {
        proxy = new BasePopupComponentProxy();
    }

    static class BasePopupComponentProxy implements BasePopupComponent {
        private BasePopupComponent IMPL;
        private static final String IMPL_X = "razerdp.basepopup.BasePopupComponentX";

        BasePopupComponentProxy() {
            try {
                IMPL = ((BasePopupComponent) Class.forName(IMPL_X).newInstance());
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            PopupLog.i("initComponent", IMPL);
        }

        @Override
        public View findDecorView(BasePopupWindow basePopupWindow, Activity activity) {
            if (IMPL == null) return null;
            return IMPL.findDecorView(basePopupWindow, activity);
        }

        @Override
        public BasePopupWindow attachLifeCycle(BasePopupWindow basePopupWindow, Object owner) {
            if (IMPL != null) {
                IMPL.attachLifeCycle(basePopupWindow, owner);
            }
            return basePopupWindow;
        }
    }

    private static class SingleTonHolder {
        private static BasePopupComponentManager INSTANCE = new BasePopupComponentManager();
    }


    private BasePopupComponentManager() {
    }

    synchronized void init(Context context) {
        if (mApplicationContext != null) return;
        unLockSuccess = Reflection.unseal(context) != -1;
        mApplicationContext = (Application) context.getApplicationContext();
        regLifeCallback();
    }

    public Activity getTopActivity() {
        return mTopActivity == null ? null : mTopActivity.get();
    }

    public boolean hasComponent() {
        return proxy.IMPL != null;
    }

    private void regLifeCallback() {
        mApplicationContext.registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

            }

            @Override
            public void onActivityStarted(Activity activity) {
            }

            @Override
            public void onActivityResumed(Activity activity) {
                if (mTopActivity != null) {
                    mTopActivity.clear();
                }
                mTopActivity = new WeakReference<>(activity);
            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {
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
}
