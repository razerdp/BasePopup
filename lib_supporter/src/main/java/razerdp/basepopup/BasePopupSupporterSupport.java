package razerdp.basepopup;

import android.app.Activity;
import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.view.View;

/**
 * Created by 大灯泡 on 2019/5/13
 * <p>
 * Description：
 */
public class BasePopupSupporterSupport implements BasePopupSupporter {
    @Override
    public View findDecorView(BasePopupWindow basePopupWindow, Activity activity) {
        if (basePopupWindow.mAttached == null) return null;
        Object object = basePopupWindow.mAttached.get();
        if (object instanceof DialogFragment) {
            DialogFragment d = ((DialogFragment) object);
            if (d.getDialog() != null && d.getDialog().isShowing() && !d.isRemoving()) {
                return d.getView();
            }
        }
        if (object instanceof Dialog) {
            Dialog d = (Dialog) object;
            if (d.isShowing() && d.getWindow() != null) {
                return d.getWindow().getDecorView();
            }
        }
        return null;
    }

    @Override
    public BasePopupWindow attachLifeCycle(BasePopupWindow basePopupWindow, Object owner) {
        return basePopupWindow;
    }

    @Override
    public BasePopupWindow removeLifeCycle(BasePopupWindow basePopupWindow, Object owner) {
        return basePopupWindow;
    }
}
