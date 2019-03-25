package razerdp.interceptor;

import android.graphics.Point;
import android.view.View;
import android.widget.PopupWindow;

import razerdp.basepopup.BasePopupWindow;

/**
 * Created by 大灯泡 on 2018/11/20.
 * <p>
 * BasePopupWindow各个事件的拦截器
 */
public interface PopupWindowEventInterceptor<P extends BasePopupWindow> {

    /**
     * 预测量PopupWindow回调，当执行预先测量时，将会回调到该方法，允许用户拦截BasePopup预测量过程，否则按照默认程序进行
     *
     * @param basePopupWindow basepopup对象
     * @param contentView     contentView
     * @param width           预设宽度
     * @param height          预设高度
     * @return 如果返回true，则意味着用户处理了该方法，则不默认执行BasePopupWindow的默认方法
     * @see BasePopupWindow#preMeasurePopupView(int, int)
     */
    boolean onPreMeasurePopupView(P basePopupWindow, View contentView, int width, int height);


    /**
     * 当调用{@link BasePopupWindow#showPopupWindow()}、{@link BasePopupWindow#showPopupWindow(View)}、{@link BasePopupWindow#showPopupWindow(int)}时，会回调该方法,允许用户拦截BasePopup的show过程，否则按照默认程序进行
     *
     * @param basePopupWindow basePopupWindow对象
     * @param popupWindow     popupwindow对象
     * @param anchorView      锚点View
     * @param gravity         popupwindow gravity
     * @param offsetX         水平偏移
     * @param offsetY         垂直偏移
     * @return 如果返回true，则意味着用户处理了该方法，则不默认执行BasePopupWindow的默认方法
     * @see BasePopupWindow#tryToShowPopup(View, boolean, boolean)
     */
    boolean onTryToShowPopup(P basePopupWindow, PopupWindow popupWindow, View anchorView, int gravity, int offsetX, int offsetY);


    /**
     * 允许用户拦截BasePopup默认的偏移计算过程
     * <br>
     * <strong>该方法已弃用，且不再回调</strong>
     *
     * @param basePopupWindow basePopupWindow对象
     * @param anchorView      锚点View
     * @param offsetX         水平偏移
     * @param offsetY         垂直偏移
     * @return 如果返回true，则意味着用户处理了该方法，则不默认执行BasePopupWindow的默认方法
     * @see BasePopupWindow#calculateOffset(View, boolean)
     * @deprecated
     */
    @Deprecated
    Point onCalculateOffset(P basePopupWindow, View anchorView, int offsetX, int offsetY);

    /**
     * 提供BasePopup偏移量计算结果回调，允许用户在回调中做弹出前最后一次修正
     * <br>
     * <strong>该方法已弃用，且不再回调</strong>
     *
     * @param basePopupWindow basePopupWindow对象
     * @param anchorView      锚点View
     * @param offsetX         水平偏移
     * @param offsetResult    已经经过默认计算的偏移量
     * @param offsetY         垂直偏移
     * @see BasePopupWindow#tryToShowPopup(View, boolean, boolean)
     * @deprecated
     */
    @Deprecated
    void onCalculateOffsetResult(P basePopupWindow, View anchorView, Point offsetResult, int offsetX, int offsetY);


    /**
     * 提供BasePopup针对键盘适配的偏移计算结果回调，允许用户在该回调中做偏移修正
     *
     * @param keyboardHeight    键盘高度
     * @param isKeyBoardVisible 键盘是否已经弹出
     * @param offsetResult      默认计算的偏移量
     * @return 返回新的偏移量，返回0则不进行修正。
     */
    int onKeyboardChangeResult(int keyboardHeight, boolean isKeyBoardVisible, int offsetResult);

}
