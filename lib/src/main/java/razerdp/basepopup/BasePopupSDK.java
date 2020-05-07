package razerdp.basepopup;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;

import java.lang.ref.WeakReference;

/**
 * Created by 大灯泡 on 2019/5/13
 * <p>
 * Description：
 */
public final class BasePopupSDK {

    private static volatile Application mApplicationContext;
    private WeakReference<Activity> mTopActivity;

    private static class SingletonHolder {
        private static BasePopupSDK INSTANCE = new BasePopupSDK();
    }


    private BasePopupSDK() {
    }

    synchronized void init(Context context) {
        if (mApplicationContext != null) return;
        mApplicationContext = (Application) context.getApplicationContext();
        regLifeCallback();
    }

    public Activity getTopActivity() {
        return mTopActivity == null ? null : mTopActivity.get();
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

    public static BasePopupSDK getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public static Application getApplication() {
        return mApplicationContext;
    }
}
