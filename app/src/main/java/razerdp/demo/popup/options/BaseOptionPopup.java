package razerdp.demo.popup.options;

import android.content.Context;
import android.view.Gravity;
import android.view.animation.Animation;

import razerdp.basepopup.BasePopupWindow;
import razerdp.demo.model.DemoCommonUsageInfo;
import razerdp.demo.utils.ButterKnifeUtil;

/**
 * Created by 大灯泡 on 2019/9/20.
 */
public abstract class BaseOptionPopup<T extends DemoCommonUsageInfo> extends BasePopupWindow {
    protected T mInfo;

    public BaseOptionPopup(Context context) {
        super(context);
        setPopupGravity(Gravity.BOTTOM);
        ButterKnifeUtil.bind(this, getContentView());
    }

    @Override
    protected Animation onCreateShowAnimation() {
        return getTranslateVerticalAnimation(-1f, 0f, 450);
    }


    @Override
    protected Animation onCreateDismissAnimation() {
        return getTranslateVerticalAnimation(0f, -1f, 450);
    }

    public BaseOptionPopup<T> setInfo(T info) {
        mInfo = info;
        return this;
    }
}
