package razerdp.basepopup;

import android.os.Build;
import android.view.Gravity;
import android.view.View;

/**
 * Created by 大灯泡 on 2017/1/12.
 * <p>
 * 与basePopupWindow强引用(或者说与PopupController强引用)
 */

class PopupWindowProxy extends BasePopupWindowProxy {
    private static final String TAG = "PopupWindowProxy";

    public PopupWindowProxy(View contentView, int width, int height, PopupController mController) {
        super(contentView, width, height, mController);
    }

    /**
     * fix showAsDropDown when android api ver is over N
     * <p>
     * https://code.google.com/p/android/issues/detail?id=221001
     *
     * @param anchor
     * @param xoff
     * @param yoff
     * @param gravity
     */
    public void showAsDropDownProxy(View anchor, int xoff, int yoff, int gravity) {
        PopupCompatManager.showAsDropDown(this, anchor, xoff, yoff, gravity);
    }

    public void showAsDropDownProxy(View anchor, int xoff, int yoff) {
        PopupCompatManager.showAsDropDown(this, anchor, xoff, yoff, Gravity.TOP | Gravity.START);
    }

    public void showAtLocationProxy(View parent, int gravity, int x, int y) {
        PopupCompatManager.showAtLocation(this, parent, gravity, x, y);
    }


    @Override
    public void callSuperShowAsDropDown(View anchor, int xoff, int yoff, int gravity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            super.showAsDropDown(anchor, xoff, yoff, gravity);
        } else {
            super.showAsDropDown(anchor, xoff, yoff);
        }
    }

    @Override
    public void callSuperShowAtLocation(View parent, int gravity, int x, int y) {
        super.showAtLocation(parent, gravity, x, y);
    }

    @Override
    public boolean callSuperIsShowing() {
        return super.isShowing();
    }
}
