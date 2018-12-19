package razerdp.demo.fragment.basedemo;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.graphics.Color;
import android.os.Build;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import razerdp.basepopup.BasePopupWindow;
import razerdp.basepopup.R;
import razerdp.demo.fragment.other.SimpleBaseFrag;
import razerdp.demo.popup.BubblePopup;
import razerdp.demo.utils.MultiSpanUtil;
import razerdp.util.SimpleAnimationUtils;

/**
 * Created by 大灯泡 on 2018/12/11.
 */
public class UpdatePopupFrag extends SimpleBaseFrag {
    private ViewHolder vh;
    private static final String DESC = "· 更新BasePopup。";
    private BubblePopup mDemoPopup;

    @Override
    public void onInitView(View rootView) {
        vh = new ViewHolder(rootView);
        MultiSpanUtil.create(DESC)
                .append("OnBlurOptionInitListener").setTextColor(Color.RED)
                .append("setBlurOption").setTextColor(Color.RED)
                .append("单个View模糊").setTextColor(Color.RED)
                .into(vh.tvDesc);
        mDemoPopup = new BubblePopup(getContext());
        mDemoPopup.setPopupGravity(Gravity.RIGHT)
                .setAllowInterceptTouchEvent(false)
                .setShowAnimation(SimpleAnimationUtils.getDefaultAlphaAnimation(true))
                .setDismissAnimation(SimpleAnimationUtils.getDefaultAlphaAnimation(false));

        vh.popupShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDemoPopup.showPopupWindow(v);
            }
        });

        final int[] location = new int[2];

        vh.startAnimate.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                mDemoPopup.showPopupWindow(vh.popupShow);
                if (vh.popupShow.getX() <= mDemoPopup.getScreenWidth() / 2) {
                    vh.popupShow.animate()
                            .translationX(mDemoPopup.getScreenWidth() - vh.popupShow.getWidth())
                            .setDuration(1500)
                            .setUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                                @Override
                                public void onAnimationUpdate(ValueAnimator animation) {
                                    vh.popupShow.getLocationInWindow(location);
                                    mDemoPopup.setContent(String.valueOf(location[0]));
                                    mDemoPopup.update(vh.popupShow);
                                }
                            })
                            .setListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    super.onAnimationEnd(animation);
                                    vh.popupShow.animate().setUpdateListener(null).setListener(null);
                                }
                            })
                            .start();
                } else {
                    vh.popupShow.animate()
                            .translationX(0)
                            .setDuration(1500)
                            .setUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                                @Override
                                public void onAnimationUpdate(ValueAnimator animation) {
                                    vh.popupShow.getLocationInWindow(location);
                                    mDemoPopup.setContent(String.valueOf(location[0]));
                                    mDemoPopup.update(vh.popupShow);
                                }
                            })
                            .setListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    super.onAnimationEnd(animation);
                                    vh.popupShow.animate().setUpdateListener(null).setListener(null);
                                }
                            })
                            .start();
                }
            }
        });
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
        return mInflater.inflate(R.layout.frag_demo_update, container, false);
    }

    public static class ViewHolder {
        public View rootView;
        public TextView tvDesc;
        public Button popupShow;
        public Button startAnimate;

        public ViewHolder(View rootView) {
            this.rootView = rootView;
            this.tvDesc = (TextView) rootView.findViewById(R.id.tv_desc);
            this.popupShow = (Button) rootView.findViewById(R.id.popup_show);
            this.startAnimate = (Button) rootView.findViewById(R.id.start_animate);
        }

    }
}