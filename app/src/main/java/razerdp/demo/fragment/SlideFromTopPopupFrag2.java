package razerdp.demo.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import razerdp.basepopup.BasePopupWindow;
import razerdp.basepopup.R;
import razerdp.demo.popup.SlideFromTopPopup2;

/**
 * Created by 大灯泡 on 2018/4/09.
 * <p>
 * 从顶部向下滑动的popup
 */
public class SlideFromTopPopupFrag2 extends SimpleBaseFrag {

    private SlideFromTopPopup2 mSlideFromTopPopup;
    private RelativeLayout titleParent;

    private ImageView arrow;


    private RotateAnimation showArrowAnima;
    private RotateAnimation dismissArrowAnima;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        buildShowArrowAnima();
        buildDismissArrowAnima();
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    private void buildShowArrowAnima() {
        if (showArrowAnima != null) return;
        showArrowAnima = new RotateAnimation(0, 180f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        showArrowAnima.setDuration(450);
        showArrowAnima.setInterpolator(new AccelerateDecelerateInterpolator());
        showArrowAnima.setFillAfter(true);
    }

    private void buildDismissArrowAnima() {
        if (dismissArrowAnima != null) return;
        dismissArrowAnima = new RotateAnimation(180f, 0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        dismissArrowAnima.setDuration(450);
        dismissArrowAnima.setInterpolator(new AccelerateDecelerateInterpolator());
        dismissArrowAnima.setFillAfter(true);
    }

    private void startShowArrowAnima() {
        if (arrow == null) return;
        arrow.clearAnimation();
        arrow.startAnimation(showArrowAnima);
    }

    private void startDismissArrowAnima() {
        if (arrow == null) return;
        arrow.clearAnimation();
        arrow.startAnimation(dismissArrowAnima);
    }


    @Override
    public void bindEvent() {
        titleParent = (RelativeLayout) findViewById(R.id.rl_title);
        mSlideFromTopPopup = new SlideFromTopPopup2(mContext);
        mSlideFromTopPopup.setOnDismissListener(onDismissListener);
        arrow = (ImageView) findViewById(R.id.iv_arrow);

        findViewById(R.id.ll_title).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mSlideFromTopPopup.isShowing()) startShowArrowAnima();
                mSlideFromTopPopup.showPopupWindow(titleParent);
            }
        });
    }


    private BasePopupWindow.OnDismissListener onDismissListener = new BasePopupWindow.OnDismissListener() {

        @Override
        public boolean onBeforeDismiss() {
            startDismissArrowAnima();
            return super.onBeforeDismiss();
        }

        @Override
        public void onDismiss() {

        }
    };

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
        return mInflater.inflate(R.layout.frag_slide_from_top_popup, container, false);
    }
}
