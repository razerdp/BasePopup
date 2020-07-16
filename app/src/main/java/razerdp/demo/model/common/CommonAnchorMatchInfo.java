package razerdp.demo.model.common;

import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;

import razerdp.basepopup.BasePopupWindow;
import razerdp.demo.model.DemoCommonUsageInfo;
import razerdp.demo.popup.PopupSlide;
import razerdp.demo.popup.options.PopupAnchorMatchOption;
import razerdp.util.animation.AnimationHelper;
import razerdp.util.animation.ScaleConfig;

/**
 * Created by 大灯泡 on 2019/9/20
 * <p>
 * Description：
 */
public class CommonAnchorMatchInfo extends DemoCommonUsageInfo {

    public int gravity = Gravity.BOTTOM;
    public boolean widthMatch = true;
    public boolean heightMatch;

    PopupAnchorMatchOption mPopupOption;
    PopupSlide mPopupSlide;

    public CommonAnchorMatchInfo() {
        title = "关联anchor\n同时宽或者高为match_parent";
    }

    @Override
    public void toShow(View v) {
        BasePopupWindow popupWindow;
        float fromX = 0;
        float fromY = 0;
        float toX = 0;
        float toY = 0;
        Animation showAnimation = AnimationHelper.asAnimation()
                .withScale(ScaleConfig.CENTER)
                .toShow();
        Animation dismissAnimation = AnimationHelper.asAnimation()
                .withScale(ScaleConfig.CENTER)
                .toDismiss();
        if (mPopupSlide == null) {
            mPopupSlide = new PopupSlide(v.getContext());
        }
        popupWindow = mPopupSlide;
        popupWindow.setWidth(widthMatch ? ViewGroup.LayoutParams.MATCH_PARENT : ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setHeight(heightMatch ? ViewGroup.LayoutParams.MATCH_PARENT : ViewGroup.LayoutParams.WRAP_CONTENT);

        switch (gravity & Gravity.HORIZONTAL_GRAVITY_MASK) {
            case Gravity.LEFT:
                fromX = 1f;
                break;
            case Gravity.RIGHT:
                fromX = -1f;
                break;
        }

        switch (gravity & Gravity.VERTICAL_GRAVITY_MASK) {
            case Gravity.TOP:
                fromY = 1f;
                break;
            case Gravity.BOTTOM:
                fromY = -1f;
                break;
        }
        if (fromX != 0 || fromY != 0) {
            showAnimation = createTranslateAnimation(fromX, toX, fromY, toY);
            dismissAnimation = createTranslateAnimation(toX, fromX, toY, fromY);
        }


        popupWindow.setPopupGravity(gravity);
        popupWindow.setShowAnimation(showAnimation);
        popupWindow.setDismissAnimation(dismissAnimation);
        popupWindow.showPopupWindow(v);

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

    @Override
    public void toOption(View v) {
        if (mPopupOption == null) {
            mPopupOption = new PopupAnchorMatchOption(v.getContext());
            mPopupOption.setInfo(this);
        }
        mPopupOption.showPopupWindow(v);
    }


}
