package razerdp.demo.app;

import android.app.Application;
import android.content.Context;

import com.squareup.leakcanary.LeakCanary;

import razerdp.basepopup.BasePopupWindow;

/**
 * Created by 大灯泡 on 2018/12/4.
 */
public class PopupDemoApp extends Application {
    @Override
    protected void attachBaseContext(Context base) {
        BasePopupWindow.setDebugMode(true);
        super.attachBaseContext(base);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return;
        }
        LeakCanary.install(this);
    }
}
