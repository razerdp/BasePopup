package razerdp.basepopup.popup;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.os.Build;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;
import com.nineoldandroids.view.ViewHelper;
import razerdp.basepopup.R;
import razerdp.basepopup.base.BasePopupWindow;
import razerdp.basepopup.utils.DimensUtils;
import razerdp.basepopup.utils.ToastUtils;

/**
 * Created by 大灯泡 on 2016/1/22.
 * 菜单。
 */
public class MenuPopup extends BasePopupWindow implements View.OnClickListener {

    private TextView tx1;
    private TextView tx2;
    private TextView tx3;

    private int[] viewLocation;

    public MenuPopup(Activity context) {
        super(context, ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        viewLocation=new int[2];
        mPopupView.findViewById(R.id.tx_1).setOnClickListener(this);
        mPopupView.findViewById(R.id.tx_2).setOnClickListener(this);
        mPopupView.findViewById(R.id.tx_3).setOnClickListener(this);
    }

    @Override
    protected Animation getAnimation() {
        AnimationSet set=new AnimationSet(true);
        set.setInterpolator(new DecelerateInterpolator());
        set.addAnimation(getScaleAnimation(0,1,0,1,Animation.RELATIVE_TO_SELF,1,Animation.RELATIVE_TO_SELF,0));
        set.addAnimation(getDefaultAlphaAnimation());
        return set;
        //return null;
    }

    @Override
    protected Animator getAnimator() {
       /* AnimatorSet set=new AnimatorSet();
        set.playTogether(
                ObjectAnimator.ofFloat(mAnimaView,"scaleX",0.0f,1.0f).setDuration(300),
                ObjectAnimator.ofFloat(mAnimaView,"scaleY",0.0f,1.0f).setDuration(300),
                ObjectAnimator.ofFloat(mAnimaView,"alpha",0.0f,1.0f).setDuration(300*3/2));*/
        return null;
    }

    @Override
    public void showPopupWindow(View v) {
        try {
            v.getLocationOnScreen(viewLocation);
            mPopupWindow.showAtLocation(v, Gravity.RIGHT | Gravity.TOP, (int)(v.getWidth()*1.5),
                    viewLocation[1]+((v.getHeight()*2/3)));
            if (getAnimation() != null && mAnimaView != null) {
                mAnimaView.clearAnimation();
                mAnimaView.startAnimation(getAnimation());
            }
            if (getAnimation() == null && getAnimator() != null && mAnimaView != null &&
                    Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                ViewHelper.setPivotX(mAnimaView,1);
                ViewHelper.setPivotY(mAnimaView,0);
                getAnimator().start();
            }
        } catch (Exception e) {
            Log.w("error","error");
        }
    }
    @Override
    protected View getInputView() {
        return null;
    }

    @Override
    protected View getDismissView() {
        return null;
    }

    @Override
    public View getPopupView() {
        return getPopupViewById(R.layout.popup_menu);
    }

    @Override
    public View getAnimaView() {
        return mPopupView.findViewById(R.id.popup_contianer);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tx_1:
                ToastUtils.ToastMessage(mContext,"click tx_1");
                break;
            case R.id.tx_2:
                ToastUtils.ToastMessage(mContext,"click tx_2");
                break;
            case R.id.tx_3:
                ToastUtils.ToastMessage(mContext,"click tx_3");
                break;
            default:
                break;

        }

    }
}
