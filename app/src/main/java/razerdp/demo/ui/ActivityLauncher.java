package razerdp.demo.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import androidx.fragment.app.Fragment;

import razerdp.demo.base.baseactivity.BaseActivity;
import razerdp.demo.utils.ActivityUtil;
import razerdp.util.log.PopupLog;

/**
 * Created by 大灯泡 on 2019/9/20
 * <p>
 * Description：
 */
public class ActivityLauncher {
    public static void start(Object from, Class<? extends Activity> target) {
        start(from, target, null);
    }

    public static void start(Object from, Class<? extends Activity> target, BaseActivity.IntentData data) {
        try {

            if (!(from instanceof Context) && !(from instanceof Fragment)) return;
            boolean hasRequest = data != null && data.getRequestCode() != -1;

            Intent intent;

            if (from instanceof Context) {
                intent = new Intent((Context) from, target);
            } else {
                intent = new Intent(((Fragment) from).getContext(), target);
            }

            if (data != null) {
                data.writeToIntent(intent);
            }

            if (from instanceof Activity) {
                if (hasRequest) {
                    ((Activity) from).startActivityForResult(intent, data.getRequestCode());
                } else {
                    ((Activity) from).startActivity(intent);
                }
                return;
            }

            if (from instanceof Fragment) {
                if (hasRequest) {
                    ((Fragment) from).startActivityForResult(intent, data.getRequestCode());
                } else {
                    ((Fragment) from).startActivity(intent);
                }
                return;
            }

            Activity act = ActivityUtil.getActivity((Context) from);
            if (act == null) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                ((Context) from).startActivity(intent);
            } else {
                act.startActivity(intent);
            }
        } catch (Exception e) {
            PopupLog.e(e);
        }
    }

}
