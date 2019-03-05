package razerdp.demo.fragment.basedemo;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import razerdp.basepopup.BasePopupWindow;
import razerdp.basepopup.R;
import razerdp.demo.fragment.other.SimpleBaseFrag;
import razerdp.demo.popup.BubblePopup;
import razerdp.demo.utils.MultiSpanUtil;
import razerdp.demo.utils.UIHelper;
import razerdp.util.SimpleAnimationUtils;

/**
 * Created by 大灯泡 on 2018/12/11.
 */
public class UpdatePopupFrag extends SimpleBaseFrag {
    private ViewHolder vh;
    private static final String DESC = "· 更新BasePopup位置，允许指定跟随某个view或者传入某个位置。\n" +
            "· 一般应用于setAllowInterceptTouchEvent(false)情况，这样才可以响应外部事件嘛\n" +
            "· 如果允许拦截事件setAllowInterceptTouchEvent(true)，那么模糊也是允许的，但因为动态模糊，对同步性要求较高，因此强制性主线程模糊，请尽量避免持续性update的情况下使用模糊\n";
    private BubblePopup mDemoPopup;
    int[] location = new int[2];

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onInitView(View rootView) {
        vh = new ViewHolder(rootView);
        MultiSpanUtil.create(DESC)
                .append("setAllowInterceptTouchEvent(false)").setTextColor(UIHelper.getColor(R.color.colorPrimary))
                .append("false").setTextColor(Color.RED)
                .append("setAllowInterceptTouchEvent(true)").setTextColor(UIHelper.getColor(R.color.colorPrimary))
                .append("true").setTextColor(Color.RED)
                .append("模糊").setTextColor(Color.RED)
                .append("强制性主线程模糊").setTextColor(Color.RED).setTextSize(16).setTextType(Typeface.DEFAULT_BOLD)
                .append("避免持续性update").setTextColor(Color.RED)
                .into(vh.tvDesc);

        mDemoPopup = new BubblePopup(getContext());
        mDemoPopup.setPopupGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL)
                .setAllowInterceptTouchEvent(false)
                .setAllowDismissWhenTouchOutside(false)
                //linkto方法添加，因此不用自己去update
                .linkTo(vh.popupShow)
                .setShowAnimation(SimpleAnimationUtils.getDefaultAlphaAnimation(true))
                .setDismissAnimation(SimpleAnimationUtils.getDefaultAlphaAnimation(false));

        vh.popupShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mDemoPopup.isShowing()) {
                    mDemoPopup.showPopupWindow(v);
                }
            }
        });

        vh.popupShow.setOnTouchListener(new View.OnTouchListener() {
            float lastX, lastY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        lastX = event.getRawX();
                        lastY = event.getRawY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        float curX = event.getRawX();
                        float curY = event.getRawY();
                        v.offsetLeftAndRight((int) (curX - lastX));
                        v.offsetTopAndBottom((int) (curY - lastY));
                        lastX = curX;
                        lastY = curY;
                        updateLocationText(vh.popupShow);
                        break;
                }
                return false;
            }
        });


        vh.startAnimate.setOnClickListener(new View.OnClickListener() {

            @TargetApi(Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                if (!mDemoPopup.isShowing()) {
                    mDemoPopup.showPopupWindow(vh.popupShow);
                }
                if (vh.popupShow.getX() <= mDemoPopup.getScreenWidth() / 2) {
                    vh.popupShow.animate()
                            .translationX(mDemoPopup.getScreenWidth() - vh.popupShow.getX() - vh.popupShow.getWidth())
                            .setDuration(1500)
                            .setUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                                @Override
                                public void onAnimationUpdate(ValueAnimator animation) {
                                    updateLocationText(vh.popupShow);
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
                                    updateLocationText(vh.popupShow);
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

    private void updateLocationText(View v) {
        v.getLocationInWindow(location);
        mDemoPopup.setContent("x = " + location[0] + "\ny = " + location[1]);
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
        public TextView popupShow;
        public Button startAnimate;

        public ViewHolder(View rootView) {
            this.rootView = rootView;
            this.tvDesc = (TextView) rootView.findViewById(R.id.tv_desc);
            this.popupShow = (TextView) rootView.findViewById(R.id.popup_show);
            this.startAnimate = (Button) rootView.findViewById(R.id.start_animate);
        }

    }
}