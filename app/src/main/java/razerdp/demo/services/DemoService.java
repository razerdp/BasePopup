package razerdp.demo.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import razerdp.demo.utils.UIHelper;

/**
 * Created by 大灯泡 on 2019/5/16
 * <p>
 * Description：
 */
public class DemoService extends Service {
    private static final String TAG = "DemoService";
//    DemoPopup demoPopup;

    @Override
    public void onCreate() {
        super.onCreate();
        UIHelper.toast(TAG + "：onCreate()");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        if (demoPopup == null) {
//            demoPopup = new DemoPopup(getApplicationContext());
//            demoPopup.setPopupGravity(Gravity.CENTER)
//                    .setBlurBackgroundEnable(true)
//                    .setShowAnimation(SimpleAnimationUtils.getDefaultAlphaAnimation(true))
//                    .setDismissAnimation(SimpleAnimationUtils.getDefaultAlphaAnimation(false))
//                    .setOnBeforeShowCallback(new BasePopupWindow.OnBeforeShowCallback() {
//                        @Override
//                        public boolean onBeforeShow(View contentView, View anchorView, boolean hasShowAnimate) {
//                            UIHelper.toast(TAG + "：popup show");
//                            return true;
//                        }
//                    });
//        }
//        UIHelper.toast(TAG + "：onStartCommand()");
//        demoPopup.showPopupWindow();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
