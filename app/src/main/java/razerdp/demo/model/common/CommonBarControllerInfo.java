package razerdp.demo.model.common;

import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;

import razerdp.basepopup.BasePopupFlag;
import razerdp.demo.model.DemoCommonUsageInfo;
import razerdp.demo.popup.DemoPopup;
import razerdp.demo.popup.options.PopupBarControllerOption;
import razerdp.util.animation.AnimationHelper;
import razerdp.util.animation.ScaleConfig;

/**
 * Created by 大灯泡 on 2020/10/30.
 */
public class CommonBarControllerInfo extends DemoCommonUsageInfo {
    public int gravity = Gravity.CENTER;
    public boolean matchHorizontal = false;
    public boolean matchVertical = false;
    public boolean overlayStatusbar = true;
    public boolean overlayNavigationBar = true;
    public int overlayStatusbarMode = BasePopupFlag.OVERLAY_MASK | BasePopupFlag.OVERLAY_CONTENT;
    public int overlayNavigationBarMode = BasePopupFlag.OVERLAY_MASK;

    DemoPopup mDemoPopup;
    PopupBarControllerOption option;

    public CommonBarControllerInfo() {
        title = "控制系统Bar覆盖";
    }

    @Override
    public void toShow(View v) {
        if (mDemoPopup == null) {
            mDemoPopup = new DemoPopup(v.getContext());
        }
        mDemoPopup.setWidth(matchHorizontal ? ViewGroup.LayoutParams.MATCH_PARENT : 0);
        mDemoPopup.setHeight(matchVertical ? ViewGroup.LayoutParams.MATCH_PARENT : 0);
        mDemoPopup.setOverlayStatusbar(overlayStatusbar);
        mDemoPopup.setOverlayNavigationBar(overlayNavigationBar);
        mDemoPopup.setOverlayStatusbarMode(overlayStatusbarMode);
        mDemoPopup.setOverlayNavigationBarMode(overlayNavigationBarMode);
        mDemoPopup.setPopupGravity(gravity);
        Animation showAnimation = AnimationHelper.asAnimation()
                .withScale(ScaleConfig.CENTER)
                .toShow();
        Animation dismissAnimation = AnimationHelper.asAnimation()
                .withScale(ScaleConfig.CENTER)
                .toDismiss();
        float fromX = 0;
        float fromY = 0;
        float toX = 0;
        float toY = 0;

        switch (gravity & Gravity.HORIZONTAL_GRAVITY_MASK) {
            case Gravity.LEFT:
                fromX = -1f;
                break;
            case Gravity.RIGHT:
                fromX = 1f;
                break;
        }

        switch (gravity & Gravity.VERTICAL_GRAVITY_MASK) {
            case Gravity.TOP:
                fromY = -1f;
                break;
            case Gravity.BOTTOM:
                fromY = 1f;
                break;
        }
        if (fromX != 0 || fromY != 0) {
            showAnimation = createTranslateAnimation(fromX, toX, fromY, toY);
            dismissAnimation = createTranslateAnimation(toX, fromX, toY, fromY);
        }
        mDemoPopup.setShowAnimation(showAnimation);
        mDemoPopup.setDismissAnimation(dismissAnimation);
        mDemoPopup.showPopupWindow();
    }

    @Override
    public void toOption(View v) {
        if (option == null) {
            option = new PopupBarControllerOption(v.getContext());
            option.setInfo(this);
        }
        option.showPopupWindow();
    }


    private Animation createTranslateAnimation(float fromX, float toX, float fromY, float toY) {
        Animation animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF,
                fromX,
                Animation.RELATIVE_TO_SELF,
                toX,
                Animation.RELATIVE_TO_SELF,
                fromY,
                Animation.RELATIVE_TO_SELF,
                toY);
        animation.setDuration(500);
        animation.setInterpolator(new DecelerateInterpolator());
        return animation;
    }
}
