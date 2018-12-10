package razerdp.demo.app;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;

/**
 * Created by 大灯泡 on 2017/3/22.
 * <p>
 * baselib appcontext类
 * <p>
 * 该工具类方法来自：
 * https://github.com/kymjs/Common/blob/master/Common/common/src/main/java/com/kymjs/common/App.java
 */

public class AppContext {

    private static final String TAG = "AppContext";
    public static final Application sApplication;
    private static final InnerLifecycleHandler INNER_LIFECYCLE_HANDLER;

    static {
        Application app = null;
        try {
            app = (Application) Class.forName("android.app.AppGlobals").getMethod("getInitialApplication").invoke(null);
            if (app == null)
                throw new IllegalStateException("Static initialization of Applications must be on main thread.");
        } catch (final Exception e) {
            Log.e(TAG, "Failed to get current application from AppGlobals." + e.getMessage());
            try {
                app = (Application) Class.forName("android.app.ActivityThread").getMethod("currentApplication").invoke(null);
            } catch (final Exception ex) {
                Log.e(TAG, "Failed to get current application from ActivityThread." + e.getMessage());
            }
        } finally {
            sApplication = app;
        }
        INNER_LIFECYCLE_HANDLER = new InnerLifecycleHandler();
        if (sApplication != null) {
            sApplication.registerActivityLifecycleCallbacks(INNER_LIFECYCLE_HANDLER);
        }
    }

    public static boolean isAppVisable() {
        return INNER_LIFECYCLE_HANDLER != null && INNER_LIFECYCLE_HANDLER.started > INNER_LIFECYCLE_HANDLER.stopped;
    }

    public static boolean isAppBackground() {
        return INNER_LIFECYCLE_HANDLER != null && INNER_LIFECYCLE_HANDLER.resumed <= INNER_LIFECYCLE_HANDLER.stopped;
    }

    private static void checkAppContext() {
        if (sApplication == null)
            throw new IllegalStateException("app reference is null");
    }

    public static Application getAppInstance() {
        checkAppContext();
        return sApplication;
    }

    public static Context getAppContext() {
        checkAppContext();
        return sApplication.getApplicationContext();
    }

    public static Resources getResources() {
        checkAppContext();
        return sApplication.getResources();
    }

    public static boolean isMainThread() {
        return Looper.getMainLooper().getThread() == Thread.currentThread();
    }


    private static class InnerLifecycleHandler implements Application.ActivityLifecycleCallbacks {
        private int created;
        private int resumed;
        private int paused;
        private int started;
        private int stopped;

        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
            ++created;

        }

        @Override
        public void onActivityStarted(Activity activity) {
            ++started;

        }

        @Override
        public void onActivityResumed(Activity activity) {
            ++resumed;

        }

        @Override
        public void onActivityPaused(Activity activity) {
            ++paused;

        }

        @Override
        public void onActivityStopped(Activity activity) {
            ++stopped;

        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

        }

        @Override
        public void onActivityDestroyed(Activity activity) {

        }
    }

}
