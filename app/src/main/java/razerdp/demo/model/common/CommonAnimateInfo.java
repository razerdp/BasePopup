package razerdp.demo.model.common;

import android.view.View;
import android.view.animation.Animation;

import razerdp.demo.model.DemoCommonUsageInfo;
import razerdp.demo.popup.PopupAnimate;
import razerdp.demo.popup.options.PopupAnimateOption;

/**
 * Created by 大灯泡 on 2019/9/20
 * <p>
 * Description：
 */
public class CommonAnimateInfo extends DemoCommonUsageInfo {

    public Animation showAnimation;
    public Animation dismissAnimation;
    public boolean blur;
    public boolean clip;

    PopupAnimateOption popupAnimateOption;
    PopupAnimate popupAnimate;

    public CommonAnimateInfo() {
        title = "动画展示";
    }

    @Override
    public void toShow(View v) {
        if (popupAnimate == null) {
            popupAnimate = new PopupAnimate(v.getContext());
        }
        popupAnimate.setShowAnimation(showAnimation)
                .setDismissAnimation(dismissAnimation)
                .setBlurBackgroundEnable(blur)
                .setClipChildren(clip);
        popupAnimate.showPopupWindow();

    }

    @Override
    public void toOption(View v) {
        if (popupAnimateOption == null) {
            popupAnimateOption = new PopupAnimateOption(v.getContext());
            popupAnimateOption.setInfo(this);
        }
        popupAnimateOption.showPopupWindow(v);

    }
}
