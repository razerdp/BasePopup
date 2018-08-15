package razerdp.basepopup;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import razerdp.library.R;
import razerdp.util.PopupUtil;

/**
 * Created by 大灯泡 on 2018/5/9.
 * <p>
 * background蒙层
 */
class PopupBackgroundView extends View {
    BasePopupHelper mHelper;

    private PopupBackgroundView(Context context) {
        this(context, null);
    }

    private PopupBackgroundView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    private PopupBackgroundView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public static PopupBackgroundView creaete(Context context, BasePopupHelper helper) {
        PopupBackgroundView view = new PopupBackgroundView(context);
        view.init(context, helper);
        return view;
    }

    private void init(Context context, final BasePopupHelper mHelper) {
        if (PopupUtil.isBackgroundInvalidated(mHelper.getPopupBackground()))
        {
            setVisibility(GONE);
            return;
        }
        this.mHelper = mHelper;
        setVisibility(VISIBLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            setBackground(mHelper.getPopupBackground());
        } else {
            setBackgroundDrawable(mHelper.getPopupBackground());
        }
        if (mHelper.isPopupFadeEnable()) {
            getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    Animation fadeIn = AnimationUtils.loadAnimation(getContext(), R.anim.basepopup_fade_in);
                    if (fadeIn != null) {
                        long fadeInTime = mHelper.getShowAnimationDuration() - 300;
                        fadeIn.setDuration(Math.max(fadeIn.getDuration(), fadeInTime));
                        fadeIn.setFillAfter(true);
                        startAnimation(fadeIn);
                    }
                    getViewTreeObserver().removeOnPreDrawListener(this);
                    return true;
                }
            });
        }
    }

    public void destroy() {
        mHelper = null;
    }

    public void handleAnimateDismiss() {
        if (mHelper != null && mHelper.isPopupFadeEnable()) {
            Animation fadeOut = AnimationUtils.loadAnimation(getContext(), R.anim.basepopup_fade_out);
            if (fadeOut != null) {
                long fadeDismissTime = mHelper.getExitAnimationDuration() - 300;
                fadeOut.setDuration(Math.max(fadeOut.getDuration(), fadeDismissTime));
                fadeOut.setFillAfter(true);
                startAnimation(fadeOut);
            }
        }
    }
}
