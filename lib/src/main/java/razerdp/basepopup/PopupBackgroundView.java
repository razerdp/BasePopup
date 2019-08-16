package razerdp.basepopup;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import razerdp.library.R;
import razerdp.util.PopupUiUtils;
import razerdp.util.PopupUtils;

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
        if (PopupUtils.isBackgroundInvalidated(mHelper.getPopupBackground())) {
            setVisibility(GONE);
            return;
        }
        this.mHelper = mHelper;
        setVisibility(VISIBLE);
        PopupUiUtils.setBackground(this, mHelper.getPopupBackground());
        if (mHelper.isPopupFadeEnable()) {
            Animation fadeIn = AnimationUtils.loadAnimation(getContext(), R.anim.basepopup_fade_in);
            if (fadeIn != null) {
                long fadeInTime = mHelper.showDuration - 200;
                fadeIn.setDuration(Math.max(fadeIn.getDuration(), fadeInTime));
                fadeIn.setFillAfter(true);
                startAnimation(fadeIn);
            }
        }
    }

    public void destroy() {
        mHelper = null;
    }

    public void handleAnimateDismiss() {
        if (mHelper != null && mHelper.isPopupFadeEnable()) {
            Animation fadeOut = AnimationUtils.loadAnimation(getContext(), R.anim.basepopup_fade_out);
            if (fadeOut != null) {
                long fadeDismissTime = mHelper.dismissDuration - 200;
                fadeOut.setDuration(Math.max(fadeOut.getDuration(), fadeDismissTime));
                fadeOut.setFillAfter(true);
                startAnimation(fadeOut);
            }
        }
    }

    public void update() {
        if (mHelper != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                setBackground(mHelper.getPopupBackground());
            } else {
                setBackgroundDrawable(mHelper.getPopupBackground());
            }
        }
    }
}
