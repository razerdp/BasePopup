package razerdp.demo.app;

import android.app.Application;

import com.squareup.leakcanary.LeakCanary;

/**
 * Created by 大灯泡 on 2018/12/4.
 */
public class PopupDemoApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return;
        }
        LeakCanary.install(this);
    }
}
