package razerdp.interceptor;

import android.graphics.Point;
import android.view.View;
import android.widget.PopupWindow;

import razerdp.basepopup.BasePopupWindow;

/**
 * Created by 大灯泡 on 2018/11/20.
 */
public abstract class SimplePopupWindowEventInterceptor<P extends BasePopupWindow> implements PopupWindowEventInterceptor<P> {
    @Override
    public boolean onPreMeasurePopupView(P basePopupWindow, View contentView, int width, int height) {
        return false;
    }

    @Override
    public boolean onTryToShowPopup(P basePopupWindow, PopupWindow popupWindow, View anchorView, int gravity, int offsetX, int offsetY) {
        return false;
    }

    @Override
    public Point onCalculateOffset(P basePopupWindow, View anchorView, int offsetX, int offsetY) {
        return null;
    }

    @Override
    public void onCalculateOffsetResult(P basePopupWindow, View anchorView, Point calculatedOffset, int offsetX, int offsetY) {

    }

    @Override
    public int onKeyboardChangeResult(int keyboardHeight, boolean isKeyBoardVisible, int calculatedOffset) {
        return 0;
    }
}
