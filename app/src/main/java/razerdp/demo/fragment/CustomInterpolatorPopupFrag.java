package razerdp.demo.fragment;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import razerdp.basepopup.BasePopupWindow;
import razerdp.basepopup.R;
import razerdp.demo.interpolator.CustomInterpolatorFactory;
import razerdp.demo.popup.CustomInterpolatorPopup;

/**
 * Created by 大灯泡 on 2016/1/28.
 * 各种插值器
 */
public class CustomInterpolatorPopupFrag extends SimpleBaseFrag {
    private Button jelly;
    private Button anti;
    private Button anti2;
    private Button spring;
    private Button overshoot;


    private CustomInterpolatorPopup mInterpolatorPopup;

    @Override
    public void bindEvent() {
        jelly= (Button) mFragment.findViewById(R.id.jelly);
        anti= (Button) mFragment.findViewById(R.id.anti);
        anti2= (Button) mFragment.findViewById(R.id.anti2);
        spring= (Button) mFragment.findViewById(R.id.spring);
        overshoot= (Button) mFragment.findViewById(R.id.overshoot);

        jelly.setOnClickListener(this);
        anti.setOnClickListener(this);
        anti2.setOnClickListener(this);
        spring.setOnClickListener(this);
        overshoot.setOnClickListener(this);

        mInterpolatorPopup=new CustomInterpolatorPopup(mContext);
    }

    @Override
    public BasePopupWindow getPopup() {
        return null;
    }

    @Override
    public Button getButton() {
        return null;
    }

    @Override
    public View getFragment() {
        return mInflater.inflate(R.layout.frag_custominterpolator_popup, container, false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.jelly:
                Animation scaleAnimation =
                        new ScaleAnimation(0f, 1f, 0f, 1f, Animation.RELATIVE_TO_SELF, 0.5f,
                                Animation.RELATIVE_TO_SELF, 0.5f);
                scaleAnimation.setDuration(3000);
                scaleAnimation.setInterpolator(CustomInterpolatorFactory.getJellyInterpolator());
                scaleAnimation.setFillEnabled(true);
                scaleAnimation.setFillAfter(true);
                mInterpolatorPopup.setCustomAnimation(scaleAnimation);
                mInterpolatorPopup.showPopupWindow();
                break;
            case R.id.anti:
                Animation rotateAnima =
                        new RotateAnimation(0,360,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
                rotateAnima.setDuration(2500);
                rotateAnima.setInterpolator(CustomInterpolatorFactory.getAnticipateInterpolator());
                rotateAnima.setFillEnabled(true);
                rotateAnima.setFillAfter(true);
                mInterpolatorPopup.setCustomAnimation(rotateAnima);
                mInterpolatorPopup.showPopupWindow();
                break;
            case R.id.anti2:
                Animation rotateAnima2 =
                        new RotateAnimation(0,360,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
                rotateAnima2.setDuration(2500);
                rotateAnima2.setInterpolator(CustomInterpolatorFactory.getAnticipateOverShootInterpolator());
                rotateAnima2.setFillEnabled(true);
                rotateAnima2.setFillAfter(true);
                mInterpolatorPopup.setCustomAnimation(rotateAnima2);
                mInterpolatorPopup.showPopupWindow();
                break;
            case R.id.spring:
                Animation scaleAnimation2 =
                        new ScaleAnimation(0f, 1f, 0f, 1f, Animation.RELATIVE_TO_SELF, 0.5f,
                                Animation.RELATIVE_TO_SELF, 0.5f);
                scaleAnimation2.setDuration(2500);
                scaleAnimation2.setInterpolator(CustomInterpolatorFactory.getSpringInterPolator());
                scaleAnimation2.setFillEnabled(true);
                scaleAnimation2.setFillAfter(true);
                mInterpolatorPopup.setCustomAnimation(scaleAnimation2);
                mInterpolatorPopup.showPopupWindow();
                break;
            case R.id.overshoot:
                Animation translateAnimation = new TranslateAnimation(0, 0, 250*2, 0);
                translateAnimation.setDuration(2500);
                translateAnimation.setFillEnabled(true);
                translateAnimation.setFillAfter(true);
                translateAnimation.setInterpolator(CustomInterpolatorFactory.getOverShootInterpolator());
                mInterpolatorPopup.setCustomAnimation(translateAnimation);
                mInterpolatorPopup.showPopupWindow();
                break;
        }
    }
}