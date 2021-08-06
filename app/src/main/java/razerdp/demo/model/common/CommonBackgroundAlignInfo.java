package razerdp.demo.model.common;

import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;

import razerdp.demo.model.DemoCommonUsageInfo;
import razerdp.demo.popup.DemoPopup;
import razerdp.demo.popup.options.PopupAlignBackgroundOption;
import razerdp.util.animation.AnimationHelper;
import razerdp.util.animation.TranslationConfig;

/**
 * Created by 大灯泡 on 2019/9/21.
 */
public class CommonBackgroundAlignInfo extends DemoCommonUsageInfo {
    DemoPopup mDemoPopup;
    PopupAlignBackgroundOption mPopupAlignBackgroundOption;

    public boolean align;
    public boolean blur;
    public boolean syncMaskAnimation = true;
    public boolean overlayMask = false;
    public int alignGravity;
    public Animation maskShowAnimation;
    public Animation maskDismissAnimation;
    public int maskOffsetX;
    public int maskOffsetY;
    public long contentDuration = 500;

    public CommonBackgroundAlignInfo() {
        title = "背景蒙层控制";
        name = "DemoPopup";
        javaUrl = "https://github.com/razerdp/BasePopup/blob/master/app/src/main/java/razerdp/demo/popup/DemoPopup.java";
        resUrl = "https://github.com/razerdp/BasePopup/blob/master/app/src/main/res/layout/popup_demo.xml";
    }

    @Override
    public void toShow(View v) {
        if (mDemoPopup == null) {
            mDemoPopup = new DemoPopup(v.getContext()).setText("背景蒙层控制\n点我弹出新的DemoPopup");
            mDemoPopup.getTextView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new DemoPopup(v.getContext()).setOverlayMask(overlayMask).showPopupWindow();
                }
            });
            mDemoPopup.setPopupGravity(Gravity.TOP);
        }
        mDemoPopup.setAlignBackground(align)
                .setMaskOffsetX(maskOffsetX)
                .setMaskOffsetY(maskOffsetY)
                .syncMaskAnimationDuration(syncMaskAnimation)
                .setAlignBackgroundGravity(alignGravity)
                .setBlurBackgroundEnable(blur);
        mDemoPopup.setShowAnimation(AnimationHelper.asAnimation()
                                            .withTranslation(TranslationConfig.FROM_BOTTOM.duration(contentDuration))
                                            .toShow())
                .setDismissAnimation(AnimationHelper.asAnimation()
                                             .withTranslation(TranslationConfig.TO_BOTTOM.duration(contentDuration))
                                             .toDismiss());
        if (maskShowAnimation != null) {
            maskShowAnimation.setDuration(syncMaskAnimation ? 0 : 500);
            mDemoPopup.setMaskViewShowAnimation(maskShowAnimation);
        }
        if (maskDismissAnimation != null) {
            maskDismissAnimation.setDuration(syncMaskAnimation ? 0 : 500);
            mDemoPopup.setMaskViewDismissAnimation(maskDismissAnimation);
        }
        mDemoPopup.showPopupWindow(v);
    }

    @Override
    public void toOption(View v) {
        if (mPopupAlignBackgroundOption == null) {
            mPopupAlignBackgroundOption = new PopupAlignBackgroundOption(v.getContext());
            mPopupAlignBackgroundOption.setInfo(this);
        }
        mPopupAlignBackgroundOption.showPopupWindow();

    }
}
