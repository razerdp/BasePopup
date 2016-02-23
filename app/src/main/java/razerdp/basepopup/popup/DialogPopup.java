package razerdp.basepopup.popup;

import android.app.Activity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.CycleInterpolator;
import android.view.animation.RotateAnimation;
import razerdp.basepopup.R;
import razerdp.basepopup.base.BasePopupWindow;

/**
 * Created by 大灯泡 on 2016/1/23.
 * dialogpopup :)
 * 客串一下dialog
 */
public class DialogPopup extends BasePopupWindow{

    public DialogPopup(Activity context) {
        super(context);
    }

    @Override
    protected Animation getShowAnimation() {
        AnimationSet set=new AnimationSet(false);
        Animation shakeAnima=new RotateAnimation(0,15,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        shakeAnima.setInterpolator(new CycleInterpolator(5));
        shakeAnima.setDuration(400);
        set.addAnimation(getDefaultAlphaAnimation());
        set.addAnimation(shakeAnima);
        return set;
    }

    @Override
    protected View getClickToDismissView() {
        return mPopupView;
    }

    @Override
    public View getPopupView() {
        return getPopupViewById(R.layout.popup_dialog);
    }

    @Override
    public View getAnimaView() {
        return mPopupView.findViewById(R.id.popup_anima);
    }
}
