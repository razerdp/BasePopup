package razerdp.demo.fragment.basedemo;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.graphics.Color;
import android.support.v7.widget.AppCompatCheckBox;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.TextView;

import razerdp.basepopup.BasePopupWindow;
import razerdp.basepopup.R;
import razerdp.demo.fragment.other.SimpleBaseFrag;
import razerdp.demo.popup.DemoPopup;
import razerdp.demo.utils.MultiSpanUtil;
import razerdp.util.SimpleAnimationUtils;

/**
 * Created by 大灯泡 on 2018/12/10.
 */
public class AnimatePopupFrag extends SimpleBaseFrag {
    private ViewHolder vh;
    private static final String DESC = "· 设置BasePopup弹出/退出动画，如果同种动作同时实现了animtion和animator，则放弃animator。\n" +
            "· 动画的setter会覆盖内部onCreateAnimation/onCreateAnimator的动画";
    private DemoPopup mDemoPopup;

    @Override
    public void onInitView(View rootView) {
        vh = new ViewHolder(rootView);
        MultiSpanUtil.create(DESC)
                .append("则放弃animator").setTextColor(Color.RED)
                .append("覆盖").setTextColor(Color.RED)
                .into(vh.tvDesc);
        mDemoPopup = new DemoPopup(getContext());
        mDemoPopup.setPopupGravity(Gravity.CENTER);

        mDemoPopup.setOnBeforeShowCallback(new BasePopupWindow.OnBeforeShowCallback() {
            @Override
            public boolean onBeforeShow(View contentView, View anchorView, boolean hasShowAnimate) {
                if (hasShowAnimate) {
                    mDemoPopup.getDisplayAnimateView().setTranslationY(0);
                }
                return true;
            }
        });

        vh.popupShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDemoPopup.setShowAnimation(null);
                mDemoPopup.setShowAnimator(null);
                mDemoPopup.setDismissAnimation(null);
                mDemoPopup.setDismissAnimator(null);

                if (vh.checkShowAnimation.isChecked()) {
                    mDemoPopup.setShowAnimation(showAnimation());
                }
                if (vh.checkShowAnimator.isChecked()) {
                    mDemoPopup.setClipChildren(false);
                    mDemoPopup.setShowAnimator(showAnimator(mDemoPopup.getDisplayAnimateView(), mDemoPopup.getHeight()));
                }
                if (vh.checkDismissAnimation.isChecked()) {
                    mDemoPopup.setDismissAnimation(dismissAnimation());
                }
                if (vh.checkDismissanimator.isChecked()) {
                    mDemoPopup.setClipChildren(false);
                    mDemoPopup.setDismissAnimator(dismissAnimator(mDemoPopup.getDisplayAnimateView(), mDemoPopup.getHeight()));
                }
                mDemoPopup.showPopupWindow();
            }
        });

    }

    private Animation showAnimation() {
        return SimpleAnimationUtils.getDefaultScaleAnimation(true);
    }

    private Animation dismissAnimation() {
        return SimpleAnimationUtils.getDefaultScaleAnimation(false);
    }

    private Animator showAnimator(View target, int translationY) {
        ObjectAnimator result = ObjectAnimator.ofFloat(target, View.TRANSLATION_Y, translationY * 0.75f, 0);
        result.setDuration(1200);
        result.setInterpolator(new OvershootInterpolator(6));
        return result;
    }

    private Animator dismissAnimator(View target, int translationY) {
        ObjectAnimator result = ObjectAnimator.ofFloat(target, View.TRANSLATION_Y, 0, translationY * 0.75f);
        result.setDuration(1200);
        result.setInterpolator(new OvershootInterpolator(-8));
        return result;
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
        return mInflater.inflate(R.layout.frag_demo_animate, container, false);
    }

    public static class ViewHolder {
        public View rootView;
        public TextView tvDesc;
        public AppCompatCheckBox checkShowAnimation;
        public AppCompatCheckBox checkShowAnimator;
        public AppCompatCheckBox checkDismissAnimation;
        public AppCompatCheckBox checkDismissanimator;
        public Button popupShow;

        public ViewHolder(View rootView) {
            this.rootView = rootView;
            this.tvDesc = (TextView) rootView.findViewById(R.id.tv_desc);
            this.checkShowAnimation = (AppCompatCheckBox) rootView.findViewById(R.id.check_show_animation);
            this.checkShowAnimator = (AppCompatCheckBox) rootView.findViewById(R.id.check_show_animator);
            this.checkDismissAnimation = (AppCompatCheckBox) rootView.findViewById(R.id.check_dismiss_animation);
            this.checkDismissanimator = (AppCompatCheckBox) rootView.findViewById(R.id.check_dismissanimator);
            this.popupShow = (Button) rootView.findViewById(R.id.popup_show);
        }

    }
}
