package razerdp.demo.utils;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import java.util.ArrayList;
import java.util.List;

import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;

/**
 * Created by 大灯泡 on 2019/8/22
 * <p>
 * Description：
 */
public class TransitionUtil {
    public static final boolean ENABLE = Build.VERSION.SDK_INT >= 21;

    public static void enableTransition(Activity activity) {
        if (ENABLE) {
            activity.getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        }
    }

    /**
     * 解决默认fade导致的闪屏问题
     */
    public static void fixFadeFlashTransition(Activity act) {
        if (act == null || !ENABLE) return;
        Window window = act.getWindow();
        window.setTransitionBackgroundFadeDuration(400);
        if (window.getEnterTransition() != null) {
            window.getEnterTransition()
                    .excludeTarget(android.R.id.statusBarBackground, true)
                    .excludeTarget(android.R.id.navigationBarBackground, true);
        }
        if (window.getExitTransition()!=null){
            window.getExitTransition()
                    .excludeTarget(android.R.id.statusBarBackground, true)
                    .excludeTarget(android.R.id.navigationBarBackground, true);
        }
    }

    public static Bundle getTransitionBundle(Activity activity, View... shareViews) {
        if (!ENABLE || shareViews == null) {
            return null;
        }
        List<Pair<View, String>> pairs = new ArrayList<>();
        //添加ShareElements
        for (int i = 0; i < shareViews.length; i++) {
            View view = shareViews[i];
            pairs.add(Pair.create(view, view.getTransitionName()));
        }
        Pair<View, String>[] pairsArray = new Pair[pairs.size()];
        pairs.toArray(pairsArray);
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, pairsArray);
        return options.toBundle();
    }
}
