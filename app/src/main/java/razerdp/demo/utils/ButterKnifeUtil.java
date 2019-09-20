package razerdp.demo.utils;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import razerdp.util.log.PopupLog;

/**
 * Created by 大灯泡 on 2019/8/19
 * <p>
 * Description：增加lifecycle监听
 */
public class ButterKnifeUtil {
    private static final String TAG = "ButterKnifeUtil";

    public static void bind(@NonNull Object target, @NonNull View source) {
        LifeCycleHolder.handle(ActivityUtil.getActivity(source.getContext()), ButterKnife.bind(target, source), new LifeCycleHolder.Callback<Unbinder>() {
            @Override
            public void onDestroy(@Nullable Unbinder obj) {
                PopupLog.i(TAG, source.getContext(), obj);
                try {
                    if (obj != null) {
                        obj.unbind();
                    }
                } catch (Exception e) {
                    PopupLog.e(TAG, e);
                }
            }
        });
    }
}
