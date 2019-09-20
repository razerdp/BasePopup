package razerdp.demo.utils;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.NonNull;

import razerdp.demo.app.AppContext;


/**
 * Created by 大灯泡 on 2019/4/23
 * <p>
 * Description：
 */
public class ActivityUtil {

    public static Activity getActivity(@NonNull Context from) {
        int limit = 15;
        Context result = from;
        if (result instanceof Activity) {
            return (Activity) result;
        }
        int tryCount = 0;
        while (result instanceof ContextWrapper) {
            if (result instanceof Activity) {
                return (Activity) result;
            }
            if (tryCount > limit) {
                //break endless loop
                return null;
            }
            result = ((ContextWrapper) result).getBaseContext();
            tryCount++;
        }
        return null;
    }

    public static boolean isActivityExists(@NonNull final String pkg,
                                           @NonNull final String cls) {
        Intent intent = new Intent();
        intent.setClassName(pkg, cls);
        return !(AppContext.getAppContext().getPackageManager().resolveActivity(intent, 0) == null ||
                intent.resolveActivity(AppContext.getAppContext().getPackageManager()) == null ||
                AppContext.getAppContext().getPackageManager().queryIntentActivities(intent, 0).size() == 0);
    }

    public static boolean isActivityAlive(Activity act) {
        if (act == null) return false;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            return !act.isFinishing() && !act.isDestroyed();
        } else {
            return !act.isFinishing();
        }
    }
}
