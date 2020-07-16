package razerdp.basepopup;

import android.content.Context;
import android.content.res.Resources;
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
    }

    public void destroy() {
        mHelper = null;
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
