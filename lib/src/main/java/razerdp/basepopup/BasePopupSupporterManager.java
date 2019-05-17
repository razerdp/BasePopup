package razerdp.basepopup;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import razerdp.util.PopupUtils;
import razerdp.util.log.PopupLog;

/**
 * Created by 大灯泡 on 2019/5/13
 * <p>
 * Description：支持管理器
 */
public final class BasePopupSupporterManager {


    BasePopupSupporterProxy proxy;
    private WeakReference<Activity> mTopActivity;
    private int account = 0;


    class BasePopupSupporterProxy implements BasePopupSupporter {
        private List<BasePopupSupporter> IMPL;
        private static final String IMPL_SUPPORT = "razerdp.basepopup.BasePopupSupporterSupport";
        private static final String IMPL_LIFECYCLE = "razerdp.basepopup.BasePopupSupporterLifeCycle";
        private static final String IMPL_X = "razerdp.basepopup.BasePopupSupporterX";


        BasePopupSupporterProxy(Context context) {
            IMPL = new ArrayList<>();
            try {
                if (isClassExist(IMPL_SUPPORT)) {
                    IMPL.add((BasePopupSupporter) Class.forName(IMPL_SUPPORT).newInstance());
                }
                if (isClassExist(IMPL_LIFECYCLE)) {
                    IMPL.add((BasePopupSupporter) Class.forName(IMPL_LIFECYCLE).newInstance());
                }
                if (isClassExist(IMPL_X)) {
                    IMPL.add((BasePopupSupporter) Class.forName(IMPL_X).newInstance());
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
            if (PopupUtils.isListEmpty(IMPL)) return null;
            for (BasePopupSupporter basePopupSupporter : IMPL) {
                View result = basePopupSupporter.findDecorView(basePopupWindow, activity);
                if (result != null) {
                    return result;
                }
            }
            return null;
        }

        @Override
        public BasePopupWindow attachLifeCycle(BasePopupWindow basePopupWindow, Object owner) {
            if (PopupUtils.isListEmpty(IMPL)) return null;
            for (BasePopupSupporter basePopupSupporter : IMPL) {
                if (basePopupWindow.lifeCycleObserver != null) return basePopupWindow;
                basePopupSupporter.attachLifeCycle(basePopupWindow, owner);
            }
            return basePopupWindow;
        }

        @Override
        public BasePopupWindow removeLifeCycle(BasePopupWindow basePopupWindow, Object owner) {
            if (PopupUtils.isListEmpty(IMPL)) return null;
            for (BasePopupSupporter basePopupSupporter : IMPL) {
                if (basePopupWindow.lifeCycleObserver == null) return basePopupWindow;
                basePopupSupporter.removeLifeCycle(basePopupWindow, owner);
            }
            return basePopupWindow;
        }
    }


    private static class SingleTonHolder {
        private static BasePopupSupporterManager INSTANCE = new BasePopupSupporterManager();
    }


    private BasePopupSupporterManager() {

    }

    void init(Context context) {
        if (proxy != null) return;
        if (context instanceof Application) {
            regLifeCallback(((Application) context));
        } else {
            regLifeCallback((Application) context.getApplicationContext());
        }
        proxy = new BasePopupSupporterProxy(context);
    }

    public Activity getTopActivity() {
        return mTopActivity == null ? null : mTopActivity.get();
    }

    public boolean isAppOnBackground() {
        PopupLog.i("isAppOnBackground", account <= 0);
        return account <= 0;
    }

    private void regLifeCallback(Application context) {
        context.registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
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

    public static BasePopupSupporterManager getInstance() {
        return SingleTonHolder.INSTANCE;
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
