package razerdp.basepopup;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import razerdp.library.R;

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
        init();
    }

    public static PopupBackgroundView creaete(Context context, BasePopupHelper helper) {
        PopupBackgroundView view = new PopupBackgroundView(context);
        view.mHelper = helper;
        return view;
    }

    private void init() {
        if (mHelper == null || mHelper.getPopupBackgroundColor() == Color.TRANSPARENT) {
            setVisibility(GONE);
            return;
        }
        setVisibility(VISIBLE);
        setBackgroundColor(mHelper.getPopupBackgroundColor());
        if (mHelper.isPopupFadeEnable()) {
            final Animation fadeIn = AnimationUtils.loadAnimation(getContext(), R.anim.basepopup_fade_in);
            getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    startAnimation(fadeIn);
                    getViewTreeObserver().removeOnPreDrawListener(this);
                    return true;
                }
            });
        }
    }

    public void destroy() {
        clearAnimation();
    }

    public void handleAnimateDismiss() {
        if (mHelper.isPopupFadeEnable()) {
            Animation fadeOut = AnimationUtils.loadAnimation(getContext(), R.anim.basepopup_fade_out);
            startAnimation(fadeOut);
        }
    }
}
